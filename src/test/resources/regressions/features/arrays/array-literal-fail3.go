package pkg

func foo() {
	//:: ExpectedOutput(type_error)
	a := [2]int{1, 2, 3}
}
