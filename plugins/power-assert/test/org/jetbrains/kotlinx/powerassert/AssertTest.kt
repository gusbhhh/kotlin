/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlinx.powerassert

import org.jetbrains.kotlin.name.FqName
import kotlin.test.Test

class AssertTest {
  @Test
  fun memberFunctions() {
    assertMessage(
      """
fun main() {
  val hello = "Hello"
  assert(hello.length == "World".substring(1, 4).length)
}""",
      """
Assertion failed
assert(hello.length == "World".substring(1, 4).length)
       |     |      |          |               |
       |     |      |          |               3
       |     |      |          orl
       |     |      false
       |     5
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun transformations() {
    assertMessage(
      """
fun main() {
  val hello = listOf("Hello", "World")
  assert(hello.reversed() == emptyList<String>())
}""",
      """
Assertion failed
assert(hello.reversed() == emptyList<String>())
       |     |          |  |
       |     |          |  []
       |     |          false
       |     [World, Hello]
       [Hello, World]
      """.trimIndent(),
    )
  }

  @Test
  fun customMessage() {
    assertMessage(
      """
fun main() {
  assert(1 == 2) { "Not equal" }
}""",
      """
Not equal
assert(1 == 2) { "Not equal" }
         |
         false
      """.trimIndent(),
    )
  }

  @Test
  fun customLocalVariableMessage() {
    assertMessage(
      """
fun main() {
  val lambda = { "Not equal" }
  assert(1 == 2, lambda) 
}""",
      """
      Not equal
      assert(1 == 2, lambda)
               |
               false
      """.trimIndent(),
    )
  }

  @Test
  fun booleanExpressionsShortCircuit() {
    assertMessage(
      """
fun main() {
  val text: String? = null
  assert(text != null && text.length == 1)
}""",
      """
Assertion failed
assert(text != null && text.length == 1)
       |    |
       |    false
       null
      """.trimIndent(),
    )
  }

  @Test
  fun booleanAnd() {
    assertMessage(
      """
fun main() {
  val text: String? = "Hello"
  assert(text != null && text.length == 5 && text.toLowerCase() == text)
}""",
      """
Assertion failed
assert(text != null && text.length == 5 && text.toLowerCase() == text)
       |    |          |    |      |       |    |             |  |
       |    |          |    |      |       |    |             |  Hello
       |    |          |    |      |       |    |             false
       |    |          |    |      |       |    hello
       |    |          |    |      |       Hello
       |    |          |    |      true
       |    |          |    5
       |    |          Hello
       |    true
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun booleanOr() {
    assertMessage(
      """
fun main() {
  val text: String? = "Hello"
  assert(text == null || text.length == 1 || text.toLowerCase() == text)
}""",
      """
Assertion failed
assert(text == null || text.length == 1 || text.toLowerCase() == text)
       |    |          |    |      |       |    |             |  |
       |    |          |    |      |       |    |             |  Hello
       |    |          |    |      |       |    |             false
       |    |          |    |      |       |    hello
       |    |          |    |      |       Hello
       |    |          |    |      false
       |    |          |    5
       |    |          Hello
       |    false
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun booleanMixAndFirst() {
    assertMessage(
      """
fun main() {
  val text: String? = "Hello"
  assert(text != null && (text.length == 1 || text.toLowerCase() == text))
}""",
      """
Assertion failed
assert(text != null && (text.length == 1 || text.toLowerCase() == text))
       |    |           |    |      |       |    |             |  |
       |    |           |    |      |       |    |             |  Hello
       |    |           |    |      |       |    |             false
       |    |           |    |      |       |    hello
       |    |           |    |      |       Hello
       |    |           |    |      false
       |    |           |    5
       |    |           Hello
       |    true
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun booleanMixAndLast() {
    assertMessage(
      """
fun main() {
  val text = "Hello"
  assert((text.length == 1 || text.toLowerCase() == text) && text.length == 1)
}""",
      """
Assertion failed
assert((text.length == 1 || text.toLowerCase() == text) && text.length == 1)
        |    |      |       |    |             |  |
        |    |      |       |    |             |  Hello
        |    |      |       |    |             false
        |    |      |       |    hello
        |    |      |       Hello
        |    |      false
        |    5
        Hello
      """.trimIndent(),
    )
  }

  @Test
  fun booleanMixOrFirst() {
    assertMessage(
      """
fun main() {
  val text: String? = "Hello"
  assert(text == null || (text.length == 5 && text.toLowerCase() == text))
}""",
      """
Assertion failed
assert(text == null || (text.length == 5 && text.toLowerCase() == text))
       |    |           |    |      |       |    |             |  |
       |    |           |    |      |       |    |             |  Hello
       |    |           |    |      |       |    |             false
       |    |           |    |      |       |    hello
       |    |           |    |      |       Hello
       |    |           |    |      true
       |    |           |    5
       |    |           Hello
       |    false
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun booleanMixOrLast() {
    assertMessage(
      """
fun main() {
  val text = "Hello"
  assert((text.length == 5 && text.toLowerCase() == text) || text.length == 1)
}""",
      """
Assertion failed
assert((text.length == 5 && text.toLowerCase() == text) || text.length == 1)
        |    |      |       |    |             |  |        |    |      |
        |    |      |       |    |             |  |        |    |      false
        |    |      |       |    |             |  |        |    5
        |    |      |       |    |             |  |        Hello
        |    |      |       |    |             |  Hello
        |    |      |       |    |             false
        |    |      |       |    hello
        |    |      |       Hello
        |    |      true
        |    5
        Hello
      """.trimIndent(),
    )
  }

  @Test
  fun conditionalAccess() {
    assertMessage(
      """
fun main() {
  val text: String? = "Hello"
  assert(text?.length?.minus(2) == 1)
}""",
      """
Assertion failed
assert(text?.length?.minus(2) == 1)
       |     |       |        |
       |     |       |        false
       |     |       3
       |     5
       Hello
      """.trimIndent(),
    )
  }

  @Test
  fun infixFunctions() {
    assertMessage(
      """
fun main() {
  assert(1.shl(1) == 4)
}""",
      """
      Assertion failed
      assert(1.shl(1) == 4)
               |      |
               |      false
               2
      """.trimIndent(),
    )

    assertMessage(
      """
fun main() {
  assert(1 shl 1 == 4)
}""",
      """
Assertion failed
assert(1 shl 1 == 4)
         |     |
         |     false
         2
      """.trimIndent(),
    )
  }

  @Test
  fun multiline() {
    assertMessage(
      """fun main() {
  val text: String? = "Hello"
  assert(
    text
        == null ||
        (
            text.length == 5 &&
                text.toLowerCase() == text
            )
  )
}""",
      """
Assertion failed
assert(
  text
  |
  Hello
      == null ||
      |
      false
      (
          text.length == 5 &&
          |    |      |
          |    |      true
          |    5
          Hello
              text.toLowerCase() == text
              |    |             |  |
              |    |             |  Hello
              |    |             false
              |    hello
              Hello
          )
)
      """.trimIndent(),
    )
  }

  @Test
  fun assertTrueCustomMessage() {
    assertMessage(
        """
import kotlin.test.assertTrue

fun main() {
  val text: String? = "Hello"
  assertTrue(1 == 2, message = "${"$"}text, the world is broken")
}""",
        """
Hello, the world is broken
assertTrue(1 == 2, message = "${"$"}text, the world is broken")
             |
             false
      """.trimIndent(),
        PowerAssertCompilerPluginRegistrar(setOf(FqName("kotlin.test.assertTrue"))),
    )
  }

  @Test
  fun requireCustomMessage() {
    assertMessage(
        """
fun main() {
  require(1 == 2) { "the world is broken" }
}""",
        """
the world is broken
require(1 == 2) { "the world is broken" }
          |
          false
      """.trimIndent(),
        PowerAssertCompilerPluginRegistrar(setOf(FqName("kotlin.require"))),
    )
  }

  @Test
  fun checkCustomMessage() {
    assertMessage(
        """
fun main() {
  check(1 == 2) { "the world is broken" }
}""",
        """
the world is broken
check(1 == 2) { "the world is broken" }
        |
        false
      """.trimIndent(),
        PowerAssertCompilerPluginRegistrar(setOf(FqName("kotlin.check"))),
    )
  }

  @Test
  fun carriageReturnRemoval() {
    assertMessage(
      """
fun main() {
  val a = 0
  assert(a == 42)
}""".replace("\n", "\r\n"),
      """
Assertion failed
assert(a == 42)
       | |
       | false
       0
      """.trimIndent(),
    )
  }

  @Test
  fun constantExpression() {
    assertMessage(
      """
fun main() {
  assert(true)
  assert(false)
}""",
      """
Assertion failed
      """.trimIndent(),
    )
  }
}
