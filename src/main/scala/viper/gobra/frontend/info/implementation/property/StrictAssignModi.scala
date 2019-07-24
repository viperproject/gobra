package viper.gobra.frontend.info.implementation.property

sealed trait AssignMode

object AssignMode {
  case object Single extends AssignMode
  case object Multi  extends AssignMode
  case object Error  extends AssignMode
}

object StrictAssignModi {
  def apply(left: Int, right: Int): AssignMode =
    if (left > 0 && left == right) AssignMode.Single
    else if (left > right && right == 1) AssignMode.Multi
    else AssignMode.Error


}

object NonStrictAssignModi {
  def apply(left: Int, right: Int): AssignMode =
    if (left >= 0 && left == right) AssignMode.Single
    else if (left > right && right == 1) AssignMode.Multi
    else AssignMode.Error
}

