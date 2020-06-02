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
      case Right(PSize(
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
          PSequenceUpdateClause(PIntLit(c), PSize(PNamedOperand(PIdnUse("xs"))))
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
          PSize(PNamedOperand(PIdnUse("zs"))),
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
      case PSetUnion(
        PNamedOperand(PIdnUse("s")),
        PNamedOperand(PIdnUse("t"))
      ) =>
    }
  }

  test("Parser: set union should by default associate to the left") {
    frontend.parseExpOrFail("s union t union u") should matchPattern {
      case PSetUnion(
        PSetUnion(
          PNamedOperand(PIdnUse("s")),
          PNamedOperand(PIdnUse("t"))
        ),
        PNamedOperand(PIdnUse("u"))
      ) =>
    }
  }

  test("Parser: set union with parentheses should correctly be parsed") {
    frontend.parseExpOrFail("s union (t union u)") should matchPattern {
      case PSetUnion(
        PNamedOperand(PIdnUse("s")),
        PSetUnion(
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
