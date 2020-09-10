package pkg

func foo() {
  // fails: assigning ghost expression to non-ghost variable
  //:: ExpectedOutput(type_error)
  b := 42 in seq[1..100]
}
