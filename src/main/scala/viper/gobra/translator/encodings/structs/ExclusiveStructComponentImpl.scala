// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.structs
import viper.gobra.ast.internal.Node
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.structs.StructEncoding.ComponentParameter
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.ast.AnonymousDomainAxiom
import viper.silver.{ast => vpr}

import scala.collection.mutable

class ExclusiveStructComponentImpl extends ExclusiveStructComponent {

  private val domains: mutable.Map[Int, vpr.Domain] = mutable.Map.empty

  override def finalize(col: Collector): Unit = {
    domains.values foreach col.addMember
  }

  override def typ(vti: ComponentParameter)(ctx: Context): vpr.Type = ctx.tuple.typ(vti.map(_._1))
  override def get(base: vpr.Exp, idx: Int, vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = withSrc(ctx.tuple.get(base, idx, vti.size), src)
  override def create(args: Vector[vpr.Exp], vti: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = withSrc(ctx.tuple.create(args), src)

  private def withSrc[T](node: (vpr.Position, vpr.Info, vpr.ErrorTrafo) => T, src: in.Node): T = {
    val (pos, info, errT) = src.vprMeta
    node(pos, info, errT)
  }

  def ensureCompanionDomain(arity: Int)(ctx: Context): Unit = {
    if (!domains.isDefinedAt(arity)) {
      val typeVars = 0.until(arity) map (ix => vpr.TypeVar(s"T$ix"))

      val decls = 0.until(arity) map (ix => vpr.LocalVarDecl(s"t$ix", typeVars(ix))())
      val vars : Vector[vpr.Exp] = (decls map (_.localVar)).toVector

      val constructor = ctx.tuple.create(vars)()

      val containedInCons = 0.until(arity) map { ix =>
        ctx.contains.contains(ctx.tuple.get(constructor, ix, arity)(), constructor)()
      }

      val containsCompanionDomainName = s"ContainsTuple$arity"

      val axiom = AnonymousDomainAxiom(
        exp = vpr.Forall(
          decls,
          Seq(vpr.Trigger(Seq(constructor))()),
          viper.silicon.utils.ast.BigAnd(containedInCons)
        )()
      )(domainName = containsCompanionDomainName)

      val containsDomain = vpr.Domain(
        containsCompanionDomainName,
        Seq(),
        Seq(axiom),
        typeVars
      )()

      domains.update(arity, containsDomain)
    }
  }

  /** Checks if a element is in the struct. */
  override def contains(left: vpr.Exp, right: vpr.Exp, arity: Int)(src: Node)(ctx: Context): vpr.Exp = {
    ensureCompanionDomain(arity)(ctx)

    val (pos, info, errT) = src.vprMeta

    ctx.contains.contains(left, right)(pos, info, errT)
  }
}
