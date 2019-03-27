package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import viper.gobra.ast.frontend.{PFunctionDecl, PMethodDecl, PTopLevel}
import viper.gobra.frontend.info.base.Type.{DeclaredT, PointerT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TopLevelTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefTop: WellDefinedness[PTopLevel] = createWellDef {

    case n: PFunctionDecl => noMessages

    case m: PMethodDecl => miscType(m.receiver) match {
      case DeclaredT(_) | PointerT(DeclaredT(_)) => noMessages
      case _ => message(m, s"method cannot have non-defined receiver")
    }
  }
}
