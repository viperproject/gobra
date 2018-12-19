/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.reporting

import viper.silver.ast.SourcePosition

sealed trait VerifierError {
  def position: SourcePosition
  def message: String
  def id: String

  def formattedMessage: String =
    s"$message (${position.line}:${position.column})"

  override def toString: String = formattedMessage
}

case class ParserError(message: String, position: SourcePosition) extends VerifierError {
  val id = "parser_error"
}
