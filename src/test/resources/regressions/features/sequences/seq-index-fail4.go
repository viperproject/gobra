package pkg

requires 0 < |xs|;
func foo(ghost xs seq[bool]) {
  // fails: negative index into `xs`
  //:: ExpectedOutput(assignment_error:seq_index_negative_error)
  ghost n := xs[-1]
}
