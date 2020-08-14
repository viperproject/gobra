package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert [3]int { 1, 2, 3 } == [3]int { 3, 2, 1 }
}
