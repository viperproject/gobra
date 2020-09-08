package pkg

func foo(ghost xs seq[int]) {
  // fails: cannot assign ghost expression to non-ghost (local) variable
  //:: ExpectedOutput(type_error)
  ys := xs[2:3]
}
