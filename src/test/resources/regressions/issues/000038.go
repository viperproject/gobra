package main

type Rectangle struct {
    Width, Height int
}

func CreateRectangle() (res Rectangle) {
    return Rectangle{Width: 2, Height: 5}
}

func main() {
    r! := CreateRectangle()
}