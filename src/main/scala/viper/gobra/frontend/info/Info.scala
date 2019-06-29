package viper.gobra.frontend.info

import java.io.File
import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.FileUtils
import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PProgram}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{TypeError, VerifierError}
import viper.gobra.util.OutputUtil

object Info {
  type GoTree = Tree[PNode, PProgram]

  def check(program: PProgram)(config: Config): Either[Vector[VerifierError], TypeInfo] = {
    val tree = new GoTree(program)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree)

    val errors = info.errors
    if (errors.isEmpty) {

      // print program with ghost code erased
      if (config.printGhostLess()) {
        val ghostLessPrinter = new GhostLessPrinter(info)
        val outputFile = OutputUtil.postfixFile(config.inputFile(), "ghostLess")
        FileUtils.writeStringToFile(
          outputFile,
          ghostLessPrinter.format(program),
          UTF_8
        )
      }

      Right(info)
    } else {
      Left(program.positions.translate(errors, TypeError))
    }
  }
}
