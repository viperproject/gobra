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

    case PMethodDecl(id, rec, args, res, spec, body) =>
      super.showMember(PMethodDecl(id, rec, filterParamList(args), filterResult(res), PFunctionSpec(Vector.empty, Vector.empty), body))

    case PFunctionDecl(id, args, res, _, body) =>
      super.showMember(PFunctionDecl(id, filterParamList(args), filterResult(res), PFunctionSpec(Vector.empty, Vector.empty), body))

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

    case PAssignment(right, left) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isExprGhost(p._2)).unzip
          if (aLeft.isEmpty) ghostToken else super.showStmt(PAssignment(aRight, aLeft))

        case AssignMode.Multi =>
          val aLeft = left.filter(!classifier.isExprGhost(_))
          if (aLeft.isEmpty) ghostToken else super.showStmt(PAssignment(right, aLeft))

        case AssignMode.Error => errorMsg
      }

    case PShortVarDecl(right, left, _) =>
      StrictAssignModi(left.size, right.size) match {
        case AssignMode.Single =>
          val (aRight, aLeft) = right.zip(left).filter(p => !classifier.isIdGhost(p._2)).unzip
          if (aLeft.isEmpty) ghostToken else super.showStmt(PShortVarDecl(aRight, aLeft, aLeft.map(_ => false)))

        case AssignMode.Multi =>
          val aLeft = left.filter(!classifier.isIdGhost(_))
          if (aLeft.isEmpty) ghostToken else super.showStmt(PShortVarDecl(right, aLeft, aLeft.map(_ => false)))

        case AssignMode.Error => errorMsg
      }

    case n@ PReturn(right) =>
      val gt = classifier.expectedReturnGhostTyping(n)
      val aRight = right.zip(gt.toTuple).filter(!_._2).map(_._1)
      super.showStmt(PReturn(aRight))

    case s if classifier.isStmtGhost(s) => ghostToken
    case s => super.showStmt(stmt)
  }

  override def showExpr(expr: PExpression): Doc = expr match {

    case n: PInvoke =>
      val gt = classifier.expectedArgGhostTyping(n)
      val aArgs = n.args.zip(gt.toTuple).filter(!_._2).map(_._1)
      super.showExpr(n.copy(args = aArgs))

    case e: PActualExprProofAnnotation => showExpr(e.op)
    case e if classifier.isExprGhost(e) => "<removed expr>" // should not be printed
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
