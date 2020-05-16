package pkg

// fails: cannot take the length of an integer
//:: ExpectedOutput(type_error)
requires 0 < |n|;
func foo(n int) {
}
