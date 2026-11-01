// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.encodings.LevelEncoding
import viper.gobra.translator.encodings.typeless.TerminationEncoding
import viper.silver.ast.utility.Simplifier
import viper.silver.ast.utility.rewriter.Traverse
import viper.silver.ast.utility.Statements.EmptyStmt
import viper.silver.plugin.standard.termination.{DecreasesClause, DecreasesSpecification, DecreasesTuple, DecreasesWildcard, FunctionTerminationError, MethodTerminationError}
import viper.silver.plugin.standard.termination.transformation.{DecreasesCheck, ExpTransformer, ExpressionContext, NestedPredicates, ProgramManager, ReasonTrafoFactory}
import viper.silver.verifier.AbstractError
import viper.silver.verifier.errors.ErrorNode
import viper.silver.verifier.errors.AssertFailed
import viper.silver.{ast => vpr}

/**
  * Makes termination checking modular: Viper's termination plugin checks that the termination measure decreases only
  * for calls between (mutually) recursive members of the Viper program, which requires the whole program to be
  * present. Instead, this transformation checks that the measure decreases at *every* call between members whose
  * measures start with a termination level (see [[viper.gobra.ast.internal.transform.TerminationLevelsTransform]]),
  * including calls of interface methods, closure specs, and members of imported packages:
  *  - before every method call in a method body, it adds assertions that the callee's measure (at the call) is smaller
  *    than the caller's measure (in the pre-state of the caller),
  *  - for every function, it adds methods that perform the same checks for the calls in the function's precondition,
  *    postcondition, and body.
  * Calls in the bodies of closure implementation proofs are compared with the measure of the implemented spec, which
  * makes the measure of the implementation at most the measure of the spec.
  *
  * Members of imported packages have already been checked when their package was verified. Their measures are only
  * used to check the calls to them; for Viper's termination plugin, they are replaced by `decreases _`.
  * The termination plugin still checks loops and the remaining calls.
  */
class ModularTerminationTransformer extends ViperTransformer {
  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    var errors: Seq[AbstractError] = Seq.empty
    val checks = new ModularTerminationChecks(task.program, e => errors :+= e)
    val newProgram = checks.transformedProgram
    if (errors.nonEmpty) Left(errors) else Right(task.copy(program = newProgram))
  }
}

object ModularTerminationTransformer {
  /** Attached to the loop encoding a closure implementation proof; `tuple` is the measure of the implemented spec. */
  case class ClosureProofTermination(tuple: DecreasesTuple) extends vpr.Info {
    override val comment: Seq[String] = Nil
    override val isCached: Boolean = false
  }
}

private class ModularTerminationChecks(override val program: vpr.Program, override val reportError: AbstractError => Unit)
  extends ProgramManager with DecreasesCheck with ExpTransformer with NestedPredicates {

  import ModularTerminationTransformer._

  private case class Spec(tuple: Option[DecreasesTuple], wildcard: Option[DecreasesWildcard]) {
    /** Measures starting with a termination level are subject to the modular checks. */
    lazy val leveled: Boolean = tuple.exists(_.tupleExpressions.headOption.exists(_.typ == LevelEncoding.levelType))
    lazy val imported: Boolean = (tuple.toSeq ++ wildcard).exists(TerminationEncoding.isImported)
    lazy val spec: DecreasesSpecification = DecreasesSpecification(tuple, wildcard, None)
  }

  private def specOf(exps: Seq[vpr.Exp]): Spec = Spec(
    exps.collectFirst { case t: DecreasesTuple => t },
    exps.collectFirst { case w: DecreasesWildcard => w },
  )

  private lazy val methodSpecs: Map[String, Spec] = program.methods.map(m => m.name -> specOf(m.pres)).toMap
  private lazy val functionSpecs: Map[String, Spec] = program.functions.map(f => f.name -> specOf(f.pres ++ f.posts)).toMap

  lazy val transformedProgram: vpr.Program = {
    val methods = program.methods.map(transformMethod)
    val functions = program.functions.map(hideLeveledMeasures)
    val proofMethods = program.functions.flatMap(functionChecks)
    program.copy(methods = methods ++ proofMethods, functions = functions)(program.pos, program.info, program.errT)
  }

  /*
   * Hiding leveled measures from Viper's termination plugin.
   */

  /**
    * This transformation performs all termination checks for members with leveled measures (except for the checks
    * that loops decrease their own measures). Thus, for Viper's termination plugin, their termination is assumed.
    */
  private def hideLeveledMeasure(spec: Spec, exps: Seq[vpr.Exp]): Seq[vpr.Exp] = {
    if (!spec.leveled) exps
    else {
      val conditions = spec.tuple.map(_.condition) ++ spec.wildcard.map(_.condition)
      val condition =
        if (conditions.exists(_.isEmpty)) None
        else Some(conditions.flatten.reduce[vpr.Exp](vpr.Or(_, _)()))
      val (pos, info, errT) = spec.tuple.get.meta
      exps.filterNot(_.isInstanceOf[DecreasesClause]) :+ DecreasesWildcard(condition)(pos, info, errT)
    }
  }

  private def hideLeveledMeasures(f: vpr.Function): vpr.Function = {
    val spec = functionSpecs(f.name)
    if (!spec.leveled) f
    else f.copy(pres = hideLeveledMeasure(spec, f.pres), posts = f.posts.filterNot(_.isInstanceOf[DecreasesClause]))(f.pos, f.info, f.errT)
  }

  /*
   * Checks for methods.
   */

  private def transformMethod(m: vpr.Method): vpr.Method = {
    val spec = methodSpecs(m.name)
    val withChecks = m.body match {
      case Some(body) if spec.leveled && !spec.imported =>
        val callerTuple = spec.tuple.get
        val checked = addCallChecks(body, callerTuple.tupleExpressions.map(vpr.Old(_)()), callerTuple.condition.map(vpr.Old(_)()))
        val withNested = if (containsPredicateInstances(spec.spec)) addNestedPredicateInformation.execute[vpr.Seqn](checked) else checked
        m.copy(body = Some(withNested))(m.pos, m.info, m.errT)
      case _ => m
    }
    withChecks.copy(pres = hideLeveledMeasure(spec, withChecks.pres))(m.pos, m.info, m.errT)
  }

  /**
    * Adds termination checks before every method call and before every loop: the callee has to terminate and, if its
    * measure is leveled, its measure has to be smaller than the caller's measure. Loops have to terminate as well;
    * Viper's termination plugin checks that they decrease their own measures.
    */
  private def addCallChecks(body: vpr.Seqn, callerTuple: Seq[vpr.Exp], callerCondition: Option[vpr.Exp]): vpr.Seqn = {
    def goSeqn(seqn: vpr.Seqn): vpr.Seqn = vpr.Seqn(seqn.ss.map(go), seqn.scopedSeqnDeclarations)(seqn.pos, seqn.info, seqn.errT)

    def go(stmt: vpr.Stmt): vpr.Stmt = stmt match {
      case w: vpr.While if w.info.getUniqueInfo[ClosureProofTermination].isDefined =>
        // calls in closure implementation proofs are compared with the measure of the implemented spec
        val proofTuple = w.info.getUniqueInfo[ClosureProofTermination].get.tuple
        val snapshots = proofTuple.tupleExpressions.map(e => vpr.LocalVarDecl(uniqueName("proof_measure"), e.typ)(e.pos, e.info, e.errT))
        val assignments = snapshots.zip(proofTuple.tupleExpressions).map { case (v, e) => vpr.LocalVarAssign(v.localVar, e)(e.pos, e.info, e.errT) }
        val checkedBody = addCallChecks(w.body, snapshots.map(_.localVar), proofTuple.condition)
        val newBody = vpr.Seqn(assignments :+ checkedBody, snapshots)(w.body.pos, w.body.info, w.body.errT)
        w.copy(body = newBody)(w.pos, w.info.removeUniqueInfo[ClosureProofTermination], w.errT)

      case w: vpr.While =>
        val errTrafo = vpr.ErrTrafo({
          case AssertFailed(_, r, c) => MethodTerminationError(w, r, c)
          case d => d
        })
        val loopSpec = specOf(w.invs)
        val required = callerCondition.fold[vpr.Exp](w.cond)(c => vpr.And(c, w.cond)())
        val loopTerminates = createConditionCheck(Some(required), Some(loopSpec.spec.getTerminationCondition), errTrafo,
          ReasonTrafoFactory(w).generateTerminationConditionFalse(w))
        vpr.Seqn(Seq(loopTerminates, w.copy(body = goSeqn(w.body))(w.pos, w.info, w.errT)), Nil)(w.pos, vpr.NoInfo, vpr.NodeTrafo(w))
      case i: vpr.If => i.copy(thn = goSeqn(i.thn), els = goSeqn(i.els))(i.pos, i.info, i.errT)
      case seqn: vpr.Seqn => goSeqn(seqn)
      case p: vpr.Package => p.copy(proofScript = goSeqn(p.proofScript))(p.pos, p.info, p.errT)

      case mc: vpr.MethodCall =>
        val callee = program.findMethod(mc.methodName)
        val calleeSpec = methodSpecs(mc.methodName)
        val substitution: Map[vpr.LocalVar, vpr.Exp] = callee.formalArgs.map(_.localVar).zip(mc.args).toMap
        val errTrafo = vpr.ErrTrafo({
          case AssertFailed(_, r, c) => MethodTerminationError(mc, r, c)
          case d => d
        })
        val checks = callChecks(callee, calleeSpec, substitution, callerTuple, callerCondition, errTrafo)
        vpr.Seqn(checks :+ mc, Nil)(mc.pos, vpr.NoInfo, vpr.NodeTrafo(mc))

      case other => other
    }

    goSeqn(body)
  }

  private def callChecks(callee: ErrorNode, calleeSpec: Spec, substitution: Map[vpr.LocalVar, vpr.Exp], callerTuple: Seq[vpr.Exp],
                         callerCondition: Option[vpr.Exp], errTrafo: vpr.ErrTrafo): Seq[vpr.Stmt] = {
    if (!calleeSpec.leveled) {
      // the callee does not have a leveled measure; it only needs to terminate
      val terminationCondition = calleeSpec.spec.getTerminationCondition.replace(substitution)
      Seq(createConditionCheck(callerCondition, Some(terminationCondition), errTrafo,
        ReasonTrafoFactory(callee).generateTerminationConditionFalse(callee)))
    } else {
      val calleeTuple = calleeSpec.tuple.get
      val reasonTrafoFactory = ReasonTrafoFactory(calleeTuple)
      val conditionCheck = createConditionCheck(callerCondition, calleeTuple.condition.map(_.replace(substitution)),
        errTrafo, reasonTrafoFactory.generateTupleConditionFalse(calleeTuple))
      val calleeExps = calleeTuple.tupleExpressions.map(_.replace(substitution))
      val tupleCheck = createTupleCheck(callerCondition, callerTuple, calleeExps, errTrafo, reasonTrafoFactory)
      Seq(conditionCheck) ++ levelHints(callerTuple.head, calleeExps.head) :+ tupleCheck
    }
  }

  /**
    * The multiset ordering on levels requires the SMT solver to find atoms dominating the atoms of the smaller level.
    * These are usually atoms that occur in the levels being compared. The (trivially true) assertions returned here
    * mention the multiplicities of these atoms, which triggers the axioms needed to find such a witness.
    */
  private def levelHints(callerLevel: vpr.Exp, calleeLevel: vpr.Exp): Seq[vpr.Stmt] = {
    def atoms(e: vpr.Exp): Seq[vpr.DomainFuncApp] = e.deepCollect {
      case a: vpr.DomainFuncApp if a.args.isEmpty && a.typ == LevelEncoding.atomType => a
    }
    def count(atom: vpr.Exp, level: vpr.Exp): vpr.Exp =
      vpr.GeCmp(vpr.AnySetContains(atom, vpr.DomainFuncApp(LevelEncoding.atomsFunc, Seq(level), Map.empty)())(), vpr.IntLit(0)())()
    if (callerLevel.typ != LevelEncoding.levelType || calleeLevel.typ != LevelEncoding.levelType) Seq.empty
    else (atoms(callerLevel) ++ atoms(calleeLevel)).distinct.map { atom =>
      vpr.Assert(vpr.And(count(atom, calleeLevel), count(atom, callerLevel))())()
    }
  }

  /*
   * Checks for functions.
   */

  private case class FunctionContext(function: vpr.Function, callerTuple: Seq[vpr.Exp], callerCondition: Option[vpr.Exp]) extends ExpressionContext {
    override val conditionInEx: Option[vpr.LocalVarDecl] = Some(vpr.LocalVarDecl(uniqueName("condInEx"), vpr.Bool)())
  }

  override def transformExp(e: vpr.Exp, c: ExpressionContext, inhaleExp: Boolean): vpr.Stmt = (e, c) match {
    case (_: DecreasesClause, _) => EmptyStmt
    case (call: vpr.FuncApp, context: FunctionContext) =>
      val argChecks = call.args.map(transformExp(_, context, inhaleExp = false))
      val callee = program.findFunction(call.funcname)
      val substitution: Map[vpr.LocalVar, vpr.Exp] = callee.formalArgs.map(_.localVar).zip(call.args).toMap
      val errTrafo = vpr.ErrTrafo({
        case AssertFailed(_, r, c) => FunctionTerminationError(call, r, c)
        case d => d
      })
      val checks = callChecks(callee, functionSpecs(call.funcname), substitution, context.callerTuple, context.callerCondition, errTrafo)
      vpr.Seqn(argChecks ++ checks, Nil)()
    case _ => super.transformExp(e, c, inhaleExp)
  }

  /** Generates methods checking the calls in the precondition, postcondition, and body of `f`. */
  private def functionChecks(f: vpr.Function): Seq[vpr.Method] = {
    val spec = functionSpecs(f.name)
    if (!spec.leveled || spec.imported) return Seq.empty

    val tuple = spec.tuple.get
    val pres = f.pres.filterNot(_.isInstanceOf[DecreasesClause])
    val posts = f.posts.filterNot(_.isInstanceOf[DecreasesClause])

    def method(suffix: String, methodPres: Seq[vpr.Exp], check: vpr.Stmt, decls: Seq[vpr.Declaration]): Option[vpr.Method] = {
      val simplified = Simplifier.simplify(check)
      val withNested = if (containsPredicateInstances(spec.spec)) addNestedPredicateInformation.execute[vpr.Stmt](simplified) else simplified
      if (isEmptyStatement(withNested)) None
      else Some(vpr.Method(uniqueName(s"${f.name}_$suffix"), f.formalArgs, Nil, methodPres, Nil,
        Some(vpr.Seqn(Seq(withNested), decls)()))(f.pos, f.info, f.errT))
    }

    val oldTuple = tuple.tupleExpressions.map(vpr.Old(_)())
    val oldCondition = tuple.condition.map(vpr.Old(_)())

    val bodyCheck = f.body.flatMap { body =>
      val context = FunctionContext(f, oldTuple, oldCondition)
      method("termination_levels", pres, transformExp(body, context, inhaleExp = false), context.conditionInEx.toSeq)
    }

    val postsCheck = if (posts.isEmpty) None else {
      val context = FunctionContext(f, oldTuple, oldCondition)
      val result = vpr.LocalVarDecl(uniqueName("$result"), f.typ)()
      val conjunction = posts.map(_.transform({ case vpr.Result(_) => result.localVar }, Traverse.BottomUp)).reduce[vpr.Exp](vpr.And(_, _)())
      method("posts_termination_levels", pres, transformExp(conjunction, context, inhaleExp = false), result +: context.conditionInEx.toSeq)
    }

    // in preconditions, the caller's measure is evaluated at the call, as the precondition is not yet established
    val presCheck = if (pres.isEmpty) None else {
      val context = FunctionContext(f, tuple.tupleExpressions, tuple.condition)
      val conjunction = pres.reduce[vpr.Exp](vpr.And(_, _)())
      method("pres_termination_levels", Seq.empty, transformExp(conjunction, context, inhaleExp = true), context.conditionInEx.toSeq)
    }

    bodyCheck.toSeq ++ postsCheck ++ presCheck
  }
}
