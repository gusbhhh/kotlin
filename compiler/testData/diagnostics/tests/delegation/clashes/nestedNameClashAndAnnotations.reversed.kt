// FIR_IDENTICAL
// !DIAGNOSTICS: -UNUSED_PARAMETER
// FIR_DUMP
package second

@Target(AnnotationTarget.TYPE)
annotation class Anno(val i: Int)

interface Base<A> {
    fun foo() {}
}

const val outer = 0
const val inner = ""

class MyClass(val prop: @Anno(0 + inner) second.Base<@Anno(1 + inner) second.Base<@Anno(2 + inner) Int>>): @Anno(3 <!NONE_APPLICABLE, NONE_APPLICABLE!>+<!> outer) Base<@Anno(4 <!NONE_APPLICABLE, NONE_APPLICABLE!>+<!> outer) Base<@Anno(5 <!NONE_APPLICABLE, NONE_APPLICABLE!>+<!> outer) Int>> by prop {
    interface Base<B>

    companion object {
        const val outer = ""
        const val inner = 0
    }
}
