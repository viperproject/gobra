// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._
import viper.silver.ast.pretty.FastPrettyPrinter.{ContOps, parens, show, text}
import viper.silver.ast.pretty.PrettyPrintPrimitives
import viper.silver.verifier.{ConsistencyError, Failure, VerificationResult}

/**
  * AST nodes that the hyperfunctions plugin adds to the ones of Viper's information-flow plugin
  * ([[viper.silver.plugin.sif.SIFLowExp]], [[viper.silver.plugin.sif.SIFLowEventExp]],
  * [[viper.silver.plugin.sif.SIFRelExp]], which are reused as they are).
  */
object HyperfuncsAst {
  /** The name of the type of execution identifiers. */
  val ExecTypeName: String = "Exec"
  val ExecType: DomainType = DomainType(ExecTypeName, Map.empty[TypeVar, Type])(Seq.empty)

  def isExecType(t: Type): Boolean = t match {
    case DomainType(ExecTypeName, _) => true
    case _ => false
  }

  def unsupported(what: String, pos: Position): VerificationResult =
    Failure(Seq(ConsistencyError(s"$what must be translated by the hyperfunctions plugin before verification.", pos)))
}

/** `active(i)`: execution `i` is active at the current program point. `idx` is an integer literal or has type Exec. */
case class HFActiveExp(idx: Exp)(val pos: Position = NoPosition, val info: Info = NoInfo, val errT: ErrorTrafo = NoTrafos)
  extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq(idx)
  override def typ: Type = Bool
  override def extensionIsPure: Boolean = true
  override def verifyExtExp(): VerificationResult = HyperfuncsAst.unsupported("active", pos)
  override def prettyPrint: PrettyPrintPrimitives#Cont = text("active") <> parens(show(idx))
}

/**
  * `rel(e, i)` where `i` is an expression of type Exec (an execution parameter or a variable bound by an execution
  * quantifier). With an integer literal as index, the plugin uses the SIF plugin's [[viper.silver.plugin.sif.SIFRelExp]].
  */
case class HFRelExp(exp: Exp, idx: Exp)(val pos: Position = NoPosition, val info: Info = NoInfo, val errT: ErrorTrafo = NoTrafos)
  extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq(exp, idx)
  override def typ: Type = exp.typ
  override def extensionIsPure: Boolean = exp.isPure
  override def verifyExtExp(): VerificationResult = HyperfuncsAst.unsupported("rel", pos)
  override def prettyPrint: PrettyPrintPrimitives#Cont = text("rel") <> parens(show(exp) <> text(",") <+> show(idx))
}

/** An integer literal used as an execution identifier, i.e., as an argument for a hyperfunction parameter of type Exec. */
case class HFExecLit(n: BigInt)(val pos: Position = NoPosition, val info: Info = NoInfo, val errT: ErrorTrafo = NoTrafos)
  extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq.empty
  override def typ: Type = HyperfuncsAst.ExecType
  override def extensionIsPure: Boolean = true
  override def verifyExtExp(): VerificationResult = HyperfuncsAst.unsupported("An execution identifier", pos)
  override def prettyPrint: PrettyPrintPrimitives#Cont = text(n.toString)
}

/**
  * A placeholder used inside [[HyperfuncsTransformer]]: an already translated expression that the SIF plugin's
  * translation functions must not traverse. It is a leaf; the translated expression is stored outside the AST.
  */
case class HFHole(id: Int, holeTyp: Type)(val pos: Position = NoPosition, val info: Info = NoInfo, val errT: ErrorTrafo = NoTrafos)
  extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq.empty
  override def typ: Type = holeTyp
  override def extensionIsPure: Boolean = true
  override def verifyExtExp(): VerificationResult = HyperfuncsAst.unsupported("A hole", pos)
  override def prettyPrint: PrettyPrintPrimitives#Cont = text(s"hole$id")
}
