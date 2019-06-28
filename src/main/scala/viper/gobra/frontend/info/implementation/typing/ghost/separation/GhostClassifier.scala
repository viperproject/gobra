package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._

trait GhostClassifier {

  def isMemberGhost(member: PMember): Boolean

  def isStmtGhost(stmt: PStatement): Boolean

  def isExprGhost(expr: PExpression): Boolean = exprGhostTyping(expr).isGhost

  def exprGhostTyping(expr: PExpression): GhostType

  def isTypeGhost(typ: PType): Boolean

  def isIdGhost(id: PIdnNode): Boolean

  def isParamGhost(param: PParameter): Boolean

  def isStructClauseGhost(clause: PStructClause): Boolean

  def isInterfaceClauseGhost(clause: PInterfaceClause): Boolean

  def expectedReturnGhostTyping(ret: PReturn): GhostType

  def expectedArgGhostTyping(call: PCall): GhostType

  def expectedArgGhostTyping(call: PConversionOrUnaryCall): GhostType
}
