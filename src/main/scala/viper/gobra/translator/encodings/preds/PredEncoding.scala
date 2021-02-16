// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.preds

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{DefaultErrorBackTranslator, FoldError, Source, UnfoldError}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.silver.verifier.{errors => vprerr}

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
    * [dflt(Pred(ts)°)] -> default() where default is a 0-ary function in the domain
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case n@ in.PredicateConstructor(p, pTs, args) :: ctx.Pred(_) / Exclusive =>
        val (pos, info, errT) = n.vprMeta
        for {
          vArgs <- sequence(args map (a => option(a map goE)))
        } yield defunc.construct(p, pTs.args, vArgs)(pos, info, errT)(ctx)

      case (e: in.DfltVal) :: ctx.Pred(ts) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        unit(defunc.default(ts)(pos, info, errT)(ctx))
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
      case n@ in.Access(in.Accessible.PredExpr(in.PredExprInstance(p :: ctx.Pred(ts), args)), perm) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vArgs <- sequence(args map goE)
          vBase <- goE(p)
          vPerm <- goE(perm)
        } yield vpr.PredicateAccessPredicate(defunc.instance(vBase, ts, vArgs)(pos, info, errT)(ctx), vPerm)(pos, info, errT) : vpr.Exp
    }
  }

  /**
    * Encodes statements.
    * This includes make-statements.
    *
    * The default implements:
    * [v: *T = make(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    *
    * [unfold acc(Q{d1, ..., dk}(e1, ..., en), p)] ->
    *   exhale acc(eval_S([Q{d1, ..., dk}], [e1], ..., [en]), [p])
    *   inhale acc(Q(a1, ..., ak), [p])
    *   unfold acc(Q(a1, ..., ak), [p])
    *
    * [fold acc(Q{d1, ..., dk}(e1, ..., en), p)] ->
    *   fold acc(Q(a1, ..., ak), [p])
    *   exhale acc(Q(a1, ..., ak), [p])
    *   inhale acc(eval_S([Q{d1, ..., dk}], [e1], ..., [en]), [p])
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    def mergeArgs[A](ctrArgs: Vector[Option[A]], instanceArgs: Vector[A]): Vector[A] = {
      ctrArgs.foldLeft((instanceArgs, Vector.empty[A])){
        case ((ys, res), Some(e)) => (ys,      res :+ e)
        case ((ys, res), None)    => (ys.tail, res :+ ys.head)
      }._2
    }

    default(super.statement(ctx)){
      case n@ in.PredExprFold(in.PredicateConstructor(q, qT, ctrArgs) :: ctx.Pred(ctrTs), accArgs, perm) =>
        val (pos, info, errT) = n.vprMeta
        seqn(
          for {
            // [d1], ..., [dk]
            vCtrArgs <- sequence(ctrArgs map (a => option(a map goE)))
            // [e1], ..., [en]
            vAccArgs <- sequence(accArgs map goE)
            // a1, ..., ak
            qArgs = mergeArgs(ctrArgs, accArgs)
            // acc(Q(a1, ..., ak), [p])
            qAcc <- ctx.predicate.proxyAccess(q, qArgs, perm)(n.info)(ctx)
            // fold acc(Q(a1, ..., ak), [p])
            fold = vpr.Fold(qAcc)(pos, info, errT)
            _ <- write(fold)
            _ <- errorT{
              case e@ vprerr.FoldFailed(Source(info), reason, _) if e causedBy fold =>
                FoldError(info) dueTo DefaultErrorBackTranslator.defaultTranslate(reason) // we might want to change the message
            }
            // exhale acc(Q(a1, ..., ak), [p])
            _ <- write(vpr.Exhale(qAcc)(pos, info, errT))
            // [Q{d1, ..., dk}]
            ctr = defunc.construct(q, qT.args, vCtrArgs)(pos, info, errT)(ctx)
            // eval_S([Q{d1, ..., dk}], [e1], ..., [en])
            eval = defunc.instance(ctr, ctrTs, vAccArgs)(pos, info, errT)(ctx)
            // inhale acc(eval_S([Q{d1, ..., dk}], [e1], ..., [en]), [p])
            vPerm <- goE(perm)
          } yield vpr.Inhale(vpr.PredicateAccessPredicate(eval, vPerm)(pos, info, errT))(pos, info, errT)
        )

      case n@ in.PredExprUnfold(in.PredicateConstructor(q, qT, ctrArgs) :: ctx.Pred(ctrTs), accArgs, perm) =>
        val (pos, info, errT) = n.vprMeta
        seqn(
          for {
            // [d1], ..., [dk]
            vCtrArgs <- sequence(ctrArgs map (a => option(a map goE)))
            // [e1], ..., [en]
            vAccArgs <- sequence(accArgs map goE)
            // a1, ..., ak
            qArgs = mergeArgs(ctrArgs, accArgs)
            vPerm <- goE(perm)
            // [Q{d1, ..., dk}]
            ctr = defunc.construct(q, qT.args, vCtrArgs)(pos, info, errT)(ctx)
            // eval_S([Q{d1, ..., dk}], [e1], ..., [en])
            eval = defunc.instance(ctr, ctrTs, vAccArgs)(pos, info, errT)(ctx)
            // exhale acc(eval_S([Q{d1, ..., dk}], [e1], ..., [en]), [p])
            exhale = vpr.Exhale(vpr.PredicateAccessPredicate(eval, vPerm)(pos, info, errT))(pos, info, errT)
            _ <- write(exhale)
            _ <- errorT{
              case e@ vprerr.ExhaleFailed(Source(info), reason, _) if e causedBy exhale =>
                UnfoldError(info) dueTo DefaultErrorBackTranslator.defaultTranslate(reason) // we might want to change the message
            }
            // acc(Q(a1, ..., ak), [p])
            qAcc <- ctx.predicate.proxyAccess(q, qArgs, perm)(n.info)(ctx)
            // inhale acc(Q(a1, ..., ak), [p])
            _ <- write(vpr.Inhale(qAcc)(pos, info, errT))
            // unfold acc(Q(a1, ..., ak), [p])
          } yield vpr.Unfold(qAcc)(pos, info, errT)
        )
    }
  }
}
