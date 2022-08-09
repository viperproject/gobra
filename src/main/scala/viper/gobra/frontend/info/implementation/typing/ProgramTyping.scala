// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend.{PExpression, POld, PPackage, PProgram, PVarDecl}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.util.Violation

trait ProgramTyping extends BaseTyping { this: TypeInfoImpl =>

  lazy val wellDefProgram: WellDefinedness[PProgram] = createWellDef {
    case PProgram(_, posts, imports, members) =>
      // Obtains global variable declarations sorted by the order in which they appear in the file
      val sortedByPosDecls: Vector[PVarDecl] = {
        val unsortedDecls: Vector[PVarDecl] = members.collect{ case d: PVarDecl => d }
        // we require a package to be able to obtain position information
        val pkgOpt: Option[PPackage] = unsortedDecls.headOption.flatMap(tryEnclosingPackage)
        // sort declarations by the order in which they appear in the program
        unsortedDecls.sortBy{ decl =>
          pkgOpt.get.positions.positions.getStart(decl) match {
            case Some(pos) => (pos.line, pos.column)
            case _ => Violation.violation(s"Could not find position information of $decl.")
          }
        }
      }
      // HACK: without this explicit check, Gobra does not find repeated declarations
      //       of global variables. This has to do with the changes introduced in PR #186.
      //       We need this check nonetheless because the checks performed in the "true" branch
      //       assume that the ids are well-defined.
      val idsOkMsgs = sortedByPosDecls.flatMap(d => d.left).flatMap(l => wellDefID(l).out)
      if (idsOkMsgs.isEmpty) {
        globalDeclSatisfiesDepOrder(sortedByPosDecls) ++
          hasOldExpression(posts) ++
          hasOldExpression(imports.flatMap(_.importPres))
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

  private def hasOldExpression(posts: Vector[PExpression]): Messages = {
    posts.flatMap{n =>
      val hasOld = allChildren(n).exists(_.isInstanceOf[POld])
      error(n, "'old' expressions cannot occur in init-postconditions and import-preconditions", hasOld)
    }
  }
}
