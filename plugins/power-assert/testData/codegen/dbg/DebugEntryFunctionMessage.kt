// FUNCTION: dbg

fun box() = verifyMessage(
    """
    sum=6
    Message:
    dbg(operation, 1 + 2 + 3, "Message:")
        |            |   |
        |            |   6
        |            3
        sum
    """.trimIndent(),
) {
    val operation = "sum"
    dbg(operation, 1 + 2 + 3, "Message:")
}

fun <T> dbg(key: Any, value: T): T = value
fun <T> dbg(key: Any, value: T, msg: String): T = throw RuntimeException(key.toString() + "=" + value + "\n" + msg)
