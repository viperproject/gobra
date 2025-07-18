// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.frontend

import org.antlr.v4.runtime.misc.IntervalSet
import org.antlr.v4.runtime.{BaseErrorListener, CommonTokenStream, FailedPredicateException, InputMismatchException, Lexer, NoViableAltException, Parser, RecognitionException, Recognizer, Token}
import org.bitbucket.inkytonik.kiama.util.{FileSource, Source}
import viper.gobra.frontend.GobraParser._
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.reporting.ParserError
import viper.silver.ast.SourcePosition

import java.nio.file.Path
import scala.annotation.unused
import scala.collection.mutable.ListBuffer

class InformativeErrorListener(val messages: ListBuffer[ParserError], val source: Source) extends BaseErrorListener {

  /**
    * First token that Gobra introduces that is not present in Go.
    * First refers to the order of token declarations in GobraLexer.g4
    * as this order is preserved by ANTLR4 when generating GobraParser.java
    */
  private val FIRST_GOBRA_TOKEN = GobraParser.TRUE
  /**
    * Last token that Gobra introduces that is not present in Go.
    * Last refers to the order of token declarations in GobraLexer.g4
    * this order is preserved by ANTLR4 when generating GobraParser.java
    */
  private val LAST_GOBRA_TOKEN = GobraParser.FRIENDPKG

  /**
    *
    * @param recognizer The recognizer that encountered the error
    * @param offendingSymbol The symbol that caused the error
    * @param line The line number in the Source
    * @param charPositionInLine The column in the line
    * @param msg The message emitted by the [[org.antlr.v4.runtime.ANTLRErrorListener]]
    * @param e The specific [[RecognitionException]] thrown by the parser
    */
  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    // We don't analyze Lexer errors any further: The defaults are sufficient
    val error = recognizer match {
      case lexer: Lexer => DefaultErrorType()(LexerErrorContext(lexer, null, line, charPositionInLine, msg))
      case parser: Parser =>
        analyzeParserError(ParserErrorContext(parser, offendingSymbol.asInstanceOf[Token], line, charPositionInLine, msg), e)
    }

    // Depending on the source, get the applicable type of position information
    val pos = source match {
      case source: FileSource => Some(SourcePosition(Path.of(source.name), line, charPositionInLine))
      case source: FromFileSource => Some(SourcePosition(source.path, line, charPositionInLine))
      case _ => None
    }

    // Wrap the error in Gobra's error class
    val message = Some(ParserError(error.full, pos))
    messages.appendAll(message)
  }

  /**
    * Check the given error context against a set of known patterns to check for specific common errors.
    * @param context
    * @param e
    * @return
    */
  def analyzeParserError(implicit context: ParserErrorContext, e: RecognitionException): ErrorType = {
    e match {
      case exception: FailedPredicateException => analyzeFailedPredicate(context, exception)
      case exception: InputMismatchException => analyzeInputMismatch(context, exception)
      case exception: NoViableAltException => analyzeNoViable(context, exception)
      case null => context.msg match {
        case extraneous() => analyzeExtraneous(context)
        case missing() => DefaultErrorType()
      }
      case _ => DefaultErrorType()
    }
  }


  /**
    * Failed predicate errors are emitted when no viable alternatives remain, as all alternatives conflict with the input
    * or contain a semantic predicate that evaluated to false. Because the only predicate present in Gobra's grammar
    * is responsible for inducing semicolons, we know that if a predicate failed, we are at a point where a statement
    * could have ended, but another token was discovered.
    *
    * @see [[FailedPredicateException]]
    * @param context
    * @param exception
    * @return
    */
  def analyzeFailedPredicate(implicit context: ParserErrorContext, @unused exception: FailedPredicateException): ErrorType = {
    val parser = context.recognizer
    parser.getContext match {
      // One example of a known pattern: Parser reads ':=' when expecting the end of statement: The user probably
      // used ':=' instead of '='
      case _: GobraParser.EosContext =>
        context.offendingSymbol.getType match {
          // An unexpected := was encountered, perhaps the user meant =
          case GobraParser.DECLARE_ASSIGN => GotAssignErrorType()
          case GobraParser.IN => UnmatchedInErrorType()
          case _ => DefaultFailedEOS()
        }
      case _ => DefaultErrorType()
    }
  }

  /**
    * An extraneous token error is emitted whenever a single token stands in the way of correctly
    * parsing the input.
    *
    * @param context The context of the error
    * @return
    */
  def analyzeExtraneous(implicit context: ParserErrorContext): ErrorType = {
    (context.offendingSymbol.getType, context.recognizer.getContext) match {
      // Type aliases use an = token, while type definitions do not use an assignemnt token at all
      // The extraneous := was most likely supposed to be a =
      case (GobraParser.DECLARE_ASSIGN, _: TypeSpecContext) => GotAssignErrorType()
      // We expected more tokens inside a slice expression but got a closing bracket: One of the
      // limits must be missing.
      case (GobraParser.R_BRACKET, expr: ExpressionContext) if expr.parent.isInstanceOf[CapSliceArgumentContext] => SliceMissingIndex(3)
      case _ => DefaultExtraneous()
    }
  }

  /**
    * The input did not match the expected tokens. This is one of the most common exceptions.
    *
    * @see [[InputMismatchException]]
    * @param context The context of the error
    * @param exception
    * @return
    */
  def analyzeInputMismatch(implicit context: ParserErrorContext, @unused exception: InputMismatchException): ErrorType = {
    (context.offendingSymbol.getType, context.recognizer.getContext) match {
      case (Token.EOF, _) => DefaultMismatch()
      // Again, we have an unexpected :=, so suggest using a =
      case (GobraParser.DECLARE_ASSIGN, _) => GotAssignErrorType()
      case (GobraParser.R_BRACKET, e : ExpressionContext) if e.parent.isInstanceOf[CapSliceArgumentContext] => SliceMissingIndex(3)
      case _ => DefaultMismatch()
    }
  }

  /**
    * The parser simulated all possible rule alternatives, but did not find any that matched the input.
    *
    * @see [[NoViableAltException]]
    * @param context The context of the error
    * @param exception
    * @return
    */
  def analyzeNoViable(implicit context: ParserErrorContext, exception: NoViableAltException): ErrorType = {
    val parser = context.recognizer
    val ctx = parser.getContext
    ctx match {
      case _: Slice_Context =>
        // Missing either the second or second and third argument (or completely wrong)
        SliceMissingIndex()
      case _: VarSpecContext | _: Type_Context if context.offendingSymbol.getType == GobraParser.DECLARE_ASSIGN => GotAssignErrorType()
      case _: EosContext =>
        parser.getTokenStream.LT(2).getType match {
          case GobraParser.DECLARE_ASSIGN => GotAssignErrorType()(context.copy(offendingSymbol = parser.getTokenStream.LT(2)))
          case _ => DefaultNoViable(exception)
        }
      case e: ExpressionContext if e.parent.isInstanceOf[CapSliceArgumentContext] => SliceMissingIndex(3)
      case _ if new_reserved.contains(context.offendingSymbol.getType) => ReservedWord()
      case _ => DefaultNoViable(exception)
    }
  }


  /**
    * This method will print out the line containing the offending symbol, as well as
    * caret character underlining the error. This is slightly modified from the base version found
    * in the official ANTLR Guide
    *
    * @param context The context of the error
    * @param restOfTheLine Also underline the rest of the line. Useful if the rest of the line is most likely wrong as
    *                      well as the token in the [[ErrorContext]]
    * @return
    */
  protected def underlineError(context: ErrorContext, restOfTheLine: Boolean = false): String = {
    val offendingToken = context.offendingSymbol match {
      case t : Token => t
      case _ => return ""
    }
    val tokens = context.recognizer.getInputStream.asInstanceOf[CommonTokenStream]
    val input = tokens.getTokenSource.getInputStream.toString
    val lines = input.split("\r?\n", -1)
    var message = lines(offendingToken.getLine - 1)
    val rest = message.length
    message += "\n"
    message += " " * offendingToken.getCharPositionInLine
    val start = offendingToken.getCharPositionInLine
    val stop = if (restOfTheLine) rest else offendingToken.getCharPositionInLine + offendingToken.getText.length
    if (start >= 0 && stop >= 0) { message += "^" * (stop - start) }
    message += "\n"
    message
  }


  /**
    * Return the display name associated with a specific rule
    * @param index
    * @return
    */
  def getRuleDisplay(index: Int): String = {
    betterRuleNames.getOrElse(index, ruleNames(index))
  }

  /**
    * Not all rules have a very descriptive name, this map provides more user-friendly names for them.
    */
  private val betterRuleNames: Map[Int, String] = Map{
    RULE_type_ -> "type"
    RULE_eos -> "end of line"
    RULE_varDecl -> "var declaration"
    RULE_shortVarDecl -> "short variable declaration"
    RULE_blockWithBodyParameterInfo -> "block"
  }

  /**
    * The same as [[betterRuleNames]], but for tokens.
    * @param t
    * @return
    */
  def getTokenDisplay(t: Token): String = {
    t.getText match {
      case "\n" => "end of line"
      case s => s
    }
  }


  private val extraneous = "extraneous.*".r
  private val missing = "missing.*".r

  /**
    * This is a wrapper around all context informatin passed to the error listener.
    */
  sealed trait ErrorContext {
    val recognizer: Recognizer[_, _]
    val offendingSymbol: Token
    val line: Int
    val charPositionInLine: Int
    val msg: String
  }

  case class LexerErrorContext(recognizer: Lexer, offendingSymbol: Null, line: Int, charPositionInLine: Int, msg: String) extends ErrorContext

  case class ParserErrorContext(recognizer: Parser, offendingSymbol: Token, line: Int, charPositionInLine: Int, msg: String) extends ErrorContext


  /**
    * This class and its inheritors characterise different error types and
    * include specific error messages for them.
    */
  sealed trait ErrorType {
    val context: ErrorContext
    val msg: String
    lazy val expected: IntervalSet = context.recognizer match {
      case _: Lexer => IntervalSet.EMPTY_SET
      case parser: Parser => parser.getExpectedTokens
    }
    lazy val underlined: String = underlineError(context)
    lazy val full: String = msg + "\n" + underlined
  }

  case class DefaultErrorType()(implicit val context: ErrorContext) extends ErrorType {
    val msg: String = context.msg
  }

  case class DefaultExtraneous()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = s"extraneous ${context.offendingSymbol.getText} in ${context.recognizer.getRuleNames()(context.recognizer.getContext.getRuleIndex)}"
  }

  case class DefaultMismatch()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = s"unexpected ${getTokenDisplay(context.offendingSymbol)}" +
      (if (context.offendingSymbol == context.recognizer.getContext.getStart) s", expecting ${context.recognizer.getRuleNames()(context.recognizer.getContext.getRuleIndex)}"
      else s" in ${getRuleDisplay(context.recognizer.getContext.getRuleIndex)}, expecting ${context.recognizer.getExpectedTokens.toString(GobraParser.VOCABULARY)}")
  }

  case class DefaultNoViable(e: NoViableAltException)(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = context.msg
    override lazy val underlined: String = underlineError(context.copy(offendingSymbol = e.getStartToken), restOfTheLine = true)
  }

  case class DefaultFailedEOS()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = s"Could not finish parsing the line."
  }

  case class GotAssignErrorType()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = "Unexpected ':=', did you mean '='?"
  }

  case class UnmatchedInErrorType()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = "Unexpected 'in' encountered. Did you mean to use 'elem' denoting ghost collection membership?"
  }

  case class SliceMissingIndex(index: Int = 0)(implicit val context: ParserErrorContext) extends ErrorType {
    val msg: String = s"Wrong syntax inside slice type. In a 3-index slice, the ${
      index match {
        case 2 => "2nd is"
        case 3 => "3rd is"
        case _ => "2nd and 3rd are"
      }
    } required."
  }

  case class RangeNoSpaces(hint: String = "")(implicit val context: ErrorContext) extends ErrorType {
    val msg = "Missing spaces"
  }


  case class EOFError()(implicit val context: ErrorContext) extends ErrorType {
    val msg = "Unexpectedly reached end of file."
    override lazy val full: String = msg
  }

  case class ReservedWord()(implicit val context: ErrorContext) extends ErrorType {
    val msg = s"Unexpected reserved word ${context.offendingSymbol.getText}."
  }

  // All tokens reserved by Gobra, but not by Go
  private val new_reserved = IntervalSet.of(FIRST_GOBRA_TOKEN, LAST_GOBRA_TOKEN)
}
