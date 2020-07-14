package pkg

// fails: length of `a` must be non-negative
//:: ExpectedOutput(type_error)
func foo(a [-12]int) {
}
