package viper.gobra.translator.encodings.channels

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

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
    */
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Channel(t) / Exclusive =>
        unit(withSrc(vpr.IntLit(0), exp))

      case (lit: in.NilLit) :: ctx.Channel(t) =>
        unit(withSrc(vpr.IntLit(0), lit))
    }
  }

  /**
    * Encodes allocation of a new channel.
    *
    * [r := make(chan T, bufferSize)] ->
    *   assert 0 <= [bufferSize]
    *   var a [ chan T ]
    *   inhale [a].isChannel([bufferSize])
    *   r := a
    *
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
            vprIsBufferSizePositive = vpr.GeCmp(vpr.IntLit(0)(pos, info, errT), vprBufferSize)(pos, info, errT)
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
    }
  }
}
