// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, warning, noMessages}
import viper.gobra.GoVerifier
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.base.SymbolTable.MPredicateSpec
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Constants

trait MemberTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefMember: WellDefinedness[PMember] = createWellDef {
    case member: PActualMember => wellDefActualMember(member)
    case member: PGhostMember  => wellDefGhostMember(member)
  }

  // Find all unnamed parameters
  private def unnamedReturnParameters(n: PFunctionDecl): Messages = {
    n.result.outs.collect {
      case p: PUnnamedParameter => p
    }.flatMap { p =>
      warning(p,
        s"Output parameter not constrained by the function postcondition.")
    }
  }

  // Find all named parameters that do not appear in the postcondition
  private def unconstrainedReturnParameters(n: PFunctionDecl): Messages = {
    val parameterNames = n.spec.posts.flatMap {
      allChildren(_).collect {
        case PNamedOperand(id) => id.name
      }
    }
    n.result.outs.collect {
      case p: PNamedParameter if !parameterNames.contains(p.id.name) => p
    }.flatMap { p =>
      warning(p,
        s"Output parameter '${p.id.name}' does not appear in function" +
          s" '${n.id.name}' postcondition, and might thus be unconstrained.")
    }
  }

  private def findReferencesOutsideOld(expr: PExpression): Vector[PNamedOperand] = expr match {
    case POld(_) => Vector.empty
    case PAccess(_, _) => Vector.empty
    case p @ PNamedOperand(_) => Vector(p)
    case _ =>
      children(expr).collect { case e: PExpression => e }
        .flatMap(e => findReferencesOutsideOld(e))
  }

  private def isVariableFixed(p: PExpression): Boolean = {
    p match {
      case o: PNamedOperand => true
      case ...
    }
  }

  // Find all wildcards perms in postcondition
  private def wildcardsPerm(n: PFunctionDecl): Messages = {
    // 1. We have a wildcard pointer access in the precond.
    // FIXME: collect first expressions, then filter for the ones that vary over time
    val extractId: PartialFunction[PExpression, PExpression] = {
      // FIXME: should be conditional on whether p is fixed or not
      case PAccess(p, PWildcardPerm()) if isVariableFixed(p) => p
      // case PAccess(p @ PNamedOperand(_), PWildcardPerm())         => p
      //case _ @ PAccess(PReference(p), PWildcardPerm()) => p
      // FIXME: &s.f is PBoolLit and PDot, should I check for this?
    }
    // FIXME: ...
    val presParamsWithWildcardAccess =
      n.spec.pres.collect {
          extractId
      }
    // println(paramsWithWildcardAccess.map(_.getClass.getName).mkString(", "))
    // 2. we have the wc acc to the same pointer in the postcond.
    val postsParamsWithWildcardAcc =
      n.spec.posts.collect {
        extractId
      }.filter { p : PNamedOperand =>
        presParamsWithWildcardAccess.map(_.id.name).contains(p.id.name)
      }
    // 3. there is no occurrence of x outside an old in the postcond. (occurrence: not a dereference)
    val outSideOldOccurrencesInPost = n.spec.posts.flatMap(findReferencesOutsideOld)
    postsParamsWithWildcardAcc.filter { p : PNamedOperand =>
      !outSideOldOccurrencesInPost.map(_.id.name).contains(p.id.name)
    }.flatMap { p =>
      warning(p,
        "Wildcard permission likely to be wrong.")
    }
  }

  private[typing] def wellDefActualMember(member: PActualMember): Messages = member match {
    case n: PFunctionDecl =>
      wellDefVariadicArgs(n.args) ++
        wellDefIfPureFunction(n) ++
        wellDefIfInitBlock(n) ++
        wellDefIfMain(n) ++
        wellFoundedIfNeeded(n) ++
        unnamedReturnParameters(n) ++
        unconstrainedReturnParameters(n) ++
        wildcardsPerm(n)
    case m: PMethodDecl =>
      wellDefVariadicArgs(m.args) ++
        isReceiverType.errors(miscType(m.receiver))(member) ++
        wellDefIfPureMethod(m) ++
        wellFoundedIfNeeded(m)
    case b: PConstDecl =>
      b.specs.flatMap(wellDefConstSpec)
    case g: PVarDecl if isGlobalVarDeclaration(g) =>
      if (config.enableLazyImports) {
        error(g, s"Global variables are not allowed when executing ${GoVerifier.name} with ${Config.enableLazyImportOptionPrettyPrinted}")
      } else {
        // HACK: without this explicit check, Gobra does not find repeated declarations
        //       of global variables. This has to do with the changes introduced in PR #186.
        //       We need this check nonetheless because the checks performed in the "true" branch
        //       assume that the ids are well-defined.
        val idsOkMsgs = g.left.flatMap(l => wellDefID(l).out)
        if (idsOkMsgs.isEmpty) {
          val isGhost = isEnclosingGhost(g)
          g.right.flatMap(isExpr(_).out) ++
            declarableTo.errors(g.right map exprType, g.typ map typeSymbType, g.left map idType)(g) ++
            error(g, s"Currently, global variables cannot be made ghost", isGhost) ++
            acyclicGlobalDeclaration.errors(g)(g)
        } else {
          idsOkMsgs
        }
      }
    case s: PActualStatement =>
      wellDefStmt(s).out

    case ip: PImplementationProof =>
      val subType = symbType(ip.subT)
      val superType = symbType(ip.superT)

      val syntaxImplementsMsgs = syntaxImplements(subType, superType).asReason(ip, s"${ip.subT} does not implement the interface ${ip.superT}")
      if (syntaxImplementsMsgs.nonEmpty) syntaxImplementsMsgs
      else {
        addDemandedImplements(subType, superType)

        {
          val badReceiverTypes = ip.memberProofs.map(m => miscType(m.receiver))
            .filter(t => !identicalTypes(t, subType))
          error(ip, s"The receiver of all methods included in the implementation proof must be $subType, " +
            s"but encountered: ${badReceiverTypes.distinct.mkString(", ")}", cond = badReceiverTypes.nonEmpty)
        } ++ {
          val superPredNames = memberSet(superType).collect { case (n, m: MPredicateSpec) => (n, m) }
          val allPredicatesDefined = PropertyResult.bigAnd(superPredNames.map { case (name, symb) =>
            val valid = tryMethodLikeLookup(subType, PIdnUse(name)).isDefined ||
              ip.alias.exists(al => al.left.name == name)
            failedProp({
              val argTypes = symb.args map symb.context.typ

              s"predicate $name is not defined for type $subType. " +
                s"Either declare a predicate 'pred ($subType) $name(${argTypes.mkString(", ")})' " +
                s"or declare a predicate 'pred p($subType${if (argTypes.isEmpty) "" else ", "}${argTypes.mkString(", ")})' with some name p and add 'pred $name := p' to the implementation proof."
            }, !valid)
          })
          allPredicatesDefined.asReason(ip, "Some predicate definitions are missing")
        }
      }
  }

  private def wellDefConstSpec(spec: PConstSpec): Messages = {
    val hasInitExpr = error(spec, s"missing init expr for ${spec.left}", spec.right.isEmpty)
    lazy val canAssignInitExpr = error(
      spec,
      s"${spec.right} cannot be assigned to ${spec.left}",
      // Assignability in Go is a property between a value and and a type. In Gobra, we model this as a relation
      // between two types, which is less precise. Because of this limitation, and with the goal of handling
      // untyped literals, we introduce an extra condition here. This makes the type checker of Gobra accept Go
      // expressions that are not accepted by the compiler.
      !(multiAssignableTo(spec.left.map(typ), spec.right.map(typ)) ||
        multiAssignableTo(spec.left.map(n => underlyingType(typ(n))), spec.right.map(typ)))
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
    val isInitFunc = n.id.name == Constants.INIT_FUNC_NAME
    if (isInitFunc && config.enableLazyImports) {
      error(n, s"Init functions are not supported when executing ${GoVerifier.name} with ${Config.enableLazyImportOptionPrettyPrinted}")
    } else if (isInitFunc) {
      val errorMsgEmptySpec =
        s"Currently, ${Constants.INIT_FUNC_NAME} blocks cannot have specification. Instead, use package postconditions and import preconditions."
      val errorMsgNoInOut = s"func ${Constants.INIT_FUNC_NAME} must have no arguments and no return values"
      val errorMsgGhost = s"func ${Constants.INIT_FUNC_NAME} cannot be ghost"
      val isGhost = isEnclosingGhost(n)
      val noInputsAndOutputs = n.args.isEmpty && n.result.outs.isEmpty
      val hasEmptySpec = !n.spec.isPure &&
        !n.spec.isTrusted &&
        n.spec.pres.isEmpty &&
        n.spec.preserves.isEmpty &&
        n.spec.posts.isEmpty &&
        n.spec.terminationMeasures.isEmpty
      error(n, errorMsgEmptySpec, !hasEmptySpec) ++
        error(n, errorMsgNoInOut, !noInputsAndOutputs) ++
        error(n, errorMsgGhost, isGhost)
    } else {
      noMessages
    }
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
