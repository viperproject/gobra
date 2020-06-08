package pkg

func foo(ghost x int, ghost y int, ghost m mset[int]) {
  //:: ExpectedOutput(type_error)
  ghost n := x in m + y in m
}
