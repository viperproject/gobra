// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// Contributed by Patricia Firlejczyk based on one of the examples
// not suitable for initialization-time irrelevance reported in
// https://dl.acm.org/doi/10.1145/3622844

// @ pkgInvariant StdNamesAltInv()

package stdNamesAlt

// ##(-I ..)

import (
	"names"
	"names/name"
)

var anyRef *name.Name = names.Name("anyRef")
var array *name.Name = names.Name("array")
var list *name.Name = names.Name("list")

func init() {
	// @ fold StdNamesAltInv()
}
