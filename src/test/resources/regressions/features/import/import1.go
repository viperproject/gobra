package main

import "fmt"
import "a";
import ("b")
import ("c");
import (
    "d"
    "e")
import (
    "f"
    "g"
)

import "lib/math"
import m "lib/mathm"
import . "lib/mathn"

func test() {
  //:: ExpectedOutput(type_error)
  m.foo();
}
