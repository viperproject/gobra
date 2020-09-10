package pkg

func foo(ghost xs seq[int]) {
  // fails: 'high' index is not an integer
  //:: ExpectedOutput(type_error)
  ghost ys := xs[1:xs]
}
