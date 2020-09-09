package pkg

requires 0 < len(xs)
func foo(ghost xs seq[bool]) {
  //:: ExpectedOutput(for_loop_error:seq_index_negative_error)
  ghost for ;xs[-1]; { }
}
