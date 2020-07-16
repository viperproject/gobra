package pkg;

//:: ExpectedOutput(parser_error)
requires forall x int :: { x, } 0 < x
func test () { }
