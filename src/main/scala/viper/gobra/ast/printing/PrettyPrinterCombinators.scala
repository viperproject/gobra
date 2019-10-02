package viper.gobra.ast.printing

import org.bitbucket.inkytonik.kiama

trait PrettyPrinterCombinators { this: kiama.output.PrettyPrinter =>

  def opt[T](x: Option[T])(f: T => Doc): Doc = x.fold(emptyDoc)(f)

  def when(x: Boolean, b: => Doc): Doc = if (x) b else emptyDoc

  def block(doc: Doc): Doc = {
    braces(nest(line <> doc) <> line)
  }

  def sequence(doc: Doc): Doc = nest(line <> doc)

  def spec(doc: Doc): Doc = nest(line <> doc)
}