
package pkg;

type cell struct{
	val int;
};

requires acc(x.val) && acc(y.val);
requires x.val == a && y.val == b;
ensures  acc(x.val) && acc(y.val);
ensures  x.val == b && y.val == a;
func swap1(x, y *cell, ghost a, b int) {
	x.val, y.val = y.val, x.val;
};

func client() {
    x := cell{42};
    y := cell{17};
    swap1(&x, &y, 42, 17);
    assert x == cell{17} && y.val == 42;

    //:: ExpectedOutput(assert_error:assertion_error)
    assert false;
};



