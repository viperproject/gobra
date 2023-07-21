package viper.gobra.frontend.info.implementation.typing.modifiers
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend.{PExpression, PIdnNode, PInterfaceClause, PMember, PMisc, PNode, PParameter, PStatement, PStructClause, PType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostAssignability, GhostTyping, GhostWellDef}
import viper.gobra.util.{Memoization, Safety}

class GhostModifierUnit(final val ctx: TypeInfoImpl) extends Attribution with ModifierUnit[GhostModifier]
  with GhostWellDef
  with GhostTyping
  with GhostAssignability
{
  override def hasWellDefModifier: WellDefinedness[PNode] = createIndependentWellDef[PNode] {
    case m: PMember => memberGhostSeparation(m)
    case s: PStatement => stmtGhostSeparation(s)
    case e: PExpression => exprGhostSeparation(e)
    case t: PType => typeGhostSeparation(t)
    case m: PMisc => miscGhostSeparation(m)
  }(n => ctx.children(n).forall(selfWellGhostSeparated))

  private def selfWellGhostSeparated(n: PNode): Boolean = n match {
    case i: PIdnNode => idGhostSeparation(i).valid
    case n => hasWellDefModifier.valid(n)
  }

  override def getModifier: ModifierTyping[PNode, GhostModifier] = createModifier[PNode, GhostModifier](
    n => if (n match {
      case m: PMember => isMemberGhost(m)
      case s: PStatement => isStmtGhost(s)
      case e: PExpression => exprGhostTyping(e).isGhost
      case t: PType => isTypeGhost(t)
      case i: PIdnNode => isIdGhost(i)
      case p: PParameter => isParamGhost(p)
      case s: PStructClause => isStructClauseGhost(s)
      case i: PInterfaceClause => isInterfaceClauseGhost(i)
    }) GhostModifier.Ghost else GhostModifier.Actual
  )(hasWellDefModifier)
}
