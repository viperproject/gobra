package pkg

func foo(b bool) {
  // fails: incorrectly typed right operand
  //:: ExpectedOutput(type_error)
  ghost xs := seq[1 .. b]
}
