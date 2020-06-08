package pkg

func foo(ghost x int, ghost m mset[bool]) {
  ghost var n int
  // fails since the types of `in`'s operands are incompatible
  //:: ExpectedOutput(type_error)
  n = x in m
}
