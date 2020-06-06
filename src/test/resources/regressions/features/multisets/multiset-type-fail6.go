package pkg

func foo(ghost m mset[int], ghost s set[int]) {
  //:: ExpectedOutput(type_error)
  assert m == s
}
