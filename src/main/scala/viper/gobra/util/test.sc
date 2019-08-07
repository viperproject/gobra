

val a = Vector(1,2,3)
val b = Vector(1,2,3)
val c = Vector(1,2,3,4)
val d = Vector(1,3,2)
val e = Vector(1,2)
val f = Vector(3)

println(a == b)
println(a == c)
println(a == d)
println(a == (e ++ f))
