package main

// ##(-I src/test/resources/regressions/features/import/fpredicate_import)
import b "bar"

func foo() {
    r! := b.Rectangle{Width: 2, Height: 5}
    fold b.RectMem(&r)
    assert r.Area() == 10
    unfold b.RectMem(&r)
    fold b.RectMem(&r)
    assert (*(b.Rectangle)).Area(&r) == 10
    unfold b.RectMem(&r)
}
