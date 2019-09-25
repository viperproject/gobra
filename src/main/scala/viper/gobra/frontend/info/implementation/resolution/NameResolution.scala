package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignModi}

trait NameResolution { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.{Entity, UnknownEntity}
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._

  private[resolution] lazy val defEntity: PDefLikeId => Entity =
    attr[PDefLikeId, Entity] {
      case PWildcard() => ???
      case id@ tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {

        case decl: PConstDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => SingleConstant(decl.right(idx), decl.typ, isGhost)
            case AssignMode.Multi => MultiConstant(idx, decl.right.head, isGhost)
            case _ => UnknownEntity()
          }

        case decl: PVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => SingleLocalVariable(Some(decl.right(idx)), decl.typ, isGhost)
            case AssignMode.Multi  => MultiLocalVariable(idx, decl.right.head, isGhost)
            case _ if decl.right.isEmpty => SingleLocalVariable(None, decl.typ, isGhost)
            case _ => UnknownEntity()
          }

        case decl: PTypeDef => NamedType(decl, isGhost)
        case decl: PTypeAlias => TypeAlias(decl, isGhost)
        case decl: PFunctionDecl => Function(decl, isGhost)
        case decl: PMethodDecl => MethodImpl(decl, isGhost)
        case spec: PMethodSig => MethodSpec(spec, isGhost)

        case decl: PFieldDecl => Field(decl, isGhost)
        case decl: PEmbeddedDecl => Embbed(decl, isGhost)

        case tree.parent.pair(decl: PNamedParameter, _: PResultClause) => OutParameter(decl, isGhost)
        case decl: PNamedParameter => InParameter(decl, isGhost)
        case decl: PNamedReceiver => ReceiverParameter(decl, isGhost)

        case decl: PTypeSwitchStmt => TypeSwitchVariable(decl, isGhost)


            // Ghost additions

        case decl: PBoundVariable => BoundVariable(decl)

        case decl: PFPredicateDecl => FPredicate(decl)
        case decl: PMPredicateDecl => MPredicateImpl(decl)
        case decl: PMPredicateSig => MPredicateSpec(decl)
      }
    }



  private lazy val unkEntity: PIdnUnk => Entity =
    attr[PIdnUnk, Entity] {
      case id@tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {
        case decl: PShortVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          StrictAssignModi(decl.left.size, decl.right.size) match {
            case AssignMode.Single => SingleLocalVariable(Some(decl.right(idx)), None, isGhost)
            case AssignMode.Multi => MultiLocalVariable(idx, decl.right.head, isGhost)
            case _ => UnknownEntity()
          }

        case decl: PShortForRange =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          val len = decl.shorts.size
          RangeVariable(idx, decl.range, isGhost)

        case decl: PSelectShortRecv =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          val len = decl.shorts.size

          StrictAssignModi(len, 1) match {
            case AssignMode.Single => SingleLocalVariable(Some(decl.recv), None, isGhost)
            case AssignMode.Multi  => MultiLocalVariable(idx, decl.recv, isGhost)
            case _ => UnknownEntity()
          }

        case _ => violation("unexpected parent of unknown id")
      }
    }

  private lazy val isGhostDef: PNode => Boolean = isEnclosingExplicitGhost

  private[resolution] def serialize(id: PIdnNode): String = id.name

  private[resolution] lazy val sequentialDefenv: Chain[Environment] =
    chain(defenvin, defenvout)

  private def defenvin(in: PNode => Environment): PNode ==> Environment = {
    case n: PProgram => addShallowDefToEnv(rootenv())(n)
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
      defineIfNew(out(id), serialize(id), defEntity(id))

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
      case n: PProgram => n.declarations flatMap { m =>

        def actualMember(a: PActualMember): Vector[PIdnDef] = a match {
          case d: PConstDecl => d.left
          case d: PVarDecl => d.left
          case d: PFunctionDecl => Vector(d.id)
          case d: PTypeDecl => Vector(d.left)
          case _: PMethodDecl => Vector.empty
        }

        m match {
          case a: PActualMember => actualMember(a)
          case PExplicitGhostMember(a) => actualMember(a)
          case p: PMPredicateDecl => Vector(p.id)
          case p: PFPredicateDecl => Vector(p.id)
        }
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
    }

    shallowDefs(n).foldLeft(env) {
      case (e, id) => defineIfNew(e, serialize(id), defEntity(id))
    }
  }

  private def isUnorderedDef(id: PIdnDef): Boolean = id match { // TODO: a bit hacky, clean up at some point
    case tree.parent(tree.parent(c)) => enclosingScope(c).isInstanceOf[PUnorderedScope]
  }


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
    }

  lazy val entity: PIdnNode => Entity =
    attr[PIdnNode, Entity] {

      case tree.parent.pair(id: PIdnUse, e@ PSelectionOrMethodExpr(_, f)) if id == f =>
        resolveSelectionOrMethodExpr(e)
        { case (b, i) => findSelection(b, i) }
        { case (b, i) => findMethodLike(idType(b), i) }
          .flatten.getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PMethodExpr) =>
        findMethodLike(typeType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PSelection) =>
        findSelection(e.base, id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnDef, _: PMethodDecl) => defEntity(id)


      case tree.parent.pair(id: PIdnUse, e@ PMPredOrMethRecvOrExprCall(_, f, _)) if id == f =>
        resolveMPredOrMethExprOrRecvCall(e)
        { case (b, i, _) => findSelection(b, i) }
        { case (b, i, _) => findMethodLike(idType(b), i)}
          .flatten.getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PMPredOrMethExprCall) =>
        findMethodLike(typeType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PMPredOrBoolMethCall) =>
        findSelection(e.recv, id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnDef, _: PMPredicateDecl) => defEntity(id)


      case n =>
        lookup(sequentialDefenv(n), serialize(n), UnknownEntity())
    }

}
