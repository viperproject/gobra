// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package pkg;


ensures y == x + 42;
func foo(x int) (y int) {
	return x + 42;
};

func client() {
	x := 13;
	z := foo(x) + 14;
	assert z == 69;
};


requires x >= 0;
func fib(x int) (y int) {
	if x == 0 {
		return 0;
	} else if x == 1 {
		return 1;
	} else {
		a, b := fib(x-1), fib(x-2);
		return a + b;
	};
};



