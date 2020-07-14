package pkg

// fails: `a` should've been marked as ghost
//:: ExpectedOutput(type_error)
func foo(a [42]seq[bool]) {
}
