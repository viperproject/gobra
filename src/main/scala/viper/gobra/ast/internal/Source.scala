package viper.gobra.ast.internal

import viper.silver.ast.SourcePosition

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

sealed trait Source

object Source {
  case class Single(src: Origin) extends Source
  case class Multi(src: Origin) extends Source
  case object Internal extends Source
}

case class Origin(code: String, pos: SourcePosition)

