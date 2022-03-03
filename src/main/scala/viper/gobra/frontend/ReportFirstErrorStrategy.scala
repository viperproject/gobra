// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.frontend

import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.{BailErrorStrategy, DefaultErrorStrategy, FailedPredicateException, InputMismatchException, Parser, ParserRuleContext, RecognitionException, Token}
import viper.gobra.frontend.GobraParser.{BlockContext, ExprSwitchStmtContext}

/**
  * An ANTLR Error strategy that does not try to recover from errors, but instead reports the first error encountered.
  * It tries to differentiate between true parser errors and stack-dependent ambiguities to potentially avoid parsing
  * an incorrect input twice.
  */
class ReportFirstErrorStrategy extends DefaultErrorStrategy {


  override def sync(recognizer: Parser): Unit = {  }

  override def reportFailedPredicate(recognizer: Parser, e: FailedPredicateException): Unit = {
    val msg = e.getMessage
    recognizer.notifyErrorListeners(e.getOffendingToken, msg, e)
  }

  override def recover(recognizer: Parser, e: RecognitionException): Unit = {
    var context = recognizer.getContext
    // For blocks that could be interpreted as struct literals (like `if a == b { }` or `switch tag := 0; tag { }`)
    // Cast a wide net to catch every case, but still allow faster parsing if the error can't be an ambiguity.
    // Thee rest of recover and recoverInline is the same as in the superclass.
    context match {
      case _ : BlockContext => throw new AmbiguityException
      case _ : ExprSwitchStmtContext => throw new AmbiguityException
      case _ =>
    }
    while (context != null) {
      context.exception = e
      context = context.getParent
    }
    throw new ParseCancellationException(e)
  }

  override def recoverInline(recognizer: Parser): Token = {
    val e = new InputMismatchException(recognizer)
    var context = recognizer.getContext
    context match {
      case _ : BlockContext => throw new AmbiguityException
      case _ : ExprSwitchStmtContext => throw new AmbiguityException
      case _ =>
    }
    while ( context != null) {
      context.exception = e
      context = context.getParent
    }
    reportError(recognizer, e)
    throw new ParseCancellationException(e)
  }

}

class AmbiguityException() extends RuntimeException