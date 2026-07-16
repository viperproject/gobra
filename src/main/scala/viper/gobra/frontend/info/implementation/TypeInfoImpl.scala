// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation

import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend._
import viper.gobra.frontend.PackageResolver.AbstractImport
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.frontend.info.base.SymbolTable.{Regular, TypeMember, UnknownEntity, lookup}
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.implementation.property._
import viper.gobra.frontend.info.implementation.resolution.{AmbiguityResolution, Enclosing, LabelResolution, MemberPath, MemberResolution, NameResolution}
import viper.gobra.frontend.info.implementation.typing._
import viper.gobra.frontend.info.implementation.typing.ghost._
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostSeparation
import viper.gobra.frontend.info.{ExternalTypeInfo, Info, TypeInfo}

class TypeInfoImpl(final val tree: Info.GoTree, override final val dependentTypeInfo: Map[AbstractImport, ExternalTypeInfo], val isMainContext: Boolean = false)(val config: Config) extends Attribution with TypeInfo with ExternalTypeInfo

  with NameResolution
  with LabelResolution
  with MemberResolution
  with AmbiguityResolution
  with Enclosing

  with ProgramTyping
  with ImportTyping
  with MemberTyping
  with BuiltInMemberTyping
  with StmtTyping
  with ExprTyping
  with TypeTyping
  with IdTyping
  with MiscTyping

  with GhostMemberTyping
  with GhostStmtTyping
  with GhostExprTyping
  with GhostTypeTyping
  with GhostIdTyping
  with GhostMiscTyping

  with GhostSeparation

  with Convertibility
  with Comparability
  with DependencyAnalysis
  with Assignability
  with Addressability
  with TypeIdentity
  with Orderability
  with PointsTo
  with Executability
  with ConstantEvaluation
  with Implements
  with UnderlyingType
  with TypeMerging

  with Errors
  with StrictLogging
{
  import viper.gobra.util.Violation._

  import org.bitbucket.inkytonik.kiama.attribution.Decorators
  protected val decorators = new Decorators(tree)

  override def pkgName: PPkgDef = tree.originalRoot.packageClause.id

  override def pkgInfo: PackageInfo = tree.originalRoot.info

  override def typ(expr: PExpression): Type.Type = exprType(expr)

  override def typOfExprOrType(expr: PExpressionOrType): Type.Type = exprOrTypeType(expr)

  override def typ(misc: PMisc): Type.Type = miscType(misc)

  override def symbType(typ: PType): Type.Type = typeSymbType(typ)

  override def typ(id: PIdnNode): Type.Type = idType(id)

  override def scope(n: PIdnNode): PScope = enclosingIdScope(n)

  override def codeRoot(n: PNode): PCodeRoot with PScope = enclosingCodeRoot(n)

  override def enclosingFunction(n: PNode): Option[PFunctionDecl] = tryEnclosingFunction(n)

  override def enclosingFunctionOrMethod(n: PNode): Option[PFunctionOrMethodDecl] = tryEnclosingFunctionOrMethod(n)

  override def enclosingLabeledLoopNode(label: PLabelUse, n: PNode) : Option[PGeneralForStmt] = enclosingLabeledLoop(label, n).toOption

  override def enclosingLoopNode(n: PNode) : Option[PGeneralForStmt] = enclosingLoopUntilOutline(n).toOption

  override def enclosingInvariantNode(n: PExpression) : PExpression = enclosingInvariant(n)

  override def samePkgDepsOfGlobalVar(n: SymbolTable.GlobalVariable): Vector[SymbolTable.GlobalVariable] =
    samePackageDependenciesGlobals(n) match {
      case Right(deps) => deps
      case Left(errs) => violation(s"found errors while computing dependencies of $n: $errs")
    }

  override def regular(n: PIdnNode): SymbolTable.Regular = entity(n) match {
    case r: Regular => r
    case _ => violation("found non-regular entity")
  }

  private var externallyAccessedMembers: Vector[PNode] = Vector()
  private def registerExternallyAccessedEntity(r: SymbolTable.Regular): SymbolTable.Regular = {
    if (!externallyAccessedMembers.contains(r.rep)) externallyAccessedMembers = externallyAccessedMembers :+ r.rep
    r
  }

  override def externalRegular(n: PIdnNode): Option[SymbolTable.Regular] = {
    // TODO restrict lookup to members starting with a capital letter
    lookup(topLevelEnvironment, n.name, UnknownEntity()) match {
      case r: Regular => Some(registerExternallyAccessedEntity(r))
      case _ => None
    }
  }

  override def tryAddressableMethodLikeLookup(typ: Type.Type, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])] = {
    val res = addressableMethodSet(typ).lookupWithPath(id.name)
    res.foreach { case (ml, _) => registerExternallyAccessedEntity(ml) }
    res
  }

  override def tryNonAddressableMethodLikeLookup(typ: Type.Type, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])] = {
    val res = nonAddressableMethodSet(typ).lookupWithPath(id.name)
    res.foreach { case (ml, _) => registerExternallyAccessedEntity(ml) }
    res
  }

  override def isUsed(m: PMember): Boolean = {
    externallyAccessedMembers.contains(m)
  }

  override def struct(n: PNode): Option[Type.StructT] =
    enclosingStruct(n).map(structDecl => symbType(structDecl).asInstanceOf[Type.StructT])

  override def boolConstantEvaluation(expr: PExpression): Option[Boolean] = boolConstantEval(expr)

  override def intConstantEvaluation(expr: PExpression): Option[BigInt] = intConstantEval(expr)

  override def permConstantEvaluation(expr: PExpression): Option[(BigInt, BigInt)] = permConstantEval(expr)

  override def stringConstantEvaluation(expr: PExpression): Option[String] = stringConstantEval(expr)

  override def getTypeInfo: TypeInfo = this

  override def isPureExpression(expr: PExpression): Boolean = isPureExpr(expr).isEmpty

  override def getTransitiveTypeInfos(includeThis: Boolean = true): Set[ExternalTypeInfo] = {
    val directTypeInfos = dependentTypeInfo.values.toSet
    // note that we call `getTransitiveTypeInfos` recursively with including the parameter in the results (which
    // corresponds to the parameter's default value)
    val dependentTypeInfos = directTypeInfos.flatMap(directTypeInfo => directTypeInfo.getTransitiveTypeInfos())
    if (includeThis) dependentTypeInfos + this
    else dependentTypeInfos
  }
}
