package pkg

requires 0 < x # xs
func foo(ghost x int, ghost xs seq[int]) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert !(x in mset(xs))
}
