package pkg

func test1() {
  var a [4][3]int
  b := a[2]
  assert b[1] == 0
}