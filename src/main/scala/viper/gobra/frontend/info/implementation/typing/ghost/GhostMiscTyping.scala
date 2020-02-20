package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable
import viper.gobra.frontend.info.base.SymbolTable.{GhostTypeMember, MPredicateImpl, MPredicateSpec}
import viper.gobra.frontend.info.base.Type.{AssertionT, FunctionT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping

trait GhostMiscTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostMisc(misc: PGhostMisc) = misc match {
    case n@ PExplicitGhostParameter(param) => noMessages
  }

  private[typing] def ghostMiscType(misc: PGhostMisc): Type = misc match {
    case n@ PExplicitGhostParameter(param) => miscType(param)
  }

  private[typing] def ghostMemberType(typeMember: GhostTypeMember): Type = typeMember match {
    case MPredicateImpl(decl) => FunctionT(decl.args map miscType, AssertionT)
    case MPredicateSpec(decl) => FunctionT(decl.args map miscType, AssertionT)
    case member: SymbolTable.GhostStructMember => ???
  }

  implicit lazy val wellDefSpec: WellDefinedness[PSpecification] = createWellDef {
    case n@ PFunctionSpec(pres, posts, _) =>
      pres.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
        posts.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n)) ++
      pres.flatMap(e => allChildren(e).flatMap(illegalPreconditionNode))

    case n@ PLoopSpec(invariants) =>
      invariants.flatMap(p => assignableTo.errors(exprType(p), AssertionT)(n))
  }

  private def illegalPreconditionNode(n: PNode): Messages = {
    n match {
      case n: POld => message(n, s"old not permitted in precondition")
      case _ => noMessages
    }
  }

}
