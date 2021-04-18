// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.Source.Verifier
import viper.gobra.reporting.{Source, VerificationError}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Conditions
import viper.gobra.translator.util.FunctionGeneratorWithoutContext
import viper.silver.verifier.ErrorReason
import viper.silver.{ast => vpr}
import viper.silver.verifier.{errors => vprerr}

class ConditionsImpl extends Conditions {

  override def finalize(col: Collector): Unit = {
    if (isAssertFuncUsed) {
      col.addMember(assertFunction)
    }
    typedAssertFunc.finalize(col)
  }

  /**
    * Generates:
    * function assertArg1(b: Boolean): Boolean
    *   requires b
    * { true }
    */
  private val assertFunction: vpr.Function = {
    val b = vpr.LocalVarDecl("b", vpr.Bool)()
    vpr.Function(
      name = Names.assertFunc,
      formalArgs = Seq(b),
      typ = vpr.Bool,
      pres = Seq(b.localVar),
      posts = Seq.empty,
      body = Some(vpr.TrueLit()())
    )()
  }
  private var isAssertFuncUsed: Boolean = false

  /**
    * Generates:
    * function assertArg2(b: Boolean, y: T): T
    *   requires b
    * { y }
    */
  private val typedAssertFunc = new FunctionGeneratorWithoutContext[vpr.Type] {
    override def genFunction(t: vpr.Type): vpr.Function = {
      val b = vpr.LocalVarDecl("b", vpr.Bool)()
      val y = vpr.LocalVarDecl("y", t)()
      vpr.Function(
        name = Names.typedAssertFunc(t),
        formalArgs = Seq(b, y),
        typ = t,
        pres = Seq(b.localVar),
        posts = Seq.empty,
        body = Some(y.localVar)
      )()
    }
  }

  /** Returns true, but asserts that the argument holds. */
  override def assert(x: vpr.Exp): vpr.Exp = {
    isAssertFuncUsed = true
    vpr.FuncApp(assertFunction, Seq(x))(x.pos, x.info, x.errT)
  }

  override def assert(x: vpr.Exp, trans: (Verifier.Info, ErrorReason) => VerificationError): (vpr.Exp, ErrorTransformer) = {
    isAssertFuncUsed = true
    val res = vpr.FuncApp(assertFunction, Seq(x))(x.pos, x.info, x.errT)
    val errorT: ErrorTransformer = {
      case e@ vprerr.PreconditionInAppFalse(Source(info), reason, _) if e causedBy res =>
        trans(info, reason)
    }
    (res, errorT)
  }

  /** Returns 'exp', but asserts that 'cond' holds. */
  override def assert(cond: vpr.Exp, exp: vpr.Exp, trans: (Source.Verifier.Info, ErrorReason) => VerificationError): (vpr.Exp, ErrorTransformer) = {
    val res = typedAssertFunc(Vector(cond, exp), exp.typ)(exp.pos, exp.info, exp.errT)
    val errorT: ErrorTransformer = {
      case e@ vprerr.PreconditionInAppFalse(Source(info), reason, _) if e causedBy res =>
        trans(info, reason)
    }
    (res, errorT)
  }
}
