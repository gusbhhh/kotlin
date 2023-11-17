/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.name.FqName
import java.io.OutputStream
import java.lang.reflect.InvocationTargetException
import kotlin.test.assertEquals
import kotlin.test.fail

private val DEFAULT_COMPILER_PLUGIN_REGISTRARS = arrayOf(
  PowerAssertCompilerPluginRegistrar(setOf(FqName("kotlin.assert"))),
)

fun compile(
  list: List<SourceFile>,
  vararg compilerPluginRegistrars: CompilerPluginRegistrar = DEFAULT_COMPILER_PLUGIN_REGISTRARS,
): KotlinCompilation.Result {
  return KotlinCompilation().apply {
    sources = list
    messageOutputStream = object : OutputStream() {
      override fun write(b: Int) {
        // black hole all writes
      }

      override fun write(b: ByteArray, off: Int, len: Int) {
        // black hole all writes
      }
    }
    this.compilerPluginRegistrars = compilerPluginRegistrars.toList()
    inheritClassPath = true
  }.compile()
}

fun executeAssertion(
  @Language("kotlin") source: String,
  vararg compilerPluginRegistrars: CompilerPluginRegistrar = DEFAULT_COMPILER_PLUGIN_REGISTRARS,
): String {
  val result = compile(
    listOf(SourceFile.kotlin("main.kt", source, trimIndent = false)),
    *compilerPluginRegistrars,
  )
  assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode, result.messages)

  val kClazz = result.classLoader.loadClass("MainKt")
  val main = kClazz.declaredMethods.single { it.name == "main" && it.parameterCount == 0 }
  try {
    try {
      main.invoke(null)
    } catch (t: InvocationTargetException) {
      throw t.cause!!
    }
    fail("should have thrown assertion")
  } catch (t: Throwable) {
    return t.message ?: ""
  }
}

fun executeMainAssertion(mainBody: String) = executeAssertion(
  """
fun main() {
  $mainBody
}
""",
)

fun assertMessage(
  @Language("kotlin") source: String,
  message: String,
  vararg compilerPluginRegistrars: CompilerPluginRegistrar = DEFAULT_COMPILER_PLUGIN_REGISTRARS,
) {
  val actual = executeAssertion(source, *compilerPluginRegistrars)
  assertEquals(message, actual)
}
