// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.channels

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{ChannelMakePreconditionError, ChannelReceiveError, ChannelSendError, InsufficientPermissionFromTagError, Source}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation.violation
import viper.silver.{ast => vpr}

class ChannelEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Channel(_) / m =>  m match {
      case Exclusive => vpr.Int // for now we simply map it to Viper Ints
      case Shared => vpr.Ref
    }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * R[ dflt(channel[T]) ] -> 0
    * R[ nil : channel[T] ] -> 0
    * R[ <- (c: channel[T] ] ->
    *   exhale acc([c].RecvChannel(), wildcard)
    *   exhale [c].RecvGivenPerm()()
    *   var res [ T ]
    *   inhale [c].RecvChannel()
    *   // if a non-zero value is received, this implies that the channel is not closed and thus the invariant holds:
    *   // if a zero value is received, no knowledge about the channel's close state is gained
    *   inhale res != Dflt[T] ==> [c].RecvGotPerm()(res)
    *   res
    */
  override def expression(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    default(super.expression(ctx)) {
      case (exp : in.DfltVal) :: ctx.Channel(_) / Exclusive =>
        unit(withSrc(vpr.IntLit(0), exp))

      case (lit: in.NilLit) :: ctx.Channel(_) =>
        unit(withSrc(vpr.IntLit(0), lit))

      case exp@in.Receive(channel :: ctx.Channel(typeParam), recvChannel, recvGivenPerm, recvGotPerm) =>
        val (pos, info, errT) = exp.vprMeta
        val res = in.LocalVar(ctx.freshNames.next(), typeParam.withAddressability(Addressability.Exclusive))(exp.info)
        val vprRes = ctx.variable(res)
        val recvChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, recvChannel, Vector())(exp.info))
        for {
          // assert acc([c].RecvChannel(), wildcard)
          sendChannelPerm <- ctx.expression(in.CurrentPerm(recvChannelPred)(exp.info))
          _ <- assert(vpr.PermGtCmp(sendChannelPerm, vpr.NoPerm()(pos, info, errT))(pos, info, errT),
            (info, _) => ChannelReceiveError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.RecvChannel()")
          )

          // exhale [c].RecvGivenPerm()()
          recvGivenPermInst = getChannelInvariantAccess(channel, recvGivenPerm, Vector())(exp.info)
          vprRecvGivenPermInst <- ctx.assertion(recvGivenPermInst)
          _ <- exhale(vprRecvGivenPermInst,
            (info, _) => ChannelReceiveError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.RecvGivenPerm()()")
          )

          // var res [ T ]
          _ <- local(vprRes)

          // inhale [c].RecvChannel()
          recvChannelFull = in.Access(recvChannelPred, in.FullPerm(exp.info))(exp.info)
          vprRecvChannelFull <- ctx.assertion(recvChannelFull)
          vprInhaleRecvChannelFull = vpr.Inhale(vprRecvChannelFull)(pos, info, errT)
          _ <- write(vprInhaleRecvChannelFull)

          // inhale res != Dflt[T] ==> [c].RecvGotPerm()(res)
          isNotZero = in.UneqCmp(res, in.DfltVal(res.typ)(exp.info))(exp.info)
          recvGotPermInst = getChannelInvariantAccess(channel, recvGotPerm, Vector(res))(exp.info)
          notZeroImpl = in.Implication(isNotZero, recvGotPermInst)(exp.info)
          vprNotZeroImpl <- ctx.assertion(notZeroImpl)
          vprInhaleNotZeroImpl = vpr.Inhale(vprNotZeroImpl)(pos, info, errT)
          _ <- write(vprInhaleNotZeroImpl)

          // res
          vprResRead <- ctx.expression(res)
        } yield vprResRead
    }
  }

  /**
    * Encodes
    *  - allocation of a new channel
    *  - channel receive operation that returns received message as well as a success flag
    *
    * [r := make(chan T, bufferSize)] ->
    *   assert 0 <= [bufferSize]
    *   var a [ chan T ]
    *   inhale [a].isChannel()
    *   inhale [a].BufferSize() == [bufferSize]
    *   r := a
    *
    * [(c : channel[T] <- (m : T)] ->
    *   assert acc(SendChannel([c], wildcard)
    *   exhale [c].SendGivenPerm()([m])
    *   inhale [c].SendGotPerm()()
    * Note: using an assertion instead of exhale & inhale (to model pre- and postcondition) allows us to
    * assert a wildcard permission amount. Otherwise, a ghost perm argument would be required such that the
    * same permission amount can be exhaled and inhaled.
    *
    * [resTarget, successTarget := <- (c : channel[T])] ->
    *   exhale acc([c].RecvChannel(), wildcard)
    *   exhale [c].RecvGivenPerm()()
    *   var res [ T ]
    *   var ok Bool
    *   inhale [c].RecvChannel()
    *   inhale ok ==> [c].RecvGotPerm()(res)
    *   inhale not(ok) ==> [c].Closed() && res == Dflt[T]
    *   resTarget := res
    *   successTarget := ok
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)){
      case makeStmt@in.MakeChannel(target, in.ChannelT(typeParam, _), optBufferSizeArg, isChannelPred, bufferSizeMProxy) =>
        val (pos, info, errT) = makeStmt.vprMeta
        val a = in.LocalVar(ctx.freshNames.next(), in.ChannelT(typeParam.withAddressability(Addressability.channelElement), Addressability.Exclusive))(makeStmt.info)
        val vprA = ctx.variable(a)
        val bufferSizeArg = optBufferSizeArg.getOrElse(in.IntLit(0)(makeStmt.info)) // create an unbuffered channel by default
        seqn(
          for {
            // var a [ chan T ]
            _ <- local(vprA)

            vprBufferSize <- ctx.expression(bufferSizeArg)

            // assert 0 <= [bufferSize]
            vprIsBufferSizePositive = vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), vprBufferSize)(pos, info, errT)
            _ <- assert(vprIsBufferSizePositive, (info, _) => ChannelMakePreconditionError(info) )

            // inhale [a].isChannel()
            isChannelInst = in.Access(
              in.Accessible.Predicate(in.MPredicateAccess(a, isChannelPred, Vector())(makeStmt.info)),
              in.FullPerm(makeStmt.info)
            )(makeStmt.info)
            vprIsChannelInst <- ctx.assertion(isChannelInst)
            vprIsChannelInhale = vpr.Inhale(vprIsChannelInst)(pos, info, errT)
            _ <- write(vprIsChannelInhale)

            // inhale [a].BufferSize() == [bufferSize]
            bufferSizeCall = in.PureMethodCall(a, bufferSizeMProxy, Vector(), in.IntT(Addressability.outParameter))(makeStmt.info)
            bufferSizeEq = in.EqCmp(bufferSizeCall, bufferSizeArg)(makeStmt.info)
            vprBufferSizeEq <- ctx.expression(bufferSizeEq)
            vprBufferSizeInhale = vpr.Inhale(vprBufferSizeEq)(pos, info, errT)
            _ <- write(vprBufferSizeInhale)

            // r := a
            ass <- ctx.assignment(in.Assignee.Var(target), a)(makeStmt)
          } yield ass
        )

      case stmt@in.Send(channel :: ctx.Channel(typeParam), message, sendChannel, sendGivenPerm, sendGotPerm) =>
        violation(message.typ == typeParam, s"message type ${message.typ} has to be the same as the channel element type $typeParam")
        val (pos, info, errT) = stmt.vprMeta
        val sendChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, sendChannel, Vector())(stmt.info))
        seqn(
          for {
            // assert acc(SendChannel([c], wildcard)
            sendChannelPerm <- ctx.expression(in.CurrentPerm(sendChannelPred)(stmt.info))
            _ <- assert(vpr.PermGtCmp(sendChannelPerm, vpr.NoPerm()(pos, info, errT))(pos, info, errT),
              (info, _) => ChannelSendError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.SendChannel()")
            )

            // exhale [c].SendGivenPerm()([m])
            sendGivenPermInst = getChannelInvariantAccess(channel, sendGivenPerm, Vector(message))(stmt.info)
            vprSendGivenPermInst <- ctx.assertion(sendGivenPermInst)
            _ <- exhale(vprSendGivenPermInst,
              (info, _) => ChannelSendError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.SendGivenPerm()(${message.info.tag})")
            )

            // inhale [c].SendGotPerm()()
            sendGotPermInst = getChannelInvariantAccess(channel, sendGotPerm, Vector())(stmt.info)
            vprSendGotPermInst <- ctx.assertion(sendGotPermInst)
            vprInhaleSendGotPermInst = vpr.Inhale(vprSendGotPermInst)(pos, info, errT)
          } yield vprInhaleSendGotPermInst
        )

      case stmt@in.SafeReceive(resTarget, successTarget, channel :: ctx.Channel(typeParam), recvChannel, recvGivenPerm, recvGotPerm, closed) =>
        val (pos, info, errT) = stmt.vprMeta
        val res = in.LocalVar(ctx.freshNames.next(), typeParam.withAddressability(Addressability.Exclusive))(stmt.info)
        val vprRes = ctx.variable(res)
        val ok = in.LocalVar(ctx.freshNames.next(), in.BoolT(Addressability.Exclusive))(stmt.info)
        val vprOk = ctx.variable(ok)
        val recvChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, recvChannel, Vector())(stmt.info))
        seqn(
          for {
            // assert acc([c].RecvChannel(), wildcard)
            sendChannelPerm <- ctx.expression(in.CurrentPerm(recvChannelPred)(stmt.info))
            _ <- assert(vpr.PermGtCmp(sendChannelPerm, vpr.NoPerm()(pos, info, errT))(pos, info, errT),
              (info, _) => ChannelReceiveError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.RecvChannel()")
            )

            // exhale [c].RecvGivenPerm()()
            recvGivenPermInst = getChannelInvariantAccess(channel, recvGivenPerm, Vector())(stmt.info)
            vprRecvGivenPermInst <- ctx.assertion(recvGivenPermInst)
            _ <- exhale(vprRecvGivenPermInst,
              (info, _) => ChannelReceiveError(info) dueTo InsufficientPermissionFromTagError(s"${channel.info.tag}.RecvGivenPerm()()")
            )

            // var res [ T ]
            _ <- local(vprRes)

            // var ok Bool
            _ <- local(vprOk)

            // inhale [c].RecvChannel()
            recvChannelFull = in.Access(recvChannelPred, in.FullPerm(stmt.info))(stmt.info)
            vprRecvChannelFull <- ctx.assertion(recvChannelFull)
            vprInhaleRecvChannelFull = vpr.Inhale(vprRecvChannelFull)(pos, info, errT)
            _ <- write(vprInhaleRecvChannelFull)

            // inhale ok ==> [c].RecvGotPerm()(res)
            recvGotPermInst = getChannelInvariantAccess(channel, recvGotPerm, Vector(res))(stmt.info)
            okImpl = in.Implication(ok, recvGotPermInst)(stmt.info)
            vprOkImpl <- ctx.assertion(okImpl)
            vprInhaleOkImpl = vpr.Inhale(vprOkImpl)(pos, info, errT)
            _ <- write(vprInhaleOkImpl)

            // inhale not(ok) ==> [c].Closed() && res == Dflt[T]
            closedPred = in.Accessible.Predicate(in.MPredicateAccess(channel, closed, Vector())(stmt.info))
            closedChannelInst = in.Access(closedPred, in.FullPerm(stmt.info))(stmt.info)
            isZero = in.ExprAssertion(in.EqCmp(res, in.DfltVal(res.typ)(stmt.info))(stmt.info))(stmt.info)
            notOkImpl = in.Implication(
              in.Negation(ok)(stmt.info),
              in.SepAnd(closedChannelInst, isZero)(stmt.info)
            )(stmt.info)
            vprNotOkImpl <- ctx.assertion(notOkImpl)
            vprInhaleNotOkImpl = vpr.Inhale(vprNotOkImpl)(pos, info, errT)
            _ <- write(vprInhaleNotOkImpl)

            // resTarget := res
            resAss <- ctx.assignment(in.Assignee.Var(resTarget), res)(stmt)
            _ <- write(resAss)

            // successTarget := ok
            okAss <- ctx.assignment(in.Assignee.Var(successTarget), ok)(stmt)
          } yield okAss
        )
    }
  }

  /**
    * Constructs `[channel].invariant()([args])`
    */
  private def getChannelInvariantAccess(channel: in.Expr, invariant: in.MethodProxy, args: Vector[in.Expr])(src: Source.Parser.Info): in.Access = {
    val permReturnT = in.PredT(args.map(_.typ), Addressability.outParameter)
    val permPred = in.PureMethodCall(channel, invariant, Vector(), permReturnT)(src)
    in.Access(in.Accessible.PredExpr(in.PredExprInstance(permPred, args)(src)), in.FullPerm(src))(src)
  }
}
