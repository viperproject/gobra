/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.parser

import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.ast.parser.PNode.PPkg


sealed trait PNode extends Product

object PNode {
  type PPkg = String

}


case class PProgram(
                     packageClause: PPackageClause,
                     imports: Vector[PImportDecl],
                     declarations: Vector[PDeclaration],
                     positions: Positions
                   ) extends PNode


case class PPackageClause() extends PNode


sealed trait PImportDecl extends PNode {
  def pkg: PPkg
}

case class PQualifiedImport(qualifier: PIdnDef, pkg: PPkg)

case class PUnqualifiedImport(pkg: PPkg)


sealed trait PDeclaration extends PNode

case class PConstDecl() extends PDeclaration

case class PTypeDecl() extends PDeclaration

case class PVarDecl() extends PDeclaration

case class PFunctionDecl() extends PDeclaration

case class PMethodDecl() extends PDeclaration


sealed trait PIdnNode extends PNode {
  def name: String
}

case class PIdnDef(name: String) extends PIdnNode

case class PIdnUse(name: String) extends PIdnNode
