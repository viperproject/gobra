package pkg;

type Point struct {
  x int
  y int
}

//:: ExpectedOutput(type_error)
requires exists p *Point :: acc(p.x)
func test () { }
