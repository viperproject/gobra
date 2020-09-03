package pkg

type Point struct {
	x int
	y int
}

func test() {
  //:: ExpectedOutput(type_error)
  assert forall p *Point :: acc(p.z)
}
