package viper.gobra.frontend.info

import viper.gobra.ast.frontend.{PEmbeddedDecl, PExpression, PFieldDecl, PIdnNode, PMPredicateDecl, PMPredicateSig, PMember, PMethodDecl, PMethodSig, PMisc, PNode, PPkgDef, PScope, PType}
import viper.gobra.frontend.info.base.Type.StructT
import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field, MPredicateImpl, MPredicateSpec, MethodImpl, MethodSpec, Regular}
import viper.gobra.frontend.info.base.Type.Type

trait ExternalTypeInfo {

  def pkgName: PPkgDef

  /**
    * Gets called by the type checker to perform a symbol table lookup in an imported package
    */
  def externalRegular(n: PIdnNode): Option[Regular]

  /**
    * Returns true if a symbol table lookup was made through `externalRegular` for the given member
    */
  def isUsed(m: PMember): Boolean

  def regular(n: PIdnNode): Regular

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

  def struct: PNode => Option[StructT]

  def boolConstantEvaluation(expr: PExpression): Option[Boolean]

  def intConstantEvaluation(expr: PExpression): Option[BigInt]
}
