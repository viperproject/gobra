// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{Position, Source}
import viper.gobra.ast.frontend.{PImport, PNode, PPackage}
import viper.gobra.frontend.{Config, Job, PackageResolver, Parser, Source, TaskManager}
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage, BuiltInImport, RegularImport, RegularPackage}
import viper.gobra.frontend.info.Info.TypeCheckMode.TypeCheckMode
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostLessPrinter, GoifyingPrinter}
import viper.gobra.reporting.{CyclicImportError, NotFoundError, TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}
import viper.gobra.util.{GobraExecutionContext, Violation}

import java.util.concurrent.Callable
import scala.collection.immutable.ListMap
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success}

object Info {

  type GoTree = Tree[PNode, PPackage]

  /**
    * ImportCycle describes a cyclic import. `importClosingCycle` is the AST node that closes the cycle and
    * `cyclicPackages` stores the packages involved in the cycle.
    */
  case class ImportCycle(importNodeCausingCycle: PImport, importNodeStart: Option[Position], cyclicPackages: Vector[AbstractImport])

  object TypeCheckMode extends Enumeration {
    type TypeCheckMode = Value
    val Lazy, Sequential, Parallel = Value
  }

  /**
    * All TypeInfo instances share a single context instance.
    * Therefore, package management is centralized.
    */
  class Context(executionContext: GobraExecutionContext, config: Config) {
    /** stores the results of all imported packages that have been parsed so far */
    private var parserContextMap: Map[AbstractPackage, Either[Vector[VerifierError], (Vector[Source], PPackage)]] = ListMap()
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var parserPendingPackages: Vector[AbstractImport] = Vector()
    /** stores all cycles that have been discovered so far */
    var parserKnownImportCycles: Set[ImportCycle] = Set()

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

    /**
      * returns all parser errors and cyclic errors transitively found in imported packages
      */
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
    def getParserImportCycle(importTarget: AbstractImport): Option[ImportCycle] =
      parserKnownImportCycles.find(_.cyclicPackages.contains(importTarget))

    case class ParseJob(importTarget: AbstractImport) extends Job[Either[Vector[VerifierError], (Vector[Source], PPackage)]] {
      def compute(): Either[Vector[VerifierError], (Vector[Source], PPackage)] = {
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
          parsedProgram <- Parser.parse(nonEmptyPkgSources, Source.getPackageInfo(nonEmptyPkgSources.head, config.projectRoot), specOnly = true)(config)
          durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
          _ = println(s"parsing $importTarget done (took ${durationS}s)")
          // submit jobs to parse dependent packages:
          _ = parsedProgram.imports.foreach(importNode => {
            val directImportTarget = RegularImport(importNode.importPath)
            val directImportPackage = AbstractPackage(directImportTarget)(config)
            parseManager.addIfAbsent(directImportPackage, ParseJob(directImportTarget))(executionContext)
          })
        } yield (pkgSources, parsedProgram)
      }
    }

    val parseManager = new TaskManager[AbstractPackage, Either[Vector[VerifierError], (Vector[Source], PPackage)]](config)
    def parse(importTarget: AbstractImport): Unit = {
      val abstractPackage = AbstractPackage(importTarget)(config)
      val parseJob = ParseJob(importTarget)
      parseManager.addIfAbsent(abstractPackage, parseJob)(executionContext)
    }

    case class TypeCheckJob(pkgSources: Vector[Source], pkg: PPackage, context: Context) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] {
      def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = {
        println(s"start type-checking ${pkg.info.id}")
        val startMs = System.currentTimeMillis()
        val res = Info.check(pkg, pkgSources, context)(config)
        val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
        println(s"type-checking ${pkg.info.id} done (took ${durationS}s)")
        res
      }
    }

    case class FailureJob(errs: Vector[VerifierError]) extends Job[Either[Vector[VerifierError], ExternalTypeInfo]] {
      def compute(): Either[Vector[VerifierError], ExternalTypeInfo] = Left(errs)
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
    private val typeCheckManager = new TaskManager[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]](config)
    def typeCheck(): Unit = {
      /*parserContextMap.foreach { case (abstractPackage, parseResult) =>
        val typeCheckJob = parseResult match {
          case Right((pkgSources, pkg)) => TypeCheckJob(pkgSources, pkg, this)
          case Left(errs) => FailureJob(errs)
        }
        println(s"adding task $abstractPackage")
        typeCheckManager.addIfAbsent(abstractPackage, typeCheckJob)(executionContext)
      }
      */
      parseManager.getAllResultsWithKeys.foreach { case (abstractPackage, parseResult) =>
        val typeCheckJob = parseResult match {
          case Right((pkgSources, pkg)) => TypeCheckJob(pkgSources, pkg, this)
          case Left(errs) => FailureJob(errs)
        }
        println(s"adding task $abstractPackage")
        // since we're adding the packages in arbitrary order, we cannot directly run the task in SEQUENTIAL mode because
        // depending packages might not have been inserted yet
        typeCheckManager.addIfAbsent(abstractPackage, typeCheckJob, insertOnly = config.typeCheckMode == TypeCheckMode.Sequential)(executionContext)
      }
    }
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

    def getContexts: Iterable[ExternalTypeInfo] =
      // typeContextMap.values.map(fut => Await.result(fut, Duration.Inf)).collect { case Right(info) => info }
      typeCheckManager.getAllResults.collect { case Right(info) => info }
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

    def getTypeInfo(importTarget: AbstractImport)(config: Config): Either[Vector[VerifierError], ExternalTypeInfo] = {
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
      typeCheckManager.getResult(packageTarget)
    }
  }


    def check(pkg: PPackage, sources: Vector[Source], context: Context /*= new Context()*/, isMainContext: Boolean = false)(config: Config): Either[Vector[VerifierError], TypeInfo with ExternalTypeInfo] = {
    val tree = new GoTree(pkg)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree, context, isMainContext)(config: Config)

    def createImportError(importNode: PImport, errs: Vector[VerifierError]): Messages = {
      val importTarget = RegularImport(importNode.importPath)
      // create an error message located at the import statement to indicate errors in the imported package
      // we distinguish between parse and type errors, cyclic imports, and packages whose source files could not be found
      val notFoundErr = errs.collectFirst { case e: NotFoundError => e }
      // alternativeErr is a function to compute the message only when needed
      val alternativeErr = () => context.getParserImportCycle(importTarget) match {
        case Some(cycle) =>
          val positionalInfo = cycle.importNodeStart.map(pos => s" at ${pos.format}").getOrElse("")
          message(importNode, s"Package '$importTarget' is part of the following import cycle that involves the import ${cycle.importNodeCausingCycle}${positionalInfo}: ${cycle.cyclicPackages.mkString("[", ", ", "]")}")
        case _ => message(importNode, s"Package '$importTarget' contains errors: $errs")
      }
      notFoundErr.map(e => message(importNode, e.message))
        .getOrElse(alternativeErr())
    }

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
      /*
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
       */

      messages
    } else {
      noMessages
    }

    val typeCheckingStartMs = System.currentTimeMillis()
    if (isMainContext && parserMessages.isEmpty) {
      // context.startTypeChecking()
      context.typeCheck()
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
