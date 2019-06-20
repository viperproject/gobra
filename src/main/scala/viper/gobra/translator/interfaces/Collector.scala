package viper.gobra.translator.interfaces
import viper.gobra.reporting.BackTranslator
import viper.silver.{ast => vpr}

trait Collector {
  def addMember(m: vpr.Member)
  def addErrorT(errT: BackTranslator.ErrorTransformer)
  def addReasonT(resT: BackTranslator.ReasonTransformer)

  def domains: Seq[vpr.Domain]
  def fields: Seq[vpr.Field]
  def predicate: Seq[vpr.Predicate]
  def functions: Seq[vpr.Function]
  def methods: Seq[vpr.Method]

  def errorT: Seq[BackTranslator.ErrorTransformer]
  def reasonT: Seq[BackTranslator.ReasonTransformer]
}
