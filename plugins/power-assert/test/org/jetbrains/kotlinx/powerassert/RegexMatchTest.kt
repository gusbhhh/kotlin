/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test
import kotlin.test.assertEquals

class RegexMatchTest {
    @Test
    fun `regex matches`() {
        val actual = executeMainAssertion("""assert("Hello, World".matches("[A-Za-z]+".toRegex()))""")
        assertEquals(
            """
      Assertion failed
      assert("Hello, World".matches("[A-Za-z]+".toRegex()))
                            |                   |
                            |                   [A-Za-z]+
                            false
      """.trimIndent(),
            actual,
        )
    }

    @Test
    fun `infix regex matches`() {
        val actual = executeMainAssertion("""assert("Hello, World" matches "[A-Za-z]+".toRegex())""")
        assertEquals(
            """
      Assertion failed
      assert("Hello, World" matches "[A-Za-z]+".toRegex())
                            |                   |
                            |                   [A-Za-z]+
                            false
      """.trimIndent(),
            actual,
        )
    }
}
