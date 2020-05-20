package pkg

func foo(ghost xs seq[int]) (ghost ys seq[bool]) {
  // fails: integer sequence not assignable to Boolean sequence
  //:: ExpectedOutput(type_error)
  ys = xs[0 = 42]
}
