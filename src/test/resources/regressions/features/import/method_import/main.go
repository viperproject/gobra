package main

// ##(-I src/test/resources/regressions/features/import/method_import)
import b "bar"

func foo() {
    r! := b.Rectangle{Width: 2, Height: 5}
    assert r.Area() == 10
    assert (*(b.Rectangle)).Area(&r) == 10
}
