package pkg

func test1() {
	var a [12]int
	n := a[4]
  assert n == 0
}

requires forall i int :: 0 <= i && i < len(a) ==> acc(a[i])
requires a[0] == false
func test2(a [12]bool) {
	var b bool
  b = a[0]
  assert !b
}
