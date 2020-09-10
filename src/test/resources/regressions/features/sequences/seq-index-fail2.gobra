package pkg

func foo(ghost xs int) {
  // fails: cannot use an indexed operation on something that isn't indexable
  //:: ExpectedOutput(type_error)
  ghost n := xs[2]
}
