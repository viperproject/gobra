package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PProgram}
import viper.gobra.frontend.Config
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.ghost.separation.GhostLessPrinter
import viper.gobra.reporting.{TypeCheckDebugMessage, TypeCheckFailureMessage, TypeCheckSuccessMessage, TypeError, VerifierError}

import scala.concurrent.{Future, ExecutionContext}

object Info {

  implicit val executionContext = ExecutionContext.global

  type GoTree = Tree[PNode, PProgram]

  def check(program: PProgram)(config: Config): Future[Either[Vector[VerifierError], TypeInfo]] = {
    Future {
      val tree = new GoTree(program)
      //    println(program.declarations.head)
      //    println("-------------------")
      //    println(tree)
      val info = new TypeInfoImpl(tree)

      val errors = info.errors
      config.reporter report TypeCheckDebugMessage(config.inputFile, () => program, () => getDebugInfo(program, info))
      if (errors.isEmpty) {
        config.reporter report TypeCheckSuccessMessage(config.inputFile, () => program, () => getErasedGhostCode(program, info))
        Right(info)
      } else {
        val typeErrors = program.positions.translate(errors, TypeError)
        config.reporter report TypeCheckFailureMessage(config.inputFile, () => program, typeErrors)
        Left(typeErrors)
      }
    }

  }

  private def getErasedGhostCode(program: PProgram, info: TypeInfoImpl): String = {
    
    

    import viper.gobra.frontend.info.implementation.typing.ghost.separation.GoifyingPrinter
    new GoifyingPrinter(info).format(program)

    //TODO: CHANGE THIS BACK TO GHOSTLESSPRINTER (was used only for testing)
    //new GhostLessPrinter(info).format(program)
  }

  private def getDebugInfo(program: PProgram, info: TypeInfoImpl): String = {
    new InfoDebugPrettyPrinter(info).format(program)
  }
}
