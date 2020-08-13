package pkg

/*
func test1() {
	a := [2]int { 12, 24 }
  assert a[0] == 12
  assert a[1] == 24
}

func test2() {
  a := [...]bool { true, false, true }
  assert len(a) == 3
}

func test3() {
	a := [2][2]int{[2]int{1, 2}, [2]int{3, 4}}
}

requires a == [2]int { 1, 2 }
func test4(a [2]int) {
  assert a[0] == 1
  assert a[1] == 2
}

ensures a == [...]bool { true, false, false }
func test5() (a [3]bool) {
  a[0] = true
  a[1] = false
  a[2] = false
}

requires a == [...]int { 42, 44 }
ensures b == [...]int { 42, 44 }
func test6(a [2]int) (b [2]int) {
  b = a
}
*/

requires a == [1][1][1]int { [1][1]int { [1]int { 1 } } }
func test7(a [1][1][1]int) {
  assert a[0][0][0] == 1
}