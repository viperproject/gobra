// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.{Closeable, InputStream}
import java.nio.file.{FileSystem, FileSystemAlreadyExistsException, FileSystems, Files, Path, Paths}
import java.util.Collections

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils
import viper.gobra.ast.frontend.PImplicitQualifiedImport

import scala.io.BufferedSource
import scala.util.Properties
import scala.jdk.CollectionConverters._

object PackageResolver {

  val extension = """gobra"""
  /** directory containing the package of built-in members that are automatically unqualifiedly imported */
  val builtInDirectory = "builtin"
  /** directory paths containing stubs relative to src/main/resources */
  val stubDirectories = Vector("stubs")
  val fileUriScheme = "file"
  val jarUriScheme = "jar"

  trait AbstractImport
  /** represents an implicit unqualified import that should resolve to the built-in package */
  case object BuiltInImport extends AbstractImport
  /** relative import path that should be resolved; an empty importPath results in looking for files in the current directory */
  case class RegularImport(importPath: String) extends AbstractImport {
    override def toString: String = importPath
  }

  /**
    * Resolves a package name (i.e. import path) to specific input files
    * @param importTarget
    * @param includeDirs list of directories that will be used for package resolution before falling back to $GOPATH
    * @return list of files belonging to the package (right) or an error message (left) if no directory could be found
    *         or the directory contains input files having different package clauses
    */
  def resolve(importTarget: AbstractImport, includeDirs: Vector[Path]): Either[String, Vector[InputResource]] = {
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importTarget, includeDirs)
      sourceFiles = getSourceFiles(pkgDir)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      _ <- checkPackageClauses(sourceFiles, importTarget)
    } yield sourceFiles
  }

  /**
    * Similar to `resolve` but returns the package name of the files instead of the files themselves.
    * The returned package name can be used as qualifier for the implicitly qualified import.
    *
    * @param n implicitely qualified import for which a qualifier should be resolved
    * @param includeDirs list of directories that will be used for package resolution before falling back to $GOPATH
    * @return qualifier with which members of the imported package can be accessed (right) or an error message (left)
    *         if no directory could be found or the directory contains input files having different package clauses
    */
  def getQualifier(n: PImplicitQualifiedImport, includeDirs: Vector[Path]): Either[String, String] = {
    val importTarget = RegularImport(n.importPath)
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importTarget, includeDirs)
      sourceFiles = getSourceFiles(pkgDir)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      pkgName <- checkPackageClauses(sourceFiles, importTarget)
      // close all files as we do not need them anymore:
      _ = sourceFiles.foreach(_.close())
    } yield pkgName
  }

  private def getIncludeResources(includeDirs: Vector[Path]): Vector[InputResource] = {
    includeDirs.map(FileResource)
  }

  private val getBuiltInResource: Option[InputResource] = getResource(builtInDirectory)
  private val getStubResources: Vector[InputResource] = stubDirectories.flatMap(getResource)

  private def getResource(path: String): Option[InputResource] = {
    val nullableResourceUri = getClass.getClassLoader.getResource(path).toURI
    for {
      resourceUri <- Option.when(nullableResourceUri != null)(nullableResourceUri)
      // when executing the tests, the resource URI points to regular files in the filesystem (i.e. the
      // URI scheme is "file"). However, when directly running Gobra, files in the resource folder are contained in
      // the jar file. As this is some kind of zip file, accessing the stubs as regular files is not possible.
      // BaseJarResource provides an abstraction that internally uses an adhoc filesystem to enable file-like access
      // to these resources.
      resource <- resourceUri.getScheme match {
        case s if s == fileUriScheme => Some(FileResource(Paths.get(resourceUri)))
        case s if s == jarUriScheme =>
          val fs = try {
            FileSystems.newFileSystem(resourceUri, Collections.emptyMap[String, Any]())
          } catch {
            case _: FileSystemAlreadyExistsException => FileSystems.getFileSystem(resourceUri)
          }
          Some(JarResource(fs, path))
        case _ => None
      }
    } yield resource
  }

  private def getGoPathResources: Vector[InputResource] = {
    // run `go help gopath` to get a detailed explanation of package resolution in go
    val path = Properties.envOrElse("GOPATH", "")
    val paths = (if (SystemUtils.IS_OS_WINDOWS) path.split(";") else path.split(":")).filter(_.nonEmpty)
    paths
      .map(Paths.get(_))
      // for now, we restrict our search to the "src" subdirectory:
      .map(_.resolve("src"))
      .map(p => FileResource(p))
      .toVector
  }

  /**
    * Resolves import target using includeDirs to a directory which exists and from which source files should be retrieved
    */
  private def getLookupPath(importTarget: AbstractImport, includeDirs: Vector[Path]): Either[String, InputResource] = {
    importTarget match {
      case BuiltInImport => getBuiltInResource.toRight(s"Loading builtin package has failed")
      case RegularImport(importPath) =>
        val resources = getIncludeResources(includeDirs) ++ getStubResources ++ getGoPathResources
        // the desired package should now be located in a subdirectory named after the package name:
        val packageDirs = resources.map(_.resolve(importPath))
        // take first one that exists:
        val pkgDirOpt = packageDirs.collectFirst { case p if p.exists() => p }
        // close all resources that we no longer need:
        (resources ++ packageDirs).foreach {
          case resource if !pkgDirOpt.contains(resource) => resource.close()
          case _ =>
        }
        pkgDirOpt.toRight(s"No existing directory found for import path '$importPath'")
    }
  }

  /**
    * Returns all source files with file extension 'extension' in the input resource
    */
  private def getSourceFiles(input: InputResource): Vector[InputResource] = {
    val dirContent = input.listContent()
    val res = dirContent
      .filter(resource => Files.isRegularFile(resource.path))
      // only consider files with the particular extension
      .filter(resource => FilenameUtils.getExtension(resource.path.toString) == extension)
    (dirContent :+ input).foreach({
      case resource if !res.contains(resource) => resource.close()
      case _ =>
    })
    res
  }

  /**
    * Looks up the package clauses for all files and checks whether they match.
    * Returns right with the package name used in the package clause if they do, otherwise returns left with an error message
    */
  def checkPackageClauses(files: Vector[InputResource], importTarget: AbstractImport): Either[String, String] = {
    // importPath is only used to create an error message that is similar to the error message of the official Go compiler
    def getPackageClauses(files: Vector[InputResource]): Either[String, Vector[(InputResource, String)]] = {
      val pkgClauses = files.map(f => {
        getPackageClause(f) match {
          case Some(pkgClause) => Right(f -> pkgClause)
          case _ => Left(f)
        }
      })
      val (failedFiles, validFiles) = pkgClauses.partitionMap(identity)
      if (failedFiles.nonEmpty) Left(s"Parsing package clause for these files has failed: ${failedFiles.mkString(", ")}")
      else Right(validFiles)
    }

    def isEqual(pkgClauses: Vector[(InputResource, String)]): Either[String, String] = {
      val differingClauses = pkgClauses.filter(_._2 != pkgClauses.head._2)
      if (differingClauses.isEmpty) Right(pkgClauses.head._2)
      else {
        val foundPackages = differingClauses.collect { case (f, clause) => s"$clause (${f})" }.mkString(", ")
        Left(s"Found packages $foundPackages in $importTarget")
      }
    }

    for {
      pkgClauses <- getPackageClauses(files)
      pkgName <- isEqual(pkgClauses)
    } yield pkgName
  }

  private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

  private def getPackageClause(file: InputResource): Option[String] = {
    val inputStream = file.asStream()
    val bufferedSource = new BufferedSource(inputStream)
    val content = bufferedSource.mkString
    bufferedSource.close()

    // TODO is there a way to perform the regex lazily on the file's content?
    pkgClauseRegex
      .findAllMatchIn(content)
      .collectFirst { case m if m.group(1) != null => m.group(1) }
  }

  trait InputResource extends Closeable {
    val path: Path

    def resolve(pathComponent: String): InputResource

    def exists(): Boolean = Files.exists(path)

    private var stream: Option[InputStream] = None
    /**
      * stream has to be closed after use either by calling `close` on the resource or directly on the stream.
      * In case the resource (if applicable incl. its file system) is continued to be used by the stream is no
      * longer needed, it is recommended to directly call `close` on the stream as soon as possible and call `close`
      * on the resource when it is no longer needed.
      */
    def asStream(): InputStream = {
      val inputStream = Files.newInputStream(path)
      stream = Some(inputStream)
      inputStream
    }

    /** returns files that are part of this directory */
    def listContent(): Vector[InputResource]

    /** closes stream and filesystem (if applicable) */
    override def close(): Unit = stream match {
      case Some(s) =>
        s.close()
        stream = None
      case _ =>
    }
  }

  case class FileResource(path: Path) extends InputResource {

    override def resolve(pathComponent: String): FileResource =
      FileResource(path.resolve(pathComponent))

    override def listContent(): Vector[FileResource] = {
      Files.newDirectoryStream(path).asScala.toVector
        .map(p => FileResource(p))
    }
  }

  case class JarResource(filesystem: FileSystem, pathString: String) extends InputResource {
    override def resolve(pathComponent: String): JarResource =
      JarResource(filesystem, path.resolve(pathComponent).toString)

    override def listContent(): Vector[JarResource] =
      Files.newDirectoryStream(path).asScala.toVector.map(p => JarResource(filesystem, p.toString))

    override val path: Path = filesystem.getPath(pathString)
  }
}
