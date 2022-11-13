// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ initEnsures PackageMem()
// @ initEnsures forall t Type :: 0 <= t && t < MaxType ==> !Registered(t)
package path

/*@
ghost const MaxType = 256
@*/

var (
	// the original code uses an array of length 256,
	// but arrays of custom types are not yet supported
	registeredPaths/*@@@*/ []Metadata
	strictDecoding/*@@@*/ bool = true
)

type Type uint8

type Metadata struct {
	Desc  string
	InUse bool
	Typ   Type
}

/*@
pred PackageMem() {
	acc(&registeredPaths) &&
	len(registeredPaths) == MaxType &&
	(forall i int :: 0 <= i && i < len(registeredPaths) ==> acc(&registeredPaths[i])) &&
	acc(&strictDecoding)
}
@*/

/*@
ghost
requires 0 <= t && t < MaxType
requires acc(PackageMem(), _)
ensures  res == unfolding acc(PackageMem(), _) in registeredPaths[t].InUse
pure func Registered(t Type) (res bool) {
	return unfolding acc(PackageMem(), _) in registeredPaths[t].InUse
}
@*/

/*@
ghost
requires 0 <= t && t < MaxType
requires acc(PackageMem(), _)
pure func GetType(t Type) (res Metadata) {
	return unfolding acc(PackageMem(), _) in registeredPaths[t]
}
@*/

func init() {
	//@ assume len(registeredPaths) == MaxType
	//@ fold PackageMem()
}

// RegisterPath registers a new path type globally.
// The type passed in must be unique, or a runtime panic will occur.
// @ requires 0 <= pathMeta.Typ && pathMeta.Typ < MaxType
// @ requires PackageMem()
// @ requires !Registered(pathMeta.Typ)
// @ ensures  PackageMem()
// @ ensures  forall t Type :: 0 <= t && t < MaxType ==>
// @ 	t != pathMeta.Typ ==> old(Registered(t)) == Registered(t)
// @ ensures  Registered(pathMeta.Typ)
// @ decreases
func RegisterPath(pathMeta Metadata) {
	//@ unfold PackageMem()
	pm := registeredPaths[pathMeta.Typ]
	if pm.InUse {
		panic("path type already registered")
	}
	registeredPaths[pathMeta.Typ] = pathMeta
	registeredPaths[pathMeta.Typ].InUse = true
	//@ fold PackageMem()
}
