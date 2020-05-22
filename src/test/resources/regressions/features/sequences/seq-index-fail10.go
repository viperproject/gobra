package pkg

requires 0 < |xs|;
func foo(ghost xs seq[bool]) {
  //:: ExpectedOutput(for_loop_error:seq_index_exceeds_length_error)
  ghost for ;xs[42]; { }
}
