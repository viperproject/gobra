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
    // adds overflow checks per statement that contains subexpressions of bounded integer type and adds assume
    /// statements at the beginning of a function or method body assuming that the value of an argument (of
    // bounded integer type) respects the bounds.
    case f@Function(name, args, results, pres, posts, body) =>
      Function(name, args, results, pres, posts, body map (computeNewBody(args, _)))(f.info)

    // same as functions
    case m@Method(receiver, name, args, results, pres, posts, body) =>
      Method(receiver, name, args, results, pres, posts, body map (computeNewBody(args, _)))(m.info)

    // Adds pre-conditions stating the bounds of each argument and a post-condition to check if the body expression
    // overflows
    case f@PureFunction(name, args, results, pres, posts, body) => body match {
      case Some(expr) =>
        val newPre = pres ++ getPureBlockPres(args)
        val newPost = posts ++ getPureBlockPosts(expr, results)
        PureFunction(name, args, results, newPre, newPost, body)(f.info)
      case None => f
    }

    // Same as pure functions
    case m@PureMethod(receiver, name, args, results, pres, posts, body) => body match {
      case Some(expr) =>
        val newPre = pres ++ getPureBlockPres(args)
        val newPost = posts ++ getPureBlockPosts(expr, results)
        PureMethod(receiver, name, args, results, newPre, newPost, body)(m.info)
      case None => m
    }

    /* As discussed on the Gobra meeting (27/10/2020), overflow checks should not be added to predicates, assertions
    * and any other purely logical (i.e. non-executable code) statements and expressions. This seems to be the approach taken
    * by other verification tools such as FramaC, as noted by Wytse
    */

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
  private def getPureBlockPres(args: Vector[Parameter.In]): Vector[Assertion] = {
    args.map { p: Parameter.In => assertionExprInBounds(p, p.typ)(p.info) }
  }

  /**
    * Computes the post-conditions to be added to pure functions and methods to check for overflows, i.e.
    * that the expression result is within the bounds of its type
    */
  private def getPureBlockPosts(body: Expr, results: Vector[Parameter.Out]): Vector[Assertion] = {
    // relies on the current assumption that pure functions and methods must have exactly one result argument
    if (results.length != 1) violation("Pure functions and methods must have exactly one result argument")
    Vector(assertionExprInBounds(body, results(0).typ)(addAnnotation(body.info)))
  }

  private def stmtTransform(stmt: Stmt): Stmt = stmt match {
    case b@Block(decls, stmts) => Block(decls, stmts map stmtTransform)(b.info)

    case s@Seqn(stmts) => Seqn(stmts map stmtTransform)(s.info)

    case i@If(cond, thn, els) =>
      val condInfo = addAnnotation(cond.info)
      val ifInfo = addAnnotation(i.info)
      val assertCond = Assert(assertionExprInBounds(cond, cond.typ)(condInfo))(condInfo)
      val ifStmt = If(cond, stmtTransform(thn), stmtTransform(els))(ifInfo)
      Seqn(Vector(assertCond, ifStmt))(ifInfo)

    case w@While(cond, invs, body) =>
      val condInfo = addAnnotation(cond.info)
      val whileInfo = addAnnotation(w.info)
      val assertCond = Assert(assertionExprInBounds(cond, cond.typ)(condInfo))(condInfo)
      val whileStmt = While(cond, invs, stmtTransform(body))(whileInfo)
      Seqn(Vector(assertCond, whileStmt))(whileInfo)

    case ass@SingleAss(l, r) =>
      val assertBounds = {
        val info = addAnnotation(r.info)
        Assert(assertionExprInBounds(r, l.op.typ)(info))(info)
      }
      Seqn(Vector(assertBounds, ass))(addAnnotation(l.op.info))

    case x => x
  }

  // Checks if expr and its subexpressions are within bounds of type `typ`
  private def assertionExprInBounds(expr: Expr, typ: Type)(info: Source.Parser.Info): Assertion = {
    def genAssertion(expr: Expr, typ: Type): Expr =
      typ match {
        case IntT(_, kind) if kind.isInstanceOf[BoundedIntegerKind] =>
          val boundedKind = kind.asInstanceOf[BoundedIntegerKind]
          And(
            AtMostCmp(IntLit(boundedKind.lower)(info), expr)(info),
            AtMostCmp(expr, IntLit(boundedKind.upper)(info))(info))(info)

        case _ => BoolLit(true)(info)
      }

    val exprBoundCheck = genAssertion(expr, typ) // typ must be provided from the outside to obtain the most precise type info possible
    val subExprsBoundChecks =
      Expr.getProperSubExpressions(expr).filter(_.typ.isInstanceOf[IntT]).map{x => genAssertion(x, x.typ)}

    ExprAssertion(subExprsBoundChecks.foldRight(exprBoundCheck)((x,y) => And(x,y)(info)))(info)
  }

  // should this be moved to Source class?
  case object OverflowCheckAnnotation extends Source.Annotation

  private def addAnnotation(info: Source.Parser.Info): Source.Parser.Info =
    info match {
      case s: Single => s.annotateOrigin(OverflowCheckAnnotation)
      case i => violation(s"l.op.info ($i) is expected to be a Single")
    }
}