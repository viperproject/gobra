// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import java.io.{Closeable, File, InputStream}
import java.net.URI
import java.nio.file.{FileSystems, Files, Path, Paths}
import java.util.Collections

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils
import viper.gobra.ast.frontend.PImplicitQualifiedImport

import scala.io.BufferedSource
import scala.util.Properties
import scala.jdk.CollectionConverters._

object PackageResolver {

  val extension = """gobra"""
  /** directory paths containing stubs relative to src/main/resources */
  val stubDirectories = Vector("stubs")
  val fileUriScheme = "file"
  val jarUriScheme = "jar"

  /**
    * Resolves a package name (i.e. import path) to specific input files
    * @param importPath relative import path that should be resolved
    * @param includeDirs list of directories that will be used for package resolution before falling back to $GOPATH
    * @return list of files belonging to the package (right) or an error message (left) if no directory could be found
    *         or the directory contains input files having different package clauses
    */
  def resolve(importPath: String, includeDirs: Vector[File]): Either[String, Vector[InputResource]] = {
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(importPath, includeDirs)
      sourceFiles = getSourceFiles(pkgDir)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      _ <- checkPackageClauses(sourceFiles, importPath)
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
  def getQualifier(n: PImplicitQualifiedImport, includeDirs: Vector[File]): Either[String, String] = {
    for {
      // pkgDir stores the path to the directory that should contain source files belonging to the desired package
      pkgDir <- getLookupPath(n.importPath, includeDirs)
      sourceFiles = getSourceFiles(pkgDir)
      // check whether all found source files belong to the same package (the name used in the package clause can
      // be absolutely independent of the import path)
      pkgName <- checkPackageClauses(sourceFiles, n.importPath)
      // close all files as we do not need them anymore:
      _ = sourceFiles.foreach(_.close())
    } yield pkgName
  }

  private def getIncludeResources(includeDirs: Vector[File]): Vector[InputResource] = {
    includeDirs.map(FileResource)
  }

  private lazy val getStubResources: Vector[InputResource] = {
    // get path to stubs
    stubDirectories.flatMap(stubDir => {
      val nullableResourceUri = getClass.getClassLoader.getResource(stubDir).toURI
      for {
        resourceUri <- Option.when(nullableResourceUri != null)(nullableResourceUri)
        resource <- resourceUri.getScheme match {
          case s if s == fileUriScheme => Some(FileResource(Paths.get(resourceUri).toFile))
          case s if s == jarUriScheme => Some(BaseJarResource(resourceUri, stubDir))
          case _ => None
        }
      } yield resource
    })
  }

  private def getGoPathResources: Vector[InputResource] = {
    // run `go help gopath` to get a detailed explanation of package resolution in go
    val path = Properties.envOrElse("GOPATH", "")
    val paths = (if (SystemUtils.IS_OS_WINDOWS) path.split(";") else path.split(":")).filter(_.nonEmpty)
    paths
      .map(Paths.get(_))
      // for now, we restrict our search to the "src" subdirectory:
      .map(_.resolve("src"))
      .map(p => FileResource(p.toFile))
      .toVector
  }

  /**
    * Resolves importPath using includeDirs to a directory which exists and from which source files should be retrieved
    */
  private def getLookupPath(importPath: String, includeDirs: Vector[File]): Either[String, InputResource] = {
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
  def checkPackageClauses(files: Vector[InputResource], importPath: String): Either[String, String] = {
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
        Left(s"Found packages $foundPackages in $importPath")
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
      case Some(s) => {
        s.close()
        stream = None
      }
      case _ =>
    }
  }

  case class FileResource(file: File) extends InputResource {
    override lazy val path: Path = file.toPath

    def resolve(pathComponent: String): FileResource =
      FileResource(path.resolve(pathComponent).toFile)

    override def listContent(): Vector[FileResource] = {
      Files.newDirectoryStream(path).asScala.toVector
        .map(p => FileResource(p.toFile))
    }
  }

  trait JarResource extends InputResource with Closeable {
    protected val filesystem: ManagedFileSystem[JarResource]

    override def resolve(pathComponent: String): JarFileResource =
      JarFileResource(filesystem, path.resolve(pathComponent).toString)

    override def listContent(): Vector[JarFileResource] = {
      Files.newDirectoryStream(path).asScala.toVector
        .map(p => JarFileResource(filesystem, p.toString))
    }

    override def close(): Unit = {
      super.close()
      filesystem.release(this)
    }
  }

  /**
    *
    * @param baseUri URI to a resource in a JAR
    * @param rootPath Path relative to the resource directory. Note that rootPath occurs as the last path components in baseUrl
    */
  case class BaseJarResource(baseUri: URI, rootPath: String) extends JarResource {
    if (baseUri.getScheme != jarUriScheme) {
      throw new IllegalArgumentException(s"BaseJarResource expects an URI to a JAR but got $baseUri")
    }
    override protected lazy val filesystem = new ManagedFileSystem(baseUri, this)
    override val path: Path = filesystem.getPath(rootPath)
  }

  /**
    *
    * @param filesystem Filesystem in which this resource is located
    * @param pathString Path to the resource relative to the resource directory.
    */
  case class JarFileResource(override protected val filesystem: ManagedFileSystem[JarResource],
                             pathString: String) extends JarResource {
    // retain the filesystem when constructing an instance:
    filesystem.retain(this)

    override val path: Path = filesystem.getPath(pathString)
  }

  /**
    * Wrapper around FileSystem to ensure that the same file system is reused if it already exists.
    * Calls to retain and release keep track of the number of clients currently using this file system.
    * After construction, one client exists (i.e. the one that constructed it). As soon as no clients exist, the
    * file system is closed and can no longer be used.
    */
  class ManagedFileSystem[T](val baseUri: URI, firstClient: T) {
    private val filesystem = FileSystems.newFileSystem(baseUri, Collections.emptyMap[String, Any]())
    private var clients: Set[T] = Set(firstClient)

    def retain(client: T): Unit = {
      if (!filesystem.isOpen) {
        throw new IllegalStateException("managed file system can no longer be retained when it is already closed")
      }
      clients = clients + client
    }

    def release(client: T): Unit = {
      val oldCount = clients.size
      clients = clients - client
      val newCount = clients.size
      if (newCount + 1 != oldCount) {
        throw new IllegalStateException("managed file system was double-released or released with a client that did not retain it")
      }
      if (newCount == 0 && filesystem.isOpen) {
        filesystem.close()
      }
    }

    def getPath(component: String): Path = {
      if (!filesystem.isOpen) {
        throw new IllegalStateException("managed file system is already closed")
      }
      filesystem.getPath(component)
    }
  }
}
