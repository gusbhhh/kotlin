/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test
import kotlin.test.assertEquals

class CastExpressionTest {
  @Test
  fun `instance check is correctly aligned`() {
    val actual = executeMainAssertion("""assert(null is String)""")
    assertEquals(
      """
      Assertion failed
      assert(null is String)
                  |
                  false
      """.trimIndent(),
      actual,
    )
  }

  @Test
  fun `negative instance check is correctly aligned`() {
    val actual = executeMainAssertion("""assert("Hello, world!" !is String)""")
    assertEquals(
      """
      Assertion failed
      assert("Hello, world!" !is String)
                             |
                             false
      """.trimIndent(),
      actual,
    )
  }

  @Test
  fun `smart casts do not duplicate output`() {
    val actual = executeMainAssertion(
      """
      val greeting: Any = "hello"
      assert(greeting is String && greeting.length == 2)
      """.trimIndent(),
    )
    assertEquals(
      """
      Assertion failed
      assert(greeting is String && greeting.length == 2)
             |        |            |        |      |
             |        |            |        |      false
             |        |            |        5
             |        |            hello
             |        true
             hello
      """.trimIndent(),
      actual,
    )
  }
}
