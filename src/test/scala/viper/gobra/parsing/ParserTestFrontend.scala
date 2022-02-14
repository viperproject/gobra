package viper.gobra.parsing

import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.Assertions.fail
import viper.gobra.ast.frontend.{PExpression, PImport, PMember, PProgram, PStatement, PType}
import viper.gobra.frontend.Parser
import viper.gobra.reporting.ParserError

import scala.reflect.ClassTag

class ParserTestFrontend {
  private def parse[T: ClassTag](source: String, parser: Source => Either[Vector[ParserError], T]) : Either[Vector[ParserError], T] =
    parser(StringSource(source))

  private def parseOrFail[T: ClassTag](source: String, parser: Source => Either[Vector[ParserError], T]): T = {
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
  def parseFunctionDecl(source: String, specOnly: Boolean = false): PMember = parseOrFail(source, (s: Source) => Parser.parseFunction(s, specOnly = specOnly))

}