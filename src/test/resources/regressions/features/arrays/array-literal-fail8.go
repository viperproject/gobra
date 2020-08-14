package pkg

func foo() {
  //:: ExpectedOutput(type_error)
  assert [3]int { 1, 2, 3 } == [4]int { 3, 2, 1, 0 }
}
