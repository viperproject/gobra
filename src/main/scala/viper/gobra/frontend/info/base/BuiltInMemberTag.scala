// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.util.Messaging.{error, noMessages}
import viper.gobra.frontend.info.base.BuiltInMemberTag.GhostBuiltInMember
import viper.gobra.frontend.info.base.Type.{AssertionT, AuxType, AuxTypeLike, BooleanT, ChannelModus, ChannelT, FunctionT, SingleAuxType, Type}

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

  /**
    * this tag is special as it is only used in the desugarer to simplify the creation of a havoc function for
    * the internal representation. Thus, this tag does not occur in the following partial functions.
    */
  case class HavocFunctionTag(returnType: Type) extends BuiltInFunctionTag with GhostBuiltInMember {
    override def identifier: String = "Havoc"
    override def name: String = "HavocFunctionTag"
  }


  /** Built-in FPredicate Tags */



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

  /**
    * this tag is special as it is only used in the desugarer to simplify the creation of a receive method for
    * the internal representation. Thus, this tag does not occur in the following partial functions.
    */
  case object ReceiveMethodTag extends BuiltInMethodTag {
    override def ghost: Boolean = false
    override def identifier: String = "Receive"
    override def name: String = "ReceiveMethodTag"
  }


  /** Built-in MPredicate Tags */

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
    SendGivenPermMethodTag,
    SendGotPermMethodTag,
    RecvGivenPermMethodTag,
    RecvGotPermMethodTag,
    SendChannelMPredTag,
    RecvChannelMPredTag,
    ClosedMPredTag
  )

  def types(tag: BuiltInMemberTag): AuxTypeLike = tag match {
    case t: BuiltInAuxTypeTag => auxTypes(t)
    case t: BuiltInSingleAuxTypeTag => singleAuxTypes(t)
  }

  def auxTypes(tag: BuiltInAuxTypeTag): AuxType = tag match {
    case _ => unknownTagAuxType(tag)
  }

  def singleAuxTypes(tag: BuiltInSingleAuxTypeTag): SingleAuxType = tag match {
    case _: SendPermMethodTag => sendChannelInvariantType
    case RecvGivenPermMethodTag => recvChannelInvariantType(false)
    case RecvGotPermMethodTag => recvChannelInvariantType(true)
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
