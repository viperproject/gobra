package main


type cell struct{
	val int
}

requires acc(x.val)
ensures  acc(x.val)
pure func get(x *cell) int {
  return x.val
}

func test() {
  x := &cell{42}
  assert get(x) == 42
}



