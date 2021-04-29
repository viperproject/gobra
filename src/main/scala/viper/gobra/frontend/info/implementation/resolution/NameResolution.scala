// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.BuiltInMemberTag
import viper.gobra.frontend.info.base.BuiltInMemberTag.{BuiltInFPredicateTag, BuiltInFunctionTag, BuiltInMPredicateTag, BuiltInMethodTag}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type.StructT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignModi}
import viper.gobra.util.Violation

trait NameResolution { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.Entity
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._

  private[resolution] lazy val defEntity: PDefLikeId => Entity =
    attr[PDefLikeId, Entity] {
      case w: PWildcard => Wildcard(w, this)
      case id@ tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {

        case decl: PConstDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => decl.left(idx) match {
              case idn: PIdnDef => SingleConstant(decl, idn, decl.right(idx), decl.typ, isGhost, this)
              case w: PWildcard => Wildcard(w, this)
            }
            case _ => UnknownEntity()
          }

        case decl: PVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => SingleLocalVariable(Some(decl.right(idx)), decl.typ, decl, isGhost, decl.addressable(idx), this)
            case AssignMode.Multi  => MultiLocalVariable(idx, decl.right.head, isGhost, decl.addressable(idx), this)
            case _ if decl.right.isEmpty => SingleLocalVariable(None, decl.typ, decl, isGhost, decl.addressable(idx), this)
            case _ => UnknownEntity()
          }

        case decl: PTypeDef => NamedType(decl, isGhost, this)
        case decl: PTypeAlias => TypeAlias(decl, isGhost, this)
        case decl: PFunctionDecl => Function(decl, isGhost, this)
        case decl: PMethodDecl => MethodImpl(decl, isGhost, this)
        case tree.parent.pair(spec: PMethodSig, tdef: PInterfaceType) => MethodSpec(spec, tdef, isGhost, this)

        case decl: PFieldDecl => Field(decl, isGhost, this)
        case decl: PEmbeddedDecl => Embbed(decl, isGhost, this)

        case tree.parent.pair(decl: PNamedParameter, _: PResult) => OutParameter(decl, isGhost, canParameterBeUsedAsShared(decl), this)
        case decl: PNamedParameter => InParameter(decl, isGhost, canParameterBeUsedAsShared(decl), this)
        case decl: PNamedReceiver => ReceiverParameter(decl, isGhost, decl.addressable, this)

        case decl: PTypeSwitchStmt => TypeSwitchVariable(decl, isGhost, addressable = false, this) // TODO: check if type switch variables are addressable in Go

        case decl: PImport => Import(decl, this)

        // Ghost additions
        case decl: PBoundVariable => BoundVariable(decl, this)

        case decl: PFPredicateDecl => FPredicate(decl, this)
        case decl: PMPredicateDecl => MPredicateImpl(decl, this)
        case tree.parent.pair(decl: PMPredicateSig, tdef: PInterfaceType) => MPredicateSpec(decl, tdef, this)

        case tree.parent.pair(decl: PDomainFunction, domain: PDomainType) => DomainFunction(decl, domain, this)

        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
      case c => Violation.violation(s"Only the root has no parent, but got $c")
    }

  private lazy val unkEntity: PIdnUnk => Entity =
    attr[PIdnUnk, Entity] {
      case id@tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {
        case decl: PShortVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => SingleLocalVariable(Some(decl.right(idx)), None, decl, isGhost, decl.addressable(idx), this)
            case AssignMode.Multi => MultiLocalVariable(idx, decl.right.head, isGhost, decl.addressable(idx), this)
            case _ => UnknownEntity()
          }

        case decl: PShortForRange =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          RangeVariable(idx, decl.range, isGhost, addressable = false, this) // TODO: check if range variables are addressable in Go

        case decl: PSelectShortRecv =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          val len = decl.shorts.size

          StrictAssignModi(len, 1) match { // TODO: check if selection variables are addressable in Go
            case AssignMode.Single => SingleLocalVariable(Some(decl.recv), None, decl, isGhost, addressable = false, this)
            case AssignMode.Multi  => MultiLocalVariable(idx, decl.recv, isGhost, addressable = false, this)
            case _ => UnknownEntity()
          }

        case _ => violation("unexpected parent of unknown id")
      }
      case _ => violation("PIdnUnk always has a parent")
    }

  private[resolution] lazy val isGhostDef: PNode => Boolean = isEnclosingExplicitGhost

  private[resolution] def serialize(id: PIdnNode): String = id.name

  private lazy val sequentialDefenv: Chain[Environment] =
    chain(defenvin, defenvout)

  private def initialEnv(n: PPackage): Vector[(String, Entity)] = {
    val members = BuiltInMemberTag.builtInMembers()
    members.flatMap(m => {
      val entity = m match {
        case tag: BuiltInFunctionTag => Some(BuiltInFunction(tag, n, this))
        case _: BuiltInMethodTag => None
        case tag: BuiltInFPredicateTag => Some(BuiltInFPredicate(tag, n, this))
        case tag: BuiltInMPredicateTag => Some(BuiltInMPredicate(tag, n, this))
      }
      entity match {
        case Some(e) => Some((m.identifier, e))
        case _ => None
      }
    })
  }

  private def defenvin(in: PNode => Environment): PNode ==> Environment = {
    case n: PPackage => addShallowDefToEnv(rootenv(initialEnv(n):_*))(n)
    case scope: PUnorderedScope => addShallowDefToEnv(enter(in(scope)))(scope)
    case scope: PScope if !scopeSpecialCaseWithNoNewScope(scope) =>
      logger.debug(scope.toString)
      enter(in(scope))
  }

  private def scopeSpecialCaseWithNoNewScope(s: PScope): Boolean = s match {
    case tree.parent.pair(_: PBlock, _: PMethodDecl | _: PFunctionDecl) => true
    case _ => false
  }

  private def defenvout(out: PNode => Environment): PNode ==> Environment = {

    case id: PIdnDef if doesAddEntry(id) && !isUnorderedDef(id) =>
      defineIfNew(out(id), serialize(id), MultipleEntity(), defEntity(id))

    case id: PIdnUnk if !isDefinedInScope(out(id), serialize(id)) =>
      define(out(id), serialize(id), unkEntity(id))

    case scope: PScope if !scopeSpecialCaseWithNoNewScope(scope) =>
      leave(out(scope))
  }

  private lazy val doesAddEntry: PIdnDef => Boolean =
    attr[PIdnDef, Boolean] {
      case tree.parent(_: PMethodDecl) => false
      case _ => true
    }

  private def addShallowDefToEnv(env: Environment)(n: PUnorderedScope): Environment = {

    def shallowDefs(n: PUnorderedScope): Vector[PIdnDef] = n match {
      case n: PPackage => n.declarations flatMap { m =>

        def actualMember(a: PActualMember): Vector[PIdnDef] = a match {
          case d: PConstDecl => d.left.collect{ case x: PIdnDef => x }
          case d: PVarDecl => d.left.collect{ case x: PIdnDef => x }
          case d: PFunctionDecl => Vector(d.id)
          case d: PTypeDecl => Vector(d.left) ++ leakingIdentifier(d.right)
          case _: PMethodDecl => Vector.empty
        }

        /* Returns identifier definitions with a package scope occurring in a type. */
        def leakingIdentifier(t: PType): Vector[PIdnDef] = t match {
          case t: PDomainType => t.funcs.map(_.id)
          case _ => Vector.empty
        }

        m match {
          case a: PActualMember => actualMember(a)
          case PExplicitGhostMember(a) => actualMember(a)
          case p: PMPredicateDecl => Vector(p.id)
          case p: PFPredicateDecl => Vector(p.id)
          case _: PImplementationProof => Vector.empty
        }
      }

      // imports do not belong to the root environment but are file/program specific (instead of package specific):
      case n: PProgram => n.imports flatMap {
        case PExplicitQualifiedImport(id: PIdnDef, _) => Vector(id)
        case _ => Vector.empty
      }

      case n: PStructType => n.clauses.flatMap { c =>
        def collectStructIds(clause: PActualStructClause): Vector[PIdnDef] = clause match {
          case d: PFieldDecls => d.fields map (_.id)
          case d: PEmbeddedDecl => Vector(d.id)
        }

        c match {
          case clause: PActualStructClause => collectStructIds(clause)
          case PExplicitGhostStructClause(clause) => collectStructIds(clause)
        }
      }

      case n: PInterfaceType =>
        n.methSpecs.map(_.id) ++ n.predSpec.map(_.id)

        // domain members are added at the package level
      case _: PDomainType => Vector.empty
    }

    shallowDefs(n).foldLeft(env) {
      case (e, id) => defineIfNew(e, serialize(id), MultipleEntity(), defEntity(id))
    }
  }

  private def isUnorderedDef(id: PIdnDef): Boolean = id match {
    case tree.parent(tree.parent(c)) => enclosingScope(c).isInstanceOf[PUnorderedScope]
    case c => Violation.violation(s"Only the root has no parent, but got $c")
  }

  /** returns whether or not identified `id` is defined at node `n`. */
  def isDefinedAt(id: PIdnNode, n: PNode): Boolean = isDefinedInScope(sequentialDefenv.in(n), serialize(id))


  /**
    * The environment to use to lookup names at a node. Defined to be the
    * completed defining environment for the smallest enclosing scope.
    */
  lazy val scopedDefenv: PNode => Environment =
    attr[PNode, Environment] {

      case tree.lastChild.pair(_: PScope, c) =>
        sequentialDefenv(c)

      case tree.parent(p) =>
        scopedDefenv(p)

      case c => Violation.violation(s"Only the root has no parent, but got $c")
    }

  lazy val topLevelEnvironment: Environment = scopedDefenv(tree.originalRoot)

  lazy val entity: PIdnNode => Entity =
    attr[PIdnNode, Entity] {

      case w@PWildcard() => Wildcard(w, this)

      case tree.parent.pair(id: PIdnUse, n: PDot) =>
        tryDotLookup(n.base, id).map(_._1).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, tree.parent.pair(_: PMethodImplementationProof, ip: PImplementationProof)) =>
        tryMethodLikeLookup(ip.superT, id).map(_._1).getOrElse(UnknownEntity()) // reference method of the super type

      case tree.parent.pair(id: PIdnUse, tree.parent.pair(alias: PImplementationProofPredicateAlias, ip: PImplementationProof)) if alias.left == id =>
        tryMethodLikeLookup(ip.superT, id).map(_._1).getOrElse(UnknownEntity()) // reference predicate of the super type

      case tree.parent.pair(id: PIdnDef, _: PMethodDecl) => defEntity(id)

      case tree.parent.pair(id: PIdnDef, _: PMPredicateDecl) => defEntity(id)

      case n@ tree.parent.pair(id: PIdnUse, tree.parent(tree.parent(lv: PLiteralValue))) =>
        val litType = expectedMiscType(lv)
        if (underlyingType(litType).isInstanceOf[StructT]) { // if the enclosing literal is a struct then id is a field
          findField(litType, id).getOrElse(UnknownEntity())
        } else lookup(sequentialDefenv(n), serialize(n), UnknownEntity()) // otherwise it is just a variable

      case n =>
        (n, lookup(sequentialDefenv(n), serialize(n), UnknownEntity())) match {
          // in case no entity was found in the current package, look for it in unqualifiedly imported packages:
          case (n: PIdnUse, UnknownEntity()) => tryUnqualifiedPackageLookup(n)
          case (_, e: Entity) => e
        }
    }
}
