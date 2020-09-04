package pkg

func test1() {
	var a [3]int = [3]int{1, 2, 3}
}

func test2() {
	var a = [3]int{1, 2, 3}
  assert len(a) == 3
  assert cap(a) == 3
}

func test3() {
	n := ([3]int{1, 2, 3})[1]
  assert n == 2
}

func test4() {
	var a! [2]int = [...]int{1: 10, 0: 20}
  assert a == [2]int { 20, 10 }
}
