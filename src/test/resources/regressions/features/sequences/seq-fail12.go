package pkg

func foo(ghost xs seq[int]) {
  // fails: cannot assign ghost expression to actual variable
  //:: ExpectedOutput(type_error)
  n := |xs|
}
