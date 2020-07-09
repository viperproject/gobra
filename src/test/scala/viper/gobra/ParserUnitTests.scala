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

  test("Parser: Wildcard") {
    // PWildcard is not an expression
    frontend.parseExp("_") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: multi import") {
    frontend.parseImportDecl("import (\"f\";\"g\")") should matchPattern {
      case Vector(PQualifiedImport(None, "f"), PQualifiedImport(None, "g")) =>
    }
  }

  test("Parser: dot import") {
    frontend.parseImportDecl("import . \"lib/math\"") should matchPattern {
      case Vector(PUnqualifiedImport("lib/math")) =>
    }
  }

  test("Parser: underscore import") {
    frontend.parseImportDecl("import _ \"lib/math\"") should matchPattern {
      case Vector(PQualifiedImport(Some(PWildcard()), "lib/math")) =>
    }
  }

  test("Parser: default import") {
    frontend.parseImportDecl("import \"lib/math\"") should matchPattern {
      case Vector(PQualifiedImport(None, "lib/math")) =>
    }
  }

  test("Parser: qualified import") {
    frontend.parseImportDecl("import m \"lib/math\"") should matchPattern {
      case Vector(PQualifiedImport(Some(PIdnDef("m")), "lib/math")) =>
    }
  }


  /* ** Mathematical sequences */

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

  test("Parser: should have the '++' operator associate to the left") {
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
      case Right(PCardinality(PNamedOperand(PIdnUse("xs")))) =>
    }
  }

  test ("Parser: length of concatenated sequences") {
    frontend.parseExp("|xs ++ ys|") should matchPattern {
      case Right(
      PCardinality(
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
        Vector(
          PSequenceUpdateClause(
            PNamedOperand(PIdnUse("i")),
            PIntLit(n)
          )
        )
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
        Vector(
          PSequenceUpdateClause(
            PNamedOperand(PIdnUse("i")),
            PBoolLit(true)
          )
        )
      )) =>
    }
  }

  test("Parser: sequence update in combination with append") {
    frontend.parseExp("xs ++ ys[i = v]") should matchPattern {
      case Right(PSequenceAppend(
        PNamedOperand(PIdnUse("xs")),
        PSequenceUpdate(
          PNamedOperand(PIdnUse("ys")),
          Vector(
            PSequenceUpdateClause(
              PNamedOperand(PIdnUse("i")),
              PNamedOperand(PIdnUse("v"))
            )
          )
        )
      )) =>
    }
  }

  test("Parser: taking the length of sequence update expression") {
    frontend.parseExp("|xs[x = false]|") should matchPattern {
      case Right(PCardinality(
        PSequenceUpdate(
          PNamedOperand(PIdnUse("xs")),
          Vector(
            PSequenceUpdateClause(
              PNamedOperand(PIdnUse("x")),
              PBoolLit(false)
            )
          )
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
        Vector(PSequenceUpdateClause(PIntLit(i), PBoolLit(true)))
      )) if i == BigInt(1) =>
    }
  }

  test("Parser: chaining sequence updates") {
    frontend.parseExp("xs[i = true][j = false]") should matchPattern {
      case Right(PSequenceUpdate(
        PSequenceUpdate(
          PNamedOperand(PIdnUse("xs")),
          Vector(
            PSequenceUpdateClause(
              PNamedOperand(PIdnUse("i")),
              PBoolLit(true)
            )
          )
        ),
        Vector(
          PSequenceUpdateClause(
            PNamedOperand(PIdnUse("j")),
            PBoolLit(false)
          )
        )
      )) =>
    }
  }

  test("Parser: nested sequence updates") {
    frontend.parseExp("xs[i = ys[j = v]]") should matchPattern {
      case Right(PSequenceUpdate(
        PNamedOperand(PIdnUse("xs")),
        Vector(
          PSequenceUpdateClause(
            PNamedOperand(PIdnUse("i")),
            PSequenceUpdate(
              PNamedOperand(PIdnUse("ys")),
              Vector(
                PSequenceUpdateClause(
                  PNamedOperand(PIdnUse("j")),
                  PNamedOperand(PIdnUse("v"))
                )
              )
            )
          )
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
        PCardinality(
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

  test("Parser: should be able to parse simple sequence membership expressions") {
    frontend.parseExpOrFail("x in xs") should matchPattern {
      case PIn(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should have membership expressions associate to the left") {
    frontend.parseExp("x in xs in ys") should matchPattern {
      case Right(PIn(
        PIn(
          PNamedOperand(PIdnUse("x")),
          PNamedOperand(PIdnUse("xs"))
        ),
        PNamedOperand(PIdnUse("ys"))
      )) =>
    }
  }

  test("Parser: should parse a simple chain of membership expressions with parentheses left") {
    frontend.parseExp("(x in xs) in ys") should matchPattern {
      case Right(PIn(
        PIn(
          PNamedOperand(PIdnUse("x")),
          PNamedOperand(PIdnUse("xs"))
        ),
        PNamedOperand(PIdnUse("ys"))
      )) =>
    }
  }

  test("Parser: should parse a simple chain of membership expressions with parentheses right") {
    frontend.parseExp("x in (xs in ys)") should matchPattern {
      case Right(PIn(
        PNamedOperand(PIdnUse("x")),
        PIn(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("ys"))
        )
      )) =>
    }
  }

  test("Parser: should not parse a membership expression with missing left-hand side") {
    frontend.parseExp("in xs") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a membership expression with missing right-hand side") {
    frontend.parseExp("x in") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse 'in' as a keyword") {
    frontend.parseExp("in") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a membership expression with a sequence range expression") {
    frontend.parseExpOrFail("x + 12 in seq[1..100]") should matchPattern {
      case PIn(
        PAdd(PNamedOperand(PIdnUse("x")), PIntLit(a)),
        PRangeSequence(PIntLit(b), PIntLit(c))
      ) if a == BigInt(12) && b == BigInt(1) && c == BigInt(100) =>
    }
  }

  test("Parser: should not parse a sequence update expression without any updates") {
    frontend.parseExp("xs[]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a sequence update expression with three clauses") {
    frontend.parseExpOrFail("xs[1 = true,2 = b , 7 = |xs|]") should matchPattern {
      case PSequenceUpdate(
        PNamedOperand(PIdnUse("xs")),
        Vector(
          PSequenceUpdateClause(PIntLit(a), PBoolLit(true)),
          PSequenceUpdateClause(PIntLit(b), PNamedOperand(PIdnUse("b"))),
          PSequenceUpdateClause(PIntLit(c), PCardinality(PNamedOperand(PIdnUse("xs"))))
        )
      ) if a == BigInt(1) && b == BigInt(2) && c == BigInt(7) =>
    }
  }

  test("Parser: should not parse a sequence update with incomplete clauses") {
    frontend.parseExp("xs[1 = true, ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse any sequence update with an incorrectly starting clause") {
    frontend.parseExp("xs[,2 = false]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse any sequence update with only commas as clauses") {
    frontend.parseExp("ys[,,,]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse simple indexed expressions") {
    frontend.parseExpOrFail("xs[i]") should matchPattern {
      case PIndexedExp(PNamedOperand(PIdnUse("xs")), PNamedOperand(PIdnUse("i"))) =>
    }
  }

  test("Parser: should be able to parse slightly complex indexed expressions") {
    frontend.parseExpOrFail("(xs ++ ys)[|zs| + 2]") should matchPattern {
      case PIndexedExp(
        PSequenceAppend(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("ys"))
        ),
        PAdd(
          PCardinality(PNamedOperand(PIdnUse("zs"))),
          PIntLit(n)
        )
      ) if n == BigInt(2) =>
    }
  }

  test("Parser: should be able to parse a chain of indexed expressions") {
    frontend.parseExpOrFail("xs[i][j][k]") should matchPattern {
      case PIndexedExp(
        PIndexedExp(
          PIndexedExp(
            PNamedOperand(PIdnUse("xs")),
            PNamedOperand(PIdnUse("i")),
          ),
          PNamedOperand(PIdnUse("j")),
        ),
        PNamedOperand(PIdnUse("k")),
      ) =>
    }
  }

  test("Parser: shouldn't parse an indexed expression with a missing opening bracket") {
    frontend.parseExp("xs]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: shouldn't parse an indexed expression with a missing closing bracket") {
    frontend.parseExp("xs[2") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: shouldn't parse an indexed expression with too many opening brackets") {
    frontend.parseExp("xs[[2]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: shouldn't parse an indexed expression with too many closing brackets") {
    frontend.parseExp("xs[2]]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse an indexed expression together with a sequence literal") {
    frontend.parseExpOrFail("seq[bool] { true, false }[1]") should matchPattern {
      case PIndexedExp(
        PSequenceLiteral(PBoolType(), Vector(PBoolLit(true), PBoolLit(false))),
        PIntLit(n)
      ) if n == BigInt(1) =>
    }
  }

  test("Parser: should parse indexed expression with sequence range expressions") {
    frontend.parseExpOrFail("seq[1..10][2]") should matchPattern {
      case PIndexedExp(
        PRangeSequence(PIntLit(low), PIntLit(high)),
        PIntLit(i)
      ) if low == BigInt(1) && high == BigInt(10) && i == BigInt(2) =>
    }
  }

  test("Parser: shouldn't parse a chain of sequence range operations") {
    frontend.parseExp("seq[1..10][11..20]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a slicing expression with two 'range' indices") {
    frontend.parseExpOrFail("xs[i:j]") should matchPattern {
      case PSliceExp(
        PNamedOperand(PIdnUse("xs")),
        Some(PNamedOperand(PIdnUse("i"))),
        Some(PNamedOperand(PIdnUse("j"))),
        None
      ) =>
    }
  }

  test("Parser: should parse a slicing expression with three 'range' indices") {
    frontend.parseExpOrFail("xs[i:j:k]") should matchPattern {
      case PSliceExp(
        PNamedOperand(PIdnUse("xs")),
        Some(PNamedOperand(PIdnUse("i"))),
        Some(PNamedOperand(PIdnUse("j"))),
        Some(PNamedOperand(PIdnUse("k")))
      ) =>
    }
  }

  test("Parser: should not parse a slicing expression with an expected but missing capacity") {
    frontend.parseExp("xs[i:j:]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse slice expressions with only a 'low' index") {
    frontend.parseExpOrFail("xs[i:]") should matchPattern {
      case PSliceExp(
        PNamedOperand(PIdnUse("xs")),
        Some(PNamedOperand(PIdnUse("i"))),
        None,
        None
      ) =>
    }
  }

  test("Parser: should be able to parse slice expressions with only a 'high' index") {
    frontend.parseExpOrFail("zs[:42]") should matchPattern {
      case PSliceExp(
        PNamedOperand(PIdnUse("zs")),
        None,
        Some(PIntLit(n)),
        None
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse slice expression without a 'low' or 'high' index") {
    frontend.parseExpOrFail("xs[:]") should matchPattern {
      case PSliceExp(
        PNamedOperand(PIdnUse("xs")),
        None,
        None,
        None
      ) =>
    }
  }

  test("Parser: should not be able to parse slice expressions without any indices while three were expected") {
    frontend.parseExp("xs[::]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse slice expressions with only a 'high' index while three were expected") {
    frontend.parseExp("xs[:i:]") should matchPattern {
      case Left(_) =>
    }
  }


  /* ** Mathematical sets */

  test("Parser: should parse standard (integer) set types as expected") {
    frontend.parseTypeOrFail("set[int]") should matchPattern {
      case PSetType(PIntType()) =>
    }
  }

  test("Parser: should parse standard set types with spacings as expected") {
    frontend.parseTypeOrFail(" set [ int ] ") should matchPattern {
      case PSetType(PIntType()) =>
    }
  }

  test("Parser: should parse nested set types as expected") {
    frontend.parseTypeOrFail("set[set[bool]]") should matchPattern {
      case PSetType(PSetType(PBoolType())) =>
    }
  }

  test("Parser: should not parse set types with a missing opening square bracket") {
    frontend.parseType("set int]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse set types with a missing closing square bracket (1)") {
    frontend.parseType("set [ int ") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse set types with a missing closing square bracket (2)") {
    frontend.parseType("set [ seq[bool ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a simple empty integer set literal") {
    frontend.parseExpOrFail("set[int] {  }") should matchPattern {
      case PSetLiteral(PIntType(), Vector()) =>
    }
  }

  test("Parser: should parse a simple empty integer set literal with some spaces added") {
    frontend.parseExpOrFail("set [ int ] {}") should matchPattern {
      case PSetLiteral(PIntType(), Vector()) =>
    }
  }

  test("Parser: should not parse an empty integer set literal with a missing opening bracet") {
    frontend.parseExp("set[int]  }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an empty integer set literal with a missing closing bracet") {
    frontend.parseExp("set[int] {") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer set literal with just a comma in it") {
    frontend.parseExp("set[int] { , }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer set literal with a missing opening bracket") {
    frontend.parseExp("set int] { }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer set literal with a missing closing bracket") {
    frontend.parseExp("set[int { }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a singleton integer set literal") {
    frontend.parseExpOrFail("set[int] { 42 }") should matchPattern {
      case PSetLiteral(PIntType(), Vector(PIntLit(n)))
        if n == BigInt(42) =>
    }
  }

  test("Parser: should not be able to parse a singleton integer set literal with a wrongly placed extra comma (1)") {
    frontend.parseExp("set[int] { ,42 }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a singleton integer set literal with a wrongly placed extra comma (2)") {
    frontend.parseExp("set[int] { 42, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a Boolean set literal with multiple elements") {
    frontend.parseExpOrFail("set[bool] { true, false, true }") should matchPattern {
      case PSetLiteral(PBoolType(), Vector(
        PBoolLit(true),
        PBoolLit(false),
        PBoolLit(true)
      )) =>
    }
  }

  test("Parser: should be able to parse a set literal with a nested type") {
    frontend.parseExpOrFail("set[set[set[bool]]] { }") should matchPattern {
      case PSetLiteral(
        PSetType(PSetType(PBoolType())),
        Vector()
      ) =>
    }
  }

  test("Parser: should be able to parse nested set literals") {
    frontend.parseExpOrFail("set[bool] { set[int] { 42 } }") should matchPattern {
      case PSetLiteral(
        PBoolType(),
        Vector(
          PSetLiteral(PIntType(), Vector(PIntLit(n)))
        )
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should not be able to parse 'seq' as an identifier") {
    frontend.parseExp("seq") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse 'set' as an identifier") {
    frontend.parseExp("set") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse 'union' as an identifier") {
    frontend.parseExp("union") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a simple set union expression") {
    frontend.parseExpOrFail("s union t") should matchPattern {
      case PUnion(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ) =>
    }
  }

  test("Parser: set union should by default associate to the left") {
    frontend.parseExpOrFail("s union t union u") should matchPattern {
      case PUnion(
        PUnion(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t"))
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: set union with parentheses should correctly be parsed") {
    frontend.parseExpOrFail("s union (t union u)") should matchPattern {
      case PUnion(
        PNamedOperand(PIdnUse("s")),
        PUnion(
          PNamedOperand(PIdnUse("t")),
          PNamedOperand(PIdnUse("u"))
        )
      ) =>
    }
  }

  test("Parser: should not be able to parse set union with missing left-hand side") {
    frontend.parseExp("union t") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse set union with missing right-hand side") {
    frontend.parseExp("s union") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse simple set intersection") {
    frontend.parseExpOrFail("s intersection t") should matchPattern {
      case PIntersection(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ) =>
    }
  }

  test("Parser: should have set intersection associate to the left") {
    frontend.parseExpOrFail("s intersection t intersection u") should matchPattern {
      case PIntersection(
        PIntersection(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t"))
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: should let set intersection correctly handle parentheses") {
    frontend.parseExpOrFail("s intersection (t intersection u)") should matchPattern {
      case PIntersection(
        PNamedOperand(PIdnUse("s")),
        PIntersection(
          PNamedOperand(PIdnUse("t")),
          PNamedOperand(PIdnUse("u"))
        )
      ) =>
    }
  }

  test("Parser: should not let 'intersection' be parsed as an identifier") {
    frontend.parseExp("intersection") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse set intersection with missing left-hand side") {
    frontend.parseExp("intersection t") should matchPattern {
      case Left(_) =>
    }
  }

  // TODO is this desirable?
  test("Parser: should parse set intersection with missing right-hand side as set/seq inclusion") {
    frontend.parseExpOrFail("s intersection") should matchPattern {
      case PIn(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("tersection")),
      ) =>
    }
  }

  test("Parser: should correctly parse set intersection with literals") {
    frontend.parseExpOrFail("set[bool] { true } intersection set[int] { }") should matchPattern {
      case PIntersection(
        PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
        PSetLiteral(PIntType(), Vector())
      ) =>
    }
  }

  test("Parser: should let set union and intersection have the same precedence") {
    frontend.parseExpOrFail("s union t intersection u") should matchPattern {
      case PIntersection(
        PUnion(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t")),
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: should be able to parse simple set differences") {
    frontend.parseExpOrFail("s setminus t") should matchPattern {
      case PSetMinus(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ) =>
    }
  }

  test("Parser: should have set difference associate to the left") {
    frontend.parseExpOrFail("s setminus t setminus u") should matchPattern {
      case PSetMinus(
      PSetMinus(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t"))
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: should let set difference correctly handle parentheses") {
    frontend.parseExpOrFail("s setminus (t setminus u)") should matchPattern {
      case PSetMinus(
        PNamedOperand(PIdnUse("s")),
        PSetMinus(
          PNamedOperand(PIdnUse("t")),
          PNamedOperand(PIdnUse("u"))
        )
      ) =>
    }
  }

  test("Parser: should not let 'setminus' be parsed as an identifier") {
    frontend.parseExp("setminus") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse set difference with missing left-hand side") {
    frontend.parseExp("setminus t") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse set difference with missing right-hand side") {
    frontend.parseExp("s setminus") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should correctly parse set difference with literals") {
    frontend.parseExpOrFail("set[bool] { true } setminus set[int] { }") should matchPattern {
      case PSetMinus(
        PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
        PSetLiteral(PIntType(), Vector())
      ) =>
    }
  }

  test("Parser: should let set union and difference have the same precedence") {
    frontend.parseExpOrFail("s union t setminus u") should matchPattern {
      case PSetMinus(
        PUnion(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t")),
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: should parse a standard use of the subset relation") {
    frontend.parseExpOrFail("s subset t") should matchPattern {
      case PSubset(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ) =>
    }
  }

  test("Parser: should let the subset relation associate to the left") {
    frontend.parseExpOrFail("s subset t subset u") should matchPattern {
      case PSubset(
        PSubset(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t"))
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: should let a subset relation correctly handle parentheses") {
    frontend.parseExpOrFail("s subset (t subset u)") should matchPattern {
      case PSubset(
        PNamedOperand(PIdnUse("s")),
        PSubset(
          PNamedOperand(PIdnUse("t")),
          PNamedOperand(PIdnUse("u"))
        )
      ) =>
    }
  }

  test("Parser: should not be able to parse 'subset' as an identifier") {
    frontend.parseExp("subset") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a use of the subset relation with a missing left-hand side") {
    frontend.parseExp("subset t") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a use of the subset relation with a missing right-hand side") {
    frontend.parseExp("s subset") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a subset relation in combination with set literals") {
    frontend.parseExpOrFail("set[bool] { true } subset set[int] { 42 }") should matchPattern {
      case PSubset(
        PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
        PSetLiteral(PIntType(), Vector(PIntLit(n)))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse the type of integer multisets") {
    frontend.parseTypeOrFail("mset[int]") should matchPattern {
      case PMultisetType(PIntType()) =>
    }
  }

  test("Parser: should be able to parse a nested multiset type") {
    frontend.parseTypeOrFail("mset[mset[bool]]") should matchPattern {
      case PMultisetType(PMultisetType(PBoolType())) =>
    }
  }

  test("Parser: should not be able to parse a multiset type with missing opening bracket") {
    frontend.parseType("mset int]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a multiset type with missing closing bracket") {
    frontend.parseType("mset [ int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse 'mset' as an identifier") {
    frontend.parseType("mset") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to correctly parse an empty Boolean multiset literal") {
    frontend.parseExpOrFail("mset[bool] { }") should matchPattern {
      case PMultisetLiteral(PBoolType(), Vector()) =>
    }
  }

  test("Parser: should be able to correctly parse an empty integer multiset literal") {
    frontend.parseExpOrFail("mset [ int ]{}") should matchPattern {
      case PMultisetLiteral(PIntType(), Vector()) =>
    }
  }

  test("Parser: should be able to correctly parse an empty multiset literal with a nested type") {
    frontend.parseExpOrFail("mset[mset[mset[bool]]] { }") should matchPattern {
      case PMultisetLiteral(PMultisetType(PMultisetType(PBoolType())), Vector()) =>
    }
  }

  test("Parser: should not be able to parse an empty multiset literal with a missing opening bracket") {
    frontend.parseExp("mset int] { }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse an empty multiset literal with a missing closing bracket") {
    frontend.parseExp("mset [int { }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse an empty multiset literal with a missing opening curly bracket") {
    frontend.parseExp("mset [bool] }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse an empty multiset literal with a missing closing curly bracket") {
    frontend.parseExp("mset [bool] {") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a singleton Boolean multiset literal") {
    frontend.parseExpOrFail("mset[bool] { false }") should matchPattern {
      case PMultisetLiteral(PBoolType(), Vector(
        PBoolLit(false)
      )) =>
    }
  }

  test("Parser: should not be able to parse a multiset literal with just a comma inside") {
    frontend.parseExp("mset[int] { , }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a singleton integer multiset literal with a missing opening curly bracket") {
    frontend.parseExp("mset[int] 42 }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a singleton integer multiset literal with a missing closing curly bracket") {
    frontend.parseExp("mset[int] { 42") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a Boolean multiset literal with multiple elements") {
    frontend.parseExpOrFail("mset[bool] { true, false, false }") should matchPattern {
      case PMultisetLiteral(PBoolType(), Vector(
        PBoolLit(true),
        PBoolLit(false),
        PBoolLit(false)
      )) =>
    }
  }

  test("Parser: should not be able to parse a Boolean multiset literal with a missing comma") {
    frontend.parseExp("mset[bool] { true false }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer multiset literal with an extra comma on the left") {
    frontend.parseExp("mset[int] { ,1, 2 }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer multiset literal with an extra comma on the right") {
    frontend.parseExp("mset[int] { 1, 2, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer multiset literal with an extra comma in the middle") {
    frontend.parseExp("mset[int] { 1,, 2 }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a union of two multiset literals") {
    frontend.parseExpOrFail("mset[bool] { true } union mset[int] { 2 }") should matchPattern {
      case PUnion(
        PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(n)))
      ) if n == BigInt(2) =>
    }
  }

  test("Parser: should be able to parse an intersection of two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 42 } intersection mset[bool] { false }") should matchPattern {
      case PIntersection(
        PMultisetLiteral(PIntType(), Vector(PIntLit(n))),
        PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse the set difference of two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 42 } setminus mset[bool] { true }") should matchPattern {
      case PSetMinus(
        PMultisetLiteral(PIntType(), Vector(PIntLit(n))),
        PMultisetLiteral(PBoolType(), Vector(PBoolLit(true)))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse a subset relation applied to two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 12, 24 } subset mset[bool] { false }") should matchPattern {
      case PSubset(
        PMultisetLiteral(PIntType(), Vector(PIntLit(n1), PIntLit(n2))),
        PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
      ) if n1 == BigInt(12) && n2 == BigInt(24) =>
    }
  }

  test("Parser: should be able to correctly parse multiset cardinality") {
    frontend.parseExpOrFail("|mset[bool] { }|") should matchPattern {
      case PCardinality(PMultisetLiteral(PBoolType(), Vector())) =>
    }
  }

  test("Parser: should correctly parse multiset inclusion (1)") {
    frontend.parseExpOrFail("true in mset[bool] { false, true }") should matchPattern {
      case PIn(
        PBoolLit(true),
        PMultisetLiteral(PBoolType(), Vector(PBoolLit(false), PBoolLit(true)))
      ) =>
    }
  }

  test("Parser: should correctly parse multiset inclusion (2)") {
    frontend.parseExpOrFail("mset[int] { } in mset[bool] { }") should matchPattern {
      case PIn(
        PMultisetLiteral(PIntType(), Vector()),
        PMultisetLiteral(PBoolType(), Vector())
      ) =>
    }
  }

  test("Parser: should correctly parse a comparison of (multi)set inclusions") {
    frontend.parseExpOrFail("x in s == y in s") should matchPattern {
      case PEquals(
        PIn(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("s"))),
        PIn(PNamedOperand(PIdnUse("y")), PNamedOperand(PIdnUse("s")))
      ) =>
    }
  }

  test("Parser: should correctly parse a comparison of (multi)set unions") {
    frontend.parseExpOrFail("a union b == c union d") should matchPattern {
      case PEquals(
        PUnion(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PUnion(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should correctly parse a comparison of (multi)set intersections") {
    frontend.parseExpOrFail("a intersection b == c intersection d") should matchPattern {
      case PEquals(
        PIntersection(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
      PIntersection(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should correctly parse a comparison of (multi)set differences") {
    frontend.parseExpOrFail("a setminus b == c setminus d") should matchPattern {
      case PEquals(
        PSetMinus(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PSetMinus(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to parse a comparison of set cardinality expressions") {
    frontend.parseExpOrFail("|s| == |t|") should matchPattern {
      case PEquals(
        PCardinality(PNamedOperand(PIdnUse("s"))),
        PCardinality(PNamedOperand(PIdnUse("t")))
      ) =>
    }
  }

  test("Parser: should correctly parse a comparison of (multi)set subset expressions") {
    frontend.parseExpOrFail("a subset b == c subset d") should matchPattern {
      case PEquals(
        PSubset(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PSubset(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set inclusion in combination with ordinary addition (1)") {
    frontend.parseExpOrFail("a in b + c") should matchPattern {
      case PIn(
        PNamedOperand(PIdnUse("a")),
        PAdd(PNamedOperand(PIdnUse("b")), PNamedOperand(PIdnUse("c")))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set inclusion in combination with ordinary addition (2)") {
    frontend.parseExpOrFail("a + b in c") should matchPattern {
      case PIn(
        PAdd(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PNamedOperand(PIdnUse("c"))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set inclusion in combination with ordinary addition (3)") {
    frontend.parseExpOrFail("a in b + c in d") should matchPattern {
      case PIn(
        PIn(
          PNamedOperand(PIdnUse("a")),
          PAdd(PNamedOperand(PIdnUse("b")), PNamedOperand(PIdnUse("c")))
        ),
        PNamedOperand(PIdnUse("d"))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set union in combination with addition") {
    frontend.parseExpOrFail("a + b union c + d") should matchPattern {
      case PUnion(
        PAdd(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PAdd(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set intersection in combination with addition") {
    frontend.parseExpOrFail("a + b intersection c + d") should matchPattern {
      case PIntersection(
        PAdd(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PAdd(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to parse a (multi)set difference in combination with addition") {
    frontend.parseExpOrFail("a + b setminus c + d") should matchPattern {
      case PSetMinus(
        PAdd(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PAdd(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to correctly parse (multi)set union in combination with subset") {
    frontend.parseExpOrFail("a union b subset c union d") should matchPattern {
      case PSubset(
        PUnion(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PUnion(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to correctly parse (multi)set intersection in combination with subset") {
    frontend.parseExpOrFail("a intersection b subset c intersection d") should matchPattern {
      case PSubset(
        PIntersection(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PIntersection(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to correctly parse (multi)set difference in combination with subset") {
    frontend.parseExpOrFail("a setminus b subset c setminus d") should matchPattern {
      case PSubset(
        PSetMinus(PNamedOperand(PIdnUse("a")), PNamedOperand(PIdnUse("b"))),
        PSetMinus(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("d")))
      ) =>
    }
  }

  test("Parser: should be able to translate a simple set conversion") {
    frontend.parseExpOrFail("set(xs)") should matchPattern {
      case PSetConversion(PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should be able to parse a set conversion expression with a more complex body expression") {
    frontend.parseExpOrFail("set(seq[int] { 1 } ++ seq[2..3])") should matchPattern {
      case PSetConversion(
        PSequenceAppend(
          PSequenceLiteral(PIntType(), Vector(PIntLit(a))),
          PRangeSequence(PIntLit(b), PIntLit(c))
        )
      ) if a == BigInt(1) && b == BigInt(2) && c == BigInt(3) =>
    }
  }

  test("Parser: should be able to parse the union of two set conversions") {
    frontend.parseExpOrFail("set(xs) union set(ys)") should matchPattern {
      case PUnion(
        PSetConversion(PNamedOperand(PIdnUse("xs"))),
        PSetConversion(PNamedOperand(PIdnUse("ys")))
      ) =>
    }
  }

  test("Parser: should be able to parse a nested set conversion") {
    frontend.parseExpOrFail("set(set(xs))") should matchPattern {
      case PSetConversion(PSetConversion(PNamedOperand(PIdnUse("xs")))) =>
    }
  }

  test("Parser: should not be able to parse a set conversion with missing opening parenthesis") {
    frontend.parseExp("set xs)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a set conversion with a missing closing parenthesis") {
    frontend.parseExp("set(xs") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a set conversion with extra spaces added") {
    frontend.parseExpOrFail("set ( xs )") should matchPattern {
      case PSetConversion(PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should not parse a set conversion with a parsing problem in the argument") {
    frontend.parseExp("set(xs ++ )") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a simple range set") {
    frontend.parseExpOrFail("set[1 .. 10]") should matchPattern {
      case PSetConversion(PRangeSequence(PIntLit(a), PIntLit(b)))
        if a == BigInt(1) && b == BigInt(10) =>
    }
  }

  test("Parser: should be able to parse a slightly more complicated range set expression") {
    frontend.parseExpOrFail("set[x + y .. |xs|]") should matchPattern {
      case PSetConversion(PRangeSequence(
        PAdd(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y"))),
        PCardinality(PNamedOperand(PIdnUse("xs")))
      )) =>
    }
  }

  test("Parser: should not be able to parse a set range expression with a missing left-hand side") {
    frontend.parseExp("set[ .. x]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a set range expression with a missing right-hand side") {
    frontend.parseExp("set[x .. ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a set range expression with extra spaces added") {
    frontend.parseExpOrFail("set [ x .. y ]") should matchPattern {
      case PSetConversion(PRangeSequence(
        PNamedOperand(PIdnUse("x")),
        PNamedOperand(PIdnUse("y"))
      )) =>
    }
  }

  test("Parser: should not parse a sequence range expression with too few dots") {
    frontend.parseExp("seq[x . y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a set range expression with too few dots") {
    frontend.parseExp("set[x . y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a sequence range expression with too many dots") {
    frontend.parseExp("seq[x ... y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a set range expression with too many dots") {
    frontend.parseExp("set[x ... y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a set range with a missing opening bracket") {
    frontend.parseExp("set x .. y ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a set range with a missing closing bracket") {
    frontend.parseExp("set [ x .. y") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a sequence range expression with a space in between the two dots") {
    frontend.parseExp("seq[x . . y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a set range expression with a space in between the two dots") {
    frontend.parseExp("set[x . . y]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a very simple multiplicity expression") {
    frontend.parseExpOrFail("e # s") should matchPattern {
      case PMultiplicity(
        PNamedOperand(PIdnUse("e")),
        PNamedOperand(PIdnUse("s"))
      ) =>
    }
  }

  test("Parser: should be able to parse a slightly more complex multiplicity expression") {
    frontend.parseExpOrFail("x + 2 # seq[int] { n }") should matchPattern {
      case PMultiplicity(
        PAdd(PNamedOperand(PIdnUse("x")), PIntLit(a)),
        PSequenceLiteral(PIntType(), Vector(PNamedOperand(PIdnUse("n"))))
      ) if a == BigInt(2) =>
    }
  }

  test("Parser: should let multiplicity associate to the left") {
    frontend.parseExpOrFail("x # y # z") should matchPattern {
      case PMultiplicity(
        PMultiplicity(
          PNamedOperand(PIdnUse("x")),
          PNamedOperand(PIdnUse("y"))
        ),
        PNamedOperand(PIdnUse("z"))
      ) =>
    }
  }

  test("Parser: should correctly parse a multiplicity expression with parentheses") {
    frontend.parseExpOrFail("x # (y # z)") should matchPattern {
      case PMultiplicity(
        PNamedOperand(PIdnUse("x")),
        PMultiplicity(
          PNamedOperand(PIdnUse("y")),
          PNamedOperand(PIdnUse("z"))
        )
      ) =>
    }
  }

  test("Parser: should not be able to parse a multiplicity expression without operands") {
    frontend.parseExp("#") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiplicity expression with a missing left-hand side") {
    frontend.parseExp("# y") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiplicity expression with a missing right-hand side") {
    frontend.parseExp("x #") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a doubly applied multiplicity operator") {
    frontend.parseExp("x ## y") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a multiplicity operator with a parsing problem in the left-hand side") {
    frontend.parseExp("seq[int] { x ++ } # ys") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a multiplicity operator with a parsing problem in the right-hand side") {
    frontend.parseExp("xs # seq[int] { x ++ }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a very simple multiset conversion expression") {
    frontend.parseExpOrFail("mset(xs)") should matchPattern {
      case PMultisetConversion(PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should be able to parse a slightly more complex multiset conversion expression") {
    frontend.parseExpOrFail("mset(seq[int] { x } ++ set[bool] { y, z })") should matchPattern {
      case PMultisetConversion(
        PSequenceAppend(
          PSequenceLiteral(PIntType(), Vector(PNamedOperand(PIdnUse("x")))),
          PSetLiteral(PBoolType(), Vector(
            PNamedOperand(PIdnUse("y")),
            PNamedOperand(PIdnUse("z"))
          ))
        )
      ) =>
    }
  }

  test("Parser: should be able to parse a union of multiset conversions") {
    frontend.parseExpOrFail("mset(xs) union mset(ys)") should matchPattern {
      case PUnion(
        PMultisetConversion(PNamedOperand(PIdnUse("xs"))),
        PMultisetConversion(PNamedOperand(PIdnUse("ys")))
      ) =>
    }
  }

  test("Parser: should be able to parse a nested multiset conversion") {
    frontend.parseExpOrFail("mset(mset(xs))") should matchPattern {
      case PMultisetConversion(
        PMultisetConversion(PNamedOperand(PIdnUse("xs")))
      ) =>
    }
  }

  test("Parser: should parse a multiset conversion with some extra spaces added") {
    frontend.parseExpOrFail("mset ( xs )") should matchPattern {
      case PMultisetConversion(PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should not parse a multiset conversion with a missing opening bracket") {
    frontend.parseExp("mset xs )") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset conversion with a missing closing bracket") {
    frontend.parseExp("mset ( xs") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset conversion expression with a space in 'mset'") {
    frontend.parseExp("m set(xs)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset conversion expression with a parsing error in the inner expression") {
    frontend.parseExp("mset(xs ++)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset conversion expression with an empty body") {
    frontend.parseExp("mset( )") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a simple multiset range expression") {
    frontend.parseExpOrFail("mset[1..10]") should matchPattern {
      case PMultisetConversion(PRangeSequence(PIntLit(low), PIntLit(high)))
        if low == BigInt(1) && high == BigInt(10) =>
    }
  }

  test("Parser: should be able to parse a slightly more complex multiset range expression") {
    frontend.parseExpOrFail("mset [ x + (y) .. | xs | ]") should matchPattern {
      case PMultisetConversion(
        PRangeSequence(
          PAdd(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y"))),
          PCardinality(PNamedOperand(PIdnUse("xs")))
        )
      ) =>
    }
  }

  test("Parser: should be able to parse the size of a multiset range expression") {
    frontend.parseExpOrFail("|(mset[(x)..y])|") should matchPattern {
      case PCardinality(
        PMultisetConversion(PRangeSequence(
          PNamedOperand(PIdnUse("x")),
          PNamedOperand(PIdnUse("y"))
        ))
      ) =>
    }
  }

  test("Parser: should not parse a multiset range expression with a missing opening bracket") {
    frontend.parseExp("mset x .. y ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression with a missing closing bracket") {
    frontend.parseExp("mset [ x .. y") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression with a missing left operand") {
    frontend.parseExp("mset [ .. y ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression with a missing right operand") {
    frontend.parseExp("mset [ x .. ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression without any operands") {
    frontend.parseExp("mset [ .. ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression with a space in between the dots") {
    frontend.parseExp("mset [ x . . y ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a multiset range expression with three dots instead of two") {
    frontend.parseExp("mset [ x ... y ]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a very simple use of the built-in 'len' function") {
    frontend.parseExpOrFail("len(42)") should matchPattern {
      case PLength(PIntLit(n)) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse a use of the 'len' function with some spaces added") {
    frontend.parseExpOrFail("len ( xs )") should matchPattern {
      case PLength(PNamedOperand(PIdnUse("xs"))) =>
    }
  }

  test("Parser: should parse a slightly more complex use of the 'len' function") {
    frontend.parseExpOrFail("len(seq[x .. y] ++ seq[bool] { false })") should matchPattern {
      case PLength(
        PSequenceAppend(
          PRangeSequence(
            PNamedOperand(PIdnUse("x")),
            PNamedOperand(PIdnUse("y"))
          ),
          PSequenceLiteral(PBoolType(), Vector(PBoolLit(false)))
        )
      ) =>
    }
  }

  test("Parser: should not be able to parse just 'len' as an identifier") {
    frontend.parseExp("len") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a use of 'len' with a missing opening parenthesis") {
    frontend.parseExp("len 42)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a use of 'len' with a missing closing parenthesis") {
    frontend.parseExp("len ( 42") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a use of the built-in 'len' function if there is a wrong space inside 'len'") {
    frontend.parseExp("le n(xs)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not be able to parse a use of the built-in 'len' function if there is a parsing problem in the operand") {
    frontend.parseExp("len(n ++ |xs)") should matchPattern {
      case Left(_) =>
    }
  }


  /* ** Stubs, mocks and other test setup */

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
    def parseImportDecl(source: String): Vector[PImportDecl] = parseOrFail(source, Parser.parseImportDecl)
  }
}
