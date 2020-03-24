type List struct {
  val int
  next *List
}

requires acc(l.val) && acc(l.next) && acc(l.next.val)
func test(l *List) {
  x := l.next.val
}