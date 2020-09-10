package pkg

func foo(ghost xs seq[bool]) {
  // fails: cannot mix up ghost expressions and actual program constructs
  //:: ExpectedOutput(type_error)
  if (xs[0]) { }
}
