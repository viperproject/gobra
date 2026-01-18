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
import viper.gobra.util.Violation
import viper.silver.ast._
import viper.silver.ast.utility.Simplifier
import viper.silver.plugin.standard.predicateinstance.PredicateInstance
import viper.silver.plugin.standard.termination.{DecreasesClause, DecreasesTuple, DecreasesWildcard}
import viper.silver.sif._
import viper.silver.verifier.{AbstractError, ConsistencyError}

import scala.annotation.{tailrec, unused}

trait SIFLowGuardTransformer {

  def program(
                 p: Program,
                 onlyMajor: String => Boolean,
                 noMinor: Boolean,
               ): Program = {

    val relPreds = relationalPredicates(p)
    val noDups = noDuplicates(p)
    val ctx = newContext(relPreds, noDups, onlyMajor, noMinor)

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
          formalArgs = m.formalArgs.map(runLocalVarDecl(_, ctx.major)) ++ m.formalArgs.map(runLocalVarDecl(_, ctx.minor)),
          formalReturns = m.formalReturns.map(runLocalVarDecl(_, ctx.major)) ++ m.formalReturns.map(runLocalVarDecl(_, ctx.minor)),
          pres = m.pres.map(assertion(_, ctx)),
          posts = m.posts.map(assertion(_, ctx)),
          body = m.body.map(s => toSeqn(statement(s, ctx))),
        )(m.pos, m.info, m.errT)
      )
    }
  }

  def function(f: Function, ctx: Context): Vector[Member] = {
    if (ctx.hasNoDuplicates(f.name) || ctx.onlyMajor(f.name)) {
      Vector(runFunction(f, ctx.major))
    } else Vector(runFunction(f, ctx.major), runFunction(f, ctx.minor))
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

      val major = runPredicate(p, ctx.major)
      val minor = runPredicate(p, ctx.minor)

      p.body match {
        case Some(body) if ctx.isRelationalPredicate(p.name) =>
          val args = p.formalArgs.map(_.localVar)
          val relBody = assertionToRelational(body, ctx)
          val majorAccess = runPredicateAccess(p.name, args, ctx.major)(body.pos, body.info, body.errT)
          val minorAccess = runPredicateAccess(p.name, args, ctx.minor)(body.pos, body.info, body.errT)
          val majorAccessPredicate = PredicateAccessPredicate(majorAccess, Some(WildcardPerm()(body.pos, body.info, body.errT)))(body.pos, body.info, body.errT)
          val minorAccessPredicate = PredicateAccessPredicate(minorAccess, Some(WildcardPerm()(body.pos, body.info, body.errT)))(body.pos, body.info, body.errT)

          val func = Function(
            name = ctx.predicateFunctionName(p.name),
            formalArgs = p.formalArgs.map(runLocalVarDecl(_, ctx.major)) ++ p.formalArgs.map(runLocalVarDecl(_, ctx.minor)),
            typ = Bool,
            pres = Seq(majorAccessPredicate, minorAccessPredicate),
            posts = Seq.empty,
            body = Some(
              Unfolding(majorAccessPredicate, Unfolding(minorAccessPredicate, relBody)(body.pos, body.info, body.errT))(body.pos, body.info, body.errT)
            )
          )()

          Vector(major, minor, func)

        case _ => Vector(major, minor)
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
    } else Vector(runField(f, ctx.major), runField(f, ctx.minor))
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

      val newFormalArgs = formalArgs.map(runLocalVarDecl(_, ctx.major)) ++ formalArgs.map(runLocalVarDecl(_, ctx.minor))
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

  def directlyRelational(e: Exp): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case _: SIFRelExp => true
      case f: DomainFuncApp => isRelationalTrigger(f.funcname)
      case _ => false
    }
  }

  def isRelational(e: Exp, ctx: Context): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case _: SIFRelExp => true
      case f: DomainFuncApp => isRelationalTrigger(f.funcname)
      case p: PredicateAccess => ctx.isRelationalPredicate(p.predicateName)
      case _ => false
    }
  }

  // TODO (joao): do sth similar for functions
  def relationalPredicates(p: Program): String => Boolean = {
    val (directlyRelationalPredicates, other) = p.predicates.partition(_.body.exists(directlyRelational))

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
    def positiveUnary(e: Exp): Exp = {
      e match {
        case d: DecreasesClause =>
          d match {
            case t: DecreasesTuple =>
              val tupleMajor = t.tupleExpressions.map(runExp(_, ctx.major))
              val tupleMinor = t.tupleExpressions.map(runExp(_, ctx.minor))
              val condMajor = t.condition.map(runExp(_, ctx.major))
              val condMinor = t.condition.map(runExp(_, ctx.minor))
              val newTuple = tupleMajor ++ tupleMinor
              val newCond = for {
                c1 <- condMajor
                c2 <- condMinor
              } yield Or(c1, c2)(c1.pos, c1.info, c1.errT)
              DecreasesTuple(newTuple, newCond)(d.pos, d.info, d.errT)
            case w: DecreasesWildcard =>
              val condMajor = w.condition.map(runExp(_, ctx.major))
              val condMinor = w.condition.map(runExp(_, ctx.minor))
              val newCond = for {
                c1 <- condMajor
                c2 <- condMinor
              } yield Or(c1, c2)(c1.pos, c1.info, c1.errT)
              DecreasesWildcard(newCond)(d.pos, d.info, d.errT)
            case d => d
          }
        case _ =>
          And(runExp(e, ctx.major), runExp(e, ctx.minor))(e.pos, e.info, e.errT)
      }
    }
    assertionWithPositiveUnary(e, ctx, positiveUnary)
  }

  def assertionWithPositiveUnary(e: Exp, ctx: Context, positiveUnary: Exp => Exp): Exp = {
    def negativeUnary(e: Exp): Exp = And(runExp(e, ctx.major), runExp(e, ctx.minor))(e.pos, e.info, e.errT)

    def go(e: Exp, isPositive: Boolean): Exp = {
      e match {
        case e: Exp if !isRelational(e, ctx) =>
          if (isPositive) positiveUnary(e) else negativeUnary(e)

        case l: SIFLowExp => comparison(l, ctx)
        case l: SIFLowEventExp => TrueLit()(l.pos, l.info, l.errT)
        case SIFRelExp(e, lit) if lit.i == 0 => runExp(e, ctx.major)
        case SIFRelExp(e, lit) if lit.i == 1 => runExp(e, ctx.minor)

        case a: DomainFuncApp =>
          assert(isRelationalTrigger(a.funcname), s"Expected relational trigger, but got $a.")
          a.copy(
            funcname = ctx.predicateFunctionName(a.funcname),
            args = a.args.map(runExp(_, ctx.major)) ++ a.args.map(runExp(_, ctx.minor)),
          )(a.pos, a.info, a.typ, a.domainName, a.errT)

        case p: PredicateAccess =>
          assert(isPositive)
          And(positiveUnary(p), predicateAccess(p.predicateName, p.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)
        case p: PredicateAccessPredicate =>
          assert(isPositive)
          And(positiveUnary(p), predicateAccess(p.loc.predicateName, p.loc.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)

        case f: Forall =>
          Forall(
            variables = f.variables.map(runLocalVarDecl(_, ctx.major)) ++ f.variables.map(runLocalVarDecl(_, ctx.minor)),
            triggers = f.triggers.flatMap(trigger(_, ctx)),
            exp = go(f.exp, isPositive),
          )(f.pos, f.info, f.errT).autoTrigger

        case a: And => And(go(a.left, isPositive), go(a.right, isPositive))(a.pos, a.info, a.errT)
        case i: Implies => Implies(go(i.left, !isPositive), go(i.right, isPositive))(i.pos, i.info, i.errT)
        case c: CondExp => CondExp(go(c.cond, !isPositive), go(c.thn, isPositive), go(c.els, isPositive))(c.pos, c.info, c.errT)
        case e: EqCmp => EqCmp(go(e.left, isPositive), go(e.right, isPositive))(e.pos, e.info, e.errT)

        case u: Unfolding =>
          Unfolding(runExp(u.acc, ctx.major).asInstanceOf[PredicateAccessPredicate],
            Unfolding(runExp(u.acc, ctx.minor).asInstanceOf[PredicateAccessPredicate], go(u.body, isPositive))(u.pos, u.info, u.errT)
          )(u.pos, u.info, u.errT)

        case e => throw new IllegalArgumentException(
          s"Encountered unexpected node during product-program transformation of assertions: $e"
        )
      }
    }

    Simplifier.simplify(go(e, isPositive = true))
  }

  def trigger(t: Trigger, ctx: Context): Vector[Trigger] = {
    def triggerExp(e: Exp): Vector[Exp] = {
      if (isRelational(e, ctx)) Vector(assertion(e, ctx))
      else Vector(runExp(e, ctx.major), runExp(e, ctx.minor))
    }
    Vector(Trigger(t.exps.flatMap(triggerExp))(t.pos,t.info,t.errT))
  }

  def predicateAccess(p: String, args: Seq[Exp], ctx: Context)(pos: Position, info: Info, errT: ErrorTrafo): FuncApp = {
    FuncApp(
      funcname = ctx.predicateFunctionName(p),
      args = args.map(runExp(_, ctx.major)) ++ args.map(runExp(_, ctx.minor)),
    )(pos, info, Bool, errT)
  }

  def runPredicateAccess(p: String, args: Seq[Exp], ctx: RunContext)(pos: Position, info: Info, errT: ErrorTrafo): PredicateAccess = {
    PredicateAccess(args = args.map(runExp(_, ctx)), predicateName = ctx.rename(p))(pos, info, errT)
  }

  def runPredicateInstance(p: String, args: Seq[Exp], ctx: RunContext)(pos: Position, info: Info, errT: ErrorTrafo): PredicateInstance = {
    PredicateInstance(args = args.map(runExp(_, ctx)), p = ctx.rename(p))(pos, info, errT)
  }

  def comparison(l: SIFLowExp, ctx: Context): Exp = {
    EqCmp(runExp(l.exp, ctx.major), runExp(l.exp, ctx.minor))(l.pos, l.info, l.errT)
  }

  def assertionToUnary(e: Exp, @unused ctx: Context): Exp = {
    e.transform{
      case l: SIFLowExp => TrueLit()(l.pos, l.info, l.errT)
    }
  }

  def assertionToRelational(e: Exp, ctx: Context): Exp = {
    def positiveUnary(e: Exp): Exp = TrueLit()(e.pos, e.info, e.errT)
    assertionWithPositiveUnary(e, ctx, positiveUnary)
  }


  def runExp(e: Exp, ctx: RunContext): Exp = {
    def go(e: Exp): Exp = runExp(e, ctx)

    val reducedE = removeMinorExp(e, ctx)

    reducedE.transform {
      case v: LocalVar => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case v: LocalVarDecl => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case f: Field => runField(f, ctx)
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
      Seqn(Seq(runStatement(s, ctx.major), runStatement(s, ctx.minor)), Seq.empty)(s.pos, s.info, s.errT)
    }

    s match {
      case s if !synced(s) => unary(s)

      case s: Inhale => Inhale(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Exhale => Exhale(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Assume => Assume(assertion(s.exp, ctx))(s.pos, s.info, s.errT)
      case s: Assert => Assert(assertion(s.exp, ctx))(s.pos, s.info, s.errT)

      case s: Fold =>
        Seqn(Seq(
          runStatement(s, ctx.major),
          runStatement(s, ctx.minor),
          Assert(assertion(s.acc, ctx))(s.pos, s.info, s.errT),
        ), Seq.empty)(s.pos, s.info, s.errT)

      case s: Unfold =>
        Seqn(Seq(
          Assert(assertion(s.acc, ctx))(s.pos, s.info, s.errT),
          runStatement(s, ctx.major),
          runStatement(s, ctx.minor)
        ), Seq.empty)(s.pos, s.info, s.errT)

      case s: MethodCall =>
        s.copy(
          args = s.args.map(runExp(_, ctx.major)) ++ s.args.map(runExp(_, ctx.minor)),
          targets = s.targets.map(runExp(_, ctx.major).asInstanceOf[LocalVar]) ++ s.targets.map(runExp(_, ctx.minor).asInstanceOf[LocalVar])
        )(s.pos, s.info, s.errT)

      case s: Seqn =>
        val declarations = s.scopedDecls.flatMap{
          case x: LocalVarDecl => Seq(runLocalVarDecl(x, ctx.major), runLocalVarDecl(x, ctx.minor))
          case x => Seq(x)
        }
        Seqn(s.ss.map(statement(_, ctx)), declarations)(s.pos, s.info, s.errT)

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

  def removeLow[N <: Node](n: N): Either[Seq[AbstractError], N] = {
    def memberHasProofObligations(m: Member): Boolean = m match {
      case m: Method => m.body.nonEmpty
      case m: Function => m.body.nonEmpty
      case _ => true // conservative choice
    }

    var errs: Seq[AbstractError] = Seq.empty

    def removeNodeAndReportErrorIfProofObligationsChange(n: ExtensionExp, optMember: Option[Member], message: String): (Node, Option[Member]) = {
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
                ): Context = {

    trait MajorContext extends RunContext { self: Context =>
      override val removeMinor: Boolean = _noMinor
      override def rename(n: String): String =
        if (hasNoDuplicates(n)) n else s"${n}0"
    }

    trait MinorContext extends RunContext { self: Context =>
      override val removeMinor: Boolean = true
      override def rename(n: String): String =
        if (hasNoDuplicates(n)) n else s"${n}1"
    }

    trait ContextImpl extends Context {
      override def onlyMajor(n: String): Boolean = _onlyMajor(n)

      override def isRelationalPredicate(p: String): Boolean = _relPreds(p)
      override def predicateFunctionName(n: String): String = s"${n}F"
      override def hasNoDuplicates(n: String): Boolean = _noDups(n)

      override def major: RunContext = new ContextImpl with MajorContext
      override def minor: RunContext = new ContextImpl with MinorContext
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

  def major: RunContext
  def minor: RunContext
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
        Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = false), task.backtrack))

      case Hyper.Disabled =>
        removeLow(task.program).map(BackendVerifier.Task(_, task.backtrack))

      case Hyper.NoMajor =>
        Right(BackendVerifier.Task(program(task.program, onlyMajor(task.program), noMinor = true), task.backtrack))

      case Hyper.EnabledExtended => Violation.violation(s"SIFLowGuardTransformer does not support hyper mode 'extended'")
    }
  }
}
