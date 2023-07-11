package viper.gobra.frontend.info.implementation.typing.modifiers
import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.ast.frontend.{PExpression, PIdnNode, PMember, PMisc, PNode, PStatement, PType}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostTyping, GhostWellDef}
import viper.gobra.util.{Memoization, Safety}

object GhostModifierUnit extends ModifierUnit[GhostModifier] with GhostWellDef with GhostTyping {
  override def hasWellDefModifier(ctx: TypeInfoImpl): WellDefinedness[PNode] = createIndependentWellDef[PNode] {
    case m: PMember => memberGhostSeparation(ctx, m)
    case s: PStatement => stmtGhostSeparation(ctx, s)
    case e: PExpression => exprGhostSeparation(ctx, e)
    case t: PType => typeGhostSeparation(ctx, t)
    case m: PMisc => miscGhostSeparation(ctx, m)
  }(n => ctx.children(n).forall(selfWellGhostSeparated(ctx, _)))

  private def selfWellGhostSeparated(ctx: TypeInfoImpl, n: PNode): Boolean = n match {
    case i: PIdnNode => idGhostSeparation(ctx)(i).valid
    case n => hasWellDefModifier(ctx).valid(n)
  }

  override def getModifier(ctx: TypeInfoImpl): ModifierTyping[PNode, GhostModifier] = createModifier[PNode, GhostModifier] {
    case m: PMember => ghostMemberClassification(member)
    case s: PStatement => ghostStmtClassification(stmt)
    case e: PExpression => ghostExprTyping(expr)
    case t: PType => ghostTypeClassification(typ)
    case i: PIdnNode => ghostIdClassification(id)
    case p: PParameter => ghostParameterClassification(param)
    case s: PStructClause =>
  }(hasWellDefModifier(ctx))
}
