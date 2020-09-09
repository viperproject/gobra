package pkg

func foo() {
  // error: overlapping keys
  //:: ExpectedOutput(type_error)
  ghost s := seq[int] { 1:42, 12 }
}
