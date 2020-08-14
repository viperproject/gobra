package pkg

func foo() {
	//:: ExpectedOutput(type_error)
	a := [1]int{false}
}
