package pkg

func foo(ghost x int, ghost s set[bool]) {
  ghost var n int
  //:: ExpectedOutput(type_error)
  n = x # s
}
