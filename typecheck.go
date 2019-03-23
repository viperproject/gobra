package wap;

// type t int;

// // type r = int;

// var q int = test(420, x);

// var z, r = (func (x, t int) (int, int) { return x + t, 42; })(3,4);

// var (
// 	q11, w11 int = 9, 19;

// 	q22 int = 8;
// );


type freed struct{

	f, g int;
	k *freed;
};

func (x *freed) sum() int {
	return x.f + x.g;
};

func foo() {

	x := freed{f: 1, g: 2};

	xp := &freed{f: 1, g: 2};

	// q := x.sum(); // causes type error
	q := xp.sum();
};

var f = 5;


// func test(x, y int) int {
// 	v, q := x + y, test(v, x);
// 	return q;
// };

// const x = 42;



