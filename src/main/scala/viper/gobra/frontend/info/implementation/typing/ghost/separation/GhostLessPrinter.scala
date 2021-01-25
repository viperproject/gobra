// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost.separation

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignModi}
import viper.gobra.util.Violation

class GhostLessPrinter(classifier: GhostClassifier) extends DefaultPrettyPrinter {

  override def showMember(mem: PMember): Doc = mem match {

    case PMethodDecl(id, rec, args, res, _, body) =>
      super.showMember(
        PMethodDecl(
          id,
          rec,
          filterParamList(args),
          filterResult(res),
          PFunctionSpec(Vector.empty, Vector.empty),
          body.map( b => (PBodyParameterInfo(Vector.empty), b._2) )
        )
      )

    case PFunctionDecl(id, args, res, _, body) =>
      super.showMember(
        PFunctionDecl(
          id,
          filterParamList(args),
          filterResult(res),
          PFunctionSpec(Vector.empty, Vector.empty),
          body.map( b => (PBodyParameterInfo(Vector.empty), b._2) )
        )
      )

    case m if classifier.isMemberGhost(m) => emptyDoc
    case m => super.showMember(m)
  }

  private def filterParamList[T <: PParameter](paras: Vector[T]): Vector[T] =
    paras.filter(!classifier.isParamGhost(_))

  private def filterResult(res: PResult): PResult = {
    val aOuts = res.outs.filter(!classifier.isParamGhost(_))
    PResult(aOuts)
  }

  override def showStmt(stmt: PStatement): Doc = stmt match {

    case PForStmt(pre, cond, post, _, body) =>
      super.showStmt(PForStmt(pre, cond, post, PLoopSpec(Vector.empty), body))

    case p@PAssignment(right, left) =>
      showAssign[PAssignee](right, left, (r, l, _) => p.copy(right = r, left = l))

    case p@PVarDecl(_, right, left, _) =>
      showAssign[PDefLikeId](right, left, (r, l, a) => p.copy(right = r, left = l, addressable = a))

    case p@PShortVarDecl(right, left, _) =>
      showAssign[PUnkLikeId](right, left, (r, l, a) => p.copy(right = r, left = l, addressable = a))

    case n@ PReturn(right) =>
      val gt = classifier.expectedReturnGhostTyping(n)
      val aRight = right.zip(gt.toTuple).filter(!_._2).map(_._1)
      super.showStmt(PReturn(aRight))

    case s if classifier.isStmtGhost(s) => ghostToken
    case _ => super.showStmt(stmt)
  }

  /**
    * Filters and shows assignment, variable declaration, and short variable declaration statements by filtering their lhs and rhs
    */
  private def showAssign[N <: PNode](right: Vector[PExpression], left: Vector[N], copy: (Vector[PExpression], Vector[N], Vector[Boolean]) => PStatement): Doc = {
    def isGhost(l: N): Boolean = l match {
      case l: PIdnNode => classifier.isIdGhost(l)
      case l: PExpression => classifier.isExprGhost(l)
    }

    def handleMultiOrEmptyRhs = {
      val aLeft = left.filter(!isGhost(_))
      if (aLeft.isEmpty) ghostToken else super.showStmt(copy(right, aLeft, aLeft.map(_ => false)))
    }

    StrictAssignModi(left.size, right.size) match {
      case AssignMode.Single =>
        val (aRight, aLeft) = right.zip(left).filter(p => !isGhost(p._2)).unzip
        if (aLeft.isEmpty) ghostToken else super.showStmt(copy(aRight, aLeft, aLeft.map(_ => false)))

      case AssignMode.Multi => handleMultiOrEmptyRhs

      case AssignMode.Error if right.isEmpty => handleMultiOrEmptyRhs

      case AssignMode.Error => errorMsg
    }
  }

  override def showExpr(expr: PExpression): Doc = expr match {

    // invokes of ghost functions and methods should not be printed
    case n: PInvoke => n.base match {
      case e: PExpression if classifier.isExprGhost(e) => ghostToken
      case t: PType if classifier.isTypeGhost(t) => ghostToken
      case _ =>
        val gt = classifier.expectedArgGhostTyping(n)
        val aArgs = n.args.zip(gt.toTuple).filter(!_._2).map(_._1)
        super.showExpr(n.copy(args = aArgs))
    }

    case e: PActualExprProofAnnotation => showExpr(e.op)
    case e if classifier.isExprGhost(e) => ghostToken
    case e => super.showExpr(e)
  }

  override def showType(typ: PType): Doc = typ match {

    case PStructType(clauses) =>
      super.showType(PStructType(filterStructClauses(clauses)))

    case PInterfaceType(embedded, mspecs, pspecs) =>
      super.showType(PInterfaceType(
        filterInterfaceClause(embedded),
        filterInterfaceClause(mspecs),
        filterInterfaceClause(pspecs)
      ))

    case t => super.showType(t)
  }

  private def filterStructClauses[T <: PStructClause](cl: Vector[T]): Vector[T] =
    cl.filter(!classifier.isStructClauseGhost(_))

  private def filterInterfaceClause[T <: PInterfaceClause](cl: Vector[T]): Vector[T] =
    cl.filter(!classifier.isInterfaceClauseGhost(_))

  def ghostToken: Doc = emptyDoc
  private def errorMsg: Nothing = Violation.violation("GhostLessPrinter has to be run after the type check")

}
