package main

// ##(-I src/test/resources/regressions/features/import/struct_import)
import b "bar"

func client() {
    c := b.BarCell{5};
    assert c.val == 5;
    d := &b.BarCell{10};
    assert d.val == 10;
}
