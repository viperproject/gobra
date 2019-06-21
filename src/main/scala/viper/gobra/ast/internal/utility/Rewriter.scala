package viper.gobra.ast.internal.utility

import org.bitbucket.inkytonik.kiama.rewriting.{CallbackRewriter, Cloner}

class Rewriter extends CallbackRewriter with Cloner {

  override def rewriting[T](oldTerm: T, newTerm: T): T = newTerm
}
