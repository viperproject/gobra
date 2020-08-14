package pkg

func foo() {
	//:: ExpectedOutput(parser_error)
	a := [...][...]bool{}
}
