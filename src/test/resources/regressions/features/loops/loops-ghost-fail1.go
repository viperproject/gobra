package pkg

func foo() {
  ghost n := 64
  //:: ExpectedOutput(type_error)
	for i := 0; i < n; i++ { }
}
