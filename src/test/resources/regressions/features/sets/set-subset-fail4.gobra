package pkg

func foo(ghost s set[int], ghost t set[int]) {
  // fails: mixing ghost with non-ghost code
  //:: ExpectedOutput(type_error)
  if (s subset t) {
  }
}
