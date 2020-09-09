package pkg

func foo() (b bool) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert set[bool] { true, false } == set[bool] { false } union set[bool] { b }
}
