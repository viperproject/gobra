package viper.gobra.frontend

import org.antlr.v4.runtime.misc.IntervalSet
import org.antlr.v4.runtime.{BaseErrorListener, CommonTokenStream, FailedPredicateException, InputMismatchException, Lexer, NoViableAltException, Parser, RecognitionException, Recognizer, Token}
import org.bitbucket.inkytonik.kiama.util.{FileSource, Source}
import viper.gobra.frontend.GobraParser.{CapContext, EosContext, ExpressionContext, ImplementationProofContext, RULE_blockWithBodyParameterInfo, RULE_eos, RULE_shortVarDecl, RULE_type_, RULE_varDecl, Slice_Context, TypeSpecContext, Type_Context, VarSpecContext, ruleNames}
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.reporting.ParserError
import viper.silver.ast.SourcePosition

import java.nio.file.Path
import scala.collection.mutable.ListBuffer

class InformativeErrorListener(val messages: ListBuffer[ParserError], val source: Source) extends BaseErrorListener{

  var ignoredImplementationProofError : Option[ParserError] = None

  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    val error = recognizer match {
      case lexer: Lexer => DefaultErrorType()(LexerErrorContext(lexer, null, line, charPositionInLine, msg))
      case parser: Parser => {
        analyzeParserError(ParserErrorContext(parser, offendingSymbol.asInstanceOf[Token], line, charPositionInLine, msg), e)
      }
    }

    val pos = source match {
      case source: FileSource => Some(SourcePosition(Path.of(source.name), line, charPositionInLine))
      case source: FromFileSource => Some(SourcePosition(source.path, line, charPositionInLine))
      case _ => None
    }
    val message = error match {
      case i : IgnoreError => {
        val ignored = ParserError(error.full, pos)
        ignoredImplementationProofError = ignoredImplementationProofError match {
          case None => Some(ignored)
          case s => s
        }
        Some(ignored)
      }
      case a => Some(ParserError(error.full, pos))
    }
    messages.prependAll(message)
  }

  protected def underlineError(context : ErrorContext, restOfTheLine : Boolean = false): String = {
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
    for (i <- 0 until offendingToken.getCharPositionInLine) {
      message += " "
    }
    val start = offendingToken.getCharPositionInLine
    val stop = if (restOfTheLine) rest else offendingToken.getCharPositionInLine + offendingToken.getText.length
    if (start >= 0 && stop >= 0) for (_ <- start until stop) {
      message += "^"
    }
    message += "\n"
    message
  }


  def analyzeFailedPredicate(implicit context: ParserErrorContext, exception: FailedPredicateException): ErrorType = {
    val parser = context.recognizer
    parser.getContext match {
      case eos : GobraParser.EosContext => {
        context.offendingSymbol.getType match {
          case GobraParser.DECLARE_ASSIGN => GotAssignErrorType()
          case _ => DefaultFailedEOS()
        }
      }
      case _ => DefaultErrorType()
    }
  }

  def analyzeExtraneous(implicit context: ParserErrorContext): ErrorType = {
    (context.offendingSymbol.getType, context.recognizer.getContext) match {
      case (GobraParser.DECLARE_ASSIGN, _ : TypeSpecContext) => GotAssignErrorType()
      case (GobraParser.R_BRACKET, expr : ExpressionContext) if expr.parent.isInstanceOf[CapContext] => SliceMissingIndex(3)
      case _ => DefaultExtraneous()
    }
  }

  def analyzeInputMismatch(implicit context: ParserErrorContext, exception: InputMismatchException): ErrorType = {
    (context.offendingSymbol.getType, context.recognizer.getContext) match {
      case (Token.EOF, _) => DefaultMismatch()
      case (s, i : ImplementationProofContext) if context.recognizer.getExpectedTokens == IntervalSet.of(GobraParser.IMPL)=> {
        IgnoreError()
      }
      case (GobraParser.DECLARE_ASSIGN, _) => GotAssignErrorType()
      case (GobraParser.R_BRACKET, e : ExpressionContext) if e.parent.isInstanceOf[CapContext] => SliceMissingIndex(3)
      case _ => DefaultMismatch()
    }
  }

  def analyzeNoViable(implicit context: ParserErrorContext, exception: NoViableAltException): ErrorType = {
    val parser = context.recognizer
    val ctx = parser.getContext
    ctx match {
      case slice : Slice_Context => {
        // Missing either the second or second an third (or completely wrong)
        SliceMissingIndex()
      }
      case _ : VarSpecContext | _ : Type_Context => GotAssignErrorType()
      case eos : EosContext => {
        parser.getTokenStream.LT(2).getType match {
          case GobraParser.DECLARE_ASSIGN => GotAssignErrorType()(context.copy(offendingSymbol = parser.getTokenStream.LT(2)))
          case _ => DefaultNoViable(exception)
        }
      }
      case e : ExpressionContext if e.parent.isInstanceOf[CapContext] => SliceMissingIndex(3)
      case _ => DefaultNoViable(exception)
    }
  }

  def analyzeParserError(implicit context : ParserErrorContext, e : RecognitionException): ErrorType = {
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

  def getDisplay(index : Int)(implicit context : ParserErrorContext): String = {
    betterNames.getOrElse(index, ruleNames(index))
  }

  private val betterNames : Map[Int, String] = Map{
    RULE_type_ -> "type"
    RULE_eos -> "end of statement"
    RULE_varDecl -> "var declaration"
    RULE_shortVarDecl -> "short variable declaration"
    RULE_blockWithBodyParameterInfo -> "block"
  }

  private val extraneous = "extraneous.*".r
  private val missing = "missing.*".r

  sealed trait ErrorContext {
    val recognizer : Recognizer[_, _]
    val offendingSymbol: Any
    val line: Int
    val charPositionInLine: Int
    val msg: String
  }

  case class LexerErrorContext(recognizer: Lexer, offendingSymbol: Null, line: Int, charPositionInLine: Int, msg: String) extends ErrorContext

  case class ParserErrorContext(recognizer: Parser, offendingSymbol: Token, line: Int, charPositionInLine: Int, msg: String) extends ErrorContext


  sealed trait ErrorType {
    val context: ErrorContext
    val msg : String
    lazy val expected : IntervalSet = context.recognizer match {
      case lexer: Lexer => IntervalSet.EMPTY_SET
      case parser: Parser => parser.getExpectedTokens
    }
    lazy val underlined : String = underlineError(context)
    lazy val full : String = msg + "\n" + underlined
  }

  case class DefaultErrorType()(implicit val context : ErrorContext) extends ErrorType {
    val msg : String = context.msg
  }

  case class DefaultExtraneous()(implicit  val context : ParserErrorContext) extends ErrorType {
    val msg : String = s"extraneous ${context.offendingSymbol.getText} in ${context.recognizer.getRuleNames()(context.recognizer.getContext.getRuleIndex)}"
  }

  case class DefaultMismatch()(implicit  val context : ParserErrorContext) extends ErrorType {
    val msg : String = s"unexpected ${context.offendingSymbol.getText}" +
      (if (context.offendingSymbol == context.recognizer.getContext.getStart) s", expecting ${context.recognizer.getRuleNames()(context.recognizer.getContext.getRuleIndex)}"
      else s" in ${context.recognizer.getRuleNames()(context.recognizer.getContext.getRuleIndex)}, expecting ${context.recognizer.getExpectedTokens.toString(GobraParser.VOCABULARY)}")

  }

  case class DefaultNoViable(e : NoViableAltException)(implicit val context: ParserErrorContext) extends ErrorType {
    val msg : String = s"Wrong Syntax after '${e.getStartToken.getText}'."
    override lazy val underlined: String = underlineError(context.copy(offendingSymbol = context.recognizer.getInputStream.LT(2)), restOfTheLine = true)
  }

  case class DefaultFailedEOS()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg : String = s"Could not finish parsing the line."
  }

  case class IgnoreError()(implicit val context: ParserErrorContext) extends ErrorType {
    val msg : String = s"Wrong Syntax."
  }

  case class GotAssignErrorType()(implicit val context : ParserErrorContext) extends  ErrorType {
    val msg : String = "Unexpected ':=', did you mean '='?"
  }

  case class SliceMissingIndex(index : Int = 0)(implicit val context : ParserErrorContext) extends  ErrorType {
    val msg : String = s"Wrong syntax inside slice type. In a 3-index slice, the ${
      index match {
        case 2 => "2nd is"
        case 3 => "3rd is"
        case _ => "2nd and 3rd are"
      }
    } required."
  }

  case class RangeNoSpaces(hint : String = "")(implicit val context : ErrorContext) extends ErrorType {
    val msg = "Missing spaces"
  }


  case class EOFError()(implicit val context : ErrorContext) extends ErrorType {
    val msg = "Unexpectedly reached end of file."
    override lazy val full: String = msg
  }
}
