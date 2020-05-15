package pkg

ghost func example1(xs seq[int], ys seq[bool]) {
  // fails since it tries to append two sequences of unidentical types
  //:: ExpectedOutput(type_error)
  zs := xs ++ ys
}
