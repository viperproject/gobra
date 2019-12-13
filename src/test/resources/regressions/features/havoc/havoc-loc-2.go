package pkg;

type cell struct { f int; };
type pair struct { a cell; b cell; };

func foo() {
    c1 := cell{1};
    c2 := cell{2};
    v! := pair{c1,c2};
    pv := &((v.a).f);
    havoc v;
    assert(pv == &((v.a).f));
    //:: ExpectedOutput(assert_error:assertion_error)
   	assert ((v.a).f == 1);
    assert ((v.b).f == 2);
};
