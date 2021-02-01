// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.PNode
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.base.Type.{AbstractType, AssertionT, ChannelModus, ChannelT, FunctionT, IntT, PredT, Type, VoidType}
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostType
import viper.gobra.util.TypeBounds.UnboundedInteger
import viper.gobra.util.Violation


/**
  * Module to add built-in functions, methods, fpredicates, and mpredicates to Gobra.
  * Two steps have to be performed for adding a new built-in member:
  * (1) add a tag representing that built-in member,
  * (2) add it to `builtInMembers()`, and
  * (3) add an additional case to BuiltInMembersImpl that maps that built-in member (the tag and its specific use) to
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

    /** abstract type for this tag */
    def typ(config: Config): AbstractType

    /** ghost typing for arguments */
    def argGhostTyping(args: Vector[Type])(config: Config): GhostType

    /** ghost typing for return values */
    def returnGhostTyping(args: Vector[Type])(config: Config): GhostType
  }

  sealed trait ActualBuiltInMember extends BuiltInMemberTag

  sealed trait GhostBuiltInMember extends BuiltInMemberTag {
    override def ghost: Boolean = true

    override def argGhostTyping(args: Vector[Type])(config: Config): GhostType =
      if (typ(config).typing.isDefinedAt(args)) {
        ghostArgs(typ(config).typing(args).args.length)
      } else {
        Violation.violation(s"argGhostTyping not defined for $name")
      }

    /**
      * Returns 0 ghost results for functions returning `void` and 1 ghost result otherwise.
      * Members having multiple ghost results have to overwrite this function.
      */
    def returnGhostTyping(args: Vector[Type])(config: Config): GhostType =
      if (typ(config).typing.isDefinedAt(args)) {
        typ(config).typing(args).result match {
          case VoidType => ghostArgs(0)
          case _ => ghostArgs(1) // as a default, we pick a single ghost result.
        }
      } else {
        Violation.violation(s"returnGhostTyping not defined for $name")
      }
  }

  sealed trait BuiltInFunctionTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInFPredicateTag extends GhostBuiltInMember
  sealed trait BuiltInMethodTag extends BuiltInMemberTag {
    def isPure: Boolean
  }
  sealed trait BuiltInMPredicateTag extends GhostBuiltInMember


  /** Built-in Function Tags */

  case object CloseFunctionTag extends BuiltInFunctionTag {
    override def identifier: String = "close"
    override def name: String = "CloseFunctionTag"
    override def ghost: Boolean = false
    override def isPure: Boolean = false

    override def typ(config: Config): AbstractType = AbstractType(
      {
        case (_, Vector(c: ChannelT, IntT(UnboundedInteger), IntT(UnboundedInteger)/* PermissionT */, PredT(Vector()))) if sendAndBiDirections.contains(c.mod) => noMessages
        case (n, ts) => error(n, s"type error: close expects parameters of bidirectional or sending channel, int, int, and pred() types but got ${ts.mkString(", ")}")
      },
      {
        case ts@Vector(c: ChannelT, IntT(UnboundedInteger), IntT(UnboundedInteger)/* PermissionT */, PredT(Vector())) if sendAndBiDirections.contains(c.mod) => FunctionT(ts, VoidType)
      })

    override def argGhostTyping(args: Vector[Type])(config: Config): GhostType =
      GhostType.ghostTuple(Vector(false, true, true /* true */, true))

    override def returnGhostTyping(args: Vector[Type])(config: Config): GhostType = ghostArgs(0)
  }


  /** Built-in FPredicate Tags */

  case object PredTrueFPredTag extends BuiltInFPredicateTag {
    override def identifier: String = "PredTrue"
    override def name: String = "PredTrueFPredTag"

    override def typ(config: Config): AbstractType = AbstractType(
      {
        case (_, _) => noMessages // it is well-defined for arbitrary arguments
      },
      {
        case args => FunctionT(args, AssertionT)
      })
  }


  /** Built-in Method Tags */
  case object BufferSizeMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "BufferSize"
    override def name: String = "BufferSizeMethodTag"
    override def isPure: Boolean = true

    override def typ(config: Config): AbstractType =
      channelReceiverType(allDirections, _ => FunctionT(Vector(), IntT(config.typeBounds.Int)))
  }

  sealed trait ChannelInvariantMethodTag extends BuiltInMethodTag with GhostBuiltInMember
  sealed trait SendPermMethodTag extends ChannelInvariantMethodTag
  case object SendGivenPermMethodTag extends SendPermMethodTag {
    override def identifier: String = "SendGivenPerm"
    override def name: String = "SendGivenPermMethodTag"
    override def isPure: Boolean = true

    override def typ(config: Config): AbstractType =
      channelReceiverType(sendAndBiDirections, c => FunctionT(Vector(), PredT(Vector(c.elem))))
  }

  case object SendGotPermMethodTag extends SendPermMethodTag {
    override def identifier: String = "SendGotPerm"
    override def name: String = "SendGotPermMethodTag"
    override def isPure: Boolean = true

    override def typ(config: Config): AbstractType =
      channelReceiverType(sendAndBiDirections, _ => FunctionT(Vector(), PredT(Vector()))) // we restrict it to pred()
  }

  sealed trait RecvPermMethodTag extends ChannelInvariantMethodTag
  case object RecvGivenPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGivenPerm"
    override def name: String = "RecvGivenPermMethodTag"
    override def isPure: Boolean = true

    override def typ(config: Config): AbstractType =
      channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), PredT(Vector())))
  }

  case object RecvGotPermMethodTag extends RecvPermMethodTag {
    override def identifier: String = "RecvGotPerm"
    override def name: String = "RecvGotPermMethodTag"
    override def isPure: Boolean = true

    override def typ(config: Config): AbstractType =
      channelReceiverType(recvAndBiDirections, c => FunctionT(Vector(), PredT(Vector(c.elem))))
  }

  case object InitChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Init"
    override def name: String = "InitChannelMethodTag"
    override def isPure: Boolean = false

    override def typ(config: Config): AbstractType = channelReceiverType(allDirections, c => {
      // init's signature is adapted to the heavy simplifications that are in place for the initial support for channels.
      // in particular, the permission for SendGivenPerm and RecvGotPerm as well as SendGotPerm and RecvGivenPerm have
      // to be equal. Thus, they get merged into two parameters: The former pair is called `proPerm` as they describe
      // the invariant that is exhaled at the sender's and inhaled at the receiver's side ("pro" because it "travels" in
      // direction of the send operation). The latter pair is merged to `contraPerm` representing the invariant that
      // "travels" in the opposite direction.
      val proPermArgType = PredT(Vector(c.elem))
      val contraPermArgType = PredT(Vector()) // pred() because we enforce that sendGotPermArgType == recvGivenPermArgType
      FunctionT(Vector(proPermArgType, contraPermArgType), VoidType)
    })
  }

  case object CreateDebtChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "CreateDebt"
    override def name: String = "CreateDebtChannelMethodTag"
    override def isPure: Boolean = false

    override def typ(config: Config): AbstractType = channelReceiverType(allDirections, _ => {
      val dividend = IntT(UnboundedInteger)
      val divisor = IntT(UnboundedInteger)
      // val amountArgType = PermissionT
      val predArgType = PredT(Vector())
      FunctionT(Vector(dividend, divisor /* amountArgType */, predArgType), VoidType)
    })
  }

  case object RedeemChannelMethodTag extends BuiltInMethodTag with GhostBuiltInMember {
    override def identifier: String = "Redeem"
    override def name: String = "RedeemChannelMethodTag"
    override def isPure: Boolean = false

    override def typ(config: Config): AbstractType = channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      FunctionT(Vector(predArgType), VoidType)
    })
  }


  /** Built-in MPredicate Tags */

  case object IsChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "IsChannel"
    override def name: String = "IsChannelMPredTag"

    override def typ(config: Config): AbstractType =
      channelReceiverType(allDirections, _ => FunctionT(Vector(), AssertionT))
  }

  case object SendChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "SendChannel"
    override def name: String = "SendChannelMPredTag"

    override def typ(config: Config): AbstractType =
      channelReceiverType(sendAndBiDirections, _ => FunctionT(Vector(), AssertionT))
  }

  case object RecvChannelMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "RecvChannel"
    override def name: String = "RecvChannelMPredTag"

    override def typ(config: Config): AbstractType =
      channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), AssertionT))
  }

  case object ClosedMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Closed"
    override def name: String = "ClosedMPredTag"

    override def typ(config: Config): AbstractType =
      channelReceiverType(recvAndBiDirections, _ => FunctionT(Vector(), AssertionT))
  }

  case object ClosureDebtMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "ClosureDebt"
    override def name: String = "ClosureDebtMPredTag"

    override def typ(config: Config): AbstractType = channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      val dividend = IntT(UnboundedInteger)
      val divisor = IntT(UnboundedInteger)
      // val amountArgType = PermissionT
      FunctionT(Vector(predArgType, dividend, divisor /* amountArgType */), AssertionT)
    })
  }

  case object TokenMPredTag extends BuiltInMPredicateTag {
    override def identifier: String = "Token"
    override def name: String = "TokenMPredTag"

    override def typ(config: Config): AbstractType = channelReceiverType(allDirections, _ => {
      val predArgType = PredT(Vector())
      FunctionT(Vector(predArgType), AssertionT)
    })
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


  private lazy val allDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Send, ChannelModus.Recv)
  private lazy val sendAndBiDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Send)
  private lazy val recvAndBiDirections: Set[ChannelModus] = Set(ChannelModus.Bi, ChannelModus.Recv)

  /**
    * Simplifies creation of an AbstracType specialized to a channel being the (method or mpredicate) receiver
    */
  private def channelReceiverType(permittedModi: Set[ChannelModus], typing: ChannelT => FunctionT): AbstractType = AbstractType(
    channelReceiverMessages(permittedModi),
    channelReceiverTyping(permittedModi, typing)
  )
  private def channelReceiverMessages(permittedModi: Set[ChannelModus]): (PNode, Vector[Type]) => Messages =
    {
      case (_, Vector(c: ChannelT)) if permittedModi.contains(c.mod) => noMessages
      case (n, ts) => error(n, s"type error: expected a single argument of channel type (permitted channel modi: $permittedModi) but got $ts")
    }
  private def channelReceiverTyping(permittedModi: Set[ChannelModus], typing: ChannelT => FunctionT): Vector[Type] ==> FunctionT =
    {
      case Vector(c: ChannelT) if permittedModi.contains(c.mod) => typing(c)
    }

  private def ghostArgs(arity: Int): GhostType = GhostType.ghostTuple(Vector.fill(arity)(true))
}
