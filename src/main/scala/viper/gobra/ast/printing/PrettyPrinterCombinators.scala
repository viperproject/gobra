package viper.gobra.ast.printing

import org.bitbucket.inkytonik.kiama

trait PrettyPrinterCombinators { this: kiama.output.PrettyPrinter =>

  def opt[T](x: Option[T])(f: T => Doc): Doc = x.fold(emptyDoc)(f)

  def block(doc: Doc): Doc = {
    braces(nest(line <> doc) <> line)
  }

  def blockBothBracesGoified(doc: Doc): Doc = {
    "//@" <+> braces(nest(line <> doc) <> line <> "//@" <> space)
  }

  def blockClosingBraceGoified(doc: Doc): Doc = {
    braces(nest(line <> doc) <> line <> "//@" <> space)
  }

  def sequence(doc: Doc): Doc = nest(line <> doc)

  def spec(doc: Doc): Doc = nest(line <> doc)
}