// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error}
import viper.gobra.ast.frontend.{PFunctionSpec, PGlobalVarDecl, PPackage, PProgram}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.property.{AssignMode, StrictAssignMode}
import viper.gobra.util.Violation

trait ProgramTyping extends BaseTyping { this: TypeInfoImpl =>
  // TODO: Check for redeclarations

  lazy val wellDefProgram: WellDefinedness[PProgram] = createWellDef {
    case PProgram(_, spec, _, members) =>
      // Obtains global variable declarations sorted by the order in which they appear in the file
      val sortedByPosDecls: Vector[PGlobalVarDecl] = {
        val unsortedDecls: Vector[PGlobalVarDecl] = members.collect{ case d: PGlobalVarDecl => d }
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
      globalDeclSatisfiesDepOrder(sortedByPosDecls) ++ isValidProgramSpec(spec)
  }

  private def isValidProgramSpec: PFunctionSpec => Messages = {
    case s@ PFunctionSpec(pres, preserves, _, terminationMeasures, isPure, isTrusted) =>
      val validCond = pres.isEmpty &&
        preserves.isEmpty &&
        terminationMeasures.isEmpty &&
        !isPure &&
        !isTrusted
      error(s, s"Only postconditions can be specified in package specifications", !validCond)
  }

  /** TODO:
    * Currently, Gobra requires that global variables are declared in an order such that all dependencies of a global
    * variable are declared before it. In practice, it rules out declarations like the following:
    *     var B int = A
    *     var A int
    * Instead, one would need to write it in the following order:
    *     var A int
    *     var B int = A
    * This is not limiting, as an order can always be found such that the declaration order is compatible
    * with the dependency relation. Nonetheless, this should be addressed in the future.
    */
  private def globalDeclSatisfiesDepOrder(globalDeclsInPosOrder: Vector[PGlobalVarDecl]): Messages = {
    var visitedGlobals = Vector.empty[Entity]
    globalDeclsInPosOrder.flatMap{ decl =>
      decl.left.zipWithIndex.flatMap{ case (id, idx) =>
        val visitedAllDeps: Boolean = regular(id) match {
          case g: st.GlobalVariable =>
            val result = dependenciesOfGlobal(g).forall(visitedGlobals.contains)
            visitedGlobals :+= g
            result
          case _: st.Wildcard if decl.right.isEmpty => true
          case _: st.Wildcard =>
            val deps = StrictAssignMode(decl.left.length, decl.right.length) match {
              case AssignMode.Single => globalsExprDependsOn(decl.right(idx))
              case AssignMode.Multi => globalsExprDependsOn(decl.right.head)
              case m => Violation.violation(s"Expected this case to be unreachable, but got $m instead.")
            }
            deps.forall(visitedGlobals.contains)
          case e =>
            Violation.violation(s"Expected this case to be unreachable, but resolved $id as a $e.")
        }
        val errorMsg: String = "Currently, Gobra requires dependencies of a global variable to be declared before it."
        error(decl, errorMsg, !visitedAllDeps)
      }
    }
  }
}
