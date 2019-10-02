package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type.BooleanT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait AssertionTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefAssertion: WellDefinedness[PAssertion] = createWellDef {

    case n@ PStar(left, right) => noMessages
    case n@ PImplication(left, right) => assignableTo.errors(exprType(left), BooleanT)(n)
    case n@ PExprAssertion(exp) => assignableTo.errors(exprType(exp), BooleanT)(n) ++ isPureExpr(exp)
    case n@ PAccess(exp) => exp match {
      case _: PDereference => noMessages
      case _: PReference => noMessages
      case s: PSelection => message(n, "selections in access predicates have to target fields", !entity(s.id).isInstanceOf[Field])
    }

    case n: PPredicateCall => n match {
      case PFPredCall(id, args) =>
        val formalsOpt = entity(id) match {
          case FPredicate(decl) => Right(decl.args map miscType)
          case e => Left(message(n, s"expected predicate but got $e"))
        }
        args.flatMap(isPureExpr) ++
          getLeftOrElse(formalsOpt)(formals => multiAssignableTo.errors(args map exprType, formals)(n))

      case PMPredCall(recv, id, args) =>
        val formalsOpt = entity(id) match {
          case MPredicateImpl(decl) => Right(decl.args map miscType)
          case MPredicateSpec(decl) => Right(decl.args map miscType)
          case e => Left(message(n, s"expected predicate but got $e"))
        }
        isPureExpr(recv) ++ args.flatMap(isPureExpr) ++
          getLeftOrElse(formalsOpt)(formals => multiAssignableTo.errors(args map exprType, formals)(n))

      case PMPredExprCall(base, id, args) =>
        val formalsOpt = entity(id) match {
          case MPredicateImpl(decl) => Right(typeType(base) +: (decl.args map miscType))
          case MPredicateSpec(decl) => Right(typeType(base) +: (decl.args map miscType))
          case e => Left(message(n, s"expected predicate but got $e"))
        }
        args.flatMap(isPureExpr) ++
          getLeftOrElse(formalsOpt)(formals => multiAssignableTo.errors(args map exprType, formals)(n))

      case n: PFPredOrBoolFuncCall => wellDefAssertion(rewriter.resolveFPredOrBoolFuncCall(n, resolver)).out
      case n: PMPredOrBoolMethCall => wellDefAssertion(rewriter.resolveMPredOrBoolMethCall(n, resolver)).out
      case n: PMPredOrMethExprCall => wellDefAssertion(rewriter.resolveMPredOrMethExprCall(n, resolver)).out
      case n: PMPredOrMethRecvOrExprCall => wellDefAssertion(rewriter.resolveMPredOrMethRecvOrExprCall(n, resolver)).out
    }

    case n@ PPredicateAccess(predicateCall) => predicateCall match {
      case _: PFPredCall | _: PMPredCall | _: PMPredExprCall => noMessages
      case PFPredOrBoolFuncCall(id, args) => isPredicate(id)
      case PMPredOrBoolMethCall(recv, id, args) => isPredicate(id)
      case PMPredOrMethExprCall(base, id, args) => isPredicate(id)
      case PMPredOrMethRecvOrExprCall(base, deref, id, args) => isPredicate(id)
      case PMemoryPredicateCall(arg) => noMessages
    }
  }

  private def getLeftOrElse[L,R](e: Either[L,R])(f: R => L): L =
    e.left.getOrElse(f(e.right.get))

  private def isPredicate(id: PIdnNode): Messages = {
    val ent = entity(id)
    message(id, s"expected predicate but got $ent", !ent.isInstanceOf[Predicate])
  }
}
