// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ dup pkgInvariant acc(DupPkgInv(), _)
package main

// ##(-I ../)

import (
	"concfib"
	"fib"
)

// @ pred DupPkgInv() { acc(&x) && x == 10 }

var x /*@@@*/ int = valX()

// I am not super happy with having implicit free preconditions (which are sound) for
// this function (it is inconsistent with the design of main, where we
// must provide the preconditions for main). Nonetheless, having to provide
// a spec (including termination measures) for this function is a bit excessive.
// An alternative would be to change the encoding for main.
func init() {
	// this function is checked to be mayInit
	foo()

	// this one is not checked for mayInit, it is imported.
	y := fib.Fib(3)
	// also, the call above requires fib.StaticInv(). We can actually
	// perform this call because the runtime guarantes that the main thread of
	// different static initializers does not interleave. Gobra checks that the
	// invariant of all imported packages is preserved.

	// @ assert fib.FibSpec(0) == 1
	// @ assert fib.FibSpec(1) == 1
	// @ assert fib.FibSpec(2) == 2
	// @ assert fib.FibSpec(3) == 3
	// @ assert y == 3
	_ = y

	// @ fold DupPkgInv()
}

func Test() {
	x := concfib.FibV1(3)
	// @ assert concfib.FibSpec(0) == 1
	// @ assert concfib.FibSpec(1) == 1
	// @ assert concfib.FibSpec(2) == 2
	// @ assert concfib.FibSpec(3) == 3
	// @ assert x == 3
	_ = x
}

func TestOpenDupInv() {
	// @ concfib.AcquireDupPkgInv()
	// @ assert acc(concfib.StaticInv(), _)
}

// Must be marked with `mayInit`, otherwise Gobra throws an error in the declaration
// of `x`. `mayInit` signals that a method might be called from the static initializer
// of the package in which it is declared, and thus, it is not allowed to assume any
// of its invariants. Notice that this is an implementation detail! This fact is not
// considered part of the public interface of the function (if there is one). However,
// we provide this detail with the function spec because we currently do not have any
// systematic way of adding annotations regarding implementation details to Gobra.
// @ mayInit
// @ ensures r == 10
// @ decreases
func valX() (r int) {
	return 10
}

// termination is necessary, otherwise `foo` cannot be
// called from the static initializer.
// @ mayInit
// @ decreases
func foo() {}

// We check that no interface methods are annotated with mayInit.
type I interface {
	// @ decreases
	m()
}

// the following precondition is checked to follow from all the non-duplicable
// package invariants of all direct imports:
// @ requires fib.StaticInv()
// @ decreases
func main() {
	x := fib.Fib(3)
	// @ assert fib.FibSpec(0) == 1
	// @ assert fib.FibSpec(1) == 1
	// @ assert fib.FibSpec(2) == 2
	// @ assert fib.FibSpec(3) == 3
	// @ assert x == 3
	_ = x
}
