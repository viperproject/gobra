package viper.gobra.util

import scala.language.higherKinds

object WriterUtil1 {

  trait Monoid[X] {
    def empty: X
    def combine(l: X, r: X): X

    def combine(xs: Iterable[X]): X = xs.foldLeft(empty)(combine)
  }

  abstract class WriterCompanion[S, I[_]](implicit m: Monoid[S]) {
    def create[R](newSum: S, newRes: R): I[R]
    def destruct[R](writer: I[R]): (S, R)

    def inject[R](sum: S, writer: I[R]): I[R] = {
      val (otherSum, otherRes) = destruct(writer)
      create(m.combine(sum, otherSum), otherRes)
    }

    def unit[R](res: R): I[R] = create(m.empty, res)
    def sequence[R](ws: Vector[I[R]]): I[Vector[R]] = {
      val (sums, ress) = (ws map destruct).unzip
      create(m.combine(sums), ress)
    }
  }

  abstract class Writer[S, +R, I[_]](implicit wc: WriterCompanion[S, I]) {
    this: I[R] =>

    def run: (S, R) = wc.destruct(this)

    def map[Q](fun: R => Q): I[Q] = {
      val (sum, res) = run
      wc.create(sum, fun(res))
    }

    def flatMap[Q](fun: R => I[Q]): I[Q] = {
      val (sum, res) = run
      val newWriter = fun(res)
      wc.inject(sum, newWriter)
    }
  }

  trait SplitSum[H, T] {
    def unzip: (H, T) = (head, tail)

    def head: H
    def tail: T
  }

  trait SplitSumCompanion[H, T, S] {
    def create(head: H, tail: T): S
    def inject(head: H, tail: T, sum: S): S
  }

  def inferSplitSumMonoid[H, T, S <: SplitSum[H, T]](implicit hm: Monoid[H], tm: Monoid[T], ssc: SplitSumCompanion[H, T, S]): Monoid[S] =
    new Monoid[S] {
      override def empty: S = ssc.create(hm.empty, tm.empty)
      override def combine(l: S, r: S): S = ssc.inject(l.head, l.tail, r)
    }

  abstract class Packing[H, R, T, Q, J[_]](implicit wc: WriterCompanion[T, J]) {

    def pack(head: H, res: R): Q

    def pack[S <: SplitSum[H, T], I[_]](writer: Writer[S, R, I]): J[Q] = {
      val (sum, res) = writer.run
      val (head, tail) = sum.unzip
      wc.create(tail, pack(head, res))
    }
  }
}
