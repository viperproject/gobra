package pkg

type cell struct{
		val int;
};

const Answer = cell{42}

func bla() (c cell) {
    c := cell{20}
    assert(c == cell{20})
}

func test() {
    assert(Answer.val == 42)
    assert(Answer == cell{42})
}
