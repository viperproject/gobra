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

import math "lib/math"
import m "lib/mathm" // import of a package that does not exist
import . "lib/mathn"

func test() {
  //:: ExpectedOutput(type_error)
  m.foo();
}
