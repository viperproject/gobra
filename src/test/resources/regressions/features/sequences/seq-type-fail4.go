package pkg

type Cell struct {
  // fails since the field declaration `xs` is of a ghost type while the field is not marked as being ghost.
  //:: ExpectedOutput(type_error)
  xs seq[int]
}
