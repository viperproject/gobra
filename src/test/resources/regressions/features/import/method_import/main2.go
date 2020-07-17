package main

// ##(-I src/test/resources/regressions/features/import/method_import)
import b "bar2"

func foo() {
    r := b.CreateRectangle()
    assert r.Area() == 10
    assert (*(b.Rectangle)).Area(r) == 10
}
