package viper.gobra.translator.util


import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

trait FunctionGenerator[T] extends Generator {

  override def finalize(col: Collector): Unit = generatedMember.foreach(col.addMember(_))

  private var generatedMember: List[vpr.Function] = List.empty
  private var genMap: Map[T, vpr.Function] = Map.empty

  def genFunction(x: T)(ctx: Context): vpr.Function

  def apply(args: Vector[vpr.Exp], x: T)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
    val func = genMap.getOrElse(x, {
      val newFunc = genFunction(x)(ctx)
      genMap += x -> newFunc
      generatedMember ::= newFunc
      newFunc
    })
    vpr.FuncApp(func, args)(pos, info, errT)
  }
}
