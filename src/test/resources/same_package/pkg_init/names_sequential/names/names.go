// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// Contributed by Patricia Firlejczyk based on one of the examples
// not suitable for initialization-time irrelevance reported in
// https://dl.acm.org/doi/10.1145/3622844

// @ pkgInvariant NamesInv()

package names

// ##(-I ..)

import (
	"names/name"
)

var chrs /*@@@*/ [131072]string

func init() {
	// @ fold NamesInv()
}

// @ preserves NamesInv()
// @ ensures acc(res) && res._start == 0
// @ decreases
func Name(s string) (res *name.Name) {
	// @ unfold NamesInv()
	res = name.New(0, len(chrs))
	chrs[0] = "a"
	// @ fold NamesInv()
	return res
}
