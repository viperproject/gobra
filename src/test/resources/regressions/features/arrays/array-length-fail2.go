package pkg

func foo(a [21]int) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert len(a) == 12
}
