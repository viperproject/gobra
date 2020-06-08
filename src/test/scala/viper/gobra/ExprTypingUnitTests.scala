package viper.gobra

import org.scalatest.{FunSuite, Inside, Matchers}
import viper.gobra.ast.frontend._
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
    val expr = PSize(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify the sequence length operator as well-defined when applied to a proper sequence") {
    val expr = PSize(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a sequence length operator") {
    val expr = PSize(
      PSequenceLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should not typecheck any seq/set size operator when no sequence of (multi)set is provided") {
    val expr = PSize(PIntLit(42))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify a set cardinality operator as ghost") {
    val expr = PSize(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify the set cardinality operator as well-defined when given a proper set") {
    val expr = PSize(
      PSetLiteral(PIntType(), Vector(PIntLit(1), PIntLit(2), PIntLit(3)))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should assign the correct type to a set cardinality operator") {
    val expr = PSize(
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
    val expr = PSize(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))
    assert (frontend.isGhostExpr(expr)())
  }

  test("TypeChecker: should classify a normal use of the multiset cardinality as well-defined") {
    val expr = PSize(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should not classify a multiset cardinality as well-defined if there is a typing error in the operand") {
    val expr = PSize(PMultisetLiteral(PBoolType(), Vector(PIntLit(42))))
    assert (!frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should classify the cardinality of a nested multiset as well-defined") {
    val expr = PSize(
      PMultisetLiteral(PMultisetType(PIntType()), Vector(
        PMultisetLiteral(PIntType(), Vector(PIntLit(42)))
      ))
    )
    assert (frontend.wellDefExpr(expr)().valid)
  }

  test("TypeChecker: should correctly type a standard use of the multiset cardinality") {
    val expr = PSize(PMultisetLiteral(PBoolType(), Vector(PBoolLit(false))))

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
    }
  }

  test("TypeChecker: should correctly type a 'nested' multiset cardinality") {
    val expr = PSize(
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
      case Type.IntT =>
    }
  }

  test("TypeChecker: should correctly type a slightly more complicated multiset inclusion operation") {
    val expr = PIn(
      PMultisetLiteral(PIntType(), Vector(PIntLit(42))),
      PMultisetLiteral(PMultisetType(PIntType()), Vector())
    )

    frontend.exprType(expr)() should matchPattern {
      case Type.IntT =>
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
      )),
      new PositionManager()
    )

    def singleExprProgram(inArgs: Vector[PParameter], expr : PExpression) : PProgram = {
      val stmt = PShortVarDecl(Vector(expr), Vector(PIdnUnk("n")), Vector(false))
      stubProgram(inArgs, stmt)
    }

    def singleExprTypeInfo(inArgs: Vector[PParameter], expr : PExpression) : TypeInfoImpl = {
      val program = singleExprProgram(inArgs, expr)
      val tree = new Info.GoTree(program)
      new TypeInfoImpl(tree)
    }

    def exprType(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Type.Type =
      singleExprTypeInfo(inArgs, expr).exprType(expr)

    def isGhostExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) : Boolean =
      singleExprTypeInfo(inArgs, expr).isExprGhost(expr)

    def wellDefExpr(expr : PExpression)(inArgs: Vector[PParameter] = Vector()) =
      singleExprTypeInfo(inArgs, expr).wellDefExpr(expr)
  }
}
