// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.ast.frontend.{PNode, PPackage}
import viper.gobra.frontend.Config
import viper.gobra.frontend.PackageResolver.{AbstractImport, AbstractPackage}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.{GhostLessPrinter, GoifyingPrinter}
import viper.gobra.reporting.{CyclicImportError, TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

import scala.collection.immutable.ListMap

object Info {

  type GoTree = Tree[PNode, PPackage]

  /**
    * All TypeInfo instances share a single context instance.
    * Therefore, package management is centralized.
    */
  class Context {
    /** stores the results of all imported packages that have been parsed and type checked so far */
    private var contextMap: Map[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]] = ListMap[AbstractPackage, Either[Vector[VerifierError], ExternalTypeInfo]]()
    /** keeps track of the package dependencies that are currently resolved. This information is used to detect cycles */
    private var pendingPackages: Vector[AbstractImport] = Vector()
    /** stores all cycles that have been discovered so far */
    private var knownImportCycles: Set[Vector[AbstractImport]] = Set()

    // TODO: justify it being here
    // TODO: doc, improve name
    // Can be probably be removed from here again and make it a singleton in desugar.scala
    object ImportsCollector { // TODO: the TypeInfo is used to desugar the pres
      import viper.gobra.ast.{internal => in}

      private var importPreconditions: Vector[(PPackage, Vector[in.Assertion])] = Vector.empty
      private var packagePosts: Vector[(PPackage, Vector[in.Assertion])] = Vector.empty

      def addImportPres(pkg: PPackage, desugaredImportPre: Vector[in.Assertion]): Unit = {
        // println(s"Inserting $desugaredImportPre in ${pkg.info.name}")
        importPreconditions :+= (pkg, desugaredImportPre)
        // println(s"importPreconditions:")
      }

      def presOfPackage(pkg: PPackage): Vector[in.Assertion] = {
        // println(s"pkg: ${pkg.info.name}")
        // println(s"importPreconditions on presOfPackages: ${importPreconditions.map{case (a, b) => (a.info.name, b)}}")
        val result = importPreconditions.filter(_._1.info.id == pkg.info.id).flatMap(_._2)
        // println(s"result: $result")
        result
      }

      def addPackagePosts(pkg: PPackage, desugaredPosts: Vector[in.Assertion]): Unit = {
        //println(s"Inserting $desugaredImportPre in ${pkg.info.name}")
        packagePosts :+= (pkg, desugaredPosts)
        //println(s"importPreconditions: ${importPreconditions.map{case (a, b) => (a.info.name, b)}}")
      }

      def postsOfPackage(pkg: PPackage): Vector[in.Assertion] = {
        packagePosts.filter(_._1.info.id == pkg.info.id).flatMap(_._2)
      }

      def registeredPackages(): Vector[PPackage] = {
        // the domain of package posts should have all registered packages
        packagePosts.map(_._1).distinct
      }

      def debug() = {
        println(s"importPreconditions: ${importPreconditions.map{case (a, b) => (a.info.name, b)}}")
        println(s"packagePostconditions: ${packagePosts.toVector.map{case (a, b) => (a.info.name, b)}}")
      }
    }

    def addPackage(importTarget: AbstractImport, typeInfo: ExternalTypeInfo)(config: Config): Unit = {
      val packageTarget = AbstractPackage(importTarget)(config)
      pendingPackages = pendingPackages.filterNot(_ == importTarget)
      contextMap = contextMap + (packageTarget -> Right(typeInfo))
    }

    def addErrenousPackage(importTarget: AbstractImport, errors: Vector[VerifierError])(config: Config): Unit = {
      val packageTarget = AbstractPackage(importTarget)(config)
      pendingPackages = pendingPackages.filterNot(_ == importTarget)
      contextMap = contextMap + (packageTarget -> Left(errors))
    }

    def getTypeInfo(importTarget: AbstractImport)(config: Config): Option[Either[Vector[VerifierError], ExternalTypeInfo]] = {
      val packageTarget = AbstractPackage(importTarget)(config)
      contextMap.get(packageTarget) match {
        case s@Some(_) => s
        case _ => {
          // there is no entry yet and package resolution might need to resolve multiple depending packages
          // keep track of these packages in pendingPackages until either type information or an error is added to contextMap
          if (pendingPackages.contains(importTarget)) {
            // package cycle detected
            knownImportCycles += pendingPackages
            Some(Left(Vector(CyclicImportError(s"Cyclic package import detected starting with package '$packageTarget'"))))
          } else {
            pendingPackages = pendingPackages :+ importTarget
            None
          }
        }
      }
    }

    /**
      * Returns all package names that lie on the cycle of imports or none if no cycle was found
      */
    def getImportCycle(importTarget: AbstractImport): Option[Vector[AbstractImport]] = knownImportCycles.find(_.contains(importTarget))

    def getContexts: Iterable[ExternalTypeInfo] = contextMap.values.collect { case Right(info) => info }

    def getExternalErrors: Vector[VerifierError] = contextMap.values.collect { case Left(errs) => errs }.flatten.toVector
  }

  def check(pkg: PPackage, sources: Vector[Source], context: Context = new Context, isMainContext: Boolean = false)(config: Config): Either[Vector[VerifierError], TypeInfo with ExternalTypeInfo] = {
    val tree = new GoTree(pkg)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree, context, isMainContext)(config: Config)

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
