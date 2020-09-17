// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

// You can edit this code!
// Click here and start typing.
package main

import "fmt"

type argSuper interface {
 foo()
}

type argSub struct {
 f int
}

func (s argSub) foo() {}

type interf interface {
  // bar(argSub) argSub // causes error
  // bar(argSuper) argSuper // causes error
  bar(argSuper) argSub 
}

type interfImpl struct {
  f int
}

func (s *interfImpl) bar(x argSuper) argSub { return argSub{42}; }

func main() {
	fmt.Println("Hello, 世界")
	
	var _ argSuper = argSub{42} // checks that argSub is subtype of argSuper
	var _ interf  = &interfImpl{42} // checks that *interfImpl is subtype of interf
}
