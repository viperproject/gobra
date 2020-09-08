package main

func foo() {
  r := Rectangle{Width: 2, Height: 5}
  assert r.NewWithWidth(10) == Rectangle{Width: 10, Height: 5}
}

type Rectangle struct {
    Width, Height int
}

ensures res == r.Width && r.Height == x.Height
func (x Rectangle) NewWithWidth(res int) (r Rectangle) {
    return Rectangle{res, x.Height}
}


pure func (x Rectangle) NewWithWidth2(res int) (r Rectangle) {
    return Rectangle{res, x.Height}
}