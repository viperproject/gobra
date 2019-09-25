package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{GhostTypeMember, MPredicateImpl, MPredicateSpec}
import viper.gobra.frontend.info.base.Type.{BooleanT, FunctionT, PredicateInstance, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case n@ PBoundVariable(id, typ) => noMessages
    case n@ PTrigger(exprs) => exprs.flatMap(isPureExpr)
    case n@ PExplicitGhostParameter(param) => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case n@ PBoundVariable(id, typ) => typeType(typ)
    case n@ PTrigger(exprs) => BooleanT
    case n@ PExplicitGhostParameter(param) => miscType(param)
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl) => FunctionT(decl.args map miscType, PredicateInstance)
    case MPredicateSpec(decl) => FunctionT(decl.args map miscType, PredicateInstance)
    case member: SymbolTable.GhostStructMember => ???
  }

}
