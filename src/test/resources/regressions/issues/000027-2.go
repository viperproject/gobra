type List struct {
  val int
  next *List
}

requires acc(l.val)
ensures acc(l.val) && l.val == old(l).val
func test(l *List) {
}