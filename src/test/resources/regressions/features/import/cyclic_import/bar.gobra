package bar

// ##(-I src/test/resources/regressions/features/import/cyclic_import)
//:: ExpectedOutput(type_error)
import f "foo" // this is a cyclic import

ensures res == f.foo()
func bar() (res int) {
    f.foo()
}
