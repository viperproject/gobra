// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ dup pkgInvariant acc(StaticInv(), _)

package byte

type Byte struct {
	value byte
}

var byteCache /*@@@*/ [256]*Byte

func init() {
	// @ invariant 0 <= i && i <= 256 && acc(&byteCache)
	// @ invariant (forall j, k int :: 0 <= j && j < k && k < i  ==>
	// @ 	byteCache[j] != byteCache[k])
	// @ invariant (forall j int :: 0 <= j && j < i ==>
	// @ 	acc(byteCache[j]) && byteCache[j].value == byte(j) - 128)
	// @ decreases 256 - i
	for i := 0; i < 256; i++ {
		byteCache[i] = alloc(byte(i) - 128)
	}
	// @ fold StaticInv()
}

// @ ensures acc(&res.value) && res.value == val
// @ decreases
// @ mayInit
func alloc(val byte) (res *Byte) {
	res = new(Byte)
	res.value = val
	return res
}

// @ pure
// @ requires acc(b.Mem(), _)
// @ decreases
func (b *Byte) ByteValue() byte {
	return /*@ unfolding acc(b.Mem(), _) in @*/ b.value
}

// @ ensures acc(res.Mem(), _)
// @ ensures res.ByteValue() == val
// @ decreases
func ToVal(val byte) (res *Byte) {
	// @ assume -128 <= val && val <= 127
	// @ openDupPkgInv
	// @ unfold acc(StaticInv(), _)
	res = byteCache[val+128]
	// @ fold acc(res.Mem(), _)
	return res
}
