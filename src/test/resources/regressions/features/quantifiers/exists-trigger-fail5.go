package pkg

// invalid trigger: pattern doesn't include `y`.
//:: ExpectedOutput(type_error)
requires exists x int, y int :: { x } 0 < x 
func foo () { }
