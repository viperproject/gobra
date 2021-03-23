// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.annotation.unused

trait LabelResolution { this: TypeInfoImpl =>

  import org.bitbucket.inkytonik.kiama.util.Entity
  import org.bitbucket.inkytonik.kiama.==>
  import viper.gobra.util.Violation._

  import decorators._


  private[resolution] lazy val defLabel: PDefLikeLabel => Entity =
    attr[PDefLikeLabel, Entity] {
      case id@ tree.parent(p) =>

        val isGhost = isGhostDef(id)

        p match {
          case decl: PLabeledStmt => Label(decl, isGhost)
          case _ => violation("unexpected parent of label")
        }
    }

  private def serialize(id: PLabelNode): String = id.name

  /**
    * Labels have their own namespace.
    * The scope of a label is the body of the function in which it is declared.
    * This excludes the body of nested functions. [from the Go language specification]
    */
  private lazy val labelDefEnv: PLabelNode => Environment = {
    lazy val sequentialLabelDefEnv: Chain[Environment] =
      chain(defenvin, defenvout)

    def defenvin(@unused in: PNode => Environment): PNode ==> Environment = {
      case _: PMethodDecl | _: PFunctionDecl | _: PFunctionLit => rootenv()
    }

    def defenvout(out: PNode => Environment): PNode ==> Environment = {
      case id: PLabelDef => defineIfNew(out(id), serialize(id), MultipleEntity(), defLabel(id))
    }

    down(
      (_: PNode) => violation("Label does not root in a method or function")
    ) {
      case m@ (_: PMethodDecl | _: PFunctionDecl | _: PFunctionLit) => sequentialLabelDefEnv(m)
    }
  }

  /** returns the label definition for a label use. */
  lazy val label: PLabelNode => Entity =
    attr[PLabelNode, Entity] {
      n => lookup(labelDefEnv(n), serialize(n), UnknownEntity())
    }
}
