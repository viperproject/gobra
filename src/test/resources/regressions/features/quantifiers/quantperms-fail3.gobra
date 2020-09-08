package pkg

type Point struct {
	x int
	y int
}

func test() {
  //:: ExpectedOutput(parser_error)
  assert forall p *Point :: { p.x , } acc(p.x)
}
