// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra.translator.transformers.hyper

import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.{Config, Hyper}
import viper.gobra.reporting.Source
import viper.gobra.translator.transformers.ViperTransformer
import viper.gobra.translator.util.VprInfo
import viper.gobra.util.Violation
import viper.silver.ast._
import viper.silver.ast.utility.Simplifier
import viper.silver.plugin.standard.predicateinstance.PredicateInstance
import viper.silver.plugin.standard.termination.{DecreasesClause, DecreasesTuple, DecreasesWildcard}
import viper.silver.plugin.sif._
import viper.silver.verifier.{AbstractError, ConsistencyError}

import scala.annotation.{tailrec, unused}

trait SIFLowGuardTransformer {

  /**
    * Constructs the product program of `executions` copies of `p` (2 by default). Relational assertions relate all
    * copies; `rel(e, i)` refers to the copy `i` (0-based at the Viper level).
    */
  def program(
                 p: Program,
                 onlyMajor: String => Boolean,
                 noMinor: Boolean,
                 executions: Int = 2,
               ): Program = {
    require(executions >= 2, s"Expected at least two executions, but got $executions")

    val hyperFuncs = hyperFunctions(p)
    val relPreds = relationalPredicates(p, hyperFuncs)
    val noDups = noDuplicates(p)
    val ctx = newContext(relPreds, noDups, onlyMajor, noMinor, hyperFuncs, Set.empty, executions)

    val members =
      p.methods.flatMap(method(_, ctx)) ++
      p.functions.flatMap(function(_, ctx)) ++
      p.predicates.flatMap(predicate(_, ctx)) ++
      p.fields.flatMap(field(_, ctx)) ++
      p.domains.flatMap(domain(_, ctx))

    p.copy(
      methods = members.collect{ case x: Method => x },
      functions = members.collect{ case x: Function => x },
      predicates = members.collect{ case x: Predicate => x },
      fields = members.collect{ case x: Field => x },
      domains = members.collect{ case x: Domain => x},
    )(p.pos, p.info, p.errT)
  }

  def method(m: Method, ctx: Context): Vector[Member] = {
    if (ctx.onlyMajor(m.name)) {
      Vector(
        m.copy(
          formalArgs = m.formalArgs.map(runLocalVarDecl(_, ctx.major)),
          formalReturns = m.formalReturns.map(runLocalVarDecl(_, ctx.major)),
          pres = m.pres.map(runExp(_, ctx.major)),
          posts = m.posts.map(runExp(_, ctx.major)),
          body = m.body.map(s => toSeqn(runStatement(s, ctx.major))),
        )(m.pos, m.info, m.errT)
      )
    } else {
      Vector(
        m.copy(
          formalArgs = ctx.runs.flatMap(r => m.formalArgs.map(runLocalVarDecl(_, r))),
          formalReturns = ctx.runs.flatMap(r => m.formalReturns.map(runLocalVarDecl(_, r))),
          pres = m.pres.map(assertion(_, ctx)),
          posts = m.posts.map(assertion(_, ctx)),
          body = m.body.map(s => toSeqn(statement(s, ctx))),
        )(m.pos, m.info, m.errT)
      )
    }
  }

  def function(f: Function, ctx: Context): Vector[Member] = {
    if (ctx.isHyperFunction(f.name)) {
      Vector(hyperFunction(f, ctx))
    } else if (ctx.hasNoDuplicates(f.name) || ctx.onlyMajor(f.name)) {
      Vector(runFunction(f, ctx.major))
    } else ctx.runs.map(runFunction(f, _)).toVector
  }

  /**
    * A hyper function abstracts over all executions. It is encoded as a single function that takes the arguments
    * of all executions and whose contract and body are relational. Its result is a single value for all executions.
    */
  def hyperFunction(f: Function, ctx: Context): Function = {
    f.copy(
      formalArgs = ctx.runs.flatMap(r => f.formalArgs.map(runLocalVarDecl(_, r))),
      pres = f.pres.map(assertion(_, ctx)),
      posts = f.posts.map(assertion(_, ctx)),
      body = f.body.map(relationalExp(_, ctx)),
    )(f.pos, VprInfo.removeHyperFunc(f.info), f.errT)
  }

  def runFunction(f: Function, ctx: RunContext): Function = {
    f.copy(
      name = ctx.rename(f.name),
      formalArgs = f.formalArgs.map(runLocalVarDecl(_, ctx)),
      pres = f.pres.map(runExp(_, ctx)),
      posts = f.posts.map(runExp(_, ctx)),
      body = f.body.map(runExp(_, ctx)),
    )(f.pos, f.info, f.errT)
  }

  def predicate(p: Predicate, ctx: Context): Vector[Member] = {
    if (ctx.onlyMajor(p.name)) {
      Vector(runPredicate(p, ctx.major))
    } else {

      val copies = ctx.runs.map(runPredicate(p, _)).toVector

      p.body match {
        case Some(body) if ctx.isRelationalPredicate(p.name) =>
          val args = p.formalArgs.map(_.localVar)
          val relBody = assertionToRelational(body, ctx)
          val accessPredicates = ctx.runs.map { r =>
            val access = runPredicateAccess(p.name, args, r)(body.pos, body.info, body.errT)
            PredicateAccessPredicate(access, Some(WildcardPerm()(body.pos, body.info, body.errT)))(body.pos, body.info, body.errT)
          }

          val func = Function(
            name = ctx.predicateFunctionName(p.name),
            formalArgs = ctx.runs.flatMap(r => p.formalArgs.map(runLocalVarDecl(_, r))),
            typ = Bool,
            pres = accessPredicates,
            posts = Seq.empty,
            body = Some(
              accessPredicates.foldRight(relBody)((acc, b) => Unfolding(acc, b)(body.pos, body.info, body.errT))
            )
          )()

          copies :+ func

        case _ => copies
      }
    }
  }

  def runPredicate(p: Predicate, ctx: RunContext): Predicate = {
    p.copy(
      name = ctx.rename(p.name),
      formalArgs = p.formalArgs.map(runLocalVarDecl(_, ctx)),
      body = p.body.map(b => runExp(assertionToUnary(b, ctx), ctx)), // unary body
    )(p.pos, p.info, p.errT)
  }

  def field(f: Field, ctx: Context): Vector[Member] = {
    if (ctx.hasNoDuplicates(f.name) || ctx.onlyMajor(f.name)) {
      Vector(runField(f, ctx.major))
    } else ctx.runs.map(runField(f, _)).toVector
  }

  def runField(f: Field, ctx: RunContext): Field = {
    f.copy(name = ctx.rename(f.name))(f.pos, f.info, f.errT)
  }

  def domain(d: Domain, ctx: Context): Vector[Member] = {
    val relationalTriggers = d.functions.filter(f => isRelationalTrigger(f.name)).map { f =>
      val formalArgs = f.formalArgs.zipWithIndex.map{
        case (v: LocalVarDecl, _) => v
        case (v: UnnamedLocalVarDecl, idx) => LocalVarDecl(s"""v$idx""", v.typ)(v.pos, v.info, v.errT)
      }

      val newFormalArgs = ctx.runs.flatMap(r => formalArgs.map(runLocalVarDecl(_, r)))
      DomainFunc(ctx.predicateFunctionName(f.name), newFormalArgs, Bool)(f.pos,f.info,d.name,f.errT)
    }

    val relationalAxioms = d.axioms.filter(ax => isRelational(ax.exp, ctx)).map {
      case ax: NamedDomainAxiom => ax.copy(exp = assertion(ax.exp, ctx))(ax.pos, ax.info, ax.domainName, ax.errT)
      case ax: AnonymousDomainAxiom => ax.copy(exp = assertion(ax.exp, ctx))(ax.pos, ax.info, ax.domainName, ax.errT)
    }

    Vector(d.copy(
      functions = d.functions ++ relationalTriggers,
      axioms = d.axioms ++ relationalAxioms,
    )(d.pos,d.info,d.errT))
  }

  def directlyRelational(e: Exp, hyperFuncs: String => Boolean): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case _: SIFRelExp => true
      case f: DomainFuncApp => isRelationalTrigger(f.funcname)
      case f: FuncApp => hyperFuncs(f.funcname)
      case _ => false
    }
  }

  def isRelational(e: Exp, ctx: Context): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case _: SIFRelExp => true
      case f: DomainFuncApp => isRelationalTrigger(f.funcname)
      case f: FuncApp => ctx.isHyperFunction(f.funcname)
      case p: PredicateAccess => ctx.isRelationalPredicate(p.predicateName)
      case _ => false
    }
  }

  /** Viper functions that encode hyper functions are marked with an annotation, see [[VprInfo.maybeAttachHyperFunc]]. */
  def hyperFunctions(p: Program): String => Boolean =
    p.functions.collect{ case f if VprInfo.isHyperFunc(f.info) => f.name }.toSet.contains _

  def relationalPredicates(p: Program, hyperFuncs: String => Boolean): String => Boolean = {
    val (directlyRelationalPredicates, other) = p.predicates.partition(_.body.exists(directlyRelational(_, hyperFuncs)))

    @tailrec
    def iterate(acc: Set[String], other: Seq[Predicate]): Set[String] = {
      val (newAcc, nextOther) = other.partition(p => p.body.exists(_.exists{
        case p: PredicateAccess => acc.contains(p.predicateName)
        case _ => false
      }))
      if (newAcc.isEmpty) acc else iterate(acc ++ newAcc.map(_.name), nextOther)
    }

    iterate(directlyRelationalPredicates.map(_.name).toSet, other).contains _
  }

  def noDuplicates(p: Program): String => Boolean = {
    p.functions.collect{ case f if f.pres.forall(_.isPure) => f.name }.toSet.contains _
  }


  def assertion(e: Exp, ctx: Context): Exp = {
    def positiveUnary(e: Exp, ctx: Context): Exp = {
      e match {
        case d: DecreasesClause =>
          d match {
            case t: DecreasesTuple =>
              // the measures of all executions are compared lexicographically
              val newTuple = ctx.runs.flatMap(r => t.tupleExpressions.map(runExp(_, r)))
              val newCond = t.condition.map(c => disjunction(ctx.runs.map(runExp(c, _)))(c.pos, c.info, c.errT))
              DecreasesTuple(newTuple, newCond)(d.pos, d.info, d.errT)
            case w: DecreasesWildcard =>
              val newCond = w.condition.map(c => disjunction(ctx.runs.map(runExp(c, _)))(c.pos, c.info, c.errT))
              DecreasesWildcard(newCond)(d.pos, d.info, d.errT)
            case d => d
          }
        case _ =>
          conjunction(ctx.runs.map(runExp(e, _)))(e.pos, e.info, e.errT)
      }
    }
    assertionWithPositiveUnary(e, ctx, positiveUnary)
  }

  def assertionWithPositiveUnary(e: Exp, ctx: Context, positiveUnary: (Exp, Context) => Exp): Exp =
    Simplifier.simplify(relationalAssertion(e, ctx, isPositive = true, positiveUnary))

  /**
    * Translates a relational assertion, i.e., an assertion that may contain `low`, `rel`, calls to hyper functions,
    * and relational predicates. Maximal non-relational subexpressions are lifted to hold in all executions.
    * Variables bound by quantifiers with a relational body are rigid, i.e., they denote the same value in all
    * executions. For instance, `forall i :: low(s[i])` states that `s` agrees element-wise in all executions.
    */
  def relationalAssertion(e: Exp, ctx: Context, isPositive: Boolean, positiveUnary: (Exp, Context) => Exp): Exp = {
    def negativeUnary(e: Exp): Exp = conjunction(ctx.runs.map(runExp(e, _)))(e.pos, e.info, e.errT)
    def go(e: Exp, isPositive: Boolean): Exp = relationalAssertion(e, ctx, isPositive, positiveUnary)

    e match {
      case e: Exp if !isRelational(e, ctx) =>
        if (isPositive) positiveUnary(e, ctx) else negativeUnary(e)

      case l: SIFLowExp => comparison(l, ctx)
      case l: SIFLowEventExp => TrueLit()(l.pos, l.info, l.errT)
      case r: SIFRelExp => relationalExp(r, ctx)
      case f: FuncApp if ctx.isHyperFunction(f.funcname) => hyperFuncApp(f, ctx)

      case a: DomainFuncApp =>
        assert(isRelationalTrigger(a.funcname), s"Expected relational trigger, but got $a.")
        a.copy(
          funcname = ctx.predicateFunctionName(a.funcname),
          args = ctx.runs.flatMap(r => a.args.map(runExp(_, r))),
        )(a.pos, a.info, a.typ, a.domainName, a.errT)

      case p: PredicateAccess =>
        assert(isPositive)
        And(positiveUnary(p, ctx), predicateAccess(p.predicateName, p.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)
      case p: PredicateAccessPredicate =>
        assert(isPositive)
        And(positiveUnary(p, ctx), predicateAccess(p.loc.predicateName, p.loc.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)

      case f: Forall =>
        val rigidCtx = ctx.withRigidVariables(f.variables.map(_.name))
        Forall(
          variables = f.variables,
          triggers = f.triggers.flatMap(trigger(_, rigidCtx)),
          exp = relationalAssertion(f.exp, rigidCtx, isPositive, positiveUnary),
        )(f.pos, f.info, f.errT).autoTrigger

      case f: Exists =>
        val rigidCtx = ctx.withRigidVariables(f.variables.map(_.name))
        Exists(
          variables = f.variables,
          triggers = f.triggers.flatMap(trigger(_, rigidCtx)),
          exp = relationalAssertion(f.exp, rigidCtx, isPositive, positiveUnary),
        )(f.pos, f.info, f.errT).autoTrigger

      case a: And => And(go(a.left, isPositive), go(a.right, isPositive))(a.pos, a.info, a.errT)
      case i: Implies => Implies(go(i.left, !isPositive), go(i.right, isPositive))(i.pos, i.info, i.errT)
      case c: CondExp => CondExp(go(c.cond, !isPositive), go(c.thn, isPositive), go(c.els, isPositive))(c.pos, c.info, c.errT)

      case u: Unfolding =>
        ctx.runs.foldRight(go(u.body, isPositive)) { (r, body) =>
          Unfolding(runExp(u.acc, r).asInstanceOf[PredicateAccessPredicate], body)(u.pos, u.info, u.errT)
        }

      case _: AccessPredicate | _: MagicWand => throw new IllegalArgumentException(
        s"Encountered unexpected node during product-program transformation of assertions: $e"
      )

      // All other boolean operators (e.g., `!`, `||`, comparisons). If their non-boolean operands denote the same value
      // in all executions (e.g., `rel(x, 1) < rel(x, 2)`), the operator is applied once. Otherwise, the expression
      // mixes relational and per-execution values (e.g., `rel(j, 1) < i`): it is evaluated in each execution, with
      // the relational subexpressions shared, and lifted to hold in all executions.
      case e if nonBooleanOperands(e).forall(isSingleValued(_, ctx)) => mapRelationalOperands(e, ctx, (b: Exp) => go(b, isPositive))
      case e => conjunction(ctx.runs.map(runWithRelational(e, ctx, _)))(e.pos, e.info, e.errT)
    }
  }

  /**
    * Evaluates `e` in the execution described by `run`, where `e` may contain relational subexpressions. These denote
    * the same value in all executions and are translated once; all other parts of `e` are evaluated in `run`.
    */
  def runWithRelational(e: Exp, ctx: Context, run: RunContext): Exp = e match {
    case e if !isRelational(e, ctx) => runExp(e, run)
    case r: SIFRelExp => relationalExp(r, ctx)
    case f: FuncApp if ctx.isHyperFunction(f.funcname) => hyperFuncApp(f, ctx)
    case l: SIFLowExp => comparison(l, ctx)
    case l: SIFLowEventExp => TrueLit()(l.pos, l.info, l.errT)
    case q: QuantifiedExp => assertion(q, ctx)
    case e => e.withChildren(e.children.map {
      case c: Exp => runWithRelational(c, ctx, run)
      case cs: Seq[_] => cs.map { case c: Exp => runWithRelational(c, ctx, run); case c => c }
      case c => c
    })
  }

  /** Returns whether `e` denotes the same value in all executions, e.g., `rel(x, 1)`, a literal, or a rigid variable. */
  private def isSingleValued(e: Exp, ctx: Context): Boolean = {
    val values = ctx.runs.map(runWithRelational(e, ctx, _))
    values.forall(_ == values.head)
  }

  private def nonBooleanOperands(e: Exp): Seq[Exp] = e.children.flatMap {
    case c: Exp => Seq(c)
    case cs: Seq[_] => cs.collect { case c: Exp => c }
    case _ => Seq.empty
  }.filter(_.typ != Bool)

  /**
    * Translates a relational expression, i.e., an expression that denotes a single value for all executions.
    * Boolean expressions are translated as relational assertions.
    */
  def relationalExp(e: Exp, ctx: Context): Exp = e match {
    case SIFRelExp(e, lit) if 0 <= lit.i && lit.i < ctx.runs.size => runExp(e, ctx.runs(lit.i.toInt))
    case r: SIFRelExp => throw new IllegalArgumentException(s"Unsupported execution identifier in $r")
    case f: FuncApp if ctx.isHyperFunction(f.funcname) => hyperFuncApp(f, ctx)
    case e if e.typ == Bool => assertion(e, ctx)
    case e if !isRelational(e, ctx) =>
      // e must not depend on the execution, e.g., a literal, `result`, or a rigid variable
      val values = ctx.runs.map(runExp(e, _))
      if (values.forall(_ == values.head)) values.head
      else throw new IllegalArgumentException(s"Expected an expression that denotes the same value in all executions, but got $e")
    case e => mapRelationalOperands(e, ctx, (b: Exp) => assertion(b, ctx))
  }

  /** Rebuilds `e` with its operands translated: boolean operands with `bool`, all others as relational expressions. */
  private def mapRelationalOperands(e: Exp, ctx: Context, bool: Exp => Exp): Exp = {
    def operand(c: Exp): Exp = if (c.typ == Bool) bool(c) else relationalExp(c, ctx)
    e.withChildren(e.children.map {
      case c: Exp => operand(c)
      case cs: Seq[_] => cs.map { case c: Exp => operand(c); case c => c }
      case c => c
    })
  }

  /**
    * A call to a hyper function receives the arguments of all executions. Relational (sub)expressions of the arguments
    * denote the same value in all executions.
    */
  def hyperFuncApp(f: FuncApp, ctx: Context): FuncApp = {
    def args(run: RunContext): Seq[Exp] = f.args.map(runWithRelational(_, ctx, run))
    f.copy(args = ctx.runs.flatMap(args))(f.pos, f.info, f.typ, f.errT)
  }

  /**
    * Translates a trigger of a quantifier whose bound variables are rigid. Since a rigid variable denotes the same
    * value in all executions, the trigger terms of any single execution suffice to instantiate the quantifier. Thus,
    * the trigger is translated into one alternative per execution. (A single trigger with the terms of all executions
    * may fail to fire when only some executions' terms are present, e.g., while checking `e₀ == e₁ && e₀ == e₂`.)
    */
  def trigger(t: Trigger, ctx: Context): Vector[Trigger] = {
    def triggerExp(e: Exp, run: RunContext): Exp = if (isRelational(e, ctx)) assertion(e, ctx) else runExp(e, run)
    ctx.runs.map(r => Trigger(t.exps.map(triggerExp(_, r)))(t.pos, t.info, t.errT)).distinct.toVector
  }

  def predicateAccess(p: String, args: Seq[Exp], ctx: Context)(pos: Position, info: Info, errT: ErrorTrafo): FuncApp = {
    FuncApp(
      funcname = ctx.predicateFunctionName(p),
      args = ctx.runs.flatMap(r => args.map(runExp(_, r))),
    )(pos, info, Bool, errT)
  }

  def runPredicateAccess(p: String, args: Seq[Exp], ctx: RunContext)(pos: Position, info: Info, errT: ErrorTrafo): PredicateAccess = {
    PredicateAccess(args = args.map(runExp(_, ctx)), predicateName = ctx.rename(p))(pos, info, errT)
  }

  def runPredicateInstance(p: String, args: Seq[Exp], ctx: RunContext)(pos: Position, info: Info, errT: ErrorTrafo): PredicateInstance = {
    PredicateInstance(args = args.map(runExp(_, ctx)), p = ctx.rename(p))(pos, info, errT)
  }

  /** `low(e)` holds iff `e` has the same value in all executions. */
  def comparison(l: SIFLowExp, ctx: Context): Exp = {
    val values = ctx.runs.map(runExp(l.exp, _))
    conjunction(values.tail.map(v => EqCmp(values.head, v)(l.pos, l.info, l.errT)))(l.pos, l.info, l.errT)
  }

  def conjunction(es: Seq[Exp])(pos: Position, info: Info, errT: ErrorTrafo): Exp =
    es.reduceLeft[Exp]((l, r) => And(l, r)(pos, info, errT))

  def disjunction(es: Seq[Exp])(pos: Position, info: Info, errT: ErrorTrafo): Exp =
    es.reduceLeft[Exp]((l, r) => Or(l, r)(pos, info, errT))

  def assertionToUnary(e: Exp, @unused ctx: Context): Exp = {
    e.transform{
      case l: SIFLowExp => TrueLit()(l.pos, l.info, l.errT)
    }
  }

  def assertionToRelational(e: Exp, ctx: Context): Exp = {
    def positiveUnary(e: Exp, @unused ctx: Context): Exp = TrueLit()(e.pos, e.info, e.errT)
    assertionWithPositiveUnary(e, ctx, positiveUnary)
  }


  def runExp(e: Exp, ctx: RunContext): Exp = {
    def go(e: Exp): Exp = runExp(e, ctx)

    val reducedE = removeMinorExp(e, ctx)

    reducedE.transform {
      case v: LocalVar => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case v: LocalVarDecl => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case f: Field => runField(f, ctx)
      case f: FuncApp if ctx.isHyperFunction(f.funcname) =>
        Violation.violation(s"Unexpected call to hyper function in a non-relational position: $f")
      case f: FuncApp => f.copy(funcname = ctx.rename(f.funcname), args = f.args.map(go))(f.pos, f.info, f.typ, f.errT)
      case p: PredicateAccess => runPredicateAccess(p.predicateName, p.args, ctx)(p.pos, p.info, p.errT)
      case p: PredicateInstance => runPredicateInstance(p.p, p.args, ctx)(p.pos, p.info, p.errT)
      case l: SIFLowExp => TrueLit()(l.pos, l.info, l.errT)
      case l: SIFLowEventExp => TrueLit()(l.pos, l.info, l.errT)
    }
  }

  def hasMajorOnly(n: Node, ctx: Context): Boolean = n.exists {
    case v: LocalVar => ctx.onlyMajor(v.name)
    case v: LocalVarDecl => ctx.onlyMajor(v.name)
    case f: Field => ctx.onlyMajor(f.name)
    case f: FuncApp => ctx.onlyMajor(f.funcname)
    case f: DomainFuncApp => ctx.onlyMajor(f.funcname)
    case p: PredicateAccess => ctx.onlyMajor(p.predicateName)
    case m: MethodCall => ctx.onlyMajor(m.methodName)
    case _ => false
  }

  def removeMinorExp(e: Exp, ctx: RunContext): Exp = {
    if (!ctx.removeMinor || e.typ != Bool) e
    else {

      def go(e: Exp): Reduction[Exp] = e match {
        case e: And =>
          (go(e.left), go(e.right)) match {
            case (Reduction.Inf, y) => y.increment
            case (x, Reduction.Inf) => x.increment
            case (x, y) => for {l <- x; r <- y} yield And(l, r)(e.pos, e.info, e.errT)
          }

        case e: Or => for {l <- go(e.left).strict; r <- go(e.right).strict} yield Or(l,r)(e.pos, e.info, e.errT)
        case e: Implies => for {l <- go(e.left).strict; r <- go(e.right)} yield Implies(l,r)(e.pos, e.info, e.errT)

        case e: CondExp =>
          (go(e.cond).strict, go(e.thn), go(e.els)) match {
            case (Reduction.Zero(c), Reduction.Inf, y) => for {r <- y} yield Implies(c,r)(e.pos, e.info, e.errT)
            case (Reduction.Zero(c), x, Reduction.Inf) => for {l <- x} yield Implies(Not(c)(e.pos, e.info, e.errT),l)(e.pos, e.info, e.errT)
            case (z, x, y) => for {c <- z; l <- x; r <- y} yield CondExp(c,l,r)(e.pos, e.info, e.errT)
          }

        case e: Forall => for { body <- go(e.exp) } yield e.copy(exp = body)(e.pos, e.info, e.errT)
        case e: Exists => for { body <- go(e.exp) } yield e.copy(exp = body)(e.pos, e.info, e.errT)

        case e: Unfolding => for { x <- go(e.acc); y <- go(e.body) } yield Unfolding(x.asInstanceOf[PredicateAccessPredicate], y)(e.pos, e.info, e.errT)

        case e if e.typ == Bool && hasMajorOnly(e,ctx) => Reduction.Inf // atomic exp with removable sub-exp
        case e => Reduction.Zero(e)
      }

      go(e).toOption.getOrElse(TrueLit()(e.pos, e.info, e.errT))
    }
  }

  def runLocalVarDecl(v: LocalVarDecl, ctx: RunContext): LocalVarDecl = {
    v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
  }

  def statement(s: Stmt, ctx: Context): Stmt = {

    def synced(s: Stmt): Boolean = s match {
      case i: Inhale => isRelational(i.exp, ctx)
      case e: Exhale => isRelational(e.exp, ctx)
      case a: Assume => isRelational(a.exp, ctx)
      case a: Assert => isRelational(a.exp, ctx)
      case f: Fold => isRelational(f.acc.loc, ctx)
      case u: Unfold => isRelational(u.acc.loc, ctx)
      case m: MethodCall => !ctx.onlyMajor(m.methodName)
      case _: If | _: While | _: Goto | _: Label | _: Seqn => true
      case _ => false
    }

    def unary(s: Stmt): Stmt = {
      Seqn(ctx.runs.map(runStatement(s, _)), Seq.empty)(s.pos, s.info, s.errT)
    }

    s match {
      case s if !synced(s) => unary(s)

      case s: Inhale => Inhale(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Exhale => Exhale(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Assume => Assume(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Assert => Assert(assertion(s.exp, ctx))(s.pos, s.info, s.errT)

      case s: Fold =>
        Seqn(
          ctx.runs.map(runStatement(s, _)) :+ Assert(assertion(s.acc, ctx))(s.pos, s.info, s.errT),
          Seq.empty
        )(s.pos, s.info, s.errT)

      case s: Unfold =>
        Seqn(
          Assert(assertion(s.acc, ctx))(s.pos, s.info, s.errT) +: ctx.runs.map(runStatement(s, _)),
          Seq.empty
        )(s.pos, s.info, s.errT)

      case s: MethodCall =>
        s.copy(
          args = ctx.runs.flatMap(r => s.args.map(runExp(_, r))),
          targets = ctx.runs.flatMap(r => s.targets.map(runExp(_, r).asInstanceOf[LocalVar]))
        )(s.pos, s.info, s.errT)

      case s: Seqn =>
        val declarations = s.scopedDecls.flatMap{
          case x: LocalVarDecl => ctx.runs.map(runLocalVarDecl(x, _))
          case x => Seq(x)
        }
        Seqn(s.ss.map(statement(_, ctx)), declarations)(s.pos, s.info, s.errT)

      // A relational condition denotes the same value in all executions (e.g., `ghost if low(x)`). Thus, all
      // executions take the same branch, and the branch condition does not have to be checked to be low.
      case s: If if isRelational(s.cond, ctx) =>
        If(
          cond = assertion(s.cond, ctx),
          thn = toSeqn(statement(s.thn, ctx)),
          els = toSeqn(statement(s.els, ctx))
        )(s.pos, s.info, s.errT)

      case s: If =>
        Seqn(Seq(
          Assert(comparison(SIFLowExp(s.cond)(s.pos, s.info, s.errT), ctx))(s.pos, s.info, s.errT),
          If(
            cond = runExp(s.cond, ctx.major),
            thn = toSeqn(statement(s.thn, ctx)),
            els = toSeqn(statement(s.els, ctx))
          )(s.pos, s.info, s.errT),
        ), Seq.empty)(s.pos, s.info, s.errT)

      case s: While =>
        While(
          cond = runExp(s.cond, ctx.major),
          invs = comparison(SIFLowExp(s.cond)(s.pos, s.info, s.errT), ctx) +: s.invs.map(assertion(_, ctx)),
          body = toSeqn(statement(s.body, ctx))
        )(s.pos, s.info, s.errT)

      case s: Label => s
      case s: Goto => s

      case s => throw new IllegalArgumentException(
        s"Encountered unexpected node during product-program transformation of statements: $s"
      )

    }
  }

  def runStatement(s: Stmt, ctx: RunContext): Stmt = {
    val reducedStmt = removeMinorStmt(s, ctx)
    reducedStmt.transform {
      case v: LocalVarDecl => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case f: Field => runField(f, ctx)
      case e: Exp => runExp(e, ctx)
      case p: Package =>
        Package(
          wand = runExp(p.wand, ctx).asInstanceOf[MagicWand],
          proofScript = toSeqn(statement(p.proofScript, ctx)),
        )(p.pos, p.info, p.errT)
    }
  }

  def removeMinorStmt(s: Stmt, ctx: RunContext): Stmt = {
    if (!ctx.removeMinor || !hasMajorOnly(s, ctx)) s
    else Seqn(Seq.empty, Seq.empty)(s.pos, s.info, s.errT)
  }

  /**
    * Verification without hyper properties: hyper functions are dropped, and calls to them are treated like `low`,
    * i.e., they are replaced by `true` and an error is reported if this changes the proof obligations.
    */
  def removeHyper(p: Program): Either[Seq[AbstractError], Program] = {
    val hyperFuncs = hyperFunctions(p)
    val withoutHyperFuncs = p.copy(functions = p.functions.filterNot(f => hyperFuncs(f.name)))(p.pos, p.info, p.errT)
    removeLow(withoutHyperFuncs, hyperFuncs)
  }

  def removeLow[N <: Node](n: N, hyperFuncs: String => Boolean = _ => false): Either[Seq[AbstractError], N] = {
    def memberHasProofObligations(m: Member): Boolean = m match {
      case m: Method => m.body.nonEmpty
      case m: Function => m.body.nonEmpty
      case _ => true // conservative choice
    }

    var errs: Seq[AbstractError] = Seq.empty

    def removeNodeAndReportErrorIfProofObligationsChange(n: Exp, optMember: Option[Member], message: String): (Node, Option[Member]) = {
      if (optMember.forall(memberHasProofObligations)) {
        errs = errs :+ ConsistencyError(message, n.pos)
      }
      (TrueLit()(n.pos, n.info, n.errT), optMember)
    }
    val transformedN = n.transformWithContext[Option[Member]]({
      case (m: Member, _) => (m, Some(m)) // keep node unchanged and propagate `m` as context
      case (n: SIFLowExp, optMember) =>
        removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Low expression found: ${n.exp}")
      case (n: SIFLowEventExp, optMember) =>
        removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Low context expression found")
      case (n: FuncApp, optMember) if hyperFuncs(n.funcname) =>
        if (n.typ != Bool) {
          // a non-boolean hyper function cannot be replaced while preserving well-typedness
          errs = errs :+ ConsistencyError(s"Call to hyper function found: $n", n.pos)
          (n, optMember)
        } else removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Call to hyper function found: $n")
      case (n: SIFRelExp, optMember) =>
        // it's unclear how to remove a `rel(_, _)` expressions while preserving well-typedness. Thus,
        // we always report an error:
        errs = errs :+ ConsistencyError("Rel expression found: $n", n.pos)
        (n, optMember)
    }, None)
    if (errs.isEmpty) Right(transformedN)
    else Left(errs)
  }

  def newContext(
                  _relPreds: String => Boolean,
                  _noDups: String => Boolean,
                  _onlyMajor: String => Boolean,
                  _noMinor: Boolean,
                  _hyperFuncs: String => Boolean,
                  _rigidVars: Set[String],
                  _executions: Int,
                ): Context = {

    trait ContextImpl extends Context {
      override def onlyMajor(n: String): Boolean = _onlyMajor(n)

      override def isRelationalPredicate(p: String): Boolean = _relPreds(p)
      override def predicateFunctionName(n: String): String = s"${n}F"
      override def hasNoDuplicates(n: String): Boolean = _noDups(n)
      override def isHyperFunction(n: String): Boolean = _hyperFuncs(n)
      override def isRigidVariable(n: String): Boolean = _rigidVars.contains(n)
      override def withRigidVariables(ns: Seq[String]): Context =
        newContext(_relPreds, _noDups, _onlyMajor, _noMinor, _hyperFuncs, _rigidVars ++ ns, _executions)

      override lazy val runs: Seq[RunContext] = (0 until _executions).map(new RunContextImpl(_))
    }

    /** The context of the execution with index `i`. Execution 0 is the major execution, all others are minor. */
    class RunContextImpl(i: Int) extends ContextImpl with RunContext {
      override val removeMinor: Boolean = if (i == 0) _noMinor else true
      override def rename(n: String): String =
        if (hasNoDuplicates(n) || isRigidVariable(n)) n else s"${n}$i"
    }

    new ContextImpl {}
  }

  def toSeqn(s: Stmt): Seqn = s match {
    case s: Seqn => s
    case s => Seqn(Seq(s), Seq.empty)(s.pos, s.info, s.errT)
  }

  def isRelationalTrigger(s: String): Boolean = s.startsWith("rel_")
}

trait Context {
  def onlyMajor(@unused n: String): Boolean

  def isRelationalPredicate(@unused p: String): Boolean
  def predicateFunctionName(@unused n: String): String
  def hasNoDuplicates(@unused n: String): Boolean
  def isHyperFunction(@unused n: String): Boolean
  /** Rigid variables are bound by quantifiers in relational assertions and denote the same value in all executions. */
  def isRigidVariable(@unused n: String): Boolean
  def withRigidVariables(ns: Seq[String]): Context

  /** the contexts of all executions related by the product program */
  def runs: Seq[RunContext]
  def major: RunContext = runs.head
  def minor: RunContext = runs(1)
}

trait RunContext extends Context {
  def removeMinor: Boolean
  def rename(@unused x: String): String
}

sealed trait Reduction[+T] {
  def toOption: Option[T]
  def increment: Reduction[T] = toOption match {
    case None => Reduction.Inf
    case Some(x) => Reduction.One(x)
  }
  def strict: Reduction[T]
  def map[Q](f: T => Q): Reduction[Q]
  def flatMap[Q](f: T => Reduction[Q]): Reduction[Q]
}

object Reduction {
  case class Zero[T](x: T) extends Reduction[T] {
    override def toOption: Option[T] = Some(x)
    override def strict: Reduction[T] = Zero(x)
    override def map[Q](f: T => Q): Reduction[Q] = Zero(f(x))
    override def flatMap[Q](f: T => Reduction[Q]): Reduction[Q] = f(x)
  }

  case class One[T](x: T) extends Reduction[T] {
    override def toOption: Option[T] = Some(x)
    override def strict: Reduction[T] = Inf
    override def map[Q](f: T => Q): Reduction[Q] = One(f(x))
    override def flatMap[Q](f: T => Reduction[Q]): Reduction[Q] = f(x) match {
      case Zero(y) => One(y)
      case r => r
    }
  }

  case object Inf extends Reduction[Nothing] {
    override def toOption: Option[Nothing] = None
    override def strict: Reduction[Nothing] = Inf
    override def map[Q](f: Nothing => Q): Reduction[Q] = Inf
    override def flatMap[Q](f: Nothing => Reduction[Q]): Reduction[Q] = Inf
  }
}

class SIFLowGuardTransformerImpl(config: Config) extends SIFLowGuardTransformer with ViperTransformer {

  def onlyMajor(p: Program): String => Boolean = {
    def nameBased(n: String): Boolean = n match {
      case n if n.endsWith("termination_proof") => true
      case _ => false
    }

    val annotationBased = p.members.flatMap{ m =>
      Source.unapply(m).flatMap{ code =>
        if (code.origin.tag.contains("#[MAJOR]")) Some(m.name)
        else None
      }
    }.toSet

    (x: String) => nameBased(x) || annotationBased.contains(x)
  }

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    config.hyperModeOrDefault match {
      case Hyper.Enabled =>
        Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = false, config.hyperExecutionsOrDefault), task.backtrack))

      case Hyper.Disabled =>
        removeHyper(task.program).map(BackendVerifier.Task(_, task.backtrack))

      case Hyper.NoMajor =>
        Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = true, config.hyperExecutionsOrDefault), task.backtrack))

      case Hyper.EnabledExtended => Violation.violation(s"SIFLowGuardTransformer does not support hyper mode 'extended'")
    }
  }
}
