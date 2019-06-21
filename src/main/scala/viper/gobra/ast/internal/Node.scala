/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.internal

import viper.gobra.ast.internal.utility.GobraStrategy
import viper.gobra.reporting.Source
import viper.silver.{ast => vpr}

trait Node extends Rewritable {

  def info: Source.Parser.Info

  def getMeta: Node.Meta = info

  def undefinedMeta: Boolean = getMeta == Source.Parser.Unsourced

  /**
    * Duplicate children. Children list must be in the same order as in getChildren
    *
    * @see [[Rewritable.getChildren()]] to see why we use type AnyRef for children
    * @param children New children for this node
    * @return Duplicated node
    */
  override def duplicate(children: scala.Seq[AnyRef]): Rewritable =
    GobraStrategy.gobraDuplicator(this, children, getMeta)

  def duplicateMeta(newMeta: Node.Meta): Rewritable =
    GobraStrategy.gobraDuplicator(this, getChildren, newMeta)

}

object Node {

  type Meta = Source.Parser.Info

  implicit class RichNode[N <: Node](n: N) {

    def deepclone: N = new utility.Rewriter().deepclone(n)

    def withInfo(newInfo: Source.Parser.Info): N = GobraStrategy.gobraDuplicator(n, n.getChildren, newInfo)

    def verifierInfo: vpr.Info = n.info.toInfo(n)

  }
}

