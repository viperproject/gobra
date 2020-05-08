package pkg

pure func foo(ghost n int) bool
pure func bar(ghost n int) bool

// specifying multiple trigger patterns is supported
requires forall i int :: { foo(i) } { bar(i) } (0 < i ==> 0 <= i)
requires exists i int :: { foo(i) } { bar(i) } (0 < i ==> 0 <= i)
func example() { }
