package viper.gobra.ast.internal

import viper.silver.ast.SourcePosition
import cats.Monoid
import viper.silver.{ast => vpr}

trait Sourced {
  def src: Source
  protected def src_=(src: Source): Unit
}

object Sourced {
  def unsource[T <: Sourced](f: Factory[T]): Source => T = { src =>
    val x = f.create
    x.src = src
    x
  }
}

case class Factory[T](f: () => T) {
  def create: T = f()
}



sealed trait Source {
  def origins: Vector[Origin]
  // TODO: unify position setting
  def vprSrc: vpr.Position = if (origins.isEmpty) vpr.NoPosition else origins.head.pos
}

object Source {

  final case object Internal extends Source {
    override lazy val origins: Vector[Origin] = Vector.empty
  }

  final case class Single(src: Origin) extends Source {
    override lazy val origins: Vector[Origin] = Vector(src)
  }

  final case class Multi(origins: Vector[Origin]) extends Source

  implicit val sourceMonoid: Monoid[Source] = new Monoid[Source]{
    override def empty: Source = Internal
    override def combine(x: Source, y: Source): Source = (x.origins ++ y.origins) match {
      case os if os.isEmpty => Internal
      case os if os.size == 1 => Single(os.head)
      case os => Multi(os)
    }
  }
}

case class Origin(pos: SourcePosition)



