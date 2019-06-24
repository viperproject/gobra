package viper.gobra.util

object Violation {

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String): Unit = if (!cond) violation(msg)

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(msg: String): Nothing = throw new java.lang.IllegalStateException(s"Logic error: $msg")

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String, code: => Unit): Unit = if (!cond) { code; violation(msg) }
}