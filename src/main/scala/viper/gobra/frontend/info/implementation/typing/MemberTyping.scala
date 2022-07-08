// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl =>
      wellDefVariadicArgs(n.args) ++ wellDefIfPureFunction(n) ++ wellDefIfInitBlock(n)
    case m: PMethodDecl =>
      wellDefVariadicArgs(m.args) ++ isReceiverType.errors(miscType(m.receiver))(member) ++ wellDefIfPureMethod(m)
    case b: PConstDecl =>
      b.specs.flatMap(wellDefConstSpec)
    case g: PGlobalVarDecl =>
      // TODO: check no dynamically bound method calls
      // HACK: without this explicit check, Gobra does not find repeated declarations
      //       of global variables. This has to do with the changes introduced in PR #186.
      val idsOkMsgs = g.left.flatMap(l => wellDefID(l).out)
      if (idsOkMsgs.isEmpty) {
        g.right.flatMap(isExpr(_).out) ++
          declarableTo.errors(g.right map exprType, g.typ map typeSymbType, g.left map idType)(g) ++
          acyclicGlobalDeclaration.errors(g)(g)
      } else {
        idsOkMsgs
      }
    case s: PActualStatement =>
      wellDefStmt(s).out
  }

  private def wellDefConstSpec(spec: PConstSpec): Messages = {
    val hasInitExpr = error(spec, s"missing init expr for ${spec.left}", spec.right.isEmpty)
    lazy val canAssignInitExpr = error(
      spec,
      s"${spec.right} cannot be assigned to ${spec.left}",
      !multiAssignableTo(spec.left.map(typ), spec.right.map(typ))
    )
    lazy val constExprMsgs = spec.right.flatMap(wellDefIfConstExpr)
    // helps producing less redundant error messages
    if (hasInitExpr.nonEmpty) hasInitExpr else if (canAssignInitExpr.nonEmpty) canAssignInitExpr else constExprMsgs
  }

  private[typing] def wellDefVariadicArgs(args: Vector[PParameter]): Messages =
    args.dropRight(1).flatMap {
      p => error(p, s"Only the last argument can be variadic, got $p instead", p.typ.isInstanceOf[PVariadicType])
    }

  private def wellDefIfInitBlock(n: PFunctionDecl): Messages = {
    val errorMsg =
      "Currently, 'init' blocks cannot be specified. Instead, use package postconditions and import preconditions."
    val hasEmptySpec = !n.spec.isPure &&
      !n.spec.isTrusted &&
      n.spec.pres.isEmpty &&
      n.spec.preserves.isEmpty &&
      n.spec.posts.isEmpty &&
      n.spec.terminationMeasures.isEmpty
    val isInitFunction = n.id.name == "init"
    error(n, errorMsg, !hasEmptySpec && isInitFunction)
  }
}
