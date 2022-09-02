// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

//@ initEnsures path.PackageMem()
// Verifying the the init postcondition here fails, because this property is
// not established by the init code of this package.
//:: ExpectedOutput(postcondition_error)
//@ initEnsures path.Registered(10)
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

// However, the main function is verified successfully, given that
// the init postconditions of this file imply the preconditions of main.
//@ requires path.PackageMem() && path.Registered(10)
func main() {
	var l layers.Layer = layers.Layer{}
	_ = l
}
