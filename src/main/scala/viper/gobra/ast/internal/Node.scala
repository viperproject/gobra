// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal

import viper.gobra.ast.internal.utility.Nodes
import viper.gobra.reporting.Source
import viper.silver.ast.utility.Visitor
import viper.silver.ast.utility.rewriter.Traverse.Traverse
import viper.silver.ast.utility.rewriter.{StrategyBuilder, Traverse}
import viper.silver.{ast => vpr}

import scala.collection.mutable
import scala.reflect.ClassTag

trait Node extends Rewritable with Product {

  def info: Source.Parser.Info

  def undefinedInfo: Boolean = info == Source.Parser.Unsourced

  lazy val vprMeta: (vpr.Position, vpr.Info, vpr.ErrorTrafo) = info.vprMeta(this)

  def pretty(prettyPrinter: PrettyPrinter = Node.defaultPrettyPrinter): String = prettyPrinter.format(this)

  lazy val formatted: String = pretty()

  lazy val formattedShort: String = pretty(Node.shortPrettyPrinter)

  override def toString: String = formatted

  // Visitor and Nodes utilities

  /** @see [[Nodes.subnodes()]] */
  def subnodes: Seq[Node] = Nodes.subnodes(this)

  /** @see [[Visitor.reduceTree()]] */
  def reduceTree[A](f: (Node, Seq[A]) => A): A = Visitor.reduceTree(this, Nodes.subnodes)(f)

  /** @see [[Visitor.reduceWithContext()]] */
  def reduceWithContext[C, R](context: C, enter: (Node, C) => C, combine: (Node, C, Seq[R]) => R): R = {
    Visitor.reduceWithContext(this, Nodes.subnodes)(context, enter, combine)
  }

  /** Apply the given function to the AST node and all its subnodes. */
  def foreach[A](f: Node => A): Unit = Visitor.visit(this, Nodes.subnodes) { case a: Node => f(a) }

  /** Builds a new collection with all the AST nodes and returns an iterator over it. */
  def iterator: Iterator[Node] = {
    val elements = mutable.Queue.empty[Node]
    for (x <- this) {
      elements.append(x)
    }
    elements.iterator
  }

  /** @see [[Visitor.visit()]] */
  def visit[A](f: PartialFunction[Node, A]): Unit = {
    Visitor.visit(this, Nodes.subnodes)(f)
  }

  /** @see [[Visitor.visitWithContext()]] */
  def visitWithContext[C](c: C)(f: C => PartialFunction[Node, C]): Unit = {
    Visitor.visitWithContext(this, Nodes.subnodes, c)(f)
  }

  /** @see [[Visitor.visitWithContextManually()]] */
  def visitWithContextManually[C, A](c: C)(f: C => PartialFunction[Node, A]): Unit = {
    Visitor.visitWithContextManually(this, Nodes.subnodes, c)(f)
  }

  /** @see [[Visitor.visit()]] */
  def visit[A](f1: PartialFunction[Node, A], f2: PartialFunction[Node, A]): Unit = {
    Visitor.visit(this, Nodes.subnodes, f1, f2)
  }

  /** @see [[Visitor.visitOpt()]] */
  def visitOpt(f: Node => Boolean): Unit = {
    Visitor.visitOpt(this, Nodes.subnodes)(f)
  }

  /** @see [[Visitor.visitOpt()]] */
  def visitOpt[A](f1: Node => Boolean, f2: Node => A): Unit = {
    Visitor.visitOpt(this, Nodes.subnodes, f1, f2)
  }

  /** @see [[Visitor.existsDefined()]] */
  def existsDefined[A](f: PartialFunction[Node, A]): Boolean = Visitor.existsDefined(this, Nodes.subnodes)(f)

  /** @see [[Visitor.hasSubnode()]] */
  def hasSubnode(toFind: Node): Boolean = Visitor.hasSubnode(this, toFind, Nodes.subnodes)

  /** @see [[Visitor.deepCollect()]] */
  def deepCollect[A](f: PartialFunction[Node, A]): Seq[A] =
    Visitor.deepCollect(Seq(this), Nodes.subnodes)(f)

  /** @see [[Visitor.shallowCollect()]] */
  def shallowCollect[R](f: PartialFunction[Node, R]): Seq[R] =
    Visitor.shallowCollect(Seq(this), Nodes.subnodes)(f)

  def contains(n: Node): Boolean = this.existsDefined {
    case `n` =>
  }

  def contains[N <: Node : ClassTag]: Boolean = {
    val clazz = implicitly[ClassTag[N]].runtimeClass
    this.existsDefined {
      case n: N if clazz.isInstance(n) =>
    }
  }

  def transform(pre: PartialFunction[Node, Node] = PartialFunction.empty,
                recurse: Traverse = Traverse.Innermost)
  : this.type = {

    StrategyBuilder.Slim[Node](pre, recurse) execute[this.type] (this)
  }

  def transformForceCopy(pre: PartialFunction[Node, Node] = PartialFunction.empty,
                         recurse: Traverse = Traverse.Innermost)
  : this.type = {

    StrategyBuilder.Slim[Node](pre, recurse).forceCopy() execute[this.type] (this)
  }

  def transformNodeAndContext[C](transformation: PartialFunction[(Node, C), (Node, C)],
                                 initialContext: C,
                                 recurse: Traverse = Traverse.Innermost)
  : this.type = {

    StrategyBuilder.RewriteNodeAndContext[Node, C](transformation, initialContext, recurse).execute[this.type](this)
  }

  def replace(original: Node, replacement: Node): this.type =
    this.transform { case `original` => replacement }

  def replace[N <: Node : ClassTag](replacements: Map[N, Node]): this.type =
    if (replacements.isEmpty) this
    else this.transform { case t: N if replacements.contains(t) => replacements(t) }

}

object Node {

  val defaultPrettyPrinter = new DefaultPrettyPrinter()
  val shortPrettyPrinter = new ShortPrettyPrinter()

  type Info = Source.Parser.Info
}

