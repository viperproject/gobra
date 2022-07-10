// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Constants

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl =>
      wellDefVariadicArgs(n.args) ++ wellDefIfPureFunction(n) ++ wellDefIfInitBlock(n) ++ wellDefIfMain(n)
    case m: PMethodDecl =>
      wellDefVariadicArgs(m.args) ++ isReceiverType.errors(miscType(m.receiver))(member) ++ wellDefIfPureMethod(m)
    case b: PConstDecl =>
      b.specs.flatMap(wellDefConstSpec)
    case g: PGlobalVarDecl =>
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
    val errorMsgEmptySpec =
      s"Currently, ${Constants.INIT_FUNC_NAME} blocks cannot be specified. Instead, use package postconditions and import preconditions."
    val errorMsgNoInOut = s"func ${Constants.INIT_FUNC_NAME} must have no arguments and no return values"
    val errorMsgNotAbstract = s"func ${Constants.INIT_FUNC_NAME} cannot be abstract"
    val isInitFunc = n.id.name == Constants.INIT_FUNC_NAME
    val noInputsAndOutputs = n.args.isEmpty && n.result.outs.isEmpty
    val notAbstract = n.body.nonEmpty
    val hasEmptySpec = !n.spec.isPure &&
      !n.spec.isTrusted &&
      n.spec.pres.isEmpty &&
      n.spec.preserves.isEmpty &&
      n.spec.posts.isEmpty &&
      n.spec.terminationMeasures.isEmpty
    error(n, errorMsgEmptySpec, isInitFunc && !hasEmptySpec) ++
      error(n, errorMsgNoInOut, isInitFunc && !noInputsAndOutputs) ++
      error(n, errorMsgNotAbstract, isInitFunc && !notAbstract)
  }

  private def wellDefIfMain(n: PFunctionDecl): Messages = {
    // same message as the Go compiler
    val errorMsg = s"func ${Constants.MAIN_FUNC_NAME} must have no arguments and no return values"
    val isMainFunc = n.id.name == Constants.MAIN_FUNC_NAME
    // TODO: add support for ghost out-params
    val noInputsAndOutputs = n.args.isEmpty && n.result.outs.isEmpty
    error(n, errorMsg, isMainFunc && !noInputsAndOutputs)
  }
}
