package pkg

requires n == 2
func foo(n int) {
  //:: ExpectedOutput(type_error)
  a := [n]bool { false, true }
}
