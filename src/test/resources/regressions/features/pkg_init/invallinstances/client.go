// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ pkgInvariant PkgInv()

package invallinstances

// This example demonstrates the ability to have static invariants over all
// allocated instances of a type (using the New function), as originally demoed
// in Peter's paper (https://link.springer.com/chapter/10.1007/11526841_4)
// with the Client class. Interestingly, the proof annotations for the allocator
// (New) in this version and in the version from Peter are very similar even
// though one is based in ownership types and the other is based in SL/IDF.

var ids /*@@@*/ int

// @ ghost var allocs@ set[*Client]

func init() {
	// @ fold PkgInv()
}

type Client struct {
	id   int
	name string
}

// @ preserves PkgInv()
// @ ensures   res.Inv()
// @ ensures   res.Allocated()
// @ decreases
func New(name string) (res *Client) {
	// @ unfold PkgInv()
	// @ defer fold PkgInv()
	res = new(Client)
	res.name = name
	res.id = ids
	ids += 1
	// @ allocs = allocs union set[*Client]{res}
	// @ fold res.Inv()
	return res
}
