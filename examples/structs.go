
package pkg;

type cell struct{
	val int;
};

func foo() {
	x := &cell{42};
	x.val = 17;
	assert x.val == 17;

	y := cell{42};
	z := y;
	y.val = 17;
	assert y.val == 17 && z.val == 42;

    a := cell{42};
    z = a;
    ap := &a;
    zp := &z;
    a.val = 17;
    assert a.val == 17 && z.val == 42;
};