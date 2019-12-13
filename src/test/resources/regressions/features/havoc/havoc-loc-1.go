package pkg;

type cell struct { f int; };

func foo() {
	v := cell{42};
	havoc v;
	//:: ExpectedOutput(assert_error:assertion_error)
	assert (v.f == 42);
};
