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
//:: ExpectedOutput(type_error)
import m "lib/mathm" // wrong package name used on purpose such that this test case does not potentially depend on the configured Go path
import . "lib/mathn"

func test() {
  m.foo();
}
