package pkg

func foo() {
  // fails: clearly wrong
  //:: ExpectedOutput(assert_error:assertion_error)
  assert |mset[int] { 1, 2 } union mset[int] { 2, 3 }| == 3
}
