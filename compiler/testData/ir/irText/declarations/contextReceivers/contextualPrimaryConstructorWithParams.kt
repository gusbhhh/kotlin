// FIR_IDENTICAL
// !LANGUAGE: +ContextReceivers

// MUTE_SIGNATURE_COMPARISON_K2: NATIVE
// ^ KT-57428

class O(val o: String)

context(O)
class OK(val k: String) {
    val result: String = o + k
}

fun box(): String {
    return with(O("O")) {
        val ok = OK("K")
        ok.result
    }
}
