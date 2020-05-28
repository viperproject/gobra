package pkg

func foo() {
  // incorrectly typed sequence literal
  //:: ExpectedOutput(type_error)
  xs := seq[int] { false }
}
