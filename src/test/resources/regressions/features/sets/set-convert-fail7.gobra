package pkg

func foo(ghost xs seq[int], n int, i int) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert n in xs[i:] ==> n in set(xs[:i])
}
