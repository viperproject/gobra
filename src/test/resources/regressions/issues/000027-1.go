type List struct {
  val int
  next *List
}

requires acc(l.val) && acc(l.next) && acc((l.next).val)
ensures acc(l.val) && acc(l.next) && acc(old(l.next).val)
func test(l *List) {
}