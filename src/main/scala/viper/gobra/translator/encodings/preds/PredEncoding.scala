// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.preds

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PredEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  private val defunc: DefuncComponent = new DefuncComponentImpl

  override def finalize(col: Collector): Unit = {
    defunc.finalize(col)
  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Pred(ts) / m =>
      m match {
        case Exclusive => defunc.typ(ts)(ctx)
        case Shared    => vpr.Ref: vpr.Type
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * [Q{d1, ..., dk}: pred(S)] -> make_S_ID([d1], ..., [dk]) where ID is the ID for the pattern used in Q{d1, ..., dk}
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case n@ in.PredicateConstructor(p, pTs, args) :: ctx.Pred(_) / Exclusive =>
        val (pos, info, errT) = n.vprMeta
        for {
          vArgs <- sequence(args map (a => option(a map goE)))
        } yield defunc.construct(p, pTs.args, vArgs)(pos, info, errT)(ctx)
    }
  }

  /**
    * Encodes assertions.
    *
    * Constraints:
    * - in.Access with in.PredicateAccess has to encode to vpr.PredicateAccessPredicate.
    *
    * [acc(p(e1, ..., en))] -> eval_S([p], [e1], ..., [en]) where p: pred(S)
    */
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.assertion(ctx)) {
      case n@ in.Access(in.Accessible.Predicate(in.PredExprInstance(p :: ctx.Pred(ts), args))) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vArgs <- sequence(args map goE)
          vBase <- ctx.expr.translate(p)(ctx)
          perm = vpr.FullPerm()(pos, info, errT)
        } yield vpr.PredicateAccessPredicate(defunc.instance(vBase, ts, vArgs)(pos, info, errT)(ctx), perm)(pos, info, errT) : vpr.Exp
    }
  }
}
