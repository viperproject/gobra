// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ pkgInvariant StaticInv()

package fib

var cache /*@@@*/ map[int]int = make(map[int]int)

func init() {
	cache[0] = 1
	cache[1] = 1
	// @ fold StaticInv()
}

// @ requires  0 <= n && FibFits(n)
// @ preserves StaticInv()
// @ ensures   res == FibSpec(n)
// @ decreases n
func Fib(n int) (res int) {
	// @ unfold StaticInv()
	// @ defer fold StaticInv()
	if v, ok := cache[n]; ok {
		return v
	}
	// @ fold StaticInv()
	// @ assert FibSpec(n) == FibSpec(n-1) + FibSpec(n-2)
	v := Fib(n-1) + Fib(n-2)
	// @ unfold StaticInv()
	cache[n] = v
	return v
}
