// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import com.typesafe.scalalogging.LazyLogging
import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{Severities, Source}
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.frontend.{PImport, PNode, PPackage}
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage, BuiltInImport, BuiltInPackage, RegularImport, RegularPackage}
import viper.gobra.frontend.ParserUtils.{ParseResult, ParseSuccessResult}
import viper.gobra.util.TaskManagerMode.{Lazy, Parallel, Sequential}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostLessPrinter, GoifyingPrinter}
import viper.gobra.reporting.{CyclicImportError, NotFoundError, TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, TypeWarning, VerifierError}
import viper.gobra.util.VerifierPhase.{ErrorsAndWarnings, PhaseResult, Warnings}
import viper.gobra.util.{GobraExecutionContext, Job, TaskManager, VerifierPhase, VerifierPhaseNonFinal, Violation}

import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ConcurrentHashMap, ConcurrentMap}
import scala.concurrent.Future

// `LazyLogging` provides us with access to `logger` to emit log messages
object Info extends VerifierPhaseNonFinal[(Config, PackageInfo, Map[AbstractPackage, ParseResult]), TypeInfo] with LazyLogging {

  override val name: String = "Type-checking"

  type GoTree = Tree[PNode, PPackage]
  type TypeCheckSuccessResult = (TypeInfo with ExternalTypeInfo, Warnings)
  type TypeCheckResult = Either[ErrorsAndWarnings, TypeCheckSuccessResult]
  type DependentTypeCheckResult = Map[AbstractImport, TypeCheckResult]
  type DependentTypeInfo = Map[AbstractImport, ExternalTypeInfo]

  trait GetParseResult {
    def parseResults: Map[AbstractPackage, ParseSuccessResult]

    protected def getParseResult(abstractPackage: AbstractPackage): ParseSuccessResult = {
      Violation.violation(parseResults.contains(abstractPackage), s"GetParseResult: expects that $abstractPackage has been parsed")
      parseResults(abstractPackage)
    }
  }

  /** checks whether cyclic import patterns exist in the results produced by the parser. */
  class CycleChecker(val config: Config, val parseResults: Map[AbstractPackage, ParseResult]) {
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var parserPendingPackages: Vector[AbstractImport] = Vector()

    private def getParseResult(abstractPackage: AbstractPackage): ParseResult = {
      Violation.violation(parseResults.contains(abstractPackage), s"GetParseResult: expects that $abstractPackage has been parsed")
      parseResults(abstractPackage)
    }

    def check(abstractPackage: AbstractPackage): Either[ErrorsAndWarnings, Map[AbstractPackage, ParseSuccessResult]] = {
      for {
        parseResult <- getParseResult(abstractPackage)
        (_, ast) = parseResult
        perImportResult = ast.imports.map(importNode => {
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

    private def createImportError(importNode: PImport, errorsInImportedPackage: Vector[VerifierError]): Messages = {
      val (cyclicErrors, nonCyclicErrors) = errorsInImportedPackage.partitionMap {
        case cyclicErr: CyclicImportError => Left(cyclicErr)
        case e => Right(e)
      }
      if (cyclicErrors.isEmpty) {
        message(importNode, s"Package contains ${nonCyclicErrors.length} error(s): ${nonCyclicErrors.map(_.message).mkString(", ")}")
      } else {
        cyclicErrors.flatMap(cycle => message(importNode, cycle.message))
      }
    }
  }

  /**
    * All TypeInfo instances share a single context instance.
    * Therefore, package management is centralized.
    */
  class Context(val config: Config, val parseResults: Map[AbstractPackage, ParseSuccessResult])(implicit executor: GobraExecutionContext) extends GetParseResult {
    private val typeCheckManager = new TaskManager[AbstractPackage, (Vector[Source], PPackage, Vector[AbstractImport]), TypeCheckResult](config.parseAndTypeCheckMode)

    var typeCheckDurationMs = new AtomicLong(0L)

    /**
      * This job creates type-check jobs for all packages imported by the specified package as part of the sequential
      * pre-computations.
      * Then, the parse result is retrieved for the specified package and this result is type-checked. To enable lazy
      * processing and type-checking, this job provides the type-check result as a closure. While the result is eagerly
      * computed in `Sequential` and `Parallel` modes, the result is lazily computed on the first closure call in `Lazy`
      * mode.
      */
    private case class TypeCheckJob(abstractPackage: AbstractPackage, isMainContext: Boolean = false) extends Job[(Vector[Source], PPackage, Vector[AbstractImport]), TypeCheckResult] {
      override def toString: String = s"TypeCheckJob for $abstractPackage"

      protected override def sequentialPrecompute(): (Vector[Source], PPackage, Vector[AbstractImport]) = {
        val (sources, ast) = getParseResult(abstractPackage)
        val importTargets = ast.imports.map(importNode => RegularImport(importNode.importPath))
        val isBuiltIn = abstractPackage == BuiltInPackage
        val dependencies = if (isBuiltIn) importTargets else BuiltInImport +: importTargets
        // schedule type-checking of dependent packages:
        dependencies.foreach(importTarget => {
          val dependentPackage = AbstractPackage(importTarget)(config)
          val job = TypeCheckJob(dependentPackage)
          typeCheckManager.addIfAbsent(dependentPackage, job)
        })
        (sources, ast, dependencies)
      }

      protected def compute(precomputationResult: (Vector[Source], PPackage, Vector[AbstractImport])): TypeCheckResult = {
        val (sources, ast, dependencies) = precomputationResult
        val dependentResults = dependencies.map(importTarget => {
          val dependentPackage = AbstractPackage(importTarget)(config)
          (importTarget, typeCheckManager.getResultBlocking(dependentPackage))
        })
        val typeCheckMode = config.parseAndTypeCheckMode match {
          case Lazy =>
            logger.info(s"Lazy type-checking is no longer supported, and, thus, is performed sequentially")
            Sequential
          case m => m
        }
        typeCheckMode match {
          case Sequential | Parallel =>
            typeCheck(sources, ast, dependentResults.toMap)
          case Lazy =>
            Violation.violation("lazy type-checking is no longer supported")
        }
      }

      private def typeCheck(pkgSources: Vector[Source], pkg: PPackage, dependentTypeInfo: DependentTypeCheckResult, isLazy: Boolean = false): TypeCheckResult = {
        val startMs = System.currentTimeMillis()
        logger.trace(s"start type-checking ${pkg.info.id}")
        val res = Info.checkSources(pkgSources, pkg, dependentTypeInfo, isMainContext = isMainContext)(config)
        logger.trace {
          val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
          s"type-checking ${pkg.info.id} done (took ${durationS}s with ${res.map { case (info, _) => info.tree.nodes.length.toString }.getOrElse("_")} nodes)"
        }
        if (isLazy) {
          typeCheckDurationMs.getAndUpdate(prev => Math.max(prev, System.currentTimeMillis() - startMs))
        } else {
          typeCheckDurationMs.addAndGet(System.currentTimeMillis() - startMs)
        }
        res
      }
    }

    def typeCheck(pkg: AbstractPackage): Future[TypeCheckResult] = {
      typeCheckManager.addIfAbsent(pkg, TypeCheckJob(pkg, isMainContext = true))
      typeCheckManager.getResult(pkg)
    }
  }

  override protected def execute(input: (Config, PackageInfo, Map[AbstractPackage, ParseResult]))(implicit executor: GobraExecutionContext): PhaseResult[TypeInfo] = {
    val (config, pkgInfo, parseResults) = input
    val abstractPackage = RegularPackage(pkgInfo.id)
    for {
      // check whether parsing of this package was successful:
      parseResult <- EitherT.fromEither(Future.successful[Either[ErrorsAndWarnings, ParseSuccessResult]](parseResults(abstractPackage)))
      // check whether there are any import cycles:
      cycleResult <- EitherT.fromEither(Future.successful(new CycleChecker(config, parseResults).check(abstractPackage)))
        .leftMap(errorsAndWarnings => {
          val (sources, pkg) = parseResult
          reportTypeCheckFailure(config, sources, pkg, errorsAndWarnings)
          errorsAndWarnings
        })
      typeCheckingStartMs = System.currentTimeMillis()
      context = new Context(config, cycleResult)
      typeInfo <- EitherT.fromEither(context.typeCheck(abstractPackage))
      _ = logger.debug {
        val typeCheckingEndMs = System.currentTimeMillis()
        val sumDurationS = f"${context.typeCheckDurationMs.get() / 1000f}%.1f"
        val overheadMs = (typeCheckingEndMs - typeCheckingStartMs) - context.typeCheckDurationMs.get()
        val overheadS = f"${overheadMs / 1000f}%.1f"
        s"type-checking individual packages took ${sumDurationS}s. Overhead for tasks is thus ${overheadS}s (${(100f * overheadMs / (typeCheckingEndMs - typeCheckingStartMs)).toInt}%)"
      }
    } yield typeInfo
  }

  type TypeInfoCacheKey = String
  private val typeInfoCache: ConcurrentMap[TypeInfoCacheKey, TypeInfoImpl] = new ConcurrentHashMap()

  private def getCacheKey(pkg: PPackage, dependentTypeInfo: DependentTypeInfo, isMainContext: Boolean, config: Config): TypeInfoCacheKey = {
    // the cache key only depends on config's `typeBounds`, `int32bit`, and `enableLazyImport`
    val pkgKey = pkg.hashCode().toString
    // the computed key must be deterministic!
    val dependentTypeInfoKey = dependentTypeInfo
      .toVector
      .map { case (pkg, info) => pkg.hashCode().toString ++ info.hashCode().toString }
      .sorted
      .mkString("")
    val isMainContextKey = if (isMainContext) "1" else "0"
    val configKey = config.typeBounds.hashCode().toString ++
      (if (config.int32bit) "1" else "0") ++
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

  def checkSources(sources: Vector[Source], pkg: PPackage, dependentTypeInfo: DependentTypeCheckResult, isMainContext: Boolean = false)(config: Config): TypeCheckResult = {
    var cacheHit: Boolean = true
    def getTypeInfo(pkg: PPackage, dependentTypeInfo: DependentTypeInfo, isMainContext: Boolean, config: Config): TypeInfoImpl = {
      cacheHit = false
      val tree = new GoTree(pkg)
      new TypeInfoImpl(tree, dependentTypeInfo, isMainContext)(config: Config)
    }

    def getTypeInfoCached(pkg: PPackage, dependentTypeInfo: DependentTypeInfo, isMainContext: Boolean, config: Config): TypeInfoImpl =
      typeInfoCache.computeIfAbsent(getCacheKey(pkg, dependentTypeInfo, isMainContext, config), _ => getTypeInfo(pkg, dependentTypeInfo, isMainContext, config))

    // we type-check this package only if all imported packages got type-checked successfully
    val (failedDependents, successfulDependents) = dependentTypeInfo.partitionMap {
      case (importTarget, res) => res.fold(errs => Left(importTarget, errs), successRes => Right(importTarget, successRes))
    }

    val sourceNames = sources.map(_.name)
    val startTimeMs = System.currentTimeMillis()
    val result = if (failedDependents.isEmpty) {
      // extract the type infos per dependent package:
      val dependentInfos = successfulDependents.map {
        case (importTarget, (info, _)) => (importTarget, info)
      }
      // create warning messages at the corresponding import node for warnings in imported packages:
      val importedMessages = successfulDependents.flatMap {
        case (importTarget, (_, warnings)) => createImportMessages(pkg, importTarget, warnings)
      }
      val checkFn = if (config.cacheParserAndTypeChecker) { getTypeInfoCached _ } else { getTypeInfo _ }
      val info = checkFn(pkg, dependentInfos.toMap, isMainContext, config)
      if (!cacheHit && config.cacheParserAndTypeChecker) {
        logger.trace(s"No cache hit for type info for ${pkg.info.id}")
      }

      config.reporter report TypeCheckDebugMessage(sourceNames, () => pkg, () => getDebugInfo(pkg, info))
      convertToTypeErrorsAndWarnings(pkg, info.messages ++ importedMessages)
        .map(warnings => (info, warnings))
    } else {
      val dependentMsgs = failedDependents.flatMap {
        case (importTarget, errsAndWarnings) => createImportMessages(pkg, importTarget, errsAndWarnings)
      }.toVector
      convertToTypeErrorsAndWarnings(pkg, dependentMsgs)
        .map(_ => Violation.violation("a failed dependent should always result in an error in the importing package"))
    }

    logger.trace {
      val durationS = f"${(System.currentTimeMillis() - startTimeMs) / 1000f}%.1f"
      s"computing errors for ${pkg.info.id} done, took ${durationS}s"
    }

    result match {
      case Right((info, warnings)) => reportTypeCheckSuccess(config, sources, pkg, info, warnings)
      case Left(errorsAndWarnings) => reportTypeCheckFailure(config, sources, pkg, errorsAndWarnings)
    }
    result
  }

  private def createImportMessages(pkg: PPackage, importTarget: AbstractImport, errsAndWarnings: ErrorsAndWarnings): Messages = {
    if (errsAndWarnings.isEmpty) {
      noMessages
    } else {
      /** `msgNode` is the AST node that is used to attach the message to in the package that imports `importTarget` */
      val msgNode: PNode = importTarget match {
        case BuiltInImport =>
          // since built-in imports by definition do not have a corresponding import clause, we simply pick
          // one of the package clauses to attach the error to:
          pkg.packageClause
        case RegularImport(importPath) =>
          // a package might import `importTarget` multiple times, e.g., in every file of the package
          // we (randomly) pick on of the import nodes to report the error to avoid reporting `errsAndWarnings` multiple times
          pkg.imports.collectFirst {
            case importNode if importNode.importPath == importPath => importNode
          }.getOrElse(Violation.violation(s"could not find import node for import path $importPath"))
      }

      // create an error message located at the import statement to indicate errors and/or warnings in the imported package
      // we distinguish between regular errors and packages whose source files could not be found (note that cyclic
      // errors are handled before type-checking)
      // I.e., we report a NotFound error if there is one or alternatively propagate `errsAndWarnings`:
      val notFoundErr = errsAndWarnings.collectFirst { case e: NotFoundError => e }
      notFoundErr.map(e => message(msgNode, e.message))
        .getOrElse {
          val indentation = " " * 4
          val formattedImportedErrsAndWarnings = errsAndWarnings.map(err =>
            // indent each line of the formatted error by `indentation`:
            err.formattedMessage.linesWithSeparators.map(l => s"$indentation$l").mkString
          ).mkString("\n")
          val hasWarnings = errsAndWarnings.exists(_.severity == Severities.Warning)
          val hasErrors = errsAndWarnings.exists(_.severity == Severities.Error)
          val messageTypes = if (hasWarnings && hasErrors) "warnings and errors"
            else if (hasWarnings) "warnings"
            else "errors"
          message(msgNode,
            s"Package '$importTarget' contains $messageTypes:\n$formattedImportedErrsAndWarnings",
            if (hasErrors) Severities.Error else Severities.Warning)
        }
    }
  }

  private def convertToTypeErrorsAndWarnings(pkg: PPackage, msgs: Messages): Either[ErrorsAndWarnings, Warnings] = {
    val (warningMsgs, errorMsgs) = msgs.partition(_.severity == Severities.Warning)

    // the type checker sometimes produces duplicate errors, which we remove here (e.g., when type checking
    // a program with duplicate identifiers such as `globals/globals-type-fail04.gobra`).
    // duplicate removal should happen after translation so that the error position is correctly
    // taken into account for the equality check.
    // issue #857 keeps track that we should eventually get rid of `distinct` here.
    val warnings = pkg.positions.translate(warningMsgs, TypeWarning).distinct
    val errors = pkg.positions.translate(errorMsgs, TypeError).distinct
    if (errors.isEmpty) Right(warnings) else Left(errors ++ warnings)
  }

  private def reportTypeCheckSuccess(config: Config, sources: Vector[Source], pkg: PPackage, info: TypeInfoImpl, warnings: Warnings): Unit = {
    val sourceNames = sources.map(_.name)
    config.reporter report TypeCheckSuccessMessage(sourceNames, config.taskName, () => info, () => pkg, () => getErasedGhostCode(pkg, info), () => getGoifiedGhostCode(pkg, info), warnings)
  }

  private def reportTypeCheckFailure(config: Config, sources: Vector[Source], pkg: PPackage, errorsAndWarnings: ErrorsAndWarnings): Unit = {
    val sourceNames = sources.map(_.name)
    val (errors, warnings) = VerifierPhase.splitErrorsAndWarnings(errorsAndWarnings)
    config.reporter report TypeCheckFailureMessage(sourceNames, pkg.packageClause.id.name, () => pkg, errors, warnings)
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
