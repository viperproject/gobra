package pkg

func foo() {
  // fails: incorrectly typed left operand
  //:: ExpectedOutput(type_error)
  ghost xs := seq[true..5]
}
