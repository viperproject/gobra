package pkg

requires 0 < |xs|;
func foo(ghost xs seq[int]) {
  // fails since `xs[0]` is not assignable
  //:: ExpectedOutput(type_error)
  ghost xs[0] = 12
}
