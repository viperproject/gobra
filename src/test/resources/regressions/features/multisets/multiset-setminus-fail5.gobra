package pkg

func foo(ghost m1 mset[int], ghost m2 mset[int]) {
  // fails: the minus operator shouldn't be defined for multisets (at the moment at least)
  //:: ExpectedOutput(type_error)
  ghost m3 := m1 - m2
}
