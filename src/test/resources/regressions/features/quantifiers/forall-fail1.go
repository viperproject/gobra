package pkg;

//:: ExpectedOutput(parser_error)
requires forall n n int :: true
func test () { }
