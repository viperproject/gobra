package viper.gobra

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend.{PAssignment, PCompositeLit, PDot, PExpCompositeVal, PExplicitQualifiedImport, PExpression, PFunctionDecl, PFunctionSpec, PIdnDef, PIdnUnk, PIdnUse, PImplicitQualifiedImport, PImport, PIntLit, PInvoke, PKeyedElement, PLiteralValue, PMember, PNamedOperand, PResult, PShortVarDecl, PStatement, PUnqualifiedImport, PWildcard}
import viper.gobra.frontend.Parser

import scala.reflect.ClassTag

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

  test("Parser: Wildcard") {
    // PWildcard is not an expression
    an [TestFailedException] should be thrownBy frontend.parseExp("_")
  }

  test("Parser: multi import") {
    frontend.parseImportDecl("import (\"f\";\"g\")") should matchPattern {
      case Vector(PImplicitQualifiedImport("f"), PImplicitQualifiedImport("g")) =>
    }
  }

  test("Parser: dot import") {
    frontend.parseImportDecl("import . \"lib/math\"") should matchPattern {
      case Vector(PUnqualifiedImport("lib/math")) =>
    }
  }

  test("Parser: underscore import") {
    frontend.parseImportDecl("import _ \"lib/math\"") should matchPattern {
      case Vector(PExplicitQualifiedImport(PWildcard(), "lib/math")) =>
    }
  }

  test("Parser: default import") {
    frontend.parseImportDecl("import \"lib/math\"") should matchPattern {
      case Vector(PImplicitQualifiedImport("lib/math")) =>
    }
  }

  test("Parser: qualified import") {
    frontend.parseImportDecl("import m \"lib/math\"") should matchPattern {
      case Vector(PExplicitQualifiedImport(PIdnDef("m"), "lib/math")) =>
    }
  }

  test("Parser: spec only function") {
    frontend.parseMember("func foo() { b.bar() }", specOnly = true) should matchPattern {
      case Vector(PFunctionDecl(PIdnDef("foo"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), false), None)) =>
    }
  }

  test("Parser: spec only function with nested blocks") {
    frontend.parseMember("func foo() { if(true) { b.bar() } else { foo() } }", specOnly = true) should matchPattern {
      case Vector(PFunctionDecl(PIdnDef("foo"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), false), None)) =>
    }
  }

  test("Parser: spec only function with incomplete nested blocks") {
    an [TestFailedException] should be thrownBy
      frontend.parseMember("func foo() { if(true) { b.bar() } else { foo() }", specOnly = true)
  }

  test("Parser: imported struct initialization") {
    val parseRes = frontend.parseStmt("a := b.BarCell{10}")
    inside (parseRes) {
      case PShortVarDecl(Vector(PCompositeLit(PDot(PNamedOperand(PIdnUse("b")), PIdnUse("BarCell")),
        PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(value))))))), Vector(PIdnUnk("a")), Vector(false)) => value should be (10)
    }
  }

  class TestFrontend {
    private def parseOrFail[T: ClassTag](source: String, parser: Source => Either[Messages, T]): T = {
      parser(StringSource(source)) match {
        case Right(ast) => ast
        case Left(messages) => fail(s"Parsing failed: $messages")
      }
    }

    def parseStmt(source: String): PStatement = parseOrFail(source, Parser.parseStmt)
    def parseExp(source: String): PExpression = parseOrFail(source, Parser.parseExpr)
    def parseImportDecl(source: String): Vector[PImport] = parseOrFail(source, Parser.parseImportDecl)
    def parseMember(source: String, specOnly: Boolean = false): Vector[PMember] = parseOrFail(source, (s: Source) => Parser.parseMember(s, specOnly = specOnly))
  }
}
