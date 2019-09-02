package viper.gobra.util

object Violation {

  abstract class GobraException(msg: String) extends RuntimeException(msg)

  class LogicException(msg: String) extends GobraException(msg)

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String): Unit = if (!cond) violation(msg)

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(msg: String): Nothing = throw new LogicException(s"Logic error: $msg")

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String, code: => Unit): Unit = if (!cond) { code; violation(msg) }
}