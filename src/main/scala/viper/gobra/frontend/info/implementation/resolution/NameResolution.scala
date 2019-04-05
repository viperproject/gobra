package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait NameResolution { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.{Entity, UnknownEntity}
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._

  private lazy val defEntity: PDefLikeId => Entity =
    attr[PDefLikeId, Entity] {
      case PWildcard() => ???
      case id@ tree.parent(p) => p match {

        case decl: PConstDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          assignModi(decl.left.size, decl.right.size) match {
            case SingleAssign => SingleConstant(decl.right(idx), decl.typ)
            case MultiAssign => MultiConstant(idx, decl.right.head)
            case _ => UnknownEntity()
          }

        case decl: PVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          assignModi(decl.left.size, decl.right.size) match {
            case SingleAssign => SingleConstant(decl.right(idx), decl.typ)
            case MultiAssign => MultiConstant(idx, decl.right.head)
            case _ => UnknownEntity()
          }

        case decl: PTypeDef => NamedType(decl)
        case decl: PTypeAlias => TypeAlias(decl)
        case decl: PFunctionDecl => Function(decl)
        case decl: PMethodDecl => MethodImpl(decl)
        case spec: PMethodSpec => MethodSpec(spec)

        case decl: PFieldDecl => Field(decl)
        case decl: PEmbeddedDecl => Embbed(decl)

        case tree.parent.pair(decl: PNamedParameter, _: PResultClause) => OutParameter(decl)
        case decl: PNamedParameter => InParameter(decl)
        case decl: PNamedReceiver => ReceiverParameter(decl)

        case decl: PTypeSwitchStmt => TypeSwitchVariable(decl)
      }
    }

  private lazy val unkEntity: PIdnUnk => Entity =
    attr[PIdnUnk, Entity] {
      case id@tree.parent(p) => p match {
        case decl: PShortVarDecl =>
          val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

          assignModi(decl.left.size, decl.right.size) match {
            case SingleAssign => SingleConstant(decl.right(idx), None)
            case MultiAssign => MultiConstant(idx, decl.right.head)
            case _ => UnknownEntity()
          }

        case decl: PShortForRange =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          val len = decl.shorts.size
          RangeVariable(idx, decl.range)

        case decl: PSelectShortRecv =>
          val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
          val len = decl.shorts.size

          assignModi(len, 1) match {
            case SingleAssign => SingleConstant(decl.recv, None)
            case MultiAssign => MultiConstant(idx, decl.recv)
            case _ => UnknownEntity()
          }

        case _ => violation("unexpected parent of unknown id")
      }
    }

  private[resolution] def serialize(id: PIdnNode): String = id.name

  private[resolution] lazy val sequentialDefenv: Chain[Environment] =
    chain(defenvin, defenvout)

  private def defenvin(in: PNode => Environment): PNode ==> Environment = {
    case n: PProgram => addShallowDefToEnv(rootenv())(n)
    case scope: PUnorderedScope => addShallowDefToEnv(enter(in(scope)))(scope)
    case scope: PScope => println("enter scope"); enter(in(scope))
  }

  private def defenvout(out: PNode => Environment): PNode ==> Environment = {

    case id: PIdnDef if doesAddEntry(id) && !isUnorderedDef(id) =>
      println(s"add ${id.name} to" + out(id).map(_.keySet))
      defineIfNew(out(id), serialize(id), defEntity(id))

    case id: PIdnUnk if !isDefinedInScope(out(id), serialize(id)) =>
      define(out(id), serialize(id), unkEntity(id))

    case scope: PScope =>
      println("leave scope")
      leave(out(scope))
  }

  private lazy val doesAddEntry: PIdnDef => Boolean =
    attr[PIdnDef, Boolean] {
      case tree.parent(_: PMethodDecl) => false
      case _ => true
    }

  private def addShallowDefToEnv(env: Environment)(n: PUnorderedScope): Environment = {

    def shallowDefs(n: PUnorderedScope): Vector[PIdnDef] = n match {
      case n: PProgram => n.declarations flatMap {
        case d: PConstDecl => d.left
        case d: PVarDecl => d.left
        case d: PFunctionDecl => Vector(d.id)
        case d: PTypeDecl => Vector(d.left)
        case _: PMethodDecl => Vector.empty
      }

      case n: PStructType => n.clauses.flatMap {
        case d: PFieldDecls => d.fields map (_.id)
        case d: PEmbeddedDecl => Vector(d.id)
      }

      case n: PInterfaceType => n.specs map (_.id)
    }

    shallowDefs(n).foldLeft(env) {
      case (e, id) => defineIfNew(e, serialize(id), defEntity(id))
    }
  }

  private def isUnorderedDef(id: PIdnDef): Boolean = id match { // TODO: a bit hacky, clean up at some point
    case tree.parent(tree.parent(c)) => enclosingScope(c).isInstanceOf[PUnorderedScope]
  }


  //    /**
  //      * The environment to use to lookup names at a node. Defined to be the
  //      * completed defining environment for the smallest enclosing scope.
  //      */
  //    lazy val scopedDefenv: PNode => Environment =
  //      attr[PNode, Environment] {
  //
  //        case tree.lastChild.pair(_: PScope, c) =>
  //          sequentialDefenv(c)
  //
  //        case tree.parent(p) =>
  //          scopedDefenv(p)
  //      }

  lazy val entity: PIdnNode => Entity =
    attr[PIdnNode, Entity] {

      case tree.parent.pair(id: PIdnUse, e@ PSelectionOrMethodExpr(_, f)) if id == f =>
        resolveSelectionOrMethodExpr(e)
        { case (b, i) => findSelection(idType(b), i) }
        { case (b, i) => findMember(idType(b), i) }
          .flatten.getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PMethodExpr) =>
        findMember(typeType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUse, e: PSelection) =>
        findSelection(exprType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnDef, _: PMethodDecl) => defEntity(id)

      case n =>
        println(s"lookup of ${n.name} in" + sequentialDefenv(n).map(_.keySet))
        lookup(sequentialDefenv(n), serialize(n), UnknownEntity())
    }

}
