package pkg

func foo() {
  ghost var n int
  invariant n == 0
	for i := 0; i < 64; i++ { }
}
