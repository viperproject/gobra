// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra

import org.scalatest.{BeforeAndAfterAll, Tag}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}

import java.io.File
import java.nio.file.Path
import viper.silver.testing.{AnnotatedTestInput, AnnotationBasedTestSuite, DefaultTestInput, ProjectInfo, TestAnnotationParser, TestAnnotations, TestInput}
import viper.silver.utility.Paths

abstract class AbstractGobraTests extends AnnotationBasedTestSuite with BeforeAndAfterAll {

  val specificationCommentStart = "//@"

  val z3PropertyName = "GOBRATESTS_Z3_EXE"

  val z3Exe: Option[String] = Option(System.getProperty(z3PropertyName))

  private var _prerequisites: Option[(Gobra, GobraExecutionContext)] = None
  def prerequisites(): (Gobra, GobraExecutionContext) = {
    val res = _prerequisites.getOrElse(new Gobra(), new DefaultGobraExecutionContext())
    _prerequisites = Some(res)
    res
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    prerequisites() // we invoke `prerequisites()` for its side-effects
  }

  override def afterAll(): Unit = {
    _prerequisites.foreach { case (_, ctx) => ctx.terminateAndAssertInexistanceOfTimeout() }
    _prerequisites = None
    super.afterAll()
  }

  /**
    * Returns a class loader that can be used to access resources
    * such as test files via [[java.lang.ClassLoader]].
    *
    * @return A class loader for accessing resources.
    */
  private def classLoader: ClassLoader = getClass.getClassLoader

  override def getTestDirPath(testDir: String): Path = {
    assert(testDir != null, s"Test directory '$testDir' does not exist")
    // try to load testDir from class path (i.e. assume tests are in resources folder
    val resource = classLoader.getResource(testDir)
    if (resource != null) {
      viper.silver.utility.Paths.pathFromResource(resource)
    } else {
      val targetPath: File = Paths.canonize(testDir)
      assert(targetPath.isDirectory, s"Invalid test directory '$testDir'")
      targetPath.toPath
    }
  }

  override def buildTestInput(file: Path, prefix: String): GobraAnnotatedTestInput =
    GobraAnnotatedTestInput(file, prefix, prerequisites())

  /** we override the default annotation parser because `//@` should not be treated as a comment */
  object GobraAnnotatedTestInput extends TestAnnotationParser {
    /**
      * Creates an annotated test input by parsing all annotations in the files
      * that belong to the given test input.
      */
    def apply(i: TestInput, prerequisites: (Gobra, GobraExecutionContext)): GobraAnnotatedTestInput =
      GobraAnnotatedTestInput(i.name, i.prefix, i.files, i.tags,
        parseAnnotations(i.files), prerequisites)

    def apply(file: Path, prefix: String, prerequisites: (Gobra, GobraExecutionContext)): GobraAnnotatedTestInput =
      apply(DefaultTestInput(file, prefix), prerequisites)

    override def isCommentStart(trimmedLine: String): Boolean = {
      trimmedLine.startsWith(commentStart) && !trimmedLine.startsWith(specificationCommentStart)
    }
  }

  case class GobraAnnotatedTestInput(
                                      name: String,
                                      prefix: String,
                                      files: Seq[Path],
                                      tags: Seq[Tag],
                                      annotations: TestAnnotations,
                                      prerequisites: (Gobra, GobraExecutionContext)
                                    ) extends AnnotatedTestInput {
    /**
      * Create a test input that is specific to the given project.
      *
      * It creates an additional tag, filters files according to annotations
      * and also filters the annotations themselves.
      */
    def makeForProject(projectInfo: ProjectInfo): GobraAnnotatedTestInput = {
      val ignore = (file: Path) => projectInfo.projectNames.exists(annotations.isFileIgnored(file, _))
      copy(
        name = s"$name [${projectInfo.fullName}]",
        files = files.filter(!ignore(_)),
        tags = projectInfo.projectNames.map(Tag(_)) ++ tags.toList,
        annotations = annotations.filterByProject(projectInfo),
        prerequisites = prerequisites)
    }

    override def copyWithFiles(files: Seq[Path]): AnnotatedTestInput = copy(files = files)
  }
}
