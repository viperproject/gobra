package pkg

func foo() {
  // error: set literals cannot have keys
  //:: ExpectedOutput(type_error)
  ghost s := set[bool] { 0 : false }
}
