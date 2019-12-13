package pkg;

type cell struct { f int; };

func foo() {
	v := &cell{42};
	bar(v);
	assert (v.f == 42);
};

func bar(v *cell) {
    //:: ExpectedOutput(havoc_error:permission_error)
    havoc v.f;
};