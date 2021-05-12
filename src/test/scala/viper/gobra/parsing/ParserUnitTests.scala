// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.parsing

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util.{Source, StringSource}
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Parser

import scala.reflect.ClassTag

class ParserUnitTests extends AnyFunSuite with Matchers with Inside {
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
    frontend.parseStmtOrFail("a := b.BarCell{10}") should matchPattern {
      case PShortVarDecl(Vector(PCompositeLit(PDot(PNamedOperand(PIdnUse("b")), PIdnUse("BarCell")),
        PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(value))))))), Vector(PIdnUnk("a")), Vector(false))
          if value == 10 =>
    }
  }
  
  test("Parser: fold mpredicate call") {
    frontend.parseStmtOrFail("fold (*(b.Rectangle)).RectMem(&r)") should matchPattern {
      case PFold(PPredicateAccess(PInvoke(PDot(PDeref(PDot(PNamedOperand(PIdnUse("b")), PIdnUse("Rectangle"))), PIdnUse("RectMem")), Vector(PReference(PNamedOperand(PIdnUse("r"))))), PFullPerm())) =>
    }
  }
  
  test("Parser: fold fpredicate call") {
    frontend.parseStmtOrFail("fold b.RectMem(&r)") should matchPattern {
      case PFold(PPredicateAccess(PInvoke(PDot(PNamedOperand(PIdnUse("b")), PIdnUse("RectMem")), Vector(PReference(PNamedOperand(PIdnUse("r"))))), PFullPerm())) =>
    }
  }

  test("Parser: abstract function") {
    val modes: Set[Boolean] = Set(false, true)
    modes.foreach(specOnly => {
      frontend.parseMember("func bar()", specOnly) should matchPattern {
        case Vector(PFunctionDecl(PIdnDef("bar"), Vector(), PResult(Vector()), PFunctionSpec(Vector(), Vector(), false), None)) =>
      }
    })
  }


  /* ** structs */

  test("Parser: struct literal") {
    frontend.parseExp("bla{42}") should matchPattern {
      case Right(PCompositeLit(PNamedOperand(PIdnUse("bla")), PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(value))))))) if value == 42 =>
    }
  }

  test("Parser: struct literal with inline type") {
    val res = frontend.parseExp("struct {Number int;}{42}")
    // semicolon is optional:
    res shouldEqual frontend.parseExp("struct {Number int}{42}")
    res should matchPattern {
      case Right(PCompositeLit(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Number"), PIntType()))))),
        PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(value))))))) if value == 42 =>
    }
  }

  test("Parser: keyed struct literal with inline type") {
    val res = frontend.parseExp("struct {Number int;}{Number: 42}")
    // semicolon is optional:
    res shouldEqual frontend.parseExp("struct {Number int}{Number: 42}")
    res should matchPattern {
      case Right(PCompositeLit(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Number"), PIntType()))))),
      PLiteralValue(Vector(PKeyedElement(Some(PIdentifierKey(PIdnUse("Number"))), PExpCompositeVal(PIntLit(value))))))) if value == 42 =>
    }
  }

  test("Parser: struct literal with inline type and multiple fields") {
    val res = frontend.parseExp("struct {Number int; Text int;}{42, 1}")
    // semicolon is optional:
    res shouldEqual frontend.parseExp("struct {Number int; Text int}{42, 1}")
    res should matchPattern {
      case Right(PCompositeLit(PStructType(Vector(PFieldDecls(Vector(PFieldDecl(PIdnDef("Number"), PIntType()))), PFieldDecls(Vector(PFieldDecl(PIdnDef("Text"), PIntType()))))),
      PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(numberValue))), PKeyedElement(None, PExpCompositeVal(PIntLit(textValue))))))) if numberValue == 42 && textValue == 1 =>
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
    frontend.parseExpOrFail("seq[int] { }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: singleton integer sequence literal") {
    frontend.parseExpOrFail("seq[int] { 42 }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
        ))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: integer sequence literal with multiple elements") {
    frontend.parseExpOrFail("seq[int] { 3, 17, 142 }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PIntLit(a))),
          PKeyedElement(None, PExpCompositeVal(PIntLit(b))),
          PKeyedElement(None, PExpCompositeVal(PIntLit(c)))
        ))
      ) if a == BigInt(3) && b == BigInt(17) && c == BigInt(142) =>
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

  test("Parser: Boolean singleton sequence literal") {
    frontend.parseExpOrFail("seq[bool] { true, }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
        ))
      ) =>
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
        PCompositeLit(
          PSequenceType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        ),
        PCompositeLit(
          PSequenceType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        )
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
      case PGhostCollectionUpdate(
        PNamedOperand(PIdnUse("xs")),
        Vector(
          PGhostCollectionUpdateClause(
            PNamedOperand(PIdnUse("i")),
            PIntLit(n)
          )
        )
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: sequence update with an append on left-hand side") {
    frontend.parseExp("(xs ++ ys)[i = true]") should matchPattern {
      case Right(PGhostCollectionUpdate(
        PSequenceAppend(
          PNamedOperand(PIdnUse("xs")),
          PNamedOperand(PIdnUse("ys"))
        ),
        Vector(
          PGhostCollectionUpdateClause(
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
        PGhostCollectionUpdate(
          PNamedOperand(PIdnUse("ys")),
          Vector(
            PGhostCollectionUpdateClause(
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
        PGhostCollectionUpdate(
          PNamedOperand(PIdnUse("xs")),
          Vector(
            PGhostCollectionUpdateClause(
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
      case Right(PGhostCollectionUpdate(
        PCompositeLit(
          PSequenceType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true))),
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
          ))
        ),
        Vector(PGhostCollectionUpdateClause(PIntLit(i), PBoolLit(true)))
      )) if i == BigInt(1) =>
    }
  }

  test("Parser: chaining sequence updates") {
    frontend.parseExp("xs[i = true][j = false]") should matchPattern {
      case Right(PGhostCollectionUpdate(
        PGhostCollectionUpdate(
          PNamedOperand(PIdnUse("xs")),
          Vector(
            PGhostCollectionUpdateClause(
              PNamedOperand(PIdnUse("i")),
              PBoolLit(true)
            )
          )
        ),
        Vector(
          PGhostCollectionUpdateClause(
            PNamedOperand(PIdnUse("j")),
            PBoolLit(false)
          )
        )
      )) =>
    }
  }

  test("Parser: nested sequence updates") {
    frontend.parseExp("xs[i = ys[j = v]]") should matchPattern {
      case Right(PGhostCollectionUpdate(
        PNamedOperand(PIdnUse("xs")),
        Vector(
          PGhostCollectionUpdateClause(
            PNamedOperand(PIdnUse("i")),
            PGhostCollectionUpdate(
              PNamedOperand(PIdnUse("ys")),
              Vector(
                PGhostCollectionUpdateClause(
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
          PCompositeLit(
            PSequenceType(PBoolType()),
            PLiteralValue(Vector(
              PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
            ))
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
      case PGhostCollectionUpdate(
        PNamedOperand(PIdnUse("xs")),
        Vector(
          PGhostCollectionUpdateClause(PIntLit(a), PBoolLit(true)),
          PGhostCollectionUpdateClause(PIntLit(b), PNamedOperand(PIdnUse("b"))),
          PGhostCollectionUpdateClause(PIntLit(c), PCardinality(PNamedOperand(PIdnUse("xs"))))
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
        PCompositeLit(
          PSequenceType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true))),
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
          ))
        ),
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
      case PCompositeLit(
        PSetType(PIntType()),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: should parse a simple empty integer set literal with some spaces added") {
    frontend.parseExpOrFail("set [ int ] {}") should matchPattern {
      case PCompositeLit(
        PSetType(PIntType()),
        PLiteralValue(Vector())
      ) =>
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
      case PCompositeLit(
        PSetType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
        ))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should not be able to parse a singleton integer set literal with a wrongly placed extra comma (1)") {
    frontend.parseExp("set[int] { ,42 }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a singleton integer set literal even if there is an extra comma on the right") {
    frontend.parseExpOrFail("set[int] { 42, }") should matchPattern {
      case PCompositeLit(
        PSetType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
        ))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should not parse a singleton integer set literal if there are too many extra commas on the right") {
    frontend.parseExp("set[int] { 42,, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a Boolean set literal with multiple elements") {
    frontend.parseExpOrFail("set[bool] { true, false, true }") should matchPattern {
      case PCompositeLit(
        PSetType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PBoolLit(true))),
          PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
          PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
        ))
      ) =>
    }
  }

  test("Parser: should be able to parse a set literal with a nested type") {
    frontend.parseExpOrFail("set[set[set[bool]]] { }") should matchPattern {
      case PCompositeLit(
        PSetType(PSetType(PSetType(PBoolType()))),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: should be able to parse nested set literals") {
    frontend.parseExpOrFail("set[bool] { set[int] { 42 } }") should matchPattern {
      case PCompositeLit(
        PSetType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(
            PCompositeLit(
              PSetType(PIntType()),
              PLiteralValue(Vector(
                PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
              ))
            )
          ))
        ))
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
        PCompositeLit(
          PSetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        ),
        PCompositeLit(
          PSetType(PIntType()),
          PLiteralValue(Vector())
        )
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
        PCompositeLit(
          PSetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        ),
        PCompositeLit(
          PSetType(PIntType()),
          PLiteralValue(Vector())
        )
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
        PCompositeLit(
          PSetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        ),
        PCompositeLit(
          PSetType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
          ))
        )
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
      case PCompositeLit(
        PMultisetType(PBoolType()),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: should be able to correctly parse an empty integer multiset literal") {
    frontend.parseExpOrFail("mset [ int ]{}") should matchPattern {
      case PCompositeLit(
        PMultisetType(PIntType()),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: should be able to correctly parse an empty multiset literal with a nested type") {
    frontend.parseExpOrFail("mset[mset[mset[bool]]] { }") should matchPattern {
      case PCompositeLit(
        PMultisetType(PMultisetType(PMultisetType(PBoolType()))),
        PLiteralValue(Vector())
      ) =>
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
      case PCompositeLit(
        PMultisetType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
        ))
      ) =>
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
      case PCompositeLit(
        PMultisetType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PBoolLit(true))),
          PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
          PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
        ))
      ) =>
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

  test("Parser: should be able to parse integer multiset literal even if there is an extra comma on the very right") {
    frontend.parseExpOrFail("mset[int] { 1, 2, }") should matchPattern {
      case PCompositeLit(
        PMultisetType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PExpCompositeVal(PIntLit(a))),
          PKeyedElement(None, PExpCompositeVal(PIntLit(b)))
        ))
      ) if a == BigInt(1) && b == BigInt(2) =>
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
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        ),
        PCompositeLit(
          PMultisetType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
          ))
        )
      ) if n == BigInt(2) =>
    }
  }

  test("Parser: should be able to parse an intersection of two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 42 } intersection mset[bool] { false }") should matchPattern {
      case PIntersection(
        PCompositeLit(
          PMultisetType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
          ))
        ),
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
          ))
        )
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse the set difference of two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 42 } setminus mset[bool] { true }") should matchPattern {
      case PSetMinus(
        PCompositeLit(
          PMultisetType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
          ))
        ),
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        )
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse a subset relation applied to two multiset literals") {
    frontend.parseExpOrFail("mset[int] { 12, 24 } subset mset[bool] { false }") should matchPattern {
      case PSubset(
        PCompositeLit(
          PMultisetType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n1))),
            PKeyedElement(None, PExpCompositeVal(PIntLit(n2)))
          ))
        ),
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
          ))
        )
      ) if n1 == BigInt(12) && n2 == BigInt(24) =>
    }
  }

  test("Parser: should be able to correctly parse multiset cardinality") {
    frontend.parseExpOrFail("|mset[bool] { }|") should matchPattern {
      case PCardinality(
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector())
        )
      ) =>
    }
  }

  test("Parser: should correctly parse multiset inclusion (1)") {
    frontend.parseExpOrFail("true in mset[bool] { false, true }") should matchPattern {
      case PIn(
        PBoolLit(true),
        PCompositeLit(
          PMultisetType(PBoolType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false))),
            PKeyedElement(None, PExpCompositeVal(PBoolLit(true)))
          ))
        )
      ) =>
    }
  }

  test("Parser: should correctly parse multiset inclusion (2)") {
    frontend.parseExpOrFail("mset[int] { } in mset[bool] { }") should matchPattern {
      case PIn(
        PCompositeLit(PMultisetType(PIntType()), PLiteralValue(Vector())),
        PCompositeLit(PMultisetType(PBoolType()), PLiteralValue(Vector()))
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
          PCompositeLit(
            PSequenceType(PIntType()),
            PLiteralValue(Vector(PKeyedElement(None, PExpCompositeVal(PIntLit(a)))))
          ),
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
        PCompositeLit(
          PSequenceType(PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("n"))))
          ))
        )
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
          PCompositeLit(
            PSequenceType(PIntType()),
            PLiteralValue(Vector(
              PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("x"))))
            ))
          ),
          PCompositeLit(
            PSetType(PBoolType()),
            PLiteralValue(Vector(
              PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("y")))),
              PKeyedElement(None, PExpCompositeVal(PNamedOperand(PIdnUse("z"))))
            ))
          )
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
          PRangeSequence(PNamedOperand(PIdnUse("x")), PNamedOperand(PIdnUse("y"))),
          PCompositeLit(
            PSequenceType(PBoolType()),
            PLiteralValue(Vector(
              PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
            ))
          )
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

  test("Parser: empty integer sequence literal as a composite literal") {
    frontend.parseExpOrFail("seq[int] { }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector())
      ) =>
    }
  }

  test("Parser: should not parse an integer sequence singleton literal with too many commas on the very right") {
    frontend.parseExp("seq[int] { 12,, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse an integer set singleton literal with too many commas on the very right") {
    frontend.parseExp("set[int] { 12,, }") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: is able to parse a nested sequence literal written in 'compact' notation") {
    frontend.parseExpOrFail("seq[int] { { 42 } }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(None, PLitCompositeVal(
            PLiteralValue(Vector(
              PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
            ))
          ))
        ))
      ) if n == BigInt(42) =>
    }
  }

  test("Parser: is able to parse a nested set literal written in 'compact' notation") {
    frontend.parseExpOrFail("set[bool] { { false } }") should matchPattern {
    case PCompositeLit(
      PSetType(PBoolType()),
      PLiteralValue(Vector(
        PKeyedElement(None, PLitCompositeVal(
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PBoolLit(false)))
          ))
        ))
      ))
    ) =>
    }
  }

  test("Parser: is able to parse a nested multiset literal written in 'compact' notation") {
    frontend.parseExpOrFail("mset[int] { { 12 } }") should matchPattern {
    case PCompositeLit(
      PMultisetType(PIntType()),
      PLiteralValue(Vector(
        PKeyedElement(None, PLitCompositeVal(
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(PIntLit(n)))
          ))
        ))
      ))
      ) if n == BigInt(12) =>
    }
  }

  test("Parser: should be able to parse a (singleton) sequence integer literal with a specified key component") {
    frontend.parseExpOrFail("seq[int] { 0 : 42 }") should matchPattern {
      case PCompositeLit(
        PSequenceType(PIntType()),
        PLiteralValue(Vector(
          PKeyedElement(
            Some(PExpCompositeVal(PIntLit(a))),
            PExpCompositeVal(PIntLit(b))
          )
        ))
      ) if a == BigInt(0) && b == BigInt(42) =>
    }
  }

  test("Parser: should be able to parse a (singleton) set integer literal with a specified key component") {
    frontend.parseExpOrFail("set[bool] { 12 : true }") should matchPattern {
      case PCompositeLit(
        PSetType(PBoolType()),
        PLiteralValue(Vector(
          PKeyedElement(
            Some(PExpCompositeVal(PIntLit(a))),
            PExpCompositeVal(PBoolLit(true))
          )
        ))
      ) if a == BigInt(12) =>
    }
  }

  test("Parser: should be able to parse a (singleton) multiset integer literal with a specified key component") {
    frontend.parseExpOrFail("mset[int] { 10 : 12 }") should matchPattern {
    case PCompositeLit(
      PMultisetType(PIntType()),
      PLiteralValue(Vector(
        PKeyedElement(
          Some(PExpCompositeVal(PIntLit(a))),
          PExpCompositeVal(PIntLit(b))
        )
      ))
      ) if a == BigInt(10) && b == BigInt(12) =>
    }
  }

  test("Parser: should parse a simple sequence conversion expression") {
    frontend.parseExpOrFail("seq(a)") should matchPattern {
      case PSequenceConversion(PNamedOperand(PIdnUse("a"))) =>
    }
  }

  test("Parser: should parse a simple sequence conversion expression with some extra spaces added") {
    frontend.parseExpOrFail(" seq ( a ) ") should matchPattern {
      case PSequenceConversion(PNamedOperand(PIdnUse("a"))) =>
    }
  }

  test("Parser: should parse a slightly more complex sequence conversion expression") {
    frontend.parseExpOrFail("seq([1]int { 2 + 3 })") should matchPattern {
      case PSequenceConversion(
        PCompositeLit(
          PArrayType(PIntLit(a), PIntType()),
          PLiteralValue(Vector(
            PKeyedElement(None, PExpCompositeVal(
              PAdd(PIntLit(b), PIntLit(c))
            ))
          ))
        )
      ) if a == BigInt(1) && b == BigInt(2) && c == BigInt(3) =>
    }
  }

  test("Parser: should parse an append expression of two sequence conversions") {
    frontend.parseExpOrFail("seq(a1) ++ seq(a2)") should matchPattern {
      case PSequenceAppend(
        PSequenceConversion(PNamedOperand(PIdnUse("a1"))),
        PSequenceConversion(PNamedOperand(PIdnUse("a2")))
      ) =>
    }
  }

  test("Parser: should not parse a sequence conversion operation withing an opening parenthesis") {
    frontend.parseExp("seq a ) ") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a sequence conversion operation withing an closing parenthesis") {
    frontend.parseExp("seq ( a ") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a sequence conversion operation with a parsing problem in the inner expression") {
    frontend.parseExp("seq(seq)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: let a sequence conversion operation only take one argument") {
    frontend.parseExp("seq(a,b)") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse an indexing expression with a sequence conversion as its base expression") {
    frontend.parseExpOrFail("seq(a)[2]") should matchPattern {
      case PIndexedExp(
        PSequenceConversion(PNamedOperand(PIdnUse("a"))),
        PIntLit(i)
      ) if i == BigInt(2) =>
    }
  }

  test("Parser: should parse an option type") {
    frontend.parseTypeOrFail("option[option[int]]") should matchPattern {
      case POptionType(POptionType(PIntType())) =>
    }
  }

  test("Parser: should not parse 'get' as an identifier") {
    frontend.parseExp("get") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse a simple 'none' (option type) expression") {
    frontend.parseExpOrFail("none[option[bool]]") should matchPattern {
      case POptionNone(POptionType(PBoolType())) =>
    }
  }

  test("Parser: should parse a simple 'some' (option type) expression") {
    frontend.parseExpOrFail("some(42)") should matchPattern  {
      case POptionSome(PIntLit(n)) if n == BigInt(42) =>
    }
  }

  test("Parser: should parse a simple 'get' (option type) expression") {
    frontend.parseExpOrFail("get(x)") should matchPattern  {
      case POptionGet(PNamedOperand(PIdnUse("x"))) =>
    }
  }

  test("Parser: should not parse 'none' as a keyword") {
    frontend.parseExp("none") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse 'some' as a keyword") {
    frontend.parseExp("some") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should be able to parse a very simple integer slice type") {
    frontend.parseTypeOrFail("[]int") should matchPattern {
      case PSliceType(PIntType()) =>
    }
  }

  test("Parser: should be able to parse a slightly more complex slice type") {
    frontend.parseTypeOrFail("[]seq[set[bool]]") should matchPattern {
      case PSliceType(PSequenceType(PSetType(PBoolType()))) =>
    }
  }

  test("Parser: should correctly parse a nested (integer) slice type") {
    frontend.parseTypeOrFail("[][][]int") should matchPattern {
      case PSliceType(PSliceType(PSliceType(PIntType()))) =>
    }
  }

  test("Parser: should correctly parse a simple slice type with some extra spaces added") {
    frontend.parseTypeOrFail("[  ]  bool ") should matchPattern {
      case PSliceType(PBoolType()) =>
    }
  }

  test("Parser: should not parse a slice type with a missing opening bracket") {
    frontend.parseType("]int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a slice type with a missing closing bracket") {
    frontend.parseType("[int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a slice type if there are too many opening backets") {
    frontend.parseType("[[]int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a slice type if there are too many closing backets") {
    frontend.parseType("[]]int") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should not parse a slice type if there is a parsing problem in the inner type") {
    frontend.parseType("[]seq[set[]]") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should correctly parse a combination of slice types and sequence types") {
    frontend.parseTypeOrFail("[]seq[[]seq[[]int]]") should matchPattern {
      case PSliceType(PSequenceType(PSliceType(PSequenceType(PSliceType(PIntType()))))) =>
    }
  }

  test("Parser: should be able to parse a slice literal expression with a missing length (1)") {
    frontend.parseExpOrFail("[]int{}") should matchPattern {
      case PCompositeLit(PSliceType(PIntType()), PLiteralValue(Vector())) =>
    }
  }

  test("Parser: should be able to parse a slice literal expression with a missing length (2)") {
    frontend.parseExpOrFail("[ ]int { }") should matchPattern {
      case PCompositeLit(PSliceType(PIntType()), PLiteralValue(Vector())) =>
    }
  }

  test("Parser: should be able to parse an array type of sequences") {
    frontend.parseTypeOrFail("[n]seq[bool]") should matchPattern {
      case PArrayType(
      PNamedOperand(PIdnUse("n")),
      PSequenceType(PBoolType())
      ) =>
    }
  }

  test("Parser: should be able to parse a sequence type of integer arrays") {
    frontend.parseTypeOrFail("seq[[n]int]") should matchPattern {
      case PSequenceType(
      PArrayType(PNamedOperand(PIdnUse("n")), PIntType())
      ) =>
    }
  }

  test("Parser: should parse a simple integer array type with some extra spaces added") {
    frontend.parseTypeOrFail("[ n ] int") should matchPattern {
      case PArrayType(PNamedOperand(PIdnUse("n")), PIntType()) =>
    }
  }

  test("Parser: should be able to parse a fpredicate constructor") {
    frontend.parseExpOrFail("mutexInvariant!<x!>") should matchPattern {
      case PPredConstructor(PFPredBase(PIdnUse("mutexInvariant")), Vector(Some(PNamedOperand(PIdnUse("x"))))) =>
    }
  }

  test("Parser: should be able to parse a mpredicate constructor") {
    frontend.parseExpOrFail("p.mutexInvariant!<x!>") should matchPattern {
      case PPredConstructor(PDottedBase(PDot( PNamedOperand(PIdnUse("p")), PIdnUse("mutexInvariant"))), Vector(Some(PNamedOperand(PIdnUse("x"))))) =>
    }
  }


  /* ** Parser tests related to explicit ghost statements */

  test("Parser: should be able to parse an exlicit short var decl") {
    frontend.parseStmtOrFail("ghost res := test(s)") should matchPattern {
      case PExplicitGhostStatement(PShortVarDecl(
        Vector(PInvoke(PNamedOperand(PIdnUse("test")), Vector(PNamedOperand(PIdnUse("s"))))),
        Vector(PIdnUnk("res")),
        Vector(false))) =>
    }
  }


  /* ** Parser tests related to channels */
  test("Parser: should parse channel send statement") {
    frontend.parseStmtOrFail("c <- v") should matchPattern {
      case PSendStmt(PNamedOperand(PIdnUse("c")), PNamedOperand(PIdnUse("v"))) =>
    }
  }

  test("Parser: should parse channel send statement with an int literal") {
    frontend.parseStmtOrFail("c <- 5") should matchPattern {
      case PSendStmt(PNamedOperand(PIdnUse("c")), PIntLit(lit)) if lit == 5 =>
    }
  }

  // note that <- (channel send) might cause ambiguities with < - (smaller than a negative number)
  test("Parser: should not parse '<-' in an expression") {
    frontend.parseExp("c <- 5") should matchPattern {
      case Left(_) =>
    }
  }

  test("Parser: should parse '< -' in an expression") {
    val equivalences = Set(
      "c < -5",
      "c < - 5",
      "c< -5",
      "c <  -5"
    )
    // try to parse it as an expression
    equivalences.foreach(testcase => {
      frontend.parseExpOrFail(testcase) should matchPattern {
        case PLess(PNamedOperand(PIdnUse("c")), PSub(PIntLit(zero), PIntLit(five))) if zero == 0 && five == 5 =>
      }
    })
    // try to parse it as a statement
    equivalences.foreach(testcase => {
      frontend.parseStmtOrFail(testcase) should matchPattern {
        case PExpressionStmt(PLess(PNamedOperand(PIdnUse("c")), PSub(PIntLit(zero), PIntLit(five)))) if zero == 0 && five == 5 =>
      }
    })
  }

  test("Parser: whitespace requirement for '< -' should not affect PLess not followed by a minus sign") {
    val equivalences1 = Set(
      "c<+5",
      "c <+5",
      "c < +5",
      "c < + 5",
      "c< +5",
      "c <  +5"
    )
    // try to parse it as an expression
    equivalences1.foreach(testcase => {
      frontend.parseExpOrFail(testcase) should matchPattern {
        case PLess(PNamedOperand(PIdnUse("c")), PAdd(PIntLit(zero), PIntLit(five))) if zero == 0 && five == 5 =>
      }
    })
    // try to parse it as a statement
    equivalences1.foreach(testcase => {
      frontend.parseStmtOrFail(testcase) should matchPattern {
        case PExpressionStmt(PLess(PNamedOperand(PIdnUse("c")), PAdd(PIntLit(zero), PIntLit(five)))) if zero == 0 && five == 5 =>
      }
    })
    // it works without plus sign as well:
    val equivalences2 = Set(
      "c<5",
      "c < 5",
      "c <5",
      "c <  5",
      "c  < 5"
    )
    // try to parse it as an expression
    equivalences2.foreach(testcase => {
      frontend.parseExpOrFail(testcase) should matchPattern {
        case PLess(PNamedOperand(PIdnUse("c")), PIntLit(five)) if five == 5 =>
      }
    })
    // try to parse it as a statement
    equivalences2.foreach(testcase => {
      frontend.parseStmtOrFail(testcase) should matchPattern {
        case PExpressionStmt(PLess(PNamedOperand(PIdnUse("c")), PIntLit(five))) if five == 5 =>
      }
    })
  }

  test("Parser: string conversion") {
    // 0xf8 == 248
    val parseRes = frontend.parseExp("string(248)")
    inside (parseRes) {
      case Right(PInvoke(PStringType(), Vector(PIntLit(value)))) => value should be (0xf8)
    }
  }

  test("Parser: raw string") {
    frontend.parseExp("`Hello World`") should matchPattern {
      case Right(PStringLit("Hello World")) =>
    }
  }

  test("Parser: interpreted string") {
    frontend.parseExp("\"Hello World\"") should matchPattern {
      case Right(PStringLit("Hello World")) =>
    }
  }

  test("Parser: interpreted string with a quote") {
    frontend.parseExp("\"\\\"\"") should matchPattern {
      case Right(PStringLit("""\"""")) =>
    }
  }
  
  
   test("Parser: should be able to parse normal termination measure") {

    frontend.parseMember("decreases n\n func factorial (n int) int") should matchPattern {

      case Vector(PFunctionDecl(PIdnDef("factorial"), Vector(PNamedParameter("n", PIntType())), PResult(Vector(PUnnamedParameter(PIntType()))), PFunctionSpec(Vector(), Vector(), Some(PTupleTerminationMeasure(Vector(PNamedOperand(PIdnUse("n")))) ),false), None)) =>
    }

  }

  test("Parser: should be able to parse underscore termination measure") {

    frontend.parseMember("decreases _\n func factorial (n int) int") should matchPattern {

      case Vector(PFunctionDecl(PIdnDef("factorial"), Vector(PNamedParameter("n", PIntType())), PResult(Vector(PUnnamedParameter(PIntType()))), PFunctionSpec(Vector(), Vector(), Some(PUnderscoreCharacter()),false), None)) =>
    }

  }

  test("Parser: should be able to parse star termination measure") {

    frontend.parseMember("decreases *\n func factorial (n int) int") should matchPattern {

      case Vector(PFunctionDecl(PIdnDef("factorial"), Vector(PNamedParameter("n", PIntType())), PResult(Vector(PUnnamedParameter(PIntType()))), PFunctionSpec(Vector(), Vector(), Some( PStarCharacter()),false), None)) =>
    }

  }

  test("Parser: should be able to parse conditional termination measure" ){

    frontend.parseMember("decreases n if n>1\n decreases _ if n<1\n decreases *\n func factorial (n int) int") should matchPattern {

      case Vector(PFunctionDecl(PIdnDef("factorial"), Vector(PNamedParameter("n", PIntType())), PResult(Vector(PUnnamedParameter(PIntType()))), PFunctionSpec(Vector(), Vector(), Some( PConditionalMeasureCollection(Vector(PConditionalMeasureExpression(PNamedOperand(PIdnUse("n")), PGreater(PNamedOperand(PIdnUse("n")),PIntLit(1)) ),  PConditionalMeasureUnderscore(PUnderscoreCharacter(),PLess(PNamedOperand(PIdnUse("n")),PIntLit(1))), PConditionalMeasureAdditionalStar())))),false), None) =>
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
    def parseImportDecl(source: String): Vector[PImport] = parseOrFail(source, Parser.parseImportDecl)
    def parseMember(source: String, specOnly: Boolean = false): Vector[PMember] = parseOrFail(source, (s: Source) => Parser.parseMember(s, specOnly = specOnly))
  }
}
