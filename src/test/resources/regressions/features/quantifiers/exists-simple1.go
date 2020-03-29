package pkg;

requires exists m int :: true
func test1 () { }

requires exists x int, y int :: x < y
requires exists x int, y int :: { x < y } x < y
func test2 () { }

func test3() {
  assert exists n int :: true
  assert exists n int :: { n } true
}

requires exists x int, y int :: { x, y } x < y
func test4 () { }