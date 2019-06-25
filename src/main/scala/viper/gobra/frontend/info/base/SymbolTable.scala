package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product {
    def rep: PNode
    def isGhost: Boolean
  }

  sealed trait ActualRegular extends Regular {
    override def isGhost: Boolean = false
  }
  sealed trait DataEntity extends Regular

  sealed trait ActualDataEntity extends DataEntity with ActualRegular

  sealed trait Function extends DataEntity

  case class ActualFunction(decl: PFunctionDecl) extends Function with ActualDataEntity {
    override def rep: PNode = decl
  }

  sealed trait Constant extends DataEntity

  sealed trait ActualConstant extends Constant with ActualRegular

  case class SingleConstant(exp: PExpression, opt: Option[PType]) extends ActualConstant {
    override def rep: PNode = exp
  }

  case class MultiConstant(idx: Int, exp: PExpression) extends ActualConstant {
    override def rep: PNode = exp
  }

  sealed trait Variable extends DataEntity

  sealed trait ActualVariable extends Variable with ActualDataEntity

  case class SingleLocalVariable(exp: PExpression, opt: Option[PType]) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression) extends ActualVariable {
    override def rep: PNode = exp
  }
  case class InParameter(decl: PNamedParameter) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class ReceiverParameter(decl: PNamedReceiver) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class OutParameter(decl: PNamedParameter) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends ActualVariable {
    override def rep: PNode = decl
  }
  case class RangeVariable(idx: Int, exp: PRange) extends ActualVariable {
    override def rep: PNode = exp
  }


  sealed trait TypeEntity extends Regular

  sealed trait ActualTypeEntity extends TypeEntity with ActualRegular

  case class NamedType(decl: PTypeDef) extends ActualTypeEntity {
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias) extends ActualTypeEntity {
    override def rep: PNode = decl
  }


  sealed trait TypeMember extends Regular

  sealed trait ActualTypeMember extends TypeMember with ActualRegular

  sealed trait StructMember extends TypeMember

  sealed trait ActualStructMember extends StructMember with ActualTypeMember

  sealed trait Field extends StructMember {
    def decl: PFieldDecl
  }

  case class ActualField(decl: PFieldDecl) extends Field with ActualStructMember {
    override def rep: PNode = decl
  }

  sealed trait Embbed extends StructMember {
    def decl: PEmbeddedDecl
  }

  case class ActualEmbbed(decl: PEmbeddedDecl) extends Embbed with ActualStructMember {
    override def rep: PNode = decl
  }

  sealed trait Method extends TypeMember

  sealed trait ActualMethod extends Method with ActualTypeMember

  sealed trait MethodImpl extends Method {
    def decl: PMethodDecl
  }

  case class ActualMethodImpl(decl: PMethodDecl) extends MethodImpl with ActualMethod {
    override def rep: PNode = decl
  }

  sealed trait MethodSpec extends Method {
    def spec: PMethodSig
  }

  case class ActualMethodSpec(spec: PMethodSig) extends MethodSpec with ActualMethod {
    override def rep: PNode = spec
  }


  case class Package(decl: PQualifiedImport) extends ActualRegular {
    override def rep: PNode = decl
  }

  case class Label(decl: PLabeledStmt) extends ActualRegular {
    override def rep: PNode = decl
  }

  /**
    * Ghost
    */

  sealed trait GhostRegular extends Regular {
    override def isGhost: Boolean = true
  }

  sealed trait GhostifiedEntity[T <: ActualRegular] extends GhostRegular {
    def actual: T
    override def rep: PNode = actual.rep
  }

  object GhostifiedEntity {
    def unapply[T <: ActualRegular](arg: GhostifiedEntity[T]): Option[T] = Some(arg.actual)
  }

  object NoGhost {
    def unapply(arg: Entity): Option[Entity] = arg match {
      case GhostifiedEntity(x) => Some(x)
      case x => Some(x)
    }
  }

  sealed trait GhostDataEntity extends DataEntity with GhostRegular

  case class GhostFunction(actual: ActualFunction) extends Function with GhostDataEntity with GhostifiedEntity[ActualFunction]

  case class GhostConstant(actual: ActualConstant) extends Constant with GhostDataEntity with GhostifiedEntity[ActualConstant]

  case class GhostVariable(actual: ActualVariable) extends Variable with GhostDataEntity with GhostifiedEntity[ActualVariable]

  case class GhostTypeEntity(actual: ActualTypeEntity) extends TypeEntity with GhostRegular with GhostifiedEntity[ActualTypeEntity]

  sealed trait GhostTypeMember extends TypeMember with GhostRegular

  sealed trait GhostStructMember extends StructMember with GhostTypeMember

  case class GhostField(actual: ActualField) extends Field with GhostStructMember with GhostifiedEntity[ActualField] {
    override def decl: PFieldDecl = actual.decl
  }

  case class GhostEmbbed(actual: ActualEmbbed) extends Embbed with GhostStructMember with GhostifiedEntity[ActualEmbbed] {
    override def decl: PEmbeddedDecl = actual.decl
  }

  sealed trait GhostMethod extends Method with GhostTypeMember

  case class GhostMethodImpl(actual: ActualMethodImpl) extends MethodImpl with GhostMethod with GhostifiedEntity[ActualMethodImpl] {
    override def decl: PMethodDecl = actual.decl
  }

  case class GhostMethodSpec(actual: ActualMethodSpec) extends MethodSpec with GhostMethod with GhostifiedEntity[ActualMethodSpec] {
    override def spec: PMethodSig = actual.spec
  }

  case class GhostPackage(actual: Package) extends GhostRegular with GhostifiedEntity[Package]
  // TODO: requires checks that no actual entity from package is taken

  case class GhostLabel(actual: Label) extends GhostRegular with GhostifiedEntity[Label]
  // TODO: requires check that label is not used in any goto (can still be used for old expressions)

  def ghostify(r: ActualRegular): GhostRegular = r match {
    case a: ActualFunction => GhostFunction(a)
    case a: ActualConstant => GhostConstant(a)
    case a: ActualVariable => GhostVariable(a)
    case a: ActualTypeEntity => GhostTypeEntity(a)
    case a: ActualField => GhostField(a)
    case a: ActualEmbbed => GhostEmbbed(a)
    case a: ActualMethodImpl => GhostMethodImpl(a)
    case a: ActualMethodSpec => GhostMethodSpec(a)
    case a: Package => GhostPackage(a)
    case a: Label   => GhostLabel(a)
  }

}
