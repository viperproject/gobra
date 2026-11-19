// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PAdtClause, PCodeRoot, PEmbeddedDecl, PExpression, PFieldDecl, PFunctionDecl, PFunctionOrMethodDecl, PGeneralForStmt, PIdnNode, PIdnUse, PInterfaceType, PKeyedElement, PLabelUse, PMPredicateDecl, PMPredicateSig, PMember, PMethodDecl, PMethodSig, PMisc, PNode, PParameter, PPkgDef, PScope, PType, PTypeDecl}
import viper.gobra.frontend.PackageInfo
import viper.gobra.frontend.PackageResolver.AbstractImport
import viper.gobra.frontend.info.base.BuiltInMemberTag.BuiltInMemberTag
import viper.gobra.frontend.info.base.Type.{AbstractType, AdtClauseT, DeclaredT, InterfaceT, StructT, Type}
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field, MPredicateImpl, MPredicateSpec, MethodImpl, MethodSpec, Regular, TypeMember}
import viper.gobra.frontend.info.implementation.resolution.{AdvancedMemberSet, MemberPath}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType
import viper.gobra.util.GoString

trait ExternalTypeInfo {

  def pkgName: PPkgDef
  def pkgInfo: PackageInfo
  def dependentTypeInfo: Map[AbstractImport, ExternalTypeInfo]

  /** returns this (unless disabled via parameter) and the type information of directly and transitively dependent packages */
  def getTransitiveTypeInfos(includeThis: Boolean = true): Set[ExternalTypeInfo]

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

  /** Returns the canonical declared type for a type declaration owned by this context.
    * Kiama's attribute caches are keyed on reference identity, thus, only this canonical instance
    * (as opposed to a freshly constructed, structurally equal one) benefits from caching.
    **/
  def declaredSymbType(decl: PTypeDecl): DeclaredT

  /** Returns the canonical symbolic type for an interface declaration owned by this context.
    * In contrast to [[symbType]], the result is not gated on well-definedness of the declaration
    * and can therefore be used from within well-definedness checks without creating attribute cycles.
    **/
  def interfaceSymbType(decl: PInterfaceType): InterfaceT

  /** Returns the canonical symbolic type for an ADT clause declaration owned by this context. */
  def adtClauseSymbType(decl: PAdtClause): AdtClauseT

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

  def stringConstantEvaluation(expr: PExpression): Option[GoString]

  def isPureExpression(expr: PExpression): Boolean

  def keyElementIndices(elems : Vector[PKeyedElement]) : Vector[BigInt]

  def getTypeInfo: TypeInfo

  /* memberset within a specific context */
  def localMemberSet(t: Type): AdvancedMemberSet[TypeMember]

  /** method set of interface `t`, whose declaration must belong to this context */
  def localInterfaceMethodSet(t: InterfaceT): AdvancedMemberSet[TypeMember]

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

  /** if it exists, it returns the function or method that contains n */
  def enclosingFunctionOrMethod(n: PNode): Option[PFunctionOrMethodDecl]

  /** if it exists, it returns the for loop node that contains 'n' with label 'label' */
  def enclosingLabeledLoopNode(label: PLabelUse, n: PNode) : Option[PGeneralForStmt]

  /** if it exists, it returns the for loop node that contains 'n' */
  def enclosingLoopNode(n: PNode) : Option[PGeneralForStmt]

  /** returns the enclosing invariant of 'n' */
  def enclosingInvariantNode(n: PExpression) : PExpression

  /** returns all global variables declared in the same package as 'n' on which the declaration of 'n' depends */
  def samePkgDepsOfGlobalVar(n: SymbolTable.GlobalVariable): Vector[SymbolTable.GlobalVariable]
}
