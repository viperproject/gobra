// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.util.Violation.violation
import viper.gobra.util.{Computation, Violation}

trait ProgramTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefProgram: WellDefinedness[PProgram] = createWellDef {
    case PProgram(_, _, _, friends, _) if !config.enableExperimentalFriendClauses && friends.nonEmpty =>
      error(friends.head, "Usage of experimental 'friendPkg' clauses is disallowed by default. " +
        "Pass the flag --experimentalFriendClauses to allow it. This feature may change in the future.")
    case prog@PProgram(_, pkgInvs,  _, friends, _) =>
      // Obtains global variable declarations sorted by the order in which they appear in the file
      val sortedByPosDecls: Vector[PVarDecl] = globalVarDeclsSortedByPos(prog)
      // HACK: without this explicit check, Gobra does not find repeated declarations
      //       of global variables. This has to do with the changes introduced in PR #186.
      //       We need this check nonetheless because the checks performed in the "true" branch
      //       assume that the ids are well-defined.
      val idsOkMsgs = sortedByPosDecls.flatMap(d => d.left).flatMap(l => wellDefID(l).out)
      if (idsOkMsgs.isEmpty) {
        val globalDeclsInRightOrder = globalDeclSatisfiesDepOrder(sortedByPosDecls)
        val noOldExprs =
          hasOldExpression(pkgInvs.map(_.inv)) ++
            hasOldExpression(friends.map(_.assertion))
        globalDeclsInRightOrder ++ noOldExprs
      } else {
        idsOkMsgs
      }
  }

  /**
    * Currently, Gobra requires that global variables are declared in an order such that all dependencies of a global
    * variable are declared before it. In practice, it rules out declarations like the following:
    *     var B int = A
    *     var A int
    * Instead, one would need to write it in the following order:
    *     var A int
    *     var B int = A
    * This is not limiting, as an order can always be found such that the declaration order is compatible
    * with the dependency relation.
    * TODO: this is meant to be a temporary limitation that should be eventually
    *      replaced by a check that declarations are acyclic (there is already
    *      a check for acyclicity implemented in [[DependencyAnalysis]]).
    */
  private def globalDeclSatisfiesDepOrder(globalDeclsInPosOrder: Vector[PVarDecl]): Messages = {
    var visitedGlobals: Set[Entity] = Set.empty[Entity]
    globalDeclsInPosOrder.flatMap{ decl =>
      decl.left.zipWithIndex.flatMap{ case (id, idx) =>
        val visitedAllDeps: Boolean = regular(id) match {
          case g: st.GlobalVariable =>
            val result = samePkgDepsOfGlobalVar(g).forall(visitedGlobals.contains)
            visitedGlobals = visitedGlobals + g
            result
          case _: st.Wildcard if decl.right.isEmpty => true
          case _: st.Wildcard =>
            val errOrDeps = StrictAssignMode(decl.left.length, decl.right.length) match {
              case AssignMode.Single => globalsExprDependsOn(decl.right(idx))
              case AssignMode.Multi => globalsExprDependsOn(decl.right.head)
              case m => Violation.violation(s"Expected this case to be unreachable, but got $m instead.")
            }
            errOrDeps match {
              case Right(l) => l.forall(visitedGlobals.contains)
              case _ => true
            }
          case e =>
            Violation.violation(s"Expected this case to be unreachable, but resolved $id as a $e.")
        }
        val errorMsg: String = "Currently, Gobra requires dependencies of a global variable to be declared before it."
        error(decl, errorMsg, !visitedAllDeps)
      }
    }
  }

  private val globalVarDeclsSortedByPos: PProgram => Vector[PVarDecl] = {
    val computation: PProgram  => Vector[PVarDecl] = { p =>
        require(tree.root.programs.contains(p))
        // the following may only be called once per package, otherwise wildcard identifiers may be desugared multiple
        // times, leading to different names being generated on different occasions.
        val unsortedDecls = p.declarations.collect { case d: PVarDecl => d; case PExplicitGhostMember(d: PVarDecl) => d }
        unsortedDecls.sortBy { decl =>
          tree.root.positions.positions.getStart(decl) match {
            case Some(pos) => (pos.line, pos.column)
            case None => violation(s"Could not find the position information of node $decl.")
          }
        }
    }
    Computation.cachedComputation(computation)
  }

  /**
   * Mapping from a file (PProgram) to the global variable declarations in that file, ordered by the dependency order.
   * The initialization of a variable A depends on the initialization of B if variable B occurs in the initializing
   * expression of A.
   */
  override val globalVarDeclsSortedByDeps: PProgram => Vector[PVarDecl] = { p =>
    // Currently, we require that all global variable declarations are provided in the dependency order.
    // Thus, this implementation is trivial and simply returns the global variable declarations sorted by
    // their syntactic position. However, a dependency analysis must be performed here once we lift
    // the restriction that global variables are declared in dependency order.
    val res = globalVarDeclsSortedByPos(p)
    // This assertion may seem redundant, given that it is also performed elsewhere during type-checking. Nonetheless,
    // this guarantees that once the type checking algorithm is extended, we do not forget to update this function.
    assert(globalDeclSatisfiesDepOrder(res).isEmpty)
    res
  }

  private[typing] def hasOldExpression(posts: Vector[PExpression]): Messages = {
    posts.flatMap{n =>
      val hasOld = allChildren(n).exists(_.isInstanceOf[POld])
      error(n, "'old' expressions cannot occur in import-preconditions, friend clause assertions, and package invariants", hasOld)
    }
  }
}