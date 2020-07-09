package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.util.Position
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait LabelResolution { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.{Entity, UnknownEntity}
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._


  private[resolution] lazy val defLabel: PDefLikeLabel => Entity =
    attr[PDefLikeLabel, Entity] {
      case id@ tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {
          case decl: PLabeledStmt => Label(decl, isGhost)
          case _ => violation("unexpected parent of label")
        }
    }

  private def serialize(id: PLabelNode): String = id.name

  /**
    * Labels have their own namespace.
    * The scope of a label is the body of the function in which it is declared.
    * This excludes the body of nested functions. [from the Go language specification]
    */
  private lazy val labelDefEnv: PLabelNode => Environment = {
    lazy val sequentialLabelDefEnv: Chain[Environment] =
    chain(defenvin, defenvout)

    def defenvin(in: PNode => Environment): PNode ==> Environment = {
      case _: PMethodDecl | _: PFunctionDecl | _: PFunctionLit => rootenv()
    }

    def defenvout(out: PNode => Environment): PNode ==> Environment = {
      case id: PLabelDef => defineIfNew(out(id), serialize(id), defLabel(id))
    }

    down(
      (_: PNode) => violation("Label does not root in a method or function")
    ) {
      case m@ (_: PMethodDecl | _: PFunctionDecl | _: PFunctionLit) => sequentialLabelDefEnv(m)
    }
  }

  /** returns the label definition for a label use. */
  lazy val label: PLabelNode => Entity =
    attr[PLabelNode, Entity] {
      n => lookup(labelDefEnv(n), serialize(n), UnknownEntity())
    }


  private lazy val labelDefToUsesMap: Map[UniqueLabel, Vector[PLabelUse]] = {
    val labelUses: Vector[PLabelUse] = tree.nodes collect {
      case labelUse: PLabelUse if uniqueLabel(labelUse).isDefined => labelUse
    }
    labelUses.groupBy(uniqueLabel(_).get)
  }

  /** returns all uses for label definition. */
  def labelUses(l: PLabelDef): Vector[PLabelUse] = {
    uniqueLabel(l).fold(Vector.empty[PLabelUse])(r => labelDefToUsesMap.getOrElse(r, Vector.empty))
  }

  /** returns all variables that are evaluated with the argument label. */
  lazy val labeledVarUsesAttr: PLabelDef => Vector[PIdnUse] = {

    // returns variable uses contained in subtree starting from argument node.
    // excludes uses in nested old or now expressions.
    lazy val usesInOldScopeInSubtree: PNode => Vector[PIdnUse] =
      attr[PNode, Vector[PIdnUse]] {
        case n: PIdnUse => if (entity(n).isInstanceOf[Variable]) Vector(n) else Vector.empty
        case _: POld | _: PLabelledOld | _: PNow => Vector.empty
        case n => tree.child(n).flatMap(c => usesInOldScopeInSubtree(c))
      }

    attr[PLabelDef, Vector[PIdnUse]] { n =>
      labelUses(n).flatMap{
        case tree.parent(old: PLabelledOld) => usesInOldScopeInSubtree(old.operand)
        case _ => violation("encountered unexpected label use")
      }
    }
  }

  override def labeledVarUses(l: PLabelDef): Vector[PIdnUse] = labeledVarUsesAttr(l)


  case class UniqueLabel(r: Label, p: Position)

  def uniqueLabel(id: PLabelNode): Option[UniqueLabel] = label(id) match {
    case l: Label => Some(UniqueLabel(l, tree.root.positions.positions.getStart(l.decl).get))
    case _ => None
  }
}
