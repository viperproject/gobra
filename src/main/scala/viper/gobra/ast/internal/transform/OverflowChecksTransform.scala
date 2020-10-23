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
    case f@Function(name, args, results, pres, posts, body) =>
      Function(name, args, results, pres, posts, body map (computeNewBody(args, _)))(f.info)

    case m@Method(receiver, name, args, results, pres, posts, body) =>
      Method(receiver, name, args, results, pres, posts, body map (computeNewBody(args, _)))(m.info)

    // TODO: add overflow checks to pure functions and methods

    case x => x
  }

  /**
    * Adds overflow checks to the bodies of a block. If block is parametrized,
    * `computeNewBody` adds assumptions about the parameters' bounds
    */
  private def computeNewBody(args: Vector[Parameter.In], body: Block): Block = {
    val assumeArgBounds = args map { case p@Parameter.In(_, typ) => exprWithinBounds(assume)(p, typ)(p.info) }
    val blockStmts = body.stmts map stmtTransform
    Block(body.decls, assumeArgBounds ++ blockStmts)(body.info)
  }

  private def stmtTransform(stmt: Stmt): Stmt = stmt match {
    case b@Block(decls, stmts) => Block(decls, stmts map stmtTransform)(b.info)

    case s@Seqn(stmts) => Seqn(stmts map stmtTransform)(s.info)

    // TODO: check condition expression for overflow
    case i@If(cond, thn, els) => If(cond, stmtTransform(thn), stmtTransform(els))(i.info)

    // TODO: check condition expression for overflow
    case w@While(cond, invs, body) => While(cond, invs, stmtTransform(body))(w.info)

    case ass@SingleAss(l, r) =>

      val assertBounds = exprWithinBounds(assert)(r, l.op.typ){
        val info = r.info
        if (!info.isInstanceOf[Single]) violation(s"r.info ($info) is expected to be a Single")
        info.asInstanceOf[Single].annotateOrigin(OverflowCheckAnnotation)
      }

      Seqn(Vector(assertBounds, ass)) {
        val info = l.op.info
        if (!info.isInstanceOf[Single]) violation(s"l.op.info ($info) is expected to be a Single")
        info.asInstanceOf[Single].annotateOrigin(OverflowCheckAnnotation)
      }

    case x => x
  }

  private val assert: Assertion => Source.Parser.Info => Stmt = a => Assert(a)(_)
  private val assume: Assertion => Source.Parser.Info => Stmt = a => Assume(a)(_)

  private def exprWithinBounds(assertionType: Assertion => Source.Parser.Info => Stmt)(expr: Expr, typ: Type)(info: Source.Parser.Info): Stmt =
    // TODO: check that every subexpression is within bounds, not only the top level one
    typ match {
      case IntT(_, kind) if kind.isInstanceOf[BoundedIntegerKind] =>
        val boundedKind = kind.asInstanceOf[BoundedIntegerKind]
        // TODO: look for more elegant way to do this (`info` very repeated)
        assertionType(
          ExprAssertion(
            And(
              AtMostCmp(IntLit(boundedKind.lower)(info), expr)(info),
              AtMostCmp(expr, IntLit(boundedKind.upper)(info))(info))(info))(info))(info)

      case _ => assertionType(ExprAssertion(BoolLit(true)(info))(info))(info)
    }

  // should this be moved to Source class?
  case object OverflowCheckAnnotation extends Source.Annotation
}
