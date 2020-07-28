package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.Info
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

class ExprTypingUnitTests extends FunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  test("TypeChecker: should classify an integer literal as integer") {
    frontend.exprType(PIntLit(42))() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not classify integer literals as ghost") {
    assert(!frontend.isGhostExpr(PIntLit(42))())
  }

  test("TypeChecker: should classify a Boolean literal as Boolean") {
    frontend.exprType(PBoolLit(false))() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should not classify Boolean literals as ghost") {
    assert(!frontend.isGhostExpr(PBoolLit(true))())
  }

  test("TypeChecker: should classify a named operand by its type") {
    val inArgs = Vector(PNamedParameter(PIdnDef("x"), PIntType(), false))
    frontend.exprType(PNamedOperand(PIdnUse("x")))(inArgs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify a ghost input parameter as being ghost") {
    val inArgs = Vector(PExplicitGhostParameter(PNamedParameter(PIdnDef("x"), PIntType(), false)))
    assert (frontend.isGhostExpr(PNamedOperand(PIdnUse("x")))(inArgs))
  }

  test("TypeChecker: should classify a sequence literal as ghost") {
    assert (frontend.isGhostExpr(PSequenceLiteral(PBoolType(), Vector()))())
  }

  test("TypeChecker: should correctly type a Boolean sequence") {
    frontend.exprType(PSequenceLiteral(PBoolType(), Vector()))() should matchPattern {
      case Type.SequenceT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should classify an sequence indexed expression as ghost") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    assert(frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should correctly type a simple sequence indexed expression") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PIntLit(0))
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: mark a sequence indexed expression with an incorrect left-hand side as not well-defined") {
    val expr = PIndexedExp(PIntLit(42), PIntLit(0))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: mark a sequence indexed expression with an incorrect right-hand side as not well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PIndexedExp(base, PBoolLit(false))
    assert(!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type chained sequence indexing expressions") {
    val inArgs = Vector(
      PExplicitGhostParameter(
        PNamedParameter(PIdnDef("xs"), PSequenceType(PSequenceType(PIntType())), false)
      )
    )
    val expr = PIndexedExp(
      PIndexedExp(PNamedOperand(PIdnUse("xs")), PIntLit(2)),
      PIntLit(4)
    )
    frontend.exprType(expr)(inArgs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify a sequence slice expression as ghost") {
    val inArgs = Vector(
      PExplicitGhostParameter(
        PNamedParameter(PIdnDef("xs"), PSequenceType(PIntType()), false)
      )
    )
    val expr = PSliceExp(
      PNamedOperand(PIdnUse("xs")),
      Some(PIntLit(2)),
      Some(PIntLit(4)),
      None
    )
    frontend.isGhostExpr(expr)(inArgs) should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PIntLit(4)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should not classify a sequence slice expression with a capacity as being well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PIntLit(4)), Some(PIntLit(6)))

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'low' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, None, Some(PIntLit(4)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'high' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(1)), None, None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should classify a proper sequence slice expression with a missing 'low' and 'high' index as well-defined") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, None, None, None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case true =>
    }
  }

  test("TypeChecker: should not allow the 'low' index of a slice expression to be anything other than an integer") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PBoolLit(false)), Some(PIntLit(2)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should not allow the 'high' index of a slice expression to be anything other than an integer") {
    val base = PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2)))
    val expr = PSliceExp(base, Some(PIntLit(2)), Some(PBoolLit(false)), None)

    frontend.wellDefExpr(expr)().valid should matchPattern {
      case false =>
    }
  }

  test("TypeChecker: should correctly identify the type of a Boolean sequence slice expression") {
    val base = PSequenceLiteral(PBoolType(), Vector(PBoolLit(true), PBoolLit(false)))
    val expr = PSliceExp(base, Some(PIntLit(1)), Some(PIntLit(4)), None)

    frontend.exprType(expr)() should matchPattern {
      case Type.SequenceT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should correctly identify the type of a nested sequence slice expression") {
    val base = PSequenceLiteral(PSequenceType(PIntType()), Vector())
    val expr = PSliceExp(base, Some(PIntLit(1)), Some(PIntLit(4)), None)

    frontend.exprType(expr)() should matchPattern {
      case Type.SequenceT(Type.SequenceT(Type.IntT)) =>
    }
  }

  test("TypeChecker: should classify an (integer) set literal as ghost") {
    val expr = PSetLiteral(PIntType(), Vector())
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should correctly type an (empty) integer set literal") {
    val expr = PSetLiteral(PIntType(), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type a nested (empty) set literal") {
    val expr = PSetLiteral(PSetType(PBoolType()), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.SetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify empty Boolean set literal as well-defined") {
    val expr = PSetLiteral(PIntType(), Vector())
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a singleton (integer) set literal with wrong subexpressions") {
    val expr = PSetLiteral(PIntType(), Vector(PBoolLit(false)))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set literal with (right and) wrong members") {
    val expr = PSetLiteral(PIntType(), Vector(
      PIntLit(1),
      PIntLit(5),
      PBoolLit(false),
      PIntLit(7)
    ))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a non-empty set literal") {
    val expr = PSetLiteral(PIntType(), Vector(
      PIntLit(1),
      PIntLit(5),
      PIntLit(7)
    ))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a set union operation as ghost") {
    val expr = PUnion(
      PSetLiteral(PBoolType(), Vector()),
      PSetLiteral(PBoolType(), Vector()),
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should type check a set union with operands of matching type") {
    val expr = PUnion(
      PSetLiteral(PIntType(), Vector(PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(4), PIntLit(5))),
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a set union operation") {
    val expr = PUnion(
      PSetLiteral(PIntType(), Vector(PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(4), PIntLit(5))),
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should not type check the union of a set and sequence") {
    val expr = PUnion(
      PSetLiteral(PIntType(), Vector(PIntLit(2))),
      PSequenceLiteral(PIntType(), Vector(PIntLit(4), PIntLit(5))),
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a union of two integers") {
    val expr = PUnion(
      PIntLit(42),
      PIntLit(22)
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should type check a chain of unions") {
    val expr = PUnion(
      PSetLiteral(PIntType(), Vector(PIntLit(2))),
      PUnion(
        PSetLiteral(PIntType(), Vector(PIntLit(4))),
        PSetLiteral(PIntType(), Vector(PIntLit(5))),
      )
    )

    assert (frontend.wellDefExpr(expr)().valid)

    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should classify set intersection as ghost") {
    val expr = PIntersection(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should let an intersection of two Boolean sets be well-defined") {
    val expr = PIntersection(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let the intersection of a Boolean set and an integer set be well-defined") {
    val expr = PIntersection(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PIntType(), Vector(PIntLit(42)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type an integer set intersection") {
    val expr = PIntersection(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(3)))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should classify set difference as ghost") {
    val expr = PSetMinus(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should let the difference of two Boolean sets be well-defined") {
    val expr = PSetMinus(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let the difference of a Boolean set and an integer set be well-defined") {
    val expr = PSetMinus(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PIntType(), Vector(PIntLit(42)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type an integer set difference") {
    val expr = PSetMinus(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(3)))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should mark any use of the subset relation as ghost") {
    val expr = PSubset(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should mark a normal use of the subset relation as well-defined") {
    val expr = PSubset(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the expected type to the result of a subset expression (1)") {
    val expr = PSubset(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2))),
      PSetLiteral(PIntType(), Vector(PIntLit(3)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should assign the expected type to the result of a subset expression (2)") {
    val expr = PSubset(
      PSetLiteral(PBoolType(), Vector()),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should not mark the use of a subset relation with incompatible operands as well-defined") {
    val expr = PSubset(
      PSetLiteral(PIntType(), Vector(PIntLit(3))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not mark a subset relation well-defined if its operand is not well-defined") {
    val expr = PSubset(
      PSetLiteral(PBoolType(), Vector(PIntLit(42))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify any use of a sequence append as ghost") {
    val expr = PSequenceAppend(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PSequenceLiteral(PIntType(), Vector(PIntLit(2))),
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify any proper use of a sequence append as well-defined") {
    val expr = PSequenceAppend(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PSequenceLiteral(PIntType(), Vector(PIntLit(2))),
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a sequence append of integers") {
    val expr = PSequenceAppend(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PSequenceLiteral(PIntType(), Vector(PIntLit(2))),
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.SequenceT(Type.IntT) =>
    }
  }

  test("TypeChecker: should not just typecheck a chain of subsets") {
    val expr = PSubset(
      PSubset(
        PSetLiteral(PIntType(), Vector()),
        PSetLiteral(PIntType(), Vector())
      ),
      PSetLiteral(PIntType(), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify any use of a sequence inclusion as ghost") {
    val expr = PIn(
      PIntLit(2),
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify any proper use of a sequence inclusion as being well-defined") {
    val expr = PIn(
      PIntLit(2),
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a sequence inclusion as being well-defined if the types don't match") {
    val expr = PIn(
      PBoolLit(false),
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a seq/set inclusion as well-defined if the right-hand side is not a sequence of (multi)set") {
    val expr = PIn(PBoolLit(false), PBoolLit(true))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a sequence inclusion operation") {
    val expr = PIn(
      PIntLit(2),
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should classify any use of a set inclusion as ghost") {
    val expr = PIn(
      PIntLit(2),
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify any proper use of a set inclusion as being well-defined") {
    val expr = PIn(
      PIntLit(2),
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a set inclusion operation") {
    val expr = PIn(
      PIntLit(2),
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should classify a sequence length operator as ghost") {
    val expr = PLength(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify the sequence length operator as well-defined when applied to a proper sequence") {
    val expr = PLength(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a sequence length operator") {
    val expr = PLength(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not typecheck any seq/set size operator when no sequence of (multi)set is provided") {
    val expr = PCardinality(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a set cardinality operator as ghost") {
    val expr = PCardinality(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify the set cardinality operator as well-defined when given a proper set") {
    val expr = PCardinality(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a set cardinality operator") {
    val expr = PCardinality(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not type check the intersection of two Booleans") {
    val expr = PIntersection(PBoolLit(false), PBoolLit(true))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the set difference of two Booleans") {
    val expr = PSetMinus(PBoolLit(false), PBoolLit(true))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the subset of two Booleans") {
    val expr = PSubset(PBoolLit(false), PBoolLit(true))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a simple empty Boolean multiset literal as ghost") {
    val expr = PMultisetLiteral(PBoolType(), Vector())
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should let a simple empty integer multiset literal be well-defined") {
    val expr = PMultisetLiteral(PIntType(), Vector())
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let a simple empty multiset literal of a nested type be well-defined") {
    val expr = PMultisetLiteral(PMultisetType(PBoolType()), Vector())
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type an empty Boolean multiset literal") {
    val expr = PMultisetLiteral(PBoolType(), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should correctly type an empty multiset literal of a nested type") {
    val expr = PMultisetLiteral(PMultisetType(PIntType()), Vector())
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.IntT)) =>
    }
  }

  test("TypeChecker: should classify a singleton integer multiset literal as ghost") {
    val expr = PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a simple singleton integer multiset literal as well-defined") {
    val expr = PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let a singleton multiset literal be well-defined if the types do not match") {
    val expr = PMultisetLiteral(PIntType(), Vector(PBoolLit(false)))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a non-empty integer multiset literal as ghost") {
    val expr = PMultisetLiteral(PIntType(), Vector(
      PIntLit(1), PIntLit(2), PIntLit(3)
    ))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a proper non-empty integer multiset literal as well-defined") {
    val expr = PMultisetLiteral(PIntType(), Vector(
      PIntLit(1), PIntLit(2), PIntLit(3)
    ))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a non-empty multiset literal with incorrect types as not well-defined (1)") {
    val expr = PMultisetLiteral(PIntType(), Vector(
      PIntLit(1), PIntLit(3), PBoolLit(false)
    ))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a non-empty multiset literal with incorrect types as not well-defined (2)") {
    val expr = PMultisetLiteral(PSetType(PIntType()), Vector(
      PSequenceLiteral(PIntType(), Vector())
    ))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a non-empty integer multiset literal") {
    val expr = PMultisetLiteral(PIntType(), Vector(
      PIntLit(1), PIntLit(2), PIntLit(3)
    ))
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type a nested multiset literal") {
    val expr = PMultisetLiteral(PMultisetType(PBoolType()), Vector(
      PMultisetLiteral(PBoolType(), Vector())
    ))
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify the union of the multiset integer literals as well-defined") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a union of a multiset and a set as well-defined") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PSetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a union of a multiset and a sequence as well-defined") {
    val expr = PUnion(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify the union of two incompatible multisets as well-defined") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a union of multisets as well-defined if there is a typing error in the left operand") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a union of multisets as well-defined if there is a typing error in the right operand") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
      PMultisetLiteral(PIntType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type the union of two integer multisets") {
    val expr = PUnion(
      PMultisetLiteral(PIntType(), Vector()),
      PMultisetLiteral(PIntType(), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type the union of two nested multiset literals") {
    val expr = PUnion(
      PMultisetLiteral(PMultisetType(PBoolType()), Vector()),
      PMultisetLiteral(PMultisetType(PBoolType()), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify the intersection of two multiset integer literals as well-defined") {
    val expr = PIntersection(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify an intersection of a multiset and a set as well-defined") {
    val expr = PIntersection(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PSetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify an intersection of a multiset and a sequence as well-defined") {
    val expr = PIntersection(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify the intersection of two incompatible multisets as well-defined") {
    val expr = PIntersection(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify an intersection of multisets as well-defined if there is a typing error in the left operand") {
    val expr = PIntersection(
      PMultisetLiteral(PIntType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify an intersection of multisets as well-defined if there is a typing error in the right operand") {
    val expr = PIntersection(
      PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
      PMultisetLiteral(PIntType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type the intersection of two integer multisets") {
    val expr = PIntersection(
      PMultisetLiteral(PIntType(), Vector()),
      PMultisetLiteral(PIntType(), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type the intersection of two nested multiset literals") {
    val expr = PIntersection(
      PMultisetLiteral(PMultisetType(PBoolType()), Vector()),
      PMultisetLiteral(PMultisetType(PBoolType()), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify the set difference of two multiset integer literals as well-defined") {
    val expr = PSetMinus(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true), PBoolLit(false)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a set difference of a multiset and a set as well-defined") {
    val expr = PSetMinus(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PSetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a set difference of a multiset and a sequence as well-defined") {
    val expr = PSetMinus(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify the set difference of two incompatible multisets as well-defined") {
    val expr = PSetMinus(
      PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a set difference of multisets as well-defined if there is a typing error in the left operand") {
    val expr = PSetMinus(
      PMultisetLiteral(PIntType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a set difference of multisets as well-defined if there is a typing error in the right operand") {
    val expr = PSetMinus(
      PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
      PMultisetLiteral(PIntType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type the set difference of two integer multisets") {
    val expr = PSetMinus(
      PMultisetLiteral(PIntType(), Vector()),
      PMultisetLiteral(PIntType(), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type the set difference of two nested multiset literals") {
    val expr = PSetMinus(
      PMultisetLiteral(PMultisetType(PBoolType()), Vector()),
      PMultisetLiteral(PMultisetType(PBoolType()), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should classify a use of the subset relation between multisets as ghost") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a standard use of the subset relation between two multisets as well-defined") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a subset of multisets as well-defined if the left operand is a set instead of a multiset") {
    val expr = PSubset(
      PSetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a subset of multisets as well-defined if the right operand is a set instead of a multiset") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a subset of multisets as well-defined if there is a type error in the left operand") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PIntLit(12))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a subset of multisets as well-defined if there is a type error in the right operand") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PIntLit(24)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type a subset relation of two incompatible multisets") {
    val expr = PSubset(
      PMultisetLiteral(PIntType(), Vector(PIntLit(12))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should be able to correctly type a subset relation of Boolean multiset literals") {
    val expr = PSubset(
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(true))),
      PMultisetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: must correctly type a subset of two type-nested multiset literals") {
    val expr = PSubset(
      PMultisetLiteral(PMultisetType(PIntType()), Vector()),
      PMultisetLiteral(PMultisetType(PIntType()), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should mark the use of a multiset cardinality as ghost") {
    val expr = PCardinality(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a normal use of the multiset cardinality as well-defined") {
    val expr = PCardinality(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset cardinality as well-defined if there is a typing error in the operand") {
    val expr = PCardinality(PMultisetLiteral(PBoolType(), Vector(PIntLit(42))))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify the cardinality of a nested multiset as well-defined") {
    val expr = PCardinality(
      PMultisetLiteral(PMultisetType(PIntType()), Vector(
        PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
      ))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a standard use of the multiset cardinality") {
    val expr = PCardinality(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should correctly type a 'nested' multiset cardinality") {
    val expr = PCardinality(
      PMultisetLiteral(PMultisetType(PIntType()), Vector(
        PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
      ))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should mark a simple multiset inclusion expression as ghost") {
    val expr = PIn(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a simple multiset inclusion expression as well-defined") {
    val expr = PIn(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset inclusion as well-defined if the types of the operands aren't compatible") {
    val expr = PIn(
      PBoolLit(false),
      PMultisetLiteral(PIntType(), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset inclusion operation as well-defined if there is a typing problem in the left operand") {
    val expr = PIn(
      PMultisetLiteral(PIntType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PMultisetType(PIntType()), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset inclusion operation as well-defined if there is a typing problem in the right operand") {
    val expr = PIn(
      PBoolLit(false),
      PMultisetLiteral(PBoolType(), Vector(PIntLit(42)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset inclusion operation as well-defined if it mixed multisets with ordinary sets") {
    val expr = PIn(
      PMultisetLiteral(PIntType(), Vector()),
      PMultisetLiteral(PSetType(PIntType()), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a simple multiset inclusion operation") {
    val expr = PIn(
      PIntLit(2),
      PMultisetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should correctly type a slightly more complicated multiset inclusion operation") {
    val expr = PIn(
      PMultisetLiteral(PIntType(), Vector(PIntLit(42))),
      PMultisetLiteral(PMultisetType(PIntType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should not type a (multi)set inclusion with integer literals as operands") {
    val expr = PIn(PIntLit(1), PIntLit(2))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should be able to handle a comparison of multiset inclusions") {
    val expr = PEquals(
      PIn(PIntLit(2), PMultisetLiteral(PIntType(), Vector(PIntLit(2)))),
      PIn(PIntLit(3), PMultisetLiteral(PIntType(), Vector(PIntLit(4))))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should be able to correctly type a comparison of multiset inclusions") {
    val expr = PEquals(
      PIn(PIntLit(2), PMultisetLiteral(PIntType(), Vector(PIntLit(2)))),
      PIn(PIntLit(3), PMultisetLiteral(PIntType(), Vector(PIntLit(4))))
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should be able to correctly type a comparison of (multi)set union") {
    val expr = PEquals(
      PUnion(
        PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
      ),
      PUnion(
        PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(4)))
      )
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should be able to correctly type a comparison of (multi)set intersection") {
    val expr = PEquals(
      PIntersection(
        PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
      ),
      PIntersection(
        PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(4)))
      )
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should be able to correctly type a comparison of (multi)set difference") {
    val expr = PEquals(
      PSetMinus(
        PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(2)))
      ),
      PSetMinus(
        PMultisetLiteral(PIntType(), Vector(PIntLit(2))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(4)))
      )
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should classify the conversion of a sequence to a set as ghost") {
    val expr = PSetConversion(PRangeSequence(PIntLit(1), PIntLit(10)))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify the conversion of a sequence range to a set as well-defined") {
    val expr = PSetConversion(PRangeSequence(PIntLit(1), PIntLit(10)))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify the (explicit) conversion of an integer to a set as well-defined") {
    val expr = PSetConversion(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify the explicit conversion of a set to a set as well-defined") {
    val expr = PSetConversion(
      PSetLiteral(PBoolType(), Vector(PBoolLit(false)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify an explicit set conversion as well-defined if there is a typing error in the inner expression") {
    val expr = PSetConversion(
      PSetLiteral(PBoolType(), Vector(PIntLit(42)))
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let two nested explicit set conversions be well-defined") {
    val expr = PSetConversion(
      PSetConversion(PSequenceLiteral(PIntType(), Vector()))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the set conversion of a Boolean literal") {
    val expr = PSetConversion(PBoolLit(false))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the nested set conversion of a Boolean literal") {
    val expr = PSetConversion(PSetConversion(PBoolLit(false)))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a simple explicit conversion expression from a sequence (range) to a set") {
    val expr = PSetConversion(
      PRangeSequence(PIntLit(1), PIntLit(100))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should correctly type a simple explicit conversion from a set to a set") {
    val expr = PSetConversion(
      PSetLiteral(PBoolType(), Vector())
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should correctly type a simple nested explicit set conversion expression") {
    val expr = PSetConversion(
      PSetConversion(
        PSequenceLiteral(PIntType(), Vector())
      )
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.SetT(Type.IntT) =>
    }
  }

  test("TypeChecker: should classify the sequence multiplicity operator as ghost") {
    val expr = PMultiplicity(
      PIntLit(42),
      PSequenceLiteral(PIntType(), Vector())
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a very simple use of the seq multiplicity operator as well-defined") {
    val expr = PMultiplicity(
      PIntLit(42),
      PSequenceLiteral(PIntType(), Vector())
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiplicity operator if the right-hand side is not a sequence but for example an integer") {
    val expr = PMultiplicity(
      PIntLit(2),
      PIntLit(3)
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a sequence multiplicity operator if the types of the operands don't match") {
    val expr = PMultiplicity(
      PIntLit(42),
      PSequenceLiteral(PBoolType(), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let a slightly more complex proper use of the sequence multiplicity operator be well-defined") {
    val expr = PMultiplicity(
      PSequenceLiteral(PBoolType(), Vector(PBoolLit(false))),
      PSequenceLiteral(PSequenceType(PBoolType()), Vector())
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let a simple use of the sequence multiplicity operator be of type integer") {
    val expr = PMultiplicity(
      PIntLit(42),
      PSequenceLiteral(PIntType(), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should let a slightly more complex use of the sequence multiplicity operator be of type integer") {
    val expr = PMultiplicity(
      PSequenceLiteral(PBoolType(), Vector(PBoolLit(false))),
      PSequenceLiteral(PSequenceType(PBoolType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify a (proper) multiset conversion of a multiset as ghost") {
    val expr = PMultisetConversion(
      PMultisetLiteral(PBoolType(), Vector())
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should type check a standard multiset conversion of a multiset") {
    val expr = PMultisetConversion(
      PMultisetLiteral(PIntType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should type check a slightly more involved multiset conversion of a multiset") {
    val expr = PMultisetConversion(
      PUnion(
        PMultisetLiteral(PIntType(), Vector(PIntLit(1))),
        PMultisetLiteral(PIntType(), Vector(PIntLit(2), PIntLit(3)))
      )
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should type check the union of two multiset conversions of multisets") {
    val expr = PUnion(
      PMultisetConversion(PMultisetLiteral(PIntType(), Vector())),
      PMultisetConversion(PMultisetLiteral(PIntType(), Vector()))
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset conversion of something other than a collection") {
    val expr = PMultisetConversion(PBoolLit(false))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset conversion of a multiset if there is a typing problem in the operand") {
    val expr = PMultisetConversion(
      PUnion(
        PMultisetLiteral(PIntType(), Vector()),
        PMultisetLiteral(PBoolType(), Vector())
      )
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the union of the results of two incompatible multiset conversions of multisets") {
    val expr = PUnion(
      PMultisetConversion(PMultisetLiteral(PIntType(), Vector())),
      PMultisetConversion(PMultisetLiteral(PBoolType(), Vector()))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a (proper) multiset conversion of a sequence as ghost") {
    val expr = PMultisetConversion(
      PSequenceLiteral(PBoolType(), Vector())
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should type check a simple proper multiset conversion of a sequence") {
    val expr = PMultisetConversion(
      PSequenceLiteral(PBoolType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should type check a slightly more complex multiset conversion expression of a sequence") {
    val expr = PMultisetConversion(
      PSequenceAppend(
        PSequenceLiteral(PIntType(), Vector(PIntLit(1))),
        PSequenceLiteral(PIntType(), Vector(PIntLit(2), PIntLit(3)))
      )
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type check an mset union of the results of two multiset conversions of sequences") {
    val expr = PUnion(
      PMultisetConversion(PSequenceLiteral(PIntType(), Vector(PIntLit(1)))),
      PMultisetConversion(PSequenceLiteral(PIntType(), Vector(PIntLit(2))))
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check an multiset conversion of sequences with an incorrect body expression") {
    val expr = PMultisetConversion(
      PSequenceAppend(
        PSequenceLiteral(PIntType(), Vector()),
        PSequenceLiteral(PBoolType(), Vector())
      )
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the union of two incompatible results of multiset conversions of sequences") {
    val expr = PUnion(
      PMultisetConversion(PSequenceLiteral(PIntType(), Vector())),
      PMultisetConversion(PSequenceLiteral(PBoolType(), Vector()))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a simple multiset conversion of a multiset") {
    val expr = PMultisetConversion(
      PMultisetLiteral(PSequenceType(PIntType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.SequenceT(Type.IntT)) =>
    }
  }

  test("TypeChecker: should assign the correct type to a simple multiset conversion of a sequence") {
    val expr = PMultisetConversion(
      PSequenceLiteral(PMultisetType(PBoolType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.MultisetT(Type.BooleanT)) =>
    }
  }

  test("TypeChecker: should correctly type the union of two matching multiset conversions") {
    val expr = PUnion(
      PMultisetConversion(PSequenceLiteral(PBoolType(), Vector())),
      PMultisetConversion(PSequenceLiteral(PBoolType(), Vector())),
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.MultisetT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should not type check the multiset conversion of a multiset literal that has a typing problem") {
    val expr = PMultisetConversion(
      PMultisetLiteral(PBoolType(), Vector(PIntLit(42)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check the multiset conversion of a sequence literal that has a typing problem") {
    val expr = PMultisetConversion(
      PSequenceLiteral(PIntType(), Vector(PBoolLit(false)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set conversion of a multiset (yet)") {
    val expr = PSetConversion(
      PMultisetLiteral(PIntType(), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset conversion of an ordinary set (yet)") {
    val expr = PMultisetConversion(
      PSetLiteral(PBoolType(), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should mark the multiset multiplicity operator as ghost") {
    val expr = PMultiplicity(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should correctly type check a simple use of the multiset multiplicity operator") {
    val expr = PMultiplicity(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should type check a slightly more complex use of the multiset multiplicity operator") {
    val expr = PMultiplicity(
      PSetLiteral(PBoolType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PSetType(PBoolType()), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset multiplicity operator if the types of the operands don't match") {
    val expr = PMultiplicity(
      PIntLit(42),
      PMultisetLiteral(PBoolType(), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset multiplicity operator if there is a typing error in the left operand") {
    val expr = PMultiplicity(
      PSetLiteral(PIntType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PSetType(PIntType()), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a use of the multiset multiplicity operator if there is a typing error in the right operand") {
    val expr = PMultiplicity(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector(PBoolLit(true)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type check a small chain of multiset multiplicity operators") {
    val expr = PMultiplicity(
      PMultiplicity(
        PBoolLit(false),
        PMultisetLiteral(PBoolType(), Vector())
      ),
      PMultisetLiteral(PIntType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a simple multiset multiplicity operator") {
    val expr = PMultiplicity(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to a slightly more complicated use of the multiset multiplicity operator") {
    val expr = PMultiplicity(
      PSetLiteral(PBoolType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PSetType(PBoolType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should mark the multiset inclusion operator as ghost") {
    val expr = PIn(PIntLit(42), PMultisetLiteral(PIntType(), Vector()))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should let a simple use of the multiset inclusion operator be well-defined") {
    val expr = PIn(PIntLit(42), PMultisetLiteral(PIntType(), Vector()))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let a multiset inclusion operator be well-defined if the types do not match") {
    val expr = PIn(
      PIntLit(42),
      PMultisetLiteral(PBoolType(), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset inclusion operator if there is a typing error in the left operand") {
    val expr = PIn(
      PSetLiteral(PBoolType(), Vector(PIntLit(42))),
      PMultisetLiteral(PSetType(PBoolType()), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a multiset inclusion operator if there is a typing error in the right operand") {
    val expr = PIn(
      PSetLiteral(PBoolType(), Vector(PBoolLit(false))),
      PMultisetLiteral(PSetType(PBoolType()), Vector(PIntLit(42)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let the type of a multiset inclusion operator be Boolean (instead of integer)") {
    val expr = PIn(
      PIntLit(42),
      PMultisetLiteral(PIntType(), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should correctly type a small chain of multiset inclusions") {
    val expr = PIn(
      PIn(
        PIntLit(42),
        PMultisetLiteral(PIntType(), Vector())
      ),
      PMultisetLiteral(PBoolType(), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should classify the set multiplicity operator as ghost") {
    val expr = PMultiplicity(
      PIntLit(12),
      PSetLiteral(PIntType(), Vector())
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should let a standard use of the multiplicity operator on sets be well-defined") {
    val expr = PMultiplicity(
      PIntLit(12),
      PSetLiteral(PIntType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set multiplicity operator if the types of the operands don't match") {
    val expr = PMultiplicity(
      PIntLit(12),
      PSetLiteral(PBoolType(), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set multiplicity operator if there is a typing problem in the left operand") {
    val expr = PMultiplicity(
      PSequenceLiteral(PBoolType(), Vector(PIntLit(42))),
      PSetLiteral(PSequenceType(PBoolType()), Vector())
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a set multiplicity operator if there is a typing problem in the right operand") {
    val expr = PMultiplicity(
      PSequenceLiteral(PBoolType(), Vector(PBoolLit(false))),
      PSetLiteral(PSequenceType(PBoolType()), Vector(PIntLit(42)))
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let a simple use of a set multiplicity operator be of type integer") {
    val expr = PMultiplicity(
      PIntLit(12),
      PSetLiteral(PIntType(), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should correctly type a slightly more involved use of the set multiplicity operator") {
    val expr = PMultiplicity(
      PSequenceLiteral(PBoolType(), Vector(PBoolLit(false))),
      PSetLiteral(PSequenceType(PBoolType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should classify the use of 'len' on sequences as ghost") {
    val expr = PLength(
      PSequenceLiteral(PBoolType(), Vector())
    )

    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should be able to type a very simple use of the built-in 'len' function applied on sequences") {
    val expr = PLength(
      PSequenceLiteral(PBoolType(), Vector())
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type a 'len' function application on an integer") {
    val expr = PLength(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type a 'len' function application on, say, a Boolean") {
    val expr = PLength(PBoolLit(false))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should be able to type check a slightly more complex application of 'len' on sequences") {
    val expr = PLength(
      PSequenceAppend(
        PRangeSequence(PIntLit(1), PIntLit(10)),
        PSequenceLiteral(PIntType(), Vector(PIntLit(42)))
      )
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not type check a use of the 'len' function if there is a typing error in its argument") {
    val expr = PLength(
      PSequenceAppend(
        PRangeSequence(PIntLit(1), PIntLit(10)),
        PSequenceLiteral(PBoolType(), Vector())
      )
    )

    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should be able to type the addition of two applications of 'len' to sequences") {
    val expr = PAdd(
      PLength(PRangeSequence(PIntLit(1), PIntLit(10))),
      PLength(PSequenceLiteral(PBoolType(), Vector(PBoolLit(false))))
    )

    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a simple application of 'len' to a sequence (of some type)") {
    val expr = PLength(PSequenceLiteral(PBoolType(), Vector()))

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to a slightly more complex application of 'len'") {
    val expr = PLength(
      PSequenceAppend(
        PRangeSequence(PIntLit(1), PIntLit(10)),
        PSequenceLiteral(PIntType(), Vector(PIntLit(42)))
      )
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not type check a cardinality operation applied on a sequence") {
    val expr = PCardinality(
      PSequenceLiteral(PBoolType(), Vector())
    )
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should let a sequence range expression be pure") {
    val expr = PRangeSequence(PIntLit(1), PIntLit(10))
    assert (frontend.isPureExpr(expr)())
  }

  test("TypeChecker: should let a sequence drop operation be pure") {
    val expr = PSliceExp(
      PSequenceLiteral(PBoolType(), Vector()),
      None,
      Some(PIntLit(42)),
      None
    )

    assert (frontend.isPureExpr(expr)())
  }

  test("TypeChecker: should let a sequence take operation be pure") {
    val expr = PSliceExp(
      PSequenceLiteral(PBoolType(), Vector()),
      Some(PIntLit(42)),
      None,
      None
    )

    assert (frontend.isPureExpr(expr)())
  }

  test("TypeChecker: should let a complete sequence slice expression be marked as pure") {
    val expr = PSliceExp(
      PSequenceLiteral(PBoolType(), Vector()),
      Some(PIntLit(2)),
      Some(PIntLit(8)),
      None
    )

    assert (frontend.isPureExpr(expr)())
  }

  test("TypeChecker: should let a simple (integer) sequence index operation be marked a pure") {
    val expr = PIndexedExp(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3))),
      PIntLit(2)
    )

    assert (frontend.isPureExpr(expr)())
  }

  test("TypeChecker: should mark the 'len' function applied on an integer array as non-ghost") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PIntType()), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))
    assert (!frontend.isGhostExpr(expr)(inargs))
  }

  test("TypeChecker: should let the 'len' function applied on a simple integer array be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PIntType()), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should let the 'len' function applied on an array of sequences be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PSequenceType(PIntType())), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should let the 'len' function applied on a multidimensional array be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(1), PArrayType(PIntLit(2), PBoolType())), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let the 'len' function applied on a wrongly typed array be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(-42), PIntType()), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should assign the correct type to the 'len' function applied on a simple integer array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to a 'len' function applied on an array of sets of Booleans") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(4), PSetType(PBoolType())), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to the 'len' function applied on a multidimensional array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(1), PArrayType(PIntLit(2), PIntType())), false))
    val expr = PLength(PNamedOperand(PIdnUse("a")))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should mark a simple use of the 'cap' function (on arrays) as non-ghost") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))
    assert (!frontend.isGhostExpr(expr)(inargs))
  }

  test("TypeChecker: should mark a simple use of the 'cap' function (applied on an array) as well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let the 'cap' function applied on an integer be well-defined") {
    val expr = PCapacity(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let the 'len' function applied on an integer be well-defined") {
    val expr = PLength(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not let a function application of 'cap' be well-defined if applied on an incorrectly typed array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(-12), PIntType()), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should let a function application of 'cap' be well-defined if applied on a multidimensional array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(1), PArrayType(PIntLit(2), PIntType())), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should assign the correct type to a simple application of 'cap'") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to a simple application of 'cap' on an array of sequences of Booleans") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PSequenceType(PBoolType())), false))
    val expr = PCapacity(PNamedOperand(PIdnUse("a")))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should mark an indexing operator on an array as non-ghost") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4))
    assert (!frontend.isGhostExpr(expr)(inargs))
  }

  test("TypeChecker: should mark a very simple indexing on an integer array be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should mark integer array indexing be well-defined also if the index exceeds the array length") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(12), PIntType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(412))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let indexing on an integer array be well-defined if the array length happens to be negatieve") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(-12), PIntType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should let array indexing be well-defined if the array is multidimensional") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(10), PArrayType(PIntLit(20), PBoolType())), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let indexing be well-defined if the base is simply, say, a Boolean literal") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PBoolType(), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should let array indexing be well-defined if applied on an array of (ghost) sets") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PSetType(PIntType())), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(12))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should assign the correct type to simple indexing on an integer array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PIntType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(12))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should assign the correct type to simple indexing on a Boolean array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(12))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.BooleanT =>
    }
  }

  test("TypeChecker: should assign the correct type to simple indexing on a multidimensional array") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PArrayType(PIntLit(12), PIntType())), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(12))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.ArrayT(n, Type.IntT) if n == BigInt(12) =>
    }
  }

  test("TypeChecker: should mark a small chain of indexing operations as well-defined if the base type allows it") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PArrayType(PIntLit(12), PIntType())), false))
    val expr = PIndexedExp(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4)), PIntLit(8))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should assign the correct type to a small chain of indexing operations") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PArrayType(PIntLit(12), PMultisetType(PBoolType()))), false))
    val expr = PIndexedExp(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4)), PIntLit(8))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.MultisetT(Type.BooleanT) =>
    }
  }

  test("TypeChecker: should not allow array indexing with a negative index") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(-12))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should allow array indexing with an index that is zero") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(0))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should mark a simple array access predicate as non-ghost") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4)))
    assert (frontend.isGhostExpr(expr)(inargs))
  }

  test("TypeChecker: should let a simple array access predicate be well-defined") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(4)))
    assert (frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let a simple array access predicate be well-defined if the index is negative") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(-4)))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should not let a simple array access predicate be well-defined if the index exceeds the array length") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(42)))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }

  test("TypeChecker: should assign the correct type to a simple array access predicate") {
    val inargs = Vector(PNamedParameter(PIdnDef("a"), PArrayType(PIntLit(42), PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("a")), PIntLit(24)))

    frontend.exprType(expr)(inargs) should matchPattern {
      case Type.AssertionT =>
    }
  }

  test("TypeChecker: should not let an 'acc' predicate be well-defined when used on a sequence instead of an array") {
    val inargs = Vector(PNamedParameter(PIdnDef("xs"), PSequenceType(PBoolType()), false))
    val expr = PAccess(PIndexedExp(PNamedOperand(PIdnUse("xs")), PIntLit(4)))
    assert (!frontend.wellDefExpr(expr)(inargs).valid)
  }


  /* * Stubs, mocks, and other test setup  */

  class TestFrontend {
    def stubProgram(inArgs: Vector[PParameter], body : PStatement) : PProgram = PProgram(
      PPackageClause(PPkgDef("pkg")),
      Vector(),
      Vector(PMethodDecl(
        PIdnDef("foo"),
        PUnnamedReceiver(PMethodReceiveName(PNamedOperand(PIdnUse("self")))),
        inArgs,
        PResult(Vector()),
        PFunctionSpec(Vector(), Vector(), true),
        Some(PBlock(Vector(body)))
      ))
    )

    def singleExprProgram(inArgs: Vector[PParameter], expr : PExpression) : PProgram = {
      val stmt = PShortVarDecl(Vector(expr), Vector(PIdnUnk("n")), Vector(false))
      stubProgram(inArgs, stmt)
    }

    def singleExprTypeInfo(inArgs: Vector[PParameter], expr : PExpression) : TypeInfoImpl = {
      val program = singleExprProgram(inArgs, expr)
      val pkg = PPackage(
        PPackageClause(PPkgDef("pkg")),
        Vector(program),
        new PositionManager()
      )
      val tree = new Info.GoTree(pkg)
      val context = new Info.Context()
      val config = Config(Vector(), Vector())
      new TypeInfoImpl(tree, context)(config)
    }

    def exprType(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Type.Type =
      singleExprTypeInfo(inArgs, expr).exprType(expr)

    def isGhostExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Boolean =
      singleExprTypeInfo(inArgs, expr).isExprGhost(expr)

    def isPureExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Boolean =
      singleExprTypeInfo(inArgs, expr).isPureExpr(expr).isEmpty

    def wellDefExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) =
      singleExprTypeInfo(inArgs, expr).wellDefExpr(expr)
  }
}
