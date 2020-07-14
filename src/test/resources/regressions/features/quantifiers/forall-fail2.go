package pkg;

//:: ExpectedOutput(parser_error)
requires forall n int int :: true
func test () { }
