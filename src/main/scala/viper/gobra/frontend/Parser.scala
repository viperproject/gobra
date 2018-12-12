/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File

import org.bitbucket.inkytonik.kiama.parsing.Parsers
import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.ast.parser.PProgram
import viper.gobra.reporting.VerifierError

object Parser {

  def parse(file: File): Either[Vector[VerifierError], PProgram] = {
    Left(Vector.empty)
  }

  private class SyntaxAnalyzer(positions: Positions) extends Parsers(positions) {

  }
}


