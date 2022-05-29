// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation

import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend.AstPattern.Symbolic
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.frontend.info.base.SymbolTable.{Regular, TypeMember, UnknownEntity, lookup}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.implementation.property._
import viper.gobra.frontend.info.implementation.resolution.{AmbiguityResolution, Enclosing, LabelResolution, MemberPath, MemberResolution, NameResolution}
import viper.gobra.frontend.info.implementation.typing._
import viper.gobra.frontend.info.implementation.typing.ghost._
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostSeparation
import viper.gobra.frontend.info.{ExternalTypeInfo, Info, TypeInfo}

class TypeInfoImpl(final val tree: Info.GoTree, final val context: Info.Context, val isMainContext: Boolean = false)(val config: Config) extends Attribution with TypeInfo with ExternalTypeInfo

  with NameResolution
  with LabelResolution
  with MemberResolution
  with AmbiguityResolution
  with Enclosing

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
  with Assignability
  with Addressability
  with TypeIdentity
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

  override def enclosingLabeledLoopNode(label: PLabelUse, n: PNode) : Option[PForStmt] = enclosingLabeledLoop(label, n)

  override def enclosingLoopNode(n: PNode) : Option[PForStmt] = enclosingLoop(n)

  override def regular(n: PIdnNode): SymbolTable.Regular = entity(n) match {
    case r: Regular => r
    case _ => violation("found non-regular entity")
  }

  private var externallyAccessedMembers: Vector[PNode] = Vector()
  private def registerExternallyAccessedEntity(r: SymbolTable.Regular): SymbolTable.Regular = {
    if (!visited.contains(r)) {
      visited :+= r
      val deps = getDepsExternalRegular(r)
      println(s"rep: ${r.rep}")
      println(s"deps: $deps")
      externallyAccessedMembers = r.rep +: externallyAccessedMembers
      externallyAccessedMembers = externallyAccessedMembers ++ deps.map(_.rep) // TODO: fail if these are not members?
      deps.foreach(registerExternallyAccessedEntity)
    }
    r
  }

  private var visited:Vector[Regular] = Vector()
  private var accum: Vector[Regular] = Vector()
  private def nonVisitedReachableRegulars(n: PNode): Vector[Regular] = {
    val children = allChildren(n)
    // println(s"AllChildren: $children")
    val collectedNameOrDotChildren = children.collect {
      case n: PNameOrDot => resolve(n)
    }
    // println(s"CollectedChildren: $collectedNameOrDotChildren")
    val collectedSymbolicChildren = collectedNameOrDotChildren.collect{ case Some(s: Symbolic) => s.symb }
    // println(s"CollectedSymbolic: $collectedSymbolicChildren")
    val res = collectedSymbolicChildren.filterNot(visited.contains(_))
    // println(s"res: $res")
    res
  }

  // TODO: cleanup to not return always the accumulated regulars
  private def getDepsExternalRegular(r: Regular): Vector[Regular] = {
    r match {
      case f: st.Function =>
        accum :+= f
        val nonVisited = nonVisitedReachableRegulars(f.decl)
        accum = f +: (accum ++ nonVisited)
        accum
      case c: st.SingleConstant =>
        val nonVisited = nonVisitedReachableRegulars(c.decl)
        accum = c +: (accum ++ nonVisited)
        accum
      case t: st.NamedType =>
        val nonVisited = nonVisitedReachableRegulars(t.decl)
        accum = t +: (accum ++ nonVisited)
        accum
      case t: st.TypeAlias =>
        val nonVisited = nonVisitedReachableRegulars(t.decl)
        accum = t +: (accum ++ nonVisited)
        accum
      case m: st.MethodSpec =>
        // references to entities in the body are ignored
        val nonVisited = nonVisitedReachableRegulars(m.spec)
        accum = m +: (accum ++ nonVisited)
        accum
      case m: st.MethodImpl =>
        // references to entities in the body are ignored
        val nonVisited = nonVisitedReachableRegulars(m.decl.spec)
        accum = m +: (accum ++ nonVisited)
        accum
      case p: st.FPredicate =>
        val nonVisited = nonVisitedReachableRegulars(p.decl)
        accum = p +: (accum ++ nonVisited)
        accum
      case p: st.MPredicateImpl =>
        val nonVisited = nonVisitedReachableRegulars(p.decl)
        accum = p +: (accum ++ nonVisited)
        accum
      case f: st.DomainFunction =>
        val nonVisited = nonVisitedReachableRegulars(f.decl)
        accum = f +: (accum ++ nonVisited)
        accum
      // TODO: check if there are more
      case _ => Vector()
    }
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
    m match {
      case t: PTypeDecl =>
        t.right.isInstanceOf[PDomainType] ||  externallyAccessedMembers.contains(t)
      case _ => externallyAccessedMembers.contains(m)
    }
        // TODO: import domains - explain why

    //TODO: desugar impl proof when both impl and intf types are desugared
  }

  override def struct(n: PNode): Option[Type.StructT] =
    enclosingStruct(n).map(structDecl => symbType(structDecl).asInstanceOf[Type.StructT])

  override def boolConstantEvaluation(expr: PExpression): Option[Boolean] = boolConstantEval(expr)

  override def intConstantEvaluation(expr: PExpression): Option[BigInt] = intConstantEval(expr)

  override def stringConstantEvaluation(expr: PExpression): Option[String] = stringConstantEval(expr)

  override def getTypeInfo: TypeInfo = this
}
