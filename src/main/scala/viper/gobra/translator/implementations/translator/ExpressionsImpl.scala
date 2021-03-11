// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {}

  override def translate(x: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {

    val typEncodingOptRes = ctx.typeEncoding.expr(ctx).lift(x)
    if (typEncodingOptRes.isDefined) return typEncodingOptRes.get


    val (pos, info, errT) = x.vprMeta

    def goE(e: in.Expr): CodeWriter[vpr.Exp] = translate(e)(ctx)

    x match {

      case r: in.Ref => ctx.typeEncoding.reference(ctx)(r.ref.op)

      case in.EqCmp(l, r) => ctx.typeEncoding.goEqual(ctx)(l, r, x)
      case in.UneqCmp(l, r) => ctx.typeEncoding.goEqual(ctx)(l, r, x).map(vpr.Not(_)(pos, info, errT))

      case in.PureFunctionCall(func, args, typ) =>
        val resultType = ctx.typeEncoding.typ(ctx)(typ)

        for {
          vArgs <- sequence(args map goE)
          app = vpr.FuncApp(func.name, vArgs)(pos, info, resultType, errT)
        } yield app

      case in.PureMethodCall(recv, meth, args, typ) =>
        val resultType = ctx.typeEncoding.typ(ctx)(typ)

        for {
          vRecv <- goE(recv)
          vArgs <- sequence(args map goE)
          app = vpr.FuncApp(meth.uniqueName, vRecv +: vArgs)(pos, info, resultType, errT)
        } yield app

      case unfold: in.Unfolding =>
        for {
          a <- ctx.ass.translate(unfold.acc)(ctx)
          e <- pure(goE(unfold.in))(ctx)
        } yield vpr.Unfolding(a.asInstanceOf[vpr.PredicateAccessPredicate], e)(pos, info, errT)

      case in.Old(op, _) => for { o <- goE(op) } yield vpr.Old(o)(pos, info, errT)
      case in.LabeledOld(l, op) => for {o <- goE(op) } yield vpr.LabelledOld(o, l.name)(pos, info, errT)

      case in.LessCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LtCmp(vl, vr)(pos, info, errT)
      case in.AtMostCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LeCmp(vl, vr)(pos, info, errT)
      case in.GreaterCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GtCmp(vl, vr)(pos, info, errT)
      case in.AtLeastCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GeCmp(vl, vr)(pos, info, errT)

      case in.Negation(op) => for{o <- goE(op)} yield vpr.Not(o)(pos, info, errT)

      case in.And(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.And(vl, vr)(pos, info, errT)
      case in.Or(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Or(vl, vr)(pos, info, errT)

      case in.Conditional(cond, thn, els, _) =>
        for {
          vcond <- goE(cond)
          vthn <- goE(thn)
          vels <- goE(els)
        } yield vpr.CondExp(vcond, vthn, vels)(pos, info, errT)

      case in.PureForall(vars, triggers, body) => for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newForall.check match {
        case Seq() => newForall
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

      case in.Exists(vars, triggers, body) => for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newExists =  vpr.Exists(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newExists.check match {
        case Seq() => newExists
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

      case in.Conversion(_, expr) => goE(expr)

      case _ => Violation.violation(s"Expression $x did not match with any implemented case.")
    }
  }

  override def trigger(trigger: in.Trigger)(ctx: Context) : CodeWriter[vpr.Trigger] = {
    val (pos, info, errT) = trigger.vprMeta
    for { expr <- sequence(trigger.exprs map (translate(_)(ctx))) }
      yield vpr.Trigger(expr)(pos, info, errT)
  }

  def quantifier(vars: Vector[in.BoundVar], triggers: Vector[in.Trigger], body: in.Expr)(ctx: Context) : CodeWriter[(Seq[vpr.LocalVarDecl], Seq[vpr.Trigger], vpr.Exp)] = {
    val newVars = vars map ctx.typeEncoding.variable(ctx)

    for {
      newTriggers <- sequence(triggers map (trigger(_)(ctx)))
      newBody <- translate(body)(ctx)
    } yield (newVars, newTriggers, newBody)
  }
}
