package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert 2 in set[int] { 1, 3 }
}
