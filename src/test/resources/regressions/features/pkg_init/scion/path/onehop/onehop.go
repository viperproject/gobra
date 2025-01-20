// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package onehop

// ##(-I ../../..)

import "scion/path"

const PathType path.Type = 2

// @ requires  path.RegisteredTypes().DoesNotContain(uint16(PathType))
// @ preserves path.PkgInv()
// @ ensures   path.RegisteredTypes().Contains(uint16(PathType))
// @ decreases
func RegisterPath() {
	tmp := path.Metadata{
		Type: PathType,
		Desc: "OneHop",
	}
	path.RegisterPath(tmp)
}
