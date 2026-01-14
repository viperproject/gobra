// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import org.bitbucket.inkytonik.kiama.util.Position
import viper.gobra.ast.frontend.PNode
import viper.gobra.ast.{frontend, internal}
import viper.gobra.util.Violation
import viper.silicon.dependencyAnalysis.{DependencyAnalysisInfo, DependencyType}
import viper.silver.ast.SourcePosition
import viper.silver.ast.utility.rewriter.Traverse.Traverse
import viper.silver.ast.utility.rewriter.{SimpleContext, Strategy, StrategyBuilder, Traverse}
import viper.silver.{ast => vpr}

import java.nio.file.Paths
import scala.annotation.tailrec
import scala.reflect.ClassTag

object Source {

  sealed abstract class AbstractOrigin(val pos: SourcePosition, val tag: String)
  case class Origin(override val pos: SourcePosition, override val tag: String) extends AbstractOrigin(pos, tag)
  case class AnnotatedOrigin(origin: AbstractOrigin, annotation: Annotation) extends AbstractOrigin(origin.pos, origin.tag)

  sealed trait Annotation
  case object OverflowCheckAnnotation extends Annotation
  case object ReceiverNotNilCheckAnnotation extends Annotation
  case object ImportPreNotEstablished extends Annotation
  case object MainPreNotEstablished extends Annotation
  case object LoopInvariantNotEstablishedAnnotation extends Annotation
  case class NoPermissionToRangeExpressionAnnotation() extends Annotation
  case class InsufficientPermissionToRangeExpressionAnnotation() extends Annotation
  case class AutoImplProofAnnotation(subT: String, superT: String) extends Annotation
  case class InvalidImplTermMeasureAnnotation() extends Annotation
  class OverwriteErrorAnnotation(
                                  newError: VerificationError => VerificationError,
                                  attachReasons: Boolean = true
                                ) extends Annotation {
    def apply(err: VerificationError): VerificationError = {
      if (attachReasons) {
        err.reasons.foldLeft(newError(err)){ case (err, reason) => err dueTo reason }
      } else newError(err)
    }
  }

  object Parser {

    sealed trait Info {
      def origin: Option[AbstractOrigin]
      def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo)
      def tag: String = origin.map(_.tag.trim).getOrElse("unknown")
    }

    object Unsourced extends Info {
      override def origin: Option[AbstractOrigin] = throw new IllegalStateException()
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) = throw new IllegalStateException()
    }

    case object Internal extends Info {
      override lazy val origin: Option[AbstractOrigin] = None
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) =
        (vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
    }

    case class Single(pnode: frontend.PNode, src: AbstractOrigin) extends Info {
      override lazy val origin: Option[AbstractOrigin] = Some(src)
      override def vprMeta(node: internal.Node): (vpr.Position, vpr.Info, vpr.ErrorTrafo) =
        (vpr.TranslatedPosition(src.pos), Verifier.Info(pnode, node, src), vpr.NoTrafos)

      def createAnnotatedInfo(annotation: Annotation): Single = Single(pnode, AnnotatedOrigin(src, annotation))
    }

    object Single {
      def fromVpr(src: vpr.Exp): Single = {
        val info = Source.unapply(src).get
        Single(info.pnode, info.origin)
      }
    }
  }

  object Verifier {
    case class Info(pnode: frontend.PNode, node: internal.Node, origin: AbstractOrigin, comment: Seq[String] = Vector.empty) extends vpr.Info {
      override def isCached: Boolean = false
      def addComment(cs : Seq[String]) : Info = Info(pnode, node, origin, comment ++ cs)

      def trySrc[T <: frontend.PNode: ClassTag](postfix: String = ""): String = pnode match {
        case _: T => origin.tag.trim + postfix
        case _ => ""
      }

      def createAnnotatedInfo(annotation: Annotation): Info = copy(origin = AnnotatedOrigin(origin, annotation))
    }

    class GobraDependencyAnalysisInfo(pNode: PNode, start: Position, end: Position, pos: vpr.AbstractSourcePosition, dependencyType: Option[DependencyType] = None, infoStr: Option[String] = None) extends DependencyAnalysisInfo(infoStr.getOrElse(pNode.toString), pos, dependencyType) {
      def getStart: Position = start
      def getEnd: Position = end
      def getPNode: PNode = pNode
      def getPosition: vpr.Position = pos
    }

    val noInfo: Info = Info(
      frontend.PLabelDef("unknown"),
      internal.LabelProxy("unknown")(Source.Parser.Internal),
      Origin(SourcePosition(Paths.get("."), 0, 0), "unknown")
    )

    object / {
      def unapply(arg: Info): Option[(Info, Annotation)] = arg.origin match {
        case ann: AnnotatedOrigin => Some((arg, ann.annotation))
        case _ => None
      }
    }
  }

  object Synthesized {
    def unapply(node : vpr.Node) : Option[Verifier.Info] = node.meta._2 match {
      case vpr.SimpleInfo(comment) => searchInfo(node).map(_.addComment(comment))
      case _ => None
    }
  }

  object CertainSynthesized {
    def unapply(node: vpr.Node): Option[Verifier.Info] = {
      Synthesized.unapply(node) orElse Some(Verifier.noInfo)
    }
  }

  def unapply(node: vpr.Node): Option[Verifier.Info] = {
    val info = node.getPrettyMetadata._2
    info.getUniqueInfo[Verifier.Info]
  }

  object CertainSource {
    def unapply(node: vpr.Node): Option[Verifier.Info] = {
      Source.unapply(node) orElse Some(Verifier.noInfo)
    }
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

  /**
    * Searches for source information  in the AST (sub)graph of `node`
    * and returns the first info encountered; or `None` if no such info exists.
    */
  def searchInfo(node : vpr.Node) : Option[Verifier.Info] = node.meta._2 match {
    case info: Verifier.Info => Some(info)
    case _ => searchInfo(node.subnodes)
  }

  @tailrec
  private def searchInfo(nodes : Seq[vpr.Node]) : Option[Verifier.Info] = nodes match {
    case Seq() => None
    case nodes => searchInfo(nodes.head) match {
      case Some(info) => Some(info)
      case None => searchInfo(nodes.tail)
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
