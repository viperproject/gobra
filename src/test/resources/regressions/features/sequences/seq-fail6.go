package pkg

func foo() {
  // fails since a ghost expression is assigned to an actual variable `xs`
  //:: ExpectedOutput(type_error)
  xs := seq[int] { }
}
