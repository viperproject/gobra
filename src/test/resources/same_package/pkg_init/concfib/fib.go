// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

// @ dup pkgInvariant acc(StaticInv(), _)

package concfib

import "sync"

var cache /*@@@*/ map[int]int = make(map[int]int)
var lock *sync.Mutex = &sync.Mutex{}

func init() {
	cache[0] = 1
	cache[1] = 1
	// @ fold lockInv!<!>()
	// @ lock.SetInv(lockInv!<!>)
	// @ fold acc(StaticInv(), _)
}

// @ requires  0 <= n
// @ ensures   res == FibSpec(n)
// termination cannot be proven due to calls to Lock()
func FibV1(n int) (res int) {
	// @ openDupPkgInv
	// @ unfold acc(StaticInv(), _)
	lock.Lock()
	// @ unfold lockInv!<!>()
	if v, ok := cache[n]; ok {
		// @ fold lockInv!<!>()
		lock.Unlock()
		return v
	}
	// @ fold lockInv!<!>()
	lock.Unlock()
	v := FibV1(n-1) + FibV1(n-2)
	lock.Lock()
	// @ unfold lockInv!<!>()
	cache[n] = v
	// @ fold lockInv!<!>()
	lock.Unlock()
	return v
}

// @ requires  0 <= n
// @ ensures   res == FibSpec(n)
// termination cannot be proven due to calls to Lock()
func FibV2(n int) (res int) {
	// @ openDupPkgInv
	// @ unfold acc(StaticInv(), _)
	lock.Lock()
	v := fibImpl(n)
	lock.Unlock()
	return v
}

// @ requires  0 <= n
// @ preserves lockInv!<!>()
// @ ensures   res == FibSpec(n)
// @ decreases n
func fibImpl(n int) (res int) {
	// @ unfold lockInv!<!>()
	// @ defer fold lockInv!<!>()
	if v, ok := cache[n]; ok {
		return v
	}
	// @ fold lockInv!<!>()
	v := fibImpl(n-1) + fibImpl(n-2)
	// @ unfold lockInv!<!>()
	cache[n] = v
	return v
}
