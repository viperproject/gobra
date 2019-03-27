package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PBuildIn, PCall, PExpression}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Executability extends BaseProperty { this: TypeInfoImpl =>

  lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
    case PCall(callee, _) => !isBuildIn(callee)
    case _ => false
  }

  // TODO: probably will be unneccessary because build int functions always have to be called

  private lazy val isBuildIn: Property[PExpression] = createBinaryProperty("buit-in") {
    case t: PBuildIn => true
    case _ => false
  }
}
