package pkg

func foo(ghost xs seq[int]) {
  // fails: sequence slice expressions do not support specifying (optional) capacities
  //:: ExpectedOutput(parser_error)
  ghost ys := xs[1:2:]
}
