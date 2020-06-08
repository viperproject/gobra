package pkg

func foo(ghost x int, ghost y int, ghost m mset[int]) {
  // fails trivially
  //:: ExpectedOutput(assert_error:assertion_error)
  assert 0 < x in m ==> 0 < y in m
}
