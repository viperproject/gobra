// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.internal._
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.Parser.Single
import viper.gobra.util.TypeBounds.BoundedIntegerKind
import viper.gobra.util.Violation.violation

/**
  * Adds overflow checks to programs written in Gobra's internal language
  */
object OverflowChecksTransform extends InternalTransform {
  override def name(): String = "add_integer_overflow_checks"

  override def transform(p: Program): Program = transformMembers(memberTrans)(p)

  private def memberTrans(member: Member): Member = member match {
    // Adds overflow checks per statement that contains an expression and assume statements in the beginning
    // specifying the bounds of each argument
    case f@Function(name, args, results, pres, posts, body) =>
      Function(name, args, results, pres, posts, body map (computeNewBody(args, _)))(f.info)

    // same as functions
    case m@Method(receiver, name, args, results, pres, posts, body) =>
      Method(receiver, name, args, results, pres, posts, body map (computeNewBody(args, _)))(m.info)

    // Adds pre-conditions stating the bounds of each argument and a post-condition to check if the body expression
    // overflows
    case f@PureFunction(name, args, results, pres, posts, body) => body match {
      case Some(expr) =>
        val newPre = pres ++ getPureBlockPre(args)
        val newPost = posts ++ Vector(getPureBlockPost(expr, results))
        PureFunction(name, args, results, newPre, newPost, body)(f.info)
      case None => f
    }

    // Same as pure functions
    case m@PureMethod(receiver, name, args, results, pres, posts, body) => body match {
      case Some(expr) =>
        val newPre = pres ++ getPureBlockPre(args)
        val newPost = posts ++ Vector(getPureBlockPost(expr, results))
        PureMethod(receiver, name, args, results, newPre, newPost, body)(m.info)
      case None => m
    }

    // TODO: should we add overflow checks to predicates?

    case x => x
  }

  /**
    * Adds overflow checks to the bodies of a block. If block is parametrized,
    * `computeNewBody` adds assumptions about the parameters' bounds
    */
  private def computeNewBody(args: Vector[Parameter.In], body: Block): Block = {
    val assumeArgBounds = args map { case p@Parameter.In(_, typ) => Assume(assertionExprInBounds(p, typ)(p.info))(p.info) }
    val blockStmts = body.stmts map stmtTransform
    Block(body.decls, assumeArgBounds ++ blockStmts)(body.info)
  }

  /**
    * Computes the pre-conditions to be added to pure functions and methods to check for overflows, i.e.
    * that each argument is within the bounds for its type
    */
  private def getPureBlockPre(args: Vector[Parameter.In]): Vector[Assertion] = {
    args.map { p: Parameter.In => assertionExprInBounds(p, p.typ)(p.info) }
  }

  /**
    * Computes the post-conditions to be added to pure functions and methods to check for overflows, i.e.
    * that the expression result is within the bounds of its type
    */
  private def getPureBlockPost(body: Expr, results: Vector[Parameter.Out]): Assertion = {
    // relies on the current assumption that pure functions and methods must have exactly one result argument
    if (results.length != 1) violation("Pure functions and methods must have exactly one result argument")
    body.info match {
      case s: Single => assertionExprInBounds(body, results(0).typ)(s.annotateOrigin(OverflowCheckAnnotation))
      case i => violation(s"expr.info ($i) is expected to be a Single")
    }
  }

  private def stmtTransform(stmt: Stmt): Stmt = stmt match {
    case b@Block(decls, stmts) => Block(decls, stmts map stmtTransform)(b.info)

    case s@Seqn(stmts) => Seqn(stmts map stmtTransform)(s.info)

    // TODO: check condition expression for overflow
    case i@If(cond, thn, els) => If(cond, stmtTransform(thn), stmtTransform(els))(i.info)

    // TODO: check condition expression for overflow
    case w@While(cond, invs, body) => While(cond, invs, stmtTransform(body))(w.info)

    case ass@SingleAss(l, r) =>
      val assertBounds = {
        val info = r.info match {
          case s: Single => s.annotateOrigin(OverflowCheckAnnotation)
          case i => violation(s"r.info ($i) is expected to be a Single")
        }
        Assert(assertionExprInBounds(r, l.op.typ)(info))(info)
      }

      Seqn(Vector(assertBounds, ass)) {
        l.op.info match {
          case s: Single => s.annotateOrigin(OverflowCheckAnnotation)
          case i => violation(s"l.op.info ($i) is expected to be a Single")
        }
      }

    case x => x
  }

  private def assertionExprInBounds(expr: Expr, typ: Type)(info: Source.Parser.Info): Assertion =
    // TODO: check that every subexpression is within bounds, not only the top level one
    typ match {
      case IntT(_, kind) if kind.isInstanceOf[BoundedIntegerKind] =>
        val boundedKind = kind.asInstanceOf[BoundedIntegerKind]
        // TODO: look for more elegant way to do this (`info` very repeated)
          ExprAssertion(
            And(
              AtMostCmp(IntLit(boundedKind.lower)(info), expr)(info),
              AtMostCmp(expr, IntLit(boundedKind.upper)(info))(info))(info))(info)

      case _ => ExprAssertion(BoolLit(true)(info))(info)
    }

  // should this be moved to Source class?
  case object OverflowCheckAnnotation extends Source.Annotation
}