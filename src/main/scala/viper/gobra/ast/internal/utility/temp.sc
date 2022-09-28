case class Foo(x: Int, y: String)
case class Bar(x: Foo, y: Int)(n: String)

val a = Foo(4, "Hey")
val b = Bar(a, 5)("Boo")

a.productIterator.toSeq
b.productIterator.toSeq
