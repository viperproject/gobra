package pkg;

func foo (ghost n int) bool

// invalid trigger: impure invocation pattern
//:: ExpectedOutput(type_error)
requires exists n int :: { foo(n) } 0 < n
func bar () { }
