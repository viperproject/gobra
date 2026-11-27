// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast.{Exp, IntLit, NoPosition, Position}
import viper.silver.parser._
import viper.silver.plugin.sif.SIFRelExp

case object PActiveKeyword extends PKw("active") with PKeywordLang

private object PExecIndex {
  def isExecType(t: PType): Boolean = t match {
    case d: PDomainType => d.domain.name == HyperfuncsAst.ExecTypeName && d.typeArgs.isEmpty
    case _ => false
  }

  /**
    * Checks an execution index: an integer literal 0 or 1 (the SIF encoding relates two executions) or an expression
    * of type Exec.
    */
  def check(idx: PExp, t: TypeChecker, what: String): Option[String] = idx match {
    case PIntLit(i) => if (i == 0 || i == 1) None else Some(s"The execution of $what must be 0 or 1, but is $i.")
    case _ =>
      t.checkTopTyped(idx, None)
      if (isExecType(idx.typ)) None
      else Some(s"The execution of $what must be an integer literal or an expression of type ${HyperfuncsAst.ExecTypeName}, " +
        s"but has type ${idx.typ.pretty}.")
  }

  def translate(idx: PExp, t: Translator): Exp = idx match {
    case l@PIntLit(i) => HFExecLit(i)(t.liftPos(l))
    case _ => t.exp(idx)
  }
}

/** `rel(e, i)`, where `i` is an integer literal or an expression of type Exec. */
case class PHFRelExp(e: PExp, idx: PExp)(val pos: (Position, Position) = (NoPosition, NoPosition)) extends PExtender with PExp {
  override def typeSubstitutions: collection.Seq[PTypeSubstitution] = e.typeSubstitutions
  override def forceSubstitution(ts: PTypeSubstitution): Unit = e.forceSubstitution(ts)

  override def typecheck(t: TypeChecker, n: NameAnalyser): Option[Seq[String]] = {
    t.checkTopTyped(e, None)
    typ = e.typ
    PExecIndex.check(idx, t, "rel").map(Seq(_))
  }

  override def typecheck(t: TypeChecker, n: NameAnalyser, expected: PType): Option[Seq[String]] = {
    typecheck(t, n) match {
      case None if expected != e.typ && expected != TypeHelper.Impure && e.typ.isValidOrUndeclared =>
        t.checkTopTyped(e, Some(expected))
        None
      case res => res
    }
  }

  override def translateExp(t: Translator): Exp = idx match {
    case l@PIntLit(i) => SIFRelExp(t.exp(e), IntLit(i)(t.liftPos(l)))(t.liftPos(this))
    case _ => HFRelExp(t.exp(e), t.exp(idx))(t.liftPos(this))
  }
}

/** `active(i)`: execution `i` is active at the current program point. */
case class PHFActiveExp(idx: PExp)(val pos: (Position, Position) = (NoPosition, NoPosition)) extends PExtender with PExp {
  typ = TypeHelper.Bool

  override def typeSubstitutions: collection.Seq[PTypeSubstitution] = Seq(PTypeSubstitution.id)
  override def forceSubstitution(ts: PTypeSubstitution): Unit = {}

  override def typecheck(t: TypeChecker, n: NameAnalyser): Option[Seq[String]] =
    PExecIndex.check(idx, t, "active").map(Seq(_))

  override def typecheck(t: TypeChecker, n: NameAnalyser, expected: PType): Option[Seq[String]] =
    typecheck(t, n).orElse(
      if (expected == TypeHelper.Bool || expected == TypeHelper.Impure) None
      else Some(Seq(s"Expected type ${expected.pretty}, but got Bool.")))

  override def translateExp(t: Translator): Exp = HFActiveExp(PExecIndex.translate(idx, t))(t.liftPos(this))
}

/**
  * An integer literal passed for a hyperfunction parameter of type Exec. The plugin replaces such literals before
  * name resolution, see [[HyperfuncsPlugin.beforeResolve]]. `execType` is a type node `Exec`, resolved by the name
  * analysis like any other type.
  */
case class PHFExecLit(lit: PIntLit, execType: PDomainType)(val pos: (Position, Position) = (NoPosition, NoPosition))
  extends PExtender with PExp {
  override def typeSubstitutions: collection.Seq[PTypeSubstitution] = Seq(PTypeSubstitution.id)
  override def forceSubstitution(ts: PTypeSubstitution): Unit = {}

  override def typecheck(t: TypeChecker, n: NameAnalyser): Option[Seq[String]] = {
    t.check(execType)
    typ = execType
    if (lit.i == 0 || lit.i == 1) None else Some(Seq(s"An execution must be 0 or 1, but is ${lit.i}."))
  }

  override def typecheck(t: TypeChecker, n: NameAnalyser, expected: PType): Option[Seq[String]] =
    typecheck(t, n).orElse(
      if (PExecIndex.isExecType(expected) || expected == TypeHelper.Impure) None
      else Some(Seq(s"Expected type ${expected.pretty}, but got ${HyperfuncsAst.ExecTypeName}.")))

  override def translateExp(t: Translator): Exp = HFExecLit(lit.i)(t.liftPos(this))
}
