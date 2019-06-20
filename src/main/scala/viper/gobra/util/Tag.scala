package viper.gobra.util

object Tag {
//
//  trait Tagged[T] {
//    def tag: T
//  }
//
//  object Tagged {
//    implicit class TaggedMonoidWriter[T, A <: Tagged[T]](x: A)(implicit m: Monoid[T]) {
//      def writer: Writer[T, A] = Writer(x.tag, x)
//    }
//  }
//
//  sealed trait Tagable[+R, T] extends Tagable.TagableFlatMap[T] {
//    def tag(t: T): R
//  }
//
//  sealed trait TaggedQ[+R, T] extends Tagable[R, T] {
//    def value: R
//  }
//
//  object Tagable {
//
//    final case class Closed[+R, T](value: R) extends TaggedQ[R, T] {
//      override def tag(t: T): R = value
//    }
//
//    final case class Open[+R, T](gen: T => R, t: T) extends TaggedQ[R, T] {
//      override def tag(t2: T): R = gen(t2)
//      lazy val value: R = gen(t)
//    }
//
//    final case class Free[+R, T](gen: T => R) extends Tagable[R, T] {
//      override def tag(t: T): R = gen(t)
//    }
//
//    trait TagableFlatMap[T] {
//      type TagableForm[R] = Tagable[R, T]
//      implicit val tagableFlatMap: FlatMap[TagableForm] = new FlatMap[TagableForm]{
//
//        override def map[A, B](fa: Tagable[A, T])(f: A => B): Tagable[B, T] = fa match {
//          case Closed(value) => Closed(f(value))
//          case Open(gen, t) => Open(f compose gen, t)
//          case Free(gen) => Free(f compose gen)
//        }
//
//        override def flatMap[A, B](fa: Tagable[A, T])(f: A => Tagable[B, T]): Tagable[B, T] = fa match {
//          case Closed(value) => f(value)
//          case w@ Open(gen, t) => Open((x: T) => f(gen(x)).tag(x), t)
//          case Free(gen) => Free((x: T) => f(gen(x)).tag(x))
//        }
//
//        override def tailRecM[A, B](a: A)(f: A => Tagable[Either[A, B], T]): Tagable[B, T] = f(a) match {
//          case Closed(Left(next)) => tailRecM(next)(f)
//          case Closed(Right(value)) => Closed(value)
//          case w@ Open(gen, t) => ???
//        }
//      }
//    }
//  }
//
//  trait Untagged[R, T] {
//    def tag(t: T): R
//  }
//
//  sealed trait FuzzyTagged[R, T] extends Untagged[R, T]
//
//  implicit final case class ClosedTagged[R, T](value: R) extends FuzzyTagged[R, T] {
//    override def tag(t: T): R = value
//  }
//
//  implicit final case class OneMoreTagged[R, T](f: T => R) extends FuzzyTagged[R, T] {
//    override def tag(t: T): R = f(t)
//  }
//
//  object Untagged {
//
//    implicit class UntaggedApplicable[R <: Tagged[T], Q, T](f: Untagged[R => Q, T]) extends (R => Q) {
//      override def apply(x: R): Q = f.tag(x.tag)(x)
//
//      def close(x: R): ClosedTagged[Q, T] = f.tag(x.tag)(x)
//
//      def oneMore(x: R)(implicit m: Monoid[T]): OneMoreTagged[Q, T] =
//        (t: T) => f.tag(m.combine(x.tag, t))(x)
//    }
//  }
}


