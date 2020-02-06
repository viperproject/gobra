package viper.gobra.translator.interfaces
import viper.silver.{ast => vpr}

trait Collector {
  def addMember(m: vpr.Member)

  def domains: Seq[vpr.Domain]
  def fields: Seq[vpr.Field]
  def predicate: Seq[vpr.Predicate]
  def functions: Seq[vpr.Function]
  def methods: Seq[vpr.Method]
  def extensions: Seq[vpr.ExtensionMember]
}
