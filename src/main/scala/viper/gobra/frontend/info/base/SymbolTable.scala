// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util.{Entity, Environments}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.BuiltInMemberTag.{BuiltInFPredicateTag, BuiltInFunctionTag, BuiltInMPredicateTag, BuiltInMemberTag, BuiltInMethodTag, BuiltInPredicateTag, BuiltInTypeTag}


object SymbolTable extends Environments[Entity] {

  /**
    * An entity that represents an error situation. These entities are
    * usually accepted in most situations to avoid cascade errors.
    */
  abstract class ErrorEntity extends Entity {
    override def isError: Boolean = true
  }

  /**
    * A entity represented by names for whom we have seen more than one
    * declaration so we are unsure what is being represented.
    */
  case class MultipleEntity() extends ErrorEntity {
    override val toString: String = "multiple entities"
  }

  /**
    * An unknown entity, for example one that is represened by names whose
    * declarations are missing.
    */
  case class UnknownEntity() extends ErrorEntity {
    override val toString: String = "unknown"
  }

  /**
    * Special entity that provides an error message
    */
  case class ErrorMsgEntity(msg: Messages) extends ErrorEntity

  sealed trait Regular extends Entity with Product {
    def rep: PNode
    def ghost: Boolean
    def context: ExternalTypeInfo
    override def toString: String = rep.formattedShort
  }

  sealed trait ActualRegular extends Regular

  sealed trait DataEntity extends Regular

  sealed trait ActualDataEntity extends DataEntity with ActualRegular

  sealed trait WithArguments {
    def args: Vector[PParameter]
    def context: ExternalTypeInfo
  }

  sealed trait WithResult {
    def result: PResult
    def context: ExternalTypeInfo
  }

  case class Function(decl: PFunctionDecl, ghost: Boolean, context: ExternalTypeInfo) extends ActualDataEntity with WithArguments with WithResult {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
    override val result: PResult = decl.result
    def isPure: Boolean = decl.spec.isPure
    def isOpaque: Boolean = decl.spec.isOpaque
  }

  case class Closure(lit: PFunctionLit, ghost: Boolean, context: ExternalTypeInfo) extends ActualDataEntity with WithArguments with WithResult {
    override def rep: PNode = lit
    override val args: Vector[PParameter] = lit.args
    override val result: PResult = lit.decl.result
    def isPure: Boolean = lit.spec.isPure
  }

  sealed trait Constant extends DataEntity {
    def decl: PConstSpec
  }

  sealed trait ActualConstant extends Constant with ActualDataEntity

  case class SingleConstant(decl: PConstSpec, idDef: PIdnDef, exp: PExpression, opt: Option[PType], ghost: Boolean, context: ExternalTypeInfo) extends ActualConstant {
    override def rep: PNode = decl
  }

  sealed trait Variable extends DataEntity {
    def addressable: Boolean
  }

  sealed trait ActualVariable extends Variable with ActualDataEntity

  case class SingleLocalVariable(exp: Option[PExpression], opt: Option[PType], rep: PNode, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    require(exp.isDefined || opt.isDefined)
  }
  case class MultiLocalVariable(idx: Int, exp: PExpression, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = exp
  }

  case class GlobalVariable(decl: PVarDecl,
                            // index that the identifier of the var takes in the declaration
                            idx: Int,
                            expOpt: Option[PExpression],
                            typOpt: Option[PType],
                            ghost: Boolean,
                            override val addressable: Boolean,
                            isSingleModeDecl: Boolean,
                            context: ExternalTypeInfo
                           ) extends ActualVariable {
    require(expOpt.isDefined || typOpt.isDefined)
    require(0 <= idx && idx < decl.left.length)
    override def rep: PNode = decl
    def id: PDefLikeId = decl.left(idx)
  }

  case class Wildcard(decl: PWildcard, context: ExternalTypeInfo) extends ActualRegular with ActualDataEntity {
    override def rep: PNode = decl
    override def ghost: Boolean = false
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
    override def toString: String = decl.binder.fold("unknown")(_.toString)
  }
  case class RangeVariable(idx: Int, exp: PRange, ghost: Boolean, addressable: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def rep: PNode = exp
  }

  case class RangeEnumerateVariable(exp: PRange, ghost: Boolean, context: ExternalTypeInfo) extends ActualVariable {
    override def addressable: Boolean = false
    override def rep: PNode = exp
  }

  sealed trait TypeEntity extends Regular

  sealed trait ActualTypeEntity extends TypeEntity with ActualRegular {
    val decl: PTypeDecl
  }

  case class NamedType(decl: PTypeDef, ghost: Boolean, context: ExternalTypeInfo) extends ActualTypeEntity {
    override def rep: PNode = decl
  }
  case class TypeAlias(decl: PTypeAlias, ghost: Boolean, context: ExternalTypeInfo) extends ActualTypeEntity {
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
    def isAtomic: Boolean
  }

  case class MethodImpl(decl: PMethodDecl, ghost: Boolean, context: ExternalTypeInfo) extends Method {
    override def rep: PNode = decl
    override def isPure: Boolean = decl.spec.isPure
    override val args: Vector[PParameter] = decl.args
    override val result: PResult = decl.result
    override val isAtomic: Boolean = decl.spec.isAtomic
    def isOpaque: Boolean = decl.spec.isOpaque

  }

  case class MethodSpec(spec: PMethodSig, itfDef: PInterfaceType, ghost: Boolean, context: ExternalTypeInfo) extends Method {
    override def rep: PNode = spec
    override def isPure: Boolean = spec.spec.isPure
    override val args: Vector[PParameter] = spec.args
    override def result: PResult = spec.result
    override val isAtomic: Boolean = spec.spec.isAtomic
    val itfType: Type.InterfaceT = Type.InterfaceT(itfDef, context)
  }

  case class Import(decl: PImport, context: ExternalTypeInfo) extends ActualRegular with TypeEntity {
    override def rep: PNode = decl
    // TODO: requires checks that no actual entity from package is taken
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

  case class BoundVariable(decl: PBoundVariable, context: ExternalTypeInfo) extends GhostVariable {
    override def rep: PNode = decl
    override def addressable: Boolean = false
  }

  sealed trait GhostTypeMember extends TypeMember with GhostRegular

  sealed trait MPredicate extends MethodLike with GhostTypeMember with Predicate

  case class MPredicateImpl(decl: PMPredicateDecl, context: ExternalTypeInfo) extends MPredicate {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
  }

  case class MPredicateSpec(decl: PMPredicateSig, itfDef: PInterfaceType, context: ExternalTypeInfo) extends MPredicate {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
    val itfType: Type.InterfaceT = Type.InterfaceT(itfDef, context)
  }

  sealed trait GhostStructMember extends StructMember with GhostTypeMember

  case class DomainFunction(decl: PDomainFunction, domain: PDomainType, context: ExternalTypeInfo) extends GhostTypeMember with WithArguments with WithResult {
    override def rep: PNode = decl
    override val args: Vector[PParameter] = decl.args
    override val result: PResult = decl.result
  }

  case class MatchVariable(decl: PMatchBindVar, p: PNode, context: ExternalTypeInfo) extends GhostVariable {
    override def rep: PNode = decl

    override def addressable: Boolean = false
  }

  case class AdtClause(decl: PAdtClause, typeDecl: PTypeDef, context: ExternalTypeInfo) extends GhostTypeMember with TypeEntity {
    require(typeDecl.right.isInstanceOf[PAdtType])

    override def rep: PNode = decl

    val adtDecl: PAdtType = typeDecl.right.asInstanceOf[PAdtType]
    def getName: String = decl.id.name
    val fields: Vector[PFieldDecl] = decl.args.flatMap(f => f.fields)
  }

  sealed trait AdtMember extends GhostTypeMember {
    def getName: String
  }

  case class AdtDestructor(decl: PFieldDecl, typeDecl: PTypeDef, adtType: PAdtType, context: ExternalTypeInfo) extends AdtMember {
    override def rep: PNode = decl

    override def getName: String = decl.id.name
  }

  case class AdtDiscriminator(decl: PAdtClause, typeDecl: PTypeDef, adtType: PAdtType, context: ExternalTypeInfo) extends AdtMember {
    override def rep: PNode = decl

    override def getName: String = s"is${decl.id.name}"
  }

  /**
    * entities for built-in members
    */
  sealed trait BuiltInEntity extends Regular {
    def tag: BuiltInMemberTag
  }
  sealed trait BuiltInActualEntity extends BuiltInEntity with ActualRegular {
    override def ghost: Boolean = tag.ghost
  }

  sealed trait BuiltInMethodLike extends BuiltInEntity with TypeMember

  sealed trait BuiltInPredicate extends BuiltInEntity with GhostRegular {
    override def tag: BuiltInPredicateTag
  }

  case class BuiltInFunction(tag: BuiltInFunctionTag, rep: PNode, context: ExternalTypeInfo) extends BuiltInActualEntity {
    def isPure: Boolean = tag.isPure
  }
  case class BuiltInMethod(tag: BuiltInMethodTag, rep: PNode, context: ExternalTypeInfo) extends BuiltInActualEntity with BuiltInMethodLike with ActualTypeMember {
    def isPure: Boolean = tag.isPure
  }
  case class BuiltInFPredicate(tag: BuiltInFPredicateTag, rep: PNode, context: ExternalTypeInfo) extends BuiltInPredicate
  case class BuiltInMPredicate(tag: BuiltInMPredicateTag, rep: PNode, context: ExternalTypeInfo) extends BuiltInPredicate with BuiltInMethodLike with GhostTypeMember

  case class BuiltInType(tag: BuiltInTypeTag, rep: PNode, context: ExternalTypeInfo) extends BuiltInActualEntity with TypeEntity



  /**
    * Label
    */

  case class Label(decl: PLabeledStmt, ghost: Boolean) extends Entity with Product
  case object WandLhsLabel extends Entity
}
