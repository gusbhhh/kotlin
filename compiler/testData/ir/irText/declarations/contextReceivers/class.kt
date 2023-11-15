// FIR_IDENTICAL
// !LANGUAGE: +ContextReceivers

// MUTE_SIGNATURE_COMPARISON_K2: NATIVE
// ^ KT-57428

class Outer {
    val x: Int = 1
}

context(Outer)
class Inner(arg: Any) {
    fun bar() = x
}

fun f(outer: Outer) {
    with(outer) {
        Inner(3)
    }
}
