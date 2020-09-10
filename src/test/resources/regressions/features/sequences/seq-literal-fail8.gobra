package pkg

requires 0 == n
func foo(ghost n int) {
  // error: keys must be constant
  //:: ExpectedOutput(type_error)
  ghost xs := seq[int] { n : 10 }
}
