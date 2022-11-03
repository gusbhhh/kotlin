// FIR_DISABLE_LAZY_RESOLVE_CHECKS
// !CHECK_TYPE

interface A {
    val foo: Any?
}

interface C: A {
    override val foo: String
}
interface B: A {
    override var foo: String?
}

fun test(a: A) {
    if (a is B && a is C) {
        a.foo = ""
        a.foo = null
        a.foo
    }
}
