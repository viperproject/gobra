package main

// ##(-I src/test/resources/regressions/features/import/cyclic_import)
//:: ExpectedOutput(type_error)
import f "foo" // this is a cyclic import

func main() {
    f.foo()
}
