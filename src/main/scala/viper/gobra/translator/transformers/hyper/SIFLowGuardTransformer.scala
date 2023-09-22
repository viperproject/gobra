package viper.gobra.translator.transformers.hyper

import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.transformers.ViperTransformer
import viper.silver.ast._
import viper.silver.ast.utility.Simplifier
import viper.silver.verifier.AbstractError

import scala.annotation.{tailrec, unused}

trait SIFLowGuardTransformer {

  def program(
                 p: Program,
                 noNI: Method => Boolean,
                 noDups: String => Boolean,
               ): Program = {

    val relPreds = relationalPredicates(p)
    val moreNoDups = noDuplicates(p)
    val ctx = newContext(relPreds, s => noDups(s) || moreNoDups(s), noNI)

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
    if (ctx.noNI(m)) {
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
    if (ctx.hasNoDuplicates(f.name)) {
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
    val major = runPredicate(p, ctx.major)
    val minor = runPredicate(p, ctx.minor)

    p.body match {
      case Some(body) if ctx.isRelationalPredicate(p.name) =>
        val args = p.formalArgs.map(_.localVar)
        val relBody = assertionToRelational(body, ctx)
        val majorAccess = runPredicateAccess(p.name, args, ctx.major)(body.pos, body.info, body.errT)
        val minorAccess = runPredicateAccess(p.name, args, ctx.minor)(body.pos, body.info, body.errT)
        val majorAccessPredicate = PredicateAccessPredicate(majorAccess, WildcardPerm()(body.pos, body.info, body.errT))(body.pos, body.info, body.errT)
        val minorAccessPredicate = PredicateAccessPredicate(minorAccess, WildcardPerm()(body.pos, body.info, body.errT))(body.pos, body.info, body.errT)

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

  def runPredicate(p: Predicate, ctx: RunContext): Predicate = {
    p.copy(
      name = ctx.rename(p.name),
      formalArgs = p.formalArgs.map(runLocalVarDecl(_, ctx)),
      body = p.body.map(b => runExp(assertionToUnary(b, ctx), ctx)), // unary body
    )(p.pos, p.info, p.errT)
  }

  def field(f: Field, ctx: Context): Vector[Member] = {
    if (ctx.hasNoDuplicates(f.name)) {
      Vector(runField(f, ctx.major))
    } else Vector(runField(f, ctx.major), runField(f, ctx.minor))
  }

  def runField(f: Field, ctx: RunContext): Field = {
    f.copy(name = ctx.rename(f.name))(f.pos, f.info, f.errT)
  }

  def domain(d: Domain, @unused ctx: Context): Vector[Member] = Vector(d)


  def directlyRelational(e: Exp): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case _ => false
    }
  }

  def isRelational(e: Exp, ctx: Context): Boolean = {
    e.exists {
      case _: SIFLowExp => true
      case p: PredicateAccess => ctx.isRelationalPredicate(p.predicateName)
      case _ => false
    }
  }

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
    def positiveUnary(e: Exp): Exp = And(runExp(e, ctx.major), runExp(e, ctx.minor))(e.pos, e.info, e.errT)
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

        case p: PredicateAccess =>
          assert(isPositive)
          And(positiveUnary(p), predicateAccess(p.predicateName, p.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)
        case p: PredicateAccessPredicate =>
          assert(isPositive)
          And(positiveUnary(p), predicateAccess(p.loc.predicateName, p.loc.args, ctx)(p.pos, p.info, p.errT))(p.pos, p.info, p.errT)

        case f: Forall =>
          Forall(
            variables = f.variables.map(runLocalVarDecl(_, ctx.major)) ++ f.variables.map(runLocalVarDecl(_, ctx.minor)),
            triggers = f.triggers.map(t => Trigger(t.exps.map(runExp(_, ctx.major)))(t.pos, t.info, t.errT)) ++
              f.triggers.map(t => Trigger(t.exps.map(runExp(_, ctx.minor)))(t.pos, t.info, t.errT)),
            exp = go(f.exp, isPositive),
          )(f.pos, f.info, f.errT).autoTrigger

        case a: And => And(go(a.left, isPositive), go(a.right, isPositive))(a.pos, a.info, a.errT)
        case i: Implies => Implies(go(i.left, !isPositive), go(i.right, isPositive))(i.pos, i.info, i.errT)
        case c: CondExp => CondExp(go(c.cond, !isPositive), go(c.thn, isPositive), go(c.els, isPositive))(c.pos, c.info, c.errT)

        case u: Unfolding =>
          Unfolding(runExp(u.acc, ctx.major).asInstanceOf[PredicateAccessPredicate],
            Unfolding(runExp(u.acc, ctx.major).asInstanceOf[PredicateAccessPredicate], go(u.body, isPositive))(u.pos, u.info, u.errT)
          )(u.pos, u.info, u.errT)

        case e => throw new IllegalArgumentException(
          s"Encountered unexpected node during product-program transformation of assertions: $e"
        )
      }
    }

    Simplifier.simplify(go(e, isPositive = true))
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

    e.transform {
      case v: LocalVar => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case v: LocalVarDecl => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case f: Field => runField(f, ctx)
      case f: FuncApp => f.copy(funcname = ctx.rename(f.funcname), args = f.args.map(go))(f.pos, f.info, f.typ, f.errT)
      case p: PredicateAccess => runPredicateAccess(p.predicateName, p.args, ctx)(p.pos, p.info, p.errT)
      case l: SIFLowExp => TrueLit()(l.pos, l.info, l.errT)
      case l: SIFLowEventExp => TrueLit()(l.pos, l.info, l.errT)
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
      case _: MethodCall | _: If | _: While | _: Goto | _: Label | _: Seqn => true
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
          invs = s.invs.map(assertion(_, ctx)),
          body = toSeqn(statement(s.body, ctx))
        )(s.pos, s.info, s.errT)

        While(
          cond = runExp(s.cond, ctx.major),
          invs = s.invs.map(assertion(_, ctx)),
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
    s.transform {
      case v: LocalVarDecl => v.copy(name = ctx.rename(v.name))(v.pos, v.info, v.errT)
      case f: Field => runField(f, ctx)
      case e: Exp => runExp(e, ctx)
    }
  }

  def newContext(
                  _relPreds: String => Boolean,
                  _noDubs: String => Boolean,
                  _noNI: Method => Boolean,
                ): Context = {

    trait MajorContext extends RunContext { self: Context =>
      override def isMajor: Boolean = true
      override def rename(n: String): String =
        if (hasNoDuplicates(n)) n else s"${n}0"
    }

    trait MinorContext extends RunContext { self: Context =>
      override def isMajor: Boolean = false
      override def rename(n: String): String =
        if (hasNoDuplicates(n)) n else s"${n}1"
    }

    trait ContextImpl extends Context {
      override def isRelationalPredicate(p: String): Boolean = _relPreds(p)
      override def predicateFunctionName(n: String): String = s"${n}F"
      override def hasNoDuplicates(n: String): Boolean = _noDubs(n)
      override def noNI(m: Method): Boolean = _noNI(m)
      override def major: RunContext = new ContextImpl with MajorContext
      override def minor: RunContext = new ContextImpl with MinorContext
    }

    new ContextImpl {}
  }

  def toSeqn(s: Stmt): Seqn = s match {
    case s: Seqn => s
    case s => Seqn(Seq(s), Seq.empty)(s.pos, s.info, s.errT)
  }
}

trait Context {
  def isRelationalPredicate(@unused p: String): Boolean
  def predicateFunctionName(@unused n: String): String
  def hasNoDuplicates(@unused n: String): Boolean
  def noNI(@unused m: Method): Boolean

  def major: RunContext
  def minor: RunContext
}

trait RunContext extends Context {
  def isMajor: Boolean
  def rename(@unused x: String): String
}

class SIFLowGuardTransformerImpl extends SIFLowGuardTransformer with ViperTransformer {

  def noNI(m: Method): Boolean = m match {
    case m if m.name.endsWith("termination_proof") => true
    case _ => false
  }

  def noDup(@unused n: String): Boolean = false

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    Right(BackendVerifier.Task(program(task.program, noNI, noDup), task.backtrack))
  }
}
