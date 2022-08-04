// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.PackageResolver.RegularImport
import viper.gobra.frontend.info.base.BuiltInMemberTag
import viper.gobra.frontend.info.base.BuiltInMemberTag.{BuiltInFPredicateTag, BuiltInFunctionTag, BuiltInMPredicateTag, BuiltInMethodTag, BuiltInTypeTag}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type.{InterfaceT, StructT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.util.Violation

trait NameResolution {
  this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.Entity
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._

  private[resolution] lazy val defEntity: PDefLikeId => Entity =
    attr[PDefLikeId, Entity] {
      case w: PWildcard => Wildcard(w, this)
      case id@tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {

          case decl: PConstSpec =>
            val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

            StrictAssignMode(decl.left.size, decl.right.size) match {
              case AssignMode.Single => decl.left(idx) match {
                case idn: PIdnDef => SingleConstant(decl, idn, decl.right(idx), decl.typ, isGhost, this)
                case w: PWildcard => Wildcard(w, this)
              }
              case _ => UnknownEntity()
            }

          case decl: PVarDecl =>
            val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

            StrictAssignMode(decl.left.size, decl.right.size) match {
              case AssignMode.Single => SingleLocalVariable(Some(decl.right(idx)), decl.typ, decl, isGhost, decl.addressable(idx), this)
              case AssignMode.Multi => MultiLocalVariable(idx, decl.right.head, isGhost, decl.addressable(idx), this)
              case _ if decl.right.isEmpty => SingleLocalVariable(None, decl.typ, decl, isGhost, decl.addressable(idx), this)
              case _ => UnknownEntity()
            }

          case decl: PTypeDef => NamedType(decl, isGhost, this)
          case decl: PTypeAlias => TypeAlias(decl, isGhost, this)
          case decl: PFunctionDecl => Function(decl, isGhost, this)
          case decl: PMethodDecl => MethodImpl(decl, isGhost, this)
          case tree.parent.pair(spec: PMethodSig, tdef: PInterfaceType) =>
            // note that a ghost method is not wrapped in a ghost wrapper. Instead, `spec` has a ghost field.
            // therefore, we do not use `isGhost` but `spec.isGhost`:
            MethodSpec(spec, tdef, spec.isGhost, this)

          case decl: PFieldDecl => Field(decl, isGhost, this)
          case decl: PEmbeddedDecl => Embbed(decl, isGhost, this)

          case tree.parent.pair(decl: PNamedParameter, _: PResult) => OutParameter(decl, isGhost, canParameterBeUsedAsShared(decl), this)
          case decl: PNamedParameter => InParameter(decl, isGhost, canParameterBeUsedAsShared(decl), this)
          case decl: PNamedReceiver => ReceiverParameter(decl, isGhost, canReceiverBeUsedAsShared(decl), this)

          case decl: PTypeSwitchStmt => TypeSwitchVariable(decl, isGhost, addressable = false, this) // TODO: check if type switch variables are addressable in Go

          case decl: PImport => Import(decl, this)

          // Closure literals
          case decl: PFunctionLit => Closure(decl, isGhost, this)

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

            StrictAssignMode(decl.left.size, decl.right.size) match {
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

            StrictAssignMode(len, 1) match { // TODO: check if selection variables are addressable in Go
              case AssignMode.Single => SingleLocalVariable(Some(decl.recv), None, decl, isGhost, addressable = false, this)
              case AssignMode.Multi => MultiLocalVariable(idx, decl.recv, isGhost, addressable = false, this)
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
        case tag: BuiltInTypeTag => Some(BuiltInType(tag, n, this))
      }
      entity match {
        case Some(e) => Some((m.identifier, e))
        case _ => None
      }
    })
  }

  private def defenvin(in: PNode => Environment): PNode ==> Environment = {
    case n: PPackage => addUnorderedDefToEnv(rootenv(initialEnv(n): _*))(n)
    case scope: PUnorderedScope => addUnorderedDefToEnv(enter(in(scope)))(scope)
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

  /**
    * Returns true iff the identifier declares an entity that is added to the symbol lookup table
    */
  private lazy val doesAddEntry: PIdnDef => Boolean =
    attr[PIdnDef, Boolean] {
      case tree.parent(_: PDependentDef) => false
      case _ => true
    }

  /**
    * returns the (package-level) identifiers defined by a member
    */
  @scala.annotation.tailrec
  private def packageLevelDefinitions(m: PMember): Vector[PIdnDef] = {
    /* Returns identifier definitions with a package scope occurring in a type. */
    def leakingIdentifier(t: PType): Vector[PIdnDef] = t match {
      case t: PDomainType => t.funcs.map(_.id)
      case _ => Vector.empty
    }

    m match {
      case a: PActualMember => a match {
        case d: PConstDecl => d.specs.flatMap(v => v.left.collect { case x: PIdnDef => x })
        case d: PVarDecl => d.left.collect { case x: PIdnDef => x }
        case d: PFunctionDecl => Vector(d.id)
        case d: PTypeDecl => Vector(d.left) ++ leakingIdentifier(d.right)
        case d: PMethodDecl => Vector(d.id)
      }
      case PExplicitGhostMember(a) => packageLevelDefinitions(a)
      case p: PMPredicateDecl => Vector(p.id)
      case p: PFPredicateDecl => Vector(p.id)
      case _: PImplementationProof => Vector.empty
    }
  }

  private lazy val definitionsForScope: PUnorderedScope => Vector[PIdnDef] =
    attr[PUnorderedScope, Vector[PIdnDef]] {
      case n: PPackage => n.declarations flatMap packageLevelDefinitions

      // imports do not belong to the root environment but are file/program specific (instead of package specific):
      case n: PProgram => n.imports flatMap {
        case PExplicitQualifiedImport(id: PIdnDef, _) => Vector(id)
        case _ => Vector.empty
      }

      // note that the identifiers returned for PStructType will be filtered out before creating corresponding
      // symbol table entries
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
        n.methSpecs.map(_.id) ++ n.predSpecs.map(_.id)

      // domain members are added at the package level
      case _: PDomainType => Vector.empty
    }

  private def addUnorderedDefToEnv(env: Environment)(n: PUnorderedScope): Environment = {
    addToEnv(env)(definitionsForScope(n).filter(doesAddEntry))
  }

  private lazy val dependentDefenv: PUnorderedScope => Environment =
    attr[PUnorderedScope, Environment] {
      n: PUnorderedScope => addToEnv(rootenv())(definitionsForScope(n).filterNot(doesAddEntry))
    }

  private def addToEnv(env: Environment)(identifiers: Vector[PIdnDef]): Environment = {
    identifiers.foldLeft(env) {
      case (e, id) =>
        defineIfNew(e, serialize(id), MultipleEntity(), defEntity(id))
    }
  }

  private def isUnorderedDef(id: PIdnDef): Boolean = id match {
    case tree.parent(tree.parent(c)) => enclosingScope(c).isInstanceOf[PUnorderedScope]
    case c => Violation.violation(s"Only the root has no parent, but got $c")
  }

  /** returns whether or not identified `id` is defined at node `n`. */
  def isDefinedAt(id: PIdnNode, n: PNode): Boolean = isDefinedInScope(sequentialDefenv.in(n), serialize(id))

  def tryLookupAt(id: PIdnNode, n: PNode): Option[Entity] = tryLookup(sequentialDefenv.in(n), serialize(id))


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

      case tree.parent.pair(id: PIdnDef, _: PDependentDef) => defEntity(id) // PIdnDef that depend on a receiver or type are not placed in the symbol table

      case n@ tree.parent.pair(id: PIdnUse, tree.parent.pair(_: PIdentifierKey, tree.parent(lv: PLiteralValue))) =>
        val litType = expectedMiscType(lv)
        if (underlyingType(litType).isInstanceOf[StructT]) { // if the enclosing literal is a struct then id is a field
          findField(litType, id).getOrElse(UnknownEntity())
        } else symbTableLookup(n) // otherwise it is just a variable

      case tree.parent.pair(id: PIdnUse, tree.parent.pair(_: PIdentifierKey, tree.parent(spec: PClosureSpecInstance))) =>
        lookupFunctionMemberOrLit(spec.func).flatMap(func => lookupParamForClosureSpec(id, func)) match {
          case Some(p: InParameter) => p
          case _ => UnknownEntity() // only in-parameter names can be used as keys
        }

      case n: PIdnUse if tryEnclosingClosureImplementationProof(n).nonEmpty =>
        val proof = tryEnclosingClosureImplementationProof(n).get
        lookupFunctionMemberOrLit(proof.impl.spec.func).flatMap(func => lookupParamForClosureSpec(n, func)) match {
          case Some(p: InParameter) => p
          case Some(p: OutParameter) => p
          case _ => symbTableLookup(n)
        }

      case n => symbTableLookup(n)
    }

  private def lookupParamForClosureSpec(id: PIdnUse, func: ActualDataEntity with WithArguments with WithResult): Option[ActualVariable] = {
    def namedParam(p: PParameter): Option[PNamedParameter] = p match {
      case p: PNamedParameter => Some(p)
      case PExplicitGhostParameter(p: PNamedParameter) => Some(p)
      case _ => None
    }

    // Within a spec implementation proof or closure instance, consider all arguments of the spec non-ghost and all results ghost.
    // This is to be as permissive as possible at this point, since all ghostness-related checks are done later on.
    val inParam = func.args.flatMap(namedParam)
      .find(_.id.name == id.name)
      .map(InParameter(_, ghost = false, addressable = false, func.context))

    inParam.orElse{
      func.result.outs.flatMap(namedParam)
        .find(_.id.name == id.name)
        .map(OutParameter(_, ghost = true, addressable = false, func.context))
    }
  }

  private def lookupFunctionMemberOrLit(name: PNameOrDot): Option[ActualDataEntity with WithArguments with WithResult] = name match {
    case PNamedOperand(id) => symbTableLookup(id) match {
      case f: Function => Some(f)
      case c: Closure => Some(c)
      case _ => None
    }
    case PDot(base: PNamedOperand, id) => symbTableLookup(base.id) match {
      case pkg: Import =>
        tryPackageLookup(RegularImport(pkg.decl.importPath), id, pkg.decl) match {
          case Some((f: Function, _)) => Some(f)
          case _ => None
        }
      case _ => None
    }
    case _ => None
  }

  private def symbTableLookup(n: PIdnNode): Entity = {
    type Level = PIdnNode => Option[Entity]

    /** regular symbol table lookup in the current package */
    val level0: Level = n => tryLookup(sequentialDefenv(n), serialize(n))

    /** entity lookup in an unqualified import */
    val level1: Level = {
      case n: PIdnUse => tryUnqualifiedPackageLookup(n) match {
        case UnknownEntity() => None
        case e => Some(e)
      }
      case _ => None
    }

    /** entity lookup inside a definition of an unordered scope (e.g. interface definition) */
    val level2: Level = n =>
      tryEnclosingUnorderedScope(n) match {
        case Some(scope) =>
          // `n` appears in an unordered scope and due to the way Go works, definitions (e.g. methods & predicates) in
          // that scope have not been considered so far. Therefore, we perform a second-level lookup just on the definitions that
          // this unordered scope provides
          val dependentEnv = dependentDefenv(scope)
          // perform now a second lookup in this special dependent environment:
          val res = tryLookup(dependentEnv, serialize(n))
          (res, scope) match {
            case (None, int : PInterfaceType) =>
              try {
                memberSet(InterfaceT(int, this)).lookup(n.name) // lookup in the embeddedFields
              }
              catch { // happens if we are in a cycle because of unknown interface members
                case _ : IllegalStateException => None
              }
            case _ => res
          }
        case _ => None
      }

    /** order of precedence; first level has highest precedence */
    val levels: Seq[Level] = Seq(level0, level1, level2)

    // returns first successfully defined entity otherwise `UnknownEntity()`
    levels.iterator.map(_(n)).find(_.isDefined).flatten.getOrElse(UnknownEntity())
  }

  /**
    * Performs a lookup of `i` in environment `inv`. Returns the associated entity or `None` if no entity has been found
    */
  def tryLookup(env: Environment, i: String): Option[Entity] = {
    lookup(env, i, UnknownEntity()) match {
      case UnknownEntity() => None
      case e => Some(e)
    }
  }
}
