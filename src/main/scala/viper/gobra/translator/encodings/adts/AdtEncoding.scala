// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.adts

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.{MatchError, Source}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.util.Violation.violation
import viper.silver.{ast => vpr}

class AdtEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}
  import viper.silver.verifier.{errors => err}
  import viper.gobra.translator.util.{ViperUtil => vu}

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Adt(adt) / m =>
      m match {
        case Exclusive => adtType(adt.name)
        case Shared => vpr.Ref
      }
    case ctx.AdtClause(t) / m =>
      m match {
        case Exclusive => adtType(t.adtT.name)
        case Shared => vpr.Ref
      }
  }

  private def adtType(adtName: String): vpr.DomainType = vpr.DomainType(adtName, Map.empty)(Seq.empty)

  /**
    * [type X adt{ clause1{F11, ...}; ...; clauseN{FN1, ...} }] ->
    *
    * domain X {
    *
    *   // constructors
    *   X_clause1(Type(F11), ...): X
    *   ...
    *
    *   // destructors
    *   X_F11(X): Type(F11)
    *   ...
    *
    *   // default
    *   X_default(): X
    *
    *   // tags
    *   X_tag(X): Int
    *   unique X_clause1_tag(): Int
    *   ...
    *
    *   axiom {
    *     forall f11: F11, ... :: { X_clause1(f11, ...) }
    *       X_tag(X_clause1(f11, ...)) == X_clause1_tag() && X_F11(X_clause1(f11, ...)) )) == f11 && ...
    *     ...
    *   }
    *
    *   axiom {
    *     forall t: X :: {X_clause1_f11(t)}...{X_clause1_f1N(t)}
    *       X_tag(t) == X_clause1_tag() ==> t == X_clause1(X_clause1_f11(t), ...)
    *     ...
    *   }
    *
    *   axiom {
    *     forall t: X :: {X_tag(t)} t == X_clause1(X_clause1_f11(t), ...) || t == ...
    *   }
    *
    * }
    */
  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case adt: in.AdtDefinition =>
      val (aPos, aInfo, aErrT) = adt.vprMeta
      val adtName = adt.name // X
      val adtT = adtType(adtName)

      def adtDecl(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.LocalVarDecl =
        vpr.LocalVarDecl("t", adtT)(pos, info, errT)

      def fieldDecls(clause: in.AdtClause): Vector[vpr.LocalVarDecl] = {
        val (cPos, cInfo, cErrT) = clause.vprMeta
        clause.args map { field => vpr.LocalVarDecl(field.name, ctx.typ(field.typ))(cPos, cInfo, cErrT) }
      }

      // constructors
      val constructors = adt.clauses map { clause =>
        val (cPos, cInfo, cErrT) = clause.vprMeta
        vpr.DomainFunc(
          Names.constructorAdtName(adtName, clause.name.name),
          fieldDecls(clause),
          adtT,
        )(cPos, cInfo, adtName, cErrT)
      }

      // destructors
      val destructors = adt.clauses flatMap { clause =>
        clause.args.map { field =>
          val (fieldPos, fieldInfo, fieldErrT) = field.vprMeta
          vpr.DomainFunc(
            Names.destructorAdtName(adtName, field.name),
            Seq(adtDecl(fieldPos, fieldInfo, fieldErrT)),
            ctx.typ(field.typ)
          )(fieldPos, fieldInfo, adtName, fieldErrT)
        }
      }

      // default
      val defaultFunc = vpr.DomainFunc(
        Names.dfltAdtValue(adtName),
        Seq.empty,
        adtT,
      )(aPos, aInfo, adtName, aErrT)

      // tag
      val tagFunc = vpr.DomainFunc(
        Names.tagAdtFunction(adtName),
        Seq(adtDecl(aPos, aInfo, aErrT)),
        vpr.Int
      )(aPos, aInfo, adtName, aErrT)

      val clauseTags = adt.clauses map { clause =>
        val (cPos, cInfo, cErrT) = clause.vprMeta
        vpr.DomainFunc(
          Names.adtClauseTagFunction(adtName, clause.name.name),
          Seq.empty,
          vpr.Int,
          unique = true,
        )(cPos, cInfo, adtName, cErrT)
      }


      // axioms

      // forall fi1: Fi1, ... :: { X_clausei(fi1, ...) }
      //   X_tag(X_clausei(fi1, ...)) == X_clausei_tag() && X_Fi1(X_clausei(fi1, ...)) )) == fi1 && ...
      val constructorAxioms: Vector[vpr.AnonymousDomainAxiom] = adt.clauses.map(clause => {
        val (cPos, cInfo, cErrT) = clause.vprMeta
        val clauseFieldDecls = fieldDecls(clause)
        val args = clauseFieldDecls.map(_.localVar)
        val clauseName = clause.name.name

        val construct = constructor(clauseName, adtName, args)(cPos, cInfo, cErrT)
        val trigger = vpr.Trigger(Seq(construct))(cPos, cInfo, cErrT)
        val discriminatorOverConstructor = discriminator(clauseName, adtName, construct)(cPos, cInfo, cErrT)
        val destructorsOverConstructor = clause.args.map( field =>
          destructor(field.name, adtName, construct, ctx.typ(field.typ))(cPos, cInfo, cErrT)
        )

        if (clause.args.nonEmpty) {
          val destructorEqArg = (destructorsOverConstructor zip args) map {
            case (destructApp, arg) => vpr.EqCmp(destructApp, arg)(cPos, cInfo, cErrT)
          }

          vpr.AnonymousDomainAxiom(
            vpr.Forall(
              clauseFieldDecls,
              Seq(trigger),
              vu.bigAnd(discriminatorOverConstructor +: destructorEqArg)(cPos, cInfo, cErrT)
            )(cPos, cInfo, cErrT)
          )(cPos, cInfo, adtName, cErrT)
        } else {
          vpr.AnonymousDomainAxiom(discriminatorOverConstructor)(cPos, cInfo, adtName, cErrT)
        }
      })

      // forall t: X :: {X_clausei_fi1(t)}...{X_clausei_fiN(t)}
      //    X_tag(t) == X_clausei_tag() ==> t == X_clausei(X_clausei_fi1(t), ...)
      val destructorAxioms: Vector[vpr.AnonymousDomainAxiom] = adt.clauses.filter(c => c.args.nonEmpty).map(clause => {
        val (cPos, cInfo, cErrT) = clause.vprMeta
        val clauseName = clause.name.name
        val variableDecl = adtDecl(cPos, cInfo, cErrT)
        val variable = variableDecl.localVar
        val destructorsOverVar = clause.args.map(field =>
          destructor(field.name, adtName, variable, ctx.typ(field.typ))(cPos, cInfo, cErrT)
        )

        vpr.AnonymousDomainAxiom(
          vpr.Forall(
            Seq(variableDecl),
            destructorsOverVar.map(d => vpr.Trigger(Seq(d))(cPos, cInfo, cErrT)),
            vpr.Implies(
              discriminator(clauseName, adtName, variable)(cPos, cInfo, cErrT),
              vpr.EqCmp(
                variable,
                constructor(clauseName, adtName, destructorsOverVar)(cPos, cInfo, cErrT)
              )(cPos, cInfo, cErrT),
            )(cPos, cInfo, cErrT),
          )(cPos, cInfo, cErrT)
        )(cPos, cInfo, adtName, cErrT)
      })

      // forall t: X :: {X_tag(t)} t == X_clause1(X_clause1_f11(t), ...) || t == ...
      val exclusiveAxiom = {
        val variableDecl = adtDecl(aPos, aInfo, aErrT)
        val variable = variableDecl.localVar

        val triggerExpression = tag(adtName, variable)(aPos, aInfo, aErrT)
        val trigger = vpr.Trigger(Seq(triggerExpression))(aPos, aInfo, aErrT)

        val equalities = adt.clauses.map{clause =>
          val (cPos, cInfo, cErrT) = clause.vprMeta
          val clauseName = clause.name.name
          vpr.EqCmp(
            variable,
            constructor(
              clauseName,
              adtName,
              clause.args map { field =>
                val (argPos, argInfo, argErrT) = field.vprMeta
                destructor(field.name, adtName, variable, ctx.typ(field.typ))(argPos, argInfo, argErrT)
              }
            )(cPos, cInfo, cErrT)
          )(cPos, cInfo, cErrT)
        }

        vpr.AnonymousDomainAxiom(
          vpr.Forall(Seq(variableDecl), Seq(trigger), vu.bigOr(equalities)(aPos, aInfo, aErrT))(aPos, aInfo, aErrT)
        )(aPos, aInfo, adtName, aErrT)
      }
      ml.unit(Vector(vpr.Domain(
        adtName,
        functions = (defaultFunc +: tagFunc +: clauseTags) ++ constructors ++ destructors,
        axioms = (exclusiveAxiom +: constructorAxioms) ++ destructorAxioms
      )(pos = aPos, info = aInfo, errT = aErrT)))
  }

  /**
    * [ dflt(adt{N}) ] -> N_default()
    * [ C{args}: adt{N} ] -> N_C([args])
    * [ (e: adt{N}).isC ] -> N_tag([e]) == N_C_tag()
    * [ (e: adt{N}).f ] -> N_f([e])
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    default(super.expression(ctx)) {
      case (e: in.DfltVal) :: ctx.Adt(a) / Exclusive => unit(withSrc(defaultVal(a.name), e))
      case (e: in.DfltVal) :: ctx.AdtClause(a) / Exclusive => unit(withSrc(defaultVal(a.adtT.name), e))

      case ac: in.AdtConstructorLit =>
        for {
          args <- sequence(ac.args map ctx.expression)
        } yield withSrc(constructor(ac.clause.name, ac.clause.adtName, args), ac)

      case ad: in.AdtDiscriminator =>
        for {
          value <- ctx.expression(ad.base)
        } yield withSrc(discriminator(ad.clause.name, ad.clause.adtName, value), ad)

      case ad: in.AdtDestructor =>
        val adtType = underlyingAdtType(ad.base.typ)(ctx)
        for {
          value <- ctx.expression(ad.base)
        } yield withSrc(destructor(ad.field.name, adtType.name, value, ctx.typ(ad.field.typ)), ad)

      case p: in.PatternMatchExp => translatePatternMatchExp(ctx)(p)
      // case c@in.Contains(_, _ :: ctx.Adt(_)) => translateContains(c)(ctx)
    }
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)) {
      case p: in.PatternMatchStmt => translatePatternMatch(ctx)(p)
    }
  }

  // pattern matching

  def declareIn(ctx: Context)(e: in.Expr, p: in.MatchPattern, z: vpr.Exp): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = p.vprMeta

    p match {
      case in.MatchValue(_) | in.MatchWildcard() => unit(z)
      case in.MatchBindVar(name, typ) =>
        for {
          eV <- ctx.expression(e)
        } yield vpr.Let(
          vpr.LocalVarDecl(name, ctx.typ(typ))(pos, info, errT),
          eV,
          z
        )(pos, info, errT)
      case in.MatchAdt(clause, expr) =>
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(e, f)(e.info))
        val zipWithPattern = inDeconstructors.zip(expr)
        zipWithPattern.foldRight(unit(z))((des, acc) => for {
          v <- acc
          d <- declareIn(ctx)(des._1, des._2, v)
        } yield d)
    }
  }

  def translatePatternMatchExp(ctx: Context)(e: in.PatternMatchExp): CodeWriter[vpr.Exp] = {

    def translateCases(cases: Vector[in.PatternMatchCaseExp], dflt: in.Expr): CodeWriter[vpr.Exp] = {

      val (ePos, eInfo, eErrT) = if (cases.isEmpty) dflt.vprMeta else cases.head.vprMeta

      if (cases.isEmpty) {
        ctx.expression(dflt)
      } else {
        val c = cases.head
        for {
          check <- translateMatchPatternCheck(ctx)(e.exp, c.mExp)
          body <- ctx.expression(c.exp)
          decl <- declareIn(ctx)(e.exp, c.mExp, body)
          el <- translateCases(cases.tail, dflt)
        } yield vpr.CondExp(check, decl, el)(ePos, eInfo, eErrT)
      }
    }

    if (e.default.isDefined) {
      translateCases(e.cases, e.default.get)
    } else {

      val (pos, info, errT) = e.vprMeta

      val allChecks = e.cases
        .map(c => translateMatchPatternCheck(ctx)(e.exp, c.mExp))
        .foldLeft(unit(vpr.FalseLit()() : vpr.Exp))((acc, next) =>
          for {
            a <- acc
            n <- next
          } yield vpr.Or(a,n)(pos, info, errT))


      for {
        dummy <- ctx.expression(in.DfltVal(e.typ)(e.info))
        cond <- translateCases(e.cases, in.DfltVal(e.typ)(e.info))
        checks <- allChecks
        (checkFunc, errCheck) = ctx.condition.assert(checks, (info, _) => MatchError(info))
        _ <- errorT(errCheck)
      } yield vpr.CondExp(checkFunc, cond, dummy)(pos, info, errT)
    }


  }

  def translateMatchPatternCheck(ctx: Context)(expr: in.Expr, pattern: in.MatchPattern): CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)
    val (pos, info, errT) = pattern.vprMeta

    def matchSimpleExp(exp: in.Expr): Writer[vpr.Exp] = for {
      e1 <- goE(exp)
      e2 <- goE(expr)
    } yield vpr.EqCmp(e1, e2)(pos, info, errT)

    val (mPos, mInfo, mErr) = pattern.vprMeta

    pattern match {
      case in.MatchValue(exp) => matchSimpleExp(exp)
      case in.MatchBindVar(_, _) | in.MatchWildcard() => unit(vpr.TrueLit()(pos,info,errT))
      case in.MatchAdt(clause, exp) =>
        val tagFunction = Names.tagAdtFunction(clause.adtT.name)
        val tag = vpr.IntLit(???)(mPos, mInfo, mErr)
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(expr, f)(pattern.info))
        for {
          e1 <- goE(expr)
          tF = vpr.DomainFuncApp(tagFunction, Vector(e1), Map.empty)(mPos, mInfo, vpr.Int, clause.adtT.name, mErr)
          checkTag = vpr.EqCmp(tF, tag)(mPos)
          rec <- sequence(inDeconstructors.zip(exp) map {case (e, p) => translateMatchPatternCheck(ctx)(e,p)})
        } yield rec.foldLeft(checkTag:vpr.Exp)({case (acc, next) => vpr.And(acc, next)(mPos, mInfo, mErr)})
    }
  }

  def translateMatchPatternDeclarations(ctx: Context)(expr: in.Expr, pattern: in.MatchPattern): Option[CodeWriter[vpr.Seqn]] = {
    val (mPos, mInfo, mErrT) = pattern.vprMeta
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)
    pattern match {
      case in.MatchBindVar(name, typ) =>
        val t = ctx.typ(typ)

        val writer = for {
          e <- goE(expr)
          v = vpr.LocalVarDecl(name, t)(mPos, mInfo, mErrT)
          a = vpr.LocalVarAssign(vpr.LocalVar(name, t)(mPos, mInfo, mErrT), e)(mPos, mInfo, mErrT)
        } yield vpr.Seqn(Seq(a), Seq(v))(mPos, mInfo, mErrT)

        Some(writer)

      case in.MatchAdt(clause, exprs) =>
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(expr, f)(pattern.info))
        val recAss: Vector[CodeWriter[vpr.Seqn]] =
          inDeconstructors.zip(exprs) map {case (e, p) => translateMatchPatternDeclarations(ctx)(e,p)} collect {case Some(s) => s}
        val assignments: Vector[vpr.Seqn] = recAss map {a => a.res}
        val reduced = assignments.foldLeft(vpr.Seqn(Seq(), Seq())(mPos, mInfo, mErrT))(
          {case (l, r) => vpr.Seqn(l.ss ++ r .ss, l.scopedDecls ++ r.scopedDecls)(mPos, mInfo, mErrT)}
        )
        Some(unit(reduced))

      case _ : in.MatchValue | _: in.MatchWildcard => None
    }
  }

  def translatePatternMatch(ctx: Context)(s: in.PatternMatchStmt): CodeWriter[vpr.Stmt] = {
    val expr = s.exp
    val cases = s.cases

    val (sPos, sInfo, sErrT) = s.vprMeta

    val checkExVarDecl = vpr.LocalVarDecl(ctx.freshNames.next(), vpr.Bool)(sPos, sInfo, sErrT)
    val checkExVar = checkExVarDecl.localVar
    val initialExVar = unit(vpr.LocalVarAssign(checkExVar, vpr.FalseLit()(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))
    def exErr(ass: vpr.Stmt): ErrorTransformer = {
      case e@err.AssertFailed(Source(info), _, _) if e causedBy ass => MatchError(info)
    }

    val assertWithError = for {
      a <- unit(vpr.Assert(vpr.EqCmp(checkExVar, vpr.TrueLit()(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))
      _ <- errorT(exErr(a))
    } yield a

    def setExVar(p: vpr.Position, i: vpr.Info, e: vpr.ErrorTrafo) =
      unit(vpr.LocalVarAssign(checkExVar, vpr.TrueLit()(p,i,e))(p,i,e))

    def translateCase(c: in.PatternMatchCaseStmt): CodeWriter[vpr.Stmt] = {
      val (cPos, cInfo, cErrT) = c.vprMeta

      val assignments = translateMatchPatternDeclarations(ctx)(expr, c.mExp)

      val (ass: Seq[vpr.Stmt], decls: Seq[vpr.Declaration]) =
        if (assignments.isDefined) {
          val w = assignments.get.res
          (w.ss, w.scopedDecls)
        } else {
          (Seq(), Seq())
        }
      for {
        check <- translateMatchPatternCheck(ctx)(expr, c.mExp)
        exVar <- setExVar(cPos, cInfo, cErrT)
        body  <- seqn(ctx.statement(c.body))
      } yield vpr.If(vpr.And(check, vpr.Not(checkExVar)(cPos, cInfo, cErrT))(cPos, cInfo, cErrT),
        vpr.Seqn(exVar +: (ass :+ body), decls)(cPos, cInfo, cErrT), vpr.Seqn(Seq(), Seq())(cPos, cInfo, cErrT))(cPos, cInfo, cErrT)
    }

    if (s.strict) {
      for {
        init <- initialExVar
        cs <- sequence(cases map translateCase)
        a <- assertWithError
      } yield vpr.Seqn(init +: cs :+ a, Seq(checkExVarDecl))(sPos, sInfo, sErrT)
    } else {
      for {
        init <- initialExVar
        cs <- sequence(cases map translateCase)
      } yield vpr.Seqn(init +: cs, Seq(checkExVarDecl))(sPos, sInfo, sErrT)
    }
  }


  // constructor, destructor, discriminator, tag, default

  /** adtName_clauseName(args) */
  private def constructor(clauseName: String, adtName: String, args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(
      funcname = Names.constructorAdtName(adtName, clauseName),
      args,
      Map.empty
    )(pos, info, adtType(adtName), adtName, errT)
  }

  /** adtName_fieldName(arg) */
  private def destructor(fieldName: String, adtName: String, arg: vpr.Exp, fieldType: vpr.Type)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(
      funcname = Names.destructorAdtName(adtName, fieldName),
      Seq(arg),
      Map.empty,
    )(pos, info, fieldType, adtName, errT)
  }

  /** adtName_tag(arg) == adtName_clauseName_tag() */
  private def discriminator(clauseName: String, adtName: String, arg: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.EqCmp(
      tag(adtName, arg)(pos, info, errT),
      clauseTag(clauseName, adtName)(pos, info, errT),
    )(pos, info, errT)
  }

  /** adtName_tag(arg) */
  private def tag(adtName: String, arg: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(
      funcname = Names.tagAdtFunction(adtName),
      Seq(arg),
      Map.empty,
    )(pos, info, vpr.Int, adtName, errT)
  }

  /** adtName_clauseName_tag() */
  private def clauseTag(clauseName: String, adtName: String)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(
      funcname = Names.adtClauseTagFunction(adtName, clauseName),
      Seq.empty,
      Map.empty
    )(pos, info, vpr.Int, adtName, errT)
  }

  /** adtName_default() */
  def defaultVal(adtName: String)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp = {
    vpr.DomainFuncApp(
      funcname = Names.dfltAdtValue(adtName),
      Seq.empty,
      Map.empty
    )(pos, info, adtType(adtName), adtName, errT)
  }

  // auxiliary functions

  private def underlyingAdtType(t: in.Type)(ctx: Context): in.AdtT = {
    underlyingType(t)(ctx) match {
      case t: in.AdtT => t
      case t: in.AdtClauseT => t.adtT
      case t => violation(s"expected adt type, but got $t")
    }
  }
}
