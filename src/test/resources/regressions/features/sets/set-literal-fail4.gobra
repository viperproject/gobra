package pkg

func foo() (ghost s seq[int]) {
  // fails: sets and sequences are incompatible
  //:: ExpectedOutput(type_error)
  s = set[int] { 42 }
}
