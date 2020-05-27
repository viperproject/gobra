package pkg

func foo(ghost xs seq[int]) {
  // fails: 'low' index is not an integer
  //:: ExpectedOutput(type_error)
  ghost ys := xs[false:1]
}
