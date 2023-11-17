/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import kotlin.test.Test

class ObjectLiteralTest {
  @Test
  fun `internals of object literal should not be separated`() {
    assertMessage(
      """
      fun main() {
        assert(object { override fun toString() = "ANONYMOUS" }.toString() == "toString()")
      }""",
      """
      Assertion failed
      assert(object { override fun toString() = "ANONYMOUS" }.toString() == "toString()")
             |                                                |          |
             |                                                |          false
             |                                                ANONYMOUS
             ANONYMOUS
      """.trimIndent(),
    )
  }
}
