package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Constant, Embbed, Field, Function, MethodImpl, Variable}
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, Type}
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation.violation

trait GhostExprTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostExpr(expr: PGhostExpression): Messages = expr match {

    case POld(op) => isExpr(op).out ++ isPureExpr(op)

    case PConditional(cond, thn, els) =>
      // check that cond is of type bool:
      isExpr(cond).out ++ isExpr(thn).out ++ isExpr(els).out ++
        assignableTo.errors(exprType(cond), BooleanT)(expr) ++
        // check that thn and els have a common type
        mergeableTypes.errors(exprType(thn), exprType(els))(expr)

    case PForall(vars, triggers, body) =>
      // check whether all triggers are valid and consistent
      validTriggers(vars, triggers) ++
      // check that the quantifier `body` is either Boolean or an assertion
      isExpr(body).out ++ assignableTo.errors(exprType(body), AssertionT)(expr)

    case PExists(vars, triggers, body) =>
      // check whether all triggers are valid and consistent
      validTriggers(vars, triggers) ++
      // check that the quantifier `body` is Boolean
      isPureExpr(body) ++ isExpr(body).out ++ assignableTo.errors(exprType(body), BooleanT)(expr)

    case n: PImplication =>
      isExpr(n.left).out ++ isExpr(n.right).out ++
      // check that left side is a Boolean expression
        assignableTo.errors(exprType(n.left), BooleanT)(expr) ++
      // check that right side is either Boolean or an assertion
        assignableTo.errors(exprType(n.right), AssertionT)(expr)

    case n: PAccess => resolve(n.exp) match {
      case Some(_: ap.Deref) => noMessages
      case Some(_: ap.FieldSelection) => noMessages
      case Some(_: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.exp}")
    }

    case n: PPredicateAccess => resolve(n.pred) match {
      case Some(_: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.pred}")
    }
  }

  private[typing] def ghostExprType(expr: PGhostExpression): Type = expr match {

    case POld(op) => exprType(op)

    case PConditional(_, thn, els) =>
      typeMerge(exprType(thn), exprType(els)).getOrElse(violation("no common supertype found"))

    case PForall(_, _, body) => exprType(body)

    case PExists(_, _, body) => exprType(body)

    case n: PImplication => exprType(n.right) // implication is assertion or boolean iff its right side is

    case _: PAccess | _: PPredicateAccess => AssertionT
  }

  private[typing] def isPureExpr(expr: PExpression): Messages = {
    message(expr, s"expected pure expression but got $expr", !isPureExprAttr(expr))
  }

  private def isPureId(id: PIdnNode): Boolean = entity(id) match {
    case _: Constant => true
    case _: Variable => true
    case _: Field => true
    case _: Embbed => true
    case Function(decl, _) => decl.spec.isPure
    case MethodImpl(decl, _) => decl.spec.isPure
    case _ => false
  }

  private lazy val isPureExprAttr: PExpression => Boolean =
    attr[PExpression, Boolean] {
      case n@ PNamedOperand(id) => isPureId(id)

      case _: PBoolLit | _: PIntLit | _: PNilLit => true

      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(p: ap.Conversion)) => false // Might change at some point
        case (Left(callee), Some(p: ap.FunctionCall)) => isPureExprAttr(callee) && p.args.forall(isPureExprAttr)
        case _ => false
      }

      case n: PDot => exprOrType(n.base) match {
        case Left(e) => isPureExprAttr(e) && isPureId(n.id)
        case Right(_) => isPureId(n.id) // Maybe replace with a violation
      }

      case n@PReference(e) => isPureExprAttr(e)
      case n: PDeref =>
        resolve(n) match {
          case Some(p: ap.Deref) => isPureExprAttr(p.base)
          case _ => true
        }

      case PNegation(e) => isPureExprAttr(e)

      case x: PBinaryExp => isPureExprAttr(x.left) && isPureExprAttr(x.right) && (x match {
          case _: PEquals | _: PUnequals |
               _: PAnd | _: POr |
               _: PLess | _: PAtMost | _: PGreater | _: PAtLeast |
               _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv => true
          case _ => false
        })

      case _: PUnfolding => true
      case _: POld => true
      case _: PForall => true
      case _: PExists => true

      case PConditional(cond, thn, els) => Seq(cond, thn, els).forall(isPureExprAttr)

      case PImplication(left, right) => Seq(left, right).forall(isPureExprAttr)

        case _: PAccess | _: PPredicateAccess => false

      case n@PCompositeLit(t, lit) => true

      // Might change soon:
      case n@PIndexedExp(base, index) => false

      // Might change as some point
      case _: PFunctionLit => false
      case n@PSliceExp(base, low, high, cap) => false

      // Others
      case n@PTypeAssertion(base, typ) => false
      case n@PReceive(e) => false
    }

  /**
    * Helper operation for composing two results of `validTriggerPattern` into one.
    */
  private def combineTriggerResults(p1 : (Vector[String], Messages), p2 : (Vector[String], Messages)) : (Vector[String], Messages) =
    (p1._1 ++ p2._1, p1._2 ++ p2._2)

  /**
    * Helper operator for composing a sequence of results
    * of of `validTriggerPattern` into one.
    */
  private def combineTriggerResults(xs : Vector[(Vector[String], Messages)]) : (Vector[String], Messages) =
    xs.fold(Vector(), noMessages)(combineTriggerResults)

  /**
    * Determines whether `expr` is a valid trigger pattern.
    * @param expr The expression that is to be checked for validity.
    * @return A pair consisting of (1) all identifiers occurring freely in `expr`,
    *         to be used later for extra consistency checking; and (2) a sequence
    *         of error messages. The latter sequence is empty if
    *         (but currently not only if) `expr` is a valid trigger pattern.
    */
  private def validTriggerPattern(expr : PExpression) : (Vector[String], Messages) = {
    // shorthand definition
    def goEorT(node : PExpressionOrType) : (Vector[String], Messages) = node match {
      case PDeref(base) => goEorT(base)
      case e : PExpression => validTriggerPattern(e)
      case _ => (Vector(), noMessages)
    }

    expr match {
      case PDot(base, _) => goEorT(base)
      case PInvoke(base, args) => {
        val res1 = goEorT(base)
        val res2 = combineTriggerResults(args.map(validTriggerPattern))
        combineTriggerResults(res1, res2)
      }
      case PNamedOperand(id) => (Vector(id.name), noMessages)
      case _ => (Vector(), message(expr, s"invalid trigger pattern '$expr'"))
    }
  }

  /**
    * Determines whether `trigger` is a valid and consistent trigger.
    * Validity and consistency in this sense mean:
    *
    * 1. Any trigger expression in `trigger` must be valid according to `validTriggerPattern`;
    * 2. All variables `boundVars` bound by the quantified must occur in `trigger`.
    *
    * @param boundVars The variables bound by the quantifier in which `trigger` is used.
    * @param trigger The trigger to be tested for validity and consistency.
    * @return True if  `trigger` is a valid and consistent trigger.
    */
  private def validTrigger(boundVars: Vector[PBoundVariable], trigger : PTrigger) : Messages = {
    // [validity] check whether all expressions in `trigger` are valid trigger expressions
    val (usedVars, msgs1) = combineTriggerResults(trigger.exps.map(validTriggerPattern))

    // [consistency] check whether all `boundVars` occur inside `trigger`
    val msgs2 = (boundVars filterNot (v => usedVars.contains(v.id.name)))
      .flatMap(v => message(v, s"consistency error: variable '${v.id}' is not mentioned in the trigger pattern '$trigger'"))

    msgs1 ++ msgs2
  }

  /**
    * Determines whether `triggers` is a valid and consistent sequence of triggers.
    * Currently `triggers` is valid if every trigger `t` in this
    * sequence is valid and consistent with respect to `validTrigger(t)`.
    * @param triggers The sequence of triggers to be tested for validity.
    * @return True if `triggers` is a valid sequence of triggers.
    */
  private def validTriggers(vars: Vector[PBoundVariable], triggers : Vector[PTrigger]) : Messages =
    triggers.flatMap(validTrigger(vars, _))
}
