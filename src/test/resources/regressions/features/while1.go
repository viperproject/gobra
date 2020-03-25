package pkg;



requires x >= 0;
ensures  z == x*y;
func test(x, y int) (z int) {

	invariant z == i*y;
	invariant i <= x;
	for i := 0; i < x; i += 1 {
		z += y;
	};
};



