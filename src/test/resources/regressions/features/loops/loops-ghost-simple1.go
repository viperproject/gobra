// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package pkg

func foo() {
  ghost var n int
  invariant n == 0
	for i := 0; i < 64; i++ { }
}
