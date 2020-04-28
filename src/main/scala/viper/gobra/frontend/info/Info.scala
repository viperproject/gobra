package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PPackage}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

object Info {
  type GoTree = Tree[PNode, PPackage]

  def check(program: PPackage)(config: Config): Either[Vector[VerifierError], TypeInfo] = {
    val tree = new GoTree(program)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree)

    val errors = info.errors
    config.reporter report TypeCheckDebugMessage(config.inputFiles.head, () => program, () => getDebugInfo(program, info))
    if (errors.isEmpty) {
      config.reporter report TypeCheckSuccessMessage(config.inputFiles.head, () => program, () => getErasedGhostCode(program, info))
      Right(info)
    } else {
      val typeErrors = program.positions.translate(errors, TypeError)
      config.reporter report TypeCheckFailureMessage(config.inputFiles.head, () => program, typeErrors)
      Left(typeErrors)
    }
  }

  private def getErasedGhostCode(program: PPackage, info: TypeInfoImpl): String = {
    new GhostLessPrinter(info).format(program)
  }

  private def getDebugInfo(program: PPackage, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(program)
  }
}
