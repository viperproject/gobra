package viper.gobra.frontend.info.implementation.typing.ghost

import viper.gobra.ast.frontend.PIdnNode
import viper.gobra.frontend.info.base.SymbolTable.{GhostRegular, Predicate}
import viper.gobra.frontend.info.base.Type.{AssertionT, FunctionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation.violation

trait GhostIdTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostRegular(entity: GhostRegular, id: PIdnNode): ValidityMessages = entity match {

    case predicate: Predicate => unsafeMessage(! {
      predicate.args.forall(wellDefMisc.valid)
    })

  }

  private[typing] def ghostEntityType(entity: GhostRegular, id: PIdnNode): Type = entity match {

    case predicate: Predicate => FunctionT(predicate.args map miscType, AssertionT)

    case _ => violation("untypable")
  }
}
