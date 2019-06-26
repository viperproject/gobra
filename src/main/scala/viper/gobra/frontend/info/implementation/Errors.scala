package viper.gobra.frontend.info.implementation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, collectMessages}
import viper.gobra.ast.frontend._

trait Errors { this: TypeInfoImpl =>

  lazy val errors: Messages =
    collectMessages(tree) {
      case n: PMember   => wellDefMember(n).out
      case n: PStatement  => wellDefStmt(n).out ++ wellDefStmtGhostSeparated(n).out
      case n: PExpression => wellDefExpr(n).out
      case n: PType       => wellDefType(n).out
      case n: PIdnNode    => wellDefID(n).out
      //        case n: PIdnDef     => wellDefID(n).out
      //        case n: PIdnUnk if isDef(n) => wellDefID(n).out
      case n: PMisc       => wellDefMisc(n).out
      case n: PAssertion  => wellDefAssertion(n).out
    }
}
