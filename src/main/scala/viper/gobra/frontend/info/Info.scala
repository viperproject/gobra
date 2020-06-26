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
    private var contextMap: Map[PPkg, Either[Vector[VerifierError], ExternalTypeInfo]] = ListMap[PPkg, Either[Vector[VerifierError], ExternalTypeInfo]]()

    def addPackage(typeInfo: ExternalTypeInfo): Unit =
      contextMap = contextMap + (typeInfo.pkgName.name -> Right(typeInfo))

    def addErrenousPackage(pkg: PPkg, errors: Vector[VerifierError]): Unit =
      contextMap = contextMap + (pkg -> Left(errors))

    def getTypeInfo(pkg: PPkg): Option[Either[Vector[VerifierError], ExternalTypeInfo]] = contextMap.get(pkg)

    def getContexts: Iterable[ExternalTypeInfo] = contextMap.values.collect { case Right(info) => info }

    def getExternalErrors: Vector[VerifierError] = contextMap.values.collect { case Left(errs) => errs }.flatten.toVector
  }

  def check(program: PPackage, context: Context = new Context)(config: Config): Either[Vector[VerifierError], TypeInfo with ExternalTypeInfo] = {
    val tree = new GoTree(program)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree, context)(config: Config)

    // get errors and remove duplicates as errors related to imported packages might occur multiple times
    val errors = info.errors.distinct
    config.reporter report TypeCheckDebugMessage(config.inputFiles.head, () => program, () => getDebugInfo(program, info))
    if (errors.isEmpty) {
      config.reporter report TypeCheckSuccessMessage(config.inputFiles.head, () => program, () => getErasedGhostCode(program, info))
      Right(info)
    } else {
      val typeErrors = program.positions.translate(errors, TypeError)
      config.reporter report TypeCheckFailureMessage(config.inputFiles.head, program.packageClause.id.name, () => program, typeErrors)
      Left(typeErrors)
    }
  }

  private def getErasedGhostCode(program: PPackage, info: TypeInfoImpl): String = {
    new GhostLessPrinter(info).format(program)
  }

  private def getDebugInfo(program: PPackage, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(program)
  }
}
