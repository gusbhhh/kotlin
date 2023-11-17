/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test

class VarargTest {
    @Test
    fun `implicit array of vararg parameters is excluded from diagram`() {
        assertMessage(
            """
      fun main() {
        var i = 0
        assert(listOf("a", "b", "c") == listOf(i++, i++, i++))
      }""",
            """
      Assertion failed
      assert(listOf("a", "b", "c") == listOf(i++, i++, i++))
             |                     |  |      |    |    |
             |                     |  |      |    |    2
             |                     |  |      |    1
             |                     |  |      0
             |                     |  [0, 1, 2]
             |                     false
             [a, b, c]
      """.trimIndent(),
        )
    }
}
