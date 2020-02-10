package viper.gobra.frontend.info.implementation

import com.typesafe.scalalogging.StrictLogging
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.implementation.property._
import viper.gobra.frontend.info.implementation.resolution.{AmbiguityResolution, AstPattern, Enclosing, MemberResolution, NameResolution}
import viper.gobra.frontend.info.implementation.typing._
import viper.gobra.frontend.info.implementation.typing.ghost._
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostSeparation
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

  with AssertionExprTyping
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
  with TypeMerging

  with Errors
  with StrictLogging
{
  import viper.gobra.util.Violation._

  import org.bitbucket.inkytonik.kiama.attribution.Decorators
  protected val decorators = new Decorators(tree)

  override def typ(expr: PExpression): Type.Type = exprType(expr)

  override def typ(misc: PMisc): Type.Type = miscType(misc)

  override def typ(typ: PType): Type.Type = typeType(typ)

  override def typ(id: PIdnNode): Type.Type = idType(id)

  override def scope(n: PIdnNode): PScope = enclosingIdScope(n)

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

  override def variables(s: PScope): Vector[PIdnNode] = variablesMap.getOrElse(s, Vector.empty)


  private lazy val usesMap: Map[UniqueRegular, Vector[PIdnUse]] = {
    val ids: Vector[PIdnUse] = tree.nodes collect {case id: PIdnUse if uniqueRegular(id).isDefined => id }
    ids.groupBy(uniqueRegular(_).get)
  }

  def uses(id: PIdnNode): Vector[PIdnUse] = {
    uniqueRegular(id).fold(Vector.empty[PIdnUse])(r => usesMap.getOrElse(r, Vector.empty))
  }


  case class UniqueRegular(r: Regular, s: PScope)

  def uniqueRegular(id: PIdnNode): Option[UniqueRegular] = entity(id) match {
    case r: Regular => Some(UniqueRegular(r, enclosingIdScope(id)))
    case _ => None
  }

  override def resolve(d: PDot): Option[AstPattern with AstPattern.EntityLike] = resolveDot(d)
  override def resolve(i: PInvoke): Option[AstPattern] = resolveInvoke(i)
  override def isAssertion(e: PExpression): Boolean = isAssertionProperty(e)
  override def isExpression(e: PExpression): Boolean = isExpressionProperty(e)
}

