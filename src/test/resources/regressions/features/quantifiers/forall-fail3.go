package pkg;

//:: ExpectedOutput(type_error)
requires forall x int, x int :: 0 < x
func test () { }
