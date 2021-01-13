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
import viper.gobra.frontend.info.base.Type.{AssertionT, AuxType, AuxTypeLike, ChannelModus, ChannelT, FunctionT, IntT, PermissionT, PredT, SingleAuxType, Type, VoidType}
import viper.gobra.util.Violation


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

  sealed trait BuiltInFunctionTag extends BuiltInAuxTypeTag
  sealed trait BuiltInFPredicateTag extends BuiltInAuxTypeTag with GhostBuiltInMember
  sealed trait BuiltInMethodTag extends BuiltInSingleAuxTypeTag
  sealed trait BuiltInMPredicateTag extends BuiltInSingleAuxTypeTag with GhostBuiltInMember


  /** Built-in Function Tags */

  case object CloseFunctionTag extends BuiltInFunctionTag {
    override def ghost: Boolean = false
    override def identifier: String = "close"
    override def name: String = "CloseFunctionTag"
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
  }

  case object SendGotPermMethodTag extends SendPermMethodTag {
    override def identifier: String = "SendGotPerm"
    override def name: String = "SendGotPermMethodTag"
  }

  sealed trait RecvPermMethodTag extends ChannelInvariantMethodTag
  case object RecvGivenPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGivenPerm"
    override def name: String = "RecvGivenPermMethodTag"
  }

  case object RecvGotPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGotPerm"
    override def name: String = "RecvGotPermMethodTag"
  }

  case object InitChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Init"
    override def name: String = "InitChannelMethodTag"
  }

  case object CreateDebtChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "CreateDebt"
    override def name: String = "CreateDebtChannelMethodTag"
  }

  case object RedeemChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Redeem"
    override def name: String = "RedeemChannelMethodTag"
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

  def types(tag: BuiltInMemberTag)(config: Config): AuxTypeLike = tag match {
    case t: BuiltInAuxTypeTag => auxTypes(t)(config)
    case t: BuiltInSingleAuxTypeTag => singleAuxTypes(t)(config)
  }

  def auxTypes(tag: BuiltInAuxTypeTag)(config: Config): AuxType = tag match {
    // functions
    case CloseFunctionTag => AuxType(
      {
        case (_, Vector(c: ChannelT, PermissionT, PredT(Vector()))) if sendAndBiDirections.contains(c.mod) => noMessages
        case (n, ts) => error(n, s"type error: close expects parameters of bidirectional or sending channel, pred, and pred() types but got $ts")
      },
      {
        case ts@Vector(c: ChannelT, PermissionT, PredT(Vector())) if sendAndBiDirections.contains(c.mod) => FunctionT(ts, VoidType)
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

  def singleAuxTypes(tag: BuiltInSingleAuxTypeTag)(config: Config): SingleAuxType = tag match {
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
      val amountArgType = PermissionT
      val predArgType = PredT(Vector())
      FunctionT(Vector(amountArgType, predArgType), VoidType)
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
      val amountArgType = PermissionT
      FunctionT(Vector(predArgType, amountArgType), AssertionT)
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
    * Returns a vector of flags indicating whether an argument is ghost
    */
  def ghostArgs(tag: BuiltInAuxTypeTag): Vector[Boolean] = tag match {
    // functions
    case CloseFunctionTag => Vector(false, true, true)
    // fpredicates
    case PredTrueFPredTag => Vector() // TODO this stops us from having PredTrue with arbitrary (number of) arguments
    case t => Violation.violation(s"ghost type not defined for $t")
  }

  /**
    * Returns a vector of flags indicating whether an argument is ghost
    */
  def ghostArgs(tag: BuiltInSingleAuxTypeTag, recv: Type): Vector[Boolean] = (tag, recv) match {
    // methods
    case (SendGivenPermMethodTag, _) => Vector()
    case (SendGotPermMethodTag, _) => Vector()
    case (RecvGivenPermMethodTag, _) => Vector()
    case (RecvGotPermMethodTag, _) => Vector()
    case (InitChannelMethodTag, _) => Vector(true, true, true, true, true)
    case (CreateDebtChannelMethodTag, _) => Vector(true, true)
    case (RedeemChannelMethodTag, _) => Vector(true)
    // mpredicates
    case (IsChannelMPredTag, _) => Vector(true)
    case (SendChannelMPredTag, _) => Vector()
    case (RecvChannelMPredTag, _) => Vector()
    case (ClosedMPredTag, _) => Vector()
    case (ClosureDebtMPredTag, _) => Vector(true, true)
    case (TokenMPredTag, _) => Vector(true)
    case t => Violation.violation(s"ghost type not defined for $t")
  }
}
