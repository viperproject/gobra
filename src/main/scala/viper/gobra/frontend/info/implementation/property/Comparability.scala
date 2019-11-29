package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Comparability extends BaseProperty { this: TypeInfoImpl =>

  lazy val comparableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not comparable with $right"
  } {
    case (Single(left), Single(right)) =>
      assignableTo(left, right) && assignableTo(right, left) && ((left, right) match {
        case (l, r) if comparableType(l) && comparableType(r) => true
        case (NilType, r) if isPointerType(r) => true
        case (l, NilType) if isPointerType(l) => true
        case _ => false
      })
    case _ => false
  }

  lazy val comparableType: Property[Type] = createBinaryProperty("comparable") {
    case Single(st) => st match {
      case t: StructT =>
        structMemberSet(t).collect {
          case (_, f: Field) => typeType(f.decl.typ)
          case (_, e: Embbed) => miscType(e.decl.typ)
        }.forall(comparableType)

      case _: SliceT | _: MapT | _: FunctionT => false
      case _ => true
    }
    case _ => false
  }
}
