// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.PExpression
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.base.Type.{AssertionT, AuxType, ChannelModus, ChannelT, FunctionT, IntT, PredT, SingleAuxType, Type, VoidType}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType
import viper.gobra.util.TypeBounds.UnboundedInteger
import viper.gobra.util.Violation


/**
  * Module to add built-in functions, methods, fpredicates, and mpredicates to Gobra.
  * First, a tag representing such a built-in member has to be declared.
  * By adding it to the `builtInMembers()` functions, it will be considered during type-checking.
  * In addition, `types(...)`, `argGhostTyping(...)`, and `returnGhostTyping(...)` functions have to be adapted
  * accordingly to return the correct type and the ghost typing for its parameters and return parameters, respectively.
  * Last but not least, the newly added member needs to be considered during encoding. The desugarer automatically
  * resolves function and methods calls to as well as instances of these built-in members. However, an additional
  * case has to be added to BuiltInMembersImpl that maps a built-in member (the tag and its specific use) to a generated
  * member in the interal representation which is then encoded.
  */
object BuiltInMemberTag {
  sealed trait BuiltInMemberTag {
    /** identifier of this member as it appears in the program text */
    def identifier: String
    /** debug name for printing */
    def name: String
    def ghost: Boolean
  }

  sealed trait ActualBuiltInMember extends BuiltInMemberTag

  sealed trait GhostBuiltInMember extends BuiltInMemberTag {
    override def ghost: Boolean = true
  }

  sealed trait BuiltInAuxTypeTag extends BuiltInMemberTag
  sealed trait BuiltInSingleAuxTypeTag extends BuiltInMemberTag

  sealed trait BuiltInFunctionTag extends BuiltInAuxTypeTag {
    def isPure: Boolean
  }
  sealed trait BuiltInFPredicateTag extends BuiltInAuxTypeTag with GhostBuiltInMember
  sealed trait BuiltInMethodTag extends BuiltInSingleAuxTypeTag {
    def isPure: Boolean
  }
  sealed trait BuiltInMPredicateTag extends BuiltInSingleAuxTypeTag with GhostBuiltInMember


  /** Built-in Function Tags */

  case object CloseFunctionTag extends BuiltInFunctionTag {
    override def ghost: Boolean = false
    override def identifier: String = "close"
    override def name: String = "CloseFunctionTag"
    override def isPure: Boolean = false
  }


  /** Built-in FPredicate Tags */

  case object PredTrueFPredTag extends BuiltInFPredicateTag {
    override def identifier: String = "PredTrue"
    override def name: String = "PredTrueFPredTag"
  }


  /** Built-in Method Tags */
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
    // functions
    CloseFunctionTag,
    // fpredicates
    PredTrueFPredTag,
    // methods
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

  def types(tag: BuiltInAuxTypeTag)(config: Config): AuxType = tag match {
    // functions
    case CloseFunctionTag => AuxType(
      {
        case (_, Vector(c: ChannelT, IntT(UnboundedInteger), IntT(UnboundedInteger)/* PermissionT */, PredT(Vector()))) if sendAndBiDirections.contains(c.mod) => noMessages
        case (n, ts) => error(n, s"type error: close expects parameters of bidirectional or sending channel, int, int, and pred() types but got ${ts.mkString(", ")}")
      },
      {
        case ts@Vector(c: ChannelT, IntT(UnboundedInteger), IntT(UnboundedInteger)/* PermissionT */, PredT(Vector())) if sendAndBiDirections.contains(c.mod) => FunctionT(ts, VoidType)
      })
    // fpredicates
    case PredTrueFPredTag => AuxType(
      {
        case (_, _) => noMessages // it is well-defined for arbitrary arguments
      },
      {
        case args => FunctionT(args, AssertionT)
      })
    case _ => unknownTagAuxType(tag)
  }

  def types(tag: BuiltInSingleAuxTypeTag)(config: Config): SingleAuxType = tag match {
    // methods
    case SendGivenPermMethodTag => channelReceiverType(sendAndBiDirections, c => FunctionT(Vector(), PredT(Vector(c.elem))))
    case SendGotPermMethodTag => channelReceiverType(sendAndBiDirections, c => FunctionT(Vector(), PredT(Vector()))) // we restrict it to pred()
    case RecvGivenPermMethodTag => channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), PredT(Vector())))
    case RecvGotPermMethodTag => channelReceiverType(recvAndBiDirections, c => FunctionT(Vector(), PredT(Vector(c.elem))))
    case InitChannelMethodTag => channelReceiverType(allDirections, c => {
      val bufferSizeArgType = IntT(config.typeBounds.Int)
      val sendGivenPermArgType = PredT(Vector(c.elem))
      val sendGotPermArgType = PredT(Vector()) // pred() because we enforce that sendGotPermArgType == recvGivenPermArgType
      val recvGivenPermArgType = PredT(Vector())
      val recvGotPermArgType = PredT(Vector(c.elem))
      FunctionT(Vector(bufferSizeArgType, sendGivenPermArgType, sendGotPermArgType, recvGivenPermArgType, recvGotPermArgType), VoidType)
    })
    case CreateDebtChannelMethodTag => channelReceiverType(allDirections, _ => {
      val dividend = IntT(UnboundedInteger)
      val divisor = IntT(UnboundedInteger)
      // val amountArgType = PermissionT
      val predArgType = PredT(Vector())
      FunctionT(Vector(dividend, divisor /* amountArgType */, predArgType), VoidType)
    })
    case RedeemChannelMethodTag => channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      FunctionT(Vector(predArgType), VoidType)
    })

    // mpredicates
    case IsChannelMPredTag => channelReceiverType(allDirections, _ => FunctionT(Vector(IntT(config.typeBounds.Int)), AssertionT))
    case SendChannelMPredTag => channelReceiverType(sendAndBiDirections, _ => FunctionT(Vector(), AssertionT))
    case RecvChannelMPredTag => channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), AssertionT))
    case ClosedMPredTag => channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), AssertionT))
    case ClosureDebtMPredTag => channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      val dividend = IntT(UnboundedInteger)
      val divisor = IntT(UnboundedInteger)
      // val amountArgType = PermissionT
      FunctionT(Vector(predArgType, dividend, divisor /* amountArgType */), AssertionT)
    })
    case TokenMPredTag => channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      FunctionT(Vector(predArgType), AssertionT)
    })
    case _ => unknownTagSingleAuxType(tag)
  }

  private def unknownTagAuxType(tag: BuiltInAuxTypeTag): AuxType = AuxType(
    {
      case (n, _) => error(n, s"type error: unsupported built-in member ${tag.identifier} (tag: ${tag.name})")
    },
    PartialFunction.empty)
  private def unknownTagSingleAuxType(tag: BuiltInSingleAuxTypeTag): SingleAuxType = SingleAuxType(
    {
      case (n, _) => error(n, s"type error: unsupported built-in member ${tag.identifier} (tag: ${tag.name})")
    },
    PartialFunction.empty)

  private lazy val allDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Send, ChannelModus.Recv)
  private lazy val sendAndBiDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Send)
  private lazy val recvAndBiDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Recv)

  /**
    * Simplifies creation of SingleAuxType specialized to a channel being the (method or mpredicate) receiver
    */
  private def channelReceiverType(permittedModi: Set[ChannelModus], typing: ChannelT => FunctionT): SingleAuxType = SingleAuxType(
    channelReceiverMessages(permittedModi),
    channelReceiverTyping(permittedModi, typing)
  )
  private def channelReceiverMessages(permittedModi: Set[ChannelModus]): (PExpression, Type) => Messages =
    {
      case (_, c: ChannelT) if permittedModi.contains(c.mod) => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of channel type (permitted channel modi: $permittedModi) but got $ts")
    }
  private def channelReceiverTyping(permittedModi: Set[ChannelModus], typing: ChannelT => FunctionT): Type ==> FunctionT =
    {
      case c: ChannelT if permittedModi.contains(c.mod) => typing(c)
    }

  /**
    * Returns ghost typing for arguments
    */
  def argGhostTyping(tag: BuiltInAuxTypeTag, args: Vector[Type])(config: Config): GhostType = (tag, args) match {
    // functions
    case (CloseFunctionTag, _) => GhostType.ghostTuple(Vector(false, true, true /* true */, true))
    // fpredicates

    // fallback:
    case (t, args) if t.ghost && types(tag)(config).typing.isDefinedAt(args) => ghostArgs(types(tag)(config).typing(args).args.length)
    case t => Violation.violation(s"argGhostTyping not defined for $t")
  }

  /**
    * Returns ghost typing for arguments
    */
  def argGhostTyping(tag: BuiltInSingleAuxTypeTag, recv: Type)(config: Config): GhostType = (tag, recv) match {
    // methods
    // mpredicates

    // fallback:
    case (t, r) if t.ghost && types(tag)(config).typing.isDefinedAt(r) => ghostArgs(types(tag)(config).typing(r).args.length)
    case t => Violation.violation(s"argGhostTyping not defined for $t")
  }

  private def ghostArgs(arity: Int): GhostType = GhostType.ghostTuple(Vector.fill(arity)(true))

  /**
    * Returns ghost typing for return parameters
    */
  def returnGhostTyping(tag: BuiltInAuxTypeTag, args: Vector[Type])(config: Config): GhostType = (tag, args) match {
    // functions
    // fpredicates

    // fallback:
    case (t, args) if t.ghost && types(tag)(config).typing.isDefinedAt(args) =>
      types(tag)(config).typing(args).result match {
        case VoidType => ghostArgs(0)
        case _ => ghostArgs(1) // multi return parameters are not used yet by any built-in member
      }
    case t => Violation.violation(s"returnGhostTyping not defined for $t")
  }

  /**
    * Returns ghost typing for return parameters
    */
  def returnGhostTyping(tag: BuiltInSingleAuxTypeTag, recv: Type)(config: Config): GhostType = (tag, recv) match {
    // methods
    // mpredicates

    // fallback:
    case (t, r) if t.ghost && types(tag)(config).typing.isDefinedAt(r) =>
      types(tag)(config).typing(r).result match {
        case VoidType => ghostArgs(0)
        case _ => ghostArgs(1) // multi return parameters are not used yet by any built-in member
      }
    case t => Violation.violation(s"returnGhostTyping not defined for $t")
  }
}
