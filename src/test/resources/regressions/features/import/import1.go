package main

import fmt "fmt"
import a "a";
import (b "b")
import (c "c");
import (
    d "d"
    e "e")
import (
    f "f"
    g "g"
)

//:: ExpectedOutput(type_error)
import m "lib/mathm" // wrong package name used on purpose such that this test case does not potentially depend on the configured Go path
import . "lib/mathn"

func test() {
  m.foo();
}
