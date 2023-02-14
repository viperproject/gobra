// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.util.Violation
import viper.silver.ast._

object ViperUtil {

  def toVarDecl(v: LocalVar): LocalVarDecl = {
    LocalVarDecl(v.name, v.typ)(v.pos, v.info, v.errT)
  }

  def toVar(d: LocalVarDecl): LocalVar = {
    d.localVar
  }

  def toSeq(s: Stmt): Seqn = {
    s match {
      case s: Seqn => s
      case _ => Seqn(Vector(s), Vector.empty)(s.pos, s.info, s.errT)
    }
  }

  def bigAnd(it: Iterable[Exp])(pos: Position, info: Info, errT: ErrorTrafo): Exp = {
    it.headOption match {
      case Some(hd) =>
        val tl = it.tail
        tl.foldLeft[Exp](hd) { case (accum, elem) => And(accum, elem)(pos, info, errT) }
      case None => TrueLit()(pos, info, errT)
    }
  }

  def bigOr(it: Iterable[Exp])(pos: Position, info: Info, errT: ErrorTrafo): Exp = {
    it.headOption match {
      case Some(hd) =>
        val tl = it.tail
        tl.foldLeft[Exp](hd) { case (accum, elem) => Or(accum, elem)(pos, info, errT) }
      case None => FalseLit()(pos, info, errT)
    }
  }

  def seqn(ss: Vector[Stmt])(pos: Position, info: Info, errT: ErrorTrafo): Seqn = Seqn(ss, Vector.empty)(pos, info, errT)

  def nop(pos: Position, info: Info, errT: ErrorTrafo): Seqn = Seqn(Vector.empty, Vector.empty)(pos, info, errT)

  def valueAssign(left: Exp, right: Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): AbstractAssign = {
    left match {
      case l: LocalVar => LocalVarAssign(l, right)(pos, info, errT)
      case l: FieldAccess => FieldAssign(l, right)(pos, info, errT)
      case _ => Violation.violation(s"expected vpr variable or field access, but got $left")
    }
  }

  /**
    * TODO: should be removed once the corresponding silver function is fixed, see /silver/issues/610.
    *
    * Returns a list of all undeclared local variables used in this statement.
    * If the same local variable is used with different
    * types, an exception is thrown.
    */
  def undeclLocalVarsGobraCopy(s: Stmt): Seq[LocalVar] = {
    def extractLocal(n: Node, decls: Seq[LocalVarDecl]) =
      n match {
        case l: LocalVar => decls.find(_.name == l.name) match {
          case None => List(l)
          case Some(d) if d.typ != l.typ =>
            sys.error(s"Local variable ${l.name} is declared with type ${d.typ} but used with type ${l.typ}.")
          case _ => Nil
        }
        case _ => Nil
      }

    def combineLists(s1: Seq[LocalVar], s2: Seq[LocalVar]) = {
      for (l1 <- s1; l2 <- s2) {
        if (l1.name == l2.name && l1.typ != l2.typ) {
          sys.error(s"Local variable ${l1.name} is used with different types ${l1.typ} and ${l2.typ}.")
        }
      }
      (s1 ++ s2).distinct
    }

    def addDecls(n: Node, decls: Seq[LocalVarDecl]) = n match {
      case QuantifiedExp(variables, _) =>
        // add quantified variables
        decls ++ variables
      case Seqn(_, scoped) =>
        // add variables defined in scope
        decls ++ scoped.collect { case variable: LocalVarDecl => variable }
      case Let(variable, _, _) =>
        // add defined variable
        decls ++ Seq(variable)
      case _ =>
        decls
    }

    def combineResults(n: Node, decls: Seq[LocalVarDecl], locals: Seq[Seq[LocalVar]]) = {
      locals.fold(extractLocal(n, decls))(combineLists)
    }

    s.reduceWithContext(Nil, addDecls, combineResults)
  }

  /** Adds simple (source) information to a node without source information. */
  def synthesized[T](node: (Position, Info, ErrorTrafo) => T)(comment: String): T =
    node(NoPosition, SimpleInfo(Seq(comment)), NoTrafos)

  // TODO: explain
  def genQuasiHavocStmt(lhs: Option[Exp], e: Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): Stmt = {
    def helper(exp: Exp): (Option[Exp], ResourceAccess) = exp match {
      case f: FieldAccessPredicate => (None, f.loc)
      case PredicateAccessPredicate(predAccess, perm) =>
        // A PrediateAccessPredicate is a PredicateResourceAccess combined with
        // a Permission. Havoc expects a ResourceAccess. To make types match,
        // we must extract the PredicateResourceAccess.
        assert(perm.isInstanceOf[FullPerm]) // only full-permission supported for now // TODO: violation
        (None, predAccess)
      case exp: MagicWand => (None, exp)
      case implies: Implies =>
        val cond1 = implies.left
        val (cond2, r) = helper(implies.right)
        val finalCond = cond2.map(c => And(cond1, c)(pos, info, errT)) orElse Some(cond1)
        (finalCond, r)
      case e => sys.error(s"Can't havoc this kind of expression: $e; ${e.getClass.getName}") // TODO: violation
    }
    e match {
      case Forall(vars, _, body) =>
        val (cond, b) = helper(body)
        val finalCond = (lhs, cond) match {
          case (Some(lhs), Some(cond)) => Some(And(lhs, cond)(pos, info, errT))
          case (lhs, None) => lhs
          case (None, cond) => cond
        }
        Quasihavocall(vars, finalCond, b)(pos, info, errT)
      case e =>
        val (cond, b) = helper(e)
        val finalCond = (lhs, cond) match {
          case (Some(lhs), Some(cond)) => Some(And(lhs, cond)(pos, info, errT))
          case (lhs, None) => lhs
          case (None, cond) => cond
        }
        Quasihavoc(finalCond, b)(pos, info, errT)
    }
  }

}
