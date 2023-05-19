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

  case object QuasiHavoc {
    private def extractResourceAcc(exp: Exp)(pos: Position, info: Info, errT: ErrorTrafo): (Option[Exp], Option[ResourceAccess]) =
      exp match {
        case FieldAccessPredicate(loc, _: FullPerm) =>
          // only full-permission supported, it is not clear whether QuasiHavoc does what is intended when
          // the permission amount is different.
          (None, Some(loc))
        // TODO: Not sure if the following would behave as intended for predicates:
        // case PredicateAccessPredicate(predAccess, _: FullPerm) =>
        //   (None, Some(predAccess))
        case exp: MagicWand =>
          (None, Some(exp))
        case implies: Implies =>
          val cond1 = implies.left
          val (cond2, r) = extractResourceAcc(implies.right)(pos, info, errT)
          val finalCond = cond2.map(c => And(cond1, c)(pos, info, errT)) orElse Some(cond1)
          (finalCond, r)
        case _ =>
          (None, None)
      }

    /**
      * If possible, generates a quasihavoc of expression 'e' (or quasihavocall if 'e' is a QP). The quasichavoc can
      * be conditional on the value of 'cond'.
      */
    def apply(cond: Option[Exp], e: Exp)(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): Option[Stmt] =
      e match {
        case Forall(vars, _, body) =>
          val (additionalCond, resAccOpt) = extractResourceAcc(body)(pos, info, errT)
          resAccOpt map { b: ResourceAccess =>
            val finalCond = (cond, additionalCond) match {
              case (Some(cond1), Some(cond2)) => Some(And(cond1, cond2)(pos, info, errT))
              case (cond1, None) => cond1
              case (None, cond2) => cond2
            }
            Quasihavocall(vars, finalCond, b)(pos, info, errT)
          }

        case e =>
          val (additionalCond, resAccOpt) = extractResourceAcc(e)(pos, info, errT)
          resAccOpt map { b: ResourceAccess =>
            val finalCond = (cond, additionalCond) match {
              case (Some(cond1), Some(cond2)) => Some(And(cond1, cond2)(pos, info, errT))
              case (cond1, None) => cond1
              case (None, cond2) => cond2
            }
            Quasihavoc(finalCond, b)(pos, info, errT)
          }
      }
    }
}
