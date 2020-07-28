package pkg

const Answer = 42

func test() {
    assert(Answer == 42)
    //:: ExpectedOutput(assert_error:assertion_error)
    assert(Answer == 41)
}
