// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

//@ initEnsures path.PackageMem()
//@ initEnsures path.Registered(empty.PathType)
package main

// ##(-I ./imports)

import (
	//@ importRequires path.PackageMem()
	//@ importRequires path.Registered(empty.PathType)
	"layers"
	//@ "empty"
	_ "empty"
	//@ "path"
	_ "path"
)

//@ requires path.PackageMem() && path.Registered(empty.PathType)
func main() {
	var l layers.Layer = layers.Layer{}
	_ = l
}
