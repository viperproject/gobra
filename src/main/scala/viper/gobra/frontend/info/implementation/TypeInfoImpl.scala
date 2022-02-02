// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation

import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend._
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

  def registerImplProof(n: PImplementationProof): PImplementationProof = {
    if (!externallyAccessedMembers.contains(n)) {
      externallyAccessedMembers = externallyAccessedMembers :+ (n)
    }
    n
  }

  private var externallyAccessedMembers: Vector[PNode] = Vector()
  // TODO: make this public instead of checking
  private def registerExternallyAccessedEntity(r: SymbolTable.Regular): SymbolTable.Regular = {
    if (!externallyAccessedMembers.contains(r.rep)) {
      externallyAccessedMembers = externallyAccessedMembers :+ r.rep
      r.context match {
        case ctx: TypeInfoImpl =>
          ctx.relevantSubnodes(r.rep).flatMap(allChildren).foreach {
            case n: PExpressionOrType => ctx.resolve(n) match {
              case Some(s: AstPattern.Symbolic) => s.symb.context match {
                case ctx2: TypeInfoImpl => ctx2.registerExternallyAccessedEntity(s.symb)
              }
              case _ =>
            }
            case _ =>
          }
      }
    }
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

  // TODO: computes every node that might contain a visible invocation to the importing package
  private def relevantSubnodes(n: PNode): Vector[PNode] = n match {
    // predicates, methods, pure methods
    case decl@ PFunctionDecl(id, args, result, spec, _) =>
      // if (decl.spec.isPure) tree.child(decl) else id +: result +: spec +: args
      tree.child(decl)
    case decl: PDomainFunction => tree.child(decl)
    case sig:  PMethodSig => tree.child(sig)
    case decl@ PMethodDecl(id, receiver, args, result, spec, _) =>
      // Isn't this equivalent to tree.child(decl) because the parser will throw away the body in case of an imported non-pure function & method?
      // if (decl.spec.isPure) tree.child(decl) else id +: receiver +: result +: spec +: args
      tree.child(decl)
    case decl: PMPredicateDecl => tree.child(decl)
    case decl: PFPredicateDecl => tree.child(decl)
    case sig:  PMPredicateSig => tree.child(sig)
    case impl: PMethodImplementationProof =>
      // tree.child(impl)
      println("Here")
      ???
    case alias: PImplementationProofPredicateAlias =>
      // tree.child(impl)
      println("Here")
      ???
      //tree.child(alias)
    case decl: PTypeDecl =>
      println("Hello")
      tree.child(decl.right)
    case n => Vector()
  }

  override def isUsed(m: PMember): Boolean = {
    externallyAccessedMembers.contains(m)
  }

  // private var _visitedInterfaces = ???
  // TODO: pick better visibility
  override def wellDefGhostMember(member: PGhostMember): Messages = {
    member match {
      case n: PImplementationProof => registerImplProof(n)
      case _ =>
    }
    super.wellDefGhostMember(member)
  }

  override def syntaxImplements(l: Type.Type, r: Type.Type): PropertyResult = {
    println("Implements")
    val result = super.syntaxImplements(l, r)
    result match {
      case PropertyResult(None) =>
        // TODO
        // right now, every interface implementation and corresponding methods are added.
        // we can make this better in the future, but it requires re-implementing detection of inference of
        // implementation proofs at the concrete syntax level
        println(s"l: $l, r: $r")
        val interfaceMethodNames = memberSet(r).toMap.keys
        println(s"interface names: $interfaceMethodNames")
        val correspondingTypeMethods = interfaceMethodNames.flatMap(memberSet(l).lookup(_))
        println(s"interface methods: $correspondingTypeMethods")
        correspondingTypeMethods.foreach(registerExternallyAccessedEntity)
      case PropertyResult(_) =>
    }
    result
  }

  override def struct(n: PNode): Option[Type.StructT] =
    enclosingStruct(n).map(structDecl => symbType(structDecl).asInstanceOf[Type.StructT])

  override def boolConstantEvaluation(expr: PExpression): Option[Boolean] = boolConstantEval(expr)

  override def intConstantEvaluation(expr: PExpression): Option[BigInt] = intConstantEval(expr)

  override def stringConstantEvaluation(expr: PExpression): Option[String] = stringConstantEval(expr)

  override def getTypeInfo: TypeInfo = this
}
