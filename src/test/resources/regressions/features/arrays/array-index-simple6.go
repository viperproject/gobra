package pkg

ensures flag[0]
func test1() (flag [1]bool) {
  flag[0] = true
}

func test2() {
  a := test1()
  assert a[0]
}
