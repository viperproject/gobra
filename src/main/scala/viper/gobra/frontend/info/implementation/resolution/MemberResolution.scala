// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.relation.Relation
import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.PackageResolver.{AbstractImport, BuiltInImport, RegularImport}
import viper.gobra.frontend.info.base.BuiltInMemberTag
import viper.gobra.frontend.info.base.BuiltInMemberTag.{BuiltInMPredicateTag, BuiltInMethodTag}
import viper.gobra.frontend.{PackageResolver, Parser}
import viper.gobra.frontend.info.{ExternalTypeInfo, Info}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.reporting.{NotFoundError, VerifierError}

import scala.annotation.tailrec


trait MemberResolution { this: TypeInfoImpl =>

  override def createField(decl: PFieldDecl): Field =
    defEntity(decl.id).asInstanceOf[Field]

  override def createEmbbed(decl: PEmbeddedDecl): Embbed =
    defEntity(decl.id).asInstanceOf[Embbed]

  override def createMethodImpl(decl: PMethodDecl): MethodImpl =
    defEntity(decl.id).asInstanceOf[MethodImpl]

  override def createMethodSpec(spec: PMethodSig): MethodSpec =
    defEntity(spec.id).asInstanceOf[MethodSpec]

  override def createMPredImpl(decl: PMPredicateDecl): MPredicateImpl =
    defEntity(decl.id).asInstanceOf[MPredicateImpl]

  override def createMPredSpec(spec: PMPredicateSig): MPredicateSpec =
    defEntity(spec.id).asInstanceOf[MPredicateSpec]

  private lazy val receiverMethodSetMap: Map[Type, AdvancedMemberSet[TypeMember]] = {
    tree.root.declarations
      .collect {
        case m: PMethodDecl => createMethodImpl(m)
        case PExplicitGhostMember(m: PMethodDecl) => createMethodImpl(m)
      }
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .transform((_, ms) => AdvancedMemberSet.init(ms))
  }

  private lazy val builtInReceiverMethodSets: Vector[BuiltInMethodLike] = {
    BuiltInMemberTag.builtInMembers() flatMap {
      case tag: BuiltInMethodTag => Some(BuiltInMethod(tag, tree.root, this))
      case _ => None
    }
  }

  def builtInReceiverMethodSet(recv: Type): AdvancedMemberSet[TypeMember] = {
    // filter out all methods that are not defined for this receiver type
    val definedMethods = builtInReceiverMethodSets.filter(p => p.tag.typ(config).typing.isDefinedAt(Vector(recv)))
    AdvancedMemberSet.init(definedMethods)
  }

  def receiverMethodSet(recv: Type): AdvancedMemberSet[TypeMember] =
    receiverMethodSetMap.getOrElse(recv, AdvancedMemberSet.empty) union
      builtInReceiverMethodSet(recv)

  private lazy val receiverPredicateSetMap: Map[Type, AdvancedMemberSet[TypeMember]] = {
    tree.root.declarations
      .collect { case m: PMPredicateDecl => createMPredImpl(m) }
      .groupBy { m: MPredicateImpl => miscType(m.decl.receiver) }
      .transform((_, ms) => AdvancedMemberSet.init(ms))
  }

  private lazy val builtInReceiverPredicateSets: Vector[BuiltInMethodLike] = {
    BuiltInMemberTag.builtInMembers() flatMap {
      case tag: BuiltInMPredicateTag => Some(BuiltInMPredicate(tag, tree.root, this))
      case _ => None
    }
  }

  def builtInReceiverPredicateSet(recv: Type): AdvancedMemberSet[TypeMember] = {
    // filter out all mpredicates that are not defined for this receiver type
    val definedMPreds = builtInReceiverPredicateSets.filter(p => p.tag.typ(config).typing.isDefinedAt(Vector(recv)))
    AdvancedMemberSet.init(definedMPreds)
  }

  def receiverPredicateSet(recv: Type): AdvancedMemberSet[TypeMember] =
    receiverPredicateSetMap.getOrElse(recv, AdvancedMemberSet.empty) union
      builtInReceiverPredicateSet(recv)

  lazy val receiverSet: Type => AdvancedMemberSet[TypeMember] =
    attr[Type, AdvancedMemberSet[TypeMember]] (t => receiverMethodSet(t) union receiverPredicateSet(t))

  lazy val interfaceMethodSet: InterfaceT => AdvancedMemberSet[TypeMember] =
    attr[InterfaceT, AdvancedMemberSet[TypeMember]] {
      case InterfaceT(PInterfaceType(es, methSpecs, predSpecs), ctxt) =>
        AdvancedMemberSet.init[TypeMember](methSpecs.map(m => createMethodSpec(m))) union
          AdvancedMemberSet.init[TypeMember](predSpecs.map(m => createMPredSpec(m))) union
          AdvancedMemberSet.union {
            es.map(e => interfaceMethodSet(
              entity(e.typ.id) match {
                  // TODO: might break if there is a cycle
                case NamedType(PTypeDef(t: PInterfaceType, _), _, _) => InterfaceT(t, ctxt)
              }
            ))
          }
    }


  private def pastPromotions[M <: TypeMember](cont: Type => AdvancedMemberSet[M]): Type => AdvancedMemberSet[M] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[M] = attr[Type, AdvancedMemberSet[M]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.symbType(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        AdvancedMemberSet.union(s.decl.embedded map { e =>
          val et = miscType(e.typ)
          (cont(et) union go(pastDeref = false)(et)).promote(createEmbbed(e))
        })

      case s: InterfaceT if !pastDeref => cont(s)

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  private val fieldSuffix: Type => AdvancedMemberSet[StructMember] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[StructMember] = attr[Type, AdvancedMemberSet[StructMember]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.symbType(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        val (es, fs) = (s.decl.embedded, s.decl.fields)
        AdvancedMemberSet.init[StructMember](fs map s.context.createField) union AdvancedMemberSet.init(es map s.context.createEmbbed)

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  val structMemberSet: Type => AdvancedMemberSet[StructMember] =
    attr[Type, AdvancedMemberSet[StructMember]] {
      case Single(t) => fieldSuffix(t) union pastPromotions(fieldSuffix)(t)
      case _ => AdvancedMemberSet.empty
    }

  private val pastPromotionsMethodSuffix: Type => AdvancedMemberSet[TypeMember] =
    attr[Type, AdvancedMemberSet[TypeMember]] {
      case t: InterfaceT => interfaceMethodSet(t)
      case pt@ PointerT(t) => receiverSet(pt) union receiverSet(t).ref
      case t => receiverSet(t) union receiverSet(PointerT(t)).deref
    }

  val nonAddressableMethodSet: Type => AdvancedMemberSet[TypeMember] =
    attr[Type, AdvancedMemberSet[TypeMember]] {
      case Single(t) =>
        pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
          case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
          case _ => receiverSet(t)
        })
      case _ => AdvancedMemberSet.empty
    }

  val addressableMethodSet: Type => AdvancedMemberSet[TypeMember] =
    attr[Type, AdvancedMemberSet[TypeMember]] {
      case Single(t) =>
        pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
          case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
          case _ => receiverSet(t) union receiverSet(PointerT(t)).deref
        })
      case _ => AdvancedMemberSet.empty
    }

  override def localMemberSet(t: Type): AdvancedMemberSet[TypeMember] = {
    nonAddressableMethodSet(t)
  }

  override def memberSet(t: Type): AdvancedMemberSet[TypeMember] = {
    val context = getMethodReceiverContext(t)
    context.localMemberSet(t)
  }

  def tryFieldLookup(t: Type, id: PIdnUse): Option[(StructMember, Vector[MemberPath])] =
    structMemberSet(t).lookupWithPath(id.name)

  def tryMethodLikeLookup(e: PExpression, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])] = {
    // check whether e is well-defined:
    if (wellDefExpr(e).valid) {
      val typ = exprType(e)
      val context = getMethodReceiverContext(typ)
      if (effAddressable(e)) context.tryAddressableMethodLikeLookup(typ, id)
      else context.tryNonAddressableMethodLikeLookup(typ, id)
    } else None
  }

  def tryMethodLikeLookup(e: Type, id: PIdnUse): Option[(TypeMember, Vector[MemberPath])] = {
    val context = getMethodReceiverContext(e)
    context.tryNonAddressableMethodLikeLookup(e, id)
  }

  @tailrec
  private def getMethodReceiverContext(t: Type): ExternalTypeInfo = {
    Single.unapply(t) match {
      case Some(ct: ContextualType) => ct.context
      case Some(p: PointerT) => getMethodReceiverContext(p.elem)
      case _ => this
    }
  }

  def tryMethodLikeLookup(e: PType, id: PIdnUse): Option[(Entity, Vector[MemberPath])] = tryMethodLikeLookup(typeSymbType(e), id)

  def tryPackageLookup(importTarget: AbstractImport, id: PIdnUse, errNode: PNode): Option[(Entity, Vector[MemberPath])] = {
    def parseAndTypeCheck(importTarget: AbstractImport): Either[Vector[VerifierError], ExternalTypeInfo] = {
      val pkgFiles = PackageResolver.resolve(importTarget, config.includeDirs).getOrElse(Vector())
      val res = for {
        nonEmptyPkgFiles <- if (pkgFiles.isEmpty)
          Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
          else Right(pkgFiles)
        parsedProgram <- Parser.parse(nonEmptyPkgFiles.map(_.path), specOnly = true)(config)
        // TODO maybe don't check whole file but only members that are actually used/imported
        // By parsing only declarations and their specification, there shouldn't be much left to type check anyways
        // Info.check would probably need some restructuring to type check only certain members
        info <- Info.check(parsedProgram, context)(config)
        // we do no longer need them, so we close them:
        _ = pkgFiles.map(_.close())
      } yield info
      res.fold(
        errs => context.addErrenousPackage(importTarget, errs),
        info => context.addPackage(importTarget, info)
      )
      res
    }

    def getTypeChecker(importTarget: AbstractImport, errNode: PNode): Either[Messages, ExternalTypeInfo] = {
      def createImportError(errs: Vector[VerifierError]): Messages = {
        // create an error message located at the import statement to indicate errors in the imported package
        // we distinguish between parse and type errors, cyclic imports, and packages whose source files could not be found
        val notFoundErr = errs.collectFirst { case e: NotFoundError => e }
        // alternativeErr is a function to compute the message only when needed
        val alternativeErr = () => context.getImportCycle(importTarget) match {
          case Some(cycle) => message(errNode, s"Package '$importTarget' is part of this import cycle: ${cycle.mkString("[", ", ", "]")}")
          case _ => message(errNode, s"Package '$importTarget' contains errors")
        }
        notFoundErr.map(e => message(errNode, e.message))
          .getOrElse(alternativeErr())
      }

      // check if package was already parsed, otherwise do parsing and type checking:
      val cachedInfo = context.getTypeInfo(importTarget)
      cachedInfo.getOrElse(parseAndTypeCheck(importTarget)).left.map(createImportError)
    }

    val foreignPkgResult = for {
      typeChecker <- getTypeChecker(importTarget, errNode)
      entity = typeChecker.externalRegular(id)
    } yield entity
    foreignPkgResult.fold(
      msgs => Some((ErrorMsgEntity(msgs), Vector())),
      m => m.flatMap(m => Some((m, Vector())))
    )
  }


  def tryDotLookup(b: PExpressionOrType, id: PIdnUse): Option[(Entity, Vector[MemberPath])] = {
    exprOrType(b) match {
      case Left(expr) =>
        val methodLikeAttempt = tryMethodLikeLookup(expr, id)
        if (methodLikeAttempt.isDefined) methodLikeAttempt
        else tryFieldLookup(exprType(expr), id)

      case Right(typ) =>
        val methodLikeAttempt = tryMethodLikeLookup(typ, id)
        if (methodLikeAttempt.isDefined) methodLikeAttempt
        else typeSymbType(typ) match {
          case pkg: ImportT => tryPackageLookup(RegularImport(pkg.decl.importPath), id, pkg.decl)
          case _ => None
        }
    }
  }

  lazy val tryUnqualifiedPackageLookup: PIdnUse => Entity =
    attr[PIdnUse, Entity] {
      id =>
        // Go is weird in the sense that it let's you redeclare built-in identifiers such as "error" but won't complain
        // about the redeclaration. If the redeclaration happens in the same package, the redeclaration is
        // used (instead of the built-in one). If the redeclaration happens in an imported package Go's behavior is not
        // fully clear since "error" is not exported. However, we give higher precedence to the built-in one in Gobra
        // to approximate Go's behavior.
        tryUnqualifiedBuiltInPackageLookup(id).getOrElse(tryUnqualifiedRegularPackageLookup(id))
    }

  def tryUnqualifiedBuiltInPackageLookup(id: PIdnUse): Option[Entity] =
    tryPackageLookup(BuiltInImport, id, id).map(_._1)

  def tryUnqualifiedRegularPackageLookup(id: PIdnUse): Entity = {
    val transitiveParent = transitiveClosure(id, tree.parent(_))
    val entities = for {
      // get enclosing PProgram for this PIdnUse node
      program <- transitiveParent(id).collectFirst { case p: PProgram => p }
      // consider all unqualified imports for this program (not package)
      unqualifiedImports = program.imports.collect { case ui: PUnqualifiedImport => ui }
      // perform a package lookup in each unqualifiedly imported package
      results = unqualifiedImports.flatMap(ui => tryPackageLookup(RegularImport(ui.importPath), id, ui))
    } yield results
    entities match {
      case Some(Vector(elem)) => elem._1
      case Some(v) if v.length > 1 => MultipleEntity()
      case _ => UnknownEntity()
    }
  }

  def transitiveClosure[T](t : T, onestep : T => Vector[T]): Relation[T, T] = {
    // fromOneStep creates a new relation in which all links from t to the root are contained in
    val links = Relation.fromOneStep(t, onestep)
    // create a new relation that consists of the image of links but only has t as its domain:
    val relation = new Relation[T, T]
    for (pair <- links.pairs) {
      relation.put(t, pair._2)
    }
    relation
  }


  def findField(t: Type, id: PIdnUse): Option[StructMember] =
    structMemberSet(t).lookup(id.name)


}
