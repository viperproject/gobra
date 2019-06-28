package viper.gobra.frontend.info.implementation.property

sealed trait AssignModi

object AssignModi {
  def apply(left: Int, right: Int): AssignModi =
    if (left > 0 && left == right) Single
    else if (left > right && right == 1) Multi
    else Error

  case object Single extends AssignModi
  case object Multi  extends AssignModi
  case object Error  extends AssignModi
}

