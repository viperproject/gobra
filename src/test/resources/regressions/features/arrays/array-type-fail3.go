package pkg

//:: ExpectedOutput(type_error)
requires a == b
func foo(a [1]int, b [2]int) {
}
