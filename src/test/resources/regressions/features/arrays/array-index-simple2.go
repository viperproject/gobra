package pkg

func test1() {
  var a [3][2]int
  assert len(a) == 3 && cap(a) == 3
  assert len(a[2]) == 2 && cap(a[2]) == 2
}

requires forall i int, j int :: 0 <= i && i < 2 && 0 <= j && j < 3 ==> a[i][j] == i + j
func test2(a [2][3]int) {
  b := a
  c := b
  d := c
  assert d[1][2] == 1 + 2
}

func test3() {
  var a [2]int
  b := a
  assert a == b
  b[1] = 42
  assert a != b
}

requires forall i int, j int :: 0 <= i && i < 2 && 0 <= j && j < 3 ==> a[i][j] == 0
func test4(a [2][3]int) {
  var b [2][3]int
  assert a == b
  b[0][2] = 24
  assert a != b
}
