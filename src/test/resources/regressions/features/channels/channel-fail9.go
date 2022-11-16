// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package pkg

func c() <-chan struct{}

func test1() {
	//:: ExpectedOutput(precondition_error)
	<-c()
}

type T interface {
	getC() <-chan struct{}
}

func test2(t T) {
	//:: ExpectedOutput(precondition_error)
	<-t.getC()
}
