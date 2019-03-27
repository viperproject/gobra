package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{DeclaredT, PointerT, Single, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Convertibility extends BaseProperty { this: TypeInfoImpl =>

  // TODO: check where convertibility and where assignability is required.

  lazy val convertibleTo: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not convertible to $right"
  } {
    case (Single(lst), Single(rst)) => (lst, rst) match {
      case (left, right) if assignableTo(left, right) => true
      case (left, right) => (underlyingType(left), underlyingType(right)) match {
        case (l, r) if identicalTypes(l, r) => true
        case (PointerT(l), PointerT(r)) if identicalTypes(underlyingType(l), underlyingType(r)) &&
          !(left.isInstanceOf[DeclaredT] && right.isInstanceOf[DeclaredT]) => true
        case _ => false
      }
    }
    case _ => false
  }
}
