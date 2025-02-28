// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.parsing

import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.Assertions.fail
import viper.gobra.ast.frontend.{PExpression, PImport, PMember, PProgram, PStatement, PType}
import viper.gobra.frontend.Parser
import viper.gobra.reporting.{ParserError, ParserMessage, ParserWarning}

import scala.reflect.ClassTag

class ParserTestFrontend {
  private def parse[T: ClassTag](source: String, parser: Source => Either[Vector[ParserMessage], (T, Vector[ParserWarning])]) : Either[Vector[ParserError], T] =
    parser(StringSource(source)).fold(
      messages => Left(messages.collect { case e: ParserError => e }),
      { case (ast, _) => Right(ast) }
    )

  private def parseOrFail[T: ClassTag](source: String, parser: Source => Either[Vector[ParserMessage], (T, Vector[ParserWarning])]): T = {
    parse(source, parser) match {
      case Right(ast) => ast
      case Left(messages) => fail(s"Parsing failed: $messages")
    }
  }


  def parseProgram(source : String) : Either[Vector[ParserError], PProgram] = parse(source, source => Parser.parseProgram(source))
  def parseExp(source : String) : Either[Vector[ParserError], PExpression] = parse(source, Parser.parseExpr)
  def parseExpOrFail(source : String) : PExpression = parseOrFail(source, Parser.parseExpr)
  def parseStmt(source : String) : Either[Vector[ParserError], PStatement] = parse(source, Parser.parseStmt)
  def parseStmtOrFail(source : String) : PStatement = parseOrFail(source, Parser.parseStmt)
  def parseType(source : String) : Either[Vector[ParserError], PType] = parse(source, Parser.parseType)
  def parseTypeOrFail(source : String) : PType = parseOrFail(source, Parser.parseType)
  def parseImportDecl(source: String): Vector[PImport] = parseOrFail(source, Parser.parseImportDecl)
  def parseMember(source: String, specOnly: Boolean = false): Either[Vector[ParserError], Vector[PMember]] = parse(source, (s: Source) => Parser.parseMember(s, specOnly = specOnly))
  def parseMemberOrFail(source: String, specOnly: Boolean = false): Vector[PMember] = parseOrFail(source, (s: Source) => Parser.parseMember(s, specOnly = specOnly))
}
