// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Internal
import viper.gobra.theory.Addressability
import viper.gobra.util.TypeBounds

class InternalPrettyPrinterUnitTests extends AnyFunSuite with Matchers with Inside {
  val frontend = new TestFrontend()

  val intT: Type = IntT(Addressability.Exclusive, TypeBounds.DefaultInt)
  val boolT: Type = BoolT(Addressability.Exclusive)
  def sequenceT(memT: Type): Type = SequenceT(memT, Addressability.Exclusive)
  def setT(memT: Type): Type = SetT(memT, Addressability.Exclusive)
  def multisetT(memT: Type): Type = MultisetT(memT, Addressability.Exclusive)
  def exclusiveArrayT(length: BigInt, memT: Type): Type = ArrayT(length, memT, Addressability.Exclusive)
  def sharedArrayT(length: BigInt, memT: Type): Type = ArrayT(length, memT, Addressability.Shared)

  test("Printer: should correctly show a standard sequence index expression") {
    val expr = IndexedExp(
      LocalVar("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(42))(Internal),
      sequenceT(intT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42]" =>
    }
  }

  test("Printer: should correctly show a sequence update expression") {
    val expr = GhostCollectionUpdate(
      LocalVar("xs", sequenceT(boolT))(Internal),
      IntLit(BigInt(4))(Internal),
      BoolLit(false)(Internal),
      sequenceT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[4 = false]" =>
    }
  }

  test("Printer: should correctly show an empty integer sequence") {
    val expr = SequenceLit(0, intT, Map())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int°] { }" =>
    }
  }

  test("Printer: should correctly show an empty (nested) Boolean sequence") {
    val expr = SequenceLit(0, sequenceT(sequenceT(boolT)), Map())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[seq[seq[bool°]°]°] { }" =>
    }
  }

  test("Printer: should correctly show a sequence range expression") {
    val expr = RangeSequence(
      IntLit(BigInt(2))(Internal),
      IntLit(BigInt(44))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[2 .. 44]" =>
    }
  }

  test("Printer: should correctly show a non-empty simple integer sequence literal") {
    val expr = SequenceLit(
      3, intT,
      Map(
        BigInt(0) -> IntLit(BigInt(2))(Internal),
        BigInt(1) -> IntLit(BigInt(4))(Internal),
        BigInt(2) -> IntLit(BigInt(8))(Internal)
      )
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int°] { 0:2, 1:4, 2:8 }" =>
    }
  }

  test("Printer: should correctly show a singleton integer sequence literal") {
    val expr = SequenceLit(
      1, intT,
      Map(BigInt(0) -> IntLit(BigInt(42))(Internal))
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[int°] { 0:42 }" =>
    }
  }

  test("Printer: should correctly show an empty sequence literal") {
    val expr = SequenceLit(0, boolT, Map())(Internal)

    frontend.show(expr) should matchPattern {
      case "seq[bool°] { }" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence drop operation") {
    val expr = SequenceDrop(
      LocalVar("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(42))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[42:]" =>
    }
  }

  test("Printer: should correctly show an ordinary sequence take operation") {
    val expr = SequenceTake(
      LocalVar("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(4))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "xs[:4]" =>
    }
  }

  test("Printer: should correctly show a sequence drop followed by a take") {
    val expr1 = SequenceDrop(
      LocalVar("xs", sequenceT(intT))(Internal),
      IntLit(BigInt(2))(Internal)
    )(Internal)
    val expr2 = SequenceTake(
      expr1, IntLit(BigInt(4))(Internal)
    )(Internal)

    frontend.show(expr2) should matchPattern {
      case "xs[2:][:4]" =>
    }
  }

  test("Printer: should correctly show an integer sequence type") {
    val t = sequenceT(intT)
    frontend.show(t) should matchPattern {
      case "seq[int°]°" =>
    }
  }

  test("Printer: should correctly show a nested sequence type") {
    val t = sequenceT(sequenceT(sequenceT(boolT)))
    frontend.show(t) should matchPattern {
      case "seq[seq[seq[bool°]°]°]°" =>
    }
  }

  test("Printer: should correctly show an integer set type") {
    val t = setT(intT)
    frontend.show(t) should matchPattern {
      case "set[int°]°" =>
    }
  }

  test("Printer: should correctly show a nested set type") {
    val t = setT(setT(setT(boolT)))
    frontend.show(t) should matchPattern {
      case "set[set[set[bool°]°]°]°" =>
    }
  }

  test("Printer: should correctly show an empty integer set") {
    val expr = SetLit(intT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int°] { }" =>
    }
  }

  test("Printer: should correctly show an empty nested set") {
    val expr = SetLit(setT(boolT), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "set[set[bool°]°] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer set literal") {
    val expr = SetLit(intT, Vector(IntLit(42)(Internal)))(Internal)
    frontend.show(expr) should matchPattern {
      case "set[int°] { 42 }" =>
    }
  }

  test("Printer: should correctly show a non-empty Boolean set literal") {
    val expr = SetLit(boolT, Vector(
      BoolLit(false)(Internal),
      BoolLit(true)(Internal),
      BoolLit(true)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "set[bool°] { false, true, true }" =>
    }
  }

  test("Printer: should show a set union as expected") {
    val expr = Union(
      LocalVar("s", setT(boolT))(Internal),
      LocalVar("t", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s union t)" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (1)") {
    val expr = Union(
      Union(
        LocalVar("s", setT(boolT))(Internal),
        LocalVar("t", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      LocalVar("u", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((s union t) union u)" =>
    }
  }

  test("Printer: should show a chain of set unions as expected (2)") {
    val expr = Union(
      LocalVar("s", setT(boolT))(Internal),
      Union(
        LocalVar("t", setT(boolT))(Internal),
        LocalVar("u", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s union (t union u))" =>
    }
  }

  test("Printer: should correctly show set union in combination with literals") {
    val expr = Union(
      SetLit(boolT, Vector(
        LocalVar("s", sequenceT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar("t", sequenceT(boolT))(Internal),
        LocalVar("u", sequenceT(boolT))(Internal)
      ))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set[bool°] { s } union set[bool°] { t, u })" =>
    }
  }

  test("Printer: should show a set intersection as expected") {
    val expr = Intersection(
      LocalVar("s", setT(boolT))(Internal),
      LocalVar("t", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s intersection t)" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (1)") {
    val expr = Intersection(
      Intersection(
        LocalVar("s", setT(boolT))(Internal),
        LocalVar("t", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      LocalVar("u", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((s intersection t) intersection u)" =>
    }
  }

  test("Printer: should show a chain of set intersections as expected (2)") {
    val expr = Intersection(
      LocalVar("s", setT(boolT))(Internal),
      Intersection(
        LocalVar("t", setT(boolT))(Internal),
        LocalVar("u", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s intersection (t intersection u))" =>
    }
  }

  test("Printer: should correctly show set intersection in combination with literals") {
    val expr = Intersection(
      SetLit(boolT, Vector(
        LocalVar("s", setT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar("t", boolT)(Internal),
        LocalVar("u", boolT)(Internal)
      ))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set[bool°] { s } intersection set[bool°] { t, u })" =>
    }
  }

  test("Printer: should show a set difference as expected") {
    val expr = SetMinus(
      LocalVar("s", setT(boolT))(Internal),
      LocalVar("t", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s setminus t)" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (1)") {
    val expr = SetMinus(
      SetMinus(
        LocalVar("s", setT(boolT))(Internal),
        LocalVar("t", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      LocalVar("u", setT(boolT))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((s setminus t) setminus u)" =>
    }
  }

  test("Printer: should show a chain of set differences as expected (2)") {
    val expr = SetMinus(
      LocalVar("s", setT(boolT))(Internal),
      SetMinus(
        LocalVar("t", setT(boolT))(Internal),
        LocalVar("u", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s setminus (t setminus u))" =>
    }
  }

  test("Printer: should correctly show set differences in combination with literals") {
    val expr = SetMinus(
      SetLit(boolT, Vector(
        LocalVar("s", setT(boolT))(Internal),
      ))(Internal),
      SetLit(boolT, Vector(
        LocalVar("t", setT(boolT))(Internal),
        LocalVar("u", setT(boolT))(Internal)
      ))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set[bool°] { s } setminus set[bool°] { t, u })" =>
    }
  }

  test("Printer: should print a subset relation as expected") {
    val expr = Subset(
      LocalVar("s", sequenceT(boolT))(Internal),
      LocalVar("t", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(s subset t)" =>
    }
  }

  test("Printer: should print a chain of subset relations as expected") {
    val expr = Subset(
      Subset(
        LocalVar("s", setT(boolT))(Internal),
        LocalVar("t", setT(boolT))(Internal)
      )(Internal),
      LocalVar("u", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((s subset t) subset u)" =>
    }
  }

  test("Printer: should properly print a subset relation in combination with literals") {
    val expr = Subset(
      SetLit(intT, Vector(IntLit(42)(Internal)))(Internal),
      SetLit(boolT, Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set[int°] { 42 } subset set[bool°] { })" =>
    }
  }

  test("Printer: should correctly show a standard sequence inclusion") {
    val expr = Contains(
      LocalVar("x", sequenceT(boolT))(Internal),
      LocalVar("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(x elem xs)" =>
    }
  }

  test("Printer: should correctly show a 'chain' of sequence inclusions") {
    val expr = Contains(
      Contains(
        LocalVar("x", sequenceT(boolT))(Internal),
        LocalVar("xs", sequenceT(boolT))(Internal)
      )(Internal),
      LocalVar("ys", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((x elem xs) elem ys)" =>
    }
  }

  test("Printer: should correctly show set membership in the context of literals") {
    val expr = Contains(
      SetLit(boolT, Vector(
        BoolLit(true)(Internal),
        BoolLit(false)(Internal))
      )(Internal),
      SetLit(setT(setT(intT)), Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set[bool°] { true, false } elem set[set[set[int°]°]°] { })" =>
    }
  }

  test("Printer: should correctly show the size of a simple set") {
    val expr = Length(
      LocalVar("s", setT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(s)" =>
    }
  }

  test("Printer: should correctly show the size of a set in combination with a set intersection") {
    val expr = Length(
      Intersection(
        LocalVar("s", setT(boolT))(Internal),
        LocalVar("t", setT(boolT))(Internal),
        setT(boolT)
      )(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len((s intersection t))" =>
    }
  }

  test("Printer: should correctly show the size of a set literal") {
    val expr = Length(
      SetLit(intT, Vector(
        IntLit(1)(Internal),
        IntLit(42)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(set[int°] { 1, 42 })" =>
    }
  }

  test("Printer: should correctly show the size of an empty set") {
    val expr = Length(
      SetLit(sequenceT(intT), Vector())(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(set[seq[int°]°] { })" =>
    }
  }

  test("Printer: should correctly show a simple integer multiset type") {
    val typ = multisetT(intT)
    frontend.show(typ) should matchPattern {
      case "mset[int°]°" =>
    }
  }

  test("Printer: should correctly show a nested multiset type") {
    val typ = multisetT(multisetT(boolT))
    frontend.show(typ) should matchPattern {
      case "mset[mset[bool°]°]°" =>
    }
  }

  test("Printer: should correctly show an empty multiset integer literal") {
    val expr = MultisetLit(intT, Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[int°] { }" =>
    }
  }

  test("Printer: should correctly show an empty multiset literal of a nested type") {
    val expr = MultisetLit(multisetT(multisetT(boolT)), Vector())(Internal)
    frontend.show(expr) should matchPattern {
      case "mset[mset[mset[bool°]°]°] { }" =>
    }
  }

  test("Printer: should correctly show a singleton integer multiset literal") {
    val expr = MultisetLit(intT, Vector(
      IntLit(42)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int°] { 42 }" =>
    }
  }

  test("Printer: should correctly show a singleton nested multiset literal") {
    val expr = MultisetLit(multisetT(boolT), Vector(
      MultisetLit(boolT, Vector(
        BoolLit(false)(Internal)
      ))(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[mset[bool°]°] { mset[bool°] { false } }" =>
    }
  }

  test("Printer: should correctly show a non-empty integer multiset literal") {
    val expr = MultisetLit(intT, Vector(
      IntLit(1)(Internal),
      IntLit(2)(Internal),
      IntLit(3)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "mset[int°] { 1, 2, 3 }" =>
    }
  }

  test("Printer: should correctly show the union of two multiset integer literals") {
    val expr = Union(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal),
      multisetT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(mset[int°] { 1, 2 } union mset[int°] { 3 })" =>
    }
  }

  test("Printer: should correctly show the intersection of two multiset integer literals") {
    val expr = Intersection(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal),
      multisetT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(mset[int°] { 1, 2 } intersection mset[int°] { 3 })" =>
    }
  }

  test("Printer: should correctly show a subset relation applied to two multiset literals") {
    val expr = Subset(
      MultisetLit(intT, Vector(IntLit(1)(Internal), IntLit(2)(Internal)))(Internal),
      MultisetLit(intT, Vector(IntLit(3)(Internal)))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(mset[int°] { 1, 2 } subset mset[int°] { 3 })" =>
    }
  }

  test("Printer: should correctly show the cardinality of a multiset literal") {
    val expr = Length(
      MultisetLit(multisetT(intT), Vector(
        MultisetLit(intT, Vector(
          IntLit(1)(Internal)
        ))(Internal),
        MultisetLit(intT, Vector(
          IntLit(2)(Internal),
          IntLit(3)(Internal),
        ))(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(mset[mset[int°]°] { mset[int°] { 1 }, mset[int°] { 2, 3 } })" =>
    }
  }

  test("Printer: should correctly show a multiset inclusion expression (1)") {
    val expr = Contains(
      IntLit(2)(Internal),
      MultisetLit(intT, Vector(
        IntLit(1)(Internal),
        IntLit(2)(Internal),
        IntLit(3)(Internal),
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(2 elem mset[int°] { 1, 2, 3 })" =>
    }
  }

  test("Printer: should correctly show a set conversion of an identifier") {
    val expr = SetConversion(
      LocalVar("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(xs)" =>
    }
  }

  test("Printer: should correctly show a simple set conversion of a range sequence") {
    val expr = SetConversion(
      RangeSequence(
        IntLit(1)(Internal),
        IntLit(42)(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set(seq[1 .. 42])" =>
    }
  }

  test("Printer: should correctly show a set conversion with a sequence append") {
    val expr = SetConversion(
      SequenceAppend(
        LocalVar("xs", sequenceT(boolT))(Internal),
        LocalVar("ys", sequenceT(boolT))(Internal),
        sequenceT(boolT)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "set((xs ++ ys))" =>
    }
  }

  test("Printer: should correctly show the union of two set conversions") {
    val expr = Union(
      SetConversion(LocalVar("xs", sequenceT(boolT))(Internal))(Internal),
      SetConversion(LocalVar("ys", sequenceT(boolT))(Internal))(Internal),
      setT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(set(xs) union set(ys))" =>
    }
  }

  test("Printer: should be able to show a very simple (sequence) multiplicity operator") {
    val expr = Multiplicity(
      LocalVar("x", boolT)(Internal),
      LocalVar("xs", sequenceT(boolT))(Internal),
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(x # xs)" =>
    }
  }

  test("Printer: should correctly show a slightly more complex (sequence) multiplicity operator") {
    val expr = Multiplicity(
      Add(IntLit(40)(Internal), IntLit(2)(Internal))(Internal),
      SequenceLit(3, intT, Map(
        BigInt(0) -> IntLit(1)(Internal),
        BigInt(1) -> IntLit(2)(Internal),
        BigInt(2) -> IntLit(3)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((40 + 2) # seq[int°] { 0:1, 1:2, 2:3 })" =>
    }
  }

  test("Printer: should be able to show a nested (sequence) multiplicity") {
    val expr = Multiplicity(
      Multiplicity(
        LocalVar("x", boolT)(Internal),
        LocalVar("xs", sequenceT(boolT))(Internal),
      )(Internal),
      LocalVar("ys", sequenceT(intT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "((x # xs) # ys)" =>
    }
  }

  test("Printer: should correctly show a very simple multiset conversion") {
    val expr = MultisetConversion(
      LocalVar("xs", sequenceT(boolT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(xs)" =>
    }
  }

  test("Printer: should correctly show a multiset conversion with a sequence concatenation in the inner expression") {
    val expr = MultisetConversion(
      SequenceAppend(
        LocalVar("xs", sequenceT(boolT))(Internal),
        LocalVar("ys", sequenceT(boolT))(Internal),
        sequenceT(boolT)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset((xs ++ ys))" =>
    }
  }

  test("Printer: should correctly show the union of two multiset conversions") {
    val expr = Union(
      MultisetConversion(LocalVar("xs", sequenceT(boolT))(Internal))(Internal),
      MultisetConversion(LocalVar("ys", sequenceT(boolT))(Internal))(Internal),
      multisetT(boolT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(mset(xs) union mset(ys))" =>
    }
  }

  test("Printer: should correctly show a nested multiset conversion") {
    val expr = MultisetConversion(
      MultisetConversion(
        LocalVar("xs", sequenceT(boolT))(Internal)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "mset(mset(xs))" =>
    }
  }

  test("Printer: should correctly show a simple exclusive integer array type") {
    val t = exclusiveArrayT(42, intT)

    frontend.show(t) should matchPattern {
      case "[42]int°°" =>
    }
  }

  test("Printer: should correctly show an exclusive multidimensional Boolean array type") {
    val t = exclusiveArrayT(1, exclusiveArrayT(2, exclusiveArrayT(3, boolT)))

    frontend.show(t) should matchPattern {
      case "[1][2][3]bool°°°°" =>
    }
  }

  test("Printer: should correctly show an exclusive sequence array type") {
    val t = exclusiveArrayT(12, sequenceT(intT))

    frontend.show(t) should matchPattern {
      case "[12]seq[int°]°°" =>
    }
  }

  test("Printer: should correctly show a sequence type of exclusive Boolean arrays") {
    val t = sequenceT(exclusiveArrayT(42, boolT))

    frontend.show(t) should matchPattern {
      case "seq[[42]bool°°]°" =>
    }
  }

  test("Printer: should correctly show a very simple exclusive array length expression") {
    val expr = Length(
      LocalVar("a", exclusiveArrayT(12, intT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(a)" =>
    }
  }

  test("Printer: should be able to show the addition of two uses of the array length function") {
    val expr = Add(
      Length(LocalVar("a", exclusiveArrayT(24, boolT))(Internal))(Internal),
      Length(LocalVar("b", exclusiveArrayT(24, boolT))(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(len(a) + len(b))" =>
    }
  }

  test("Printer: should be able to show a very simple sequence length expression") {
    val expr = Length(
      LocalVar("xs", sequenceT(intT))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(xs)" =>
    }
  }

  test("Printer: should be able to show a slightly more complicated use of the sequence length function") {
    val expr = Length(
      SequenceAppend(
        SequenceLit(1, boolT, Map(BigInt(0) -> BoolLit(false)(Internal)))(Internal),
        LocalVar("xs", sequenceT(boolT))(Internal),
        sequenceT(boolT)
      )(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len((seq[bool°] { 0:false } ++ xs))" =>
    }
  }

  test("Printer: should correctly show a nested use of the built-in sequence length operator") {
    val expr = Length(
      Length(SequenceLit(0, intT, Map())(Internal))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "len(len(seq[int°] { }))" =>
    }
  }

  test("Printer: should correctly show a simple array indexing expression") {
    val expr = IndexedExp(
      LocalVar("a", exclusiveArrayT(124, intT))(Internal),
      IntLit(42)(Internal),
      sequenceT(intT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[42]" =>
    }
  }

  test("Printer: should correctly show an array indexing operation with a slightly more complex right-hand side") {
    val typ = exclusiveArrayT(124, intT)
    val expr = IndexedExp(
      LocalVar("a", typ)(Internal),
      Add(
        LocalVar("x", intT)(Internal),
        IntLit(2)(Internal)
      )(Internal),
      typ
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[(x + 2)]" =>
    }
  }

  test("Printer: should correctly show a small chain of array indexing operations") {
    val typExt = exclusiveArrayT(12, exclusiveArrayT(24, boolT))
    val typIn = exclusiveArrayT(24, boolT)
    val expr = IndexedExp(
      IndexedExp(
        LocalVar("a", typIn)(Internal),
        IntLit(2)(Internal),
        typIn
      )(Internal),
      IntLit(4)(Internal),
      typExt
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "a[2][4]" =>
    }
  }

  test("Printer: should be able to correctly show a very simple 'acc' predicate applied on an array") {
    val typ = sharedArrayT(12, sharedArrayT(24, BoolT(Addressability.Shared)))
    val expr = Access(
      Accessible.Address(
        IndexedExp(
          LocalVar("a", typ)(Internal),
          IntLit(2)(Internal),
          typ
        )(Internal)
      ),
      FullPerm(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "acc(a[2])" =>
    }
  }

  test("Printer: should correctly show a simple integer array literal") {
    val expr = ArrayLit(3, intT, Map(
      BigInt(0) -> IntLit(12)(Internal),
      BigInt(1) -> IntLit(24)(Internal),
      BigInt(2) -> IntLit(48)(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "[3]int° { 0:12, 1:24, 2:48 }" =>
    }
  }

  test("Printer: should correctly show an empty Boolean array literal") {
    val expr = ArrayLit(0, boolT, Map())(Internal)

    frontend.show(expr) should matchPattern {
      case "[0]bool° { }" =>
    }
  }

  test("Printer: should correctly show a nested array literal") {
    val expr = ArrayLit(1, exclusiveArrayT(2, intT), Map(
      BigInt(0) -> ArrayLit(2, intT, Map(
        BigInt(0) -> IntLit(24)(Internal),
        BigInt(1) -> IntLit(42)(Internal)
      ))(Internal)
    ))(Internal)

    frontend.show(expr) should matchPattern {
      case "[1][2]int°° { 0:[2]int° { 0:24, 1:42 } }" =>
    }
  }

  test("Printer: should correctly show an empty 3D array literal") {
    val expr = ArrayLit(
      0,
      exclusiveArrayT(24, exclusiveArrayT(48, boolT)),
      Map()
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "[0][24][48]bool°°° { }" =>
    }
  }

  test("Printer: should correctly show a sequence array literal") {
    val expr = ArrayLit(
      1,
      sequenceT(intT),
      Map(BigInt(0) -> SequenceLit(0, intT, Map())(Internal))
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "[1]seq[int°]° { 0:seq[int°] { } }" =>
    }
  }

  test("Printer: should correctly show a shared (integer) array type") {
    val typ = sharedArrayT(12, IntT(Addressability.Shared, TypeBounds.DefaultInt))

    frontend.show(typ) should matchPattern {
      case "[12]int@@" =>
    }
  }

  test("Printer: should correctly show a nested shared (Boolean) array type") {
    val typ = sharedArrayT(12, sharedArrayT(24, BoolT(Addressability.Shared)))

    frontend.show(typ) should matchPattern {
      case "[12][24]bool@@@" =>
    }
  }

  test("Printer: should correctly show a simple sequence conversion expression") {
    val expr = SequenceConversion(
      SequenceLit(3, intT, Map(
        BigInt(0) -> IntLit(1)(Internal),
        BigInt(1) -> IntLit(2)(Internal),
        BigInt(2) -> IntLit(4)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq(seq[int°] { 0:1, 1:2, 2:4 })" =>
    }
  }

  test("Printer: should correctly show a simple conversion expression from an array literal to a sequence") {
    val expr = SequenceConversion(
      ArrayLit(2, boolT, Map(
        BigInt(0) -> BoolLit(false)(Internal),
        BigInt(1) -> BoolLit(true)(Internal)
      ))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "seq([2]bool° { 0:false, 1:true })" =>
    }
  }

  test("Printer: should correctly show the append of two sequence conversion operations") {
    val expr = SequenceAppend(
      SequenceConversion(LocalVar("xs", sequenceT(intT))(Internal))(Internal),
      SequenceConversion(LocalVar("a", exclusiveArrayT(6, intT))(Internal))(Internal),
      sequenceT(intT)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "(seq(xs) ++ seq(a))" =>
    }
  }

  test("Printer: should correctly show a simple (exclusive) option type") {
    val a = Addressability.Exclusive
    val typ = OptionT(OptionT(BoolT(a), a), a)

    frontend.show(typ) should matchPattern {
      case "option[option[bool°]°]°" =>
    }
  }

  test("Printer: should correctly show a simple (shared) option type") {
    val a = Addressability.Shared
    val typ = OptionT(OptionT(BoolT(a), a), a)

    frontend.show(typ) should matchPattern {
      case "option[option[bool@]@]@" =>
    }
  }

  test("Printer: should correctly show an 'none' option type expression") {
    val exp = OptionNone(sequenceT(intT))(Internal)

    frontend.show(exp) should matchPattern {
      case "none[seq[int°]°]" =>
    }
  }

  test("Printer: should correctly show an 'some' option type expression") {
    val exp = OptionSome(And(BoolLit(true)(Internal), BoolLit(false)(Internal))(Internal))(Internal)

    frontend.show(exp) should matchPattern {
      case "some((true && false))" =>
    }
  }

  test("Printer: should correctly show an 'get(...)' expression") {
    val exp = OptionGet(OptionSome(IntLit(23)(Internal))(Internal))(Internal)

    frontend.show(exp) should matchPattern {
      case "get(some(23))" =>
    }
  }

  test("Printer: should correctly show a simple integer slice type") {
    val a = Addressability.Exclusive
    val t = SliceT(intT, a)

    frontend.show(t) should matchPattern {
      case "[]int°°" =>
    }
  }

  test("Printer: should correctly show a slightly more complex slice type") {
    val a = Addressability.Exclusive
    val t = SliceT(SetT(SequenceT(boolT, a), a), a)

    frontend.show(t) should matchPattern {
      case "[]set[seq[bool°]°]°°" =>
    }
  }

  test("Printer: should correctly show a nested slice type") {
    val a = Addressability.Exclusive
    val t = SliceT(SliceT(SliceT(SliceT(intT, a), a), a), a)

    frontend.show(t) should matchPattern {
      case "[][][][]int°°°°°" =>
    }
  }

  test("Printer: should correctly show a simple capacity expression") {
    val expr = Capacity(
      LocalVar("s", SliceT(intT, Addressability.Exclusive))(Internal)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "cap(s)" =>
    }
  }

  test("Printer: should correctly show a simple full slice expression") {
    val expr = Slice(
      LocalVar("s", SliceT(intT, Addressability.Exclusive))(Internal),
      IntLit(2)(Internal),
      IntLit(4)(Internal),
      Some(IntLit(6)(Internal)),
      SliceT(intT, Addressability.Exclusive)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s[2:4:6]" =>
    }
  }

  test("Printer: should correctly show a (partial) slice expression") {
    val expr = Slice(
      LocalVar("s", SliceT(intT, Addressability.Exclusive))(Internal),
      IntLit(8)(Internal),
      IntLit(4)(Internal),
      None,
      SliceT(intT, Addressability.Exclusive)
    )(Internal)

    frontend.show(expr) should matchPattern {
      case "s[8:4]" =>
    }
  }


  /* * Stubs, mocks, and other test setup  */

  class TestFrontend {
    val printer = new DefaultPrettyPrinter()
    def show(n : Node) : String = printer.format(n)
    def show(t : Type) : String = printer.format(t)
  }
}
