package pkg

func example5(ghost xs seq[bool]) {
  // fails since index `-4` into `xs` is negative
  //:: ExpectedOutput(assignment_error:seq_index_negative_error)
  ghost ys := xs[-4 = true]
}
