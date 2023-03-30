// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import com.typesafe.scalalogging.LazyLogging
import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import org.bitbucket.inkytonik.kiama.util.{Position, Source}
import viper.gobra.ast.frontend.{PImport, PNode, PPackage}
import viper.gobra.frontend.{Config, Job, ParallelJob, ParallelTaskManager, TaskManager}
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage, BuiltInImport, BuiltInPackage, RegularImport}
import viper.gobra.frontend.Parser.{ParseResult, ParseSuccessResult}
import viper.gobra.frontend.TaskManagerMode.{Lazy, Parallel, Sequential}
import viper.gobra.frontend.info.Info.CycleChecker
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostLessPrinter, GoifyingPrinter}
import viper.gobra.reporting.{CyclicImportError, ParserError, TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.security.MessageDigest
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Info extends LazyLogging {

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

  class CycleChecker(val config: Config /*, val parseResults: Map[AbstractPackage, ParseSuccessResult]*/, val parseResults: Map[AbstractPackage, ParseResult]) {
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var parserPendingPackages: Vector[AbstractImport] = Vector()

    private def getParseResult(abstractPackage: AbstractPackage): Either[Vector[ParserError], ParseSuccessResult] = {
      Violation.violation(parseResults.contains(abstractPackage), s"GetParseResult: expects that $abstractPackage has been parsed")
      parseResults(abstractPackage)
    }

    def check(abstractPackage: AbstractPackage): Either[Vector[VerifierError], Map[AbstractPackage, ParseSuccessResult]] = {
      for {
        parseResult <- getParseResult(abstractPackage)
        (_, ast) = parseResult
        perImportResult = ast.imports.map(importNode => {
          // val cycles = getCycles(RegularImport(importNode.importPath))
          /*
          val cycles = getImportErrors(RegularImport(importNode.importPath))
          val msgs = createImportError(importNode, cycles)
          if (msgs.isEmpty) Right(()) else Left(ast.positions.translate(msgs, TypeError).distinct)
           */
          val res = getImportErrors(RegularImport(importNode.importPath))
            .left
            .map(errs => {
              val msgs = createImportError(importNode, errs)
              ast.positions.translate(msgs, TypeError).distinct
            })
          res
        })
        (errs, _) = perImportResult.partitionMap(identity)
        _ <- if (errs.nonEmpty) Left(errs.flatten) else Right(())
        successParseResults = parseResults.collect {
          case (key, Right(res)) => (key, res)
        }
      } yield successParseResults
      /*
      val (_, ast) = getParseResult(abstractPackage)
      val msgs = ast.imports.flatMap(importNode => {
        val cycles = getCycles(RegularImport(importNode.importPath))
        createImportError(importNode, cycles)
      })
      if (msgs.isEmpty) {

      } else {
        Left(ast.positions.translate(msgs, TypeError).distinct)
      }
      */
    }

    /**
      * returns all parser errors and cyclic errors transitively found in imported packages
      */
    private def getImportErrors(importTarget: AbstractImport): Either[Vector[VerifierError], Unit] = {
      parserPendingPackages = parserPendingPackages :+ importTarget
      val abstractPackage = AbstractPackage(importTarget)(config)
      val res = for {
        parseResult <- getParseResult(abstractPackage)
        (_, ast) = parseResult
        perImportResult = ast.imports.map(importNode => {
          val directlyImportedTarget = RegularImport(importNode.importPath)
          if (parserPendingPackages.contains(directlyImportedTarget)) {
            // package cycle detected
            val importNodeStart = ast.positions.positions.getStart(importNode)
            // Vector(ImportCycle(importNode, importNodeStart, parserPendingPackages))
            val msg = s"Package '$importTarget' is part of the following import cycle that involves the import $importNode$importNodeStart: ${parserPendingPackages.mkString("[", ", ", "]")}"
            Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$msg'")))
          } else {
            getImportErrors(directlyImportedTarget)
          }
        })
        (errs, _) = perImportResult.partitionMap(identity)
        res <- if (errs.nonEmpty) Left(errs.flatten) else Right(())
      } yield res
      parserPendingPackages = parserPendingPackages.filterNot(_ == importTarget)
      res
    }
      /*
    private def getCycles(importTarget: AbstractImport): Vector[ImportCycle] = {
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
       */

    private def createImportError(importNode: PImport, errorsInImportedPackage: Vector[VerifierError]): Messages = {
      val importTarget = RegularImport(importNode.importPath)
      val (cyclicErrors, nonCyclicErrors) = errorsInImportedPackage.partitionMap {
        case cyclicErr: CyclicImportError => Left(cyclicErr)
        case e => Right(e)
      }
      if (cyclicErrors.isEmpty) {
        // nonCyclicErrors.flatMap(err => message(importNode, err.message))
        message(importNode, s"Package contains ${nonCyclicErrors.length} error(s): ${nonCyclicErrors.map(_.message).mkString(", ")}")
      } else {
        cyclicErrors.flatMap(cycle => {
          // val positionalInfo = cycle.importNodeStart.map(pos => s" at ${pos.format}").getOrElse("")
          // message(importNode, s"Package '$importTarget' is part of the following import cycle that involves the import ${cycle.importNodeCausingCycle}$positionalInfo: ${cycle.cyclicPackages.mkString("[", ", ", "]")}")
          message(importNode, cycle.message)
        })
      }/*
      cycles.flatMap(cycle => {
        val positionalInfo = cycle.importNodeStart.map(pos => s" at ${pos.format}").getOrElse("")
        message(importNode, s"Package '$importTarget' is part of the following import cycle that involves the import ${cycle.importNodeCausingCycle}$positionalInfo: ${cycle.cyclicPackages.mkString("[", ", ", "]")}")
      })*/
    }
  }

  /**
    * All TypeInfo instances share a single context instance.
    * Therefore, package management is centralized.
    */
  class Context(val config: Config, val parseResults: Map[AbstractPackage, ParseSuccessResult])(val executionContext: GobraExecutionContext) extends GetParseResult {
    private val typeCheckManager = new TaskManager[AbstractPackage, TypeCheckResult](if (config.typeCheckMode == Parallel) Sequential else config.typeCheckMode)
    private val parallelTypeCheckManager = new ParallelTaskManager[AbstractPackage, TypeCheckResult]()

    trait TypeCheckJob {
      protected def typeCheck(pkgSources: Vector[Source], pkg: PPackage, dependentTypeInfo: DependentTypeInfo, isMainContext: Boolean = false): TypeCheckResult = {
        val startMs = System.currentTimeMillis()
        logger.trace(s"start type-checking ${pkg.info.id}")
        val res = Info.checkSources(pkgSources, pkg, dependentTypeInfo, isMainContext = isMainContext)(config)
        logger.trace {
          val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
          s"type-checking ${pkg.info.id} done (took ${durationS}s with ${res.map(info => info.tree.nodes.length.toString).getOrElse("_")} nodes)"
        }
        res
      }
    }

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
            typeCheckManager.addIfAbsent(dependentPackage, job)
            (importTarget, () => typeCheckManager.getResult(dependentPackage))
          })

        typeCheck(sources, ast, dependentTypeInfos.toMap, isMainContext = isMainContext)
      }
    }

    // case class ParallelTypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends Job[Future[TypeCheckResult]] with TypeCheckJob {
    case class ParallelTypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends ParallelJob[TypeCheckResult] with TypeCheckJob {
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
              .map(typeInfo => (importTarget, () => typeInfo))(executionContext)
            /*
            parallelTypeCheckManager.getFuture(dependentPackage).flatten
              .map(typeInfo => (importTarget, () => typeInfo))(executionContext)
            */
          })
        implicit val executor: GobraExecutionContext = executionContext
        val dependentJobsFut = Future.sequence(dependentJobsFuts)
        dependentJobsFut.map(dependentTypeInfo => typeCheck(sources, ast, dependentTypeInfo.toMap, isMainContext = isMainContext))
      }
    }

    case class FailureJob(errs: Vector[VerifierError]) extends Job[TypeCheckResult] {
      override def compute(): TypeCheckResult = Left(errs)
    }

    def typeCheck(pkg: AbstractPackage): TypeCheckResult = {
      config.typeCheckMode match {
        case Lazy =>
          // we have to transitively add all packages to the typeCheckManager:
          lazyTypeCheckRecursively(pkg, isMainContext = true)
          typeCheckManager.getResult(pkg)
        case Sequential =>
          typeCheckManager.addIfAbsent(pkg, SequentialTypeCheckJob(pkg, isMainContext = true))
          typeCheckManager.getResult(pkg)
        case Parallel =>
          /*
          parallelTypeCheckManager.addIfAbsent(pkg, ParallelTypeCheckJob(pkg, isMainContext = true))(executionContext)
          // wait for result:
          val fut = parallelTypeCheckManager.getResult(pkg)
          */
          val fut = parallelTypeCheckManager.addIfAbsent(pkg, ParallelTypeCheckJob(pkg, isMainContext = true))(executionContext)
          Await.result(fut, Duration.Inf)
      }
    }

    private def lazyTypeCheckRecursively(abstractPackage: AbstractPackage, isMainContext: Boolean): Unit = {
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
      dependentPackages.foreach(pkg => typeCheckManager.addIfAbsent(pkg, LazyTypeCheckJob(pkg)))
      // create job for this package:
      typeCheckManager.addIfAbsent(abstractPackage, LazyTypeCheckJob(abstractPackage, isMainContext = isMainContext))
    }
    /*
    def getContexts: Iterable[ExternalTypeInfo] = {
      config.typeCheckMode match {
        case Lazy | Sequential => typeCheckManager.getAllResults(executionContext).collect { case Right(info) => info }
        case Parallel =>
          implicit val executor: GobraExecutionContext = executionContext
          val results = Await.result(Future.sequence(parallelTypeCheckManager.getAllResults(executionContext)), Duration.Inf)
          results.collect { case Right(info) => info }
      }
    }

    def getTypeInfo(importTarget: AbstractImport)(config: Config): TypeCheckResult = {
      val packageTarget = AbstractPackage(importTarget)(config)
      config.typeCheckMode match {
        case Lazy | Sequential => typeCheckManager.getResult(packageTarget)
        case Parallel =>
          val future = parallelTypeCheckManager.getFuture(packageTarget).flatten
          Violation.violation(future.isCompleted, s"job $importTarget is not yet completed")
          Await.result(future, Duration.Inf)
      }
    }
    */
  }

  def check(config: Config, abstractPackage: AbstractPackage, /*parseResults: Map[AbstractPackage, ParseSuccessResult]*/ parseResults: Map[AbstractPackage, ParseResult])(executionContext: GobraExecutionContext): TypeCheckResult = {
    // check for cycles
    // val cyclicErrors = new CycleChecker(config, parseResults).check(abstractPackage)
    for {
      successParseResult <- new CycleChecker(config, parseResults).check(abstractPackage)
        .left.map(errs => {
          val (sources, pkg) = parseResults(abstractPackage).right.get
          val sourceNames = sources.map(_.name)
          config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, errs)
          errs
        })
      typeCheckingStartMs = System.currentTimeMillis()
      context = new Context(config, successParseResult)(executionContext)
      typeInfo <- context.typeCheck(abstractPackage)
      _ = logger.debug {
        val durationS = f"${(System.currentTimeMillis() - typeCheckingStartMs) / 1000f}%.1f"
        s"type-checking done, took ${durationS}s (in mode ${config.typeCheckMode})"
      }
    } yield typeInfo


    /*
    if (cyclicErrors.isEmpty) {
      val typeCheckingStartMs = System.currentTimeMillis()
      // add type-checking jobs to context:
      val context = new Context(config, parseResults)(executionContext)
      val res = context.typeCheck(abstractPackage)
      logger.debug {
        val durationS = f"${(System.currentTimeMillis() - typeCheckingStartMs) / 1000f}%.1f"
        s"type-checking done, took ${durationS}s (in mode ${config.typeCheckMode})"
      }
      // we do not report any messages in this case, because `checkSources` will do so (for each package)
      res
    } else {
      val (sources, pkg) = parseResults(abstractPackage)
      val sourceNames = sources.map(_.name)
      val errors = pkg.positions.translate(cyclicErrors, TypeError).distinct
      config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, errors)
      Left(errors)
    }
     */
  }

  type TypeInfoCacheKey = String
  private val typeInfoCache: ConcurrentMap[TypeInfoCacheKey, TypeInfoImpl] = new ConcurrentHashMap()

  private def getCacheKey(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoCacheKey = {
    // the cache key only depends on config's `typeBounds` and `enableLazyImport`
    val pkgKey = pkg.hashCode().toString
    // the computed key must be deterministic!
    val dependentTypeInfoKey = dependentTypeInfo
      .toVector
      .map { case (pkg, fn) => pkg.hashCode().toString ++ fn().hashCode().toString }
      .sorted
      .mkString("")
    val isMainContextKey = if (isMainContext) "1" else "0"
    val configKey = config.typeBounds.hashCode().toString ++
      (if (config.enableLazyImports) "1" else "0")

    val key = pkgKey ++
      dependentTypeInfoKey ++
      isMainContextKey ++
      configKey

    val bytes = MessageDigest.getInstance("MD5").digest(key.getBytes)
    // convert `bytes` to a hex string representation such that we get equality on the key while performing cache lookups
    bytes.map { "%02x".format(_) }.mkString
  }

  def flushCache(): Unit = {
    typeInfoCache.clear()
  }

  def checkSources(sources: Vector[Source], pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean = false)(config: Config): TypeCheckResult = {
    var cacheHit: Boolean = true
    def getTypeInfo(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoImpl = {
      cacheHit = false
      val tree = new GoTree(pkg)
      new TypeInfoImpl(tree, dependentTypeInfo, isMainContext)(config: Config)
    }

    def getTypeInfoCached(pkg: PPackage, dependentTypeInfo: Map[AbstractImport, () => Either[Vector[VerifierError], ExternalTypeInfo]], isMainContext: Boolean, config: Config): TypeInfoImpl = {
      typeInfoCache.computeIfAbsent(getCacheKey(pkg, dependentTypeInfo, isMainContext, config), _ => getTypeInfo(pkg, dependentTypeInfo, isMainContext, config))
    }

    val checkFn = if (config.cacheParser) { getTypeInfoCached _ } else { getTypeInfo _ }
    val info = checkFn(pkg, dependentTypeInfo, isMainContext, config)
    if (!cacheHit && config.cacheParser) {
      println(s"No cache hit for type info for ${pkg.info.id}")
    }

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
