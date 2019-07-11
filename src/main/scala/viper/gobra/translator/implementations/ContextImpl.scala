package viper.gobra.translator.implementations

import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.translator._
import viper.silver.ast.LocalVarDecl

case class ContextImpl(
                        ass: Assertions,
                        expr: Expressions,
                        func: Functions,
                        method: Methods,
                        stmt: Statements,
                        typ: Types,
                        loc: Locations
                      ) extends Context {

  def this(conf: TranslatorConfig) {
    this(conf.ass, conf.expr, conf.func, conf.method, conf.stmt, conf.typ, conf.loc)
  }

  /** copy constructor */
  override def :=(
                   assN: Assertions = ass,
                   exprN: Expressions = expr,
                   funcN: Functions = func,
                   methodN: Methods = method,
                   stmtN: Statements = stmt,
                   typN: Types = typ,
                   locN: Locations = loc,
                 ): Context = copy(
    assN, exprN, funcN, methodN, stmtN, typN
  )

  override def addVars(vars: LocalVarDecl*): Context = this
}
