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
import viper.gobra.util.GobraExecutionContext

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait Errors { this: TypeInfoImpl =>

  private def checkNode(m: PNode): Messages = {
    val wellDef = m match {
      case n: PProgram => wellDefProgram(n).out
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
      case n: PLabelNode => wellDefLabel(n).out
      case _ => noMessages
    }

    val ghostSeparated = wellGhostSeparated(m).out

    wellDef ++ ghostSeparated
  }

  private def checkNodeParallel(m: PNode)(executionContext: GobraExecutionContext): Future[Messages] =
    // Future{ checkNode(m) }(executionContext)
  m match {
    /*case _: PProgram => Future{
      print(s"start $m")
      val res = checkNode(m)
      print(s"$m is done")
      res
    }(executionContext)*/
    case _ => Future.successful(checkNode(m))
  }

  lazy val (errors: Messages, missingImplProofs: Vector[(Type, InterfaceT, MethodImpl, MethodSpec)]) =
    {
      val parallel = false

      val partialRes = if (parallel) {
        val futs = tree.nodes.map(checkNodeParallel(_)(executionContext))
        implicit val executor: GobraExecutionContext = executionContext
        Await.result(Future.sequence(futs), Duration.Inf).flatten
      } else {
        collectMessages(tree) { case m: PNode => checkNode(m) }
        tree.nodes.flatMap(checkNode)
      }
      /*
      var durationMs = 0L
      val partialRes = collectMessages(tree) { case m: PNode =>
        val startMs = System.currentTimeMillis()
        val wellDef = m match {
          case n: PProgram => wellDefProgram(n).out
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
          case n: PLabelNode => wellDefLabel(n).out
          case _ => noMessages
        }

        val ghostSeparated = wellGhostSeparated(m).out

        val res = wellDef ++ ghostSeparated
        durationMs += System.currentTimeMillis() - startMs
        res
      }
      val durationS = f"${durationMs / 1000f}%.1f"
      println(s"collecting messages for ${tree.root.packageClause.id} took ${durationS}s")
       */

      if (partialRes.isEmpty) {
        wellImplementationProofs match {
          case Left(msgs) => (msgs, Vector.empty)
          case Right(missing) => (Vector.empty, missing)
        }
      } else (partialRes, Vector.empty)
    }
}
