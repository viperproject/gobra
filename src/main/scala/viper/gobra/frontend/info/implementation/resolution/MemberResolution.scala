// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.relation.Relation
import org.bitbucket.inkytonik.kiama.util.{Entity, MultipleEntity, UnknownEntity}
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{PackageResolver, Parser}
import viper.gobra.frontend.info.{ExternalTypeInfo, Info}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.reporting.{NotFoundError, VerifierError}

import scala.annotation.tailrec
import scala.collection.immutable.Queue


trait MemberResolution { this: TypeInfoImpl =>

  import scala.collection.breakOut

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

  private lazy val receiverMethodSetMap: Map[Type, AdvancedMemberSet[MethodLike]] = {
    tree.root.declarations
      .collect {
        case m: PMethodDecl => createMethodImpl(m)
        case PExplicitGhostMember(m: PMethodDecl) => createMethodImpl(m)
      }(breakOut)
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .mapValues(ms => AdvancedMemberSet.init(ms))
  }

  def receiverMethodSet(recv: Type): AdvancedMemberSet[MethodLike] =
    receiverMethodSetMap.getOrElse(recv, AdvancedMemberSet.empty)

  private lazy val receiverPredicateSetMap: Map[Type, AdvancedMemberSet[MethodLike]] = {
    tree.root.declarations
      .collect { case m: PMPredicateDecl => createMPredImpl(m) }(breakOut)
      .groupBy { m: MPredicateImpl => miscType(m.decl.receiver) }
      .mapValues(ms => AdvancedMemberSet.init(ms))
  }

  def receiverPredicateSet(recv: Type): AdvancedMemberSet[MethodLike] =
    receiverPredicateSetMap.getOrElse(recv, AdvancedMemberSet.empty)

  lazy val receiverSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] (t => receiverMethodSet(t) union receiverPredicateSet(t))

  lazy val interfaceMethodSet: InterfaceT => AdvancedMemberSet[MethodLike] =
    attr[InterfaceT, AdvancedMemberSet[MethodLike]] {
      case InterfaceT(PInterfaceType(es, methSpecs, predSpecs)) =>
        AdvancedMemberSet.init[MethodLike](methSpecs.map(m => createMethodSpec(m))) union
          AdvancedMemberSet.init[MethodLike](predSpecs.map(m => createMPredSpec(m))) union
          AdvancedMemberSet.union {
            es.map(e => interfaceMethodSet(
              entity(e.typ.id) match {
                case NamedType(PTypeDef(t: PInterfaceType, _), _, _) => InterfaceT(t)
              }
            ))
          }
    }


  private def pastPromotions[M <: TypeMember](cont: Type => AdvancedMemberSet[M]): Type => AdvancedMemberSet[M] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[M] = attr[Type, AdvancedMemberSet[M]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.typ(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        AdvancedMemberSet.union(s.decl.embedded map { e =>
          val et = miscType(e.typ)
          (cont(et) union go(pastDeref = false)(et)).promote(createEmbbed(e))
        })

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  private val fieldSuffix: Type => AdvancedMemberSet[StructMember] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[StructMember] = attr[Type, AdvancedMemberSet[StructMember]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.typ(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        val (es, fs) = (s.decl.embedded, s.decl.fields)
        AdvancedMemberSet.init[StructMember](fs map s.context.createField) union AdvancedMemberSet.init(es map s.context.createEmbbed)

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  val structMemberSet: Type => AdvancedMemberSet[StructMember] =
    attr[Type, AdvancedMemberSet[StructMember]] { t => fieldSuffix(t) union pastPromotions(fieldSuffix)(t) }

  private val pastPromotionsMethodSuffix: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] {
      case t: InterfaceT => interfaceMethodSet(t)
      case pt@ PointerT(t) => receiverSet(pt) union receiverSet(t).ref
      case t => receiverSet(t) union receiverSet(PointerT(t)).deref
    }

  val nonAddressableMethodSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
        case _ => receiverSet(t)
      })
    }

  val addressableMethodSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
        case _ => receiverSet(t) union receiverSet(PointerT(t)).deref
      })
    }




  def tryFieldLookup(t: Type, id: PIdnUse): Option[(StructMember, Vector[MemberPath])] =
    structMemberSet(t).lookupWithPath(id.name)

  def tryMethodLikeLookup(e: PExpression, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = {
    val typ = exprType(e)
    val context = getMethodReceiverContext(typ)
    if (effAddressable(e)) context.tryAddressableMethodLikeLookup(typ, id)
    else context.tryNonAddressableMethodLikeLookup(typ, id)
  }

  def tryMethodLikeLookup(e: Type, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = {
    val context = getMethodReceiverContext(e)
    context.tryNonAddressableMethodLikeLookup(e, id)
  }

  private def getMethodReceiverContext(t: Type): ExternalTypeInfo = {
    t match {
      case ct: ContextualType => ct.context
      case p: PointerT => getMethodReceiverContext(p.elem)
      case _ => this
    }
  }

  def tryMethodLikeLookup(e: PType, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = tryMethodLikeLookup(typeType(e), id)

  def tryPackageLookup(pkgImport: PImport, id: PIdnUse): Option[(Entity, Vector[MemberPath])] = {
    def parseAndTypeCheck(pkgImport: PImport): Either[Vector[VerifierError], ExternalTypeInfo] = {
      val importPath = pkgImport.importPath
      val pkgFiles = PackageResolver.resolve(importPath, config.includeDirs).right.getOrElse(Vector())
      val res = for {
        nonEmptyPkgFiles <- if (pkgFiles.isEmpty)
          Left(Vector(NotFoundError(s"No source files for package '$importPath' found")))
          else Right(pkgFiles)
        parsedProgram <- Parser.parse(nonEmptyPkgFiles, specOnly = true)(config)
        // TODO maybe don't check whole file but only members that are actually used/imported
        // By parsing only declarations and their specification, there shouldn't be much left to type check anyways
        // Info.check would probably need some restructuring to type check only certain members
        info <- Info.check(parsedProgram, context)(config)
      } yield info
      res.fold(
        errs => context.addErrenousPackage(importPath, errs),
        info => context.addPackage(importPath, info)
      )
      res
    }

    def getTypeChecker(pkgImport: PImport): Either[Messages, ExternalTypeInfo] = {
      def createImportError(errs: Vector[VerifierError]): Messages = {
        // create an error message located at the import statement to indicate errors in the imported package
        // we distinguish between parse and type errors, cyclic imports, and packages whose source files could not be found
        val notFoundErr = errs.collectFirst { case e: NotFoundError => e }
        // alternativeErr is a function to compute the message only when needed
        val alternativeErr = () => context.getImportCycle(pkgImport.importPath) match {
          case Some(cycle) => message(pkgImport, s"Package '${pkgImport.importPath}' is part of this import cycle: ${cycle.mkString("[", ", ", "]")}")
          case _ => message(pkgImport, s"Package '${pkgImport.importPath}' contains errors")
        }
        notFoundErr.map(e => message(pkgImport, e.message))
          .getOrElse(alternativeErr())
      }

      // check if package was already parsed, otherwise do parsing and type checking:
      val cachedInfo = context.getTypeInfo(pkgImport.importPath)
      cachedInfo.getOrElse(parseAndTypeCheck(pkgImport)).left.map(createImportError)
    }

    val foreignPkgResult = for {
      typeChecker <- getTypeChecker(pkgImport)
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
        else typeType(typ) match {
          case pkg: ImportT => tryPackageLookup(pkg.decl, id)
          case _ => None
        }
    }
  }

  lazy val tryUnqualifiedPackageLookup: PIdnUse => Entity =
    attr[PIdnUse, Entity] {
      id => {
        val transitiveParent = transitiveClosure(id, tree.parent(_))
        val entities = for {
          // get enclosing PProgram for this PIdnUse node
          program <- transitiveParent(id).collectFirst { case p: PProgram => p }
          // consider all unqualified imports for this program (not package)
          unqualifiedImports = program.imports.collect { case ui: PUnqualifiedImport => ui }
          // perform a package lookup in each unqualifiedly imported package
          results = unqualifiedImports.map(ui => tryPackageLookup(ui, id)).collect { case Some(r) => r }
        } yield results
        entities match {
          case Some(Vector(elem)) => elem._1
          case Some(v) if v.length > 1 => MultipleEntity()
          case _ => UnknownEntity()
        }
      }
    }

  def transitiveClosure[T](t : T, onestep : T => Vector[T]): Relation[T, T] = {

    def alternativeTransitiveClosure(t : T, onestep : T => Vector[T]): Relation[T, T] = {
      val relation = new Relation[T, T]

      @tailrec
      def loop(pending : Queue[T]) : Relation[T, T] =
        if (pending.isEmpty)
          relation
        else {
          val l = pending.front
          val next = onestep(l)
          if (next.nonEmpty)
            relation.putAll(t, next)
          loop(pending.tail.enqueue(next))
        }

      loop(Queue(t))
    }

    def relationEquality(relation1: Relation[T, T], relation2: Relation[T, T]): Boolean = {
      (relation1.pairs.forall(pair =>
        relation2.containsInDomain(pair._1) &&
          relation2(pair._1).contains(pair._2))
      && relation2.pairs.forall(pair =>
        relation1.containsInDomain(pair._1) &&
          relation1(pair._1).contains(pair._2)))
    }

    // fromOneStep creates a new relation in which all links from t to the root are contained in
    val links = Relation.fromOneStep(t, onestep)
    // create a new relation that consists of the image of links but only has t as its domain:
    val relation = new Relation[T, T]
    for (pair <- links.pairs) {
      relation.put(t, pair._2)
    }

    // this should be equivalent to the following:
    assert(relationEquality(relation, alternativeTransitiveClosure(t, onestep)))

    relation
  }


  def findField(t: Type, id: PIdnUse): Option[StructMember] =
    structMemberSet(t).lookup(id.name)


}
