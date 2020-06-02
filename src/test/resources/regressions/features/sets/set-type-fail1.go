package pkg

// fails: `s` should've been marked ghost
//:: ExpectedOutput(type_error)
func foo(s set[int]) {
}

