// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.File
import java.nio.file.{Files, Path, Paths}

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils
import viper.gobra.ast.frontend.PImplicitQualifiedImport

import scala.util.Properties

object PackageResolver {

  val extension = """gobra"""

  /**
    * Resolves a package name (i.e. import path) to specific input files
    * @param importPath relative import path that should be resolved
    * @param includeDirs list of directories that will be used for package resolution before falling back to $GOPATH
    * @return list of files belonging to the package (right) or an error message (left)
    */
  def resolve(importPath: String, includeDirs: Vector[File]): Either[String, Vector[File]] = {
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importPath, includeDirs)
      sourceFiles = getSourceFiles(pkgDir.toFile)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      _ <- checkPackageClauses(sourceFiles, importPath)
    } yield sourceFiles
  }

  /**
    * Returns the qualifier with which members of the imported package can be accessed
    */
  def getQualifier(n: PImplicitQualifiedImport, includeDirs: Vector[File]): Option[String] = {
    (for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(n.importPath, includeDirs)
      sourceFiles = getSourceFiles(pkgDir.toFile)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      pkgName <- checkPackageClauses(sourceFiles, n.importPath)
    } yield pkgName).toOption
  }

  /**
    * Resolves importPath using includeDirs to a directory which exists and from which source files should be retrieved
    */
  private def getLookupPath(importPath: String, includeDirs: Vector[File]): Either[String, Path] = {
    // run `go help gopath` to get a detailed explanation of package resolution in go
    val path = Properties.envOrElse("GOPATH", "")
    val paths = (if (SystemUtils.IS_OS_WINDOWS) path.split(";") else path.split(":")).filter(_.nonEmpty)
    // take the parent of importPath and add it to includeDirs if it exists
    val includePaths = includeDirs.map(_.toPath)
      .map(_.resolve(importPath))
    // prepend includePaths before paths that have been derived based on $GOPATH:
    val packagePaths = includePaths ++ paths.map(p => Paths.get(p))
      // for now, we restrict our search to the "src" subdirectory:
      .map(_.resolve("src"))
      // the desired package should now be located in a subdirectory named after the package name:
      .map(_.resolve(importPath))
    val pkgDirOpt = packagePaths.collectFirst { case p if Files.exists(p) => p }
    pkgDirOpt.toRight(s"No existing directory found for import path '$importPath'")
  }

  /**
    * Returns all source files with file extension 'extension' in a specific directory `dir`
    */
  private def getSourceFiles(dir: File): Vector[File] = {
    dir
      .listFiles
      .filter(_.isFile)
      // only consider file extensions "go"
      .filter(f => FilenameUtils.getExtension(f.getName) == extension)
      .toVector
  }

  /**
    * Looks up the package clauses for all files and checks whether they match.
    * Returns right with the package name used in the package clause if they do otherwise returns left with an error message
    */
  def checkPackageClauses(files: Vector[File], importPath: String): Either[String, String] = {
    // importPath is only used to create an error message that is similar to the error message of the official Go compiler
    def getPackageClauses(files: Vector[File]): Either[String, Vector[(File, String)]] = {
      val pkgClauses = files.map(f => {
        getPackageClause(f) match {
          case Some(pkgClause) => Right(f -> pkgClause)
          case _ => Left(f)
        }
      })
      val failedFiles = pkgClauses.collect { case Left(f) => f }
      if (failedFiles.nonEmpty) Left(s"Parsing package clause for these files has failed: ${failedFiles.map(_.getPath).mkString(", ")}")
      else Right(pkgClauses.collect { case Right(pkgClause) => pkgClause })
    }

    def isEqual(pkgClauses: Vector[(File, String)]): Either[String, String] = {
      val differingClauses = pkgClauses.filter(_._2 != pkgClauses.head._2)
      if (differingClauses.isEmpty) Right(pkgClauses.head._2)
      else {
        val foundPackages = differingClauses.collect { case (f, clause) => s"$clause (${f.getPath})" }.mkString(", ")
        Left(s"Found packages $foundPackages in $importPath")
      }
    }

    for {
      pkgClauses <- getPackageClauses(files)
      pkgName <- isEqual(pkgClauses)
    } yield pkgName
  }

  private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

  private def getPackageClause(file: File): Option[String] = {
    val bufferedSource = scala.io.Source.fromFile(file)
    val content = bufferedSource.mkString
    bufferedSource.close()
    // TODO is there a way to perform the regex lazily on the file's content?
    pkgClauseRegex
      .findAllMatchIn(content)
      .collectFirst { case m if m.group(1) != null => m.group(1) }
  }
}
