package pkg

func foo(ghost xs seq[int], ghost ys seq[int]) {
  //:: ExpectedOutput(assert_error:assertion_error)
  assert set(xs ++ ys) setminus set(ys) == set(xs)
}
