/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test
import kotlin.test.assertEquals

class ArithmeticExpressionTest {
    @Test
    fun `assertion with inline addition`() {
        val actual = executeMainAssertion("assert(1 + 1 == 4)")
        assertEquals(
            """
      Assertion failed
      assert(1 + 1 == 4)
               |   |
               |   false
               2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline subtraction`() {
        val actual = executeMainAssertion("assert(3 - 1 == 4)")
        assertEquals(
            """
      Assertion failed
      assert(3 - 1 == 4)
               |   |
               |   false
               2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline multiplication`() {
        val actual = executeMainAssertion("assert(1 * 2 == 4)")
        assertEquals(
            """
      Assertion failed
      assert(1 * 2 == 4)
               |   |
               |   false
               2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline division`() {
        val actual = executeMainAssertion("assert(2 / 1 == 4)")
        assertEquals(
            """
      Assertion failed
      assert(2 / 1 == 4)
               |   |
               |   false
               2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline prefix increment`() {
        val actual = executeMainAssertion(
            """
      var i = 1
      assert(++i == 4)
      """.trimIndent(),
        )
        assertEquals(
            """
      Assertion failed
      assert(++i == 4)
             |   |
             |   false
             2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline postfix increment`() {
        val actual = executeMainAssertion(
            """
      var i = 1
      assert(i++ == 4)
      """.trimIndent(),
        )
        assertEquals(
            """
      Assertion failed
      assert(i++ == 4)
             |   |
             |   false
             1
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline prefix decrement`() {
        val actual = executeMainAssertion(
            """
      var i = 3
      assert(--i == 4)
      """.trimIndent(),
        )
        assertEquals(
            """
      Assertion failed
      assert(--i == 4)
             |   |
             |   false
             2
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `assertion with inline postfix decrement`() {
        val actual = executeMainAssertion(
            """
      var i = 3
      assert(i-- == 4)
      """.trimIndent(),
        )
        assertEquals(
            """
      Assertion failed
      assert(i-- == 4)
             |   |
             |   false
             3
      """.trimIndent(),
            actual,
        )
    }
}
