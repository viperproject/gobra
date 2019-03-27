package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._

object SymbolTable extends Environments {

  sealed trait Regular extends Entity with Product

  sealed trait DataEntity extends Regular

  case class Function(decl: PFunctionDecl) extends DataEntity

  sealed trait Constant extends DataEntity

  case class SingleConstant(exp: PExpression, opt: Option[PType]) extends Constant
  case class MultiConstant(idx: Int, exp: PExpression) extends Constant

  sealed trait Variable extends DataEntity

  case class SingleLocalVariable(exp: PExpression, opt: Option[PType]) extends Variable
  case class MultiLocalVariable(idx: Int, exp: PExpression) extends Variable
  case class InParameter(decl: PNamedParameter) extends Variable
  case class ReceiverParameter(decl: PNamedReceiver) extends Variable
  case class OutParameter(decl: PNamedParameter) extends Variable
  case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends Variable
  case class RangeVariable(idx: Int, exp: PRange) extends Variable


  sealed trait TypeEntity extends Regular

  case class NamedType(decl: PTypeDef) extends TypeEntity
  case class TypeAlias(decl: PTypeAlias) extends TypeEntity


  sealed trait TypeMember extends Regular

  case class Field(decl: PFieldDecl) extends TypeMember
  case class Embbed(decl: PEmbeddedDecl) extends TypeMember

  sealed trait Method extends TypeMember

  case class MethodImpl(decl: PMethodDecl) extends Method
  case class MethodSpec(spec: PMethodSpec) extends Method


  case class Package(decl: PQualifiedImport) extends Regular

  case class Label(decl: PLabeledStmt) extends Regular

  case class Wildcard() extends Regular

}
