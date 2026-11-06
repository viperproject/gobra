// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// Contributed by Patricia Firlejczyk based on one of the examples
// that do not satisfy Initialization-time Irrelevance as defined in
// this paper: https://dl.acm.org/doi/10.1145/3622844

// @ pkgInvariant NameInv()

package name

// ##(-I ../..)

type Name struct {
	Start  int
	Length int
}

func init() {
	// @ fold NameInv()
}

// @ ensures	acc(res)
// @ ensures	res.Length == length && res.Start == start
// @ decreases
func New(start int, length int) (res *Name) {
	res = new(Name)
	res.Start = start
	res.Length = length
	return res
}

// @ pure
// @ requires acc(&n.Start, _)
// @ decreases
func (n *Name) start() int {
	return n.Start
}

// @ pure
// @ requires acc(&n.Length, _)
// @ decreases
func (n *Name) length() int {
	return n.Length
}
