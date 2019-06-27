package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._

trait GhostClassifier {

  def isMemberGhost(member: PMember): Boolean

  def isStmtGhost(stmt: PStatement): Boolean

  def isExprGhost(expr: PExpression): Boolean

  def isIdGhost(id: PIdnNode): Boolean

  def isParameterGhost(param: PParameter): Boolean
}
