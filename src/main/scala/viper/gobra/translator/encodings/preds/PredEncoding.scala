// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.preds

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PredEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  private val defunc: DefuncComponent = new DefuncComponentImpl

  override def finalize(col: Collector): Unit = {
    defunc.finalize(col)
    genSubstitutePredicates foreach col.addMember
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
    * [v{d1, ..., dk}] -> [newQ{v, d1, ..., dk}] where v: pred(T1, ..., Tn)
    *    with pred newQ(p pred(T1, ..., Tn), x1 T1, ..., xn Tn) { p(x1, ..., xn) }
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case n@ in.PredicateConstructor(p, args) :: ctx.Pred(_) / Exclusive =>
        val (pos, info, errT) = n.vprMeta

        val (proxy, finalArgs) = p match {
          case p: in.PredicateConstructorArg.FPredArg => (p.arg, args)
          case p: in.PredicateConstructorArg.MPredArg => (p.arg, args)
          case p: in.PredicateConstructorArg.ExprArg =>
            (substitutePredicate(p.typ.args)(ctx), Some(p.arg) +: args)
        }

        for {
          vArgs <- sequence(finalArgs map (a => option(a map goE)))
        } yield defunc.construct(proxy, p.typ.args, vArgs)(pos, info, errT)(ctx)
    }
  }

  /**
    * For a sequence of types T1, ..., Tn, the function returns a proxy to a predicate
    *   pred substitutePredicate(p pred(T1, ..., Tn), x1 T1, ..., xn Tn) { p(x1, ..., xn) }
    *
    * For a combination of types T1, ..., Tn, the same proxy is returned always.
    */
  private def substitutePredicate(ts: Vector[in.Type])(ctx: Context): in.FPredicateProxy = {
    genSubstitutePredicatesMap.getOrElse(ts, {
      val name = s"${Names.substitutePred}_${genSubstitutePredicates.size}"
      val proxy = in.FPredicateProxy(name)(Source.Parser.Internal)

      val src = Source.Parser.Internal
      val p = in.Parameter.In("p", in.PredT(ts, Addressability.Exclusive))(src)
      val args = ts.zipWithIndex map { case (t, idx) => in.Parameter.In(s"x$idx", t)(src) }
      val body = in.Access(in.Accessible.Predicate(in.PredExprInstance(p, args)(src)))(src)
      val newPred = in.FPredicate(proxy, p +: args, Some(body))(src)

      genSubstitutePredicatesMap += (ts -> proxy)
      genSubstitutePredicates ::= ctx.predicate.fpredicate(newPred)(ctx).res

      proxy
    })
  }
  private var genSubstitutePredicatesMap: Map[Vector[in.Type], in.FPredicateProxy] = Map.empty
  private var genSubstitutePredicates: List[vpr.Predicate] = List.empty


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
