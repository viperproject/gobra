// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

//@ initEnsures path.PackageMem()
//@ initEnsures path.Registered(empty.PathType)
package layers

// ##(-I ..)

import (
	// no import precondition, defaults to true
	"empty"

	// Care must be taken when using ghost imports, given that having a ghost
	// import may allow the user to prove stronger init postconditions then what
	// they would have been able to prove (likewise for the main-precondition).
	// Unfortunately, Gobra does not yet make a distinction between ghost imports
	// and non-ghost imports. This should be addressed in a future PR.

	// This import is required to allow us to use the qualifier 'path' in ghost code.
	//@ importRequires path.PackageMem() && !path.Registered(empty.PathType)
	//@ "path"

	// This import is the non-ghost import of 'path' to "legitimate" the stronger
	// initPostconditions that one may learn from importing it. Unfortunately, gofmt
	// automatically removes the import if we remove the wildcard because there is no
	// non-ghost usage of the qualifier 'path'
	_ "path"
)

func init() {
	//@ assert path.PackageMem()
	//@ assert !path.Registered(empty.PathType)
	empty.RegisterPath()
	//@ assert path.PackageMem()
	//@ assert path.Registered(empty.PathType)
}

type Layer struct{}
