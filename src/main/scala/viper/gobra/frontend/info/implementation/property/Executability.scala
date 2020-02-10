package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PBuildIn, PExpression}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.resolution.{AstPattern => ap}

trait Executability extends BaseProperty { this: TypeInfoImpl =>

  lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
    e: PExpression => resolve(e) match {
      case Some(ap.Call(_, _)) => true
      case Some(ap.Conversion(_, _)) => true
      case Some(ap.PredicateAccess(_, _)) => true
      case _ => false
    }
    //case n: PCall => true
    /*
    case n: PConversionOrUnaryCall => resolveConversionOrUnaryCall(n)((_, _) => false)((_,_) => true).get
    case _ => false
     */
  }

  // TODO: probably will be unneccessary because build int functions always have to be called

  private lazy val isBuildIn: Property[PExpression] = createBinaryProperty("buit-in") {
    case t: PBuildIn => true
    case _ => false
  }
}
