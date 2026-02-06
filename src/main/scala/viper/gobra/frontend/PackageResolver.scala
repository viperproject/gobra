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
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.ast.frontend.PImplicitQualifiedImport
import viper.gobra.frontend.Source.FromFileSource

import scala.io.BufferedSource
import scala.util.Properties
import scala.jdk.CollectionConverters._

object PackageResolver {

  val gobraExtension = """gobra"""
  val goExtension = """go"""
  val inputFilePattern = s"""(.*\\.(?:${PackageResolver.gobraExtension}|${PackageResolver.goExtension}))$$"""
  val inputFileRegex = inputFilePattern.r // without Scala string interpolation escapes: """(.*\.(?:gobra|go))$""".r
  /** directory containing the package of built-in members that are automatically unqualifiedly imported */
  val builtInDirectory = "builtin"
  /** directory paths containing stubs relative to src/main/resources */
  val stubDirectories = Vector("stubs")
  val fileUriScheme = "file"
  val jarUriScheme = "jar"

  sealed trait AbstractImport
  /** represents an implicit unqualified import that should resolve to the built-in package */
  case object BuiltInImport extends AbstractImport
  /** relative import path that should be resolved; an empty importPath results in looking for files in the current directory */
  case class RegularImport(importPath: String) extends AbstractImport {
    override def toString: String = importPath
  }

  sealed trait AbstractPackage
  /** represents an error */
  case class NoPackage(importTarget: RegularImport) extends AbstractPackage
  /** represents all built-in packages together */
  case object BuiltInPackage extends AbstractPackage
  /** represents a regular package */
  case class RegularPackage(id: String) extends AbstractPackage

  object AbstractPackage {
    def apply(imp: AbstractImport)(config: Config): AbstractPackage = {
      imp match {
        case BuiltInImport => BuiltInPackage
        case imp: RegularImport =>
          getLookupPath(imp)(config) match {
            case Left(_) => NoPackage(imp)
            case Right(inputResource) =>
              try {
                RegularPackage(Source.uniquePath(inputResource.path, config.projectRoot).toString)
              } catch { case _: Throwable => NoPackage(imp) }
          }
      }
    }
  }

  /** represents some resolved source together with the knowledge of whether it was obtained from a builtin resource or not */
  case class ResolveSourcesResult(source: Source, isBuiltin: Boolean)

  /**
    * Resolves a package name (i.e. import path) to specific input sources
    * @param importTarget
    * @param config
    * @return list of sources belonging to the package (right) or an error message (left) if no directory could be found
    *         or the directory contains input files having different package clauses
    */
  def resolveSources(importTarget: AbstractImport)(config: Config): Either[String, Vector[ResolveSourcesResult]] = {
    for {
      resources <- resolve(importTarget)(config)
      sources = resources.map(r => ResolveSourcesResult(r.asSource(), r.builtin))
      // we do no longer need the resources, so we close them:
      _ = resources.foreach(_.close())
    } yield sources
  }

  /**
    * Resolves a package name (i.e. import path) to specific input files
    * @param importTarget
    * @param config
    * @return list of files belonging to the package (right) or an error message (left) if no directory could be found
    *         or the directory contains input files having different package clauses
    */
  private def resolve(importTarget: AbstractImport)(config: Config): Either[String, Vector[InputResource]] = {
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importTarget)(config)
      sourceFiles = getSourceFiles(pkgDir, onlyFilesWithHeader = config.onlyFilesWithHeaderOrDefault)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      // in case of error, iterate over all resources and close them
      _ <- checkPackageClauses(sourceFiles, importTarget)
        .left.map(err => {
          sourceFiles.foreach(_.close())
          err
        })
    } yield sourceFiles
  }

  /**
    * Similar to `resolve` but returns the package name of the files instead of the files themselves.
    * The returned package name can be used as qualifier for the implicitly qualified import.
    *
    * @param n implicitely qualified import for which a qualifier should be resolved
    * @param config
    * @return qualifier with which members of the imported package can be accessed (right) or an error message (left)
    *         if no directory could be found or the directory contains input files having different package clauses
    */
  def getQualifier(n: PImplicitQualifiedImport)(config: Config): Either[String, String] = {
    val importTarget = RegularImport(n.importPath)
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importTarget)(config)
      // note that we ignore the "onlyFilesWithHeader" option provided in `config`, because we still want to consider
      // all source files when resolving the right qualifier for a package:
      sourceFiles = getSourceFiles(pkgDir, onlyFilesWithHeader = false)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      pkgName <- checkPackageClauses(sourceFiles, importTarget)
      // close all files as we do not need them anymore:
      _ = sourceFiles.foreach(_.close())
    } yield pkgName
  }

  private def getIncludeResources(includeDirs: Vector[Path]): Vector[InputResource] = {
    includeDirs.map(FileResource(_))
  }

  private val getBuiltInResource: Option[InputResource] = getBuiltInResource(builtInDirectory)
  private val getStubResources: Vector[InputResource] = stubDirectories.flatMap(getBuiltInResource(_))

  private def getBuiltInResource(path: String): Option[InputResource] = {
    val nullableResourceUri = getClass.getClassLoader.getResource(path).toURI
    for {
      resourceUri <- Option.when(nullableResourceUri != null)(nullableResourceUri)
      // when executing the tests, the resource URI points to regular files in the filesystem (i.e. the
      // URI scheme is "file"). However, when directly running Gobra, files in the resource folder are contained in
      // the jar file. As this is some kind of zip file, accessing the stubs as regular files is not possible.
      // BaseJarResource provides an abstraction that internally uses an adhoc filesystem to enable file-like access
      // to these resources.
      resource <- resourceUri.getScheme match {
        case s if s == fileUriScheme => Some(FileResource(Paths.get(resourceUri), builtin = true))
        case s if s == jarUriScheme =>
          val fs = try {
            FileSystems.newFileSystem(resourceUri, Collections.emptyMap[String, Any]())
          } catch {
            case _: FileSystemAlreadyExistsException => FileSystems.getFileSystem(resourceUri)
          }
          Some(JarResource(fs, path, builtin = true))
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
  private def getLookupPath(importTarget: AbstractImport)(config: Config): Either[String, InputResource] = {
    val moduleName = config.moduleName
    val includeDirs = config.includeDirs
    val moduleNameWithTrailingSlash = if (moduleName.nonEmpty && !moduleName.endsWith("/")) s"$moduleName/" else moduleName
    importTarget match {
      case BuiltInImport => getBuiltInResource.toRight(s"Loading builtin package has failed")
      case RegularImport(importPath) =>
        // if importPath starts with current module name then only consider the remainder
        val moduleImportPath = if (importPath.startsWith(moduleNameWithTrailingSlash)) importPath.substring(moduleNameWithTrailingSlash.length) else importPath
        val resources = getIncludeResources(includeDirs) ++ getStubResources ++ getGoPathResources
        // the desired package should now be located in a subdirectory named after the package name:
        val packageDirs = resources.map(_.resolve(moduleImportPath))
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
    * Determines if a file is to be imported when the package implemented in the same directory is imported.
    * All test files (ending in "_test.go") are skipped, as well as all files whose path contains the `testdata` directory.
    * One difference between our implementation in Gobra and what the Go compiler does is that Gobra does not complain
    * if the package clause in a test file mentions a package other than the one implemented in the same directory or that package
    * suffixed with "_test".
    * TODO: add this check
    * More information about this can be found in https://tip.golang.org/cmd/go/#hdr-Test_packages.
    */
  private def shouldIgnoreResource(r: InputResource): Boolean = {
    val path = r.path.toString
    // files inside a directory named "testdata" should be ignored
    val testDataDir = """.*/testdata(?:$|/.*)""".r
    // test files in Go have their name terminating in "_test.go"
    val testFilesEnding = """.*_test.go$""".r
    testFilesEnding.matches(path) || testDataDir.matches(path)
  }

  /**
    * Decides whether an input resource should be considered by Gobra when considering only files with headers.
    */
  private def isResourceWithHeader(resource: InputResource): Boolean = {
    resource match {
      case i: InputResource if i.builtin =>
        // standard library methods defined in stubs are always considered by Gobra
        true
      case _ => Config.sourceHasHeader(resource.asSource())
    }
  }

  /**
    * Returns the source files with file extension 'gobraExtension' or 'goExtension' in the input resource which
    * are not test files and are not in a directory with name 'testdata'.
    * Input can also be a file in which case the same file is returned if it passes all tests
    */
  def getSourceFiles(input: InputResource, recursive: Boolean = false, onlyFilesWithHeader: Boolean = false): Vector[InputResource] = {
    // get content of directory if it's a directory, otherwise just return the file itself
    val dirContentOrInput = if (Files.isDirectory(input.path)) { input.listContent() } else { Vector(input) }
    val res = dirContentOrInput
      .flatMap { resource =>
        if (recursive && Files.isDirectory(resource.path)) {
          getSourceFiles(resource, recursive = recursive, onlyFilesWithHeader = onlyFilesWithHeader)
        } else if (Files.isRegularFile(resource.path)) {
          // ignore files that are not Go or Gobra sources, have a filename or are placed in a directory that should be
          // ignored according to the Go spec or that do not have a header we require them to have:
          lazy val validExtension = FilenameUtils.getExtension(resource.path.toString) == gobraExtension ||
            FilenameUtils.getExtension(resource.path.toString) == goExtension
          lazy val shouldBeConsidered = !shouldIgnoreResource(resource)
          // note that the following condition has to be lazily evaluated to avoid reading the file's content and applying
          // a regex. The first part in particular can fail when the file does not contain text!
          // Note that that we do not enforce the header for builtin resources (even if onlyFilesWithHeader is set to true)
          lazy val headerIsMissing = onlyFilesWithHeader && !resource.builtin && !isResourceWithHeader(resource)
          if (validExtension && shouldBeConsidered && !headerIsMissing) Vector(resource) else Vector()
        } else {
          Vector()
        }
      }
    // close all resource that are no longer needed:
    (dirContentOrInput.toSet + input).foreach({
      case resource if !res.contains(resource) => resource.close()
      case _ =>
    })
    res
  }

  /**
    * Looks up the package clauses for all files and checks whether they match.
    * Returns right with the package name used in the package clause if they do, otherwise returns left with an error message
    */
  private def checkPackageClauses(files: Vector[InputResource], importTarget: AbstractImport): Either[String, String] = {
    def isEmpty(files: Vector[InputResource]): Either[String, Vector[InputResource]] = {
      if (files.isEmpty) Left(s"No files belonging to package $importTarget found")
      else Right(files)
    }

    // importPath is only used to create an error message that is similar to the error message of the official Go compiler
    def getPackageClauses(files: Vector[InputResource]): Either[String, Vector[(InputResource, String)]] = {
      require(files.nonEmpty)
      val pkgClauses = files.map(f => {
        getPackageClause(f.asSource()) match {
          case Some(pkgClause) => Right(f -> pkgClause)
          case _ => Left(f)
        }
      })
      val (failedFiles, validFiles) = pkgClauses.partitionMap(identity)
      if (failedFiles.nonEmpty) Left(s"Parsing package clause for these files has failed: ${failedFiles.mkString(", ")}")
      else Right(validFiles)
    }

    def isEqual(pkgClauses: Vector[(InputResource, String)]): Either[String, String] = {
      require(pkgClauses.nonEmpty)
      val differingClauses = pkgClauses.filter(_._2 != pkgClauses.head._2)
      if (differingClauses.isEmpty) Right(pkgClauses.head._2)
      else {
        val foundPackages = differingClauses.collect { case (f, clause) => s"$clause (${f})" }.mkString(", ")
        Left(s"Found packages $foundPackages in $importTarget")
      }
    }

    for {
      nonEmptyFiles <- isEmpty(files)
      pkgClauses <- getPackageClauses(nonEmptyFiles)
      pkgName <- isEqual(pkgClauses)
    } yield pkgName
  }

  // Multiline comments are matched lazily, meaning it will stop at the earliest encountered '*/'
  private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*?\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

  def getPackageClause(src: Source): Option[String] = {

    // TODO is there a way to perform the regex lazily on the file's content?
    pkgClauseRegex
      .findAllMatchIn(src.content)
      .collectFirst { case m if m.group(1) != null => m.group(1) }
  }

  trait InputResource extends Closeable {
    val path: Path
    val builtin: Boolean

    def resolve(pathComponent: String): InputResource

    def exists(): Boolean = Files.exists(path)

    private var stream: Option[InputStream] = None
    /**
      * stream has to be closed after use either by calling `close` on the resource or directly on the stream.
      * In case the resource (if applicable incl. its file system) is continued to be used but the stream is no
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

    def asSource(): Source
  }

  case class FileResource(path: Path, builtin: Boolean = false) extends InputResource {

    override def resolve(pathComponent: String): FileResource =
      FileResource(path.resolve(pathComponent))

    override def listContent(): Vector[FileResource] = {
      Files.newDirectoryStream(path).asScala.toVector
        .map(p => FileResource(p, builtin))
    }

    override def asSource(): FromFileSource = FromFileSource(path, builtin)
  }

  case class JarResource(filesystem: FileSystem, pathString: String, builtin: Boolean = false) extends InputResource {
    override def resolve(pathComponent: String): JarResource =
      JarResource(filesystem, path.resolve(pathComponent).toString, builtin)

    override def listContent(): Vector[JarResource] =
      Files.newDirectoryStream(path).asScala.toVector.map(p => JarResource(filesystem, p.toString, builtin))

    override val path: Path = filesystem.getPath(pathString)

    override def asSource(): FromFileSource = {
      val bufferedSource = new BufferedSource(asStream())
      val content = bufferedSource.mkString
      bufferedSource.close()
      FromFileSource(path, content, builtin)
    }
  }
}
