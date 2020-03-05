package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PBuildIn, PExpression, PInvoke}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}

trait Executability extends BaseProperty { this: TypeInfoImpl =>

  lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
    case n: PInvoke =>
      resolve(n) match {
        case Some(p: ap.FunctionCall) => true
        case _ => false
      }
    case _ => false
  }

  // TODO: probably will be unneccessary because build int functions always have to be called

  private lazy val isBuildIn: Property[PExpression] = createBinaryProperty("buit-in") {
    case t: PBuildIn => true
    case _ => false
  }
}
