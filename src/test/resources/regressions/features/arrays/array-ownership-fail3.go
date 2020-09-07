package pkg

//:: ExpectedOutput(type_error)
requires acc(a[1])
func foo(a [4]int) {
}
