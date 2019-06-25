package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type.{DeclaredT, PointerT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl => noMessages

    case m: PMethodDecl => miscType(m.receiver) match {
      case DeclaredT(_) | PointerT(DeclaredT(_)) => noMessages
      case _ => message(m, s"method cannot have non-defined receiver")
    }

    case s: PActualStatement => wellDefStmt(s).out
  }
}
