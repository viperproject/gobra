package pkg;

type Point struct {
  x int
  y int
}

requires forall p Point :: p.x < p.y
requires forall p Point :: { p.x, p.y } p.x < p.y
func test1() { }

func test2(p Point) {
  assert forall n int :: p.x <= n && n < p.y ==> p.x < p.y
}
