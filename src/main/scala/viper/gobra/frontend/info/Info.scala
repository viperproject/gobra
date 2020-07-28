package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.PNode.PPkg
import viper.gobra.ast.frontend.{PNode, PPackage}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

import scala.collection.immutable.ListMap

object Info {
  type GoTree = Tree[PNode, PPackage]

  class Context {
    private var contextMap: Map[PPkg, ExternalTypeInfo] = ListMap[PPkg, ExternalTypeInfo]()

    def addPackage(typeInfo: ExternalTypeInfo): Unit =
      contextMap = contextMap + (typeInfo.pkgName.name -> typeInfo)

    def getTypeInfo(pkg: PPkg): Option[ExternalTypeInfo] = contextMap.get(pkg)

    def getContexts: Iterable[ExternalTypeInfo] = contextMap.values
  }

  def check(pkg: PPackage, context: Context = new Context)(config: Config): Either[Vector[VerifierError], TypeInfo with ExternalTypeInfo] = {
    val tree = new GoTree(pkg)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree, context)(config: Config)

    val errors = info.errors
    config.reporter report TypeCheckDebugMessage(config.inputFiles.head, () => pkg, () => getDebugInfo(pkg, info))
    if (errors.isEmpty) {
      config.reporter report TypeCheckSuccessMessage(config.inputFiles.head, () => pkg, () => getErasedGhostCode(pkg, info))
      Right(info)
    } else {
      val typeErrors = pkg.positions.translate(errors, TypeError)
      config.reporter report TypeCheckFailureMessage(config.inputFiles.head, () => pkg, typeErrors)
      Left(typeErrors)
    }
  }

  private def getErasedGhostCode(pkg: PPackage, info: TypeInfoImpl): String = {
    new GhostLessPrinter(info).format(pkg)
  }

  private def getDebugInfo(pkg: PPackage, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(pkg)
  }
}
