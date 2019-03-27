package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.PTypeDecl
import viper.gobra.frontend.info.base.Type.{DeclaredT, Single, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait UnderlyingType { this: TypeInfoImpl =>

  lazy val underlyingType: Type => Type =

    attr[Type, Type] {
      case Single(DeclaredT(t: PTypeDecl)) => typeType(t.right)
      case t => t
    }

}
