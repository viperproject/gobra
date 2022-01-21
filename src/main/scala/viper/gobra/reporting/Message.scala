// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import java.nio.file.Path
import viper.gobra.ast.frontend.PNode.PPkg
import viper.gobra.ast.frontend.{PPackage, PProgram}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.VerifierResult.Success
import viper.silver
import viper.silver.{ast => vpr}
import viper.silver.reporter.Message

/**
  * Messages reported by GobraReporter
  */
sealed trait GobraMessage {
  override def toString: String = name
  val name: String
}

sealed trait GobraVerificationResultMessage extends GobraMessage {
  override val name: String = s"verification_result"
  def result: VerifierResult
  val verifier: String
}

case class GobraOverallSuccessMessage(verifier: String) extends GobraVerificationResultMessage {
  override val name: String = s"overall_success_message"
  val result: VerifierResult = Success

  override def toString: String = s"overall_success_message(" +
    s"verifier=${verifier})"
}

case class GobraOverallFailureMessage(verifier: String, result: VerifierResult) extends GobraVerificationResultMessage {
  override val name: String = s"overall_failure_message"

  override def toString: String = s"overall_failure_message(" +
    s"verifier=${verifier}, " +
    s"failure=${result.toString})"
}

sealed trait GobraEntityResultMessage extends GobraVerificationResultMessage {
  val entity: vpr.Member
  val concerning: Source.Verifier.Info
}

case class GobraEntitySuccessMessage(verifier: String, entity: vpr.Member, concerning: Source.Verifier.Info) extends GobraEntityResultMessage {
  override val name: String = s"entity_success_message"
  val result: VerifierResult = Success

  override def toString: String = s"entity_success_message(" +
    s"verifier=${verifier}, " +
    s"concerning=${concerning.toString})"
}

case class GobraEntityFailureMessage(verifier: String, entity: vpr.Member, concerning: Source.Verifier.Info, result: VerifierResult) extends GobraEntityResultMessage {
  override val name: String = s"entity_failure_message"

  override def toString: String = s"entity_failure_message(" +
    s"verifier=${verifier}, " +
    s"concerning=${concerning.toString}, " +
    s"failure=${result.toString})"
}

case class ChoppedProgressMessage(idx: Int, of: Int) extends GobraMessage {
  override val name: String = "chopped_progress_message"
  override def toString: String = s"$name(idx=$idx,of=$of)"
}

case class PreprocessedInputMessage(input: String, preprocessedContent: () => String) extends GobraMessage {
  override val name: String = s"preprocessed_input_message"

  override def toString: String = s"preprocessed_input_message(" +
    s"file=${input}, " +
    s"content=${preprocessedContent()})"
}

case class ParsedInputMessage(input: String, ast: () => PProgram) extends GobraMessage {
  override val name: String = s"parsed_input_message"

  override def toString: String = s"parsed_input_message(" +
    s"file=${input}, " +
    s"ast=${ast().formatted})"
}

case class ParserErrorMessage(input: Path, result: Vector[ParserError]) extends GobraMessage {
  override val name: String = s"parser_error_message"

  override def toString: String = s"parser_error_message(" +
    s"file=${input}), " +
    s"errors=${result.map(_.toString).mkString(",")})"
}

sealed trait TypeCheckMessage extends GobraMessage {
  override val name: String = s"type_check_message"
  val inputs: Vector[String]
  val ast: () => PPackage

  override def toString: String = s"type_check_message(" +
    s"files=${inputs})"
}

case class TypeCheckSuccessMessage(inputs: Vector[String], ast: () => PPackage, erasedGhostCode: () => String, goifiedGhostCode: () => String) extends TypeCheckMessage {
  override val name: String = s"type_check_success_message"

  override def toString: String = s"type_check_success_message(" +
    s"files=${inputs})"
}

case class TypeCheckFailureMessage(inputs: Vector[String], packageName: PPkg, ast: () => PPackage, result: Vector[VerifierError]) extends TypeCheckMessage {
  override val name: String = s"type_check_failure_message"

  override def toString: String = s"type_check_failure_message(" +
    s"files=${inputs}, " +
    s"package=$packageName, " +
    s"failures=${result.map(_.toString).mkString(",")})"
}

case class TypeCheckDebugMessage(inputs: Vector[String], ast: () => PPackage, debugTypeInfo: () => String) extends TypeCheckMessage {
  override val name: String = s"type_check_debug_message"

  override def toString: String = s"type_check_debug_message(" +
    s"files=${inputs}), " +
    s"debugInfo=${debugTypeInfo()})"
}

case class DesugaredMessage(inputs: Vector[String], internal: () => in.Program) extends GobraMessage {
  override val name: String = s"desugared_message"

  override def toString: String = s"desugared_message(" +
    s"files=${inputs}, " +
    s"internal=${internal().formatted})"
}

case class AppliedInternalTransformsMessage(inputs: Vector[String], internal: () => in.Program) extends GobraMessage {
  override val name: String = s"transform_message"

  override def toString: String = s"transform_message(" +
    s"files=${inputs}, " +
    s"internal=${internal().formatted})"
}

case class GeneratedViperMessage(inputs: Vector[String], vprAst: () => vpr.Program, backtrack: () => BackTranslator.BackTrackInfo) extends GobraMessage {
  override val name: String = s"generated_viper_message"

  override def toString: String = s"generated_viper_message(" +
    s"files=${inputs}, " +
    s"vprFormated=$vprAstFormatted)"

  lazy val vprAstFormatted: String = silver.ast.pretty.FastPrettyPrinter.pretty(vprAst())
}

case class ChoppedViperMessage(inputs: Vector[String], idx: Int, vprAst: () => vpr.Program, backtrack: () => BackTranslator.BackTrackInfo) extends GobraMessage {
  override val name: String = s"chopped_viper_message"

  override def toString: String = s"chopped_viper_message(" +
    s"file=${inputs}, " +
    s"vprFormated=$vprAstFormatted)"

  lazy val vprAstFormatted: String = silver.ast.pretty.FastPrettyPrinter.pretty(vprAst())
}

case class RawMessage(msg: Message) extends GobraMessage {
  override val name: String = s"raw_message"

  override def toString: String = s"raw_message(" +
    s"msg=${msg.toString})"
}

/**
  * Simple messages contain just one text field.
  */
abstract class SimpleMessage(val text: String) extends GobraMessage {
  override val name: String = s"simple_message"
}

case class CopyrightReport(override val text: String) extends SimpleMessage(text) {
  override def toString: String = s"copyright_report(text=${text.toString})"
  override val name: String = s"copyright_report"
}
