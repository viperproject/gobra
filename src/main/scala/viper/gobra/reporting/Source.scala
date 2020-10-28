// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.silver.ast.SourcePosition
import viper.silver.{ast => vpr}
import viper.gobra.ast.{frontend, internal}
import viper.gobra.util.Violation
import viper.silver.ast.utility.rewriter.{SimpleContext, Strategy, StrategyBuilder, Traverse}
import viper.silver.ast.utility.rewriter.Traverse.Traverse

object Source {

  case class Origin(pos: SourcePosition, tag: String)

  abstract class Annotation()
  class AnnotatedOrigin(val origin: Origin, val annotation: Annotation) extends Origin(origin.pos, origin.tag)

  object Parser {

    sealed trait Info {
      def origin: Option[Origin]
      def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo)
    }

    object Unsourced extends Info {
      override def origin: Option[Origin] = throw new IllegalStateException()
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) = throw new IllegalStateException()
    }

    case object Internal extends Info {
      override lazy val origin: Option[Origin] = None
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) =
        (vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
    }

    case class Single(pnode: frontend.PNode, src: Origin) extends Info {
      override lazy val origin: Option[Origin] = Some(src)
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) =
        (vpr.TranslatedPosition(src.pos), Verifier.Info(pnode, node, src), vpr.NoTrafos)

      def annotateOrigin(annotation: Annotation): Single = Single(pnode, new AnnotatedOrigin(src, annotation))
    }

    object Single {
      def fromVpr(src: vpr.Exp): Single = {
        val info = Source.unapply(src).get
        Single(info.pnode, info.origin)
      }
    }
  }

  object Verifier {

    case class Info(pnode: frontend.PNode, node: internal.Node, origin: Origin) extends vpr.Info {
      override def comment: Seq[String] = Vector.empty
      override def isCached: Boolean = false
    }
  }

  def unapply(node: vpr.Node): Option[Verifier.Info] = {
    val info = node.getPrettyMetadata._2
    info.getUniqueInfo[Verifier.Info]
  }

  def withInfo[N <: vpr.Node](n: (vpr.Position, vpr.Info, vpr.ErrorTrafo) => N)(source: internal.Node): N = {
    source.info match {
      case Parser.Internal => n(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
      case Parser.Unsourced => throw new IllegalStateException()

      case Parser.Single(pnode, origin) =>

        val newInfo = Verifier.Info(pnode, source, origin)
        val newPos  = vpr.TranslatedPosition(origin.pos)

        n(newPos, newInfo, vpr.NoTrafos)
    }
  }

  implicit class RichViperNode[N <: vpr.Node](node: N) {

    def withInfo(source: internal.Node): N = {
      val (pos, info, errT) = node.getPrettyMetadata

      def message(fieldName: String) = {
        s"Node to annotate ('$node' of class ${node.getClass.getSimpleName}) already has " +
          s"field '$fieldName' set"
      }

      require(info == vpr.NoInfo, message("info"))
      require(pos == vpr.NoPosition, message("pos"))

      source.info match {
        case Parser.Internal => node
        case Parser.Unsourced => Violation.violation(s"information cannot be taken from an unsourced node $source")

        case Parser.Single(pnode, origin) =>

          val newInfo = Verifier.Info(pnode, source, origin)
          val newPos  = vpr.TranslatedPosition(origin.pos)

          node.withMeta(newPos, newInfo, errT).asInstanceOf[N]
      }
    }

    def transformWithNoRec(pre: PartialFunction[vpr.Node, vpr.Node] = PartialFunction.empty,
                  recurse: Traverse = Traverse.TopDown)
    : N = {
      var strategy: Strategy[vpr.Node, SimpleContext[vpr.Node]] = null
      strategy = StrategyBuilder.Slim[vpr.Node]({ case n: vpr.Node =>
        if (pre.isDefinedAt(n)) pre(n)
        else strategy.noRec(n)
      }, recurse)
      strategy.execute[N](node)
    }

    def withDeepInfo(source: internal.Node): N = {
      node.transformWithNoRec{
        case n: vpr.Node
          if {val m = n.getPrettyMetadata; m._1 == vpr.NoPosition && m._2 == vpr.NoInfo} =>
          n.withInfo(source)
      }
    }
  }


}
