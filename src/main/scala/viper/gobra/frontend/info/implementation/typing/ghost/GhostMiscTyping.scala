package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{GhostTypeMember, MPredicateImpl, MPredicateSpec}
import viper.gobra.frontend.info.base.Type.{FunctionT, PredicateInstance, Type, BooleanT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case PBoundVariable(_, _) => noMessages
    case PTrigger(exprs) => exprs.flatMap(isPureExpr)
    case PExplicitGhostParameter(_) => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case PBoundVariable(_, typ) => typeType(typ)
    case PTrigger(_) => BooleanT
    case PExplicitGhostParameter(param) => miscType(param)
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl) => FunctionT(decl.args map miscType, PredicateInstance)
    case MPredicateSpec(decl) => FunctionT(decl.args map miscType, PredicateInstance)
    case _: SymbolTable.GhostStructMember => ???
  }

}
