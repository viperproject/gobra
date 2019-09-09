

val a = Vector(1,2,3,4)
var b = List.empty[Int]

a foreach (x => b = x :: b )

b