package pkg

func foo(ghost xs seq[int]) {
  // fails since `false` is not of an integer type
  //:: ExpectedOutput(type_error)
  ghost ys := xs[0 = false]
}
