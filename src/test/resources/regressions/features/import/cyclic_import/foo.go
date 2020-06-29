package foo

// ##(-I src/test/resources/regressions/features/import/cyclic_import)
//:: ExpectedOutput(type_error)
import b "bar" // this is a cyclic import

ensures res == b.bar()
func foo() (res int) {
    b.bar()
}
