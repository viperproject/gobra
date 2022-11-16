// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package pkg

// @ ensures res.RecvChannel() && res.RecvGivenPerm()()
func c() (res <-chan struct{})

func test1() {
	<-c()
}

type T interface {
	// @ ensures res.RecvChannel() && res.RecvGivenPerm()()
	getC() (res <-chan struct{})
}

// @ requires t != nil
func test2(t T) {
	<-t.getC()
}
