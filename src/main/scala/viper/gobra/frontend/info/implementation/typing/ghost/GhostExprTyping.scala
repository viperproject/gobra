package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Constant, Embbed, Field, Function, MethodImpl, Variable}
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, IntT, SequenceT, Type}
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

    case n: PImplication =>
      isExpr(n.left).out ++ isExpr(n.right).out ++
      // check that left side is a boolean expression
        assignableTo.errors(exprType(n.left), BooleanT)(expr) ++
      // check that right side is either boolean or an assertion
        assignableTo.errors(exprType(n.right), AssertionT)(expr)

    case n: PAccess => resolve(n.exp) match {
      case Some(p: ap.Deref) => noMessages
      case Some(p: ap.FieldSelection) => noMessages
      case Some(p: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.exp}")
    }

    case n: PPredicateAccess => resolve(n.pred) match {
      case Some(p: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.pred}")
    }

    case PSize(op) => isExpr(op).out ++ (exprType(op) match {
      case SequenceT(_) => noMessages
      case t => message(op, s"expected sequence or (multi)set, but got '$t'")
    })

    case PSequenceLiteral(typ, exprs) => {
      val t = typeType(typ)
      exprs.flatMap(e => assignableTo.errors(exprType(e), t)(e))
    }

    case n@PSequenceAppend(left, right) => (exprType(left), exprType(right)) match {
      case (SequenceT(t1), SequenceT(t2)) =>
        message(n, s"both operands are expected to be of an identical type, but got '$t1' and '$t2'", !identicalTypes(t1, t2))
      case (t1, t2) => message(n, s"both operands are expected to be of a sequence type, but got '$t1' and '$t2'")
    }
  }

  private[typing] def ghostExprType(expr: PGhostExpression): Type = expr match {
    case POld(op) => exprType(op)

    case PConditional(cond, thn, els) =>
      typeMerge(exprType(thn), exprType(els)).getOrElse(violation("no common supertype found"))

    case n: PImplication => exprType(n.right) // implication is assertion or boolean iff its right side is

    case _: PAccess | _: PPredicateAccess => AssertionT

    case PSize(_) => IntT

    case PSequenceLiteral(typ, _) => SequenceT(typeType(typ))

    case PSequenceAppend(left, right) => (exprType(left), exprType(right)) match {
      case (SequenceT(t1), SequenceT(t2)) if identicalTypes(t1, t2) => SequenceT(t1)
      case (t1, t2) => violation(s"operands of sequence append are of unidentical types '$t1' and '$t2'")
    }
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

      case n: PUnfolding => true

      case _: POld => true

      case PConditional(cond, thn, els) => Seq(cond, thn, els).forall(isPureExprAttr)

      case PImplication(left, right) => Seq(left, right).forall(isPureExprAttr)

      case PSize(op) => isPureExprAttr(op)
      case PSequenceLiteral(_, exprs) => exprs.forall(isPureExprAttr)
      case PSequenceAppend(left, right) => isPureExprAttr(left) && isPureExprAttr(right)

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


}
