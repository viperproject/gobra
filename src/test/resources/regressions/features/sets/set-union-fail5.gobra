package pkg

func foo() (ghost s set[int]) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert s == s union set[int] { 42 }
}
