package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.PTypeDecl
import viper.gobra.frontend.info.base.Type.{DeclaredT, InterfaceT, PointerT, Single, StructT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait UnderlyingType { this: TypeInfoImpl =>

  lazy val underlyingType: Type => Type =
    attr[Type, Type] {
      case Single(DeclaredT(t: PTypeDecl)) => underlyingType(typeType(t.right))
      case t => t
    }

  lazy val derefType: Type => Option[Type] =
    attr[Type, Option[Type]] {
      case Single(DeclaredT(t: PTypeDecl)) => derefType(typeType(t.right))
      case Single(PointerT(elem)) => Some(elem)
      case _ => None
    }

  lazy val isClassType: Property[Type] = createBinaryProperty("is class type") { t =>
      val relevantT = derefType(t) match {
        case Some(value) => underlyingType(value)
        case None => underlyingType(t)
      }
      relevantT.isInstanceOf[StructT]
    }

  lazy val isInterfaceType: Property[Type] = createBinaryProperty("is interface type") { t =>
    underlyingType(t) match {
      case InterfaceT(decl) => true
      case _ => false
    }
  }

  lazy val isClassOrInterfaceType: Property[Type] = createBinaryProperty("is class or interface type"){
    t => isClassType(t) || isInterfaceType(t)
  }

}
