package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PEmbeddedDecl, PExpression, PFieldDecl, PIdnNode, PIdnUse, PKeyedElement, PMPredicateDecl, PMPredicateSig, PMember, PMethodDecl, PMethodSig, PMisc, PParameter, PPkgDef, PScope, PType}
import viper.gobra.frontend.info.base.Type.StructT
import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field, MPredicateImpl, MPredicateSpec, MethodImpl, MethodLike, MethodSpec, Regular}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.resolution.MemberPath

trait ExternalTypeInfo {

  def pkgName: PPkgDef

  /**
    * Gets called by the type checker to perform a symbol table lookup in an imported package
    */
  def externalRegular(n: PIdnNode): Option[Regular]

  /**
    * Gets called by the type checker to perform a method lookup for an addressable receiver in an imported package
    */
  def tryAddressableMethodLikeLookup(typ: Type, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])]

  /**
    * Gets called by the type checker to perform a method lookup for a non-addressable receiver in an imported package
    */
  def tryNonAddressableMethodLikeLookup(typ: Type, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])]

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

  def typ(typ: PType): Type

  def typ(typ: PIdnNode): Type

  def scope(n: PIdnNode): PScope

  def struct: PFieldDecl => Option[StructT]

  def boolConstantEvaluation(expr: PExpression): Option[Boolean]

  def intConstantEvaluation(expr: PExpression): Option[BigInt]

  def keyElementIndices(elems : Vector[PKeyedElement]) : Vector[BigInt]

  def getTypeInfo: TypeInfo
}
