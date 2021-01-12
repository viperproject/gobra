// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.base.Type.{AssertionT, AuxType, AuxTypeLike, BooleanT, ChannelModus, ChannelT, FunctionT, IntT, SingleAuxType}

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
    override def identifier: String = "Pred_True"
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


  /**
    * Returns a vector of tags belonging to built-in members that should be considered during name resolution
    */
  def builtInMembers(): Vector[BuiltInMemberTag] = Vector(
    // functions
    // fpredicates
    PredTrueFPredTag,
    // methods
    SendGivenPermMethodTag,
    SendGotPermMethodTag,
    RecvGivenPermMethodTag,
    RecvGotPermMethodTag,
    // mpredicates
    IsChannelMPredTag,
    SendChannelMPredTag,
    RecvChannelMPredTag,
    ClosedMPredTag
  )

  def types(tag: BuiltInMemberTag)(config: Config): AuxTypeLike = tag match {
    case t: BuiltInAuxTypeTag => auxTypes(t)(config)
    case t: BuiltInSingleAuxTypeTag => singleAuxTypes(t)(config)
  }

  def auxTypes(tag: BuiltInAuxTypeTag)(config: Config): AuxType = tag match {
    // functions
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
    case _: SendPermMethodTag => sendChannelInvariantType
    case RecvGivenPermMethodTag => recvChannelInvariantType(false)
    case RecvGotPermMethodTag => recvChannelInvariantType(true)
    case InitChannelMethodTag => SingleAuxType(
      {
        case (_, _: ChannelT) => noMessages
        case (n, ts) => error(n, s"type error: expected an argument of channel type but got $ts")
      },
      {
        case _: ChannelT =>
          val bufferSizeArgType = IntT(config.typeBounds.Int)
          val sendGivenPermArgType = BooleanT // TODO pred(T)
          val sendGotPermArgType = BooleanT // TODO pred() because we enforce that sendGotPermArgType == recvGivenPermArgType
          val recvGivenPermArgType = BooleanT // TODO pred()
          val recvGotPermArgType = BooleanT // TODO pred(T)
          FunctionT(Vector(bufferSizeArgType, sendGivenPermArgType, sendGotPermArgType, recvGivenPermArgType, recvGotPermArgType), AssertionT)
      })
    // mpredicates
    case IsChannelMPredTag => SingleAuxType(
      {
        case (_, _: ChannelT) => noMessages
        case (n, ts) => error(n, s"type error: expected an argument of channel type but got $ts")
      },
      {
        case _: ChannelT => FunctionT(Vector(IntT(config.typeBounds.Int)), AssertionT)
      })
    case SendChannelMPredTag => sendAndBiChannelType
    case RecvChannelMPredTag => recvAndBiChannelType
    case ClosedMPredTag => recvAndBiChannelType
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

  private lazy val sendChannelInvariantType: SingleAuxType = SingleAuxType(
    {
      case (_, c: ChannelT) if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Send => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of bidirectional or sending channel type but got $ts")
    },
    {
      case c: ChannelT if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Send => FunctionT(Vector(), BooleanT) // TODO pred(T)
    })
  private def recvChannelInvariantType(messageDependant: Boolean): SingleAuxType = SingleAuxType(
    {
      case (_, c: ChannelT) if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Recv => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of bidirectional or receiving channel type but got $ts")
    },
    {
      case c: ChannelT if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Recv => FunctionT(Vector(), BooleanT) // TODO messageDependant ? pred(T) : pred()
    })

  private lazy val sendAndBiChannelType: SingleAuxType = SingleAuxType(
    {
      case (_, c: ChannelT) if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Send => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of bidirectional or sending channel type but got $ts")
    },
    {
      case c: ChannelT if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Send => FunctionT(Vector(), AssertionT)
    })
  private lazy val recvAndBiChannelType: SingleAuxType = SingleAuxType(
    {
      case (_, c: ChannelT) if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Recv => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of bidirectional or receiving channel type but got $ts")
    },
    {
      case c: ChannelT if c.mod == ChannelModus.Bi || c.mod == ChannelModus.Recv => FunctionT(Vector(), AssertionT)
    })
}
