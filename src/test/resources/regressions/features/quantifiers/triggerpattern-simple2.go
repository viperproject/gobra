package pkg;

type Point struct {
  x int
  y int
}

requires forall p Point :: { p.x, p.y } p.x < p.y
func example1 () { }
