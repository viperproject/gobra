package pkg

func foo() {
  var a [4]int
  // fails: `a` is not addressable
  //:: ExpectedOutput(type_error)
  assert acc(a[2])
}