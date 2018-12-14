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


    lazy val expression: Parser[PExpression] = ???

    /**
      * Types
      */

    lazy val typ: Parser[PType] = ???

    lazy val typeLit: Parser[PTypeLit] =
      "*" ~> typ ^^ PPointerType |
        "[]" ~> typ ^^ PSliceType |
        ("[" ~> expression <~ "]") ~ typ ^^ PArrayType |
        ("map" ~> ("[" ~> typ <~ "]")) ~ typ ^^ PMapType |
        ("chan" ~> "<-") ~> typ ^^ PRecvChannelType |
        ("<-" ~> "chan") ~> typ ^^ PSendChannelType |
        "chan" ~> typ ^^ PBiChannelType |
        "func" ~> signature ^^ PFunctionType.tupled

    lazy val structType: Parser[PStructType] =
      repsep(structClause, eos) ^^ { clauses =>
        val embedded = clauses collect { case v: PEmbeddedDecl => v }
        val declss = clauses collect { case v: PFieldDecls => v }

        PStructType(embedded, declss flatMap (_.fields))
      }

    lazy val structClause: Parser[PStructClause] =
      embeddedDecl | fieldDecls

    lazy val embeddedDecl: Parser[PEmbeddedDecl] =
      "*".? ~ namedType ^^ {
        case None ~ t => PEmbeddedName(t)
        case _ ~ t => PEmbeddedPointer(t)
      }

    lazy val fieldDecls: Parser[PFieldDecls] =
      rep1sep(idnDef, ",") ~ typ ^^ { case ids ~ t =>
          PFieldDecls(ids map (PFieldDecl(_, t)))
      }


    lazy val namedType: Parser[PNamedType] =
      predeclaredType |
        declaredType

    lazy val predeclaredType: Parser[PPredeclaredType] =
      "bool" ^^^ PBoolType() |
        "int" ^^^ PIntType()

    lazy val declaredType: Parser[PDeclaredType] =
      idnUse ^^ PDeclaredType

    /**
      * Misc
      */

    lazy val signature: Parser[(Vector[PParameter], PResult)] =
      parameters ~ result

    lazy val result: Parser[PResult] =
      parameters ^^ PResultClause |
        typ ^^ (t => PResultClause(Vector(PUnnamedParameter(t)))) |
        success(PVoidResult())

    lazy val parameters: Parser[Vector[PParameter]] =
      "(" ~> (parameters <~ ",".?).? <~ ")" ^^ {
        case None => Vector.empty
        case Some(ps) => ps
      }

    lazy val parameterList: Parser[Vector[PParameter]] =
      rep1sep(parameterDecl, ",") ^^ Vector.concat

    lazy val parameterDecl: Parser[Vector[PParameter]] =
      repsep(idnDef, ",") ~ typ ^^ { case ids ~ t =>

        val names = ids filter (!PIdnNode.isWildcard(_))
        if (names.isEmpty) {
          Vector(PUnnamedParameter(t))
        } else {
          ids map (PNamedParameter(_, t))
        }
      }

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

    /**
      * EOS
      */

    lazy val eos: Parser[String] =
      ";"
  }



}


