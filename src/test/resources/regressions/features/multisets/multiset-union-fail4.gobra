package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert mset[int] { 1, 2 } union mset[int] { 2, 3 } == mset[int] { 1, 2, 3 }
}
