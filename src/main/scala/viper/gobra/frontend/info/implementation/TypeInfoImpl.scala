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
    if (!externallyAccessedMembers.contains(r.rep)) {
      externallyAccessedMembers +:= r.rep
      intransitiveDependencies(r) match { case (regulars, extraNodes) =>
        regulars foreach registerExternallyAccessedEntity
        externallyAccessedMembers ++= extraNodes
      }
    }
    r
  }

  // TODO: maybe remove or improve?
  def registerImplProof(proof: PImplementationProof): Unit = {
    println(s"$proof")
    val res = memberUsages(proof)
    println(s"res: $res")
    res.foreach(registerExternallyAccessedEntity)
  }

  private def intransitiveDependencies(r: Regular): (Vector[Regular], Vector[PNode]) =
    r match {
      case f: st.Function =>
        if (f.decl.spec.isPure) {
          (memberUsages(f.decl), Vector())
        } else {
          // references to entities in the body are ignored
          val id = nodeUsages(f.decl.id)
          val args = f.decl.args flatMap nodeUsages
          val result = nodeUsages(f.decl.result)
          val spec = nodeUsages(f.decl.spec)
          (id ++ args ++ result ++ spec, Vector())
        }
      case c: st.SingleConstant =>
        val decl = enclosingPConstDecl(c.decl)
        (decl.toVector.flatMap(memberUsages), decl.toVector)
      case t: st.NamedType => (memberUsages(t.decl), Vector())
      case t: st.TypeAlias => (memberUsages(t.decl), Vector())
      case m: st.MethodImpl =>
        if (m.decl.spec.isPure) {
          (memberUsages(m.decl), Vector())
        } else {
          // references to entities in the body are ignored
          val id = nodeUsages(m.decl.id)
          val receiver = nodeUsages(m.decl.receiver)
          val args = m.decl.args flatMap nodeUsages
          val result = nodeUsages(m.decl.result)
          val spec = nodeUsages(m.decl.spec)
          (id ++ receiver ++ args ++ result ++ spec, Vector())
        }
      case m: st.MethodSpec =>
        val itf = nodeUsages(m.itfDef)
        val spec = nodeUsages(m.spec)
        (itf ++ spec, Vector(m.itfDef, m.spec))
      case p: st.FPredicate => (memberUsages(p.decl), Vector())
      case p: st.MPredicateImpl => (memberUsages(p.decl), Vector())
      case f: st.DomainFunction =>
        // if a domain function is referenced and there is a type declaration with
        // the domain type on the rhs, then it is imported
        (enclosingPTypeDecl(f.decl).toVector flatMap memberUsages, Vector())
      case _ => (Vector(), Vector())
    }

  // TODO: use correct contexts
  private def memberUsages(n: PMember): Vector[Regular] = nodeUsages(n)
  private def nodeUsages(n: PNode): Vector[Regular] = {
    val children = allChildren(n)
    val collectedNameOrDotChildren = children.collect {
      case n: PNameOrDot => resolve(n)
    }
    collectedNameOrDotChildren.collect{ case Some(s: Symbolic) => s.symb }
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
      case p: PImplementationProof =>
        val superT = underlyingType(symbType(p.superT)) match {
          case t: Type.InterfaceT => t
          case _ => ??? // violation
        }
        val subT = symbType(p.subT)
        val membersSub = memberSet(subT).collect{ case (_, m) => m }
        val membersSup = memberSet(superT).collect{ case (_, m) => m }
        membersSub.foreach(registerExternallyAccessedEntity)
        membersSup.foreach(registerExternallyAccessedEntity)
        true
      // TODO: undo this hack for domains
      case t: PTypeDecl =>
        t.right.isInstanceOf[PDomainType] ||  externallyAccessedMembers.contains(t)
      case _ => externallyAccessedMembers.contains(m)
    }
        // TODO: import domains - explain why

    // TODO: desugar impl proof when both impl and intf types are desugared
  }

  override def struct(n: PNode): Option[Type.StructT] =
    enclosingStruct(n).map(structDecl => symbType(structDecl).asInstanceOf[Type.StructT])

  override def boolConstantEvaluation(expr: PExpression): Option[Boolean] = boolConstantEval(expr)

  override def intConstantEvaluation(expr: PExpression): Option[BigInt] = intConstantEval(expr)

  override def stringConstantEvaluation(expr: PExpression): Option[String] = stringConstantEval(expr)

  override def getTypeInfo: TypeInfo = this
}
