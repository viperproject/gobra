package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Predicates
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class PredicatesImpl extends Predicates {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  /**
    * Finalizes translation. May add to collector.
    */
  override def finalize(col: Collector): Unit = ()

  override def mpredicate(pred: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = withDeepInfo(pred){

    val (vRecv, recvWells) = ctx.loc.parameter(pred.receiver)(ctx)
    val recvWell = cl.assertUnit(recvWells)

    val (vArgss, argWells) = pred.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = sequence(argWells map cl.assertUnit).map(vu.bigAnd)

    val body = option(pred.body map {b =>
      for {
        rwc <- recvWell
        awc <- argWell
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vu.bigAnd(Vector(rwc, awc, vBody))
    })

    for {
      vBody <- body

      predicate = vpr.Predicate(
        name = pred.name.uniqueName,
        formalArgs = vRecv ++ vArgs,
        body = vBody
      )()
    } yield predicate
  }


  override def fpredicate(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = withDeepInfo(pred){

    val (vArgss, argWells) = pred.args.map(ctx.loc.parameter(_)(ctx)).unzip
    val vArgs = vArgss.flatten
    val argWell = sequence(argWells map cl.assertUnit).map(vu.bigAnd)

    val body = option(pred.body map {b =>
      for {
        wc <- argWell
        vBody <- ctx.ass.postcondition(b)(ctx)
      } yield vpr.And(wc, vBody)()
    })

    for {
      vBody <- body

      predicate = vpr.Predicate(
        name = pred.name.name,
        formalArgs = vArgs,
        body = vBody
      )()
    } yield predicate
  }
}
