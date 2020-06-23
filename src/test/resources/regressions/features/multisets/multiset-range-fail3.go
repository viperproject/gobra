package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert 0 < 42 in mset[1..10]
}
