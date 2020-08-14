package pkg

func test1() {
	var a [3]int = [3]int{1, 2, 3}
}

func test2() {
	var a = [3]int{1, 2, 3}
  assert len(a) == 3
  assert cap(a) == 3
}