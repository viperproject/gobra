package viper.gobra

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.{FunSuite, Inside, Matchers}
import scala.reflect.ClassTag
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Parser

class ParserUnitTests extends FunSuite with Matchers with Inside {
  private val frontend = new TestFrontend()

  test("Parser: Dot") {
    frontend.parseExpOrFail("self.Contains") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Contains")) =>
    }
  }

  test("Parser: Invoke") {
    frontend.parseExpOrFail("Contains(v)") should matchPattern {
      case PInvoke(PNamedOperand(PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: DotInvoke") {
    frontend.parseExpOrFail("self.Contains(v)") should matchPattern {
      case PInvoke(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: NestedDot") {
    frontend.parseExpOrFail("(self.Left)") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")) =>
    }
  }

  test("Parser: DoubleDotInvoke1") {
    frontend.parseExpOrFail("(self.Left).Contains(v)") should matchPattern {
      case PInvoke(PDot(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: DoubleDotInvoke2") {
    frontend.parseExpOrFail("self.Left.Contains(v)") should matchPattern {
      case PInvoke(PDot(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: assignment constant to variable") {
    val parseRes = frontend.parseStmtOrFail("p = 5")
    inside (parseRes) {
      case PAssignment(Vector(PIntLit(value)),
      Vector(PNamedOperand(PIdnUse("p")))) => value should be (5)
    }
  }

  test("Parser: field access") {
    frontend.parseExpOrFail("p.x") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")) =>
    }
  }

  test("Parser: assignment constant to field") {
    val parseRes = frontend.parseStmtOrFail("p.x = 5")
    inside (parseRes) {
      case PAssignment(Vector(PIntLit(value)),
      Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")))) => value should be (5)
    }
  }

  test("Parser: assignment field to field") {
    frontend.parseStmtOrFail("p.x = p.y") should matchPattern {
      case PAssignment(Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("y"))),
      Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")))) =>
    }
  }

  /* ** Sequences */

  test("Parser: simple integer sequence") {
    frontend.parseType("seq[int]") should matchPattern {
      case Right(PSequenceType(PIntType())) =>
    }
  }

  test("Parser: integer sequence with spacing") {
    frontend.parseType("seq [ int ]") should matchPattern {
      case Right(PSequenceType(PIntType())) =>
    }
  }

  test("Parser: custom typed sequence") {
    frontend.parseType("seq[T]") should matchPattern {
      case Right(PSequenceType(PNamedOperand(PIdnUse("T")))) =>
    }
  }

  test("Parser: nested sequence") {
    frontend.parseType("seq[seq[int]]") should matchPattern {
      case Right(PSequenceType(PSequenceType(PIntType()))) =>
    }
  }

  test("Parser: mistyped sequence 1") {
    frontend.parseType("seq[int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: mistyped sequence 2") {
    frontend.parseType("SEQ[int]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: empty integer sequence literal") {
    frontend.parseExp("seq[int] { }") should matchPattern {
      case Right(PSequenceLiteral(PIntType(), Vector())) =>
    }
  }

  test("Parser: singular integer sequence literal") {
    frontend.parseExp("seq[int] { 42 }") should matchPattern {
      case Right(PSequenceLiteral(PIntType(), Vector(PIntLit(n))))
        if n == BigInt(42) =>
    }
  }

  test("Parser: integer sequence literal with multiple elements") {
    frontend.parseExp("seq[int] { 3, 17, 142 }") should matchPattern {
      case Right(PSequenceLiteral(PIntType(), xs))
        if xs == Vector(PIntLit(BigInt(3)), PIntLit(BigInt(17)), PIntLit(BigInt(142))) =>
    }
  }

  test("Parser: incomplete sequence literal") {
    frontend.parseExp("seq[bool]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: incorrectly opened sequence literal") {
    frontend.parseExp("seq[bool] true }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: incorrectly closed sequence literal") {
    frontend.parseExp("seq[bool] { true") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: incorrect singular sequence literal") {
    frontend.parseExp("seq[bool] { true, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: incorrect sequence literal with multiple elements") {
    frontend.parseExp("seq[bool] { true, false,, false }") should matchPattern {
      case Left(_) =>
    }
  }

  class TestFrontend {
    private def parse[T: ClassTag](source: String, parser: Source => Either[Messages, T]) : Either[Messages, T] =
      parser(StringSource(source))

    private def parseOrFail[T: ClassTag](source: String, parser: Source => Either[Messages, T]): T = {
      parse(source, parser) match {
        case Right(ast) => ast
        case Left(messages) => fail(s"Parsing failed: $messages")
      }
    }

    def parseExp(source : String) : Either[Messages, PExpression] = parse(source, Parser.parseExpr)
    def parseExpOrFail(source : String) : PExpression = parseOrFail(source, Parser.parseExpr)
    def parseStmt(source : String) : Either[Messages, PStatement] = parse(source, Parser.parseStmt)
    def parseStmtOrFail(source : String) : PStatement = parseOrFail(source, Parser.parseStmt)
    def parseType(source : String) : Either[Messages, PType] = parse(source, Parser.parseType)
    def parseTypeOrFail(source : String) : PType = parseOrFail(source, Parser.parseType)
  }
}
