package pkg

requires 0 < |xs|;
func foo(ghost xs seq[bool]) {
  //:: ExpectedOutput(conditional_error:seq_index_negative_error)
  ghost if (xs[-1]) { } else { }
}
