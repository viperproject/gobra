package pkg

const N = 23

func foo() {
	var a [N]int
  assert cap(a) == N
  assert cap(a) == 23
}
