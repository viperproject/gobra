package pkg

const N = -4

func foo() {
	//:: ExpectedOutput(type_error)
	var a [N]int
}
