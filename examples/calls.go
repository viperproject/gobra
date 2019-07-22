package pkg;


ensures y == x + 42;
func foo(x int) (y int) {
	return x + 42;
};

func client() {
	x := 13;
	z := foo(x) + 14;
	assert z == 69;
};



