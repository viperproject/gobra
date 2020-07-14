package pkg;

//:: ExpectedOutput(parser_error)
requires forall x int :: { } 0 < x
func test () { }
