// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.internal._
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.OverflowCheckAnnotation
import viper.gobra.reporting.Source.Parser.{Internal, Single}
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
    case f@Function(name, args, results, pres, posts, terminationMeasure, annotations, body) =>
      Function(name, args, results, pres, posts, terminationMeasure, annotations, body map computeNewBody)(f.info)

    // same as functions
    case m@Method(receiver, name, args, results, pres, posts, terminationMeasure, annotations, body) =>
      Method(receiver, name, args, results, pres, posts, terminationMeasure, annotations, body map computeNewBody)(m.info)

    // Adds pre-conditions stating the bounds of each argument and a post-condition to check if the body expression
    // overflows
    case f@PureFunction(name, args, results, pres, posts, terminationMeasure, annotations, body, isOpaque) => body match {
      case Some(expr) =>
        val newPost = posts ++ getPureBlockPosts(expr, results)
        PureFunction(name, args, results, pres, newPost, terminationMeasure, annotations, body, isOpaque)(f.info)
      case None => f
    }

    // Same as pure functions
    case m@PureMethod(receiver, name, args, results, pres, posts, terminationMeasure, annotations, body, isOpaque) => body match {
      case Some(expr) =>
        val newPost = posts ++ getPureBlockPosts(expr, results)
        PureMethod(receiver, name, args, results, pres, newPost, terminationMeasure, annotations, body, isOpaque)(m.info)
      case None => m
    }

    /* As discussed on the Gobra meeting (27/10/2020), overflow checks should not be added to predicates, assertions
    * and any other purely logical (i.e. non-executable code) statements and expressions. This seems to be the approach taken
    * by other verification tools such as FramaC, as noted by Wytse
    */

    case x => x
  }

  /**
    * Adds overflow checks to the body of a method.
    */
  private def computeNewBody(body: MethodBody): MethodBody = {
    MethodBody(
      body.decls,
      MethodBodySeqn(body.seqn.stmts map stmtTransform)(body.seqn.info),
      body.postprocessing map stmtTransform,
    )(body.info)
  }

  /**
    * Computes the post-conditions to be added to pure functions and methods to check for overflows, i.e.
    * that the expression result is within the bounds of its type
    */
  private def getPureBlockPosts(body: Expr, results: Vector[Parameter.Out]): Vector[Assertion] = {
    // relies on the current assumption that pure functions and methods must have exactly one result argument
    if (results.length != 1) violation("Pure functions and methods must have exactly one result argument")
    Vector(assertionExprInBounds(body, results(0).typ)(createAnnotatedInfo(body.info)))
  }

  private def stmtTransform(stmt: Stmt): Stmt = stmt match {
    case b@Block(decls, stmts) => Block(decls, stmts map stmtTransform)(b.info)

    case s@Seqn(stmts) => Seqn(stmts map stmtTransform)(s.info)

    case i@If(cond, thn, els) =>
      val condInfo = createAnnotatedInfo(cond.info)
      val assertCond = Assert(assertionExprInBounds(cond, cond.typ)(condInfo))(condInfo)
      val ifStmt = If(cond, stmtTransform(thn), stmtTransform(els))(i.info)
      Seqn(Vector(assertCond, ifStmt))(i.info)

    case w@While(cond, invs, terminationMeasure, body) =>
      val condInfo = createAnnotatedInfo(cond.info)
      val assertCond = Assert(assertionExprInBounds(cond, cond.typ)(condInfo))(condInfo)
      val whileStmt = While(cond, invs, terminationMeasure,stmtTransform(body))(w.info)
      Seqn(Vector(assertCond, whileStmt))(w.info)

    case ass@SingleAss(l, r) =>
      val info = createAnnotatedInfo(r.info)
      val assertBounds = Assert(assertionExprInBounds(r, l.op.typ)(info))(info)
      Seqn(Vector(assertBounds, ass))(l.op.info)

    case f@FunctionCall(_, _, args) =>
      Seqn(genOverflowChecksExprs(args) :+ f)(f.info)

    case m@MethodCall(_, recv, _, args) =>
      Seqn(genOverflowChecksExprs(recv +: args) :+ m)(m.info)

    case m@New(_, expr) =>
      Seqn(genOverflowChecksExprs(Vector(expr)) :+ m)(m.info)

    case f@GoFunctionCall(_, args) =>
      Seqn(genOverflowChecksExprs(args) :+ f)(f.info)

    case m@GoMethodCall(recv, _, args) =>
      Seqn(genOverflowChecksExprs(recv +: args) :+ m)(m.info)

    case d@Defer(FunctionCall(_, _, args)) => Seqn(genOverflowChecksExprs(args) :+ d)(d.info)
    case d@Defer(MethodCall(_, recv, _, args)) => Seqn(genOverflowChecksExprs(recv +: args) :+ d)(d.info)
    case d@Defer(_: Fold | _: Unfold | _: PredExprFold | _: PredExprUnfold) => d

    case m@Send(_, expr, _, _, _) =>
      Seqn(genOverflowChecksExprs(Vector(expr)) :+ m)(m.info)

    case m@MakeSlice(_, _, arg1, optArg2) =>
      Seqn(genOverflowChecksExprs(arg1 +: optArg2.toVector) :+ m)(m.info)

    case m@MakeChannel(_, _, optArg, _, _) =>
      Seqn(genOverflowChecksExprs(optArg.toVector) :+ m)(m.info)

    case m@MakeMap(_, _, optArg) =>
      Seqn(genOverflowChecksExprs(optArg.toVector) :+ m)(m.info)

    case m@SafeMapLookup(_, _, IndexedExp(base, idx, _)) =>
      Seqn(genOverflowChecksExprs(Vector(base, idx)) :+ m)(m.info)

    // explicitly matches remaining statements to detect non-exhaustive pattern matching if a new statement is added
    case x@(_: Inhale | _: Exhale | _: Assert | _: Refute | _: Assume
            | _: Return | _: Fold | _: Unfold | _: PredExprFold | _: PredExprUnfold | _: Outline
            | _: SafeTypeAssertion | _: SafeReceive | _: Label | _: Initialization | _: PatternMatchStmt) => x

    case _ => violation("Unexpected case reached.")
  }

  private def genOverflowChecksExprs(exprs: Vector[Expr]): Vector[Assert] =
    exprs map (expr => {
      val info = createAnnotatedInfo(expr.info)
      Assert(assertionExprInBounds(expr, expr.typ)(info))(info)
    })

  // Checks if expr and its subexpressions are within bounds given by their type
  private def assertionExprInBounds(expr: Expr, typ: Type)(info: Source.Parser.Info): Assertion = {
    val trueLit: Expr = BoolLit(b = true)(info)

    def genAssertionExpr(expr: Expr, typ: Type): Expr = {
      typ match {
        case IntT(_, kind) if kind.isInstanceOf[BoundedIntegerKind] =>
          val boundedKind = kind.asInstanceOf[BoundedIntegerKind]
          And(
            AtMostCmp(IntLit(boundedKind.lower)(info), expr)(info),
            AtMostCmp(expr, IntLit(boundedKind.upper)(info))(info))(info)

        case _ => trueLit
      }
    }

    val intSubExprsWithType: Set[(Expr, Type)] = Expr.getSubExpressions(expr)
      .filter(_.typ.isInstanceOf[IntT])
      .map(e => if (e == expr) (expr, typ) else (e, e.typ))

    // values assumed to be within bounds, i.e. variables, fields from structs, dereferences of pointers and indexed expressions
    // this stops Gobra from throwing overflow errors in field accesses and pointer dereferences because Gobra was not able to prove that
    // they were within bounds even though that is guaranteed by the expression's type
    val valuesWithinBounds = intSubExprsWithType.filter(elem => elem._1 match {
      case _: Var | _: FieldRef | _: IndexedExp | _: Deref => true
      case _ => false
    })

    val computeAssertions = (exprsWithType: Set[(Expr, Type)]) =>
      exprsWithType
        .map{elem => genAssertionExpr(elem._1, elem._2)}
        .foldRight(trueLit)((x,y) => And(x,y)(info))

    // assumptions for the values that are considered within bounds
    val assumptions = computeAssertions(valuesWithinBounds)

    // Assertions that need to be verified assuming the expressions in `assumptions`
    val obligations = ExprAssertion(computeAssertions(intSubExprsWithType))(info)
    Implication(assumptions, obligations)(info)
  }

  private def createAnnotatedInfo(info: Source.Parser.Info): Source.Parser.Info =
    info match {
      case s: Single => s.createAnnotatedInfo(OverflowCheckAnnotation)
      // the following is temporary hack that will be discarded when we merge the new support for overflow checking
      case i@ Internal => i
      case i => violation(s"l.op.info ($i) is expected to be a Single")
    }
}
