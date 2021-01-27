// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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

  def expectedArgGhostTyping(invk: PInvoke): GhostType

  def isExprPure(expr: PExpression): Boolean
}
