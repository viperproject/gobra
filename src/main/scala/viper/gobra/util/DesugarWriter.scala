package viper.gobra.util
//import cats.{Monad, Traverse}
//import cats.data.Writer

class DesugarWriter[A] {

  def unit[R](res: R): Writer[R] = Writer(Vector.empty[A], res)

  def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
    val (outs, results) = ws.map(_.run).unzip
    Writer(outs.flatten, results)
  }

  def write(xs: A*): Writer[Unit] = Writer(xs.toVector, ())


  case class Writer[+R](out: Vector[A], res: R) {
    def map[Q](fun: R => Q): Writer[Q] = Writer(out, fun(res))

    def star[Q](fun: Writer[R => Q]): Writer[Q] = Writer(out ++ fun.out, fun.res(res))

    def flatMap[Q](fun: R => Writer[Q]): Writer[Q] = {
      val w = fun(res)
      Writer(out ++ w.out, w.res)
    }

    def run: (Vector[A], R) = (out, res)
    def written: Vector[A] = out
  }
}
