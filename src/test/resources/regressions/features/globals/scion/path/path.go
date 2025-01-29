// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package path

// ##(-I ../)

// This approach is sound, assuming that the paths below are unique per a package.
// This assumption is not problematic, and is similar to the assumption we make for
// imports.
// @ friendPkg "../" PkgInv()
// @ friendPkg "../" RegisteredTypes().DoesNotContain(1) &&
// @ 	RegisteredTypes().DoesNotContain(2)

// @ import "monotonicset"

/*@

pred PkgInv() {
	acc(&registeredPaths) &&
	registeredKeys.Inv()  &&
	(forall i uint16 :: 0 <= i && i < maxPathType ==>
		registeredPaths[i].inUse == registeredKeys.FContains(i))
}

ghost
decreases
pure func RegisteredTypes() monotonicset.BoundedMonotonicSet {
	return registeredKeys
}

@*/

const maxPathType = 256

var registeredPaths /*@@@*/ [maxPathType]metadata

// @ ghost var registeredKeys = monotonicset.Alloc()

// Type indicates the type of the path contained in the SCION header.
type Type uint8

type metadata struct {
	inUse bool
	Metadata
}

func init() {
	// @ fold PkgInv()
}

// Metadata defines a new SCION path type, used for dynamic SICON path type
// registration. The original definition contains an extra field named 'New'
// which stores a closure that allocates a new instance of the corresponding
// path type. To avoid complications with closures here, we ignore it.
type Metadata struct {
	// Type is a unique value for the path.
	Type Type
	// Desc is the description/name of the path.
	Desc string
}

// @ requires  0 <= pathMeta.Type && pathMeta.Type < 256
// @ requires  RegisteredTypes().DoesNotContain(uint16(pathMeta.Type))
// @ preserves PkgInv()
// @ ensures   RegisteredTypes().Contains(uint16(pathMeta.Type))
// @ decreases
func RegisterPath(pathMeta Metadata) {
	// @ unfold acc(PkgInv(), 1/2)
	// @ RegisteredTypes().DoesNotContainsImpliesAbstractDoesNotContain(uint16(pathMeta.Type), 1/4)
	// @ RegisteredTypes().DoesNotContainImpliesNotFContains(uint16(pathMeta.Type))
	// @ unfold acc(PkgInv(), 1/2)
	// @ defer fold PkgInv()
	pm := registeredPaths[pathMeta.Type]
	if pm.inUse {
		// @ assert false
		panic("path type already registered")
	}
	// @ RegisteredTypes().Add(uint16(pathMeta.Type))
	// @ RegisteredTypes().ContainsImpliesFContains(uint16(pathMeta.Type))
	registeredPaths[pathMeta.Type].inUse = true
	registeredPaths[pathMeta.Type].Metadata = pathMeta
}

// @ requires  0 <= t && t < maxPathType
// @ preserves acc(PkgInv(), 1/512)
// @ decreases
func (t Type) String() string {
	// @ unfold acc(PkgInv(), 1/512)
	// @ defer fold acc(PkgInv(), 1/512)
	pm := registeredPaths[t]
	if !pm.inUse {
		return "UNKNOWN"
	}
	return pm.Desc
}
