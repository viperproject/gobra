package pkg

func test1() {
  var a! [4]int
  assert acc(a[2])
}

func test2(a! [4]int) {
  assert acc(a[1])
}

func test3(a! [4]int) {
  assert acc(a[2])
}

requires acc(a[1])
func test4(a! [4]int) {
  assert acc(a[1])
}

func test5() (a! [4]int) {
  assert acc(a[2])
}

ensures a == [4]int { 0, 0, 12, 0 }
func test6() (a! [4]int) {
  a[2] = 12
}
