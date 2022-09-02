// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

//@ initEnsures path.PackageMem()
//@ initEnsures path.Registered(empty.PathType)
package main

// ##(-I ./imports)

import (
	//@ importRequires path.PackageMem() && path.Registered(empty.PathType)
	"layers"
	//@ "empty"
	_ "empty"
	//@ "path"
	_ "path"
)

//:: ExpectedOutput(main_pre_error)
//@ requires path.PackageMem() && path.Registered(10)
func main() {
	var l layers.Layer = layers.Layer{}
	_ = l
}
