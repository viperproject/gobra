// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.fixpoints

import viper.gobra.ast.internal.GlobalConst
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.silver.ast.{Exp, Type, TypeVar}
import viper.silver.{ast => vpr}

class FixpointImpl extends Fixpoint {

  /**
    * Finalizes translation. `addMemberFn` is called with any member that is part of the encoding.
    */
  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    generatedDomains foreach addMemberFn
  }

  override def create(gc: in.GlobalConstDecl)(ctx: Context): Unit = {
    val domainName = constantDomainName(gc.left)
    val (pos, info, errT) = gc.vprMeta

    val getFunc = constantGetDomainFunc(gc.left)(ctx)
    val getFuncApp = get(gc.left)(ctx)
    val getAxiom = vpr.NamedDomainAxiom(
      name = s"get_constant${gc.left.id}",
      exp = vpr.EqCmp(getFuncApp, ctx.expression(gc.right).res)(pos, info, errT),
    )(domainName = domainName)

    val domain = vpr.Domain(
      domainName,
      Seq(getFunc),
      Seq(getAxiom),
      Seq()
    )(pos, info, errT)
    _generatedDomains ::= domain
  }

  override def get(gc: in.GlobalConst)(ctx: Context): vpr.DomainFuncApp =
    vpr.DomainFuncApp(constantGetDomainFunc(gc)(ctx), Seq[Exp](), Map[TypeVar, Type]())()

  private def constantGetDomainFunc(gc: in.GlobalConst)(ctx: Context): vpr.DomainFunc = gc match {
    case v: in.GlobalConst.Val =>
      vpr.DomainFunc(
        name = s"constant_${v.id}",
        formalArgs = Seq(),
        typ = ctx.typ(v.typ)
      )(domainName = constantDomainName(v))
    case _ => ???
  }

  def generatedDomains: List[vpr.Domain] = _generatedDomains

  private var _generatedDomains: List[vpr.Domain] = List.empty

  private def constantDomainName(c: GlobalConst): String = s"Constant${c.id}"
}
