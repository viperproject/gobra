// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.library.construct

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.BackTranslator._
import viper.gobra.reporting.{AssignConstructError, ConstructorError, DefaultErrorBackTranslator, DerefConstructError, PermissionAssignConstructError, PermissionDerefConstructError}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.silver.ast.utility.Expressions
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}

class ConstructImpl extends Construct {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize (addMemberFn: vpr.Member => Unit): Unit = {
    dereference.foreach(addMemberFn)
    assignments.foreach(addMemberFn)
  } 

  private var dereference: List[vpr.Function] = List.empty
  private var assignments: List[vpr.Method] = List.empty

  override def dereference(ctor: in.Constructor)(ctx: Context): Writer[Option[vpr.Function]] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.name

    val vArgs = ctor.args.map(ctx.variable)

    val ret = in.LocalVar(ctor.args(0).id, ctor.typ)(src)

    val zDeref = in.Deref(ret, underlyingType(ret.typ)(ctx))(src)

    val exp = zDeref match {
      case (loc: in.Location) :: ctx.Struct(fs) =>
        for {
          x <- cl.bind(loc)(ctx)
          locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
          args <- cl.sequence(locFAs.map(fa => ctx.expression(fa)))
        } yield ctx.tuple.create(args)(pos, info, errT)

      case _ => cl.pure(ctx.expression(zDeref))(ctx)
    }

    val body = ctor.body.map{ b => pure(b match {
      case in.Block(_, stmts) => stmts.collect {
        case in.Fold(a) => for {
          acc <- ctx.assertion(a)
          e = vpr.LocalVar(ctx.freshNames.next(), ctx.typ(ret.typ))(pos, info, errT)
        } yield vpr.Unfolding(acc.asInstanceOf[vpr.PredicateAccessPredicate], e)(pos, info, errT)
      }.reverse.foldRight(exp)((u, v) => for {
        exp <- v
        unf <- u
      } yield vpr.Unfolding(unf.acc, exp)(pos, info, errT))
      case _ => ???
    })(ctx)}

    (for { 
      pres <- for { 
        s <- sequence(ctor.posts.map(ctx.precondition))
        e = s.filterNot(a => Expressions.freeVariables(a).exists(n => n.name == ctor.args(1).id))
      } yield e
      
      body <- option(body)

      _ <- errorT(generatedDerefError(src))
      _ <- errorT(permissionDerefError(true))

      function = vpr.Function(
        name = s"${name}_${Names.generatedConstruct}_${Names.derefConstruct}", 
        formalArgs = Seq(vArgs(0)),
        typ = vArgs(1).typ,
        pres = pres,
        posts = Vector.empty,
        body = body
      )(pos, info, errT)
    } yield function).map{
      case p => dereference ::= p; Some(p)
    }
  }

  override def assignments(ctor: in.Constructor)(ctx: Context): Writer[Option[vpr.Method]] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.name

    val vArgs = ctor.args.map(ctx.variable)

    val ret = in.LocalVar(ctx.freshNames.next(), ctor.typ)(src)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val thi = in.LocalVar(ctor.args(0).id, ctor.typ)(src)

    val zDeref = in.Deref(thi, underlyingType(thi.typ)(ctx))(src)
    val exprTyp = ctor.typ match {
      case in.PointerT(t, _) => t.withAddressability(Addressability.Exclusive)
      case _ => ???
    }
    val exp = in.LocalVar(ctor.args(1).id, exprTyp)(src)

    val eq1 = vpr.LocalVar(exp.id, ctx.typ(exp.typ))(pos, info, errT)
    val eq2 = vpr.LocalVar(thi.id, ctx.typ(thi.typ))(pos, info, errT)
    val eqCmp = cl.bind(vpr.EqCmp(eq1, vpr.FuncApp(s"${name}_${Names.generatedConstruct}_${Names.derefConstruct}", Seq(eq2))(pos, info, eq1.typ, errT))(pos, info, errT))(ctx)

    val vSpec = for { 
      s <- sequence(ctor.posts.map(ctx.precondition))
      e = s.filterNot(a => Expressions.freeVariables(a).exists(n => n.name == ctor.args(1).id))
    } yield e

    val body1 = ctor.body.map{ b => block{ b match {
      case in.Block(_, stmts) => cl.seqns(stmts.collect {
        case in.Fold(a) => for {
          acc <- ctx.assertion(a)
        } yield vpr.Unfold(acc.asInstanceOf[vpr.PredicateAccessPredicate])(pos, info, errT)
      }.reverse)
      case _ => ???
    }}}

    val assign = (zDeref, exp) match {
      case (lhs :: ctx.Struct(lhsFs), rhs :: ctx.Struct(rhsFs)) => 
        block(for {
          x <- cl.bind(lhs)(ctx)
          newX = in.LocalVar(x.id, ret.typ)(ret.info)
          eqDflt <- ctx.equal(newX, in.DfltVal(ret.typ)(ret.info))(ctor)
          _ <- cl.write(vpr.Inhale(eqDflt)(pos, info, errT))

          y <- cl.bind(rhs)(ctx)
          lhsFAs = lhsFs.map(f => in.FieldRef(x, f)(x.info)).map(in.Assignee.Field)
          rhsFAs = rhsFs.map(f => in.FieldRef(y, f)(y.info))
          res <- cl.seqns((lhsFAs zip rhsFAs).map { case (lhsFA, rhsFA) => ctx.assignment(lhsFA, rhsFA)(ctor) })
          _ <- cl.write(res)
          
          e = vpr.LocalVar(x.id, ctx.typ(x.typ))(pos, info, errT)
        } yield vpr.LocalVarAssign(vRetD.localVar, e)(pos, info, errT))

      case _ => ???
    }

    val body2 = ctor.body.map{ b => block{ b match {
      case in.Block(_, stmts) => cl.seqns(stmts.collect {
        case f@in.Fold(_) => ctx.statement(f)
      })
      case _ => ???
    }}}

    (for { 
      pres <- vSpec
      posts <- vSpec
      eq <- pure(eqCmp)(ctx)

      body1 <- option(body1)
      assign <- assign
      body2 <- option(body2)
      body = for {
        b1 <- body1
        b2 <- body2
      } yield vpr.Seqn(b1.ss ++ assign.ss ++ b2.ss, assign.scopedDecls)(pos, info, errT)

      _ <- errorT(generatedAssignError(src, body.getOrElse(vpr.Seqn(Seq.empty, Seq.empty)(pos, info, errT))))
      _ <- errorT(permissionAssignError(true))

      method = vpr.Method(
        name = s"${name}_${Names.generatedConstruct}_${Names.assignConstruct}",
        formalArgs = vArgs,
        formalReturns = Seq(vRetD),
        pres = pres,
        posts = posts ++ Vector(eq),
        body = body
      )(pos, info, errT)
    } yield method).map{
      case p => assignments ::= p; Some(p)
    }
  }

  private def generatedDerefError(src: Source.Parser.Info): ErrorTransformer = {
    case err.FunctionNotWellformed(Source(info), reason, _) => 
      DerefConstructError(info, src, true).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  private def generatedAssignError(src: Source.Parser.Info, res: vpr.Stmt): ErrorTransformer = {
    case e@err.AssignmentFailed(Source(info), reason, _) if e causedBy res => 
      AssignConstructError(info, src, true).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    case e@err.PostconditionViolated(Source(info), _, reason, _) if e causedBy res =>
      AssignConstructError(info, src, true).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  override def permissionDerefError(generated: Boolean): ErrorTransformer = {
    case err.PreconditionInAppFalse(Source(info), reason, _) if info.node.isInstanceOf[in.Deref] =>
      PermissionDerefConstructError(info, generated).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  override def permissionAssignError(generated: Boolean): ErrorTransformer = {
    case e@err.PreconditionInCallFalse(Source(info), reason, _) if info.node.isInstanceOf[in.SingleAss] =>
      PermissionAssignConstructError(info, generated).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  override def derefWellFormedError(src: Source.Parser.Info, res: vpr.Exp): ErrorTransformer = {
    case e@err.FunctionNotWellformed(Source(info), reason, _) if e causedBy res => 
      DerefConstructError(info, src, false).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  override def assignWellFormedError(src: Source.Parser.Info, res: vpr.Stmt): ErrorTransformer = {
    case e@err.FunctionNotWellformed(Source(info), reason, _) if e causedBy res => 
      AssignConstructError(info, src, false).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    case e@err.AssignmentFailed(Source(info), reason, _) if e causedBy res => 
      AssignConstructError(info, src, false).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }

  override def constructWellFormedError(res: vpr.Stmt): ErrorTransformer = {
    case e@err.FunctionNotWellformed(Source(info), reason, _) if e causedBy res => 
      ConstructorError(info).dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
  }
}
