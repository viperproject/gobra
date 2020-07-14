package pkg

func test1() {
  var a [12]int
  assert len(a) == 12
}

func test2(a [12]int, b [42]bool, c [6]int) {
  assert len(a) == 12
  assert len(b) == 42
  assert len(c) != 2
}

requires len(a) == 12
func test3(a [42]int) {
  assert false
}
