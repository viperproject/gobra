package main

import math "lib/math"

type cell struct{
    //:: ExpectedOutput(type_error)
    f math.foo;
};

//:: ExpectedOutput(type_error)
func test(f math.foo, pf *math.foo)
