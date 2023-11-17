/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.name.FqName
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class DebugFunctionTest {
    @Test
    fun `debug function transformation`() {
        val actual = executeMainDebug(
            """
      dbg(1 + 2 + 3)
      """.trimIndent(),
        )
        assertEquals(
            """
      dbg(1 + 2 + 3)
            |   |
            |   6
            3
      """.trimIndent(),
            actual.trim(),
        )
    }

    @Test
    fun `debug function transformation with message`() {
        val actual = executeMainDebug(
            """
      dbg(1 + 2 + 3, "Message:")
      """.trimIndent(),
        )
        assertEquals(
            """
      Message:
      dbg(1 + 2 + 3, "Message:")
            |   |
            |   6
            3
      """.trimIndent(),
            actual.trim(),
        )
    }
}

private fun executeMainDebug(mainBody: String): String {
    val file = SourceFile.kotlin(
        name = "main.kt",
        contents = """
fun <T> dbg(value: T): T = value

fun <T> dbg(value: T, msg: String): T {
    throw RuntimeException("result:"+msg)
    return value
}

fun main() {
  $mainBody
}
""",
        trimIndent = false,
    )

    val result = compile(listOf(file), PowerAssertCompilerPluginRegistrar(setOf(FqName("dbg"))))
    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)

    val kClazz = result.classLoader.loadClass("MainKt")
    val main = kClazz.declaredMethods.single { it.name == "main" && it.parameterCount == 0 }
    return getMainResult(main)
}

fun getMainResult(main: Method): String {
    try {
        main.invoke(null)
        fail("main did not throw expected exception")
    } catch (t: InvocationTargetException) {
        with(t.cause) {
            if (this is RuntimeException && message != null && message!!.startsWith("result:")) {
                return message!!.substringAfter("result:")
            }
        }
        throw t.cause!!
    }
}
