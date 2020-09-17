// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.printing

import org.bitbucket.inkytonik.kiama

trait PrettyPrinterCombinators { this: kiama.output.PrettyPrinter =>

  def opt[T](x: Option[T])(f: T => Doc): Doc = x.fold(emptyDoc)(f)

  def block(doc: Doc): Doc = {
    braces(nest(line <> doc) <> line)
  }

  def sequence(doc: Doc): Doc = nest(line <> doc)

  def spec(doc: Doc): Doc = nest(line <> doc)
}