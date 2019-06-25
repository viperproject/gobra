package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{ActualTypeMember, GhostTypeMember, GhostifiedEntity}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case n@ PExplicitGhostParameter(param) => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case n@ PExplicitGhostParameter(param) => miscType(param)
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case GhostifiedEntity(mt: ActualTypeMember) => actualMemberType(mt)
  }

}
