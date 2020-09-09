package pkg

func foo() {
  // incorrectly typed sequence literal (bool not assignable to int)
  //:: ExpectedOutput(type_error)
  xs := seq[int] { 1, 17, 32, false, 100 }
}
