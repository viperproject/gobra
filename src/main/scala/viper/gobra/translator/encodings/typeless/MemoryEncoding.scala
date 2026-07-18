// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.TypePatterns._
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class MemoryEncoding extends Encoding {

  /** True iff neither expression has a bounded integer type. */
  private def noBoundedOperand(ctx: Context)(l: in.Expr, r: in.Expr): Boolean =
    ctx.BoundedInt.unapply(l.typ).isEmpty && ctx.BoundedInt.unapply(r.typ).isEmpty

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case r: in.Ref => ctx.reference(r.ref.op)
    case x@ in.EqCmp(l, r) => ctx.goEqual(l, r)(x)
    case x@ in.UneqCmp(l, r) => ctx.goEqual(l, r)(x).map(v => withSrc(vpr.Not(v), x))
    case x@ in.GhostEqCmp(l, r) => ctx.equal(l, r)(x)
    case x@ in.GhostUneqCmp(l, r) => ctx.equal(l, r)(x).map(v => withSrc(vpr.Not(v), x))
    // Comparisons with a bounded-int operand on EITHER side are handled by BoundedIntEncoding
    // (which projects both operands to Int via `from`). Guard here to avoid a
    // SafeTypeEncodingCombiner "supported by more than one encoding" error.
    case n@ in.LessCmp(l, r)    if noBoundedOperand(ctx)(l, r) =>
      for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.LtCmp(vl, vr), n)
    case n@ in.AtMostCmp(l, r)  if noBoundedOperand(ctx)(l, r) =>
      for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.LeCmp(vl, vr), n)
    case n@ in.GreaterCmp(l, r) if noBoundedOperand(ctx)(l, r) =>
      for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.GtCmp(vl, vr), n)
    case n@ in.AtLeastCmp(l, r) if noBoundedOperand(ctx)(l, r) =>
      for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.GeCmp(vl, vr), n)
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case in.Access(in.Accessible.Address(l), p) => ctx.footprint(l, p)
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case in.Initialization(left) => ctx.initialization(left)
    case ass: in.SingleAss => ctx.assignment(ass.left, ass.right)(ass)
  }
}
