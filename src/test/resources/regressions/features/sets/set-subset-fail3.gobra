package pkg

func foo(ghost s set[int]) {
  // fails: identical set type expected
  //:: ExpectedOutput(type_error)
  ghost t := s subset set[bool] { }
}
