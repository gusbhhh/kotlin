/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.analysis.test.framework.services.libraries

import org.jetbrains.kotlin.analysis.test.framework.utils.SkipTestException
import org.jetbrains.kotlin.test.MockLibraryUtil
import java.nio.file.Path
import kotlin.io.path.*

internal object CompilerExecutor {
    fun compileLibrary(compilerKind: CompilerKind, sourcesPath: Path, options: List<String>, compilationErrorExpected: Boolean): Path {
        val library = try {
            compile(compilerKind, sourcesPath, options)
        } catch (e: Throwable) {
            if (!compilationErrorExpected) {
                throw IllegalStateException("Unexpected compilation error while compiling library", e)
            }
            null
        }

        if (library?.exists() == true && compilationErrorExpected) {
            error("Compilation error expected but, code was compiled successfully")
        }
        if (library == null || library.notExists()) {
            throw LibraryWasNotCompiledDueToExpectedCompilationError()
        }
        return library
    }

    private fun compile(compilerKind: CompilerKind, sourcesPath: Path, options: List<String>): Path {
        val sourceFiles = sourcesPath.toFile().walkBottomUp()
        val library = when (compilerKind) {
            CompilerKind.JVM -> sourcesPath / "library.jar"
            CompilerKind.JS -> sourcesPath / "library.klib"
        }
        when (compilerKind) {
            CompilerKind.JVM -> {
                val commands = buildList {
                    sourceFiles.mapTo(this) { it.absolutePath }
                    addAll(options)
                    add("-d")
                    add(library.absolutePathString())
                    add("-XXLanguage:-SkipStandaloneScriptsInSourceRoots")
                }
                MockLibraryUtil.runJvmCompiler(commands)
            }
            CompilerKind.JS -> {
                val commands = buildList {
                    add("-meta-info")
                    add("-ir-output-name"); add("library")
                    add("-ir-output-dir"); add(library.parent.absolutePathString())
                    add("-Xir-produce-klib-file")
                    sourceFiles.mapTo(this) { it.absolutePath }
                    addAll(options)
                }
                MockLibraryUtil.runJsCompiler(commands)
            }
        }

        return library
    }

    enum class CompilerKind {
        JVM, JS
    }
}

class LibraryWasNotCompiledDueToExpectedCompilationError : SkipTestException()
