// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.channels

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{InsufficientPermissionError, PreconditionError, Source}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.silver.verifier.{errors => vprerr, reasons => vprrea}

class ChannelEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Channel(t) / m =>  m match {
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
    *   res
    */
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Channel(t) / Exclusive =>
        unit(withSrc(vpr.IntLit(0), exp))

      case (lit: in.NilLit) :: ctx.Channel(t) =>
        unit(withSrc(vpr.IntLit(0), lit))

      case exp@in.Receive(channel :: ctx.Channel(typeParam), recvChannel, recvGivenPerm) =>
        val (pos, info, errT) = exp.vprMeta
        val res = in.LocalVar(Names.freshName, typeParam.withAddressability(Addressability.Exclusive))(exp.info)
        val vprRes = ctx.typeEncoding.variable(ctx)(res)
        val recvChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, recvChannel, Vector())(exp.info))
        val recvChannelWildcard = in.Access(recvChannelPred, in.WildcardPerm(exp.info))(exp.info)
        for {
          // exhale acc([c].RecvChannel(), wildcard)
          vprRecvChannelWildcard <- ctx.ass.translate(recvChannelWildcard)(ctx)
          vprExhaleRecvChannelWildcard = vpr.Exhale(vprRecvChannelWildcard)(pos, info, errT)
          _ <- write(vprExhaleRecvChannelWildcard)

          // exhale [c].RecvGivenPerm()()
          recvGivenPermInst = getChannelInvariantAccess(channel, recvGivenPerm, Vector())(exp.info)
          vprRecvGivenPermInst <- ctx.ass.translate(recvGivenPermInst)(ctx)
          vprExhaleRecvGivenPermInst = vpr.Exhale(vprRecvGivenPermInst)(pos, info, errT)
          _ <- write(vprExhaleRecvGivenPermInst)

          // var res [ T ]
          _ <- local(vprRes)

          // inhale [c].RecvChannel()
          recvChannelFull = in.Access(recvChannelPred, in.FullPerm(exp.info))(exp.info)
          vprRecvChannelFull <- ctx.ass.translate(recvChannelFull)(ctx)
          vprInhaleRecvChannelFull = vpr.Inhale(vprRecvChannelFull)(pos, info, errT)
          _ <- write(vprInhaleRecvChannelFull)

          // res
          vprResRead <- ctx.typeEncoding.expr(ctx)(res)
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
    *   inhale [a].isChannel([bufferSize])
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
      case makeStmt@in.MakeChannel(target, in.ChannelT(typeParam, _), optBufferSizeArg, isChannelPred) =>
        val (pos, info, errT) = makeStmt.vprMeta
        val a = in.LocalVar(Names.freshName, in.ChannelT(typeParam.withAddressability(Addressability.channelElement), Addressability.Exclusive))(makeStmt.info)
        val vprA = ctx.typeEncoding.variable(ctx)(a)
        val bufferSizeArg = optBufferSizeArg.getOrElse(in.IntLit(0)(makeStmt.info)) // create an unbuffered channel by default
        seqn(
          for {
            // var a [ []T ]
            _ <- local(vprA)

            vprBufferSize <- ctx.expr.translate(bufferSizeArg)(ctx)

            // assert 0 <= [bufferSize]
            vprIsBufferSizePositive = vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), vprBufferSize)(pos, info, errT)
            vprAssert = vpr.Assert(vprIsBufferSizePositive)(pos, info, errT)
            _ <- write(vprAssert)

            // inhale [a].isChannel([bufferSize])
            isChannelInst = in.Access(
              in.Accessible.Predicate(in.MPredicateAccess(a, isChannelPred, Vector(bufferSizeArg))(makeStmt.info)),
              in.FullPerm(makeStmt.info)
            )(makeStmt.info)
            vprIsChannelInst <- ctx.ass.translate(isChannelInst)(ctx)
            vprInhale = vpr.Inhale(vprIsChannelInst)(pos, info, errT)
            _ <- write(vprInhale)

            // r := a
            ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), a, makeStmt)
          } yield ass
        )

      case stmt@in.Send(channel :: ctx.Channel(typeParam), message, sendChannel, sendGivenPerm, sendGotPerm) if message.typ == typeParam =>
        val (pos, info, errT) = stmt.vprMeta
        val sendChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, sendChannel, Vector())(stmt.info))
        val sendChannelWildcard = in.Access(sendChannelPred, in.WildcardPerm(stmt.info))(stmt.info)
        seqn(
          for {
            // assert acc(SendChannel([c], wildcard)
            vprSendChannelWildcard <- ctx.ass.translate(sendChannelWildcard)(ctx)
            vprAssertSendChannelWildcard = vpr.Assert(vprSendChannelWildcard)(pos, info, errT)
            _ <- write(vprAssertSendChannelWildcard)

            // exhale [c].SendGivenPerm()([m])
            sendGivenPermInst = getChannelInvariantAccess(channel, sendGivenPerm, Vector(message))(stmt.info)
            vprSendGivenPermInst <- ctx.ass.translate(sendGivenPermInst)(ctx)
            vprExhaleSendGivenPermInst = vpr.Exhale(vprSendGivenPermInst)(pos, info, errT)
            _ <- write(vprExhaleSendGivenPermInst)

            // inhale [c].SendGotPerm()()
            sendGotPermInst = getChannelInvariantAccess(channel, sendGotPerm, Vector())(stmt.info)
            vprSendGotPermInst <- ctx.ass.translate(sendGotPermInst)(ctx)
            vprInhaleSendGotPermInst = vpr.Inhale(vprSendGotPermInst)(pos, info, errT)
          } yield vprInhaleSendGotPermInst
        )

      case stmt@in.SafeReceive(resTarget, successTarget, channel :: ctx.Channel(typeParam), recvChannel, recvGivenPerm, recvGotPerm, closed) =>
        val (pos, info, errT) = stmt.vprMeta
        val res = in.LocalVar(Names.freshName, typeParam.withAddressability(Addressability.Exclusive))(stmt.info)
        val vprRes = ctx.typeEncoding.variable(ctx)(res)
        val ok = in.LocalVar(Names.freshName, in.BoolT(Addressability.Exclusive))(stmt.info)
        val vprOk = ctx.typeEncoding.variable(ctx)(ok)
        val recvChannelPred = in.Accessible.Predicate(in.MPredicateAccess(channel, recvChannel, Vector())(stmt.info))
        val recvChannelWildcard = in.Access(recvChannelPred, in.WildcardPerm(stmt.info))(stmt.info)
        seqn(
          for {
            // exhale acc([c].RecvChannel(), wildcard)
            vprRecvChannelWildcard <- ctx.ass.translate(recvChannelWildcard)(ctx)
            vprExhaleRecvChannelWildcard = vpr.Exhale(vprRecvChannelWildcard)(pos, info, errT)
             _ <- write(vprExhaleRecvChannelWildcard)
            _ <- errorT {
              case e@vprerr.ExhaleFailed(Source(info), _, _) if e causedBy vprExhaleRecvChannelWildcard =>
                // TODO: receiver name should correspond to the receiver in the original Gobra program and not in the
                // internal representation
                PreconditionError(info) dueTo InsufficientPermissionError(info, recvChannelWildcard.toString)
            }

            // exhale [c].RecvGivenPerm()()
            recvGivenPermInst = getChannelInvariantAccess(channel, recvGivenPerm, Vector())(stmt.info)
            vprRecvGivenPermInst <- ctx.ass.translate(recvGivenPermInst)(ctx)
            vprExhaleRecvGivenPermInst = vpr.Exhale(vprRecvGivenPermInst)(pos, info, errT)
            _ <- write(vprExhaleRecvGivenPermInst)

            // var res [ T ]
            _ <- local(vprRes)

            // var ok Bool
            _ <- local(vprOk)

            // inhale [c].RecvChannel()
            recvChannelFull = in.Access(recvChannelPred, in.FullPerm(stmt.info))(stmt.info)
            vprRecvChannelFull <- ctx.ass.translate(recvChannelFull)(ctx)
            vprInhaleRecvChannelFull = vpr.Inhale(vprRecvChannelFull)(pos, info, errT)
            _ <- write(vprInhaleRecvChannelFull)

            // inhale ok ==> [c].RecvGotPerm()(res)
            recvGotPermInst = getChannelInvariantAccess(channel, recvGotPerm, Vector(res))(stmt.info)
            okImpl = in.Implication(ok, recvGotPermInst)(stmt.info)
            vprOkImpl <- ctx.ass.translate(okImpl)(ctx)
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
            vprNotOkImpl <- ctx.ass.translate(notOkImpl)(ctx)
            vprInhaleNotOkImpl = vpr.Inhale(vprNotOkImpl)(pos, info, errT)
            _ <- write(vprInhaleNotOkImpl)

            // resTarget := res
            resAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(resTarget), res, stmt)
            _ <- write(resAss)

            // successTarget := ok
            okAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(successTarget), ok, stmt)
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
