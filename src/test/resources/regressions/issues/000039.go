package main

type pair struct {
  left, right int
}

func test() {

  x1  := 5
  x2! := 5
  x3! := pair{1, 2}

  L:

  x1 = 8
  x2 = 8

  assert old[L](x1) == 8 && old[L](x2) == 5
  assert old[L](now(x2)) == 8

  // assert old[L](x3) == pair{1, 2} // crashes
}


requires acc(*x) && acc(**x) && acc(*y) && acc(**y)
requires **x == 1 && **y == 0
func test2(x, y **int) {
  *x  = *y
  **y = 2

  L: assert old(*old[L](*x)) == 0
}
