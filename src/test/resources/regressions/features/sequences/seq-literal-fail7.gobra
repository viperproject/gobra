package pkg

func foo() {
  ghost s := seq[int] { 1:12, 0:24 }
  //:: ExpectedOutput(assert_error:assertion_error)
  assert s == seq[int] { 12, 24 }
}
