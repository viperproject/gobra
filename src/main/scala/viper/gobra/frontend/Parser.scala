/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import java.io.File

import org.bitbucket.inkytonik.kiama.parsing.Parsers
import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.ast.parser._
import viper.gobra.reporting.VerifierError

object Parser {

  def parse(file: File): Either[Vector[VerifierError], PProgram] = {
    Left(Vector.empty)
  }

  private class SyntaxAnalyzer(positions: Positions) extends Parsers(positions) {

    val reservedWords: Set[String] = Set(
      "break", "default", "func", "interface", "select",
      "case", "defer", "go", "map", "struct",
      "chan", "else", "goto", "package", "switch",
      "const", "fallthrough", "if", "range", "type",
      "continue", "for", "import", "return", "var"
    )

    def isReservedWord(word: String): Boolean = reservedWords contains word

    /**
      * Types
      */

    lazy val namedType: Parser[PNamedType] = ???


    /**
      * Identifiers
      */

    lazy val idnUnknown: Parser[PIdnUnknown] =
      identifier ^^ PIdnUnknown

    lazy val idnDef: Parser[PIdnDef] =
      identifier ^^ PIdnDef

    lazy val idnUse: Parser[PIdnUse] =
      idnUnqualifiedUse

    lazy val idnUnqualifiedUse: Parser[PIdnUnqualifiedUse] =
      identifier ^^ PIdnUnqualifiedUse

    lazy val identifier: Parser[String] =
      "[a-zA-Z_][a-zA-Z0-9_]*".r into (s => {
        if (isReservedWord(s))
          failure(s"""keyword "$s" found where identifier expected""")
        else
          success(s)
      })
  }

}


