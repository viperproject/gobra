// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// Contributed by Patricia Firlejczyk based on one of the examples
// that do not satisfy Initialization-time Irrelevance as defined in
// this paper: https://dl.acm.org/doi/10.1145/3622844

// @ pkgInvariant NameInv()

package name

// ##(-I ../..)

type Name struct {
	_start  int
	_length int
}

func init() {
	// @ fold NameInv()
}

// @ ensures	acc(res)
// @ ensures	res._length == length && res._start == start
// @ decreases
func New(start int, length int) (res *Name) {
	res = new(Name)
	res._start = start
	res._length = length
	return res
}

// @ pure
// @ requires acc(&n._start, _)
// @ decreases
func (n *Name) start() int {
	return n._start
}

// @ pure
// @ requires acc(&n._length, _)
// @ decreases
func (n *Name) length() int {
	return n._length
}
