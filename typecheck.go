// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package wap;

type t int;

// type r = int;

var q int = test(420, x);

var z, r = (func (x, t int) (int, int) { return x + t, 42; })(3,4);

var (
	q11, w11 int = 9, 19;

	q22 int = 8;
);


type freed struct{

	f, g int;
	k *freed;
};

func (x *freed) sum() int {
	return x.f + x.g;
};

func foo() {

	x := freed{f: 1, g: 2};

	xp := &freed{f: 1, g: 2};

	// q := x.sum(); // causes type error
	q := xp.sum();
};

var f = 5;


func test(x, y int) int {
	v, q := x + y, test(y, x);
	return q;
};

const x = 42;



