package pkg

func foo() {
	x, y, z := 1, 2, 3
	y, z, x = z, x, y
	assert x == 2 && y == 3 && z == 1
}