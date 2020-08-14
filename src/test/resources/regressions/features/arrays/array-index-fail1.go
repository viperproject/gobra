package pkg

func foo(a [4]int) {
  //:: ExpectedOutput(type_error)
  assert a[2]
}
