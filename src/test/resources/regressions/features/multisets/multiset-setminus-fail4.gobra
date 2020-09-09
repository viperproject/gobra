package pkg

func foo() {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert mset[int] { 1, 2, 2 } setminus mset[int] { 2, 3 } == mset[int] { 1 }
}
