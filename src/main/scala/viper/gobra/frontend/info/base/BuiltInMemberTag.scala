// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import viper.gobra.frontend.info.base.Type.{BooleanT, IntT, PermissionT, StringT, Type}
import viper.gobra.util.TypeBounds


/**
  * Module to add built-in functions, methods, fpredicates, and mpredicates to Gobra.
  * Four steps have to be performed for adding a new built-in member:
  * (1) add a tag representing that built-in member,
  * (2) add it to `builtInMembers()`, and
  * (3) add type-checking code in file `BuiltInMemberTyping.scala`
  * (4) add an additional case to BuiltInMembersImpl that maps that built-in member (the tag and its specific use) to
  * a generated member in the internal representation which is then encoded.
  * The desugarer automatically resolves function and methods calls to as well as instances of these built-in members.
  * Thus, no changes in the desugarer should be necessary.
  */
object BuiltInMemberTag {
  sealed trait BuiltInMemberTag {
    /** identifier of this member as it appears in the program text */
    def identifier: String
    /** debug name for printing */
    def name: String
    /** flag whether the entire member is ghost */
    def ghost: Boolean
  }

  sealed trait ActualBuiltInMember extends BuiltInMemberTag

  sealed trait GhostBuiltInMember extends BuiltInMemberTag {
    override def ghost: Boolean = true
  }

  sealed trait BuiltInTypeTag extends ActualBuiltInMember {
    def typ: Type
  }

  sealed trait BuiltInFunctionTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInFPredicateTag extends GhostBuiltInMember
  sealed trait BuiltInMethodTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInMPredicateTag extends GhostBuiltInMember

  /** Built-in Type Tags */


  case object BoolType extends BuiltInTypeTag {
    override def identifier: String = "bool"
    override def name: String = "BoolType"
    override def ghost: Boolean = false
    override def typ: Type = BooleanT
  }
  case object StringType extends BuiltInTypeTag {
    override def identifier: String = "string"
    override def name: String = "StringType"
    override def ghost: Boolean = false
    override def typ: Type = StringT
  }
  case object PermissionType extends BuiltInTypeTag {
    override def identifier: String = "perm"
    override def name: String = "PermissionType"
    override def ghost: Boolean = true
    override def typ: Type = PermissionT
  }
  // signed integer types
  case object Rune extends BuiltInTypeTag {
    override def identifier: String = "rune"
    override def name: String = "Rune"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.Rune)
  }
  case object IntType extends BuiltInTypeTag {
    override def identifier: String = "int"
    override def name: String = "IntType"
    override def ghost: Boolean = false
    // TODO: Get the right type
    override def typ: Type = IntT(TypeBounds.SignedInteger32)
  }
  case object Int8Type extends BuiltInTypeTag {
    override def identifier: String = "int8"
    override def name: String = "Int8Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger8)
  }
  case object Int16Type extends BuiltInTypeTag {
    override def identifier: String = "int16"
    override def name: String = "Int16Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger16)
  }
  case object Int32Type extends BuiltInTypeTag {
    override def identifier: String = "int32"
    override def name: String = "Int32Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger32)
  }
  case object Int64Type extends BuiltInTypeTag {
    override def identifier: String = "int64"
    override def name: String = "Int64Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger64)
  }
  // unsigned integer types
  case object Byte extends BuiltInTypeTag {
    override def identifier: String = "byte"
    override def name: String = "Byte"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.Byte)
  }
  case object UIntType extends BuiltInTypeTag {
    override def identifier: String = "uint"
    override def name: String = "UIntType"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger32)
  }
  case object UInt8Type extends BuiltInTypeTag {
    override def identifier: String = "uint8"
    override def name: String = "UInt8Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger8)
  }
  case object UInt16Type extends BuiltInTypeTag {
    override def identifier: String = "uint16"
    override def name: String = "UInt16Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger16)
  }
  case object UInt32Type extends BuiltInTypeTag {
    override def identifier: String = "uint32"
    override def name: String = "UInt32Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger32)
  }
  case object UInt64Type extends BuiltInTypeTag {
    override def identifier: String = "uint64"
    override def name: String = "UInt64Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger64)
  }
  case object UIntPtr extends BuiltInTypeTag {
    override def identifier: String = "uintptr"
    override def name: String = "UIntPtr"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UIntPtr)
  }


  /** Built-in Function Tags */

  case object CloseFunctionTag extends BuiltInFunctionTag {
    override def identifier: String = "close"
    override def name: String = "CloseFunctionTag"
    override def ghost: Boolean = false
    override def isPure: Boolean = false
  }

  case object AppendFunctionTag extends BuiltInFunctionTag {
    override def identifier: String = "append"
    override def name: String = "AppendFunctionTag"
    override def ghost: Boolean = false
    override def isPure: Boolean = false
  }

  case object CopyFunctionTag extends BuiltInFunctionTag {
    override def identifier: String = "copy"
    override def name: String = "CopyFunctionTag"
    override def ghost: Boolean = false
    override def isPure: Boolean = false
  }

  /** Built-in FPredicate Tags */

  case object PredTrueFPredTag extends BuiltInFPredicateTag {
    override def identifier: String = "PredTrue"
    override def name: String = "PredTrueFPredTag"
  }


  /** Built-in Method Tags */
  case object BufferSizeMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "BufferSize"
    override def name: String = "BufferSizeMethodTag"
    override def isPure: Boolean = true
  }

  sealed trait ChannelInvariantMethodTag extends BuiltInMethodTag with GhostBuiltInMember
  sealed trait SendPermMethodTag extends ChannelInvariantMethodTag
  case object SendGivenPermMethodTag extends SendPermMethodTag {
    override def identifier: String = "SendGivenPerm"
    override def name: String = "SendGivenPermMethodTag"
    override def isPure: Boolean = true
  }

  case object SendGotPermMethodTag extends SendPermMethodTag {
    override def identifier: String = "SendGotPerm"
    override def name: String = "SendGotPermMethodTag"
    override def isPure: Boolean = true
  }

  sealed trait RecvPermMethodTag extends ChannelInvariantMethodTag
  case object RecvGivenPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGivenPerm"
    override def name: String = "RecvGivenPermMethodTag"
    override def isPure: Boolean = true
  }

  case object RecvGotPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGotPerm"
    override def name: String = "RecvGotPermMethodTag"
    override def isPure: Boolean = true
  }

  case object InitChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Init"
    override def name: String = "InitChannelMethodTag"
    override def isPure: Boolean = false
  }

  case object CreateDebtChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "CreateDebt"
    override def name: String = "CreateDebtChannelMethodTag"
    override def isPure: Boolean = false
  }

  case object RedeemChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Redeem"
    override def name: String = "RedeemChannelMethodTag"
    override def isPure: Boolean = false
  }


  /** Built-in MPredicate Tags */

  case object IsChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "IsChannel"
    override def name: String = "IsChannelMPredTag"
  }

  case object SendChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "SendChannel"
    override def name: String = "SendChannelMPredTag"
  }

  case object RecvChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "RecvChannel"
    override def name: String = "RecvChannelMPredTag"
  }

  case object ClosedMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Closed"
    override def name: String = "ClosedMPredTag"
  }

  case object ClosureDebtMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "ClosureDebt"
    override def name: String = "ClosureDebtMPredTag"
  }

  case object TokenMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Token"
    override def name: String = "TokenMPredTag"
  }


  /**
    * Returns a vector of tags belonging to built-in members that should be considered during name resolution
    */
  def builtInMembers(): Vector[BuiltInMemberTag] = Vector(
    // types
    BoolType,
    StringType,
    PermissionType,
    // signed integer types
    Rune,
    IntType,
    Int8Type,
    Int16Type,
    Int32Type,
    Int64Type,
    // unsigned integer types
    Byte,
    UIntType,
    UInt8Type,
    UInt16Type,
    UInt32Type,
    UInt64Type,
    UIntPtr,
    // functions
    CloseFunctionTag,
    AppendFunctionTag,
    CopyFunctionTag,
    // fpredicates
    PredTrueFPredTag,
    // methods
    BufferSizeMethodTag,
    SendGivenPermMethodTag,
    SendGotPermMethodTag,
    RecvGivenPermMethodTag,
    RecvGotPermMethodTag,
    InitChannelMethodTag,
    CreateDebtChannelMethodTag,
    RedeemChannelMethodTag,
    // mpredicates
    IsChannelMPredTag,
    SendChannelMPredTag,
    RecvChannelMPredTag,
    ClosedMPredTag,
    ClosureDebtMPredTag,
    TokenMPredTag
  )
}
