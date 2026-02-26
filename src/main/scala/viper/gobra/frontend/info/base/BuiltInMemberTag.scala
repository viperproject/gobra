// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import viper.gobra.ast.frontend.{PBoolType, PByte, PFloat32, PFloat64, PInt16Type, PInt32Type, PInt64Type, PInt8Type, PIntType, PPermissionType, PRune, PStringType, PType, PUInt16Type, PUInt32Type, PUInt64Type, PUInt8Type, PUIntPtr, PUIntType}
import viper.gobra.frontend.info.base.Type.{BooleanT, Float32T, Float64T, IntT, PermissionT, StringT, Type}
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
    def node: PType
  }

  sealed trait BuiltInPredicateTag extends GhostBuiltInMember {
    def isAbstract: Boolean
  }

  sealed trait BuiltInFunctionTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInFPredicateTag extends BuiltInPredicateTag
  sealed trait BuiltInMethodTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInMPredicateTag extends BuiltInPredicateTag

  /** Built-in Type Tags */


  case object BoolType extends BuiltInTypeTag {
    override def identifier: String = "bool"
    override def name: String = "BoolType"
    override def ghost: Boolean = false
    override def typ: Type = BooleanT
    override def node: PType = PBoolType()
  }
  case object StringType extends BuiltInTypeTag {
    override def identifier: String = "string"
    override def name: String = "StringType"
    override def ghost: Boolean = false
    override def typ: Type = StringT
    override def node: PType = PStringType()
  }
  case object PermissionType extends BuiltInTypeTag {
    override def identifier: String = "perm"
    override def name: String = "PermissionType"
    override def ghost: Boolean = false
    override def typ: Type = PermissionT
    override def node: PType = PPermissionType()
  }
  // signed integer types
  case object Rune extends BuiltInTypeTag {
    override def identifier: String = "rune"
    override def name: String = "Rune"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.Rune)
    override def node: PType = PRune()
  }
  case object IntType extends BuiltInTypeTag {
    override def identifier: String = "int"
    override def name: String = "IntType"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.DefaultInt)
    override def node: PType = PIntType()
  }
  case object Int8Type extends BuiltInTypeTag {
    override def identifier: String = "int8"
    override def name: String = "Int8Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger8)
    override def node: PType = PInt8Type()
  }
  case object Int16Type extends BuiltInTypeTag {
    override def identifier: String = "int16"
    override def name: String = "Int16Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger16)
    override def node: PType = PInt16Type()
  }
  case object Int32Type extends BuiltInTypeTag {
    override def identifier: String = "int32"
    override def name: String = "Int32Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger32)
    override def node: PType = PInt32Type()
  }
  case object Int64Type extends BuiltInTypeTag {
    override def identifier: String = "int64"
    override def name: String = "Int64Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.SignedInteger64)
    override def node: PType = PInt64Type()
  }
  // unsigned integer types
  case object Byte extends BuiltInTypeTag {
    override def identifier: String = "byte"
    override def name: String = "Byte"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.Byte)
    override def node: PType = PByte()
  }
  case object UIntType extends BuiltInTypeTag {
    override def identifier: String = "uint"
    override def name: String = "UIntType"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.DefaultUInt)
    override def node: PType = PUIntType()
  }
  case object UInt8Type extends BuiltInTypeTag {
    override def identifier: String = "uint8"
    override def name: String = "UInt8Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger8)
    override def node: PType = PUInt8Type()
  }
  case object UInt16Type extends BuiltInTypeTag {
    override def identifier: String = "uint16"
    override def name: String = "UInt16Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger16)
    override def node: PType = PUInt16Type()
  }
  case object UInt32Type extends BuiltInTypeTag {
    override def identifier: String = "uint32"
    override def name: String = "UInt32Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger32)
    override def node: PType = PUInt32Type()
  }
  case object UInt64Type extends BuiltInTypeTag {
    override def identifier: String = "uint64"
    override def name: String = "UInt64Type"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UnsignedInteger64)
    override def node: PType = PUInt64Type()
  }
  case object UIntPtr extends BuiltInTypeTag {
    override def identifier: String = "uintptr"
    override def name: String = "UIntPtr"
    override def ghost: Boolean = false
    override def typ: Type = IntT(TypeBounds.UIntPtr)
    override def node: PType = PUIntPtr()
  }

  case object Float32 extends BuiltInTypeTag {
    override def identifier: String = "float32"
    override def name: String = "Float32"
    override def ghost: Boolean = false
    override def typ: Type = Float32T
    override def node: PType = PFloat32()
  }

  case object Float64 extends BuiltInTypeTag {
    override def identifier: String = "float64"
    override def name: String = "Float64"
    override def ghost: Boolean = false
    override def typ: Type = Float64T
    override def node: PType = PFloat64()
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

  case object InvariantFunctionTag extends BuiltInFunctionTag {
    override def identifier: String = "Invariant"
    override def name: String = "InvariantTag"
    override def ghost: Boolean = true
    override def isPure: Boolean = true
  }

  /** Built-in FPredicate Tags */

  case object PredTrueFPredTag extends BuiltInFPredicateTag {
    override def identifier: String = "PredTrue"
    override def name: String = "PredTrueFPredTag"
    override def isAbstract: Boolean = false
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
    override def isAbstract: Boolean = true
  }

  case object SendChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "SendChannel"
    override def name: String = "SendChannelMPredTag"
    override def isAbstract: Boolean = true
  }

  case object RecvChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "RecvChannel"
    override def name: String = "RecvChannelMPredTag"
    override def isAbstract: Boolean = true
  }

  case object ClosedMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Closed"
    override def name: String = "ClosedMPredTag"
    override def isAbstract: Boolean = true
  }

  case object ClosureDebtMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "ClosureDebt"
    override def name: String = "ClosureDebtMPredTag"
    override def isAbstract: Boolean = true
  }

  case object TokenMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Token"
    override def name: String = "TokenMPredTag"
    override def isAbstract: Boolean = true
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
    // float types
    Float32,
    Float64,
    // functions
    CloseFunctionTag,
    AppendFunctionTag,
    CopyFunctionTag,
    // fpredicates
    PredTrueFPredTag,
    InvariantFunctionTag,
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
