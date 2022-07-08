// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PClosureImplProof, AstPattern => ap, _}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation

import scala.annotation.tailrec

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => error(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case PAssert(exp) => assignableToSpec(exp)
    case PExhale(exp) => assignableToSpec(exp)
    case PAssume(exp) => assignableToSpec(exp) ++ isPureExpr(exp)
    case PInhale(exp) => assignableToSpec(exp)
    case PFold(acc) => wellDefFoldable(acc)
    case PUnfold(acc) => wellDefFoldable(acc)
    case n@PPackageWand(wand, optBlock) => assignableToSpec(wand) ++
      error(n, "ghost error: expected ghostifiable statement", !optBlock.forall(_.isInstanceOf[PGhostifiableStatement]))
    case PApplyWand(wand) => assignableToSpec(wand)
    case p: PClosureImplProof => wellDefClosureImplProof(p)
  }

  private[typing] def wellDefFoldable(acc: PPredicateAccess): Messages = {
    def isAbstract(p: st.Predicate): Boolean = p match {
      case fp: st.FPredicate => fp.decl.body.isEmpty
      case mp: st.MPredicateImpl => mp.decl.body.isEmpty
      case _: st.MPredicateSpec =>
        // counter-intuitive: interface well-definedness will make sure that implementations implement the declared predicates
        false
    }

    resolve(acc.pred) match {
      case Some(_: ap.PredExprInstance) =>
        error(
          acc,
          s"expected a predicate constructor, but got ${acc.pred.base}",
          !acc.pred.base.isInstanceOf[PPredConstructor])
      case Some(ap.PredicateCall(pred, _)) => pred match {
        case p: ap.SymbolicPredicateKind => error(acc, s"abstract predicates are not foldable", isAbstract(p.symb))
        case p: ap.BuiltInPredicateKind => error(acc, s"abstract predicates are not foldable", p.symb.tag.isAbstract)
        case _: ap.PredExprInstance => error(acc, s"predicate expression calls are not foldable")
      }

      case _ => error(acc, s"unexpected predicate access")
    }
  }

  private def wellDefClosureImplProof(p: PClosureImplProof): Messages = {
    val PClosureImplProof(impl@PClosureImplements(closure, spec), b: PBlock) = p

    val func = resolve(spec.func) match {
      case Some(ap.Function(_, f)) => f
      case Some(ap.Closure(_, c)) => c
      case _ => Violation.violation(s"expected a function or closure, but got ${spec.func}")
    }

    val specArgs = if (spec.params.forall(_.key.isEmpty)) func.args.drop(spec.params.size)
    else {
      val paramSet = spec.params.map(_.key.get.name).toSet
      func.args.filter(nameFromParam(_).fold(true)(!paramSet.contains(_)))
    }

    val isPure = func match {
      case f: st.Function => f.isPure
      case c: st.Closure => c.isPure
    }

    lazy val expectedCallArgs = specArgs.flatMap(nameFromParam).map(a => PNamedOperand(PIdnUse(a)))

    def isExpectedCall(e: PExpression): Boolean = e match {
      case i: PInvoke => i.base == closure && i.args == expectedCallArgs
      case c: PCallWithSpec => c.base == closure && c.args == expectedCallArgs
      case _ => false
    }

    lazy val expectedCallString: String = s"$closure(${specArgs.flatMap(nameFromParam).mkString(",")}) [as _]"

    def wellDefIfNecessaryArgsNamed: Messages = error(spec,
      s"cannot find a name for all arguments or results required by $spec",
      cond = !specArgs.forall {
        case _: PUnnamedParameter | PExplicitGhostParameter(_: PUnnamedParameter) => false
        case _ => true
      }
    )

    def wellDefIfArgNamesDoNotShadowClosure: Messages = {
      val names = (func.args ++ func.result.outs).map(nameFromParam).filter(_.nonEmpty).map(_.get)

      def isShadowed(id: PIdnNode): Boolean = id match {
        case _: PIdnUse | _: PIdnUnk =>
          val entityOutside = tryLookupAt(id, impl)
          entityOutside.nonEmpty && tryLookupAt(id, id).fold(false)(_ eq entityOutside.get) && names.contains(id.name)
        case _ => false
      }

      def shadowedInside(n: PNode): Option[PIdnNode] = n match {
        case id: PIdnNode => Some(id).filter(isShadowed)
        case _ => tree.child(n).iterator.map(shadowedInside).find(_.nonEmpty).flatten
      }
      val shadowed = shadowedInside(closure)
      error(impl,
        s"identifier ${shadowed.getOrElse("")} in $closure is shadowed by an argument or result with the same name in ${spec.func}",
        shadowed.nonEmpty)
    }

    def pureWellDefIfIsSinglePureReturnExpr: Messages = if (isPure) isPureBlock(b) else noMessages

    def pureWellDefIfRightShape: Messages = if (!isPure) {
      noMessages
    } else {
      val retExpr = b.nonEmptyStmts.head.asInstanceOf[PReturn].exps.head
      pureImplementationProofHasRightShape(retExpr, isExpectedCall, expectedCallString)
        .asReason(retExpr, "invalid return expression of an implementation proof")
    }

    def wellDefIfRightShape: Messages =
      if (isPure) noMessages
      else implementationProofBodyHasRightShape(b, isExpectedCall, expectedCallString, func.result)
        .asReason(b, "invalid body of an implementation proof")

    Seq(wellDefIfNecessaryArgsNamed, wellDefIfArgNamesDoNotShadowClosure, pureWellDefIfIsSinglePureReturnExpr,
      pureWellDefIfRightShape, wellDefIfRightShape).iterator.find(_.nonEmpty).getOrElse(noMessages)
  }

  private def nameFromParam(p: PParameter): Option[String] = p match {
    case PNamedParameter(id, _) => Some(id.name)
    case PExplicitGhostParameter(PNamedParameter(id, _)) => Some(id.name)
    case _ => None
  }

  private [ghost] def pureImplementationProofHasRightShape(retExpr: PExpression,
                                                           isExpectedCall: PExpression => Boolean,
                                                           expectedCall: String): PropertyResult = {
    @tailrec
    def validExpression(expr: PExpression): PropertyResult = expr match {
      case _: PInvoke | _: PCallWithSpec => failedProp(s"The call must be $expectedCall", !isExpectedCall(expr))
      case f: PUnfolding => validExpression(f.op)
      case _ => failedProp(s"only unfolding expressions and the call $expectedCall is allowed")
    }

    validExpression(retExpr)
  }

  // the body can only contain fold, unfold, and the call
  private [ghost] def implementationProofBodyHasRightShape(body: PBlock,
                                                           isExpectedCall: PExpression => Boolean,
                                                           expectedCall: String,
                                                           result: PResult): PropertyResult = {
    val expectedResults = result.outs.flatMap(nameFromParam).map(t => PNamedOperand(PIdnUse(t)))

    def isExpectedAssignment(ass: PAssignment): Boolean = {
      result.outs.nonEmpty && expectedResults.size == result.outs.size &&
      ass.right.size == 1 && isExpectedCall(ass.right.head) && expectedResults == ass.left
    }

    def isExpectedReturn(ret: PReturn): Boolean = ret match {
      case PReturn(exps) =>
        if (result.outs.isEmpty) exps == Vector.empty
        else if (result.outs.size != expectedResults.size) exps == Vector.empty || (exps.size == 1 && isExpectedCall(exps.head))
        else exps == Vector.empty || exps == expectedResults || (exps.size == 1 && isExpectedCall(exps.head))
    }

    lazy val expectedReturns: Seq[String] =
      if (result.outs.isEmpty) Seq("return")
      else if (result.outs.size != expectedResults.size) Seq("return", s"return $expectedCall")
      else Seq("return", PReturn(expectedResults).toString, s"return $expectedCall")

    lazy val expectedCallStatement = if (result.outs.isEmpty) expectedCall
      else if (expectedResults.size != result.outs.size) s"return $expectedCall"
      else s"${expectedResults.mkString(",")} = $expectedCall"

    lazy val lastStatement: PStatement = {
      @tailrec
      def aux(stmt: PStatement): PStatement = stmt match {
        case seq: PSeq => aux(seq.nonEmptyStmts.last)
        case block: PBlock => aux(block.nonEmptyStmts.last)
        case s => s
      }
      aux(body)
    }

    var numOfImplemetationCalls = 0

    def validStatements(stmts: Vector[PStatement]): PropertyResult =
      PropertyResult.bigAnd(stmts.map {
        case _: PUnfold | _: PFold | _: PAssert | _: PEmptyStmt => successProp
        case _: PAssume | _: PInhale | _: PExhale => failedProp("Assume, inhale, and exhale are forbidden in implementation proofs")

        case b: PBlock => validStatements(b.nonEmptyStmts)
        case seq: PSeq => validStatements(seq.nonEmptyStmts)

        case ass: PAssignment =>
          // Right now, we only allow assignments that are used for the one call
          if (isExpectedAssignment(ass)) {
            numOfImplemetationCalls += 1
            successProp
          } else if (result.outs.isEmpty || expectedResults.size != result.outs.size) {
            val reason =
              if (result.outs.isEmpty) "Here, there are no out-parameters."
              else "Here, not all out-parameters have a name, so they cannot be assigned to."
            failedProp("An assignment must assign to all out-parameters. " + reason)
          } else {
            failedProp(s"The only allowed assignment is $expectedCallStatement")
          }

        // A call alone can only occur if there are no result parameters
        case PExpressionStmt(e: PExpression) if e.isInstanceOf[PInvoke] || e.isInstanceOf[PCallWithSpec] =>
          if (result.outs.nonEmpty) failedProp(s"The call '$e' is missing the out-parameters")
          else if (isExpectedCall(e)) failedProp(s"The only allowed call is $expectedCall")
          else {
            numOfImplemetationCalls += 1
            successProp
          }


        case ret@PReturn(exps) =>
          // there has to be at most one return at the end of the block
          if (lastStatement != ret) {
            failedProp("A return must be the last statement")
          } else if (isExpectedReturn(ret)) {
            if (exps.size == 1 && isExpectedCall(exps.head)) numOfImplemetationCalls += 1
            successProp
          } else failedProp(s"A return must be one of ${expectedReturns.mkString(", ")}")

        case _ => failedProp("Only fold, unfold, assert, and one call to the implementation are allowed")
      })

    val bodyHasRightShape = validStatements(body.nonEmptyStmts)
    val notTooManyCalls = {
      if (numOfImplemetationCalls != 1) {
        failedProp(s"There must be exactly one call to the implementation " +
          s"(with results and arguments in the right order '$expectedCallStatement')")
      } else successProp
    }

    bodyHasRightShape and notTooManyCalls
  }
}
