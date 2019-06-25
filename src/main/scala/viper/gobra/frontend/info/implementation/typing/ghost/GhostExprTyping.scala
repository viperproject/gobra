package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend.PGhostExpression
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostExprTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostExpr(expr: PGhostExpression): Messages = expr match {
    case _ => ???
  }

  private[typing] def ghostExprType(expr: PGhostExpression): Type = expr match {
    case _ => ???
  }
}
