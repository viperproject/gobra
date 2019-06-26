package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product {
    def rep: PNode
    def isGhost: Boolean
  }

  sealed trait ActualRegular extends Regular

  sealed trait DataEntity extends Regular

  sealed trait ActualDataEntity extends DataEntity with ActualRegular

  case class Function(decl: PFunctionDecl, isGhost: Boolean) extends ActualDataEntity {
    override def rep: PNode = decl
  }

  sealed trait Constant extends DataEntity

  sealed trait ActualConstant extends Constant with ActualDataEntity

  case class SingleConstant(exp: PExpression, opt: Option[PType], isGhost: Boolean) extends ActualConstant {
    override def rep: PNode = exp
  }

  case class MultiConstant(idx: Int, exp: PExpression, isGhost: Boolean) extends ActualConstant {
    override def rep: PNode = exp
  }

  sealed trait Variable extends DataEntity

  sealed trait ActualVariable extends Variable with ActualDataEntity

  case class SingleLocalVariable(exp: PExpression, opt: Option[PType], isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class InParameter(decl: PNamedParameter, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class ReceiverParameter(decl: PNamedReceiver, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class OutParameter(decl: PNamedParameter, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class TypeSwitchVariable(decl: PTypeSwitchStmt, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class RangeVariable(idx: Int, exp: PRange, isGhost: Boolean) extends ActualVariable {
    override def rep: PNode = exp
  }


  sealed trait TypeEntity extends Regular

  sealed trait ActualTypeEntity extends TypeEntity with ActualRegular

  case class NamedType(decl: PTypeDef, isGhost: Boolean) extends ActualTypeEntity {
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias, isGhost: Boolean) extends ActualTypeEntity {
    override def rep: PNode = decl
  }


  sealed trait TypeMember extends Regular

  sealed trait ActualTypeMember extends TypeMember with ActualRegular

  sealed trait StructMember extends TypeMember

  sealed trait ActualStructMember extends StructMember with ActualTypeMember

  case class Field(decl: PFieldDecl, isGhost: Boolean) extends ActualStructMember {
    override def rep: PNode = decl
  }

  case class Embbed(decl: PEmbeddedDecl, isGhost: Boolean) extends ActualStructMember {
    override def rep: PNode = decl
  }

  sealed trait Method extends ActualTypeMember

  case class MethodImpl(decl: PMethodDecl, isGhost: Boolean) extends Method {
    override def rep: PNode = decl
  }

  case class MethodSpec(spec: PMethodSig, isGhost: Boolean) extends Method {
    override def rep: PNode = spec
  }

  case class Package(decl: PQualifiedImport) extends ActualRegular {
    override def rep: PNode = decl
    // TODO: requires checks that no actual entity from package is taken
    override def isGhost: Boolean = false
  }

  case class Label(decl: PLabeledStmt) extends ActualRegular {
    override def rep: PNode = decl
    // TODO: requires check that label is not used in any goto (can still be used for old expressions)
    override def isGhost: Boolean = false
  }

  /**
    * Ghost
    */

  sealed trait GhostRegular extends Regular {
    override def isGhost: Boolean = true
  }

  sealed trait GhostDataEntity extends DataEntity with GhostRegular

  sealed trait GhostConstant extends Constant with GhostDataEntity

  sealed trait GhostVariable extends Variable with GhostDataEntity

  sealed trait GhostTypeMember extends TypeMember with GhostRegular

  sealed trait GhostStructMember extends StructMember with GhostTypeMember

}
