package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.ExternalTypeInfo

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product {
    def rep: PNode
    def ghost: Boolean
    def context: ExternalTypeInfo
  }

  sealed trait ActualRegular extends Regular

  sealed trait DataEntity extends Regular

  sealed trait ActualDataEntity extends DataEntity with ActualRegular

  sealed trait WithArguments {
    def args: Vector[PParameter]
  }

  sealed trait WithResult {
    def result: PResult
  }

  case class Function(decl: PFunctionDecl, ghost: Boolean, context: ExternalTypeInfo) extends ActualDataEntity with WithArguments with WithResult {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
    override val result: PResult = decl.result
    def isPure: Boolean = decl.spec.isPure
  }

  sealed trait Constant extends DataEntity {
    def decl: PConstDecl
  }

  sealed trait ActualConstant extends Constant with ActualDataEntity

  case class SingleConstant(decl: PConstDecl, idDef: PIdnDef, exp: PExpression, opt: Option[PType], ghost: Boolean, context: ExternalTypeInfo) extends ActualConstant {
    override def rep: PNode = decl
  }

  sealed trait Variable extends DataEntity {
    def addressable: Boolean
  }

  sealed trait ActualVariable extends Variable with ActualDataEntity

  case class SingleLocalVariable(exp: Option[PExpression], opt: Option[PType], ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    require(exp.isDefined || opt.isDefined)
    override def rep: PNode = exp.getOrElse(opt.get)
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class InParameter(decl: PNamedParameter, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class ReceiverParameter(decl: PNamedReceiver, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class OutParameter(decl: PNamedParameter, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class TypeSwitchVariable(decl: PTypeSwitchStmt, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class RangeVariable(idx: Int, exp: PRange, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = exp
  }


  sealed trait TypeEntity extends Regular

  sealed trait ActualTypeEntity extends TypeEntity with ActualRegular {
    val decl: PTypeDecl
  }

  case class NamedType(decl: PTypeDef, ghost: Boolean, context: ExternalTypeInfo) extends ActualTypeEntity {
    require(!ghost, "type entities are not supported to be ghost yet") // TODO
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias, ghost: Boolean, context: ExternalTypeInfo) extends ActualTypeEntity {
    require(!ghost, "type entities are not supported to be ghost yet") // TODO
    override def rep: PNode = decl
  }


  sealed trait TypeMember extends Regular

  sealed trait ActualTypeMember extends TypeMember with ActualRegular

  sealed trait StructMember extends TypeMember

  sealed trait ActualStructMember extends StructMember with ActualTypeMember

  case class Field(decl: PFieldDecl, ghost: Boolean, context: ExternalTypeInfo) extends ActualStructMember {
    override def rep: PNode = decl
  }

  case class Embbed(decl: PEmbeddedDecl, ghost: Boolean, context: ExternalTypeInfo) extends ActualStructMember {
    override def rep: PNode = decl
  }

  sealed trait MethodLike extends TypeMember with WithArguments

  sealed trait Method extends MethodLike with ActualTypeMember with WithResult {
    def isPure: Boolean
  }

  case class MethodImpl(decl: PMethodDecl, ghost: Boolean, context: ExternalTypeInfo) extends Method {
    override def rep: PNode = decl
    override def isPure: Boolean = decl.spec.isPure
    override val args: Vector[PParameter] = decl.args
    override val result: PResult = decl.result
  }

  case class MethodSpec(spec: PMethodSig, ghost: Boolean, context: ExternalTypeInfo) extends Method {
    override def rep: PNode = spec
    override def isPure: Boolean = false // TODO: adapt later
    override val args: Vector[PParameter] = spec.args
    override def result: PResult = spec.result
  }

  case class Import(decl: PImport, context: ExternalTypeInfo) extends ActualRegular with TypeEntity {
    override def rep: PNode = decl
    // TODO: requires checks that no actual entity from package is taken
    override def ghost: Boolean = false
  }

  case class Label(decl: PLabeledStmt, context: ExternalTypeInfo) extends ActualRegular {
    override def rep: PNode = decl
    // TODO: requires check that label is not used in any goto (can still be used for old expressions)
    override def ghost: Boolean = false
  }

  case class Wildcard(decl: PWildcard, context: ExternalTypeInfo) extends ActualRegular {
    override def rep: PNode = decl
    override def ghost: Boolean = false
  }

  /**
    * Ghost
    */

  sealed trait GhostRegular extends Regular {
    override def ghost: Boolean = true
  }

  sealed trait GhostDataEntity extends DataEntity with GhostRegular

  sealed trait Predicate extends GhostDataEntity with WithArguments

  case class FPredicate(decl: PFPredicateDecl, context: ExternalTypeInfo) extends Predicate {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
  }

  sealed trait GhostConstant extends Constant with GhostDataEntity

  sealed trait GhostVariable extends Variable with GhostDataEntity

  sealed trait GhostTypeMember extends TypeMember with GhostRegular

  sealed trait MPredicate extends MethodLike with GhostTypeMember with Predicate

  case class MPredicateImpl(decl: PMPredicateDecl, context: ExternalTypeInfo) extends MPredicate {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
  }

  case class MPredicateSpec(decl: PMPredicateSig, context: ExternalTypeInfo) extends MPredicate {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
  }

  sealed trait GhostStructMember extends StructMember with GhostTypeMember

}
