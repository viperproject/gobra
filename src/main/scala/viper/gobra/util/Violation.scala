package viper.gobra.util

import viper.silver.verifier.AbstractError

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

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  def checkAbstractViperErrors(errors: Seq[AbstractError]): Unit = {
    if (errors.nonEmpty) {
      var messages: Vector[String] = Vector.empty
      messages += "Found non-verification-failures"
      messages ++= errors map (_.readableMessage)

      val completeMessage = messages.mkString("\n")
      throw new java.lang.IllegalStateException(completeMessage)
    }
  }
}