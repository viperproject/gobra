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
import m "lib/mathm" // currently not supported by the type checker
import . "lib/mathn"
//:: ExpectedOutput(type_error)
import _ "lib/matho" // currently not supported by the type checker
