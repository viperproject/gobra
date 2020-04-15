package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PProgram}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

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
      config.reporter report TypeCheckSuccessMessage(config.inputFile, () => program,
        () => getErasedGhostCode(program, info), () => getDebugInfo(program, info))
      Right(info)
    } else {
      val typeErrors = program.positions.translate(errors, TypeError)
      config.reporter report TypeCheckFailureMessage(config.inputFile, () => program, typeErrors, () => getDebugInfo(program, info))
      Left(typeErrors)
    }
  }

  private def getErasedGhostCode(program: PProgram, info: TypeInfoImpl): String = {
    new GhostLessPrinter(info).format(program)
  }

  private def getDebugInfo(program: PProgram, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(program)
  }
}
