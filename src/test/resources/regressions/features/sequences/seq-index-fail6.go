package pkg

requires len(xs) == 2
func foo(ghost xs seq[int]) {
  var b bool
  // fails: cannot assign integer to Boolean variable
  //:: ExpectedOutput(type_error)
  b = xs[1]
}
