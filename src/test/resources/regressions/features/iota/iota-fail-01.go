// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package test

const (
	// Not allowed - init expression cannot be found for x1.
	//:: ExpectedOutput(type_error)
	x1
	x2 = 1
)

const (
	y1 = 1
	y2, y3 = 2, 2
	// Not allowed - init expression cannot be found for y4.
	//:: ExpectedOutput(type_error)
	y4
)

const (
	w1 = 1
	// Not allowed - init expression cannot be found for w2, w3,
	// and w4.
	//:: ExpectedOutput(type_error)
	w2, w3, w4
)

func main() {
	// Not allowed - iota can only occur inside the declaration
	// of constants.
	//:: ExpectedOutput(type_error)
	z := iota
}