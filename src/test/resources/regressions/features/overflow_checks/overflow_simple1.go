// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package main

// ##(--overflow)

const MinInt64 = -9223372036854775808 // = -1 << 63
// @ requires x > MinInt64
// @ ensures res >= 0
func abs(x int64) (res int64) {
	if x < 0 {
		return -x
	} else {
		return x
	}
}
