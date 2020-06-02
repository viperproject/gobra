package pkg

// fails: sets `s1` and `s2` are of inidentical types
//:: ExpectedOutput(type_error)
ensures s1 == s2
func foo(ghost s1 set[int]) (ghost s2 set[bool]) {
}
