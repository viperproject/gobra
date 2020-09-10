package pkg

requires m != mset[int] { }
func foo(ghost m mset[int]) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert m union m == m
}
