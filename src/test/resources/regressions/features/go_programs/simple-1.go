// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package test

//:: ExpectedOutput(postcondition_error:assertion_error)
//@ ensures false
func test() (res int) {
	res = 42
	return
}
