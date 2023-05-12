// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.transformers.hyper

import viper.silver.ast.pretty.PrettyPrintPrimitives
import viper.silver.ast.pretty.FastPrettyPrinter.{ContOps, nil, parens, show, showBlock, text}
import viper.silver.ast._
import viper.silver.verifier.VerificationResult

case class SIFReturnStmt(exp: Option[Exp], resVar: Option[LocalVar])
                        (val pos: Position = NoPosition,
                         val info: Info = NoInfo,
                         val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq(exp, resVar).collect({case Some(x) => x})

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("return") <+> (exp match {
    case Some(x) => show(x)
    case None => nil
  })
}

case class SIFBreakStmt()(val pos: Position = NoPosition,
                          val info: Info = NoInfo,
                          val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq()

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("break")
}

case class SIFContinueStmt()(val pos: Position = NoPosition,
                             val info: Info = NoInfo,
                             val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq()

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("continue")
}

case class SIFRaiseStmt(assignment: Option[LocalVarAssign])
                       (val pos: Position = NoPosition,
                        val info: Info = NoInfo,
                        val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = assignment match {
    case Some(a) => Seq(a)
    case None => Seq()
  }

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("raise") <+>
    (assignment match {case Some(a) => show(a) case None => nil})
}

case class SIFExceptionHandler(errVar: LocalVar, exception: Exp, body: Seqn)
                              (val pos: Position = NoPosition,
                               val info: Info = NoInfo,
                               val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq(errVar, exception, body)

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("catch") <+> parens(show(exception)) <+>
    showBlock(body)
}

case class SIFTryCatchStmt(body: Seqn,
                           catchBlocks: Seq[SIFExceptionHandler],
                           elseBlock: Option[Seqn],
                           finallyBlock: Option[Seqn])
                          (val pos: Position = NoPosition,
                           val info: Info = NoInfo,
                           val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq(body) ++
    Seq(elseBlock, finallyBlock).collect({case Some(x) => x}) ++
    catchBlocks.flatMap(h => h.subnodes)

  override def prettyPrint: PrettyPrintPrimitives#Cont = {
    text("try") <+> showBlock(body) <>
      catchBlocks.map(h => h.prettyPrint)
        .fold(nil)((l, r) => l <+> r) <+>
      (elseBlock match {case Some(x) => text("else") <+> showBlock(x) case None => nil}) <+>
      (finallyBlock match {case Some(x) => text("finally") <+> showBlock(x) case None => nil})
  }
}

case class SIFDeclassifyStmt(exp: Exp)
                            (val pos: Position = NoPosition,
                             val info: Info = NoInfo,
                             val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq(exp)

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("declassify") <+> show(exp)
}

case class SIFInlinedCallStmt(stmts: Seqn)
                             (val pos: Position = NoPosition,
                              val info: Info = NoInfo,
                              val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq(stmts)

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("inlined call") <> show(stmts)
}

case class SIFAssertNoException()(val pos: Position = NoPosition,
                                  val info: Info = NoInfo,
                                  val errT: ErrorTrafo = NoTrafos) extends ExtensionStmt {
  override def extensionSubnodes: Seq[Node] = Seq()

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("assert no exception")
}

case class SIFLowExp(exp: Exp, comparator: Option[String] = None, typVarMap: Map[TypeVar, Type] = Map())
                    (val pos: Position = NoPosition,
                     val info: Info = NoInfo,
                     val errT: ErrorTrafo = NoTrafos) extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq(exp)

  override def typ: Type = Bool

  override def verifyExtExp(): VerificationResult = ???

  override def prettyPrint: PrettyPrintPrimitives#Cont = (if (comparator.isDefined) text("lowVal")
  else text("low")) <> parens(show(exp))

  override val extensionIsPure: Boolean = exp.isPure
}

case class SIFLowEventExp()(val pos: Position = NoPosition,
                            val info: Info = NoInfo,
                            val errT: ErrorTrafo = NoTrafos) extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Nil

  override def typ: Type = Bool

  override def verifyExtExp(): VerificationResult = ???

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("lowEvent")

  override val extensionIsPure: Boolean = true

}

case class SIFLowExitExp()(val pos: Position = NoPosition,
                           val info: Info = NoInfo,
                           val errT: ErrorTrafo = NoTrafos) extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Nil

  override def typ: Type = Bool

  override def verifyExtExp(): VerificationResult = ???

  override def prettyPrint: PrettyPrintPrimitives#Cont = text("lowExit")

  override val extensionIsPure: Boolean = false
}

case class SIFTerminatesExp(cond: Exp)(val pos: Position = NoPosition,
                                       val info: Info = NoInfo,
                                       val errT: ErrorTrafo = NoTrafos) extends ExtensionExp {
  override def extensionSubnodes: Seq[Node] = Seq(cond)

  override def typ: Type = Bool

  override def verifyExtExp(): VerificationResult = ???

  override def prettyPrint: PrettyPrintPrimitives#Cont =
    text("terminates under condition") <+> show(cond)

  override def extensionIsPure: Boolean = cond.isPure
}

case class SIFInfo(comment: Seq[String],
                   continueUnaware: Boolean = false,
                   obligationVar: Boolean = false) extends Info{
  lazy val isCached = false
}

case class SIFDynCheckInfo(comment: Seq[String],
                           dynCheck: Exp,
                           onlyDynVersion: Boolean = false) extends Info{
  lazy val isCached = false
}
