package pkg

type Point struct {
	x int
	y int
}

func foo() {
	// should be implemented later, but does not work currently...
	//:: ExpectedOutput(type_error)
	var a [2]Point
}
