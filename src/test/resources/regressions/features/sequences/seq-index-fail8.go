package pkg

requires 0 < |xs|;
func foo(ghost xs seq[bool]) {
  //:: ExpectedOutput(conditional_error:seq_index_exceeds_length_error)
  ghost if (xs[15]) { } else { }
}
