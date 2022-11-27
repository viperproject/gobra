// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.adts

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.MatchError
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

      case p: in.PatternMatchExp => translatePatternMatchExp(p)(ctx)
    }
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)) {
      case p: in.PatternMatchStmt => translatePatternMatch(p)(ctx)
    }
  }

  // pattern matching

  /**
    * [e match { case p1: s1; ... }] ->
    *   var b: Bool = false
    *
    *   if Check(p1,e && !b) {
    *     b := true
    *     Assign(p1,e)
    *     [s1]
    *   }
    *
    *   ...
    *
    *   // if strict is true
    *   assert b
    *
    */
  def translatePatternMatch(s: in.PatternMatchStmt)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (sPos, sInfo, sErrT) = s.vprMeta

    // var b: Bool
    val checkExVarDecl = vpr.LocalVarDecl(ctx.freshNames.next(), vpr.Bool)(sPos, sInfo, sErrT)
    val checkExVar = checkExVarDecl.localVar

    // b := false
    val initialExVar = vpr.LocalVarAssign(checkExVar, vpr.FalseLit()(sPos, sInfo, sErrT))(sPos, sInfo, sErrT)

    // b := true
    def setExVar(p: vpr.Position, i: vpr.Info, e: vpr.ErrorTrafo): Writer[vpr.LocalVarAssign] =
      unit(vpr.LocalVarAssign(checkExVar, vpr.TrueLit()(p, i, e))(p, i, e))

    def translateCase(c: in.PatternMatchCaseStmt): CodeWriter[vpr.Stmt] = {
      val (cPos, cInfo, cErrT) = c.vprMeta
      for {
        check <- translateMatchPatternCheck(s.exp, c.mExp)(ctx)
        setExVarV <- setExVar(cPos, cInfo, cErrT)
        ass <- translateMatchPatternDeclarations(s.exp, c.mExp)(ctx)
        body <- seqn(ctx.statement(c.body))
      } yield vpr.If(
        vpr.And(check, vpr.Not(checkExVar)(cPos, cInfo, cErrT))(cPos, cInfo, cErrT), // Check(pi, e) && !b
        vpr.Seqn(Seq(setExVarV, ass, body), Seq.empty)(cPos, cInfo, cErrT),  // b := true; Assign(pi, e); [si]
        vpr.Seqn(Seq(), Seq())(cPos, cInfo, cErrT) // empty else
      )(cPos, cInfo, cErrT)
    }

    for {
      _ <- local(checkExVarDecl)
      _ <- write(initialExVar)
      cs <- sequence(s.cases map translateCase)
      _ <- write(cs: _*)
      _ <- if (s.strict) assert(checkExVar, (info, _) => MatchError(info)) else unit(())
    } yield vu.nop(sPos, sInfo, sErrT)
  }

  /**
    * [e match { case1 p1: e1; ...; caseN pN: eN }] ->
    *   asserting Check(p1, e) || ... || Check(pN, e) in Match(case1 p1: e1; ...; caseN pN: eN; default: dflt(T), e)
    * [e match { case1 p1: e1; ...; caseN pN: eN; default: e_ }] ->
    *   Match(case1 p1: e1; ...; caseN pN: eN; default: e_, e)
    *
    * Match(default: e_, e) -> e_
    * Match(case1 p1: e1; ...; caseN pN: eN; default: e_, e) ->
    *   Check(p1, e) ? AssignIn(p1, e, [e1]) : Match(case p2: e2; ...; caseN pN: eN; default: e_, e)
    *
    */
  def translatePatternMatchExp(e: in.PatternMatchExp)(ctx: Context): CodeWriter[vpr.Exp] = {

    def translateCases(cases: Vector[in.PatternMatchCaseExp], dflt: in.Expr): CodeWriter[vpr.Exp] = {
      val (ePos, eInfo, eErrT) = if (cases.isEmpty) dflt.vprMeta else cases.head.vprMeta
      cases match {
        case c +: cs =>
          for {
            check <- translateMatchPatternCheck(e.exp, c.mExp)(ctx)
            body <- ctx.expression(c.exp)
            decl <- declareIn(e.exp, c.mExp, body)(ctx)
            el <- translateCases(cs, dflt)
          } yield vpr.CondExp(check, decl, el)(ePos, eInfo, eErrT)
        case _ => ctx.expression(dflt)
      }
    }

    if (e.default.isDefined) {
      translateCases(e.cases, e.default.get)
    } else {
      val (pos, info, errT) = e.vprMeta

      val checkExpressionMatchesOnePattern =
        sequence(e.cases.map(c => translateMatchPatternCheck(e.exp, c.mExp)(ctx))).map(vu.bigOr(_)(pos, info, errT))

      for {
        matching <- translateCases(e.cases, in.DfltVal(e.typ)(e.info))
        checks <- checkExpressionMatchesOnePattern
        checkedMatching <- assert(checks, matching, (info, _) => MatchError(info))(ctx)
      } yield checkedMatching
    }
  }

  /**
    * Encodes the check whether `expr` matches pattern
    *
    * Check(_, e) -> true
    * Check(x, e) -> true
    * Check(`v`, e) -> [v] == [e]
    * Check(C{f1: p1, ...}, e) -> [e.isC] && Check(p1, e.f1) && ...
    *
    */
  def translateMatchPatternCheck(expr: in.Expr, pattern: in.MatchPattern)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = pattern.vprMeta

    pattern match {
      case _: in.MatchBindVar | _: in.MatchWildcard =>
        unit(vpr.TrueLit()(pos,info,errT))

      case in.MatchValue(exp) =>
        for {
          e1 <- ctx.expression(exp)
          e2 <- ctx.expression(expr)
        } yield vpr.EqCmp(e1, e2)(pos, info, errT)

      case in.MatchAdt(clause, patternArgs) =>
        val destructorOverExp = clause.fields.map(f => in.AdtDestructor(expr, f)(expr.info))
        for {
          eV <- ctx.expression(expr)
          discr = discriminator(clause.name, clause.adtT.name, eV)(pos, info, errT)
          rec <- sequence((destructorOverExp zip patternArgs) map { case (e, p) => translateMatchPatternCheck(e,p)(ctx)} )
        } yield vu.bigAnd(discr +: rec)(pos, info, errT)
    }
  }

  /**
    * Assign(_, e) -> nop
    * Assign(`v`, e) -> nop
    * Assign(x, e) -> var x; x = [e]
    * Assign(C{f1: p1, ...}, e) -> Assign(p1, e.f1); Assign(p2, e.f2); ...
    *
    */
  def translateMatchPatternDeclarations(expr: in.Expr, pattern: in.MatchPattern)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (mPos, mInfo, mErrT) = pattern.vprMeta

    pattern match {
      case _ : in.MatchValue | _: in.MatchWildcard => unit(vu.nop(mPos, mInfo, mErrT))

      case in.MatchBindVar(name, typ) =>
        val t = ctx.typ(typ)
        for {
          e <- ctx.expression(expr)
          v = vpr.LocalVarDecl(name, t)(mPos, mInfo, mErrT)
          _ <- local(v) // definition of locals is propagated to body of match case
          a = vpr.LocalVarAssign(vpr.LocalVar(name, t)(mPos, mInfo, mErrT), e)(mPos, mInfo, mErrT)
        } yield a


      case in.MatchAdt(clause, exprs) =>
        val destructorOverExp = clause.fields.map(f => in.AdtDestructor(expr, f)(pattern.info))
        val recAss = (destructorOverExp zip exprs) map { case (e, p) => translateMatchPatternDeclarations(e,p)(ctx) }
        sequence(recAss).map(vpr.Seqn(_, Seq.empty)(mPos, mInfo, mErrT))
    }
  }

  /**
    * AssignIn(_, e, z) -> z
    * AssignIn(`v`, e, z) -> z
    * AssignIn(x, e, z) -> let x == ([e]) in z
    * AssignIn(C{f1: p1, ...}, e, z) -> AssignIn(p1, e.f1, AssignIn(p2, e.f2, ...z))
    *
    */
  def declareIn(e: in.Expr, p: in.MatchPattern, z: vpr.Exp)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = p.vprMeta

    p match {
      case _: in.MatchValue | _: in.MatchWildcard => unit(z)
      case in.MatchBindVar(name, typ) =>
        for {
          eV <- ctx.expression(e)
        } yield vpr.Let(vpr.LocalVarDecl(name, ctx.typ(typ))(pos, info, errT), eV, z)(pos, info, errT)

      case in.MatchAdt(clause, expr) =>
        val destructorOverExp = clause.fields.map(f => in.AdtDestructor(e, f)(e.info))
        (destructorOverExp zip expr).foldRight(unit(z))((des, acc) => for {
          v <- acc
          d <- declareIn(des._1, des._2, v)(ctx)
        } yield d)
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
