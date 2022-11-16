// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package main

var x /*@@@*/ int = 1

func test() {
	f := func /*@ g @*/ () int {
		return x
	}
}
