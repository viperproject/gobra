package pkg;

type Point struct {
  x int
  y int
}

requires exists p Point :: p.x < p.y
requires exists p Point :: { p.x, p.y } p.x < p.y
func test1() { }

requires exists n int :: p.x <= n && n < p.y
func test2(p Point) {
  assert p.x < p.y
}
