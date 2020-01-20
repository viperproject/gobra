package viper.gobra.frontend.info.implementation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, collectMessages, noMessages}
import viper.gobra.ast.frontend._

trait Errors { this: TypeInfoImpl =>

  lazy val errors: Messages =
    collectMessages(tree) { case m: PNode =>

      val wellDef = m match {
        case n: PMember   => wellDefMember(n).out
        case n: PStatement  => wellDefStmt(n).out
        case n: PExpression => wellDefExpr(n).out
        case n: PType       => wellDefType(n).out
        case n: PIdnNode    => wellDefID(n).out
        //        case n: PIdnDef     => wellDefID(n).out
        //        case n: PIdnUnk if isDef(n) => wellDefID(n).out
        case n: PMisc       => wellDefMisc(n).out
        case _ => noMessages
      }


      val ghostSeparated = wellGhostSeparated(m).out

      wellDef ++ ghostSeparated
    }
}
