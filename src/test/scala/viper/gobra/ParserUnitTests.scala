package viper.gobra

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend.{PAssignee, PAssignment, PDot, PExpression, PIdnDef, PIdnUse, PIntLit, PInvoke, PNamedOperand, PPointerType, PSeq, PStatement, PStringLit, PStringType, PVarDecl}
import viper.gobra.frontend.Parser

import scala.reflect.{ClassTag, classTag}

class ParserUnitTests extends FunSuite with Matchers with Inside {
  private val frontend = new TestFrontend()

  test("Parser: Dot") {
    frontend.parseExp("self.Contains") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Contains")) =>
    }
  }

  test("Parser: Invoke") {
    frontend.parseExp("Contains(v)") should matchPattern {
      case PInvoke(PNamedOperand(PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: DotInvoke") {
    frontend.parseExp("self.Contains(v)") should matchPattern {
      case PInvoke(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: NestedDot") {
    frontend.parseExp("(self.Left)") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")) =>
    }
  }

  test("Parser: DoubleDotInvoke1") {
    frontend.parseExp("(self.Left).Contains(v)") should matchPattern {
      case PInvoke(PDot(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: DoubleDotInvoke2") {
    frontend.parseExp("self.Left.Contains(v)") should matchPattern {
      case PInvoke(PDot(PDot(PNamedOperand(PIdnUse("self")), PIdnUse("Left")), PIdnUse("Contains")), Vector(PNamedOperand(PIdnUse("v")))) =>
    }
  }

  test("Parser: string conversion") {
    // 0xf8 == 248
    val parseRes = frontend.parseExp("string(248)")
    inside (parseRes) {
      case PInvoke(PStringType(), Vector(PIntLit(value))) => value should be (0xf8)
    }
  }

  test("Parser: raw string") {
    frontend.parseExp("`Hello World`") should matchPattern {
      case PStringLit("Hello World") =>
    }
  }

  test("Parser: interpreted string") {
    frontend.parseExp("\"Hello World\"") should matchPattern {
      case PStringLit("Hello World") =>
    }
  }

  test("Parser: interpreted string with a quote") {
    frontend.parseExp("\"\\\"\"") should matchPattern {
      case PStringLit("""\"""") =>
    }
  }

  test("Parser: assignment constant to variable") {
    val parseRes = frontend.parseStmt("p = 5")
    inside (parseRes) {
      case PAssignment(Vector(PIntLit(value)),
      Vector(PNamedOperand(PIdnUse("p")))) => value should be (5)
    }
  }

  test("Parser: field access") {
    frontend.parseExp("p.x") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")) =>
    }
  }

  test("Parser: field access assignee") {
    frontend.parseAssignee("p.x") should matchPattern {
      case PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")) =>
    }
  }

  test("Parser: assignment constant to field") {
    val parseRes = frontend.parseStmt("p.x = 5")
    inside (parseRes) {
      case PAssignment(Vector(PIntLit(value)),
        Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")))) => value should be (5)
    }
  }

  test("Parser: assignment field to field") {
    frontend.parseStmt("p.x = p.y") should matchPattern {
      case PAssignment(Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("y"))),
        Vector(PDot(PNamedOperand(PIdnUse("p")), PIdnUse("x")))) =>
    }
  }

  test("Parser: package type") {
    frontend.parseStmt("var w http.ResponseWriter") should matchPattern {
      case PSeq(Vector(PVarDecl(Some(PDot(PNamedOperand(PIdnUse("http")), PIdnUse("ResponseWriter"))),
        Vector(), Vector(PIdnDef("w")), Vector(false)))) =>
    }
  }

  test("Parser: package pointer type") {
    frontend.parseStmt("var r *http.Request") should matchPattern {
      case PSeq(Vector(PVarDecl(Some(PPointerType(PDot(PNamedOperand(PIdnUse("http")), PIdnUse("Request")))),
        Vector(), Vector(PIdnDef("r")), Vector(false)))) =>
    }
  }

  class TestFrontend {
    private def parseOrFail[T: ClassTag](source: String, parser: Source => Either[Messages, T]): T = {
      parser(StringSource(source)) match {
        case Right(ast) if classTag[T].runtimeClass.isAssignableFrom(ast.getClass) => ast
        case Left(messages) => sys.error(s"Parsing failed: $messages")
        case Right(ast) => sys.error(s"Parsing resulted in unexpected AST node ${ast.getClass.getSimpleName}")
      }
    }

    def parseStmt(source: String): PStatement = parseOrFail(source, Parser.parseStmt)
    def parseExp(source: String): PExpression = parseOrFail(source, Parser.parseExpr)
    def parseAssignee(source: String): PAssignee = parseOrFail(source, Parser.parseAssignee)
  }
}
