package main

// ##(-I src/test/resources/regressions/features/import/unqualified_import/methods)
import . "bar"

func foo() {
    r! := Rectangle{Width: 2, Height: 5}
    assert r.Area() == 10
    assert (*(Rectangle)).Area(&r) == 10
}
