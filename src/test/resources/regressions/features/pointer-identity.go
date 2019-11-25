
package pkg;

func test() {
	v := 42;
	ar, br := &v, &v;
	arr, brr := &ar, &br;

	assert ar == br; // satisfied

	//:: ExpectedOutput(assert_error:assertion_error)
	assert arr == brr; // fails
};



