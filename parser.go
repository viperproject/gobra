// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package wap;



type t int;

type r = int;

var q int = test(v, x);

var x = (func (x, t int) { return x + t; })(3,4);

var (
	q, w int = 9, 19;

	q int = 8;
);


type freed struct{

	f, g int;
	k *freed;
};

func test(x, y int) int {
	var v int = x + y;
	var q int = test(v, x);
	return q;
};



