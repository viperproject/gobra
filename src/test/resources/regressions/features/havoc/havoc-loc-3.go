package pkg;

type cell struct { f int; };
type pair struct { a cell; b cell; };
/*
func foo() {
	v! := &pair{cell{1}, cell{2}};
	//havoc v;
	//ExpectedOutput(assert_error:assertion_error)
	assert ((v.a).f == 1);
	assert ((v.b).f == 2);
};
*/
func foo() {
    c1 := cell{1};
    c2 := cell{2};
    v! := pair{c1,c2};
    pv := &v;
    w := pv;
    havoc pv;
    assert ((w.a).f == 1);
    assert ((w.b).f == 2);
    //:: ExpectedOutput(assert_error:permission_error)
   	assert ((pv.a).f == 1);
    assert ((pv.b).f == 2);
};
