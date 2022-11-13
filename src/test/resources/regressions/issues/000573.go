// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package main

type X int

func (s X) f() {
	x := func /*@ g @*/ () {
		return
	}
}
