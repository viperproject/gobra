// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.frontend

import org.bitbucket.inkytonik.kiama.util.{FileSource, Filenames, IO, Source, StringSource}
import viper.gobra.util.Violation

import java.io.Reader
import java.nio.file.{Files, Path, Paths}
import scala.io.BufferedSource

/**
  * Contains several utility functions for managing Sources, i.e. inputs to Gobra
  */
object Source {
  implicit class TransformableSource[S <: Source](source: S) {
    def transformContent(newContent: String): Source = source match {
      case StringSource(_, name) => StringSource(newContent, name)
      case FileSource(name, _) => FromFileSource(Paths.get(name), newContent)
      case FromFileSource(path, _) => FromFileSource(path, newContent)
      case _ => Violation.violation(s"encountered unknown source ${source.name}")
    }

    def toPath: Path = source match {
      case FileSource(filename, _) => Paths.get(filename)
      case FromFileSource(path, _) => path
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
  }

  /**
    * Represents a source that originates from a file (stored at `path`) but whose content has been transformed (and is now `content`)
    * @param path original file's path
    * @param content source's content after applying some transformations
    */
  case class FromFileSource(path: Path, content: String) extends Source {
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
    def apply(path: Path): FromFileSource = {
      val inputStream = Files.newInputStream(path)
      val bufferedSource = new BufferedSource(inputStream)
      val content = bufferedSource.mkString
      bufferedSource.close()
      FromFileSource(path, content)
    }
  }
}
