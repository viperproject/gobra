package viper.gobra.frontend.info.implementation.typing.modifiers.ghost

import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.modifiers.ModifierUnit

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
