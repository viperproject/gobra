// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, collectMessages, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{MethodImpl, MethodSpec}
import viper.gobra.frontend.info.base.Type.{InterfaceT, Type}

trait Errors { this: TypeInfoImpl =>

  lazy val (errors: Messages, missingImplProofs: Vector[(Type, InterfaceT, MethodImpl, MethodSpec)]) =
    {
      val partialRes = collectMessages(tree) { case m: PNode =>

        val wellDef = m match {
          case n: PImport => wellDefImport(n).out
          case n: PMember   => wellDefMember(n).out
          case n: PStatement  => wellDefStmt(n).out
          case n: PExpressionAndType => wellDefExprAndType(n).out
          case n: PExpression => wellDefExpr(n).out
          case n: PType       => wellDefType(n).out
          case n: PIdnNode    => wellDefID(n).out
          //        case n: PIdnDef     => wellDefID(n).out
          //        case n: PIdnUnk if isDef(n) => wellDefID(n).out
          case n: PMisc       => wellDefMisc(n).out
          case n: PSpecification => wellDefSpec(n).out
          case _ => noMessages
        }

        val ghostSeparated = wellGhostSeparated(m).out

        wellDef ++ ghostSeparated
      }

      if (partialRes.isEmpty) {
        wellImplementationProofs match {
          case Left(msgs) => (msgs, Vector.empty)
          case Right(missing) => (Vector.empty, missing)
        }
      } else (partialRes, Vector.empty)
    }
}
