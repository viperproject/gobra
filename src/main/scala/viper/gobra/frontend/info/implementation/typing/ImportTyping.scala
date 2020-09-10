package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import viper.gobra.ast.frontend.{PExplicitQualifiedImport, PImplicitQualifiedImport, PImport, PUnqualifiedImport}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ImportTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefImport: WellDefinedness[PImport] = createWellDef {
    case _: PExplicitQualifiedImport => noMessages
    case _: PUnqualifiedImport => noMessages
    // this case should never occur as these nodes should get converted in the parse postprocessing step
    case n: PImplicitQualifiedImport => message(n, s"Explicit qualifier could not be derived")
  }
}
