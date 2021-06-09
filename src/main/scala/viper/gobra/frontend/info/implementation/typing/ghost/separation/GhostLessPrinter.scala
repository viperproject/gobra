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
          PFunctionSpec(Vector.empty, Vector.empty, Option.empty),
          body.map( b => (PBodyParameterInfo(Vector.empty), b._2) )
        )
      )

    case PFunctionDecl(id, args, res, _, body) =>
      super.showMember(
        PFunctionDecl(
          id,
          filterParamList(args),
          filterResult(res),
          PFunctionSpec(Vector.empty, Vector.empty ,Option.empty),
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
      super.showStmt(PForStmt(pre, cond, post, PLoopSpec(Vector.empty,Option.empty), body))

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
      if (aLeft.isEmpty) {
        // no variables are left on the left-hand side. However, non-ghost non-pure right-hand side expressions should be kept:
        val nonGhostRhs = right
          .filter(r => !classifier.isExprGhost(r))
          .filter(r => !classifier.isExprPure(r))
        if (nonGhostRhs.isEmpty) ghostToken
        else {
          // directly call showExpr instead of wrapping it in a PExpressionStmt or PSeq to avoid
          // kiama's node-not-in-tree errors:
          ssep(nonGhostRhs map showExpr, line)
        }
      } else super.showStmt(copy(right, aLeft, aLeft.map(_ => false)))
    }

    /** splits v into subvectors that each satisfy or not-satisfy `f` */
    def multispan[T](v: Vector[T], f: T => Boolean): Vector[Vector[T]] = {
      val res = v.span(f)
      val remainder = if (res._2.isEmpty) Vector(res._2) else multispan(res._2, (e: T) => !f(e))
      res._1 +: remainder
    }

    StrictAssignModi(left.size, right.size) match {
      case AssignMode.Single =>
        // we have to distinguish for each pair of elements from left and right whether they remain the original
        // operation (i.e. by calling `copy`) or whether the left-hand side is dropped. In the latter case, the
        // right-hand side has to remain if it's not pure.
        val parts = right.zip(left) map {
          case (r, l) if !isGhost(l) => (Some(r), Some(l)) // lhs is not ghost -> keep them as is
          case (r, _) if !classifier.isExprGhost(r) && classifier.isExprPure(r) => (None, None) // lhs is ghost, rhs is not ghost but pure -> lhs and rhs can be erased
          case (r, _) if !classifier.isExprGhost(r) => (Some(r), None) // lhs is ghost, rhs is not ghost and not pure -> keep only rhs
          case _ => (None, None) // lhs and rhs are ghost -> lhs and rhs can be erased
        } filter {
          // filter parts of the assignment for which we neither keep the lhs nor rhs:
          case (None, None) => false
          case _ => true
        }
        // group parts by their structure to combine as many parts as possible into a single statement
        val hasRhsAndLhs: ((Option[PExpression], Option[N])) => Boolean = {
          case (Some(_), Some(_)) => true
          case _ => false
        }
        val partsSubSequences = multispan[(Option[PExpression], Option[N])](parts, hasRhsAndLhs)
            .filter(_.nonEmpty)
        val docs = partsSubSequences flatMap (subsequence => {
          val keepBoth = hasRhsAndLhs(subsequence.head)
          val aRight = subsequence.flatMap(_._1)
          if (keepBoth) {
            val aLeft = subsequence.flatMap(_._2)
            Vector(super.showStmt(copy(aRight, aLeft, aLeft.map(_ => false))))
          } else {
            // directly call showExpr instead of wrapping it in a PExpressionStmt or PSeq to avoid
            // kiama's node-not-in-tree errors:
            aRight map showExpr
          }
        })
        // line-separate the created docs:
        ssep(docs, line)

      case AssignMode.Multi => handleMultiOrEmptyRhs

      case AssignMode.Variadic if right.isEmpty => handleMultiOrEmptyRhs

      case AssignMode.Error if right.isEmpty => handleMultiOrEmptyRhs

      case AssignMode.Error | AssignMode.Variadic => errorMsg
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
