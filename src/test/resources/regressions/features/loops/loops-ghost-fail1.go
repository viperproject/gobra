// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package pkg

func foo() {
  ghost n := 64
  //:: ExpectedOutput(type_error)
	for i := 0; i < n; i++ { }
}
