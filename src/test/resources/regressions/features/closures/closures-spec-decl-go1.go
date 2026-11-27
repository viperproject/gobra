// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package closuresSpecDeclGo1

// Closure spec declarations in annotated Go files.

//@ requires x >= 0
//@ ensures r > x
//@ spec greater(x int) (r int)

//@ requires c implements greater
//@ ensures r > 1
func hof(c func(int) int) (r int) {
	return c(1) /*@ as greater @*/
}

func client() {
	// `spec` remains usable as an identifier in Go code
	spec := 2
	c := /*@ ensures r == x + 1 @*/
		func /*@ f @*/ (x int) (r int) {
			return x + 1
		}
	/*@
	proof c implements greater {
		r = c(x) as f
	}
	@*/
	r := hof(c)
	//@ assert r > 1
	_ = spec
}
