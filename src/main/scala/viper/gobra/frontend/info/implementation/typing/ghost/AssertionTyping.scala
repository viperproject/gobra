package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.BooleanT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait AssertionTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefAssertion: WellDefinedness[PAssertion] = createWellDef {

    case n@ PStar(left, right) => noMessages
    case n@ PImplication(left, right) => assignableTo.errors(exprType(left), BooleanT)(n)
    case n@ PExprAssertion(exp) => assignableTo.errors(exprType(exp), BooleanT)(n)
    case n@ PAccess(exp) => exp match {
      case _: PDereference => noMessages
    }
  }
}
