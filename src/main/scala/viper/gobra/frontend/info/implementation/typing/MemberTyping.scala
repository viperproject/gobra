package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl => wellDefIfPureFunction(n)
    case m: PMethodDecl => isClassType.errors(miscType(m.receiver))(member) ++ wellDefIfPureMethod(m)
    case s: PActualStatement => wellDefStmt(s).out
  }
}
