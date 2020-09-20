package viper.gobra.translator.util

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait DomainGenerator[T] extends Generator {

  override def finalize(col: Collector): Unit = generatedMember.foreach(col.addMember(_))

  private var generatedMember: List[vpr.Domain] = List.empty
  private var genMap: Map[T, vpr.Domain] = Map.empty

  def genDomain(x: T)(ctx: Context): vpr.Domain

  def apply(args: Vector[vpr.Type], x: T)(ctx: Context): vpr.DomainType = {
    val domain = genMap.getOrElse(x, {
      val newDomain = genDomain(x)(ctx)
      genMap += x -> newDomain
      generatedMember ::= newDomain
      newDomain
    })
    vpr.DomainType(domain, domain.typVars.zip(args).toMap)
  }
}
