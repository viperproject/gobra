package pkg

func foo(ghost xs int) {
  // fails: should not parse a sequence update with no update clauses
  //:: ExpectedOutput(parser_error)
  ghost ys := xs[]
}
