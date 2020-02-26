package main

func test() {
  //:: ExpectedOutput(type_error)
  s0 := "test0"
  //:: ExpectedOutput(type_error)
  s1 := `test1`
  /* multi line raw strings are currently not supported by the preprocessor
  s2 := `\n
\n`
  */
  //:: ExpectedOutput(type_error)
  s3 := "\""
  //:: ExpectedOutput(type_error)
  s4 := "Hello, world!\n"
}
