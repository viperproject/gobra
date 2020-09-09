package pkg

func foo(ghost x int, ghost m mset[int]) {
  var n int
  // fails since `n` isn't ghost
  //:: ExpectedOutput(type_error)
  n = x # m
}
