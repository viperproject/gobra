package main

func test() {

  x1  := 5
  x2! := 5

  L:

  x1 = 8
  x2 = 8

  assert old[L](x1) == 8 && old[L](x2) == 5
  assert old[L](now(x2)) == 8

}