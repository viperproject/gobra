// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.verifier.{errors, reasons}
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.{refute => vprrefute}
import viper.silver.plugin.sif._

class AssertionEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl}
  import cl._

  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case n@ in.Old(op) => for { o <- ctx.expression(op)} yield withSrc(vpr.Old(o), n)
    case n@ in.LabeledOld(l, op) => for {o <- ctx.expression(op)} yield withSrc(vpr.LabelledOld(o, l.name), n)

    case n@ in.Negation(op) => for{o <- ctx.expression(op)} yield withSrc(vpr.Not(o), n)

    case n@ in.And(l, r) => for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.And(vl, vr), n)
    case n@ in.Or(l, r) => for {vl <- ctx.expression(l); vr <- ctx.expression(r)} yield withSrc(vpr.Or(vl, vr), n)

    case n@ in.Conditional(cond, thn, els, _) =>
      for {
        vcond <- ctx.expression(cond)
        vthn <- ctx.expression(thn)
        vels <- ctx.expression(els)
      } yield withSrc(vpr.CondExp(vcond, vthn, vels), n)

    case n@ in.PureForall(vars, triggers, body) =>
      val (pos, info, errT) = n.vprMeta
      for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newForall.check match {
        case Seq() => newForall
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

    case n@ in.Exists(vars, triggers, body) =>
      val (pos, info, errT) = n.vprMeta
      for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newExists =  vpr.Exists(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newExists.check match {
        case Seq() => newExists
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

    case let: in.PureLet =>
      for {
        exp <- ctx.expression(let.in)
        l = ctx.variable(let.left)
        r <- ctx.expression(let.right)
      } yield withSrc(vpr.Let(l, r, exp), let)

    case n@ in.Low(e) => for {arg <- ctx.expression(e) } yield withSrc(SIFLowExp(arg), n)
    case n: in.LowContext => unit(withSrc(SIFLowEventExp(), n))
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case n@ in.SepAnd(l, r) => for {vl <- ctx.assertion(l); vr <- ctx.assertion(r)} yield withSrc(vpr.And(vl, vr), n)
    case in.ExprAssertion(e) => ctx.expression(e)
    case n@ in.Let(left, right, op) =>
      for {
        exp <- ctx.assertion(op)
        r <- ctx.expression(right)
        l = ctx.variable(left)
      } yield withSrc(vpr.Let(l, r, exp), n)
    case n@ in.MagicWand(l, r) => for {vl <- ctx.assertion(l); vr <- ctx.assertion(r)} yield withSrc(vpr.MagicWand(vl, vr), n)
    case n@ in.Implication(l, r) => for {vl <- ctx.expression(l); vr <- ctx.assertion(r)} yield withSrc(vpr.Implies(vl, vr), n)

    case n@ in.SepForall(vars, triggers, body) =>
      val newVars = vars map ctx.variable
      val (pos, info, errT) = n.vprMeta
      for {
        newTriggers <- sequence(triggers map (trigger(_)(ctx)))
        newBody <- pure(ctx.assertion(body))(ctx)
        newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT)
        desugaredForall = vpr.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(newForall)
        triggeredForall = desugaredForall.map(_.autoTrigger)
        reducedForall = triggeredForall.reduce[vpr.Exp] { (a, b) => vpr.And(a, b)(pos, info, errT) }
      } yield reducedForall
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n@ in.Assert(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Assert(v), n)
    case n@ in.Refute(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vprrefute.Refute(v), n)
    case n@ in.Assume(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Assume(v), n) // Assumes are later rewritten
    case n@ in.Inhale(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Inhale(v), n)
    case n@ in.Exhale(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Exhale(v), n)

    case n: in.AssertByProof =>
      // Dafny-style
      // assert P by { L }
      //    ~~>
      // if(*) { L; assert P; assume false }; assume P
      val (pos, info, errT) = n.vprMeta
      val src = n.info
      val nonDetChoice = in.LocalVar(ctx.freshNames.next(), in.BoolT(Addressability.exclusiveVariable))(src)
      val errInfo = n.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      for {
        _ <- cl.local(vpr.LocalVarDecl(nonDetChoice.id, ctx.typ(nonDetChoice.typ))(pos, info, errT))
        nonDetChoiceExpr <- ctx.assertion(in.ExprAssertion(nonDetChoice)(src))
        proof <- ctx.statement(n.proof)
        v <- ctx.assertion(n.ass)
        assertP = vpr.Assert(v)(pos, info, errT)
        assumeFalse = vpr.Assume(vpr.FalseLit()(pos, info, errT))(pos, info, errT)
        assumeP = vpr.Assume(v)(pos, info, errT)
        thenBranch = vu.seqn(Vector(proof, assertP, assumeFalse))(pos, info, errT)
        elseBranch = vu.nop(pos, info, errT)
        ifStmt = vpr.If(nonDetChoiceExpr, thenBranch, elseBranch)(pos, info, errT)
        _ <- cl.errorT({
          case e@errors.AssertFailed(_, reason, _) if e causedBy assertP =>
            reason match {
              case reason: reasons.AssertionFalse =>
                reporting.AssertByError(errInfo)
                  .dueTo(reporting.AssertByProofBodyError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
              case _ => reporting.AssertByError(errInfo)
            }
        }: ErrorTransformer)
      } yield vu.seqn(Vector(ifStmt, assumeP))(pos, info, errT)

    case n: in.AssertByContra =>
      // assert P by contra { L }
      //    ~~>
      // if (!P) { L; assert false }
      val (pos, info, errT) = n.vprMeta
      val assertFalse = vpr.Assert(vpr.FalseLit()(pos, info, errT))(pos, info, errT)
      val errInfo = n.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      for {
        cond <- ctx.expression(n.ass.asInstanceOf[in.ExprAssertion].exp)
        proof <- ctx.statement(n.proof)
        thenBranch = vu.seqn(Vector(proof, assertFalse))(pos, info, errT)
        elseBranch = vu.nop(pos, info, errT)
        _ <- cl.errorT({
          case e@errors.AssertFailed(_, reason, _) if e causedBy assertFalse =>
            reason match {
              case reason: reasons.AssertionFalse =>
                reporting.AssertByError(errInfo)
                  .dueTo(reporting.AssertByContraBodyError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
              case _ => reporting.AssertByError(errInfo)
            }
        }: ErrorTransformer)
      } yield vpr.If(vpr.Not(cond)(pos, info, errT), thenBranch, elseBranch)(pos, info, errT)

    case n@ in.PackageWand(wand, blockOpt) =>
      val (pos, info, errT) = n.vprMeta
      for {
        v <- ctx.assertion(wand)
        w = v.asInstanceOf[vpr.MagicWand]
        s <- sequence(blockOpt.toVector.map(ctx.statement))
      } yield vpr.Package(w, vu.seqn(s)(pos, info, errT))(pos, info, errT)

    case n@ in.ApplyWand(wand) =>
      val (pos, info, errT) = n.vprMeta
      for {
        v <- ctx.assertion(wand)
        w = v.asInstanceOf[vpr.MagicWand]
      } yield vpr.Apply(w)(pos, info, errT)
  }

  def trigger(trigger: in.Trigger)(ctx: Context) : CodeWriter[vpr.Trigger] = {
    val (pos, info, errT) = trigger.vprMeta
    for { expr <- sequence(trigger.exprs map ctx.triggerExpr)}
      yield vpr.Trigger(expr)(pos, info, errT)
  }

  def quantifier(vars: Vector[in.BoundVar], triggers: Vector[in.Trigger], body: in.Expr)(ctx: Context) : CodeWriter[(Seq[vpr.LocalVarDecl], Seq[vpr.Trigger], vpr.Exp)] = {
    val newVars = vars map ctx.variable

    for {
      newTriggers <- sequence(triggers map (trigger(_)(ctx)))
      newBody <- ctx.expression(body)
    } yield (newVars, newTriggers, newBody)
  }
}
