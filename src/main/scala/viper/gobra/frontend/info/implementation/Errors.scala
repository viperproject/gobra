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

  /** Returns the closest enclosing member of `n` (or `n` itself, if it is a member). */
  private lazy val enclosingMemberOfNode: PNode => Option[PMember] =
    attr[PNode, Option[PMember]] {
      case m: PMember => Some(m)
      case n => tree.parent(n).headOption.flatMap(enclosingMemberOfNode)
    }

  /**
    * In imported packages, the well-definedness of non-exported functions, methods, and
    * predicates (and of all nodes they contain) is not checked: the package under verification
    * cannot reference these members, they are not encoded, and they are checked when the
    * imported package itself is verified. Note that non-exported types are still checked, as
    * they may occur in the signatures of exported members.
    */
  private def skipWellDefinedness(n: PNode): Boolean =
    !isMainContext && (enclosingMemberOfNode(n) match {
      case Some(d: PFunctionDecl) => !isExportedName(d.id.name)
      case Some(d: PMethodDecl) => !isExportedName(d.id.name)
      case Some(d: PFPredicateDecl) => !isExportedName(d.id.name)
      case Some(d: PMPredicateDecl) => !isExportedName(d.id.name)
      case _ => false
    })

  lazy val (errors: Messages, missingImplProofs: Vector[(Type, InterfaceT, MethodImpl, MethodSpec)]) =
    {
      val partialRes = collectMessages(tree) { case m: PNode if !skipWellDefinedness(m) =>

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

      if (partialRes.isEmpty) {
        wellImplementationProofs match {
          case Left(msgs) => (msgs, Vector.empty)
          case Right(missing) => (Vector.empty, missing)
        }
      } else (partialRes, Vector.empty)
    }
}
