package main

type Rectangle struct {
    Width, Height int
}

ghost
requires acc(r.Height)
ensures acc(r.Height)
pure func GetHeight(r *Rectangle) (res int) {
    return r.Height
}

func main() {
    r! := Rectangle{Width: 2, Height: 5}
    h := GetHeight(&r)
    assert h == GetHeight(&r) && h == 5
}
