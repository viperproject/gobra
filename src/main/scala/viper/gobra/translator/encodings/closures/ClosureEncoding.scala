// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.{Config, Hyper}
import viper.gobra.reporting
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.plugin.standard.termination
import viper.silver.verifier.{errors, reasons}
import viper.silver.{ast => vpr}

class ClosureEncoding(config: Config) extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}

  val specs = new ClosureSpecsEncoder
  val domain = new ClosureDomainEncoder(specs)
  val moe = new MethodObjectEncoder(domain)

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case in.FunctionT(_, _, addr) =>
      addr match {
        case Exclusive => domain.vprType
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * R[ pure? func funcLitName(_)_ { _ } ] -> closureGet$funcLitName([ pointers to captured vars ])
    * R[ funcName ] -> closureGet$funcName()
    * R[ recv.methName ] -> closureGet$[methName]([recv])
    * R[ funcLitName ] -> closure (only appears inside the body of funcLitName)
    * R[ cl(args) as spec{params} ] -> closureCall$[spec]$[idx of params]([cl], [vars captured by cl], [args + params])
    * R[ cl implements spec{params} ] -> closureImplements$[spec]$[idx of params]([cl], [params])
    * R[ nil: func(_)_ ] -> closureNil()
    * R[ dflt(func(_)_) ] -> closureNil()
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){

    case l: in.FunctionLitLike => specs.callToClosureGetter(l.name, l.captured)(ctx)

    case f: in.FunctionObject => specs.callToClosureGetter(f.func)(ctx)

    case m: in.MethodObject => moe.encodeMethodObject(m)(ctx)

    case c: in.ClosureObject =>
      // A closure object is guaranteed to only be present directly within the closure with the same name
      val (pos, info, errT) = c.vprMeta
      cl.unit(vpr.LocalVar(Names.closureArg, domain.vprType)(pos, info, errT))

    case c: in.PureClosureCall => specs.pureClosureCall(c)(ctx)

    case a: in.ClosureImplements => specs.closureImplementsExpression(a)(ctx)

    case e@in.DfltVal(_) :: ctx.Function(t) / Exclusive => nilClosure(e, t)(ctx)

    case e@in.NilLit(_) :: ctx.Function(t) => nilClosure(e, t)(ctx)
  }

  private def nilClosure(e: in.Expr, fTyp: in.FunctionT)(ctx: Context): CodeWriter[vpr.Exp] =
    ctx.expression(in.DomainFunctionCall(in.DomainFuncProxy(Names.closureNilFunc, Names.closureDomain)(e.info), Vector.empty, fTyp)(e.info))

  /**
    * [ts := cl(args) as spec{params}] -> [ts] := closureCall$[spec]$[idx of params]([cl], [vars captured by cl], [args + params])
    * [proof cl implements spec { BODY }] -> see [[specImplementationProof]]
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = default(super.statement(ctx)) {
    case c: in.ClosureCall => specs.closureCall(c)(ctx)

    case c: in.GoClosureCall => specs.goClosureCall(c)(ctx)

    case p: in.SpecImplementationProof =>
      specImplementationProof(p)(ctx)
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    domain.finalize(addMemberFn)
    specs.finalize(addMemberFn)
    moe.finalize(addMemberFn)
    if (needProofIterator) {
      addMemberFn(proofIterator)
    }
  }

  /**
    * Encodes:
    * method closureProofIterator() returns (res: Int)
    *   requires decreases
    * This is used in [[specImplementationProof]]
    */
  private lazy val proofIterator: vpr.Method = {
    needProofIterator = true
    vpr.Method(
      Names.closureProofIterator,
      Seq.empty,
      Seq(vpr.LocalVarDecl("res", vpr.Int)()),
      Seq(termination.DecreasesTuple(Nil)()), Seq.empty, None
    )()
  }
  private var needProofIterator: Boolean = false

  /** Encodes a spec implementation proof as:
    *
    * [ args, results already initialised ]
    * [ parameters already assigned ]
    * if (*) {
    *   var numIterations: Int
    *   numIterations := proofIterator()
    *   while(numIterations > 0)
    *     decreases numIterations
    *   {
    *     inhale precondition of spec
    *     [ body ]
    *     exhale postcondition of spec
    *     numIterations := numIterations - 1
    *   }
    *   assume false
    * }
    * inhale closureImplements$[spec]([closure], [params])
    */
  private def specImplementationProof(proof: in.SpecImplementationProof)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (exhalePos, _, _) = proof.vprMeta
    val inhalePres = cl.seqns(proof.pres map (a => for {
          ass <- ctx.assertion(a)
        } yield vpr.Inhale(ass)()))
    val exhalePosts = for {
      assertions <- proof.posts.foldLeft(cl.unit(vpr.TrueLit() ().asInstanceOf[vpr.Exp])) ((acc, a) => for {
        rest <- acc
        ass <- ctx.assertion (a)
      } yield vpr.And(rest, ass) ())
    } yield vpr.Exhale(assertions)(exhalePos)

    def failedExhale: ErrorTransformer = {
      case e@errors.ExhaleFailed(_, reason, _) if e causedBy exhalePosts.res =>
        val info = proof.vprMeta._2.asInstanceOf[Source.Verifier.Info]
        reason match {
          case reason: reasons.AssertionFalse => reporting.SpecImplementationPostconditionError(info, proof.spec.info.tag)
            .dueTo(reporting.AssertionFalseError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
          case reason: reasons.InsufficientPermission => reporting.SpecImplementationPostconditionError(info, proof.spec.info.tag)
            .dueTo(reporting.InsufficientPermissionError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
          case _ => reporting.SpecImplementationPostconditionError(info, proof.spec.info.tag)
        }
    }

    val (pos, info, errT) = proof.vprMeta
    val src = proof.info

    val ifNdBool = in.LocalVar(ctx.freshNames.next(), in.BoolT(Addressability.exclusiveVariable))(src)
    val numIterations = in.LocalVar(ctx.freshNames.next(), in.IntT(Addressability.exclusiveVariable))(src)

    val omitLowExprs = config.hyperModeOrDefault == Hyper.Disabled
    def lowExprOrTrue(e: in.Low): in.Expr = if (omitLowExprs) in.BoolLit(b = true)(src) else e
    val assumeNdBoolIsLow = in.Assume(in.ExprAssertion(lowExprOrTrue(in.Low(ifNdBool)(src)))(src))(src)
    val assumeNumIterationsIsLow = in.Assume(in.ExprAssertion(lowExprOrTrue(in.Low(numIterations)(src)))(src))(src)

    for {
      _ <- cl.local(vpr.LocalVarDecl(ifNdBool.id, ctx.typ(ifNdBool.typ))(pos, info, errT))
      assumeNdLow <- ctx.statement(assumeNdBoolIsLow)
      ndBoolTrue <- ctx.assertion(in.ExprAssertion(ifNdBool)(src))
      ifStmt <- for {
        _ <- cl.local(vpr.LocalVarDecl(numIterations.id, ctx.typ(numIterations.typ))(pos, info, errT))
        assignNumIterations <- ctx.statement(in.FunctionCall(Vector(numIterations), in.FunctionProxy(proofIterator.name)(src), Vector.empty)(src))
        assumeNumIterLow <- ctx.statement(assumeNumIterationsIsLow)
        whileStmt <- for {
          numIterationsGT0 <- ctx.assertion(in.ExprAssertion(in.GreaterCmp(numIterations, in.IntLit(0)(src))(src))(src))
          inhalePres <- inhalePres
          body <- ctx.statement(proof.body)
          exhalePosts <- exhalePosts
          decreaseNumIterations <- ctx.statement(in.SingleAss(in.Assignee(numIterations), in.Sub(numIterations, in.IntLit(1)(src))(src))(src))
          whileBody = vu.seqn(Vector(inhalePres, body, exhalePosts, decreaseNumIterations))(pos, info, errT)
          invNumIterationsIsLow <- ctx.expression(lowExprOrTrue(in.Low(numIterations)(src)))
          terminationMeasure <- ctx.assertion(in.NonItfTupleTerminationMeasure(Vector(numIterations), None)(src))
        } yield vpr.While(numIterationsGT0, Seq(invNumIterationsIsLow, terminationMeasure), whileBody)(pos, info, errT)
        assumeFalse = vpr.Assume(vpr.FalseLit()())()
        ifThen = vu.seqn(Vector(assignNumIterations, assumeNumIterLow, whileStmt, assumeFalse))(pos, info, errT)
        ifElse = vu.nop(pos, info, errT)
      } yield vpr.If(ndBoolTrue, ifThen, ifElse)(pos, info, errT)
      implementsAssertion <- ctx.expression(in.ClosureImplements(proof.closure, proof.spec)(src))
      assumeImplements = vpr.Assume(implementsAssertion)()
      _ <- cl.errorT(failedExhale)
    } yield vu.seqn(Vector(assumeNdLow, ifStmt, assumeImplements))(pos, info, errT)
  }
}