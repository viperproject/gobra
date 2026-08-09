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

  /**
    * Drops auto-inferred trigger sets that consist solely of bounded-int `from` bridge
    * applications (see [[viper.gobra.translator.Names.boundedIntFrom]]), provided at least one
    * other trigger set remains.
    *
    * Rationale: quantifiers over bounded-int values compile comparisons like `0 <= j` to
    * `k$from(j) >= 0`, so Silver's trigger inference discovers `{ k$from(j) }` as a valid
    * trigger set — but `from` appears on essentially every bounded term in a trace, making
    * this trigger fire for every bounded ground term ever created. That eagerness is a
    * performance disaster (each loop iteration mints fresh terms, each of which re-instantiates
    * every such quantifier). Before the domain encoding, plain integer comparisons contributed
    * no trigger candidates at all, so inference picked heap- or container-based terms
    * (`m[j]`, `j in domain(m)`, …); dropping the `from`-only sets restores exactly that
    * behavior. If `from`-sets are the only candidates, they are kept — a quantifier without
    * triggers would be worse (the backend or Z3 would infer something at least as permissive).
    */
  def dropBoundedFromOnlyTriggers[T <: QuantifiedExp](q: T): T = {
    def isFromApp(e: Exp): Boolean = e match {
      case app: DomainFuncApp =>
        app.funcname.endsWith("$from") && app.domainName.startsWith("Bounded_")
      case _ => false
    }
    q match {
      case forall: Forall =>
        val (fromOnly, rest) = forall.triggers.partition(t => t.exps.nonEmpty && t.exps.forall(isFromApp))
        if (rest.nonEmpty && fromOnly.nonEmpty)
          forall.copy(triggers = rest)(forall.pos, forall.info, forall.errT).asInstanceOf[T]
        else q
      case exists: Exists =>
        val (fromOnly, rest) = exists.triggers.partition(t => t.exps.nonEmpty && t.exps.forall(isFromApp))
        if (rest.nonEmpty && fromOnly.nonEmpty)
          exists.copy(triggers = rest)(exists.pos, exists.info, exists.errT).asInstanceOf[T]
        else q
      case _ => q
    }
  }
}
