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
import viper.silver.ast.utility.{Expressions, Simplifier}
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

    val splits = caseSplitPremises(p, ctx)
    val unaryCopies = methodsNeedingUnaryCopies(p, splits.keySet, ctx)
    val pairCalls = if (executions > 2) pairwiseCallees(p, ctx) else PairwiseCalls.empty

    val members =
      p.methods.flatMap(method(_, ctx, splits, pairCalls)) ++
      p.methods.filter(m => pairCalls.wrapped(m.name)).map(pairwiseWrapper(_, ctx)) ++
      p.methods.filter(m => unaryCopies(m.name)).flatMap(unaryMethodCopies(_, ctx, unaryCopies)) ++
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

  def method(m: Method, ctx: Context, splits: Map[String, Exp] = Map.empty,
             pairCalls: PairwiseCalls = PairwiseCalls.empty): Vector[Member] = {
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
          body = m.body.map { s =>
            val product = pairCalls.redirect(m.name, toSeqn(statement(s, ctx)))
            splits.get(m.name) match {
              case None => product
              case Some(premise) => toSeqn(caseSplitBody(m, premise, product, ctx))
            }
          },
        )(m.pos, m.info, m.errT)
      )
    }
  }

  /////////////////////////////// Pairwise reuse of contracts in k-products (Phase 3) ///////////////////////////////
  //
  // With k > 2 executions, a callee is verified as a k-product: its contract holds for all k-tuples of executions,
  // including tuples that repeat an execution. If the contract relates only executions 0 and 1 (it uses `low`, `rel`
  // with indices 0 and 1, and hyper functions with such bodies), then on the tuple (σᵢ, σⱼ, …, σⱼ) it means the same
  // as on the pair (σᵢ, σⱼ). A call can therefore use the contract for every ordered pair (i, j) of the caller's
  // executions, for instance a helper verified against `NonInterf` on the rows of a 4-execution square. The call is
  // redirected to an abstract wrapper of the callee whose contract consists of these instances:
  //
  //   requires  P on the diagonal (σᵣ, …, σᵣ), for every execution r       (unary parts, resources)
  //   ensures   Q on the diagonal, for every r
  //   ensures   old(P on (σᵢ, σⱼ, …, σⱼ)) ==> Q on (σᵢ, σⱼ, …, σⱼ), for all i ≠ j      (relational parts only)
  //   ensures   old(P) ==> Q                                                   (the k-tuple itself, as before)
  //
  // The wrapper has the callee's termination measure. Calls within a recursive cycle keep calling the callee, so that
  // termination is still checked.

  case class PairwiseCalls(wrapped: Set[String], reaches: (String, String) => Boolean) {
    def wrapperName(n: String): String = s"${n}_pairs"
    def redirect(caller: String, body: Seqn): Seqn = {
      if (wrapped.isEmpty) body
      else body.transform {
        case c: MethodCall if wrapped(c.methodName) && !reaches(c.methodName, caller) =>
          c.copy(methodName = wrapperName(c.methodName))(c.pos, c.info, c.errT)
      }
    }
  }
  object PairwiseCalls {
    val empty: PairwiseCalls = PairwiseCalls(Set.empty, (_, _) => false)
  }

  def pairwiseCallees(p: Program, ctx: Context): PairwiseCalls = {
    val functions = p.functions.map(f => f.name -> f).toMap

    def relatesOnlyFirstTwo(n: Node, seen: Set[String]): Boolean =
      relatesOnlyFirst(n, 2, functions, ctx.isHyperFunction, seen) && !n.exists {
        case a: PredicateAccess => ctx.isRelationalPredicate(a.predicateName)
        case _ => false
      }

    val wrapped = p.methods.collect {
      case m if !ctx.onlyMajor(m.name) && (m.pres ++ m.posts).exists(isRelational(_, ctx)) &&
        (m.pres ++ m.posts).forall(relatesOnlyFirstTwo(_, Set.empty)) => m.name
    }.toSet

    val callees: Map[String, Set[String]] = p.methods.map { m =>
      m.name -> m.body.toSeq.flatMap(_.deepCollect { case c: MethodCall => c.methodName }).toSet
    }.toMap
    def reachable(from: String): Set[String] = {
      @tailrec
      def go(acc: Set[String], frontier: Set[String]): Set[String] = {
        val next = frontier.flatMap(callees.getOrElse(_, Set.empty)) -- acc
        if (next.isEmpty) acc else go(acc ++ next, next)
      }
      go(Set.empty, Set(from))
    }
    val reach = wrapped.map(n => n -> reachable(n)).toMap
    PairwiseCalls(wrapped, (callee, caller) => reach.get(callee).exists(_.contains(caller)))
  }

  def pairwiseWrapper(m: Method, ctx: Context): Method = {
    val k = ctx.runs.size
    val (measures, pres) = m.pres.partition(_.isInstanceOf[DecreasesClause])

    def conj(es: Seq[Exp]): Exp =
      if (es.isEmpty) TrueLit()(m.pos, m.info, m.errT) else conjunction(es)(m.pos, m.info, m.errT)

    // `e` on the tuple (σᵣ, …, σᵣ): relational atoms on the diagonal, unary parts (including resources) once
    def diagonal(e: Exp, r: Int): Exp = {
      val dctx = ctx.withRunIndices(Seq.fill(k)(r))
      Simplifier.simplify(relationalAssertion(e, dctx, isPositive = true, (u, c) => runExp(u, c.runs.head)))
    }
    def onTuple(e: Exp, is: Seq[Int]): Exp = assertionToRelational(e, ctx.withRunIndices(is))
    // the relational parts of `e` on a tuple; the unary parts are required on the diagonal
    def relationalOnTuple(e: Exp, is: Seq[Int]): Exp = Simplifier.simplify(
      relationalAssertion(e, ctx.withRunIndices(is), isPositive = true, (u, _) => TrueLit()(u.pos, u.info, u.errT)))
    def instance(is: Seq[Int]): Exp = Implies(
      Old(onTuple(conj(pres), is))(m.pos, m.info, m.errT),
      onTuple(conj(m.posts), is),
    )(m.pos, m.info, m.errT)

    val pairs = for (i <- 0 until k; j <- 0 until k if i != j) yield i +: Seq.fill(k - 1)(j)

    m.copy(
      name = PairwiseCalls.empty.wrapperName(m.name),
      formalArgs = ctx.runs.flatMap(r => m.formalArgs.map(runLocalVarDecl(_, r))),
      formalReturns = ctx.runs.flatMap(r => m.formalReturns.map(runLocalVarDecl(_, r))),
      // Relational preconditions are obligations of the caller (e.g., `low` arguments of a sink): they must hold on
      // every pair of executions.
      pres = measures.map(assertion(_, ctx)) ++ (0 until k).map(r => conj(pres.map(diagonal(_, r)))) ++
        pairs.map(is => conj(pres.map(relationalOnTuple(_, is)))),
      posts = (0 until k).map(r => conj(m.posts.map(diagonal(_, r)))) ++
        pairs.map(instance) :+ instance(0 until k),
      body = None,
    )(m.pos, m.info, m.errT)
  }

  /////////////////////////////// Relational case splits (Phase 2b) ///////////////////////////////
  //
  // A method whose relational postconditions are all implications `P ==> Q` with a premise `P` that depends only on
  // the method's arguments (directly, or as the body of a hyper function such as `NonInterf(in, out)`) is verified
  // with a case split on the premises:
  //
  //   if (P₁ || … || Pₙ) { product of the body, with low-guard checks }
  //   else { m_U0(args₀); …; m_Uₖ₋₁(argsₖ₋₁) }
  //
  // In the else-branch, all relational postconditions hold vacuously, because the premises are evaluated in the
  // pre-state and arguments are immutable. The executions are therefore related by nothing but their unary
  // contracts, which are provided by the unary copies m_Uᵢ of the method. The copy m_U0 has the body of m translated
  // for execution 0 alone, and it is verified; the copies for the other executions are identical up to renaming and
  // are thus abstract. Their contracts are the unary projections of m's contract: relational atoms are replaced by
  // `true` or `false` such that the projection is implied by the original assertion. Calls in the body of a unary
  // copy go to the unary copies of the callees, which are generated in the same way.

  /** The premises of the methods that are verified with a relational case split. */
  def caseSplitPremises(p: Program, ctx: Context): Map[String, Exp] = {
    val functions = p.functions.map(f => f.name -> f).toMap

    // The body of an opaque hyper function is not available where the postcondition is checked, so its premise
    // would not make the postcondition vacuous there.
    def onlyTerminationPres(f: Function): Boolean =
      f.pres.forall(_.isInstanceOf[DecreasesClause]) && !VprInfo.isOpaque(f.info)

    // A premise must have the same value in the pre-state and in the post-state. Thus, it may read only arguments
    // and must not depend on the heap.
    def admissible(e: Exp, args: Set[String]): Boolean = {
      val bound = e.deepCollect { case q: QuantifiedExp => q.variables.map(_.name) }.flatten.toSet
      !e.exists {
        case _: LocationAccess | _: Unfolding | _: Applying | _: Old | _: LabelledOld | _: CurrentPerm | _: ForPerm => true
        // hyper functions without preconditions, and heap-independent functions (e.g., Go's `%`), read no heap
        case f: FuncApp => !((ctx.isHyperFunction(f.funcname) && functions.get(f.funcname).exists(onlyTerminationPres)) ||
          (!ctx.isHyperFunction(f.funcname) && ctx.hasNoDuplicates(f.funcname)))
        case v: LocalVar => !(args.contains(v.name) || bound.contains(v.name))
        case _ => false
      }
    }

    def premise(e: Exp, args: Set[String], depth: Int): Option[Exp] = e match {
      case i: Implies if isRelational(i.left, ctx) && admissible(i.left, args) => Some(i.left)
      // Gobra encodes `P ==> Q` in the bodies of functions as `P ? Q : true`
      case c: CondExp if c.els == TrueLit()() && isRelational(c.cond, ctx) && admissible(c.cond, args) => Some(c.cond)
      case f: FuncApp if depth > 0 && ctx.isHyperFunction(f.funcname) && VprInfo.executions(f.info).isEmpty =>
        for {
          fn <- functions.get(f.funcname) if onlyTerminationPres(fn)
          body <- fn.body
          scope = f.args.flatMap(_.deepCollect { case v: LocalVar => v.name }).toSet
          res <- premise(Expressions.instantiateVariables(body, fn.formalArgs, f.args, scope), args, depth - 1)
        } yield res
      case _ => None
    }

    def conjuncts(e: Exp): Seq[Exp] = e match {
      case a: And => conjuncts(a.left) ++ conjuncts(a.right)
      case e => Seq(e)
    }

    p.methods.flatMap { m =>
      if (m.body.isEmpty || ctx.onlyMajor(m.name)) None
      else {
        val args = m.formalArgs.map(_.name).toSet
        val relationalPosts = m.posts.flatMap(conjuncts).filter(isRelational(_, ctx))
        val premises = relationalPosts.map(premise(_, args, depth = 3))
        if (relationalPosts.nonEmpty && premises.forall(_.isDefined)) {
          val distinct = premises.flatten.distinct
          Some(m.name -> disjunction(distinct)(distinct.head.pos, distinct.head.info, distinct.head.errT))
        } else None
      }
    }.toMap
  }

  /** The methods that need unary copies: the methods with a case split and, transitively, their callees. */
  def methodsNeedingUnaryCopies(p: Program, roots: Set[String], ctx: Context): Set[String] = {
    val methods = p.methods.map(m => m.name -> m).toMap
    def callees(n: String): Set[String] = methods.get(n).flatMap(_.body).toSeq.flatMap(_.deepCollect {
      case c: MethodCall if !ctx.onlyMajor(c.methodName) && methods.contains(c.methodName) => c.methodName
    }).toSet

    @tailrec
    def closure(acc: Set[String], frontier: Set[String]): Set[String] = {
      val next = frontier.flatMap(callees) -- acc
      if (next.isEmpty) acc else closure(acc ++ next, next)
    }
    closure(roots, roots)
  }

  def unaryMethodName(n: String, run: RunContext): String = run.rename(s"${n}_U")

  def caseSplitBody(m: Method, premise: Exp, product: Seqn, ctx: Context): Stmt = {
    val calls = ctx.runs.map { r =>
      MethodCall(
        unaryMethodName(m.name, r),
        m.formalArgs.map(a => runExp(a.localVar, r)),
        m.formalReturns.map(v => runExp(v.localVar, r).asInstanceOf[LocalVar]),
      )(m.pos, m.info, m.errT)
    }
    If(assertion(premise, ctx), product, Seqn(calls, Seq.empty)(m.pos, m.info, m.errT))(m.pos, m.info, m.errT)
  }

  /** The unary copies of `m`, one per execution. Only the copy for execution 0 has a body. */
  def unaryMethodCopies(m: Method, ctx: Context, unaryCopies: String => Boolean): Vector[Member] = {
    ctx.runs.zipWithIndex.map { case (r, idx) =>
      m.copy(
        name = unaryMethodName(m.name, r),
        formalArgs = m.formalArgs.map(runLocalVarDecl(_, r)),
        formalReturns = m.formalReturns.map(runLocalVarDecl(_, r)),
        pres = m.pres.map(unaryProjection(_, r, ctx)),
        posts = m.posts.map(unaryProjection(_, r, ctx)),
        body = if (idx == 0) m.body.map(s => toSeqn(unaryStatement(s, r, ctx, unaryCopies))) else None,
      )(m.pos, m.info, m.errT)
    }.toVector
  }

  /**
    * The projection of a relational assertion to execution `run`: maximal relational atoms (e.g., `low(x)`, hyper
    * function calls) are replaced by `true` in positive and by `false` in negative positions. The result is implied
    * by the original assertion. Relational predicate instances denote their unary instance in `run`.
    */
  def unaryProjection(e: Exp, run: RunContext, ctx: Context): Exp = {
    def weaken(e: Exp, positive: Boolean): Exp = e match {
      case e if !isRelational(e, ctx) => e
      case _: PredicateAccessPredicate | _: PredicateAccess => e
      case a: And => And(weaken(a.left, positive), weaken(a.right, positive))(a.pos, a.info, a.errT)
      case o: Or => Or(weaken(o.left, positive), weaken(o.right, positive))(o.pos, o.info, o.errT)
      case i: Implies => Implies(weaken(i.left, !positive), weaken(i.right, positive))(i.pos, i.info, i.errT)
      case n: Not => Not(weaken(n.exp, !positive))(n.pos, n.info, n.errT)
      case c: CondExp if !isRelational(c.cond, ctx) =>
        CondExp(c.cond, weaken(c.thn, positive), weaken(c.els, positive))(c.pos, c.info, c.errT)
      case c: CondExp =>
        if (positive) Or(weaken(c.thn, positive), weaken(c.els, positive))(c.pos, c.info, c.errT)
        else And(weaken(c.thn, positive), weaken(c.els, positive))(c.pos, c.info, c.errT)
      case f: Forall => f.copy(exp = weaken(f.exp, positive), triggers = Seq.empty)(f.pos, f.info, f.errT).autoTrigger
      case f: Exists => f.copy(exp = weaken(f.exp, positive), triggers = Seq.empty)(f.pos, f.info, f.errT).autoTrigger
      case u: Unfolding =>
        u.copy(body = weaken(u.body, positive))(u.pos, u.info, u.errT)
      case e if e.typ == Bool => if (positive) TrueLit()(e.pos, e.info, e.errT) else FalseLit()(e.pos, e.info, e.errT)
      case e => Violation.violation(s"Unexpected non-boolean relational assertion: $e")
    }
    Simplifier.simplify(runExp(weaken(e, positive = true), run))
  }

  /** Translates the body of a method for the unary copy of execution `run`. */
  def unaryStatement(s: Stmt, run: RunContext, ctx: Context, unaryCopies: String => Boolean): Stmt = {
    def go(s: Stmt): Stmt = unaryStatement(s, run, ctx, unaryCopies)
    def proj(e: Exp): Exp = unaryProjection(e, run, ctx)

    s match {
      case s: Seqn =>
        val declarations = s.scopedSeqnDeclarations.map {
          case x: LocalVarDecl => runLocalVarDecl(x, run)
          case x => x
        }
        Seqn(s.ss.map(go), declarations)(s.pos, s.info, s.errT)

      // A relational condition has no value in a single execution. The branch is chosen non-deterministically,
      // which over-approximates the executions of the method.
      case s: If if isRelational(s.cond, ctx) =>
        val choice = LocalVarDecl(run.rename(s"relationalChoice_${s.pos.hashCode.abs}"), Bool)(s.pos, s.info, s.errT)
        Seqn(Seq(If(choice.localVar, toSeqn(go(s.thn)), toSeqn(go(s.els)))(s.pos, s.info, s.errT)), Seq(choice))(s.pos, s.info, s.errT)
      case s: If => If(runExp(s.cond, run), toSeqn(go(s.thn)), toSeqn(go(s.els)))(s.pos, s.info, s.errT)
      case s: While => While(runExp(s.cond, run), s.invs.map(proj), toSeqn(go(s.body)))(s.pos, s.info, s.errT)

      case s: Inhale => Inhale(proj(s.exp))(s.pos, s.info, s.errT)
      case s: Exhale => Exhale(proj(s.exp))(s.pos, s.info, s.errT)
      case s: Assume => Assume(proj(s.exp))(s.pos, s.info, s.errT)
      case s: Assert => Assert(proj(s.exp))(s.pos, s.info, s.errT)

      case s: MethodCall if unaryCopies(s.methodName) =>
        val call = runStatement(s, run).asInstanceOf[MethodCall]
        call.copy(methodName = unaryMethodName(s.methodName, run))(call.pos, call.info, call.errT)

      case s => runStatement(s, run)
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
    val rigid = rigidParameters(f)
    f.copy(
      formalArgs = ctx.runs.flatMap(r => f.formalArgs.map(runLocalVarDecl(_, r))),
      pres = f.pres.map(assertion(_, ctx)),
      posts = f.posts.map(e => assertion(readRigidParameters(e, rigid), ctx)),
      body = f.body.map(e => relationalExp(readRigidParameters(e, rigid), ctx)),
    )(f.pos, VprInfo.removeHyperFunc(f.info), f.errT)
  }

  /**
    * The rigid parameters of a hyper function: parameters `n` with a top-level precondition `low(n)`. They have the
    * same value in all executions, so the body and the postcondition may use them as single values.
    */
  def rigidParameters(f: Function): Set[String] = {
    def conjuncts(e: Exp): Seq[Exp] = e match {
      case a: And => conjuncts(a.left) ++ conjuncts(a.right)
      case e => Seq(e)
    }
    val params = f.formalArgs.map(_.name).toSet
    f.pres.flatMap(conjuncts).collect { case SIFLowExp(v: LocalVar, _, _) if params(v.name) => v.name }.toSet
  }

  /** Reads the rigid parameters outside `rel` and `low` from execution 0, which is sound under `low(n)`. */
  def readRigidParameters(e: Exp, rigid: Set[String]): Exp = {
    def go(e: Exp): Exp = e match {
      case _: SIFRelExp | _: SIFLowExp => e
      case v: LocalVar if rigid(v.name) => SIFRelExp(v, IntLit(0)(v.pos, v.info, v.errT))(v.pos, v.info, v.errT)
      case e => e.withChildren(e.children.map {
        case c: Exp => go(c)
        case cs: Seq[_] => cs.map { case c: Exp => go(c); case c => c }
        case c => c
      })
    }
    if (rigid.isEmpty) e else go(e)
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

      case l: Let => relationalLet(l, ctx, (body, c) => relationalAssertion(body, c, isPositive, positiveUnary))

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
    case l: Let if isRelational(l, ctx) => relationalLet(l, ctx, (body, c) => relationalExp(body, c))
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

  /**
    * Translates a let expression in a relational context (e.g., Gobra's encoding of `===` on struct values binds the
    * operands). If the bound expression is relational, it denotes a single value, so the variable is rigid. Otherwise,
    * the variable is bound once per execution.
    */
  private def relationalLet(l: Let, ctx: Context, body: (Exp, Context) => Exp): Exp = {
    if (isRelational(l.exp, ctx)) {
      val bound = if (l.exp.typ == Bool) assertion(l.exp, ctx) else relationalExp(l.exp, ctx)
      Let(l.variable, bound, body(l.body, ctx.withRigidVariables(Seq(l.variable.name))))(l.pos, l.info, l.errT)
    } else {
      ctx.runs.distinctBy(_.index).foldRight(body(l.body, ctx)) { (r, b) =>
        Let(runLocalVarDecl(l.variable, r), runExp(l.exp, r), b)(l.pos, l.info, l.errT)
      }
    }
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
    // A re-indexed call `H@(i₀, …, iₘ₋₁)(…)` evaluates `H` on the selected executions. If m < k, the tuple is padded
    // by repeating the last selected execution, which is sound since `H` relates only its first m executions (see
    // [[reindexingErrors]]).
    val callRuns = VprInfo.executions(f.info) match {
      case Some(is) =>
        val padded = is ++ Seq.fill(ctx.runs.size - is.size)(is.last)
        ctx.withRunIndices(padded.map(i => ctx.runs(i).index)).runs
      case None => ctx.runs
    }
    f.copy(args = callRuns.flatMap(args))(f.pos, VprInfo.removeExecutions(f.info), f.typ, f.errT)
  }

  /**
    * The m-ary prefix of a k-tuple padded with its last element denotes the same value of a hyper function as the
    * m-tuple only if the hyper function relates only its first m executions. Returns an error for each re-indexed call
    * with m < k to a hyper function that may relate more executions.
    */
  def reindexingErrors(p: Program, hyperFuncs: String => Boolean, executions: Int): Seq[AbstractError] = {
    val functions = p.functions.map(f => f.name -> f).toMap
    p.deepCollect {
      case f: FuncApp if hyperFuncs(f.funcname) && VprInfo.executions(f.info).exists(_ != (0 until executions)) &&
        functions.get(f.funcname).exists(isHeapDependent) =>
        ConsistencyError(s"The hyperfunc ${f.funcname} reads the heap; it can only be applied to all ${executions} executions in order.", f.pos)
      case f: FuncApp if hyperFuncs(f.funcname) && VprInfo.executions(f.info).exists(_.size < executions) &&
        !functions.get(f.funcname).exists(fn => relatesOnlyFirst(fn, VprInfo.executions(f.info).get.size, functions, hyperFuncs)) =>
        ConsistencyError(s"The hyperfunc ${f.funcname} may relate more than the ${VprInfo.executions(f.info).get.size} selected executions; select all ${executions} executions.", f.pos)
    }
  }

  /**
    * Whether the hyper function `fn` reads the heap. Each execution has its own copy of the heap, and the i-th
    * arguments of a hyper function are evaluated in the heap of execution i. Thus, `fn` cannot be applied to a tuple
    * of executions that differs from (0, …, k-1), for instance a padded or permuted one.
    */
  def isHeapDependent(fn: Function): Boolean =
    fn.pres.exists(!_.isPure) || fn.body.exists(_.exists { case _: FieldAccess | _: Unfolding => true; case _ => false })

  /** Whether the body and contract of `fn` relate only the executions 0 … m-1. */
  def relatesOnlyFirst(fn: Function, m: Int, functions: Map[String, Function], hyperFuncs: String => Boolean): Boolean =
    !isHeapDependent(fn) && fn.body.nonEmpty && (fn.body.toSeq ++ fn.pres ++ fn.posts).forall(relatesOnlyFirst(_, m, functions, hyperFuncs, Set(fn.name)))

  def relatesOnlyFirst(n: Node, m: Int, functions: Map[String, Function], hyperFuncs: String => Boolean, seen: Set[String]): Boolean = !n.exists {
    case SIFRelExp(_, lit) => lit.i >= m
    case f: FuncApp if hyperFuncs(f.funcname) => VprInfo.executions(f.info) match {
      case Some(is) => is.exists(_ >= m) // the callee itself is checked at its own call
      case None if seen(f.funcname) => false
      case None => functions.get(f.funcname) match {
        case Some(fn) if isHeapDependent(fn) => true // cannot be evaluated on a padded tuple, see [[isHeapDependent]]
        case Some(fn) if fn.body.nonEmpty =>
          !(fn.body.toSeq ++ fn.pres ++ fn.posts).forall(relatesOnlyFirst(_, m, functions, hyperFuncs, seen + f.funcname))
        case _ => true // abstract hyper functions may relate any executions
      }
    }
    case _ => false
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
                  _runIndices: Option[Seq[Int]] = None,
                ): Context = {

    trait ContextImpl extends Context {
      override def onlyMajor(n: String): Boolean = _onlyMajor(n)

      override def isRelationalPredicate(p: String): Boolean = _relPreds(p)
      override def predicateFunctionName(n: String): String = s"${n}F"
      override def hasNoDuplicates(n: String): Boolean = _noDups(n)
      override def isHyperFunction(n: String): Boolean = _hyperFuncs(n)
      override def isRigidVariable(n: String): Boolean = _rigidVars.contains(n)
      override def withRigidVariables(ns: Seq[String]): Context =
        newContext(_relPreds, _noDups, _onlyMajor, _noMinor, _hyperFuncs, _rigidVars ++ ns, _executions, _runIndices)
      override def withRunIndices(is: Seq[Int]): Context =
        newContext(_relPreds, _noDups, _onlyMajor, _noMinor, _hyperFuncs, _rigidVars, _executions, Some(is))

      override lazy val runs: Seq[RunContext] =
        _runIndices.getOrElse(0 until _executions).map(new RunContextImpl(_))
    }

    /** The context of the execution with index `i`. Execution 0 is the major execution, all others are minor. */
    class RunContextImpl(i: Int) extends ContextImpl with RunContext {
      override val index: Int = i
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
  /**
    * The same context, but relating the executions with the given indices (in this order) instead of all executions.
    * Indices may repeat, e.g., `Seq(i, j, j)` evaluates a relational assertion on the tuple (σᵢ, σⱼ, σⱼ).
    */
  def withRunIndices(is: Seq[Int]): Context

  /** the contexts of all executions related by the product program */
  def runs: Seq[RunContext]
  def major: RunContext = runs.head
  def minor: RunContext = runs(1)
}

trait RunContext extends Context {
  /** the index of the execution */
  def index: Int
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
        val errs = reindexingErrors(task.program, hyperFunctions(task.program), config.hyperExecutionsOrDefault)
        if (errs.nonEmpty) Left(errs)
        else Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = false, config.hyperExecutionsOrDefault), task.backtrack))

      case Hyper.Disabled =>
        removeHyper(task.program).map(BackendVerifier.Task(_, task.backtrack))

      case Hyper.NoMajor =>
        Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = true, config.hyperExecutionsOrDefault), task.backtrack))

      case Hyper.EnabledExtended => Violation.violation(s"SIFLowGuardTransformer does not support hyper mode 'extended'")
    }
  }
}
