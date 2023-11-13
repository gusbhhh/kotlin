/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.mpp.smoke

import org.gradle.api.logging.LogLevel
import org.gradle.testkit.runner.BuildResult
import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.testbase.*
import org.jetbrains.kotlin.gradle.util.replaceFirst
import org.junit.jupiter.api.DisplayName
import java.nio.file.Path
import kotlin.io.path.appendText

@DisplayName("Basic incremental scenarios with KMP - K2")
@MppGradlePluginTests
open class BasicIncrementalCompilationIT : KGPBaseTest() {
    override val defaultBuildOptions: BuildOptions
        get() = super.defaultBuildOptions.copyEnsuringK2()

    @DisplayName("Base test case - local change, local recompilation")
    @GradleTest
    fun testStrictlyLocalChange(gradleVersion: GradleVersion): Unit = withProject(gradleVersion) {
        /**
         * Step 0: clean build
         */
        build("assemble")

        fun testCase(
            incrementalPath: Path? = null, executedTasks: Set<String>, assertions: BuildResult.() -> Unit = {},
        ) {
            build("assemble", buildOptions = defaultBuildOptions.copy(logLevel = LogLevel.DEBUG)) {
                assertSuccessOrUTD(
                    executedTasks = executedTasks
                )
                incrementalPath?.let {
                    assertIncrementalCompilation(listOf(it).relativizeTo(projectPath))
                }
                assertions()
            }
        }

        /**
         * Step 1: touch app:common, no abi change
         */

        testCase(
            incrementalPath = touchAndGet("app", "commonMain", "PlainPublicClass.kt"),
            executedTasks = setOf(
                ":app:compileKotlinJvm",
                ":app:compileKotlinJs",
                ":app:compileKotlinNative"
            )
        )

        /**
         * Step 2: touch app:jvm, no abi change
         */

        val appJvmClassKt = touchAndGet("app", "jvmMain", "PlainPublicClassJvm.kt")
        testCase(
            incrementalPath = null, //TODO: it just doesn't print "Incremental compilation completed", why?
            executedTasks = setOf(":app:compileKotlinJvm")
        ) {
            assertCompiledKotlinSources(listOf(appJvmClassKt).relativizeTo(projectPath), output)
        }

        /**
         * Step 3: touch app:js, no abi change
         */

        testCase(
            incrementalPath = touchAndGet("app", "jsMain", "PlainPublicClassJs.kt"),
            executedTasks = setOf(":app:compileKotlinJs"),
        )

        /**
         * Step 4: touch app:native, no abi change
         */

        touchAndGet("app", "nativeMain", "PlainPublicClassNative.kt")
        testCase(
            incrementalPath = null, // no native incremental compilation - see KT-62824
            executedTasks = setOf(":app:compileKotlinNative"),
        )

        /**
         * Step 5: touch lib:common, no abi change
         */

        testCase(
            incrementalPath = touchAndGet("lib", "commonMain", "CommonUtil.kt"),
            executedTasks = mainCompileTasks // TODO: why so suboptimal
        )

        /**
         * Step 6: touch lib:jvm, no abi change
         */

        val libJvmUtilKt = touchAndGet("lib", "jvmMain", "libJvmPlatformUtil.kt")
        testCase(
            incrementalPath = null, //TODO: it just doesn't print "Incremental compilation completed", why?
            executedTasks = setOf(":app:compileKotlinJvm", ":lib:compileKotlinJvm"),
        ) {
            assertCompiledKotlinSources(listOf(libJvmUtilKt).relativizeTo(projectPath), output)
        }

        /**
         * Step 7: touch lib:js, no abi change
         */

        testCase(
            incrementalPath = touchAndGet("lib", "jsMain", "libJsPlatformUtil.kt"),
            executedTasks = setOf(":app:compileKotlinJs", ":lib:compileKotlinJs"),
        )

        /**
         * Step 8: touch lib:native, no abi change
         */

        touchAndGet("lib", "nativeMain", "libNativePlatformUtil.kt")
        testCase(
            incrementalPath = null,
            executedTasks = setOf(":app:compileKotlinNative", ":lib:compileKotlinNative"),
        )
    }

    //TODO review fun names / display names

    @DisplayName("KMP tests are rebuilt appropriately")
    @GradleTest
    fun testAffectingTestDependencies(gradleVersion: GradleVersion): Unit = withProject(gradleVersion) {
        build("build")
        /**
         * Touch file in commonMain, recompile jvmTest / linuxX64Test / jsTest / …
         */
        val utilKtPath = subProject("lib").kotlinSourcesDir("commonMain").resolve("CommonUtil.kt")
        //TODO what's up with native compiler there
        //utilKtPath.replaceFirst("fun multiplyByTwo(n: Int): Int", "fun <T> multiplyByTwo(n: T): T")
        utilKtPath.replaceFirst("fun multiplyByTwo(n: Int): Int", "fun multiplyByTwo(n: Int, unused: Int = 42): Int")

        build("build", buildOptions = defaultBuildOptions.copy(logLevel = LogLevel.DEBUG)) {
            assertTasksExecuted(
                ":app:compileTestKotlinJvm",
                ":lib:compileTestKotlinJvm",

                ":app:compileTestKotlinNative",
                ":lib:compileTestKotlinNative",

                ":app:jvmTest",
                ":lib:jvmTest",
                ":lib:nativeTest",
            )
            assertTasksUpToDate(
                ":app:nativeTest" //TODO discrepancy between jvm in native in this case
            )
            assertTasksNoSource(
                ":app:jsTest",//TODO set up js test properly?
                ":lib:jsTest"
            )
            assertIncrementalCompilation(
                listOf(
                    utilKtPath,
                    subProject("lib").kotlinSourcesDir("jvmTest").resolve("AdvancedTest.kt")
                ).relativizeTo(projectPath)
            )
        }

        //TODO touch platform stuff, rebuild platform stuff
    }

    private fun withProject(gradleVersion: GradleVersion, test: TestProject.() -> Unit): Unit {
        nativeProject(
            "generic-kmp-app-plus-lib-with-tests",
            gradleVersion,
            configureSubProjects = true,
            test = test
        )
    }

    private fun TestProject.touchAndGet(subproject: String, srcDir: String, name: String): Path {
        val path = subProject(subproject).kotlinSourcesDir(srcDir).resolve(name)
        path.appendText("private val nothingMuch = 24")
        return path
    }

    private fun BuildResult.assertSuccessOrUTD(executedTasks: Set<String>, allTasks: Set<String> = mainCompileTasks) {
        assertTasksExecuted(executedTasks)
        assertTasksUpToDate(allTasks - executedTasks)
    }

    companion object {
        private val mainCompileTasks = setOf(
            ":app:compileKotlinJvm",
            ":app:compileKotlinJs",
            ":app:compileKotlinNative",
            ":lib:compileKotlinJvm",
            ":lib:compileKotlinJs",
            ":lib:compileKotlinNative"
        )
    }
}

@DisplayName("Basic incremental scenarios with Kotlin Multiplatform - K1")
class BasicIncrementalCompilationK1IT : BasicIncrementalCompilationIT() {
    override val defaultBuildOptions: BuildOptions
        get() = super.defaultBuildOptions.copyEnsuringK1()
}