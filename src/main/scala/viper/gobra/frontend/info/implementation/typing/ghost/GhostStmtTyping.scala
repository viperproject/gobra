package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.AssertionT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostStmtTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostStmt(stmt: PGhostStatement): Messages = stmt match {
    case n@PExplicitGhostStatement(s) => message(n, "ghost error: expected ghostifiable statement", !s.isInstanceOf[PGhostifiableStatement])
    case n@PAssert(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case n@PExhale(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case n@PAssume(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case n@PInhale(exp) => assignableTo.errors(exprType(exp), AssertionT)(stmt)
    case n@PFold(exp) => noMessages
    case n@PUnfold(exp) => noMessages
  }
}
