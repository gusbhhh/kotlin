/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test

class OperatorTest {
    @Test
    fun `contains operator is correctly aligned`() {
        assertMessage(
            """
      fun main() {
        assert("Name" in listOf("Hello", "World"))
      }""",
            """
      Assertion failed
      assert("Name" in listOf("Hello", "World"))
                    |  |
                    |  [Hello, World]
                    false
      """.trimIndent(),
        )
    }

    @Test
    fun `contains function is correctly aligned`() {
        assertMessage(
            """
      fun main() {
        assert(listOf("Hello", "World").contains("Name"))
      }""",
            """
      Assertion failed
      assert(listOf("Hello", "World").contains("Name"))
             |                        |
             |                        false
             [Hello, World]
      """.trimIndent(),
        )
    }

    @Test
    fun `negative contains operator is correctly aligned`() {
        assertMessage(
            """
      fun main() {
        assert("Hello" !in listOf("Hello", "World"))
      }""",
            """
      Assertion failed
      assert("Hello" !in listOf("Hello", "World"))
                     |   |
                     |   [Hello, World]
                     false
      """.trimIndent(),
        )
    }

    @Test
    fun `negative contains function is correctly aligned`() {
        assertMessage(
            """
      fun main() {
        assert(!listOf("Hello", "World").contains("Hello"))
      }""",
            """
      Assertion failed
      assert(!listOf("Hello", "World").contains("Hello"))
             ||                        |
             ||                        true
             |[Hello, World]
             false
      """.trimIndent(),
        )
    }
}
