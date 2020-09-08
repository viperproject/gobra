// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package pkg;

type coordinate struct{
    x, y, val int;
};

pred cooMem(self *coordinate) {
    acc(self.x) && acc(self.y) && acc(self.val)
};


requires cooMem(self);
ensures  cooMem(self);
pure func (self *coordinate) value() int {
    return unfolding cooMem(self) in self.val;
};

func test1() {
    coo := &coordinate{1,2,3};
    fold cooMem(coo);
    assert coo.value() == 3;
    unfold cooMem(coo);
    assert coo.val == 3;

    //:: ExpectedOutput(assert_error:assertion_error)
    assert false;
};

