// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import viper.gobra.util.TypeBounds.{Byte, DefaultInt, DefaultUInt, IntegerKind, Rune, SignedInteger16, SignedInteger32, SignedInteger64, SignedInteger8, UIntPtr, UnboundedInteger, UnsignedInteger16, UnsignedInteger32, UnsignedInteger64, UnsignedInteger8}
import viper.gobra.util.Violation.violation

/**
  * Defines the integer type bounds for an execution of Gobra
  */
case class TypeBounds(Int: IntegerKind = DefaultInt,
                      Int8: IntegerKind = SignedInteger8,
                      Int16: IntegerKind = SignedInteger16,
                      Int32: IntegerKind = SignedInteger32,
                      Int64: IntegerKind = SignedInteger64,
                      UInt: IntegerKind = DefaultUInt,
                      UInt8: IntegerKind = UnsignedInteger8,
                      UInt16: IntegerKind = UnsignedInteger16,
                      UInt32: IntegerKind = UnsignedInteger32,
                      UInt64: IntegerKind = UnsignedInteger64,
                      UIntPtr: IntegerKind = UIntPtr,
                      UntypedConst: IntegerKind = UnboundedInteger,
                      Byte: IntegerKind = Byte,
                      Rune: IntegerKind = Rune)

/**
  * Supported Integer Kinds and its respective bounds
  */
object TypeBounds {
  sealed abstract case class IntegerKind(name: String)

  object UnboundedInteger extends IntegerKind("integer")

  sealed abstract class BoundedIntegerKind(override val name: String, val lower: BigInt, val upper: BigInt) extends IntegerKind(name)

  object SignedInteger8 extends BoundedIntegerKind("int8", -128, 127)
  object SignedInteger16 extends BoundedIntegerKind("int16", -32768, 32767)

  sealed abstract class AbstractSignedInteger32(override val name: String) extends BoundedIntegerKind(name, -2147483648, 2147483647)
  object DefaultInt extends AbstractSignedInteger32("int") // int definition when Gobra runs in 32-bit (i.e. default) mode
  object SignedInteger32 extends AbstractSignedInteger32("int32")
  object Rune extends AbstractSignedInteger32("rune")

  sealed abstract class AbstractSignedInteger64(override val name: String) extends BoundedIntegerKind(name, -9223372036854775808L, 9223372036854775807L)
  object SignedInteger64 extends AbstractSignedInteger64("int64")
  object IntWith64Bit extends AbstractSignedInteger64("int") // int definition when Gobra runs in 64-bit mode

  sealed abstract class AbstractUnsignedInteger8(override val name: String) extends BoundedIntegerKind(name, 0, 255)
  object Byte extends AbstractUnsignedInteger8("byte")
  object UnsignedInteger8 extends AbstractUnsignedInteger8("uint8")

  object UnsignedInteger16 extends BoundedIntegerKind("uint16", 0, 65535)

  sealed abstract class AbstractUnsignedInteger32(override val name: String) extends BoundedIntegerKind(name, 0, 4294967295L)
  object UnsignedInteger32 extends AbstractUnsignedInteger32("uint32")
  object DefaultUInt extends AbstractUnsignedInteger32("uint") // uint definition when Gobra runs in 32-bit mode

  sealed abstract class AbstractUnsignedInteger64(override val name: String) extends BoundedIntegerKind(name, 0, BigInt("18446744073709551615"))
  object UnsignedInteger64 extends AbstractUnsignedInteger64("uint64")
  object UIntWith64Bit extends AbstractUnsignedInteger64("uint") // uint definition when Gobra runs in 64-bit mode
  object UIntPtr extends AbstractUnsignedInteger64("uintptr")

  def merge(integerKind1: IntegerKind, integerKind2: IntegerKind): IntegerKind = (integerKind1, integerKind2) match {
    case (a, b) if a == b => a
    case (a, UnboundedInteger) => a
    case (UnboundedInteger, b) => b
    case _ => violation(s"kinds $integerKind1 and $integerKind2 cannot be merged")
  }
}