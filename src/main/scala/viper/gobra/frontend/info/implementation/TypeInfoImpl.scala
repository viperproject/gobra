// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation

import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.base.SymbolTable.{Regular, TypeMember, UnknownEntity, lookup}
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

  override def typ(expr: PExpression): Type.Type = exprType(expr)

  override def typOfExprOrType(expr: PExpressionOrType): Type.Type = exprOrTypeType(expr)

  override def typ(misc: PMisc): Type.Type = miscType(misc)

  override def symbType(typ: PType): Type.Type = typeSymbType(typ)

  override def typ(id: PIdnNode): Type.Type = idType(id)

  override def scope(n: PIdnNode): PScope = enclosingIdScope(n)

  override def codeRoot(n: PNode): PScope = enclosingCodeRoot(n)

  override def regular(n: PIdnNode): SymbolTable.Regular = entity(n) match {
    case r: Regular => r
    case _ => violation("found non-regular entity")
  }

  private var externallyAccessedMembers: Vector[PNode] = Vector()
  private def registerExternallyAccessedEntity(r: SymbolTable.Regular): SymbolTable.Regular = {
    if (!externallyAccessedMembers.contains(r.rep)) {
      externallyAccessedMembers = externallyAccessedMembers :+ r.rep
      addTransitiveDeps(r)
    }
    r
  }

  private def addTransitiveDeps(r: SymbolTable.Regular): Unit = {
    def checkedNodesFromFuncSpec(spec: PFunctionSpec): Vector[PNode] =
      spec.pres ++ spec.posts ++ (spec.pres ++ spec.posts).flatMap(_.subNodes)
        // TODO: check why this causes node not in tree exception
        // (try { (spec.pres ++ spec.posts) flatMap allChildren } catch { case e => Seq.empty })

    def handleNodes(checkedNodes: Vector[PNode]): Unit = {
      println(s"Before Exploding: $checkedNodes")
      checkedNodes.foreach {
        case n: PExpressionOrType => resolve(n) match {
          case Some(ap.PredicateCall(p: ap.Symbolic, _)) if !p.isInstanceOf[ap.BuiltInPredicateKind] =>
            registerExternallyAccessedEntity(p.symb)
          case Some(pred: ap.PredicateKind with ap.Symbolic) if !pred.isInstanceOf[ap.BuiltInPredicateKind] =>
            registerExternallyAccessedEntity(pred.symb)
          case Some(ap.FunctionCall(f: ap.Symbolic, _)) if !f.isInstanceOf[ap.BuiltInFunctionKind] =>
            registerExternallyAccessedEntity(f.symb)
          case Some(func: ap.FunctionKind with ap.Symbolic) if !func.isInstanceOf[ap.BuiltInFunctionKind] =>
            registerExternallyAccessedEntity(func.symb)
          case Some(const: ap.Constant) =>
            // assumes that all constants are global. True for now but may change in the future
            registerExternallyAccessedEntity(const.symb)
          case _ =>
        }
        case _ =>
      }
    }

    r match {
      case SymbolTable.Function(decl, _, _) =>
        handleNodes(checkedNodesFromFuncSpec(decl.spec))
      case SymbolTable.MethodSpec(sig, _, _, _) =>
        handleNodes(checkedNodesFromFuncSpec(sig.spec))
      case SymbolTable.MethodImpl(decl, _, _) =>
        handleNodes(checkedNodesFromFuncSpec(decl.spec))
      case SymbolTable.FPredicate(decl, _) =>
        decl.body.foreach { body => handleNodes(body +: allChildren(body)) }
      case SymbolTable.MPredicateImpl(decl, _) =>
        decl.body.foreach { body => handleNodes(body +: allChildren(body)) }
      case _ =>
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
    externallyAccessedMembers.contains(m)
  }

  lazy val struct: PNode => Option[Type.StructT] =
    // lookup PStructType based on PFieldDecl and get then StructT
    attr[PNode, Option[Type.StructT]] {

      case tree.parent.pair(_: PFieldDecl, decls: PFieldDecls) =>
        struct(decls)

      case tree.parent.pair(_: PFieldDecls, structDecl: PStructType) =>
        Some(symbType(structDecl).asInstanceOf[Type.StructT])

      case _ => None
    }

  override def boolConstantEvaluation(expr: PExpression): Option[Boolean] = boolConstantEval(expr)

  override def intConstantEvaluation(expr: PExpression): Option[BigInt] = intConstantEval(expr)

  override def stringConstantEvaluation(expr: PExpression): Option[String] = stringConstantEval(expr)

  override def getTypeInfo: TypeInfo = this
}
