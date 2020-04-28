package viper.gobra.frontend

import java.io.File
import java.nio.file.{Files, Paths}

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.SystemUtils

import scala.util.Properties

object PackageResolver {

  val extension = """go"""

  /**
    * Resolves a package name to specific input files
    * @param pkgName package name that should be resolved
    * @param includeOpt list of directories that will be used for package resolution before falling back to $GOPATH
    * @return list of files belonging to the package or the input files in case pkgOrFiles is Right
    */
  def resolve(pkgName: String, includeOpt: Option[List[File]]): Vector[File] = {
    // run `go help gopath` to get a detailed explanation of package resolution in go
    val path = Properties.envOrElse("GOPATH", "")
    val paths = (if (SystemUtils.IS_OS_WINDOWS) path.split(";") else path.split(":")).filter(_.nonEmpty)
    val includePaths = includeOpt.getOrElse(List()).map(_.toPath)
    // prepend includePaths before paths that have been derived based on $GOPATH:
    val packagePaths = includePaths ++ paths.map(p => Paths.get(p))
      // for now, we restrict our search to the "src" subdirectory:
      .map(_.resolve("src"))
      // the desired package should now be located in a subdirectory named after the package name:
      .map(_.resolve(pkgName))
    val pkgDirOpt = packagePaths.collectFirst { case p if Files.exists(p) => p }
    // pkgDir stores the path to the directory that should contain source files belonging to the desired package
    pkgDirOpt.map(pkgDir => getSourceFiles(pkgDir.toFile, pkgName)).getOrElse(Vector())
  }

  /**
    * Returns all source files with file extension 'extension' in a specific directory `dir` with package name `pkg`
    */
  private def getSourceFiles(dir: File, pkg: String): Vector[File] = {
    dir
      .listFiles
      .filter(_.isFile)
      // only consider file extensions "go"
      .filter(f => FilenameUtils.getExtension(f.getName) == extension)
      // get package name for each file:
      .map(f => (f, getPackageClause(f)))
      // ignore all files that have a different package name:
      .collect { case (f, Some(pkgName)) if pkgName == pkg => f }
      .toVector
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
