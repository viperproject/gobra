package pkg

type Point struct {
  x int
  y int
}

func foo() {
  // sequences of custom defined types are not supported currently
  //:: ExpectedOutput(type_error)
  ghost var xs seq[Point]
}
