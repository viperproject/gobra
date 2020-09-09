package pkg

requires x in xs
func foo(ghost x int, ghost xs seq[int]) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert x # xs == 0
}
