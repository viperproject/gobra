package pkg

// obvious type error
func foo(x int, ghost xs seq[int], ghost ys seq[bool]) {
  //:: ExpectedOutput(type_error)
  ghost b := x in (xs in ys)
}
