// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra

import java.io.File
import java.nio.file.Path
import viper.silver.testing.{AnnotationBasedTestSuite, DefaultAnnotatedTestInput, DefaultTestInput, TestAnnotationParser, TestInput}
import viper.silver.utility.Paths

abstract class AbstractGobraTests extends AnnotationBasedTestSuite {

  val specificationCommentStart = "//@"

  val z3PropertyName = "GOBRATESTS_Z3_EXE"

  val z3Exe: Option[String] = Option(System.getProperty(z3PropertyName))

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

  override def buildTestInput(file: Path, prefix: String): DefaultAnnotatedTestInput =
    GobraAnnotatedTestInput(file, prefix)

  /** we override the default annotation parser because `//@` should not be treated as a comment */
  object GobraAnnotatedTestInput extends TestAnnotationParser {
    /**
      * Creates an annotated test input by parsing all annotations in the files
      * that belong to the given test input.
      */
    def apply(i: TestInput): DefaultAnnotatedTestInput =
      DefaultAnnotatedTestInput(i.name, i.prefix, i.files, i.tags,
        parseAnnotations(i.files))

    def apply(file: Path, prefix: String): DefaultAnnotatedTestInput =
      apply(DefaultTestInput(file, prefix))

    override def isCommentStart(trimmedLine: String): Boolean = {
      trimmedLine.startsWith(commentStart) && !trimmedLine.startsWith(specificationCommentStart)
    }
  }
}
