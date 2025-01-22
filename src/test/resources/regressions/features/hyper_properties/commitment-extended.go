// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// ##(--hyperMode extended)

package commitment

// @ requires low(hash)
// @ ensures  res ==> low(value)
func verify(hash int, value int) (res bool) {
    res = (hash == computeHash(value))
    return
}

// @ requires low(hash)
// @ ensures  res ==> low(value)
func verifyWithBranching(hash int, value int) (res bool) {
    // the following if statement succeeds because we enabled the extended
    // SIF encoding that allows branching on non-low conditions
    if hash != computeHash(value) {
        return false
    }
    return true
}

// the following postcondition specifies that the Go function `computeHash` behaves like the
// pure (mathematical) function `hashFn` for which we assume injectivity (see domain below)
// @ ensures res == hashFn(input)
func computeHash(input int) (res int)

/* @
type HashFunction domain {
    func hashFn(int) int
    func invFn(int) int

    axiom { // hashFn is injective
        forall v int :: { hashFn(v) } invFn(hashFn(v)) == v
    }
}
@ */
