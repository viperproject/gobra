// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._
import viper.silver.plugin.sif.{SIFLowExp, SIFRelExp}
import viper.silver.verifier.errors.Internal
import viper.silver.verifier.reasons.{AssertionFalse, FeatureUnsupported}

import scala.collection.mutable

/**
  * The encoding of hyperfunctions, independent of the product construction (`product`), which provides the primitives
  * ([[ProductConstruction]]). With `act` the activation (one condition per execution, see [[ProductConstruction]]):
  *
  *  - `active(i)` is `act(i)`;
  *  - `rel(e, i)` is partial: it is `asserting act(i) in e_i`;
  *  - `low(e)` relates the active executions: `∧_{i<j} (act(i) && act(j) ==> e_i == e_j)`;
  *  - in hyperfunction contracts and bodies, unary atoms are lifted (`∧_i (act(i) ==> b_i)`), and rigid atoms (such as
  *    `active(1)`, `rel(x, 0) <= rel(x, 1)`, or calls of hyperfunctions) are evaluated once;
  *  - a hyperfunction H(ē, x̄) becomes one function H(a₀ … aₖ₋₁, ē, x̄₀ … x̄ₖ₋₁) over the activation flags, its execution
  *    parameters ē (passed once), and the copies of its other parameters for each execution. A call passes the caller's
  *    activation, and the arguments of inactive executions are replaced by placeholders, which the hyperfunction
  *    cannot read since `rel` requires activity. A hyperfunction that reads an execution must require that it is
  *    active (e.g., `requires AllActive()`), which Viper's well-definedness check enforces;
  *  - termination proofs of hyperfunctions stay unary, with relational values replaced by unconstrained ones.
  *
  * @param hyperFuncNames the names of the hyperfunctions
  * @param execParams for each hyperfunction, the positions of its execution parameters
  */
class HyperfuncEncoding(product: ProductConstruction, hyperFuncNames: Set[String], execParams: Map[String, Set[Int]],
                        multiplicity: Multiplicity) {

  private def executions: Range = 0 until product.executions

  // constructors that drop trivially true activation conditions
  private def isTrue(e: Exp): Boolean = e.isInstanceOf[TrueLit]
  private def implies(c: Exp, e: Exp)(at: Exp): Exp = if (isTrue(c)) e else Implies(c, e)(at.pos, at.info, at.errT)
  private def cond(c: Exp, t: Exp, f: Exp)(at: Exp): Exp = if (isTrue(c)) t else CondExp(c, t, f)(at.pos, at.info, at.errT)

  /** The constructs this encoding translates; the product construction translates `low` and `lowEvent` in methods. */
  def isOwnRelational(e: Node): Boolean = e match {
    case _: SIFRelExp | _: HFActiveExp => true
    case f: FuncApp => hyperFuncNames(f.funcname)
    case _ => false
  }

  /** Translates one of the constructs of [[isOwnRelational]] with the activation `act`. */
  def translateOwn(e: Exp, act: Seq[Exp]): Exp = e match {
    case r: SIFRelExp => translateRel(r, act)
    case a: HFActiveExp => translateActive(a, act)
    case f: FuncApp if hyperFuncNames(f.funcname) => translateCall(f, act)
    case _ => e
  }

  private def requireActivation(n: Exp, act: Seq[Exp]): Unit =
    if (act == null || act.contains(null))
      product.reportError(Internal(n, FeatureUnsupported(n, "Relational expressions are not supported in this position.")))

  //////////////////////////////// `active`, `rel`, `low` ////////////////////////////////

  /** The activation condition of execution `i` (an integer expression). */
  private def activeOf(i: Exp, act: Seq[Exp]): Exp = i match {
    case IntLit(n) if n >= 0 && n < act.size => act(n.toInt)
    case _ =>
      executions.init.foldRight(act.last) { (j, rest) =>
        CondExp(EqCmp(i, IntLit(j)(i.pos))(i.pos), act(j), rest)(i.pos, i.info, i.errT)
      }
  }

  def translateActive(a: HFActiveExp, act: Seq[Exp]): Exp = {
    requireActivation(a, act)
    activeOf(a.idx, act)
  }

  def translateRel(r: SIFRelExp, act: Seq[Exp]): Exp = {
    requireActivation(r, act)
    if (!HyperfuncsConfig.enableRel)
      product.reportError(Internal(r, FeatureUnsupported(r, "rel-expressions are an experimental feature and must be explicitly enabled.")))
    val i = r.i.i.toInt
    val value = product.inExecution(r.exp, i, act)
    if (isTrue(act(i))) value else Asserting(activeCondition(act(i), i, r), value)(r.pos, r.info, r.errT)
  }

  /**
    * The activation condition `cond` of execution `i`; errors read "Assertion active(i) might not hold", unless the
    * front end supplies the info of the condition ([[HyperfuncsConfig.activeConditionInfo]]).
    */
  private def activeCondition(cond: Exp, i: Int, at: Node with Infoed with Positioned): Exp =
    HyperfuncsConfig.activeConditionInfo(at, i) match {
      case NoInfo =>
        val shown = HFActiveExp(IntLit(i)(at.pos))(at.pos)
        EqCmp(cond, TrueLit()(at.pos))(at.pos, NoInfo, ReTrafo({ case AssertionFalse(_) => AssertionFalse(shown) }))
      case info => EqCmp(cond, TrueLit()(at.pos))(at.pos, info, NoTrafos)
    }

  /** `low(e)` over the active executions; if every execution is active, the comparisons with execution 0 suffice. */
  def translateLow(l: SIFLowExp, act: Seq[Exp]): Exp = {
    val pairs =
      if (act.forall(isTrue)) executions.tail.map(j => product.lowComparison(l, 0, j, act))
      else for (i <- executions; j <- executions if i < j) yield
        Implies(And(act(i), act(j))(), product.lowComparison(l, i, j, act))(l.pos, l.info, l.errT): Exp
    pairs.reduceLeft((a, b) => And(a, b)(l.pos, l.info, l.errT))
  }

  //////////////////////////////// Calls ////////////////////////////////

  /** Placeholder values for the arguments of inactive executions: a literal, or an abstract function per type. */
  private val placeholderFuncs: mutable.LinkedHashMap[Type, Function] = mutable.LinkedHashMap.empty

  private def placeholder(t: Type): Exp = t match {
    case Int => IntLit(0)()
    case Bool => FalseLit()()
    case Ref => NullLit()()
    case Perm => NoPerm()()
    case SeqType(e) => EmptySeq(e)()
    case SetType(e) => EmptySet(e)()
    case MultisetType(e) => EmptyMultiset(e)()
    case MapType(k, v) => EmptyMap(k, v)()
    case _ =>
      val f = placeholderFuncs.getOrElseUpdate(t,
        Function(product.freshName("hf_placeholder"), Seq.empty, t, Seq.empty, Seq.empty, None)())
      FuncApp(f, Seq.empty)()
  }

  def translateCall(f: FuncApp, act: Seq[Exp]): Exp = {
    requireActivation(f, act)
    val execs = execParams.getOrElse(f.funcname, Set.empty)
    val indexed = f.args.zipWithIndex
    // the arguments for execution parameters are literals or execution parameters, i.e., the same in all executions
    val execArgs = indexed.collect { case (a, k) if execs(k) => product.inExecution(a, 0, act) }
    val valueArgs = executions.flatMap { i =>
      indexed.collect { case (a, k) if !execs(k) =>
        if (isTrue(act(i))) product.inExecution(a, i, act)
        else CondExp(act(i), product.inExecution(a, i, act), placeholder(a.typ))(a.pos, a.info, a.errT)
      }
    }
    val flags = if (product.activationIsTrivial) Seq.empty else act
    // errors show the call as written, not its translation over the executions
    val shown = ReTrafo({ case AssertionFalse(_) => AssertionFalse(f) })
    FuncApp(f.funcname, flags ++ execArgs ++ valueArgs)(f.pos, f.info, f.typ, MakeTrafoPair(shown, f.errT))
  }

  //////////////////////////////// Definitions ////////////////////////////////

  /**
    * Translates a hyperfunction H(x̄) into H(a₀ … aₖ₋₁, ē, x̄₀ … x̄ₖ₋₁), where ē are the execution parameters: its contract
    * and body are translated with the activation flags aᵢ, as assertions over the active executions (permissions of an
    * execution are required only if it is active). Every other parameter has one value per execution: a body that
    * needs a single value uses `rel(n, i)`.
    */
  def translateDefinition(f: Function): Function = {
    // with a trivial activation, the flags are omitted and `true`
    val flagDecls =
      if (product.activationIsTrivial) Seq.empty
      else executions.map(i => LocalVarDecl(product.freshName(s"act$i"), Bool)())
    val flags = if (product.activationIsTrivial) executions.map(_ => TrueLit()(): Exp) else flagDecls.map(_.localVar)
    val execs = execParams.getOrElse(f.name, Set.empty)
    val (execArgs, valueArgs) = f.formalArgs.zipWithIndex.partition { case (_, i) => execs(i) }
    // rigid variables: the execution parameters (and `result`)
    val rigidVars = execArgs.map(_._1.name).toSet
    product.withRigidVariables(rigidVars.toSeq)(product.withParameterCopies(valueArgs.map(_._1)) { copies =>
      def assertion(e: Exp): Exp = hyperAssertion(e, flags, rigidVars)
      // No implicit preconditions: a hyperfunction that reads an execution with `rel` must require that it is active
      // (e.g., `requires AllActive()`); otherwise, Viper's well-definedness check of the hyperfunction fails at the
      // read ("Assertion active(i) might not hold").
      val newPres = f.pres.map(assertion)
      val newPosts = f.posts.map(assertion)
      val newBody = f.body.map(b => if (b.typ == Bool) assertion(b) else hyperValue(b, flags, rigidVars))
      f.copy(formalArgs = flagDecls ++ execArgs.map(_._1) ++ copies.flatten, pres = newPres, posts = newPosts,
        body = newBody)(f.pos, HyperfuncsAnnotations.withoutHyperfunctionAnnotations(f.info), f.errT)
    })
  }

  /**
    * An assertion over the active executions, for a hyperfunction's contract and boolean body. Unary atoms are lifted:
    * they must hold in each active execution. Rigid atoms have one value for all executions and are evaluated once,
    * also where no execution is active; `rigid` are the names of the rigid variables in scope.
    */
  def hyperAssertion(e: Exp, act: Seq[Exp], rigid: Set[String]): Exp = {
    def rec(x: Exp): Exp = hyperAssertion(x, act, rigid)
    def relational(x: Exp): Boolean = product.isRelational(x)
    e match {
      case a@And(l, r) => And(rec(l), rec(r))(a.pos, a.info, a.errT)
      case o@Or(l, r) if relational(o) => Or(rec(l), rec(r))(o.pos, o.info, o.errT)
      case i@Implies(l, r) if relational(i) => Implies(rec(l), rec(r))(i.pos, i.info, i.errT)
      case n@Not(x) if relational(n) => Not(rec(x))(n.pos, n.info, n.errT)
      case c@CondExp(cond, t, f) if relational(c) => CondExp(rec(cond), rec(t), rec(f))(c.pos, c.info, c.errT)
      case l: SIFLowExp => translateLow(l, act)
      case q@Forall(vars, triggers, body) if relational(q) => product.withRigidVariables(vars.map(_.name)) {
        val newTriggers = triggers.flatMap(t => executions.map(i =>
          Trigger(t.exps.map(x => product.inExecution(x, i, act)))(t.pos, t.info, t.errT))).distinct
        Forall(vars, newTriggers, hyperAssertion(body, act, rigid ++ vars.map(_.name)))(q.pos, q.info, q.errT)
      }
      case q@Exists(vars, triggers, body) if relational(q) => product.withRigidVariables(vars.map(_.name)) {
        Exists(vars, triggers, hyperAssertion(body, act, rigid ++ vars.map(_.name)))(q.pos, q.info, q.errT)
      }
      case u@Unfolding(pap: PredicateAccessPredicate, body) if relational(body) =>
        unfoldInEach(u, pap, rec(body), act)
      case e if relational(e) && multiplicity.isRigid(e, rigid) =>
        // a rigid atom: one value for all executions
        product.inExecution(e, 0, act)
      case e =>
        // a unary atom: evaluated in each active execution
        executions.map(i => implies(act(i), product.inExecution(e, i, act))(e))
          .reduceLeft((a, b) => And(a, b)(e.pos, e.info, e.errT))
    }
  }

  /** A non-boolean hyperfunction body, which denotes one value for the tuple of executions. */
  def hyperValue(e: Exp, act: Seq[Exp], rigid: Set[String]): Exp = e match {
    case u@Unfolding(pap: PredicateAccessPredicate, body) if product.isRelational(body) =>
      unfoldInEach(u, pap, hyperValue(body, act, rigid), act)
    case c@CondExp(cond, t, f) if product.isRelational(c) =>
      val newCond = if (product.isRelational(cond)) hyperAssertion(cond, act, rigid) else product.inExecution(cond, 0, act)
      CondExp(newCond, hyperValue(t, act, rigid), hyperValue(f, act, rigid))(c.pos, c.info, c.errT)
    case e => product.inExecution(e, 0, act)
  }

  /** Unfolds the predicate instance in each active execution around `body`. */
  private def unfoldInEach(u: Unfolding, pap: PredicateAccessPredicate, body: Exp, act: Seq[Exp]): Exp =
    executions.foldRight(body) { (i, inner) =>
      val papI = product.inExecution(pap, i, act).asInstanceOf[PredicateAccessPredicate]
      cond(act(i), Unfolding(papI, inner)(u.pos, u.info, u.errT), inner)(u)
    }

  //////////////////////////////// Termination ////////////////////////////////

  /** Unconstrained values, one abstract function per type, applied to a distinct literal at each use. */
  private val unconstrainedFuncs: mutable.LinkedHashMap[Type, Function] = mutable.LinkedHashMap.empty
  private var unconstrainedCounter: Int = 0

  private def unconstrained(t: Type, at: Positioned): Exp = {
    val f = unconstrainedFuncs.getOrElseUpdate(t,
      Function(product.freshName("hf_unconstrained"), Seq(LocalVarDecl("k", Int)()), t, Seq.empty, Seq.empty, None)())
    unconstrainedCounter += 1
    FuncApp(f, Seq(IntLit(unconstrainedCounter)(at.pos)))(at.pos)
  }

  /**
    * The termination proof that Viper's termination plugin generates for a hyperfunction. Termination is a property of
    * each execution, so the proof stays a unary method. Its relational values (`low`, `rel`, `active`, and calls of
    * hyperfunctions) are replaced by unconstrained values, a distinct one at each occurrence, such that every path
    * through the hyperfunction's body is checked; the precondition is made unary by `toUnary`, which may only weaken it.
    */
  def translateTerminationProof(m: Method, toUnary: Exp => Exp): Method = {
    def isRelationalValue(e: Node): Boolean = e match {
      case _: SIFLowExp => true
      case other => isOwnRelational(other)
    }
    val pres = m.pres.map(toUnary)
    val body = m.body.map(b => b.transform { case e: Exp if isRelationalValue(e) => unconstrained(e.typ, e) })
    m.copy(pres = pres, body = body)(m.pos, HyperfuncsAnnotations.withoutHyperfunctionAnnotations(m.info), m.errT)
  }

  /** The functions this encoding introduced (placeholders and unconstrained values), which exist once. */
  def auxiliaryFunctions: Seq[Function] = placeholderFuncs.values.toSeq ++ unconstrainedFuncs.values.toSeq

  def isAuxiliaryFunction(name: String): Boolean = auxiliaryFunctions.exists(_.name == name)
}
