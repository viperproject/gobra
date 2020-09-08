package pkg

func foo(ghost x int, ghost m mset[int]) (b bool) {
  // fails since `b` is not ghost
  //:: ExpectedOutput(type_error)
  b = x in m
}
