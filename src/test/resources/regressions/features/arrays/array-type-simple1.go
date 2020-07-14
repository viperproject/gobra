package pkg

func test1(a [12]int) {
}

func test2() {
	var a [42]bool
}

func test3(a [12]int) {
	var b [12]int
	b = a
}

requires a == b
func test4(a [2]int, b [2]int) {
}
