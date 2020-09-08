package pkg;

type cell struct{
	val int;
};

func foo() {
	x := &cell{42};
	assert acc(x.val);

	y! := cell{42};
	assert acc(y.val);

	p := &y;
	assert acc(p.val);

	//:: ExpectedOutput(assert_error:assertion_error)
	assert false;
};
