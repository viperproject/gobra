// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal

import viper.gobra.reporting.Source
import viper.silver.{ast => vpr}
import viper.silver.ast.utility.rewriter.{Rewritable => ViperRewritable}

/**
  * This trait makes the functionality of [[viper.silver.ast.utility.rewriter.Strategy]] available.
  * For that, only the `withChildren` and `children` methods of the [[ViperRewritable]] trait are necessary.
  * The trait provides reasonable implementations for the other two methods, namely `meta` and `withMeta`.
  */
trait Rewritable extends ViperRewritable { this: Node =>

  private def create(children: Seq[Any])(info: Any): this.type = {
    import scala.reflect.runtime.{universe => reflection}
    val mirror = reflection.runtimeMirror(reflection.getClass.getClassLoader)
    val instanceMirror = mirror.reflect(this)
    val classSymbol = instanceMirror.symbol
    val classMirror = mirror.reflectClass(classSymbol)
    val constructorSymbol = instanceMirror.symbol.primaryConstructor.asMethod
    val constructorMirror = classMirror.reflectConstructor(constructorSymbol)

    val firstArgList = children
    val secondArgList = Seq(info)

    // Call constructor
    val newNode = constructorMirror(firstArgList ++ secondArgList: _*)
    newNode.asInstanceOf[this.type]
  }

  def withInfo(info: Source.Parser.Info): this.type = {
    create(children)(info)
  }

  /**
    * This method is necessary to use Viper's rewriting framework.
    * When calling this method from Gobra, do not set the `pos` argument.
    * */
  override def withChildren(children: Seq[Any], pos: Option[(vpr.Position, vpr.Position)], forceRewrite: Boolean): this.type = {
    assert(pos.isEmpty, "The pos argument must be set to nil if called on Gobra nodes.")

    if (!forceRewrite && this.children == children) {
      this
    } else {
      create(children)(info)
    }
  }

  override def meta: (vpr.Position, vpr.Info, vpr.ErrorTrafo) = vprMeta

  /** This method assumes that `posInfoTrafo` is from a viper node generated by Gobra. */
  override def withMeta(posInfoTrafo: (vpr.Position, vpr.Info, vpr.ErrorTrafo)): this.type = {
    val info = posInfoTrafo._2 match {
      case vpr.NoInfo => Source.Parser.Internal
      case vprInfo =>
        val verifierInfo = vprInfo.getUniqueInfo[Source.Verifier.Info].get
        Source.Parser.Single(verifierInfo.pnode, verifierInfo.origin)
    }
    withInfo(info)
  }
}
