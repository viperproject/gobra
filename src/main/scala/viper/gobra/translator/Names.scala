// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

object Names {
  // fresh name prefix
  def freshNamePrefix: String = "fn$$"

  def returnLabel: String = "returnLabel"

  /**
    * Hashes the argument. The result is a valid Viper name if prepended with a valid starting character.
    * For instance, the first character of the result may be a digit.
    */
  def hash(s: String): String = {
    scala.util.hashing.MurmurHash3.stringHash(s).toHexString
  }

  object InterfaceMethod {
    /**
      * To copy an interface method, the copied method must use a method proxy returned from this function.
      * @param ext must not contain '$'
      * */
    def copy(proxy: in.MethodProxy, ext: String): in.MethodProxy =
      in.MethodProxy(proxy.name, s"${proxy.uniqueName}$$itfcopy$$$ext")(proxy.info)

    /** Returns the original proxy for a copy or not copied proxy. */
    def origin(proxy: in.MethodProxy): in.MethodProxy = {
      val splits = proxy.uniqueName.split('$')
      if (splits.length > 2 && splits(splits.length-2) == "itfcopy") {
        val originUniqueName = splits.take(splits.length-2).mkString("$")
        in.MethodProxy(proxy.name, originUniqueName)(proxy.info)
      } else proxy // no match
    }
  }

  /* sanitizes type name to a valid Viper name */
  def serializeType(t: vpr.Type): String = {
    t.toString()
      .replace('[', '_')
      .replace("]", "")
      .replace(",", "") // a parameterized Viper type uses comma-space separated types if there are multiple
      .replace(" ", "")
  }

  def serializeType(typ: in.Type): String = typ match {
    case in.BoolT(addr) => s"Bool${serializeAddressability(addr)}"
    case in.StringT(addr) => s"String${serializeAddressability(addr)}"
    case in.IntT(addr, kind) => s"Int${kind.name}${serializeAddressability(addr)}"
    case in.VoidT => ""
    case in.PermissionT(addr) => s"Permission${serializeAddressability(addr)}"
    case in.SortT => "Sort"
    case in.ArrayT(len, elemT, addr) => s"Array$len${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.SliceT(elemT, addr) => s"Slice${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.MapT(keyT, valueT, addr) => s"Map${serializeType(keyT)}_${serializeType(valueT)}_${serializeAddressability(addr)}"
    case in.SequenceT(elemT, addr) => s"Sequence${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.SetT(elemT, addr) => s"Set${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.MultisetT(elemT, addr) => s"Multiset${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.OptionT(elemT, addr) => s"Option${serializeType(elemT)}${serializeAddressability(addr)}"
    case in.DefinedT(name, addr) => s"Defined$name${serializeAddressability(addr)}"
    case in.PointerT(t, addr) => s"Pointer${serializeType(t)}${serializeAddressability(addr)}"
    // we use a dollar sign to mark the beginning and end of the type list to avoid that `Tuple(Tuple(X), Y)` and `Tuple(Tuple(X, Y))` map to the same name:
    case in.TupleT(ts, addr) => s"Tuple$$${ts.map(serializeType).mkString("")}$$${serializeAddressability(addr)}"
    case in.PredT(ts, addr) => s"Pred$$${ts.map(serializeType).mkString("")}$$${serializeAddressability(addr)}"
    case in.StructT(fields, addr) => s"Struct${serializeFields(fields)}${serializeAddressability(addr)}"
    case in.FunctionT(args, res, addr) => s"Func$$${args.map(serializeType).mkString("")}$$${res.map(serializeType).mkString("")}$$${serializeAddressability(addr)}"
    case in.InterfaceT(name, addr) => s"Interface$name${serializeAddressability(addr)}"
    case in.ChannelT(elemT, addr) => s"Channel${serializeType(elemT)}${serializeAddressability(addr)}"
    case t => Violation.violation(s"cannot stringify type $t")
  }

  def serializeAddressability(addr: Addressability): String = addr match {
    case Addressability.Shared => "$$$_S_$$$"
    case Addressability.Exclusive => "$$$$_E_$$$"
  }

  def serializeFields(fields: Vector[in.Field]): String = {
    val serializedFields = fields.map(f => s"${f.name}_${serializeType(f.typ)}").mkString("_")
    // we use a dollar sign to mark the beginning and end of the type list to avoid that `Tuple(Tuple(X), Y)` and `Tuple(Tuple(X, Y))` map to the same name:
    s"$$$serializedFields$$"
  }

  /**
    * Serialize the given type to a string.
    * Information about addressability is disregarded.
    * Ignoring addressability, there is a 1-to-1 mapping from a type to a string.
    */
  def serializeTypeIgnoringAddr(typ: in.Type): String = typ match {
    case in.BoolT(_) => s"B"
    case in.StringT(_) => s"String"
    case in.IntT(_, kind) => s"I$$${kind.name}$$"
    case in.VoidT => "V"
    case in.PermissionT(_) => s"Perm"
    case in.SortT => "Sort"
    case in.ArrayT(len, elemT, _) => s"Arr$len${serializeTypeIgnoringAddr(elemT)}"
    case in.SliceT(elemT, _) => s"Sl${serializeTypeIgnoringAddr(elemT)}"
    case in.MapT(keyT, valueT, _) => s"Map${serializeTypeIgnoringAddr(keyT)}${serializeTypeIgnoringAddr(valueT)}"
    case in.SequenceT(elemT, _) => s"Seq${serializeTypeIgnoringAddr(elemT)}"
    case in.SetT(elemT, _) => s"Set${serializeTypeIgnoringAddr(elemT)}"
    case in.MultisetT(elemT, _) => s"Mset${serializeTypeIgnoringAddr(elemT)}"
    case in.OptionT(elemT, _) => s"Opt${serializeTypeIgnoringAddr(elemT)}"
    case in.DefinedT(name, _) => s"Def$$$name$$"
    case in.PointerT(t, _) => s"Ptr${serializeTypeIgnoringAddr(t)}"
    case in.TupleT(ts, _) => s"T${ts.map(serializeTypeIgnoringAddr).mkString("")}$$"
    case in.PredT(ts, _) => s"Pred${ts.map(serializeTypeIgnoringAddr).mkString("")}$$"
    case in.StructT(fields, _) => s"Struct$$${fields.map(f => s"${f.name}$$${serializeTypeIgnoringAddr(f.typ)}").mkString("$")}$$$$"
    case in.FunctionT(args, res, _) => s"F${args.map(serializeTypeIgnoringAddr).mkString("")}$$${res.map(serializeTypeIgnoringAddr).mkString("")}$$"
    case in.InterfaceT(name, _) => s"Ifce$$$name$$"
    case in.ChannelT(elemT, _) => s"Ch${serializeTypeIgnoringAddr(elemT)}"
    case t => Violation.violation(s"cannot stringify type $t")
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

  // closures
  def closureDomain: String = "Closure"
  def closureDefaultFunc: String = "closureDefault"
  def funcLitGetter: String = "getClosure"
  def satisfiesFunc: String = "closureSatisfies"

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

  // tuples
  def tupleDomain: String = "Tuple"

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
