// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ pkgInvariant path.PkgInv()
// @ dup pkgInvariant acc(path.RegisteredTypes().Contains(1), _) &&
// @ 	acc(path.RegisteredTypes().Contains(2), _)

package scion

// ##(-I ..)

// Example inspired by how PathTypes are registered in the SCION border router.
// Based on https://github.com/viperproject/VerifiedSCION/tree/master/pkg/slayers/scion.go

// We should have a system of checking ghost imports to make sure they do not introduce
// side effects that would not happen otherwise.
import (
	// @ "monotonicset"
	// @ importRequires path.PkgInv()
	// @ importRequires path.RegisteredTypes().DoesNotContain(1) &&
	// @ 	path.RegisteredTypes().DoesNotContain(2)
	//  importRequires false // TODO: check and then drop
	// @ "scion/path"
	"scion/path/onehop"
	"scion/path/scion"
)

// We should introduce preconditions and postconditions to this function to guarantee that
// all init functions in parallel imply the initialization obligations. The current
// solution is sound only if we have at most one init function (otherwise, resources from
// friend packages may be acquired multiple times). At the moment, only one init function
// per package is supported, so this is not currently a problem.
func init() {
	scion.RegisterPath()
	onehop.RegisterPath()
}
