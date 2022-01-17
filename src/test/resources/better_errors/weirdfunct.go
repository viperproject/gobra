package main

import (
	"fmt"
	"math/big"
)

type myStruct struct {
	F0 [0]struct{}
	b  float32
}

type (
	unknownVal struct{}
	intVal     struct{ val *big.Int }   // Int values not representable as an int64
	ratVal     struct{ val *big.Rat }   // Float values representable as a fraction
	floatVal   struct{ val *big.Float } // Float values not representable as a fraction
	complexVal struct{ re, im Value }
)

func (x complexVal) String() string { return fmt.Sprintf("(%s + %si)", x.re, x.im) }

func newFloat() *big.Float { return new(big.Float).SetPrec(prec) }
