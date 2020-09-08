package main

func foo() {
  r := Rectangle{Width: 2, Height: 5}
  assert Area(r) == 10
}

type Rectangle struct {
    Width, Height int
}

ensures res == r.Width * r.Height
func Area(r Rectangle) (res int) {
    return r.Width * r.Height
}


pure func Area2(r Rectangle) (res int) {
    return r.Width * r.Height
}