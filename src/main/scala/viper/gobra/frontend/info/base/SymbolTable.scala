package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product {
    def rep: PNode
  }

  sealed trait DataEntity extends Regular

  case class Function(decl: PFunctionDecl) extends DataEntity {
    override def rep: PNode = decl
  }

  sealed trait Constant extends DataEntity

  case class SingleConstant(exp: PExpression, opt: Option[PType]) extends Constant {
    override def rep: PNode = exp
  }
  case class MultiConstant(idx: Int, exp: PExpression) extends Constant {
    override def rep: PNode = exp
  }

  sealed trait Variable extends DataEntity

  case class SingleLocalVariable(exp: PExpression, opt: Option[PType]) extends Variable {
    override def rep: PNode = exp
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression) extends Variable {
    override def rep: PNode = exp
  }
  case class InParameter(decl: PNamedParameter) extends Variable {
    override def rep: PNode = decl
  }
  case class ReceiverParameter(decl: PNamedReceiver) extends Variable {
    override def rep: PNode = decl
  }
  case class OutParameter(decl: PNamedParameter) extends Variable {
    override def rep: PNode = decl
  }
  case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends Variable {
    override def rep: PNode = decl
  }
  case class RangeVariable(idx: Int, exp: PRange) extends Variable {
    override def rep: PNode = exp
  }


  sealed trait TypeEntity extends Regular

  case class NamedType(decl: PTypeDef) extends TypeEntity {
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias) extends TypeEntity {
    override def rep: PNode = decl
  }


  sealed trait TypeMember extends Regular

  case class Field(decl: PFieldDecl) extends TypeMember {
    override def rep: PNode = decl
  }
  case class Embbed(decl: PEmbeddedDecl) extends TypeMember {
    override def rep: PNode = decl
  }

  sealed trait Method extends TypeMember

  case class MethodImpl(decl: PMethodDecl) extends Method {
    override def rep: PNode = decl
  }
  case class MethodSpec(spec: PMethodSpec) extends Method {
    override def rep: PNode = spec
  }


  case class Package(decl: PQualifiedImport) extends Regular {
    override def rep: PNode = decl
  }

  case class Label(decl: PLabeledStmt) extends Regular {
    override def rep: PNode = decl
  }

}
