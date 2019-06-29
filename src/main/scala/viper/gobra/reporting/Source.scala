package viper.gobra.reporting

import viper.gobra.ast.internal.Node
import viper.silver.ast.SourcePosition
import viper.silver.{ast => vpr}
import viper.gobra.ast.{frontend, internal}

object Source {

  case class Origin(pos: SourcePosition, tag: String)

  object Parser {

    sealed trait Info {
      def origin: Option[Origin]
      def toInfo(node: internal.Node): vpr.Info
    }

    object Unsourced extends Info {
      override def origin: Option[Origin] = throw new IllegalStateException()
      override def toInfo(node: Node): vpr.Info = throw new IllegalStateException()
    }

    case object Internal extends Info {
      override lazy val origin: Option[Origin] = None
      override def toInfo(node: Node): vpr.Info = vpr.NoInfo
    }

    case class Single(pnode: frontend.PNode, src: Origin) extends Info {
      override lazy val origin: Option[Origin] = Some(src)
      override def toInfo(node: Node): vpr.Info = Verifier.Info(pnode, node, src)
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

      // require(info == vpr.NoInfo, message("info"))
      // require(pos == vpr.NoPosition, message("pos"))

      source.info match {
        case Parser.Internal => node
        case Parser.Unsourced => throw new IllegalStateException()

        case Parser.Single(pnode, origin) =>

          val newInfo = Verifier.Info(pnode, source, origin)
          val newPos  = vpr.TranslatedPosition(origin.pos)

          node.duplicateMeta((newPos, newInfo, errT)).asInstanceOf[N]
      }
    }
  }


}
