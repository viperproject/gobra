// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package empty

// ##(-I ..)

// The import precondition defaults to true.
// No ownership of any resource is transferred from the `path`
// package to the current package.
import "path"

const PathType path.Type = 0

type Path struct{}

//@ requires path.PackageMem()
//@ requires !path.Registered(PathType)
//@ ensures  path.PackageMem()
//@ ensures  path.Registered(PathType)
//@ ensures  forall t path.Type :: 0 <= t && t < path.MaxType ==>
//@ 	t != PathType ==> old(path.Registered(t)) == path.Registered(t)
//@ decreases
func RegisterPath() {
	path.RegisterPath(path.Metadata{
		Typ:  PathType,
		Desc: "Empty",
	})
}
