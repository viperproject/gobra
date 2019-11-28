package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product {
    def rep: PNode
    def ghost: Boolean
  }

  sealed trait ActualRegular extends Regular

  sealed trait DataEntity extends Regular

  sealed trait ActualDataEntity extends DataEntity with ActualRegular

  case class Function(decl: PFunctionDecl, ghost: Boolean) extends ActualDataEntity {
    override def rep: PNode = decl
    def isPure: Boolean = decl.spec.isPure
  }

  sealed trait Constant extends DataEntity

  sealed trait ActualConstant extends Constant with ActualDataEntity

  case class SingleConstant(exp: PExpression, opt: Option[PType], ghost: Boolean) extends ActualConstant {
    override def rep: PNode = exp
  }

  case class MultiConstant(idx: Int, exp: PExpression, ghost: Boolean) extends ActualConstant {
    override def rep: PNode = exp
  }

  sealed trait Variable extends DataEntity {
    def addressable: Boolean
  }

  sealed trait ActualVariable extends Variable with ActualDataEntity

  case class SingleLocalVariable(exp: Option[PExpression], opt: Option[PType], ghost: Boolean, addressable: Boolean) extends ActualVariable {
    require(exp.isDefined || opt.isDefined)
    override def rep: PNode = exp.getOrElse(opt.get)
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class InParameter(decl: PNamedParameter, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class ReceiverParameter(decl: PNamedReceiver, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class OutParameter(decl: PNamedParameter, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class TypeSwitchVariable(decl: PTypeSwitchStmt, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class RangeVariable(idx: Int, exp: PRange, ghost: Boolean, addressable: Boolean) extends ActualVariable {
    override def rep: PNode = exp
  }


  sealed trait TypeEntity extends Regular

  sealed trait ActualTypeEntity extends TypeEntity with ActualRegular

  case class NamedType(decl: PTypeDef, ghost: Boolean) extends ActualTypeEntity {
    require(!ghost, "type entities are not supported to be ghost yet") // TODO
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias, ghost: Boolean) extends ActualTypeEntity {
    require(!ghost, "type entities are not supported to be ghost yet") // TODO
    override def rep: PNode = decl
  }


  sealed trait TypeMember extends Regular

  sealed trait ActualTypeMember extends TypeMember with ActualRegular

  sealed trait StructMember extends TypeMember

  sealed trait ActualStructMember extends StructMember with ActualTypeMember

  case class Field(decl: PFieldDecl, ghost: Boolean) extends ActualStructMember {
    override def rep: PNode = decl
  }

  case class Embbed(decl: PEmbeddedDecl, ghost: Boolean) extends ActualStructMember {
    override def rep: PNode = decl
  }

  sealed trait MethodLike extends TypeMember

  sealed trait Method extends MethodLike with ActualTypeMember {
    def isPure: Boolean
    def result: PResult
  }

  case class MethodImpl(decl: PMethodDecl, ghost: Boolean) extends Method {
    override def rep: PNode = decl
    override def isPure: Boolean = decl.spec.isPure
    override def result: PResult = decl.result
  }

  case class MethodSpec(spec: PMethodSig, ghost: Boolean) extends Method {
    override def rep: PNode = spec
    override def isPure: Boolean = false // TODO: adapt later
    override def result: PResult = spec.result
  }

  case class Package(decl: PQualifiedImport) extends ActualRegular {
    override def rep: PNode = decl
    // TODO: requires checks that no actual entity from package is taken
    override def ghost: Boolean = false
  }

  case class Label(decl: PLabeledStmt) extends ActualRegular {
    override def rep: PNode = decl
    // TODO: requires check that label is not used in any goto (can still be used for old expressions)
    override def ghost: Boolean = false
  }

  /**
    * Ghost
    */

  sealed trait GhostRegular extends Regular {
    override def ghost: Boolean = true
  }

  sealed trait GhostDataEntity extends DataEntity with GhostRegular

  sealed trait Predicate extends GhostDataEntity

  case class FPredicate(decl: PFPredicateDecl) extends Predicate {
    override def rep: PNode = decl
  }

  sealed trait GhostConstant extends Constant with GhostDataEntity

  sealed trait GhostVariable extends Variable with GhostDataEntity

  sealed trait GhostTypeMember extends TypeMember with GhostRegular

  sealed trait MPredicate extends MethodLike with GhostTypeMember with Predicate

  case class MPredicateImpl(decl: PMPredicateDecl) extends MPredicate {
    override def rep: PNode = decl
  }

  case class MPredicateSpec(decl: PMPredicateSig) extends MPredicate {
    override def rep: PNode = decl
  }

  sealed trait GhostStructMember extends StructMember with GhostTypeMember

}
