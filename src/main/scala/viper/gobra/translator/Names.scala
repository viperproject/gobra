// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

object Names {
  def returnLabel: String = "returnLabel"

  def freshName(ctx: Context): String = s"fn$$$$${ctx.getAndIncrementFreshVariableCounter}"

  /* sanitizes type name to a valid Viper name */
  def serializeType(t: vpr.Type): String = {
    t.toString()
      .replace('[', '_')
      .replace("]", "")
      .replace(",", "") // a parameterized Viper type uses comma-space separated types if there are multiple
      .replace(" ", "")
  }

  def serializeType(typ: in.Type): String = typ match {
    case _: in.BoolT => "Bool"
    case _: in.StringT => "String"
    case in.IntT(_, kind) => s"Int${kind.name}"
    case in.VoidT => ""
    case _: in.PermissionT => "Permission"
    case in.SortT => "Sort"
    case in.ArrayT(len, elemT, _) => s"Array$len${serializeType(elemT)}"
    case in.SliceT(elemT, _) => s"Slice${serializeType(elemT)}"
    case in.MapT(keyT, valueT, _) => s"Map${serializeType(keyT)}_${serializeType(valueT)}"
    case in.SequenceT(elemT, _) => s"Sequence${serializeType(elemT)}"
    case in.SetT(elemT, _) => s"Set${serializeType(elemT)}"
    case in.MultisetT(elemT, _) => s"Multiset${serializeType(elemT)}"
    case in.OptionT(elemT, _) => s"Option${serializeType(elemT)}"
    case in.DefinedT(name, _) => s"Defined$name"
    case in.PointerT(t, _) => s"Pointer${serializeType(t)}"
    // we use a dollar sign to mark the beginning and end of the type list to avoid that `Tuple(Tuple(X), Y)` and `Tuple(Tuple(X, Y))` map to the same name:
    case in.TupleT(ts, _) => s"Tuple$$${ts.map(serializeType).mkString("")}$$"
    case in.PredT(ts, _) => s"Pred$$${ts.map(serializeType).mkString("")}$$"
    case in.StructT(fields, _) => s"Struct${serializeFields(fields)}"
    case in.InterfaceT(name, _) => s"Interface$name"
    case in.ChannelT(elemT, _) => s"Channel${serializeType(elemT)}"
    case t => Violation.violation(s"cannot stringify type $t")
  }

  def serializeFields(fields: Vector[in.Field]): String = {
    val serializedFields = fields.map(f => s"${f.name}_${serializeType(f.typ)}").mkString("_")
    // we use a dollar sign to mark the beginning and end of the type list to avoid that `Tuple(Tuple(X), Y)` and `Tuple(Tuple(X, Y))` map to the same name:
    s"$$$serializedFields$$"
  }


  // assert
  def assertFunc: String = "assertArg1"
  def typedAssertFunc(t: vpr.Type): String = s"assertArg2_${serializeType(t)}"

  // equality
  def equalityDomain: String = "Equality"
  def equalityFunc: String = "eq"

  // embedding domain
  def embeddingDomain: String = "Emb"
  def embeddingBoxFunc: String = "box"
  def embeddingUnboxFunc: String = "unbox"

  // polymorph-value domain
  def polyValueDomain: String = "Poly"
  def polyValueBoxFunc: String = "box"
  def polyValueUnboxFunc: String = "unbox"

  // interface
  def emptyInterface: String = "empty_interface"
  def toInterfaceFunc: String = "toInterface"
  def typeOfFunc: String = "typeOfInterface"
  def dynamicPredicate: String = "dynamic_pred"
  def implicitThis: String = "thisItf"

  // pointer
  def pointerField(t : vpr.Type) : String = {
    s"val$$_${serializeType(t)}"
  }

  // struct
  def sharedStructDomain: String = "ShStruct"
  def sharedStructDfltFunc: String = "shStructDefault"

  // types
  def typesDomain: String = "Types"
  def stringsDomain: String = "String"
  def mapsDomain: String = "GobraMap"

  // array
  def sharedArrayDomain: String = "ShArray"
  def arrayConversionFunc: String = "arrayConversion"
  def arrayDefaultFunc: String = "arrayDefault"
  def arrayNilFunc: String = "arrayNil"

  // slices
  def fullSliceFromArray: String = "sfullSliceFromArray"
  def fullSliceFromSlice: String = "sfullSliceFromSlice"
  def sliceConstruct: String = "sconstruct"
  def sliceDefaultFunc: String = "sliceDefault"
  def sliceFromArray: String = "ssliceFromArray"
  def sliceFromSlice: String = "ssliceFromSlice"

  // sequences
  def emptySequenceFunc: String = "sequenceEmpty"

  // predicate
  def predDomain: String = "Pred"

  // domain
  def dfltDomainValue(domainName: String): String = s"dflt$domainName"

  // unknown values
  def unknownValuesDomain: String = "UnknownValueDomain"
  def unknownValueFunc: String = "unknownValue"

  // built-in members
  def builtInMember: String = "built_in"

  // ints
  def bitwiseAnd: String = "intBitwiseAnd"
  def bitwiseOr: String = "intBitwiseOr"
  def bitwiseXor: String = "intBitwiseXor"
  def bitClear: String = "intBitClear"
  def shiftLeft: String = "intShiftLeft"
  def shiftRight: String = "intShiftRight"
  def bitwiseNeg: String = "intBitwiseNeg"
}
