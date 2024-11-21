// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.frontend

import java.io.Reader
import java.nio.file.{Files, Path, Paths}

import org.bitbucket.inkytonik.kiama.util.{FileSource, Filenames, IO, Source, StringSource}
import viper.gobra.util.Violation
import viper.silver.ast.SourcePosition
import java.util.Objects

import viper.gobra.translator.Names

import scala.io.BufferedSource

/**
 * Contains information about a package. Note that this class must not be a case class, since it is stored as information
 * attached to an AST node. Kiama, treats every Product instance as an AST node. Case classes are instances of said product
 * type. Therefore, Kiama would treat this as a AST node which leads to errors as this class does not extend PNode.
 *
 * @param id a unique identifier for the package
 * @param name the name of the package, does not have to be unique
 * @param isBuiltIn a flag indicating, if the package comes from within Gobra
 */
class PackageInfo(val id: String, val name: String, val isBuiltIn: Boolean) {

  /**
   * Unique id of the package to use in Viper member names.
   */
  lazy val viperId: String = Names.hash(id)

  override def equals(obj: Any): Boolean = obj match {
    case other: PackageInfo => other.id == this.id
    case _ => false
  }

  override def hashCode: Int = Objects.hash(id)
}


/**
  * Contains several utility functions for managing Sources, i.e. inputs to Gobra
  */
object Source {

  /**
   * Returns an object containing information about the package a source belongs to.
   */
  def getPackageInfo(src: Source, projectRoot: Path): PackageInfo = {

    val isBuiltIn: Boolean = src match {
      case FromFileSource(_, _, builtin) => builtin
      case _ => false
    }

    val packageName: String = PackageResolver.getPackageClause(src: Source)
      .getOrElse(Violation.violation("Missing package clause in " + src.name))

    /**
     * A unique identifier for packages
     */
    val packageId: String = {
      val parentPath = TransformableSource(src).toPath.toAbsolutePath.getParent
      // paths to builtIn packages cannot be made relative to the project root. Thus, we have to special case them:
      val prefix = if (isBuiltIn) parentPath.toString else uniquePath(parentPath, projectRoot).toString
      if(prefix.nonEmpty) {
        // The - is enough to unambiguously separate the prefix from the package name, since it can't occur in the package name
        // per Go's spec (https://go.dev/ref/spec#Package_clause)
        prefix + " - " + packageName
      } else {
        // Fallback case if the prefix is empty, for example if the directory of a FileSource is in the current directory
        packageName
      }
    }
    new PackageInfo(packageId, packageName, isBuiltIn)
  }

  /**
    * Returns the given path such that it is relative to the projectRoot.
    * If the path isn't a child of the projectRoot it is returned unchanged instead.
    * We do not use the canonical path because we want a unique path that is computer independent.
    */
  def uniquePath(path: Path, projectRoot: Path): Path =
    projectRoot.relativize(path).normalize()

  implicit class TransformableSource[S <: Source](source: S) {
    def transformContent(newContent: String): Source = source match {
      case StringSource(_, name) => StringSource(newContent, name)
      case FileSource(name, _) => FromFileSource(Paths.get(name), newContent, builtin = false)
      case FromFileSource(path, _, builtin) => FromFileSource(path, newContent, builtin)
      case _ => Violation.violation(s"encountered unknown source ${source.name}")
    }

    def toPath: Path = source match {
      case FileSource(filename, _) => Paths.get(filename)
      case FromFileSource(path, _, _) => path
      case StringSource(_, _) =>
        // StringSource stores some name but there is no guarantee that it corresponds to a valid file path
        // there are the following ideas:
        // - create a file adhoc if a filepath is really necessary (in the spirit of `StringSource.useAsFile`)
        // - adapt Silver to not require a Path object but some abstraction
        // - create an implementation implementing Path that simply stores the StringSource and leaves all functions
        //    unimplemented. This works as long as nobody relies on any operations available for paths. It's also bad
        //    style but at least any use fails in an understandable way (as opposed to simply returning `null` here)
      ???
    }

    def contains(position: SourcePosition): Boolean = Files.isSameFile(toPath, position.file)
  }

  /**
    * Represents a source that originates from a file (stored at `path`) but whose content has been transformed (and is now `content`)
    * @param path original file's path
    * @param content source's content after applying some transformations
    */
  case class FromFileSource(path: Path, content: String, builtin: Boolean) extends Source {
    override val name: String = path.toString
    val shortName : Option[String] = Some(Filenames.dropCurrentPath(name))

    def reader: Reader = IO.stringreader(content)

    def useAsFile[T](fn : String => T) : T = {
      // copied from StringSource
      val filename = Filenames.makeTempFilename(name)
      IO.createFile(filename, content)
      val t = fn(filename)
      IO.deleteFile(filename)
      t
    }
  }

  object FromFileSource {
    def apply(path: Path, builtin: Boolean = false): FromFileSource = {
      val inputStream = Files.newInputStream(path)
      val bufferedSource = new BufferedSource(inputStream)
      val content = bufferedSource.mkString
      bufferedSource.close()
      FromFileSource(path, content, builtin)
    }
  }
}
