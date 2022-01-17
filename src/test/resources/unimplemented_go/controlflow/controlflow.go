package controlflow;

ensures res > 0
func a() (res int) {
    for {continue}
}