type cell struct {
    val int;
};

func test () {
    c := &cell{42};
		var v int = (*c).val; // problem was here
		assert v == 42;
};