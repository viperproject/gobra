package main

type List struct {
  val int
  next *List
}

//:: ExpectedOutput(type_error)
requires acc(l.val) && acc(l.next) && acc(old(l.next).val)
func test(l *List) {
  x := l.next.val
}