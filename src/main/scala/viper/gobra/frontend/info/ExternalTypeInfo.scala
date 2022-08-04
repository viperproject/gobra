// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PCodeRoot, PEmbeddedDecl, PExpression, PFieldDecl, PForStmt, PFunctionDecl, PIdnNode, PIdnUse, PKeyedElement, PLabelUse, PMPredicateDecl, PMPredicateSig, PMember, PMethodDecl, PMethodSig, PMisc, PNode, PParameter, PPkgDef, PScope, PType}
import viper.gobra.frontend.PackageInfo
import viper.gobra.frontend.info.base.BuiltInMemberTag.BuiltInMemberTag
import viper.gobra.frontend.info.base.Type.{AbstractType, InterfaceT, StructT, Type}
import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field, MPredicateImpl, MPredicateSpec, MethodImpl, MethodSpec, Regular, TypeMember}
import viper.gobra.frontend.info.implementation.resolution.{AdvancedMemberSet, MemberPath}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType

trait ExternalTypeInfo {

  def pkgName: PPkgDef
  def pkgInfo: PackageInfo

  /**
    * Gets called by the type checker to perform a symbol table lookup in an imported package
    */
  def externalRegular(n: PIdnNode): Option[Regular]

  /**
    * Gets called by the type checker to perform a method lookup for an addressable receiver in an imported package
    */
  def tryAddressableMethodLikeLookup(typ: Type, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])]

  /**
    * Gets called by the type checker to perform a method lookup for a non-addressable receiver in an imported package
    */
  def tryNonAddressableMethodLikeLookup(typ: Type, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])]

  def isParamGhost(param: PParameter): Boolean

  /**
    * Returns true if a symbol table lookup was made through `externalRegular` for the given member
    */
  def isUsed(m: PMember): Boolean

  def createField(decl: PFieldDecl): Field

  def createEmbbed(decl: PEmbeddedDecl): Embbed

  def createMethodImpl(decl: PMethodDecl): MethodImpl

  def createMethodSpec(spec: PMethodSig): MethodSpec

  def createMPredImpl(decl: PMPredicateDecl): MPredicateImpl

  def createMPredSpec(spec: PMPredicateSig): MPredicateSpec

  def typ(misc: PMisc): Type

  def symbType(typ: PType): Type

  def typ(typ: PIdnNode): Type

  def typ(expr: PExpression): Type

  def typ(expr: BuiltInMemberTag): AbstractType

  def argGhostTyping(tag: BuiltInMemberTag, args: Vector[Type]): GhostType

  def returnGhostTyping(tag: BuiltInMemberTag, args: Vector[Type]): GhostType

  def scope(n: PIdnNode): PScope

  def struct(n: PNode): Option[StructT]

  def boolConstantEvaluation(expr: PExpression): Option[Boolean]

  def intConstantEvaluation(expr: PExpression): Option[BigInt]

  def permConstantEvaluation(expr: PExpression): Option[(BigInt, BigInt)]

  def stringConstantEvaluation(expr: PExpression): Option[String]

  def isPureExpression(expr: PExpression): Boolean

  def keyElementIndices(elems : Vector[PKeyedElement]) : Vector[BigInt]

  def getTypeInfo: TypeInfo

  /* memberset within a specific context */
  def localMemberSet(t: Type): AdvancedMemberSet[TypeMember]

  /** returns all subtype relation found in the current package */
  def localRequiredImplements: Set[(Type, InterfaceT)]

  /** returns all subtype relation guaranteed in the current package */
  def localGuaranteedImplements: Set[(Type, InterfaceT)]

  /** returns all implementation proofs found in the current package */
  def localImplementationProofs: Vector[(Type, InterfaceT, Vector[String], Vector[String])]

  /** returns the code root for a given node; can only be called on nodes that are enclosed in a code root */
  def codeRoot(n: PNode): PCodeRoot with PScope

  /** if it exists, it returns the function that contains n */
  def enclosingFunction(n: PNode): Option[PFunctionDecl]

  /** if it exists, it returns the for loop node that contains 'n' with label 'label' */
  def enclosingLabeledLoopNode(label: PLabelUse, n: PNode) : Option[PForStmt]

  /** if it exists, it returns the for loop node that contains 'n' */
  def enclosingLoopNode(n: PNode) : Option[PForStmt]
}
