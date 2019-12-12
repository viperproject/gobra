
package pkg;

type Point struct {
  x int;
  y int;
};

func Origin() (r Point) {
  r = Point{0, 0};
};


func init() {
  a := Point{y: 5};
  assert a.x == 0 && a.y == 5;

  b := Point{};
  assert b.x == 0 && b.y == 0;
};
