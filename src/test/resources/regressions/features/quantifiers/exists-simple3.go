package pkg;

pure func foo(ghost n int) bool

requires exists k int :: foo(k)
func bar() { }
