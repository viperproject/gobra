package pkg

func foo(ghost xs seq[int]) {
  // fails: index into `xs` isn't an integer
  //:: ExpectedOutput(type_error)
  ghost n := xs[true]
}
