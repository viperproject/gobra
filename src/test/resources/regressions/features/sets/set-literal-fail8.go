package pkg

func foo() {
  ghost s := set[seq[int]] { { 1 : 10, 0 : 20 } }
  //:: ExpectedOutput(assert_error:assertion_error)
  assert s == set[seq[int]] { { 10, 20 } }
}
