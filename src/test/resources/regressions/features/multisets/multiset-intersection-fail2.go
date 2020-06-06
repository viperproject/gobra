package pkg

func foo() {
  //:: ExpectedOutput(type_error)
  assert mset[int] { } intersection mset[bool] { } == mset[int] { }
}
