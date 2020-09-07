package bar2

type Rectangle struct {
    Width, Height int
}

ensures acc(res.Width) && acc(res.Height)
ensures res.Width == 2 && res.Height == 5
func CreateRectangle() (res *Rectangle) {
    return &Rectangle{Width: 2, Height: 5}
}

requires acc(r.Width) && acc(r.Height)
ensures acc(r.Width) && acc(r.Height)
ensures res == r.Width * r.Height
ensures old(r.Width) == r.Width && old(r.Height) == r.Height
func (r *Rectangle) Area() (res int) {
    return r.Width * r.Height
}
