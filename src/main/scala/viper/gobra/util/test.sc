

val x: Vector[Any] = Vector(Vector(4))

x match {
  case Seq(x: Seq[Int@unchecked]) => println(x)
}