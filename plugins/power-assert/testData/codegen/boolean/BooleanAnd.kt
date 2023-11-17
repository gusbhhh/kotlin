fun box() = verifyMessage(
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
      """.trimIndent()
) {
    val text: String? = "Hello"
    assert(text != null && text.length == 5 && text.toLowerCase() == text)
}
