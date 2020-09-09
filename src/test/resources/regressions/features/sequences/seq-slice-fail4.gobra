package pkg

func foo(ghost xs seq[int]) {
  // fails: cannot specify a capacity in a sequence slice expression
  //:: ExpectedOutput(type_error)
  ghost ys := xs[1:2:3]
}
