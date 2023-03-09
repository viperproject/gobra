// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import org.bitbucket.inkytonik.kiama.util.{Position, Source}
import viper.gobra.ast.frontend.{PImport, PNode, PPackage}
import viper.gobra.frontend.{Config, Job, TaskManager}
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage, BuiltInImport, BuiltInPackage, RegularImport}
import viper.gobra.frontend.Parser.ParseSuccessResult
import viper.gobra.frontend.TaskManagerMode.{Lazy, Parallel, Sequential}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostLessPrinter, GoifyingPrinter}
import viper.gobra.reporting.{TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.security.MessageDigest
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Info {

  type GoTree = Tree[PNode, PPackage]
  type TypeCheckResult = Either[Vector[VerifierError], TypeInfo with ExternalTypeInfo]
  type DependentTypeInfo = Map[AbstractImport, () => TypeCheckResult] // values are functions such that laziness is possible

  trait GetParseResult {
    def parseResults: Map[AbstractPackage, ParseSuccessResult]

    protected def getParseResult(abstractPackage: AbstractPackage): ParseSuccessResult = {
      Violation.violation(parseResults.contains(abstractPackage), s"GetParseResult: expects that $abstractPackage has been parsed")
      parseResults(abstractPackage)
    }
  }

  /**
    * ImportCycle describes a cyclic import. `importClosingCycle` is the AST node that closes the cycle and
    * `cyclicPackages` stores the packages involved in the cycle.
    */
  case class ImportCycle(importNodeCausingCycle: PImport, importNodeStart: Option[Position], cyclicPackages: Vector[AbstractImport])

  class CycleChecker(val config: Config, val parseResults: Map[AbstractPackage, ParseSuccessResult]) extends GetParseResult {
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var parserPendingPackages: Vector[AbstractImport] = Vector()

    def check(abstractPackage: AbstractPackage): Messages = {
      val (_, ast) = getParseResult(abstractPackage)
      ast.imports.flatMap(importNode => {
        val cycles = getCycles(RegularImport(importNode.importPath))
        createImportError(importNode, cycles)
      })
    }

    /**
      * returns all parser errors and cyclic errors transitively found in imported packages
      */
    private def getCycles(importTarget: AbstractImport): Vector[ImportCycle] = {
      parserPendingPackages = parserPendingPackages :+ importTarget
      val abstractPackage = AbstractPackage(importTarget)(config)
      val (_, ast) = getParseResult(abstractPackage)
      val res = ast.imports.flatMap(importNode => {
        val directlyImportedTarget = RegularImport(importNode.importPath)
        if (parserPendingPackages.contains(directlyImportedTarget)) {
          // package cycle detected
          val importNodeStart = ast.positions.positions.getStart(importNode)
          Vector(ImportCycle(importNode, importNodeStart, parserPendingPackages))
        } else {
          getCycles(directlyImportedTarget)
        }
      })
      parserPendingPackages = parserPendingPackages.filterNot(_ == importTarget)
      res
    }

    private def createImportError(importNode: PImport, cycles: Vector[ImportCycle]): Messages = {
      val importTarget = RegularImport(importNode.importPath)
      cycles.flatMap(cycle => {
        val positionalInfo = cycle.importNodeStart.map(pos => s" at ${pos.format}").getOrElse("")
        message(importNode, s"Package '$importTarget' is part of the following import cycle that involves the import ${cycle.importNodeCausingCycle}$positionalInfo: ${cycle.cyclicPackages.mkString("[", ", ", "]")}")
      })
    }
  }

  /**
    * All TypeInfo instances share a single context instance.
    * Therefore, package management is centralized.
    */
  class Context(val config: Config, val parseResults: Map[AbstractPackage, ParseSuccessResult])(val executionContext: GobraExecutionContext) extends GetParseResult {
    /** stores the results of all imported packages that have been parsed so far */
    // private var parserContextMap: Map[AbstractPackage, Either[Vector[VerifierError], (Vector[Source], PPackage)]] = ListMap()
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    // private var parserPendingPackages: Vector[AbstractImport] = Vector()
    /** stores all cycles that have been discovered so far */
    // var parserKnownImportCycles: Set[ImportCycle] = Set()

    /*
    def parseRecursively(importTarget: AbstractImport)(config: Config): Either[Vector[VerifierError], PPackage] = {
      val packageTarget = AbstractPackage(importTarget)(config)
      parserPendingPackages = parserPendingPackages :+ importTarget
      val pkgSources = PackageResolver.resolveSources(importTarget)(config)
        .getOrElse(Vector())
        .map(_.source)
      val res = for {
        nonEmptyPkgSources <- if (pkgSources.isEmpty)
          Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
        else Right(pkgSources)
        parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
        directImportTargets = parsedProgram.imports.map(i => RegularImport(i.importPath))
        errorsInTransitivePackages = directImportTargets
          .map(directImportTarget => {
            if (parserPendingPackages.contains(directImportTarget)) {
              // package cycle detected
              parserKnownImportCycles += parserPendingPackages
              Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$packageTarget'")))
            } else {
              parseRecursively(directImportTarget)(config)
            }
          })
          .flatMap(_.left.getOrElse(Vector.empty))
        res <- if (errorsInTransitivePackages.isEmpty) Right(parsedProgram) else Left(errorsInTransitivePackages)
      } yield res
      parserPendingPackages = parserPendingPackages.filterNot(_ == importTarget)
      parserContextMap = parserContextMap + (packageTarget -> res)
      res
    }
    */
    /*
    def parseRecursively(importTarget: AbstractImport)(config: Config): Either[Vector[VerifierError], PPackage] = {
      val packageTarget = AbstractPackage(importTarget)(config)

      // skip packages that we have already parsed:
      if (parserContextMap.contains(packageTarget)) {
        return parserContextMap(packageTarget) match {
          case Right((_, pkg)) => Right(pkg)
          case Left(errs) => Left(errs)
        }
      }

      println(s"parsing $importTarget")
      parserPendingPackages = parserPendingPackages :+ importTarget
      val pkgSources = PackageResolver.resolveSources(importTarget)(config)
        .getOrElse(Vector())
        .map(_.source)

      val res = for {
        nonEmptyPkgSources <- if (pkgSources.isEmpty)
          Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
          // Left(message(importNode, s"No source files for package '$importTarget' found"))
          else Right(pkgSources)
        parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
        errorsInTransitivePackages = parsedProgram.imports
          .map(importNode => {
            val directImportTarget = RegularImport(importNode.importPath)
            if (parserPendingPackages.contains(directImportTarget)) {
              // package cycle detected
              val importNodeStart = parsedProgram.positions.positions.getStart(importNode)
              parserKnownImportCycles += ImportCycle(importNode, importNodeStart, parserPendingPackages)
              println(s"cycle detected: ${importTarget} has import ${directImportTarget} that occurs in ${parserKnownImportCycles}")
              Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$packageTarget'")))
              // val cycle = parserPendingPackages
              // Left(message(importNode, s"Package '$importTarget' is part of this import cycle: ${cycle.mkString("[", ", ", "]")}"))
            } else {
              parseRecursively(directImportTarget)(config)
            }
          })
          .flatMap(_.left.getOrElse(Vector.empty))
        res <- if (errorsInTransitivePackages.isEmpty) Right(parsedProgram) else Left(errorsInTransitivePackages)
      } yield res
      /*
      val res: Either[Messages, PPackage] = for {
        nonEmptyPkgSources <- if (pkgSources.isEmpty)
          // Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
          Left(message(importNode, s"No source files for package '$importTarget' found"))
        else Right(pkgSources)
        parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
        directImportTargets = parsedProgram.imports // .map(i => RegularImport(i.importPath))
        errorsInTransitivePackages: Messages = directImportTargets
          .map(directImportTarget => {
            if (parserPendingPackages.contains(directImportTarget)) {
              // package cycle detected
              parserKnownImportCycles += parserPendingPackages
              // Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$packageTarget'")))
              val cycle = parserPendingPackages
              Left(message(importNode, s"Package '$importTarget' is part of this import cycle: ${cycle.mkString("[", ", ", "]")}"))
            } else {
              parseRecursively(directImportTarget)(config)
            }
          })
          .flatMap(_.left.getOrElse(noMessages))
        res <- if (errorsInTransitivePackages.isEmpty) Right(parsedProgram) else Left(errorsInTransitivePackages)
      } yield res
      */
      parserPendingPackages = parserPendingPackages.filterNot(_ == importTarget)
      parserContextMap = parserContextMap + (packageTarget -> res.map(pkg => (pkgSources, pkg)))
      res
    }
    */
    /**
      * returns all parser errors and cyclic errors transitively found in imported packages
      */
      /*
    def computeCycles(importTarget: AbstractImport): Vector[VerifierError] = {
      parserPendingPackages = parserPendingPackages :+ importTarget
      val abstractPackage = AbstractPackage(importTarget)(config)
      val res = for {
        res <- parseManager.getResult(abstractPackage)
        (_, ast) = res
        directImportTargets = ast.imports
        errorsInTransitivePackages = directImportTargets
          .flatMap(importNode => {
            val directImportTarget = RegularImport(importNode.importPath)
            if (parserPendingPackages.contains(directImportTarget)) {
              // package cycle detected
              val importNodeStart = ast.positions.positions.getStart(importNode)
              parserKnownImportCycles += ImportCycle(importNode, importNodeStart, parserPendingPackages)
              Vector(CyclicImportError(s"Cyclic package import detected starting with package '$directImportTarget'"))
              // val cycle = parserPendingPackages
              // message(importNode, s"Package '$importTarget' is part of this import cycle: ${cycle.mkString("[", ", ", "]")}")
            } else {
              computeCycles(directImportTarget)
            }
          })
        res <- if (errorsInTransitivePackages.isEmpty) Right(ast) else Left(errorsInTransitivePackages)
      } yield res
      parserPendingPackages = parserPendingPackages.filterNot(_ == importTarget)
      res.left.getOrElse(Vector.empty)
    }
       */
    /*
    def parseRecursively(pkg: PPackage)(config: Config): Either[Vector[VerifierError], PPackage] = {
      val imports = Seq(BuiltInImport) ++
        pkg.imports.map(i => RegularImport(i.importPath))
      imports.map(importTarget => {
        parserPendingPackages = parserPendingPackages :+ importTarget
        val pkgSources = PackageResolver.resolveSources(importTarget)(config)
          .getOrElse(Vector())
          .map(_.source)
        val res = for {
          nonEmptyPkgSources <- if (pkgSources.isEmpty)
            Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
          else Right(pkgSources)
          parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
      })

    }
    */

    /**
      * Returns all package names that lie on the cycle of imports or none if no cycle was found
      */
      /*
    def getParserImportCycle(importTarget: AbstractImport): Option[ImportCycle] =
      parserKnownImportCycles.find(_.cyclicPackages.contains(importTarget))
       */

    /*
    case class ParseJob(importTarget: AbstractImport) extends Job[Either[Vector[VerifierError], (Vector[Source], PPackage)]] {
      override def compute(): Either[Vector[VerifierError], (Vector[Source], PPackage)] = {
        println(s"start parsing $importTarget")
        val startMs = System.currentTimeMillis()
        val pkgSources = PackageResolver.resolveSources(importTarget)(config)
          .getOrElse(Vector())
          .map(_.source)
        for {
          nonEmptyPkgSources <- if (pkgSources.isEmpty)
            Left(Vector(NotFoundError(s"No source files for package '$importTarget' found")))
          // Left(message(importNode, s"No source files for package '$importTarget' found"))
          else Right(pkgSources)

          // before parsing, get imports and add these parse jobs
          _ = fastParse(nonEmptyPkgSources)
            .map(directImportTarget => {
              val directImportPackage = AbstractPackage(directImportTarget)(config)
              parseManager.addIfAbsent(directImportPackage, ParseJob(directImportTarget))(executionContext)
            })

          parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
          durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
          _ = println(s"parsing $importTarget done (took ${durationS}s)")
          /*
          // submit jobs to parse dependent packages:
          _ = parsedProgram.imports.foreach(importNode => {
            val directImportTarget = RegularImport(importNode.importPath)
            val directImportPackage = AbstractPackage(directImportTarget)(config)
            parseManager.addIfAbsent(directImportPackage, ParseJob(directImportTarget))(executionContext)
          })
          */
        } yield (pkgSources, parsedProgram)
      }
    }

    case class SuccessParseJob(sources: Vector[Source], pkg: PPackage) extends Job[Either[Vector[VerifierError], (Vector[Source], PPackage)]] {
      override def compute(): Either[Vector[VerifierError], (Vector[Source], PPackage)] =
        Right((sources, pkg))
    }

    // val parseManager = new TaskManager[AbstractPackage, Either[Vector[VerifierError], (Vector[Source], PPackage)]](config)
    def parse(importTarget: AbstractImport): Unit = {
      val abstractPackage = AbstractPackage(importTarget)(config)
      val parseJob = ParseJob(importTarget)
      parseManager.addIfAbsent(abstractPackage, parseJob)(executionContext)
    }
    */
    trait TypeCheckJob {
      protected def typeCheck(pkgSources: Vector[Source], pkg: PPackage, dependentTypeInfo: DependentTypeInfo, isMainContext: Boolean = false): TypeCheckResult = {
        println(s"start type-checking ${pkg.info.id}")
        val startMs = System.currentTimeMillis()
        val res = Info.checkSources(pkgSources, pkg, dependentTypeInfo, isMainContext = isMainContext)(config)
        val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
        println(s"type-checking ${pkg.info.id} done (took ${durationS}s)")
        res
      }
    }
    /*
    case class SimpleTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] with TypeCheckJob {
      override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = typeCheck(pkgSources, pkg, context)
    }
    */
    /*
    case class DependentTypeCheckJob(importTarget: AbstractImport, context: Context) extends Job[Future[Either[Vector[VerifierError], ExternalTypeInfo]]] with TypeCheckJob {
      override def toString: String = s"DependentTypeCheckJob for $importTarget"

      override def compute(): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = importTarget == BuiltInImport
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        val dependentJobsFuts = dependencies
          .map(DependentTypeCheckJob(_, context))
          // add to manager & typecheck them if not present yet
          .map(dependentJob => {
            val dependentAbstractPackage = AbstractPackage(dependentJob.importTarget)(config)
            typeCheckManager.addIfAbsent(dependentAbstractPackage, dependentJob)(executionContext)
            typeCheckManager.getFuture(dependentAbstractPackage).flatten
          })
        implicit val executor: GobraExecutionContext = executionContext
        val dependentJobsFut = Future.sequence(dependentJobsFuts)
        dependentJobsFut.map(_ => typeCheck(sources, ast, context))
      }
    }
    */
    /*
    case class LazyTypeCheckJob(importTarget: AbstractImport, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] with TypeCheckJob {
      override def toString: String = s"LazyTypeCheckJob for $importTarget"

      override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
        // in lazy mode, this function is called exactly when this package needs to be type-checked
        // we also do not care about any dependent packages, because they will be lazily type-checked
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        typeCheck(sources, ast, context)
      }
    }
    */

    case class LazyTypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends Job[TypeCheckResult] with TypeCheckJob {
      override def toString: String = s"LazyTypeCheckJob for $abstractPackage"

      override def compute(): TypeCheckResult = {
        // in lazy mode, this function is called exactly when this package needs to be type-checked
        // we also do not care about any dependent packages, because they will be lazily type-checked
        val (sources, ast) = getParseResult(abstractPackage)
        // we assume that all packages have been registered with the typeCheckManager
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = abstractPackage == BuiltInPackage
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        val dependentTypeInfo: DependentTypeInfo = dependencies
          .map(importTarget => (importTarget, () => typeCheckManager.getResult(AbstractPackage(importTarget)(config))))
          .toMap
        typeCheck(sources, ast, dependentTypeInfo, isMainContext = isMainContext)
      }
    }
    /*
    case class LazyTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] with TypeCheckJob {
      override def toString: String = s"LazyTypeCheckJob for ${pkg.info.id}"

      override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
        // in lazy mode, this function is called exactly when this package needs to be type-checked
        // we also do not care about any dependent packages, because they will be lazily type-checked
        // val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $abstractPackage failed"))
        typeCheck(pkgSources, pkg, context)
      }
    }
    */
    /*
    case class SequentialTypeCheckJob(importTarget: AbstractImport, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] with TypeCheckJob {
      override def toString: String = s"SequentialTypeCheckJob for $importTarget"

      override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
        // in lazy mode, this function is called exactly when this package needs to be type-checked
        // we also do not care about any dependent packages, because they will be lazily type-checked
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = importTarget == BuiltInImport
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        // first type-check dependent packages:
        dependencies
          .map(SequentialTypeCheckJob(_, context))
          // add to manager & typecheck them if not present yet
          .foreach(dependentJob => {
            val dependentAbstractPackage = AbstractPackage(dependentJob.importTarget)(config)
            typeCheckManager.addIfAbsent(dependentAbstractPackage, dependentJob)(executionContext)
          })
        typeCheck(sources, ast, context)
        // typeCheck(abstractPackage, context)
      }
    }
     */
    case class SequentialTypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends Job[TypeCheckResult] with TypeCheckJob {
      override def toString: String = s"SequentialTypeCheckJob for $abstractPackage"

      override def compute(): TypeCheckResult = {
        val (sources, ast) = getParseResult(abstractPackage)
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = abstractPackage == BuiltInPackage
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        // first type-check dependent packages:
        val dependentTypeInfos = dependencies
          .map(importTarget => {
            val dependentPackage = AbstractPackage(importTarget)(config)
            // add to manager & typecheck them if not present yet
            val job = SequentialTypeCheckJob(dependentPackage)
            typeCheckManager.addIfAbsent(dependentPackage, job)(executionContext)
            (importTarget, () => typeCheckManager.getResult(dependentPackage))
          })

        typeCheck(sources, ast, dependentTypeInfos.toMap, isMainContext = isMainContext)
      }
    }
    /*
    case class ParallelTypeCheckJob(importTarget: AbstractImport, context: Context) extends Job[Future[Either[Vector[VerifierError], ExternalTypeInfo]]] with TypeCheckJob {
      override def toString: String = s"ParallelTypeCheckJob for $importTarget"

      override def compute(): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        // in lazy mode, this function is called exactly when this package needs to be type-checked
        // we also do not care about any dependent packages, because they will be lazily type-checked
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = importTarget == BuiltInImport
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        // first type-check dependent packages:
        val dependentJobsFuts = dependencies
          .map(ParallelTypeCheckJob(_, context))
          // add to manager & typecheck them if not present yet
          .map(dependentJob => {
            val dependentAbstractPackage = AbstractPackage(dependentJob.importTarget)(config)
            parallelTypeCheckManager.addIfAbsent(dependentAbstractPackage, dependentJob)(executionContext)
            parallelTypeCheckManager.getFuture(dependentAbstractPackage).flatten
          })
        implicit val executor: GobraExecutionContext = executionContext
        val dependentJobsFut = Future.sequence(dependentJobsFuts)
        dependentJobsFut.map(_ => typeCheck(sources, ast, context))
      }
    }
    */
    case class ParallelTypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends Job[Future[TypeCheckResult]] with TypeCheckJob {
      override def toString: String = s"ParallelTypeCheckJob for $abstractPackage"

      override def compute(): Future[TypeCheckResult] = {
        val (sources, ast) = getParseResult(abstractPackage)
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = abstractPackage == BuiltInPackage
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        // first type-check dependent packages:
        val dependentJobsFuts = dependencies
          .map(importTarget => {
            val dependentPackage = AbstractPackage(importTarget)(config)
            // add to manager & typecheck them if not present yet
            val job = ParallelTypeCheckJob(dependentPackage)
            parallelTypeCheckManager.addIfAbsent(dependentPackage, job)(executionContext)
            parallelTypeCheckManager.getFuture(dependentPackage).flatten
              .map(typeInfo => (importTarget, () => typeInfo))(executionContext)
          })
        implicit val executor: GobraExecutionContext = executionContext
        val dependentJobsFut = Future.sequence(dependentJobsFuts)
        dependentJobsFut.map(dependentTypeInfo => typeCheck(sources, ast, dependentTypeInfo.toMap, isMainContext = isMainContext))
      }
    }
    /*
    case class ChainingTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends Job[Future[Either[Vector[VerifierError], ExternalTypeInfo]]] with TypeCheckJob {
    // case class ChainingTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] with TypeCheckJob {
    // case class ChainingTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends TypeCheckJob {
      override def toString: String = s"ChainingTypeCheckJob for ${pkg.info.id}"

      private def lookupPackage(importTarget: AbstractImport): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        val job = ChainingTypeCheckJob(sources, ast, context)
        // typeCheckManager.addIfAbsent(abstractPackage, job)(executionContext)
        // typeCheckManager.getFuture(abstractPackage)
        // typeCheckManager.getResult(abstractPackage)
        typeCheckManager.addIfAbsent(abstractPackage,() => job.compute())
      }

      // override def compute(): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
      // override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
      def compute(): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        println(s"$this compute()")
        implicit val executor: GobraExecutionContext = executionContext
        // val abstractPackage = AbstractPackage(importTarget)(config)
        // val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        // for type-checking this package, we need type information for imported packages:
        val importedTypeInfoFutures = // ast.imports.map(tuplesTypeCheck)
          pkg.imports.map(importNode => lookupPackage(RegularImport(importNode.importPath)))
        // add `BuiltInImport` since it's implicitly imported by every package
        val (_, builtInAst) = parseManager.getResult(BuiltInPackage).getOrElse(() => Violation.violation(s"parsing $BuiltInImport failed"))
        val isBuiltIn = pkg == builtInAst
        val dependentFutures = if (isBuiltIn) importedTypeInfoFutures else lookupPackage(BuiltInImport) +: importedTypeInfoFutures
        // execute call synchronously if there are no dependencies:
        // if (dependentFutures.isEmpty) {
        //   Future { typeCheck(pkgSources, pkg, context) }
        // } else {
          Future.sequence(dependentFutures)
            .map(packageResults => {
              val errsInImportedPackages = packageResults
                .collect { case Left(errs) => errs }
                .flatten
              if (errsInImportedPackages.isEmpty) {
                typeCheck(pkgSources, pkg, context)
              } else {
                Left(errsInImportedPackages)
              }
            })
          // Await.result(fut, Duration.Inf)
        // }
      }
    }
     */
    /*
    case class ChainingTypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends TypeCheckJob {
      private def lookupPackage(importTarget: AbstractImport): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        val job = ChainingTypeCheckJob(sources, ast, context)
        typeCheckManager.addIfAbsent(abstractPackage, job)(executionContext)
        typeCheckManager.getFuture(abstractPackage)
      }

      override def getFuture: Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
        implicit val executor: GobraExecutionContext = executionContext
        // val abstractPackage = AbstractPackage(importTarget)(config)
        // val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
        // for type-checking this package, we need type information for imported packages:
        val importedTypeInfoFutures = // ast.imports.map(tuplesTypeCheck)
          pkg.imports.map(importNode => lookupPackage(RegularImport(importNode.importPath)))
        // add `BuiltInImport` since it's implicitly imported by every package
        val (builtInSources, builtInAst) = parseManager.getResult(BuiltInPackage).getOrElse(() => Violation.violation(s"parsing $BuiltInImport failed"))
        val isBuiltIn = pkg == builtInAst
        val dependentFutures = if (isBuiltIn) importedTypeInfoFutures else lookupPackage(BuiltInImport) +: importedTypeInfoFutures
        Future.sequence(dependentFutures)
          .map(packageResults => {
            val errsInImportedPackages = packageResults
              .collect { case Left(errs) => errs }
              .flatten
            if (errsInImportedPackages.isEmpty) {
              typeCheck
            } else {
              Left(errsInImportedPackages)
            }
          })
      }

      override def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
        println(s"ERROR: compute called job ${pkg.info.id}")
        Left(Vector.empty)
      }
    }
    */
    case class FailureJob(errs: Vector[VerifierError]) extends Job[TypeCheckResult] {
      override def compute(): TypeCheckResult = Left(errs)
    }

    /*
    lazy val typeContextMap: Map[AbstractPackage, Future[Either[Vector[VerifierError], ExternalTypeInfo]]] =
      parserContextMap.map{ case (abstractPackage, value) => (abstractPackage, value match {
        case Right((pkgSources, pkg)) =>
          if (parallelizeTypechecking) Future { typeCheck(pkgSources, pkg) }(executionContext)
          else {
            val res = typeCheck(pkgSources, pkg)
            if (res.isLeft) {
              println(s"type-checking ${abstractPackage} failed: ${res.left.get}")
            }
            Future.successful(res)
          }
        case Left(errs) => Future.successful(Left(errs))
      })}
     */
    private val typeCheckManager = new TaskManager[AbstractPackage, TypeCheckResult](config.typeCheckMode)
    private val parallelTypeCheckManager = new TaskManager[AbstractPackage, Future[TypeCheckResult]](config.typeCheckMode)
    // private val typeCheckManager = new TaskManager[AbstractPackage, Future[Either[Vector[VerifierError], ExternalTypeInfo]]](config)
    // private val typeCheckManager = new FutureManager[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]]

    // def typeCheck(): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
    def typeCheck(pkg: AbstractPackage): TypeCheckResult = {
      config.typeCheckMode match {
        case Lazy =>
          // we have to transitively add all packages to the typeCheckManager:
          lazyTypeCheckRecursively(pkg, isMainContext = true)
          typeCheckManager.getResult(pkg)
        case Sequential =>
          typeCheckManager.addIfAbsent(pkg, SequentialTypeCheckJob(pkg, isMainContext = true))(executionContext)
          typeCheckManager.getResult(pkg)
        case Parallel =>
          parallelTypeCheckManager.addIfAbsent(pkg, ParallelTypeCheckJob(pkg, isMainContext = true))(executionContext)
          // wait for result:
          val fut = parallelTypeCheckManager.getResult(pkg)
          Await.result(fut, Duration.Inf)
      }
      // typeCheckManager.addIfAbsent(mainPackage, DependentTypeCheckJob(RegularImport(""), this))(executionContext)
      // typeCheckManager.getFuture(mainPackage).flatten
      // typeCheckManager.getFuture(mainPackage)
    }

    /*
    def lazyTypeCheckRecursively(importTarget: AbstractImport): Unit = {
      def allImports(importTarget: AbstractImport): Set[AbstractImport] = {
        val abstractPackage = AbstractPackage(importTarget)(config)
        val (_, ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $abstractPackage failed"))
        ast.imports.toSet.flatMap[AbstractImport](importNode => allImports(RegularImport(importNode.importPath))) + importTarget
      }

      val packages = allImports(importTarget) + BuiltInImport
      packages.foreach(importTarget => typeCheckManager.addIfAbsent(AbstractPackage(importTarget)(config), LazyTypeCheckJob(importTarget, this))(executionContext))
      println(s"packages added to typeCheckManager: ${packages.mkString("\n")}")
    }
    */
    /*
    def lazyTypeCheckRecursively(abstractPackage: AbstractPackage): Unit = {
      def allImports(abstractPackage: AbstractPackage): Set[AbstractPackage] = {
        val (_, ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $abstractPackage failed"))
        ast.imports.toSet.flatMap[AbstractPackage](importNode => allImports(AbstractPackage(RegularImport(importNode.importPath))(config))) + abstractPackage
      }

      val packages = allImports(abstractPackage) + BuiltInPackage
      packages.foreach(pkg => typeCheckManager.addIfAbsent(pkg, LazyTypeCheckJob(pkg, this))(executionContext))
      println(s"packages added to typeCheckManager: ${packages.mkString("\n")}")
    }
     */
    def lazyTypeCheckRecursively(abstractPackage: AbstractPackage, isMainContext: Boolean = false): Unit = {
      /** returns all transitively imported packages. However, `abstractPackage` is not included in the returned set! */
      def allImports(abstractPackage: AbstractPackage): Set[AbstractPackage] = {
        val (_, ast: PPackage) = getParseResult(abstractPackage)
        ast.imports
          .map(importNode => AbstractPackage(RegularImport(importNode.importPath))(config))
          .toSet
          .flatMap[AbstractPackage](directlyImportedPackage => allImports(directlyImportedPackage) + directlyImportedPackage)
      }

      val dependentPackages = allImports(abstractPackage) + BuiltInPackage
      // create jobs for all dependent packages
      dependentPackages.foreach(pkg => typeCheckManager.addIfAbsent(pkg, LazyTypeCheckJob(pkg))(executionContext))
      // create job for this package:
      typeCheckManager.addIfAbsent(abstractPackage, LazyTypeCheckJob(abstractPackage, isMainContext = isMainContext))(executionContext)
      // println(s"packages added to typeCheckManager: ${(dependentPackages + abstractPackage).mkString("\n")}")
    }
//    def typeCheck(sources: Vector[Source], pkg: PPackage, importTarget: AbstractImport): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
//      /*parserContextMap.foreach { case (abstractPackage, parseResult) =>
//        val typeCheckJob = parseResult match {
//          case Right((pkgSources, pkg)) => TypeCheckJob(pkgSources, pkg, this)
//          case Left(errs) => FailureJob(errs)
//        }
//        println(s"adding task $abstractPackage")
//        typeCheckManager.addIfAbsent(abstractPackage, typeCheckJob)(executionContext)
//      }
//      */
//      /*
//      parseManager.getAllResultsWithKeys.foreach { case (abstractPackage, parseResult) =>
//        val typeCheckJob = parseResult match {
//          case Right((pkgSources, pkg)) => TypeCheckJob(pkgSources, pkg, this)
//          case Left(errs) => FailureJob(errs)
//        }
//        println(s"adding task $abstractPackage")
//        // since we're adding the packages in arbitrary order, we cannot directly run the task in SEQUENTIAL mode because
//        // depending packages might not have been inserted yet
//        typeCheckManager.addIfAbsent(abstractPackage, typeCheckJob, insertOnly = config.typeCheckMode == TypeCheckMode.Sequential)(executionContext)
//      }
//
//       */
//      /*
//      val job = ChainingTypeCheckJob(sources, pkg, this)
//      /** this is a bit artificial as this is the top-level package, i.e. the one we want to verify */
//      val abstractPackage = AbstractPackage(RegularImport(""))(config)
//      // typeCheckManager.addIfAbsent(abstractPackage, job)(executionContext)
//      // typeCheckManager.getResult(abstractPackage)
//      // typeCheckManager.getFuture(abstractPackage)
//      typeCheckManager.addIfAbsent(abstractPackage, () => job.compute())
//       */
//
//      /*
//      pkg.imports
//        .map(importNode => RegularImport(importNode.importPath))
//        .map(typeCheckInternal)
//       */
//
//      // TODO perform lookup in typeCheckManager before re-checking every dependent package
//      val dependentTypeCheckFuts = pkg.imports
//        .map(importNode => RegularImport(importNode.importPath))
//        .map(typeCheckInternal)
//
//      val job = ChainingTypeCheckJob(sources, pkg, this)
//
//      implicit val executor: GobraExecutionContext = executionContext
//      val fut = for {
//        dependentTypeChecks <- Future.sequence(dependentTypeCheckFuts)
//        dependentErrs = dependentTypeChecks.flatMap {
//          case Right(_) => Vector.empty
//          case Left(errs) => errs
//        }
//        res = if (dependentErrs.isEmpty) {
//          println(s"start type-checking ${pkg.info.id}")
//          val startMs = System.currentTimeMillis()
//          val typeCheckRes = Info.check(pkg, sources, this)(config)
//          val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
//          println(s"type-checking ${pkg.info.id} done (took ${durationS}s)")
//          typeCheckRes
//        } else Left(dependentErrs)
//      } yield res
//      typeCheckManager.addIfAbsent(AbstractPackage(importTarget)(config), () => fut)
//    }
//
//    private def typeCheckInternal(importTarget: AbstractImport): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
//      // we iterate down to the package having no dependencies and register futures bottom up
//      val parseRes = parseManager.getResult(AbstractPackage(importTarget)(config))
//      parseRes.fold(errs => Future.successful(Left(errs)), { case (sources, pkg) => typeCheck(sources, pkg, importTarget) })
//      /*
//      for {
//        (sources: Vector[Source], pkg: PPackage) <- parseManager.getResult(AbstractPackage(importTarget)(config))
//          // .left.map(errs => Future.successful(Left(errs)))
//        dependentTypeCheckFuts = pkg.imports
//          .map(importNode => RegularImport(importNode.importPath))
//          .map(typeCheckInternal)
//        fut = for {
//          dependentTypeChecks <- Future.sequence(dependentTypeCheckFuts)
//          dependentErrs = dependentTypeChecks.flatMap {
//            case Right(_) => Vector.empty
//            case Left(errs) => errs
//          }
//          res = if (dependentErrs.isEmpty) {
//            println(s"start type-checking ${pkg.info.id}")
//            val startMs = System.currentTimeMillis()
//            val typeCheckRes = Info.check(pkg, sources, this)(config)
//            val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
//            println(s"type-checking ${pkg.info.id} done (took ${durationS}s)")
//            typeCheckRes
//          } else Left(dependentErrs)
//        } yield res
//      } yield fut
//       */
//    }

    /*
    def typeCheck(importTarget: AbstractImport): Future[Either[Vector[VerifierError], ExternalTypeInfo]] = {
      implicit val executor: GobraExecutionContext = executionContext
      *//*
      def tuplesTypeCheck(importNode: PImport): Future[(PImport, Either[Vector[VerifierError], ExternalTypeInfo])] = {
        val abstractPackage = RegularImport(importNode.importPath)
        typeCheck(abstractPackage)
          .map(res => (importNode, res))
      }
      *//*
      val abstractPackage = AbstractPackage(importTarget)(config)
      val (sources: Vector[Source], ast: PPackage) = parseManager.getResult(abstractPackage).getOrElse(() => Violation.violation(s"parsing $importTarget failed"))
      // for type-checking this package, we need type information for imported packages:
      val importedTypeInfoFutures = // ast.imports.map(tuplesTypeCheck)
        ast.imports.map(importNode => typeCheck(RegularImport(importNode.importPath)))
      // add `BuiltInImport` since it's implicitly imported by every package
      Future.sequence(typeCheck(BuiltInImport) +: importedTypeInfoFutures)
        .flatMap(packageResults => {
          val errsInImportedPackages = packageResults
            // .collect { case (_, Left(errs)) => errs }
            .collect { case Left(errs) => errs }
            .flatten
          val job = if (errsInImportedPackages.isEmpty) {
            TypeCheckJob(sources, ast, this)
          } else {
            FailureJob(errsInImportedPackages)
          }
          typeCheckManager.addIfAbsent(abstractPackage, job, insertOnly = config.typeCheckMode == TypeCheckMode.Sequential)(executionContext)
          job.getFuture
        })
    }
     */
    /*
    lazy val jobMap: Map[AbstractPackage, Job] =
      parserContextMap.transform { case (_, value) => value match {
        case Right((pkgSources, pkg)) => TypeCheckJob(pkgSources, pkg, this)
        case Left(errs) => FailureJob(errs)
      }}
     */

    def typeCheckSequentially(): Unit = {
      /*
      val jobs = jobMap.toVector.sorted(Ordering.by[(AbstractPackage, Job), Int](_._1.hashCode()))
      println(s"typeCheckSequentially: ${jobs.map(_._1)}")
      jobs.foreach { case (abstractPackage, job) =>
        // println(s"typeCheckSequentially: $abstractPackage")
        job.call() }
       */
      // NOP
    }

    def typeCheckInParallel(): Unit = {
      // NOP
      // jobMap.foreach { case (_, job) => Future{ job.call() }(executionContext) }
    }
    /*
    lazy val typeContextMap: Map[AbstractPackage, Future[Either[Vector[VerifierError], ExternalTypeInfo]]] =
      jobMap.transform { case (_, job) => job.getFuture }
    */
//    /** stores the results of all imported packages that have been parsed and type checked so far */
//    private var contextMap: Map[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]] = ListMap[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]]()
//    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
//    private var pendingPackages: Vector[AbstractImport] = Vector()
//    /** stores all cycles that have been discovered so far */
//    private var knownImportCycles: Set[Vector[AbstractImport]] = Set()
//
//    def addPackage(importTarget: AbstractImport, typeInfo: ExternalTypeInfo)(config: Config): Unit = {
//      val packageTarget = AbstractPackage(importTarget)(config)
//      pendingPackages = pendingPackages.filterNot(_ == importTarget)
//      contextMap = contextMap + (packageTarget -> Right(typeInfo))
//    }
//
//    def addErrenousPackage(importTarget: AbstractImport, errors: Vector[VerifierError])(config: Config): Unit = {
//      val packageTarget = AbstractPackage(importTarget)(config)
//      pendingPackages = pendingPackages.filterNot(_ == importTarget)
//      contextMap = contextMap + (packageTarget -> Left(errors))
//    }
//
//    def getTypeInfo(importTarget: AbstractImport)(config: Config): Option[Either[Vector[VerifierError], ExternalTypeInfo]] = {
//      val packageTarget = AbstractPackage(importTarget)(config)
//      contextMap.get(packageTarget) match {
//        case s@Some(_) => s
//        case _ => {
//          // there is no entry yet and package resolution might need to resolve multiple depending packages
//          // keep track of these packages in pendingPackages until either type information or an error is added to contextMap
//          if (pendingPackages.contains(importTarget)) {
//            // package cycle detected
//            knownImportCycles += pendingPackages
//            Some(Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$packageTarget'"))))
//          } else {
//            pendingPackages = pendingPackages :+ importTarget
//            None
//          }
//        }
//      }
//    }
//
//    /**
//      * Returns all package names that lie on the cycle of imports or none if no cycle was found
//      */
//    def getImportCycle(importTarget: AbstractImport): Option[Vector[AbstractImport]] = knownImportCycles.find(_.contains(importTarget))
//
//    def getContexts: Iterable[ExternalTypeInfo] = contextMap.values.collect { case Right(info) => info }
//
//    def getExternalErrors: Vector[VerifierError] = contextMap.values.collect { case Left(errs) => errs }.flatten.toVector

    def getContexts: Iterable[ExternalTypeInfo] = {
      // typeContextMap.values.map(fut => Await.result(fut, Duration.Inf)).collect { case Right(info) => info }
      config.typeCheckMode match {
        case Lazy | Sequential => typeCheckManager.getAllResults(executionContext).collect { case Right(info) => info }
        case Parallel =>
          implicit val executor: GobraExecutionContext = executionContext
          val results = Await.result(Future.sequence(parallelTypeCheckManager.getAllResults(executionContext)), Duration.Inf)
          results.collect { case Right(info) => info }
      }
      // typeCheckManager.getAllResults.collect { case Right(info) => info }
      // typeCheckManager.getAllResults(executionContext).collect { case Right(info) => info }
      /*
      implicit val executor: GobraExecutionContext = executionContext
      val results = Await.result(Future.sequence(typeCheckManager.getAllResults), Duration.Inf)
      results.collect { case Right(info) => info }
       */
    }
    /*
    def getTypeInfoNonBlocking(importTarget: AbstractImport)(config: Config): Option[Future[Either[Vector[VerifierError], ExternalTypeInfo]]] = {
      val packageTarget = AbstractPackage(importTarget)(config)
      typeContextMap.get(packageTarget)
    }

    def getTypeInfo(importTarget: AbstractImport)(config: Config): Option[Either[Vector[VerifierError], ExternalTypeInfo]] =
      getTypeInfoNonBlocking(importTarget)(config).map(Await.result(_, Duration.Inf))

     */

    /*
    def startTypeChecking(): Unit = {
      config.typeCheckMode match {
        case TypeCheckMode.Parallel => typeCheckInParallel()
        case TypeCheckMode.Sequential => typeCheckSequentially()
        case TypeCheckMode.Lazy => // don't do anything yet
      }
    }
     */

    def getTypeInfo(importTarget: AbstractImport)(config: Config): TypeCheckResult = {
      val packageTarget = AbstractPackage(importTarget)(config)
      /*
      Violation.violation(typeContextMap.contains(packageTarget), s"expected that a job for ${packageTarget} but found none")
      val fut = typeContextMap(packageTarget)
      Await.result(fut, Duration.Inf)
       */
      /*
      Violation.violation(jobMap.contains(packageTarget), s"expected that a job for ${packageTarget} but found none")
      val job = jobMap(packageTarget)
      config.typeCheckMode match {
        case TypeCheckMode.Lazy => job.call()
        case TypeCheckMode.Sequential =>
          // note that we cannot await the future here as type-checking of this package might not have started yet.
          // Thus, we use `.call()` that either returns a previously calculated type-checking result or will calculate it.
          job.call()
        case TypeCheckMode.Parallel => Await.result(job.getFuture, Duration.Inf)
      }
       */
      /*
      val future = typeCheckManager.getResult(packageTarget)
      Violation.violation(future.isCompleted, s"job $importTarget is not yet completed")
      Await.result(future, Duration.Inf)
       */
      config.typeCheckMode match {
        case Lazy | Sequential => typeCheckManager.getResult(packageTarget)
        case Parallel =>
          val future = parallelTypeCheckManager.getFuture(packageTarget).flatten
          Violation.violation(future.isCompleted, s"job $importTarget is not yet completed")
          Await.result(future, Duration.Inf)
      }
      // typeCheckManager.getResult(packageTarget)
      // val future = typeCheckManager.getFuture(packageTarget).flatten
      // val future = typeCheckManager.getFuture(packageTarget)
      // Violation.violation(future.isCompleted, s"job $importTarget is not yet completed")
      // Await.result(future, Duration.Inf)
    }
  }


  def check(config: Config, abstractPackage: AbstractPackage, parseResults: Map[AbstractPackage, ParseSuccessResult])(executionContext: GobraExecutionContext): TypeCheckResult = {
    // check for cycles
    val cyclicErrors = new CycleChecker(config, parseResults).check(abstractPackage)
    if (cyclicErrors.isEmpty) {
      val typeCheckingStartMs = System.currentTimeMillis()
      // add type-checking jobs to context:
      val context = new Context(config, parseResults)(executionContext)
      val res = context.typeCheck(abstractPackage)
      val durationS = f"${(System.currentTimeMillis() - typeCheckingStartMs) / 1000f}%.1f"
      println(s"type-checking done, took ${durationS}s (in mode ${config.typeCheckMode})")
      // we do not report any messages in this case, because `checkSources` will do so (for each package)
      res
    } else {
      val (sources, pkg) = parseResults(abstractPackage)
      val sourceNames = sources.map(_.name)
      val errors = pkg.positions.translate(cyclicErrors, TypeError).distinct
      config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, errors)
      Left(errors)
    }
    /*
    val pkg = context.parseManager.getResult(pkgInfo)
    val tree = new GoTree(pkg)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree, context, isMainContext)(config: Config)



    val mainPackage = AbstractPackage(RegularImport(""))(config)
    context.parseManager.addIfAbsent(mainPackage, context.SuccessParseJob(sources, pkg))(context.executionContext)

    //val parserMessages = noMessages
    val parserMessages = if (isMainContext) {
      val sequential = false
      val parsingStartMs = System.currentTimeMillis()
      val messages = if (sequential) {
        // parse BuiltInImport
        val builtInParseResult = context.parseRecursively(BuiltInImport)(config)
        val builtInMessages = if (builtInParseResult.isRight) noMessages else message(pkg, s"Parsing package with Gobra's built-in definitions failed. This is an internal bug.")

        val importedPackagesMessages = pkg.imports.flatMap(importNode => {
          val importTarget = RegularImport(importNode.importPath)
          val parseResult = context.parseRecursively(importTarget)(config)
          parseResult.left.map(errs => createImportError(importNode, errs)).left.getOrElse(noMessages)
        })

        builtInMessages ++ importedPackagesMessages
      } else {
        // parse BuiltInImport
        context.parse(BuiltInImport)

        pkg.imports.foreach(importNode => {
          val importTarget = RegularImport(importNode.importPath)
          context.parse(importTarget)
        })

        // collect parse errors and cyclic import errors:
        val builtInParseErrors = context.computeCycles(BuiltInImport)
        val builtInMessages = if (builtInParseErrors.isEmpty) noMessages else message(pkg, s"Parsing package with Gobra's built-in definitions failed. This is an internal bug.")

        val importedPackagesMessages = pkg.imports.flatMap(importNode => {
          val importTarget = RegularImport(importNode.importPath)
          val parseErrors = context.computeCycles(importTarget)
          if (parseErrors.isEmpty) noMessages else createImportError(importNode, parseErrors)
        })

        builtInMessages ++ importedPackagesMessages
      }
      val durationS = f"${(System.currentTimeMillis() - parsingStartMs) / 1000f}%.1f"
      println(s"parsing done, took ${durationS}s, ${messages.length} errors found")
      *//*
      val imports = Vector(BuiltInImport) ++
        pkg.imports.map(i => RegularImport(i.importPath))
      val parseResult = imports.map(importTarget => {
        context.parseRecursively(importTarget)(config)
      })
      val transitiveParseErrors = parseResult.flatMap(res => res.left.getOrElse(Vector.empty))
      if (transitiveParseErrors.nonEmpty) {
        println(s"parse error encountered")
        println(transitiveParseErrors)
        return Left(transitiveParseErrors)
      }
      println(s"parsing was fine")
       *//*

      messages
    } else {
      noMessages
    }

    val typeCheckingStartMs = System.currentTimeMillis()
    if (isMainContext && parserMessages.isEmpty) {
      // context.startTypeChecking()
      context.typeCheck()
      // val fut = context.typeCheck()
      // val importTargets = BuiltInImport +: pkg.imports.map(importNode => RegularImport(importNode.importPath))
      // val typeCheckFutures = importTargets.map(importTarget => context.typeCheck(importTarget))
      // implicit val executor: GobraExecutionContext = context.executionContext
      // Await.ready(Future.sequence(typeCheckFutures), Duration.Inf)
      // Await.ready(fut, Duration.Inf)
    }

    // val errors = info.errors
    val errors = if (parserMessages.nonEmpty) parserMessages else {
      info.errors
    }
    if (isMainContext && errors.isEmpty) {
      val durationS = f"${(System.currentTimeMillis() - typeCheckingStartMs) / 1000f}%.1f"
      println(s"type-checking done, took ${durationS}s (in mode ${config.typeCheckMode})")
    }

    val sourceNames = sources.map(_.name)
    // use `sources` instead of `context.inputs` for reporting such that the message is correctly attributed in case of imports
    config.reporter report TypeCheckDebugMessage(sourceNames, () => pkg, () => getDebugInfo(pkg, info))
    if (errors.isEmpty) {
      config.reporter report TypeCheckSuccessMessage(sourceNames, config.taskName, () => info, () => pkg, () => getErasedGhostCode(pkg, info), () => getGoifiedGhostCode(pkg, info))
      Right(info)
    } else {
      // remove duplicates as errors related to imported packages might occur multiple times
      // consider this: each error in an imported package is converted to an error at the import node with
      // message 'Package <pkg name> contains errors'. If the imported package contains 2 errors then only a single error
      // should be reported at the import node instead of two.
      // however, the duplicate removal should happen after translation so that the error position is correctly
      // taken into account for the equality check.
      val typeErrors = pkg.positions.translate(errors, TypeError).distinct
      config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, typeErrors)
      Left(typeErrors)
    }
     */
  }

  type TypeInfoCacheKey = String
  private val typeInfoCache: ConcurrentMap[TypeInfoCacheKey, TypeInfoImpl] = new ConcurrentHashMap()

  private def getCacheKey(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoCacheKey = {
    // type-checking depends on `typeBounds` and `enableLazyImport`
    val key = pkg.hashCode().toString ++
      dependentTypeInfo.hashCode().toString ++
      (if (isMainContext) "1" else "0") ++
      config.typeBounds.hashCode().toString ++
      (if (config.enableLazyImports) "1" else "0")

    val bytes = MessageDigest.getInstance("MD5").digest(key.getBytes)
    // convert `bytes` to a hex string representation such that we get equality on the key while performing cache lookups
    bytes.map { "%02x".format(_) }.mkString
  }

  def flushCache(): Unit = {
    typeInfoCache.clear()
  }

  def checkSources(sources: Vector[Source], pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean = false)(config: Config): TypeCheckResult = {
    def getTypeInfo(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoImpl = {
      val tree = new GoTree(pkg)
      new TypeInfoImpl(tree, dependentTypeInfo, isMainContext)(config: Config)
    }

    def getTypeInfoCached(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoImpl = {
      typeInfoCache.computeIfAbsent(getCacheKey(pkg, dependentTypeInfo, isMainContext, config), _ => getTypeInfo(pkg, dependentTypeInfo, isMainContext, config))
    }

    val checkFn = if (config.cacheParser) { getTypeInfoCached _ } else { getTypeInfo _ }
    val info = checkFn(pkg, dependentTypeInfo, isMainContext, config)

    val errors = info.errors

    val sourceNames = sources.map(_.name)
    // use `sources` instead of `context.inputs` for reporting such that the message is correctly attributed in case of imports
    config.reporter report TypeCheckDebugMessage(sourceNames, () => pkg, () => getDebugInfo(pkg, info))
    if (errors.isEmpty) {
      config.reporter report TypeCheckSuccessMessage(sourceNames, config.taskName, () => info, () => pkg, () => getErasedGhostCode(pkg, info), () => getGoifiedGhostCode(pkg, info))
      Right(info)
    } else {
      // remove duplicates as errors related to imported packages might occur multiple times
      // consider this: each error in an imported package is converted to an error at the import node with
      // message 'Package <pkg name> contains errors'. If the imported package contains 2 errors then only a single error
      // should be reported at the import node instead of two.
      // however, the duplicate removal should happen after translation so that the error position is correctly
      // taken into account for the equality check.
      val typeErrors = pkg.positions.translate(errors, TypeError).distinct
      config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, typeErrors)
      Left(typeErrors)
    }
  }

  private def getErasedGhostCode(pkg: PPackage, info: TypeInfoImpl): String = {
    new GhostLessPrinter(info).format(pkg)
  }

  private def getGoifiedGhostCode(program: PPackage, info: TypeInfoImpl): String = {
    new GoifyingPrinter(info).format(program)
  }

  private def getDebugInfo(pkg: PPackage, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(pkg)
  }
}
