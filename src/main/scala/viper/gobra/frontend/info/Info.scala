package viper.gobra.frontend.info

import org.bitbucket.inkytonik.kiama.relation.Tree
import viper.gobra.ast.frontend.{PNode, PProgram}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.reporting.{TypeError, VerifierError}

object Info {
  type GoTree = Tree[PNode, PProgram]

  def check(program: PProgram): Either[Vector[VerifierError], TypeInfo] = {
    val tree = new GoTree(program)
    //    println(program.declarations.head)
    //    println("-------------------")
    //    println(tree)
    val info = new TypeInfoImpl(tree)

    val errors = info.errors
    if (errors.isEmpty) {
      Right(info)
    } else {
      Left(program.positions.translate(errors, TypeError))
    }
  }
}
