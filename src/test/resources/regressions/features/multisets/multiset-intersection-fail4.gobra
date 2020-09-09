package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert mset[int] { 1, 2, 2 } intersection mset[int] { 2, 3 } == mset[int] { 2, 2 }
}
