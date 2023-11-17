/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.test.framework.services.libraries

import org.jetbrains.kotlin.analysis.test.framework.services.libraries.CompilerExecutor.CompilerKind
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.arguments.cliArgument
import org.jetbrains.kotlin.config.JvmTarget
import org.jetbrains.kotlin.platform.isCommon
import org.jetbrains.kotlin.platform.isJs
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.test.directives.JvmEnvironmentConfigurationDirectives
import org.jetbrains.kotlin.test.directives.LanguageSettingsDirectives
import org.jetbrains.kotlin.test.directives.model.SimpleDirectivesContainer
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.TestServices
import org.jetbrains.kotlin.test.services.sourceFileProvider
import org.jetbrains.kotlin.test.services.standardLibrariesPathProvider
import org.jetbrains.kotlin.test.util.KtTestUtil
import java.io.ByteArrayInputStream
import java.nio.file.Path
import java.util.jar.Attributes
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.jar.Manifest
import kotlin.io.path.createFile
import kotlin.io.path.div
import kotlin.io.path.outputStream
import kotlin.io.path.writeText

object TestModuleCompiler {
    fun compileTestModuleToLibrary(module: TestModule, testServices: TestServices): Path {
        val tmpDir = KtTestUtil.tmpDir("testSourcesToCompile").toPath()
        for (testFile in module.files) {
            val text = testServices.sourceFileProvider.getContentOfSourceFile(testFile)
            val tmpSourceFile = (tmpDir / testFile.name).createFile()
            tmpSourceFile.writeText(text)
        }

        return CompilerExecutor.compileLibrary(
            module.compilerKind,
            tmpDir,
            buildCompilerOptions(module, testServices),
            compilationErrorExpected = Directives.COMPILATION_ERRORS in module.directives
        )
    }

    fun compileTestModuleToLibrarySources(module: TestModule, testServices: TestServices): Path {
        val tmpDir = KtTestUtil.tmpDir("testSourcesToCompile").toPath()
        val librarySourcesPath = tmpDir / "library-sources.jar"
        val manifest = Manifest().apply { mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0" }
        JarOutputStream(librarySourcesPath.outputStream(), manifest).use { jarOutputStream ->
            for (testFile in module.files) {
                val text = testServices.sourceFileProvider.getContentOfSourceFile(testFile)
                addFileToJar(testFile.relativePath, text, jarOutputStream)
            }
        }
        return librarySourcesPath
    }

    private fun buildCompilerOptions(module: TestModule, testServices: TestServices): List<String> = buildList {
        buildCommonCompilerOptions(module)
        buildPlatformOptions(module, testServices)
    }

    private fun MutableList<String>.buildCommonCompilerOptions(module: TestModule) {
        module.directives[LanguageSettingsDirectives.API_VERSION].firstOrNull()?.let { apiVersion ->
            addAll(listOf(CommonCompilerArguments::apiVersion.cliArgument, apiVersion.versionString))
        }

        module.directives[LanguageSettingsDirectives.LANGUAGE].firstOrNull()?.let {
            add("-XXLanguage:$it")
        }

        if (LanguageSettingsDirectives.ALLOW_KOTLIN_PACKAGE in module.directives) {
            add(CommonCompilerArguments::allowKotlinPackage.cliArgument)
        }

        addAll(module.directives[Directives.COMPILER_ARGUMENTS])
    }

    private fun MutableList<String>.buildPlatformOptions(module: TestModule, testServices: TestServices) {
        when (module.compilerKind) {
            CompilerKind.JVM -> addJvmDirectives(module)
            CompilerKind.JS -> addJsDirectives(testServices)
        }
    }

    private fun MutableList<String>.addJvmDirectives(module: TestModule) {
        module.directives[JvmEnvironmentConfigurationDirectives.JVM_TARGET].firstOrNull()?.let { jvmTarget ->
            addAll(listOf(K2JVMCompilerArguments::jvmTarget.cliArgument, jvmTarget.description))

            val jdkHome = when {
                jvmTarget <= JvmTarget.JVM_1_8 -> KtTestUtil.getJdk8Home()
                jvmTarget <= JvmTarget.JVM_11 -> KtTestUtil.getJdk11Home()
                jvmTarget <= JvmTarget.JVM_17 -> KtTestUtil.getJdk17Home()
                jvmTarget <= JvmTarget.JVM_21 -> KtTestUtil.getJdk21Home()
                else -> error("JDK for $jvmTarget is not found")
            }

            addAll(listOf(K2JVMCompilerArguments::jdkHome.cliArgument, jdkHome.toString()))
        }
    }

    private fun MutableList<String>.addJsDirectives(testServices: TestServices) {
        // IR compiler no longer adds the stdlib path by default
        add("-libraries"); add(testServices.standardLibrariesPathProvider.fullJsStdlib().absolutePath)
    }

    private fun addFileToJar(path: String, text: String, jarOutputStream: JarOutputStream) {
        jarOutputStream.putNextEntry(JarEntry(path))
        ByteArrayInputStream(text.toByteArray()).copyTo(jarOutputStream)
        jarOutputStream.closeEntry()
    }

    private val TestModule.compilerKind: CompilerKind
        get() {
            return when {
                targetPlatform.isJvm() -> CompilerKind.JVM
                targetPlatform.isJs() -> CompilerKind.JS
                else -> error("Can't compile a mock library for the platform: $targetPlatform")
            }
        }

    object Directives : SimpleDirectivesContainer() {
        val COMPILER_ARGUMENTS by stringDirective("List of additional compiler arguments")
        val COMPILATION_ERRORS by directive("Is compilation errors expected in the file")
    }
}