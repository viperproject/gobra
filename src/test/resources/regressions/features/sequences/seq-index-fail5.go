package pkg

requires |xs| == 2
func foo(ghost xs seq[bool]) {
  // fails: index into `xs` exceeds length
  //:: ExpectedOutput(assignment_error:seq_index_exceeds_length_error)
  ghost n := xs[42]
}
