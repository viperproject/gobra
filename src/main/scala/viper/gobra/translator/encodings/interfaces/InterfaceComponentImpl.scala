package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

class InterfaceComponentImpl extends InterfaceComponent{

  def typ(polyType: vpr.Type, dynTypeType: vpr.Type)(ctx: Context): vpr.Type = ctx.tuple.typ(Vector(polyType, dynTypeType))

  def create(polyVal: vpr.Exp, dynType: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    ctx.tuple.create(Vector(polyVal, dynType))(pos, info, errT)

  def dynTypeOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    ctx.tuple.get(itf, 1, 2)(pos, info, errT)

  def polyValOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp =
    ctx.tuple.get(itf, 0, 2)(pos, info, errT)
}
