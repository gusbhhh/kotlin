/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import org.jetbrains.kotlin.name.FqName
import kotlin.test.Test

class LamdaTest {
  @Test
  fun `list operations assert`() {
    assertMessage(
      """
      fun main() {
        val list = listOf("Jane", "John")
        assert(list.map { "Doe, ${'$'}it" }.any { it == "Scott, Michael" })
      }""",
      """
      Assertion failed
      assert(list.map { "Doe, ${'$'}it" }.any { it == "Scott, Michael" })
             |    |                  |
             |    |                  false
             |    [Doe, Jane, Doe, John]
             [Jane, John]
      """.trimIndent(),
    )
  }

  @Test
  fun `list operations require`() {
    assertMessage(
        """
      fun main() {
        val list = listOf("Jane", "John")
        require(
            value = list
                .map { "Doe, ${'$'}it" }
                .any { it == "Scott, Michael" }
        )
      }""",
        """
      Assertion failed
      require(
          value = list
                  |
                  [Jane, John]
              .map { "Doe, ${'$'}it" }
               |
               [Doe, Jane, Doe, John]
              .any { it == "Scott, Michael" }
               |
               false
      )
      """.trimIndent(),
        PowerAssertCompilerPluginRegistrar(setOf(FqName("kotlin.require"))),
    )
  }
}
