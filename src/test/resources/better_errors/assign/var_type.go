// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package main

func var_type() {
	//:: ExpectedOutput(parser_error)
	var a int := 10
}