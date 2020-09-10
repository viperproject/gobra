package pkg

func foo(ghost x int, ghost m mset[bool]) {
  ghost var b bool
  
  // fails: operands are of incompatible types
  //:: ExpectedOutput(type_error)
  b = x in m
}
