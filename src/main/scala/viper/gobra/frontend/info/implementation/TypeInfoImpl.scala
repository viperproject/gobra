package viper.gobra.frontend.info.implementation

import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.implementation.property._
import viper.gobra.frontend.info.implementation.resolution.{AmbiguityResolution, Enclosing, MemberResolution, NameResolution}
import viper.gobra.frontend.info.implementation.typing._
import viper.gobra.frontend.info.implementation.typing.ghost._
import viper.gobra.frontend.info.{Info, TypeInfo}

class TypeInfoImpl(final val tree: Info.GoTree) extends Attribution with TypeInfo

  with NameResolution
  with MemberResolution
  with AmbiguityResolution
  with Enclosing

  with MemberTyping
  with StmtTyping
  with ExprTyping
  with TypeTyping
  with IdTyping
  with MiscTyping

  with AssertionTyping
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

  with Errors
{
  import viper.gobra.util.Violation._

  import org.bitbucket.inkytonik.kiama.attribution.Decorators
  protected val decorators = new Decorators(tree)

  override def typ(expr: PExpression): Type.Type = exprType(expr)

  override def typ(typ: PType): Type.Type = typeType(typ)

  override def typ(id: PIdnNode): Type.Type = idType(id)

  override def scope(n: PIdnNode): PScope = enclosingIdScope(n)

  lazy val hasAddressedUse: PIdnNode => Boolean =
    attr[PIdnNode, Boolean] { id =>
      uses(id) exists isAddressedUse
    }

  lazy val isAddressedUse: PIdnUse => Boolean =
    attr[PIdnUse, Boolean] {
      case tree.parent(tree.parent(_: PReference)) => true
      case id => enclosingIdCodeRoot(id) match {
        case f: PFunctionLit if !containedIn(enclosingIdScope(id), f) => true
        case _ => false
      }
    }



  override def addressed(id: PIdnNode): Boolean = hasAddressedUse(id)

  override def regular(n: PIdnNode): SymbolTable.Regular = entity(n) match {
    case r: Regular => r
    case _ => violation("found non-regular entity")
  }

  private lazy val variablesMap: Map[PScope, Vector[PIdnNode]] = {
    val ids: Vector[PIdnNode] = tree.nodes collect {
      case id: PIdnDef              => id
      case id: PIdnUnk if isDef(id) => id
    }

    ids.groupBy(enclosingIdScope)
  }

  override def variables(s: PScope): Vector[PIdnNode] = variablesMap(s)


  private lazy val usesMap: Map[UniqueRegular, Vector[PIdnUse]] = {
    val ids: Vector[PIdnUse] = tree.nodes collect {case id: PIdnUse if uniqueRegular(id).isDefined => id }
    ids.groupBy(uniqueRegular(_).get)
  }

  def uses(id: PIdnNode): Vector[PIdnUse] = {
    uniqueRegular(id).fold(Vector.empty[PIdnUse])(usesMap)
  }


  case class UniqueRegular(r: Regular, s: PScope)

  def uniqueRegular(id: PIdnNode): Option[UniqueRegular] = entity(id) match {
    case r: Regular => Some(UniqueRegular(r, enclosingIdScope(id)))
    case _ => None
  }
}

