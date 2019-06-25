package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, noMessages}
import viper.gobra.ast.frontend.{PExplicitGhostMember, PGhostMember}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMemberTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMember(member: PGhostMember): Messages = member match {
    case PExplicitGhostMember(m) => noMessages
  }
}
