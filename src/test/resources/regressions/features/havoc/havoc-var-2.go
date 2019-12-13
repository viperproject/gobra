package pkg;

func foo() {
	x! := 1;
	havoc x;
	//:: ExpectedOutput(assert_error:assertion_error)
	assert x == 1;
};
