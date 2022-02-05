// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package main

func main() {
	var s = [3]int{1,2,3} //@ addressable: s
	//:: ExpectedOutput(parser_error)
	n := s[1::3]
}