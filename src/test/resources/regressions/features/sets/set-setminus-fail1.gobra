package pkg

func foo(ghost s seq[int], ghost t set[int]) {
  // fails: `s` is a sequence rather than a set
  //:: ExpectedOutput(type_error)
  ghost u := s setminus t
}
