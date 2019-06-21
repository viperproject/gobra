package viper.gobra.reporting

import viper.gobra.ast.internal.{Node, Origin, Source}
import viper.gobra.reporting.Source.Parser.Info
import viper.silver.ast.SourcePosition
import viper.silver.{ast, ast => vpr}
import viper.gobra.ast.{frontend, internal}

object Source {

  case class Origin(pos: SourcePosition)

  object Parser {

    sealed trait Info {
      def origin: Option[Origin]
      def toInfo(node: internal.Node): vpr.Info
    }

    case object Internal extends Info {
      override lazy val origin: Option[Origin] = None
      override def toInfo(node: Node): vpr.Info = vpr.NoInfo
    }

    case class Single(pnode: frontend.PNode, src: Origin) extends Info {
      override lazy val origin: Option[Origin] = Some(src)
      override def toInfo(node: Node): ast.Info = Verifier.Info(pnode, node, src)
    }
  }

  object Verifier {

    case class Info(pnode: frontend.PNode, node: internal.Node, origin: Origin) extends vpr.Info {
      override def comment: Seq[String] = Vector.empty
      override def isCached: Boolean = false
    }
  }

  implicit class RichViperNode[N <: vpr.Node](node: N) {

    def withSource(source: internal.Node): N = {
      val (pos, info, errT) = node.getPrettyMetadata

      def message(fieldName: String) = {
        s"Node to annotate ('$node' of class ${node.getClass.getSimpleName}) already has " +
          s"field '$fieldName' set"
      }

      require(info == vpr.NoInfo, message("info"))
      require(pos == vpr.NoPosition, message("pos"))

      source.src match {
        case Parser.Internal => node

        case Parser.Single(pnode, origin) =>

          val newInfo = Verifier.Info(pnode, source, origin)
          val newPos  = vpr.TranslatedPosition(origin.pos)

          node.duplicateMeta((newPos, newInfo, errT)).asInstanceOf[N]
      }
    }
  }


}
