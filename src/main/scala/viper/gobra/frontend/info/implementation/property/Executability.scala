package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PBuildIn, PCall, PConversionOrUnaryCall, PExpression}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Executability extends BaseProperty { this: TypeInfoImpl =>

  lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
    case n: PCall => true
    case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n)((_, _) => false)((_,_) => true).get
    case _ => false
  }

  // TODO: probably will be unneccessary because build int functions always have to be called

  private lazy val isBuildIn: Property[PExpression] = createBinaryProperty("buit-in") {
    case t: PBuildIn => true
    case _ => false
  }
}
