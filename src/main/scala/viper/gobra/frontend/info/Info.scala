package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PPackage}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{CyclicImportError, TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

import scala.collection.immutable.ListMap

object Info {
  type GoTree = Tree[PNode, PPackage]

  class Context {
    /** stores the results of all imported packages that have been parsed and type checked so far */
    private var contextMap: Map[String, Either[Vector[VerifierError], ExternalTypeInfo]] = ListMap[String, Either[Vector[VerifierError], ExternalTypeInfo]]()
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var pendingPackages: Vector[String] = Vector()
    /** stores all cycles that have been discovered so far */
    private var knownImportCycles: Set[Vector[String]] = Set()

    def addPackage(typeInfo: ExternalTypeInfo): Unit = {
      pendingPackages = pendingPackages.filterNot(_ == typeInfo.pkgName.name)
      contextMap = contextMap + (typeInfo.pkgName.name -> Right(typeInfo))
    }

    def addErrenousPackage(importPath: String, errors: Vector[VerifierError]): Unit = {
      pendingPackages = pendingPackages.filterNot(_ == importPath)
      contextMap = contextMap + (importPath -> Left(errors))
    }

    def getTypeInfo(importPath: String): Option[Either[Vector[VerifierError], ExternalTypeInfo]] = contextMap.get(importPath) match {
      case s@Some(_) => s
      case _ => {
        // there is no entry yet and package resolution might need to resolve multiple depending packages
        // keep track of these packages in pendingPackages until either type information or an error is added to contextMap
        if (pendingPackages.contains(importPath)) {
          // package cycle detected
          knownImportCycles += pendingPackages
          Some(Left(Vector(CyclicImportError(s"Cyclic package import detected starting with import path '$importPath'"))))
        } else {
          pendingPackages = pendingPackages :+ importPath
          None
        }
      }
    }

    /**
      * Returns all import paths that lie on the cycle of imports or none if no cycle was found
      */
    def getImportCycle(importPath: String): Option[Vector[String]] = knownImportCycles.find(_.contains(importPath))

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
