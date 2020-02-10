package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend.{PAccess, PAssertionExpression, PConditional, PDereference, PGhostExpression, POld, PReference, PSelection}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type.{AssertionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation.violation

trait AssertionExprTyping extends BaseTyping {
  this: TypeInfoImpl =>

  private[typing] def wellDefAssertionExpr(expr: PAssertionExpression): Messages = expr match {
    case n@PAccess(exp) => exp match {
      case _: PDereference => noMessages
      case _: PReference => noMessages
      case s: PSelection => message(n, "selections in access predicates have to target fields", !entity(s.id).isInstanceOf[Field])
    }
  }

  private[typing] def assertionExprType(expr: PAssertionExpression): Type = expr match {
    case _: PAccess => AssertionT
  }
}
