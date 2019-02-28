
package wap;



type t int;

type r = int;

var q int = test(v, x);

var x = (func (x, t int) { return x + t; })(3,4);

var (
	q, w int = 9, 19;

	q int = 8;
);


type freed struct{

	f, g int;
	k *freed;
};

func test(x, y int) int {
	var v int = x + y;
	var q int = test(v, x);
	return q;
};



