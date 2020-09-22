// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.sequences

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class SequenceEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Seq(t) / m =>
      m match {
        case Exclusive => vpr.SeqType(ctx.typeEncoding.typ(ctx)(t))
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * To avoid conflicts with other encodings, an encoding for type T
    * should be defined at the following left-hand sides:
    * (1) exclusive variables of type T
    * (2) exclusive operations on type T (e.g. a field access for structs)
    * (3) shared expressions of type T
    * In particular, being defined at shared operations on type T causes conflicts with (3)
    *
    * Super implements:
    * [v: T° = rhs] -> VAR[v] = [rhs]
    * [loc: T@ = rhs] -> [loc] = [rhs]
    *
    * [(e: seq[T])[idx] = rhs] -> [ e = e[idx := rhs] ]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee(in.IndexedExp(base :: ctx.Array(_, _), idx) :: _ / Exclusive), rhs, src) =>
      ctx.typeEncoding.assignment(ctx)(in.Assignee(base), in.SequenceUpdate(base, idx, rhs)(src.info), src)
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * Most cases are a one-to-one mapping to Viper's sequence operations.
    * R[ seq(e: [n]T) ] -> [e]
    * R[ set(e: [n]T) ] -> seqToSet([e])
    * R[ mset(e: [n]T) ] -> seqToMultiset([e])
    * R[ x # (e: [n]T) ] -> [x] # [e]
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){

      case n@ in.IndexedExp(e :: ctx.Seq(_), idx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vE <- goE(e)
          vIdx <- goE(idx)
        } yield vpr.SeqIndex(vE, vIdx)(pos, info, errT)

      case n@ in.SequenceUpdate(base, left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vBase <- goE(base)
          vLeft <- goE(left)
          vRight <- goE(right)
        } yield vpr.SeqUpdate(vBase, vLeft, vRight)(pos, info, errT)

      case (e: in.DfltVal) :: ctx.Seq(t) / Exclusive => unit(withSrc(vpr.EmptySeq(ctx.typeEncoding.typ(ctx)(t)), e))

      case (lit: in.SequenceLit) :: ctx.Seq(t) =>
        val (pos, info, errT) = lit.vprMeta
        if (lit.exprs.isEmpty) unit(vpr.EmptySeq(ctx.typeEncoding.typ(ctx)(t))(pos, info, errT))
        else sequence(lit.exprs map goE).map(args => vpr.ExplicitSeq(args)(pos, info, errT))

      case n@ in.Length(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(vpr.SeqLength(_)(pos, info, errT))

      case in.SequenceConversion(e :: ctx.Seq(_)) => goE(e)

      case n@ in.SetConversion(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(ctx.seqToSet.create(_)(pos, info, errT))

      case n@ in.SequenceConversion(e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        goE(e).map(ctx.seqToMultiset.create(_)(pos, info, errT))

      case n@ in.Contains(x, e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield vpr.SeqContains(vX, vE)(pos, info, errT)

      case n@ in.Multiplicity(x, e :: ctx.Seq(_)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vX <- goE(x)
          vE <- goE(e)
        } yield ctx.seqMultiplicity.create(vX, vE)(pos, info, errT)

      case n@ in.RangeSequence(low, high) =>
        val (pos, info, errT) = n.vprMeta
        for {
          lowT <- goE(low)
          highT <- goE(high)
        } yield vpr.RangeSeq(lowT, highT)(pos, info, errT)

      case n@ in.SequenceAppend(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqAppend(leftT, rightT)(pos, info, errT)

      case n@ in.SequenceDrop(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqDrop(leftT, rightT)(pos, info, errT)

      case n@ in.SequenceTake(left, right) =>
        val (pos, info, errT) = n.vprMeta
        for {
          leftT <- goE(left)
          rightT <- goE(right)
        } yield vpr.SeqTake(leftT, rightT)(pos, info, errT)
    }
  }
}
