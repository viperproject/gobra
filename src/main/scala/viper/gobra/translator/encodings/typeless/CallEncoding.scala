// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{GoCallPreconditionReason, PreconditionError, Source}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class CallEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case x@in.PureFunctionCall(func, args, typ) =>
      val (pos, info, errT) = x.vprMeta
      val resultType = ctx.typ(typ)

      for {
        vArgs <- sequence(args map ctx.expression)
        app = vpr.FuncApp(func.name, vArgs)(pos, info, resultType, errT)
      } yield app

    case x@in.PureMethodCall(recv, meth, args, typ) =>
      val (pos, info, errT) = x.vprMeta
      val resultType = ctx.typ(typ)

      for {
        vRecv <- ctx.expression(recv)
        vArgs <- sequence(args map ctx.expression)
        app = vpr.FuncApp(meth.uniqueName, vRecv +: vArgs)(pos, info, resultType, errT)
      } yield app
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case x@in.FunctionCall(targets, func, args) =>
      val (pos, info, errT) = x.vprMeta
      for {
        vArgss <- sequence(args map ctx.expression)
        vTargets <- sequence(targets map ctx.expression)
        // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
        // Therefore, we introduce auxiliary variables and
        // add an assignment from the auxiliary variables to the actual targets
        (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget(_)(ctx)).unzip
        (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
        _ <- local(auxTargetDecls: _*)
        _ <- write(vpr.MethodCall(func.name, vArgss, vUsedTargets)(pos, info, errT))
        assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
      } yield assignToTargets

    case x@in.MethodCall(targets, recv, meth, args) =>
      val (pos, info, errT) = x.vprMeta
      for {
        vRecv <- ctx.expression(recv)
        vArgss <- sequence(args map ctx.expression)
        vTargets <- sequence(targets map ctx.expression)
        // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
        // Therefore, we introduce auxiliary variables and
        // add an assignment from the auxiliary variables to the actual targets
        (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget(_)(ctx)).unzip
        (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
        _ <- local(auxTargetDecls: _*)
        _ <- write(vpr.MethodCall(meth.uniqueName, vRecv +: vArgss, vUsedTargets)(pos, info, errT))
        assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
      } yield assignToTargets

    case x@in.GoFunctionCall(func, args) =>
      val (pos, info, errT) = x.vprMeta
      val funcM = ctx.lookup(func)
      translateGoCall(funcM.pres, funcM.args, args)(ctx)(pos, info, errT)

    case x@in.GoMethodCall(recv, meth, args) =>
      val (pos, info, errT) = x.vprMeta
      val methM = ctx.lookup(meth)
      translateGoCall(methM.pres, methM.receiver +: methM.args, recv +: args)(ctx)(pos, info, errT)

    case x@in.GoClosureCall(closure, args) =>
      val (pos, info, errT) = x.vprMeta
      closure match {
        case f: in.FunctionLit =>
          translateGoCall(f.pres, f.args, args)(ctx)(pos, info, errT)
      }
  }

  /**
    * Translates a go call to a function or method with pre-condition `pre` which is parameterized by
    * formal parameters `formalParams` and is instantiated with `args`
    */
  private def translateGoCall(pre: Vector[in.Assertion],
                              formalParams: Vector[in.Parameter.In],
                              args: Vector[in.Expr]
                             )(ctx: Context)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)
  : Writer[vpr.Stmt] = {
    Violation.violation(
      args.length == formalParams.length,
      "number of passed arguments must match number of expected arguments"
    )

    import viper.silver.verifier.{errors => err}

    for {
      vArgss <- sequence(args map ctx.expression)
      funcArgs <- sequence(formalParams map ctx.expression)
      substitutions = (funcArgs zip vArgss).toMap
      preCond <- sequence(pre map ctx.assertion)
      preCondInstance = preCond.map{ _.replace(substitutions) }
      and = vu.bigAnd(preCondInstance)(pos, info, errT)
      exhale = vpr.Exhale(and)(pos, info, errT)
      _ <- errorT {
        case err.ExhaleFailed(Source(info), _, _) => PreconditionError(info).dueTo(GoCallPreconditionReason(info))
      }
    } yield exhale
  }

  private def viperTarget(x: vpr.Exp)(ctx: Context): (vpr.LocalVar, Option[(vpr.LocalVarDecl, vpr.AbstractAssign)]) = {
    x match {
      case x: vpr.LocalVar => (x, None)
      case _ =>
        val decl = vpr.LocalVarDecl(ctx.freshNames.next(), x.typ)(x.pos, x.info, x.errT)
        val ass  = vu.valueAssign(x, decl.localVar)(x.pos, x.info, x.errT)
        (decl.localVar, Some((decl, ass)))
    }
  }
}
