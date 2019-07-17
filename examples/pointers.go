package pkg;

func test() {
	v := 42;
	ar, br := &v, &v;
	arr, brr := &ar, &br;

	assert ar == br;
	assert arr == brr;
};

