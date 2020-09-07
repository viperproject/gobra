package pkg

type Point struct {
	x int
	y int
}

//:: ExpectedOutput(parser_error)
ensures forall p *Point :: {  } acc(p.x)
func test() { }
