package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type.{BooleanT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation

trait AssertionTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefAssertion: WellDefinedness[PAssertion] = createWellDef {

    case n@ PStar(left, right) => noMessages
    case n@ PImplication(left, right) => isExpr(left).out ++ assignableTo.errors(exprType(left), BooleanT)(n)
    case n@ PExprAssertion(exp) => isExpr(exp).out ++ assignableTo.errors(exprType(exp), BooleanT)(n) ++ isPureExpr(exp)
    case n@ PAccess(exp) => exp match {
      case _: PReference => noMessages
      case n: PDeref => isExpr(n).out
      case n: PDot => resolve(n) match {
        case Some(_: ap.FieldSelection) => noMessages
        case _ => message(n, "selections in access predicates have to target fields")
      }
    }

    case n: PPredicateCall => n match {

      case PFPredOrBoolFuncCall(id, args) =>
        args.flatMap(isPureExpr) ++
          getLeftOrElse(wellDefBase(id)(n))(formalT => multiAssignableTo.errors(args map exprType, formalT)(n))

      case PMPredOrBoolMethCall(recv, id, args) =>
        isPureExpr(recv) ++ args.flatMap(isPureExpr) ++
        getLeftOrElse(wellDefBase(recv, id)(n))(formalT => multiAssignableTo.errors(args map exprType, formalT)(n))

      case PMPredOrMethExprCall(base, id, args) =>
        args.flatMap(isPureExpr) ++
          getLeftOrElse(wellDefBase(base, id)(n))(formalT => multiAssignableTo.errors(args map exprType, formalT)(n))

      case PMPredOrMethRecvOrExprCall(base, id, args) =>
        args.flatMap(isPureExpr) ++
          getLeftOrElse(wellDefBase(base, id)(n))(formalT => multiAssignableTo.errors(args map exprType, formalT)(n))

      case PMemoryPredicateCall(arg) => isPureExpr(arg) ++ isClassType.errors(exprType(arg))(n)
    }

    case n@ PPredicateAccess(predicateCall) => predicateCall match {
      case PFPredOrBoolFuncCall(id, args) => isPredicate(id)
      case PMPredOrBoolMethCall(recv, id, args) => isPredicate(id)
      case PMPredOrMethExprCall(base, id, args) => isPredicate(id)
      case PMPredOrMethRecvOrExprCall(base, id, args) => isPredicate(id)
      case PMemoryPredicateCall(arg) => noMessages
    }
  }


  private def getLeftOrElse[L,R](e: Either[L,R])(f: R => L): L =
    e.left.getOrElse(f(e.right.get))

  private def ifNoMessages[T](m: Messages)(f: => T): Either[Messages, T] =
    if (m.isEmpty) Left(m) else Right(f)


  private def wellDefBase(id: PIdnUse)(n: PNode): Either[Messages, Vector[Type]] = entity(id) match {
    case Function(decl, _) =>
      ifNoMessages(
        assignableTo.errors(miscType(decl.result), BooleanT)(n) ++ message(n, "expected pure method", !decl.spec.isPure)
      )(decl.args map miscType)

    case FPredicate(decl) => Right(decl.args map miscType)

    case e => Left(message(n, s"expected function of predicate but got $e"))
  }

  private def wellDefBase(recv: PExpression, id: PIdnUse)(n: PNode): Either[Messages, Vector[Type]] = entity(id) match {
    case MethodImpl(decl, _) =>
      ifNoMessages(
          assignableTo.errors(miscType(decl.result), BooleanT)(n) ++
          wellDefSelection(recv, id)(n) ++
          message(n, "expected pure method", !decl.spec.isPure)
      )(decl.args map miscType)

    case MethodSpec(sig, _) =>
      ifNoMessages(
        assignableTo.errors(miscType(sig.result), BooleanT)(n) ++ wellDefSelection(recv, id)(n) // TODO ++ message(n, "expected pure method", !sig.spec.isPure)
      )(sig.args map miscType)


    case MPredicateImpl(decl) =>
      ifNoMessages(
        wellDefPredicateSelection(recv, id)(n)
      )(decl.args map miscType)

    case MPredicateSpec(decl) =>
      ifNoMessages(
        wellDefPredicateSelection(recv, id)(n)
      )(decl.args map miscType)

    case e => Left(message(n, s"expected function of predicate but got $e"))
  }

  private def wellDefBase(recv: PMethodRecvType, id: PIdnUse)(n: PNode): Either[Messages, Vector[Type]] = entity(id) match {
    case MethodImpl(decl, _) =>
      ifNoMessages(
        assignableTo.errors(miscType(decl.result), BooleanT)(n) ++ wellDefMethodExpr(recv, id)(n) ++ message(n, "expected pure method", !decl.spec.isPure)
      )(typeType(recv) +: (decl.args map miscType))

    case MethodSpec(sig, _) =>
      ifNoMessages(
        assignableTo.errors(miscType(sig.result), BooleanT)(n) ++ wellDefMethodExpr(recv, id)(n) // TODO ++ message(n, "expected pure method", !sig.spec.isPure)
      )(typeType(recv) +: (sig.args map miscType))

    case MPredicateImpl(decl) =>
      ifNoMessages(
        wellDefPredicateExpr(recv, id)(n)
      )(typeType(recv) +: (decl.args map miscType))

    case MPredicateSpec(decl) =>
      ifNoMessages(
        wellDefPredicateExpr(recv, id)(n)
      )(typeType(recv) +: (decl.args map miscType))

    case e => Left(message(n, s"expected function of predicate but got $e"))
  }

  private def wellDefBase(recv: PIdnUse, id: PIdnUse)(n: PNode): Either[Messages, Vector[Type]] = {
    val recvOpt = if (pointsToType(recv)) Vector(idType(recv)) else Vector.empty

    entity(id) match {
      case MethodImpl(decl, _) =>
        ifNoMessages(
          assignableTo.errors(miscType(decl.result), BooleanT)(n) ++ wellDefSelectionOrMethodExpr(recv, id)(n) ++ message(n, "expected pure method", !decl.spec.isPure)
        )(recvOpt ++ (decl.args map miscType))

      case MethodSpec(sig, _) =>
        ifNoMessages(
          assignableTo.errors(miscType(sig.result), BooleanT)(n) ++ wellDefSelectionOrMethodExpr(recv, id)(n) // TODO ++ message(n, "expected pure method", !sig.spec.isPure)
        )(recvOpt ++ (sig.args map miscType))

      case MPredicateImpl(decl) =>
        ifNoMessages(
          wellDefPredicateSelectionOrExpr(recv, id)(n)
        )(recvOpt ++ (decl.args map miscType))

      case MPredicateSpec(decl) =>
        ifNoMessages(
          wellDefPredicateSelectionOrExpr(recv, id)(n)
        )(recvOpt ++ (decl.args map miscType))

      case e => Left(message(n, s"expected function of predicate but got $e"))
    }
  }



  /** predicate version of @see [[wellDefSelectionOrMethodExpr()]] */
  private def wellDefPredicateSelectionOrExpr(base: PIdnUse, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${idType(base)} does not have method ${id.name}"
      , if (pointsToType(base)) !findMethodLike(idType(base), id).exists(_.isInstanceOf[MPredicate])
      else if (pointsToData(base)) !findSelection(base, id).exists(_.isInstanceOf[MPredicate])
      else Violation.violation("base should be either a type or data")
    )
  }

  /** predicate version of @see [[wellDefMethodExpr()]] */
  private def wellDefPredicateExpr(t: PMethodRecvType, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${typeType(t)} does not have method ${id.name}"
      , !findMethodLike(typeType(t), id).exists(_.isInstanceOf[MPredicate]))
  }

  /** predicate version of @see [[wellDefSelection]] */
  private def wellDefPredicateSelection(base: PExpression, id: PIdnUse)(n: PNode): Messages = {
    message(n, s"type ${exprType(base)} does not have method ${id.name}"
      , !findSelection(base, id).exists(_.isInstanceOf[MPredicate]))
  }



  private def isPredicate(id: PIdnNode): Messages = {
    val ent = entity(id)
    message(id, s"expected predicate but got $ent", !ent.isInstanceOf[Predicate])
  }
}
