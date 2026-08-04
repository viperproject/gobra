// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{AssertByContraBodyError, AssertByError, AssertByProofBodyError, AssignSuchThatError, AssignSuchThatNoWitnessError}
import viper.gobra.theory.Addressability
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.gobra.translator.Names
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.ast.utility.ViperStrategy
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.{refute => vprrefute}
import viper.silver.plugin.sif._

class AssertionEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl}
  import viper.gobra.translator.util.TypePatterns._
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
        (newVars, newTriggers, guard, newBody) <- quantifier(vars, triggers, body)(ctx)
        guardedBody = guard.fold(newBody)(g => vpr.Implies(g, newBody)(pos, info, errT))
        newForall = vu.dropBoundedFromOnlyTriggers(vpr.Forall(newVars, newTriggers, guardedBody)(pos, info, errT).autoTrigger)
      } yield newForall.check match {
        case Seq() => newForall
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

    // Existential bound variables of bounded kinds stay at the domain sort: domain values are
    // intrinsically in-range (no guard needed) and, unlike the Int-plus-range-guard lowering
    // used for universals, the domain sort does not break witness finding — a pure arithmetic
    // guard gives Z3 no term to instantiate ('exists n int :: true' would fail), whereas SMT
    // sorts are non-empty and ground 'to'/'from' terms anchor instantiation.
    case n@ in.Exists(vars, triggers, body) =>
      val newVars = vars map ctx.variable
      val (pos, info, errT) = n.vprMeta
      for {
        newTriggers <- sequence(triggers map (trigger(_)(ctx)))
        newBody <- ctx.expression(body)
        newExists =  vu.dropBoundedFromOnlyTriggers(vpr.Exists(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger)
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
    case n@ in.Rel(e, i) => for {
      ve <- ctx.expression(e)
      vi <- ctx.expression(i)
    } yield withSrc(SIFRelExp(ve, vi.asInstanceOf[vpr.IntLit]), n)
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
      val lowering = BoundedQuantLowering(ctx, vars)
      val newVars = lowering.decls
      val (pos, info, errT) = n.vprMeta
      for {
        rawTriggers <- sequence(triggers map (trigger(_)(ctx)))
        newTriggers = lowering.rewriteTriggers(rawTriggers)
        rawBody <- pure(ctx.assertion(body))(ctx)
        rewrittenBody = lowering.rewrite(rawBody)
        newBody = lowering.guard.fold(rewrittenBody)(g => vpr.Implies(g, rewrittenBody)(pos, info, errT))
        newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT)
        desugaredForall = vpr.utility.QuantifiedPermissions.desugarSourceQuantifiedPermissionSyntax(newForall)
        triggeredForall = desugaredForall.map(f => vu.dropBoundedFromOnlyTriggers(f.autoTrigger))
        reducedForall = triggeredForall.reduce[vpr.Exp] { (a, b) => vpr.And(a, b)(pos, info, errT) }
      } yield reducedForall
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n@ in.Assert(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Assert(v), n)
    case n@ in.Refute(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vprrefute.Refute(v), n)
    case n@ in.Assume(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Assume(v), n) // Assumes are later rewritten
    case n@ in.Inhale(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Inhale(v), n)
    case n@ in.Exhale(ass) => for {v <- ctx.assertion(ass)} yield withSrc(vpr.Exhale(v), n)

    case n@ in.AssignSuchThat(v, cond) =>
      // `var x T :| P` is encoded as
      //   assert exists x' : T :: P[x -> x']
      //   inhale P
      // The local variable `x` is already registered as a block-level Viper decl by the
      // desugarer (via `declare`), so it is in scope after the statement.
      // The existential carries `cond`'s source info so error messages show
      // just `P` rather than the whole `var x T :| P` statement.
      val (condPos, condInfo, condErrT) = cond.vprMeta
      // The witness existential keeps the bound variable at its (possibly domain) sort — see
      // the in.Exists case for why the Int-plus-guard lowering is not used for existentials.
      val boundVar = in.BoundVar(v.id + "_B", v.typ.withAddressability(Addressability.boundVariable))(v.info)
      val renaming: Map[in.LocalVar, in.Node] = Map(v -> boundVar)
      val renamedCond = cond.replace(renaming)
      val condAss = in.ExprAssertion(cond)(n.info)
      val vprBoundVar = ctx.variable(boundVar)
      seqnUnits(Vector(for {
        vprBody <- ctx.expression(renamedCond)
        existsExpr = vu.dropBoundedFromOnlyTriggers(vpr.Exists(Seq(vprBoundVar), Seq.empty, vprBody)(condPos, condInfo, condErrT).autoTrigger)
        condEnc <- ctx.assertion(condAss)
        _ <- assert(existsExpr,
          (info, _) => AssignSuchThatError(info) dueTo AssignSuchThatNoWitnessError(info)
        )
        _ <- write(withSrc(vpr.Inhale(condEnc), n))
      } yield ()))

    case n: in.AssertByProof =>
      // Dafny-style
      // assert P by { L }
      //    ~~>
      // if(*) { L; assert P; assume false }; assume P
      val nonDetChoice = in.LocalVar(ctx.freshNames.next(), in.BoolT(Addressability.exclusiveVariable))(n.info)
      for {
        _ <- cl.local(withSrc(vpr.LocalVarDecl(nonDetChoice.id, ctx.typ(nonDetChoice.typ)), n))
        cond <- ctx.assertion(in.ExprAssertion(nonDetChoice)(n.info))

        p <- ctx.assertion(n.ass)

        thenBranch <- seqnUnits(Vector(for {
          // L
          proof <- ctx.statement(n.proof)
          _ <- write(proof)

          // assert P
          _ <- assert(p,
            (info, _) => AssertByError(info) dueTo AssertByProofBodyError(info)
          )

          // assume false
          ass <- assume(withSrc(vpr.FalseLit(), n))
        } yield ass))

        ifStmt = withSrc(vpr.If(cond, thenBranch, withSrc(vu.nop, n)), n)
        assumeP = withSrc(vpr.Assume(p), n)
      } yield withSrc(vu.seqn(Vector(ifStmt, assumeP)), n)

    case n: in.AssertByContra =>
      // assert P by contra { L }
      //    ~~>
      // if (!P) { L; assert false }
      for {
        p <- ctx.assertion(n.ass)
        cond = withSrc(vpr.Not(p), n)

        thenBranch <- seqnUnits(Vector(for {
          // L
          proof <- ctx.statement(n.proof)
          _ <- write(proof)

          // assert false
          ass <- assert(withSrc(vpr.FalseLit(), n),
            (info, _) => AssertByError(info) dueTo AssertByContraBodyError(info)
          )
        } yield ass))
      } yield withSrc(vpr.If(cond, thenBranch, withSrc(vu.nop, n)), n)

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

  def quantifier(vars: Vector[in.BoundVar], triggers: Vector[in.Trigger], body: in.Expr)(ctx: Context) : CodeWriter[(Seq[vpr.LocalVarDecl], Seq[vpr.Trigger], Option[vpr.Exp], vpr.Exp)] = {
    val lowering = BoundedQuantLowering(ctx, vars)

    for {
      newTriggers <- sequence(triggers map (trigger(_)(ctx)))
      newBody <- ctx.expression(body)
    } yield (lowering.decls, lowering.rewriteTriggers(newTriggers), lowering.guard, lowering.rewrite(newBody))
  }

  /**
    * Lowering for universally quantified variables of bounded integer kinds (existentials keep
    * the domain sort — see the in.Exists case).
    *
    * A bound variable declared at a bounded integer kind `k` ranges over exactly the values of
    * the corresponding `Bounded_k` domain (`forall x uint8 :: x >= 0` holds). Binding the Viper
    * variable at the domain sort, however, would force every arithmetic or indexing use of the
    * variable through `k$from(x)`, which destroys the linear injective receivers Silicon needs
    * for quantified permissions (`acc(&s[x])` would become `sadd(offset, from(x))`, a shape on
    * which Z3's inverse-function reasoning diverges). The variable is therefore bound at the
    * `Int` sort, the kind's range is added as an explicit guard, and body and triggers are
    * rewritten:
    *   - `k$from(x)`                             --> `x` (now Int-sorted)
    *   - any remaining domain-sorted use of `x`  --> `k$inv(x)`
    * This is equivalent to quantifying over the domain: by the bridge axioms `inv(from(x)) == x`
    * and `from(to(n)) == n`, `from` restricts to a bijection between the domain values and
    * `[lower, upper]` whose inverse on that range is `inv` (== `to` there). `inv` is used
    * rather than `to` because its axiom triggers on `{ from(x) }`: every ground projected
    * value `i` yields a known `inv(from(i)) == i`, so a lowered trigger like `{ m[inv(v)] }`
    * e-matches the ground `m[i]` via congruence — with `to`, the corresponding link
    * `to(from(i)) == i` is never established and quantifiers over map keys, set elements,
    * etc. of bounded kinds silently fail to instantiate.
    *
    * Under `--unboundedIntegers`, `ctx.BoundedInt` matches nothing and the lowering is a no-op.
    */
  private case class BoundedQuantLowering(ctx: Context, vars: Vector[in.BoundVar]) {
    // maps the encoded variable name to the bounded kind, for bound variables of bounded kinds
    private val lowered: Map[String, BoundedIntegerKind] =
      vars.flatMap(x => ctx.BoundedInt.unapply(x.typ).map(k => ctx.variable(x).name -> k)).toMap

    private val isTrivial: Boolean = lowered.isEmpty

    /** The bound-variable declarations, with lowered variables declared at the Int sort. */
    def decls: Seq[vpr.LocalVarDecl] = vars.map { x =>
      val decl = ctx.variable(x)
      if (lowered.contains(decl.name)) vpr.LocalVarDecl(decl.name, vpr.Int)(decl.pos, decl.info, decl.errT)
      else decl
    }

    /** Conjunction of the range guards `lower <= x && x <= upper` of all lowered variables. */
    def guard: Option[vpr.Exp] = {
      val conjuncts = vars.flatMap { x =>
        val decl = ctx.variable(x)
        lowered.get(decl.name).map { k =>
          val v = vpr.LocalVar(decl.name, vpr.Int)(decl.pos, decl.info, decl.errT)
          vpr.And(
            vpr.LeCmp(vpr.IntLit(k.lower)(decl.pos, decl.info, decl.errT), v)(decl.pos, decl.info, decl.errT),
            vpr.LeCmp(v, vpr.IntLit(k.upper)(decl.pos, decl.info, decl.errT))(decl.pos, decl.info, decl.errT)
          )(decl.pos, decl.info, decl.errT): vpr.Exp
        }
      }
      conjuncts.reduceOption((a, b) => vpr.And(a, b)(a.pos, a.info, a.errT))
    }

    private def isFromOfLowered(app: vpr.DomainFuncApp): Boolean = app.args match {
      case Seq(lv: vpr.LocalVar) => lowered.get(lv.name).exists(k => app.funcname == Names.boundedIntFrom(k))
      case _ => false
    }

    /** Rewrites `from(x)` to Int-sorted `x` and remaining domain-sorted `x` to `to(x)`. */
    def rewrite[T <: vpr.Node](n: T): T =
      if (isTrivial) n else ViperStrategy.Slim({
        case app: vpr.DomainFuncApp if isFromOfLowered(app) =>
          val lv = app.args.head.asInstanceOf[vpr.LocalVar]
          vpr.LocalVar(lv.name, vpr.Int)(app.pos, app.info, app.errT)
        case lv: vpr.LocalVar if lv.typ != vpr.Int && lowered.contains(lv.name) =>
          val k = lowered(lv.name)
          vpr.DomainFuncApp(
            Names.boundedIntInv(k),
            Seq(vpr.LocalVar(lv.name, vpr.Int)(lv.pos, lv.info, lv.errT)),
            Map.empty
          )(lv.pos, lv.info, vpr.DomainType(Names.boundedIntDomain(k), Map.empty)(Seq.empty), Names.boundedIntDomain(k), lv.errT)
      }).execute[T](n)

    /**
      * Rewrites trigger expressions, dropping those that degenerate to a bare bound variable
      * (`{ from(x) }` becomes `{ x }`, which is not a valid trigger term) and any trigger left
      * without expressions. A quantifier that loses all triggers falls back to auto-triggering.
      */
    def rewriteTriggers(ts: Seq[vpr.Trigger]): Seq[vpr.Trigger] =
      if (isTrivial) ts else ts.flatMap { t =>
        val exps = t.exps.map(rewrite(_)).filterNot(_.isInstanceOf[vpr.LocalVar])
        if (exps.isEmpty) None else Some(vpr.Trigger(exps)(t.pos, t.info, t.errT))
      }
  }
}
