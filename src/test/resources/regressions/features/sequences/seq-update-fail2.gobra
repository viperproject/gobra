package pkg

func foo(ghost xs seq[int]) {
  // fails since `true` is not of an integer type
  //:: ExpectedOutput(type_error)
  ghost ys := xs[true = 42]
}
