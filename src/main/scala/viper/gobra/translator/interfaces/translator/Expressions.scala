package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.util.ViperWriter.ExprWriter
import viper.silver.{ast => vpr}

trait Expressions
  extends BaseTranslator[in.Expr, ExprWriter[vpr.Exp]] {

  implicit val toFieldAcc: FromToContract[in.Deref, ExprWriter[vpr.FieldAccess]] = new FromToContract[in.Deref, ExprWriter[vpr.FieldAccess]] {}
  implicit val toLocalVar: FromToContract[in.LocalVar, ExprWriter[vpr.LocalVar]] = new FromToContract[in.LocalVar, ExprWriter[vpr.LocalVar]] {}
}
