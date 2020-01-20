package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Field, Variable}
import viper.gobra.frontend.info.base.Type.{ArrayT, SliceT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Addressability extends BaseProperty { this: TypeInfoImpl =>

  lazy val effAddressable: Property[PExpression] = createBinaryProperty("effective addressable") {
    case _: PCompositeLit => true
    case e => addressable(e)
  }

  lazy val goEffAddressable: Property[PExpression] = createBinaryProperty("effective addressable") {
    case _: PCompositeLit => true
    case e => goAddressable(e)
  }

  // depends on: entity, tipe
  lazy val addressable: Property[PExpression] = createBinaryProperty("addressable") {
    case PNamedOperand(id) => addressableVar(id)
    case _: PDereference => true
    case PIndexedExp(b, _) => val bt = exprType(b); bt.isInstanceOf[SliceT] || (b.isInstanceOf[ArrayT] && addressable(b))
    case PSelection(b, id) => entity(id).isInstanceOf[Field] && goAddressable(b)
    //case PSelectionOrMethodExpr(b, id) => entity(id).isInstanceOf[Field]
    case _ => false
  }

  lazy val goAddressable: Property[PExpression] = createBinaryProperty("addressable") {
    case PNamedOperand(id) => entity(id).isInstanceOf[Variable]
    case _: PDereference => true
    case PIndexedExp(b, _) => val bt = exprType(b); bt.isInstanceOf[SliceT] || (b.isInstanceOf[ArrayT] && goAddressable(b))
    case PSelection(b, id) => entity(id).isInstanceOf[Field] && goAddressable(b)
    //case PSelectionOrMethodExpr(b, id) => entity(id).isInstanceOf[Field]
    case _ => false
  }

  private lazy val addressableVarAttr: PIdnNode => Boolean =
    attr[PIdnNode, Boolean] { n => regular(n) match {
      case v: Variable => v.addressable
      case _ => false
    }}

  override def addressableVar(id: PIdnNode): Boolean = addressableVarAttr(id)
}
