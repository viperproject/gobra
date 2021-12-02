package viper.gobra.frontend

import org.antlr.v4.runtime.tree.pattern.RuleTagToken
import org.antlr.v4.runtime.{BaseErrorListener, CommonTokenStream, RecognitionException, Recognizer, Token, WritableToken}
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.Parser.FromFileSource
import viper.gobra.reporting.ParserError
import viper.silver.ast.SourcePosition

import scala.collection.mutable.ListBuffer

class InformativeErrorListener(val messages: ListBuffer[ParserError], val source: Source) extends BaseErrorListener{
  override def syntaxError(recognizer: Recognizer[_, _], offendingSymbol: Any, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
    val pos = source match {
      case source : FromFileSource => Some(SourcePosition(source.path, line, charPositionInLine))
      case _ => None
    }
    if((e != null) && (offendingSymbol != null)){
      val errormessage = analyzeExprError(recognizer, offendingSymbol, e) match {
        case DefaultErrorPattern() => msg+" in rule: "+recognizer.getRuleNames()(e.getCtx.getRuleIndex)+"\n" + underlineError(recognizer, offendingSymbol.asInstanceOf[Token], line, charPositionInLine)
        case RangeNoSpaces() => "Numbers before range dots must be followed by whitespace to avoid ambiguity with floats." + underlineError(recognizer, offendingSymbol.asInstanceOf[Token], line, charPositionInLine)
      }
      messages.append(ParserError(errormessage, pos))
    } else {
      messages.append(ParserError(msg, pos))
    }
  }

  protected def underlineError(recognizer: Recognizer[_, _], offendingToken: Token, line: Int, charPositionInLine: Int): String = {
    val tokens = recognizer.getInputStream.asInstanceOf[CommonTokenStream]
    val input = tokens.getTokenSource.getInputStream.toString
    val lines = input.split("\n")
    var message = lines(line - 1)
    message += "\n"
    print(charPositionInLine)
    for (i <- 0 until charPositionInLine) {
      message += " "
    }
    val start = offendingToken.getStartIndex
    val stop = offendingToken.getStopIndex
    if (start >= 0 && stop >= 0) for (i <- start to stop) {
      message += "^"
    }
    message += "\n"
    message
  }



  def analyzeExprError(recognizer: Recognizer[_, _], offendingSymbol: Any, exception: RecognitionException): ErrorPattern = {
    val tok = offendingSymbol.asInstanceOf[Token]
    DefaultErrorPattern()
  }

  protected def errorAnalyzer(recognizer: Recognizer[_, _], offendingSymbol: Any, e: RecognitionException): ErrorPattern = {
    e.getCtx.getRuleIndex match {
      case GobraParser.RULE_expression => analyzeExprError(recognizer: Recognizer[_, _], offendingSymbol: Any, e: RecognitionException)
      case _ => DefaultErrorPattern()
    }
  }

  sealed trait ErrorPattern {

  }
  case class DefaultErrorPattern() extends ErrorPattern

  case class RangeNoSpaces() extends ErrorPattern

}
