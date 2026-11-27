// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._
import viper.silver.plugin.sif.{SIFLowExp, SIFRelExp}

/**
  * The multiplicity rules of the assertion language: an expression is *rigid* if it has one value for the tuple of
  * executions, and *unary* if it has one value per execution. Used by the static rules ([[HyperfuncsChecks]]: the body
  * of a non-boolean hyperfunction must be rigid) and by the encoding ([[HyperfuncEncoding]]: rigid atoms are evaluated
  * once, unary atoms in each execution).
  *
  * @param hyper the names of the hyperfunctions of `p`
  */
class Multiplicity(p: Program, hyper: Set[String]) {

  private def isHeapDependentFunction(name: String): Boolean =
    p.findFunctionOptionally(name).exists(f => f.pres.exists(_.isHeapDependent(p)))

  /** Is `e` rigid, where `rigid` are the names of the rigid variables in scope? */
  def isRigid(e: Exp, rigid: Set[String]): Boolean = e match {
    case _: Literal | _: Result => true
    case _: SIFRelExp | _: HFRelExp | _: SIFLowExp | _: HFActiveExp | _: HFExecLit => true
    case f: FuncApp if hyper(f.funcname) => true
    case v: LocalVar => rigid(v.name)
    case f: FuncApp => !isHeapDependentFunction(f.funcname) && f.args.forall(isRigid(_, rigid))
    case _: FieldAccess | _: LocationAccess | _: AccessPredicate | _: Old | _: LabelledOld | _: CurrentPerm => false
    case q: QuantifiedExp => isRigid(q.exp, rigid ++ q.variables.map(_.name))
    case Let(v, e1, body) => isRigid(body, if (isRigid(e1, rigid)) rigid + v.name else rigid - v.name)
    case u: Unfolding => isRigid(u.body, rigid)
    case e => e.subExps.forall(isRigid(_, rigid))
  }
}
