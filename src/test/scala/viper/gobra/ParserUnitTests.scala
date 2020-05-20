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

  test("Parser: singleton integer sequence literal") {
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

  test("Parser: incorrect singleton sequence literal") {
    frontend.parseExp("seq[bool] { true, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: incorrect sequence literal with multiple elements") {
    frontend.parseExp("seq[bool] { true, false,, false }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: appending two simple sequences") {
    frontend.parseExpOrFail("xs ++ ys" ) should matchPattern {
      case PSequenceAppend(PNamedOperand(PIdnUse("xs")), PNamedOperand(PIdnUse("ys"))) =>
    }
  }

  test("Parser: appending two Booleans as sequences") {
    frontend.parseExpOrFail("true ++ false" ) should matchPattern {
      case PSequenceAppend(PBoolLit(true), PBoolLit(false)) =>
    }
  }

  test("Parser: appending three sequences (1)") {
    frontend.parseExpOrFail("xs ++ ys ++ zs" ) should matchPattern {
      case PSequenceAppend(
        PSequenceAppend(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("ys"))
        ),
        PNamedOperand(PIdnUse("zs"))
      ) =>
    }
  }

  test("Parser: appending three sequences (2)") {
    frontend.parseExpOrFail("xs ++ (ys ++ zs)" ) should matchPattern {
      case PSequenceAppend(
        PNamedOperand(PIdnUse("xs")),
        PSequenceAppend(
          PNamedOperand(PIdnUse("ys")),
          PNamedOperand(PIdnUse("zs"))
        )
      ) =>
    }
  }

  test("Parser: expressions of the form '+e' should be parsed to '0 + e'") {
    frontend.parseExpOrFail("+x") should matchPattern {
      case PAdd(PIntLit(n), PNamedOperand(PIdnUse("x"))) if n == BigInt(0) =>
    }
  }

  test("Parser: expressions of the form '++e' should be parsed to '0 + 0 + e'") {
    frontend.parseExp("++ x") should matchPattern {
      case Right(PAdd(PIntLit(a), PAdd(PIntLit(b), PNamedOperand(PIdnUse("x")))))
        if a == BigInt(0) && b == BigInt(0) =>
    }
  }

  test("Parser: expressions of the form 'e++' should not be parsed") {
    frontend.parseExp("x ++") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: expressions of the form 'e1 + + e2' should be parsed as 'e1 + (+e2)'") {
    frontend.parseExpOrFail("x + + y") should matchPattern {
      case PAdd(PNamedOperand(PIdnUse("x")), PAdd(PIntLit(n), PNamedOperand(PIdnUse("y"))))
        if n == BigInt(0) =>
    }
  }

  test("Parser: expressions of the form 'e+' should not be parsed") {
    frontend.parseExp("x+") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: appending two sequence literals") {
    frontend.parseExpOrFail("seq[bool] { true } ++ seq[bool] { false, true }") should matchPattern {
      case PSequenceAppend(
        PSequenceLiteral(PBoolType(), Vector(PBoolLit(true))),
        PSequenceLiteral(PBoolType(), Vector(PBoolLit(false), PBoolLit(true)))
      ) =>
    }
  }

  test("Parser: should be able to parse basic '|e|'-shaped expressions") {
    frontend.parseExp("|xs|") should matchPattern {
      case Right(PSize(PNamedOperand(PIdnUse("xs")))) =>
    }
  }

  test ("Parser: length of concatenated sequences") {
    frontend.parseExp("|xs ++ ys|") should matchPattern {
      case Right(
        PSize(
          PSequenceAppend(
            PNamedOperand(PIdnUse("xs")),
            PNamedOperand(PIdnUse("ys"))
          )
        )
      ) =>
    }
  }

  test("Parser: should be able to parse simple disjunctions") {
    frontend.parseExpOrFail("x || y") should matchPattern {
      case POr(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y"))) =>
    }
  }

  test("Parser: simple sequence update") {
    frontend.parseExpOrFail("xs[i = 42]") should matchPattern {
      case PSequenceUpdate(
        PNamedOperand(PIdnUse("xs")),
        PNamedOperand(PIdnUse("i")),
        PIntLit(n)
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: sequence update with an append on left-hand side") {
    frontend.parseExp("(xs ++ ys)[i = true]") should matchPattern {
      case Right(PSequenceUpdate(
        PSequenceAppend(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("ys"))
        ),
      PNamedOperand(PIdnUse("i")),
      PBoolLit(true)
      )) =>
    }
  }

  test("Parser: sequence update in combination with append") {
    frontend.parseExp("xs ++ ys[i = v]") should matchPattern {
      case Right(PSequenceAppend(
        PNamedOperand(PIdnUse("xs")),
        PSequenceUpdate(
          PNamedOperand(PIdnUse("ys")),
          PNamedOperand(PIdnUse("i")),
          PNamedOperand(PIdnUse("v"))
        )
      )) =>
    }
  }

  test("Parser: taking the length of sequence update expression") {
    frontend.parseExp("|xs[x = false]|") should matchPattern {
      case Right(PSize(
        PSequenceUpdate(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("x")),
          PBoolLit(false)
        )
      )) =>
    }
  }

  test("Parser: updating sequence literals") {
    frontend.parseExp("seq[bool] { true, false, false }[1 = true]") should matchPattern {
      case Right(PSequenceUpdate(
        PSequenceLiteral(
          PBoolType(),
          Vector(PBoolLit(true), PBoolLit(false), PBoolLit(false))
        ),
        PIntLit(i),
        PBoolLit(true)
      )) if i == BigInt(1) =>
    }
  }

  test("Parser: chaining sequence updates") {
    frontend.parseExp("xs[i = true][j = false]") should matchPattern {
      case Right(PSequenceUpdate(
        PSequenceUpdate(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("i")),
          PBoolLit(true)
        ),
        PNamedOperand(PIdnUse("j")),
        PBoolLit(false)
      )) =>
    }
  }

  test("Parser: nested sequence updates") {
    frontend.parseExp("xs[i = ys[j = v]]") should matchPattern {
      case Right(PSequenceUpdate(
        PNamedOperand(PIdnUse("xs")),
        PNamedOperand(PIdnUse("i")),
        PSequenceUpdate(
        PNamedOperand(PIdnUse("ys")),
          PNamedOperand(PIdnUse("j")),
          PNamedOperand(PIdnUse("v"))
        )
      )) =>
    }
  }

  test("Parser: should not parse incorrectly typed sequence update (1)") {
    frontend.parseExp("xs[x := v]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse incorrectly typed sequence update (2)") {
    frontend.parseExp("xs[x = v") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse incorrectly typed sequence update (3)") {
    frontend.parseExp("xs[x =]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a simple sequence range expression") {
    frontend.parseExpOrFail("seq[1..5]") should matchPattern {
      case PRangeSequence(PIntLit(low), PIntLit(high))
        if low == BigInt(1) && high == BigInt(5) =>
    }
  }

  test("Parser: should parse a slightly more complex sequence range expression") {
    frontend.parseExpOrFail("seq[x + y .. |seq[bool] { true }|]") should matchPattern {
      case PRangeSequence(
        PAdd(
          PNamedOperand(PIdnUse("x")),
          PNamedOperand(PIdnUse("y"))
        ),
        PSize(
          PSequenceLiteral(
            PBoolType(),
            Vector(PBoolLit(true))
          )
        )
      ) =>
    }
  }

  test("Parser: should not allow sequence range expressions to have the left-hand side optional") {
    frontend.parseExp("seq[..x]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not allow sequence range expressions to have the right-hand side optional") {
    frontend.parseExp("seq[x..]") should matchPattern {
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
