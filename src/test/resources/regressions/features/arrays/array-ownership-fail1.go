package pkg

func test1() {
  var a [4]int
  //:: ExpectedOutput(type_error)
  assert acc(a[2])
}