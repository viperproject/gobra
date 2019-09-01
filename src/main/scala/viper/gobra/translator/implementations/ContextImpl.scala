package viper.gobra.translator.implementations

import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.translator._
import viper.gobra.translator.interfaces.components._
import viper.silver.ast.LocalVarDecl

case class ContextImpl(
                        tuple: Tuples,
                        ass: Assertions,
                        expr: Expressions,
                        method: Methods,
                        pureMethod: PureMethods,
                        predicate: Predicates,
                        stmt: Statements,
                        typ: Types,
                        loc: Locations
                      ) extends Context {

  def this(conf: TranslatorConfig) {
    this(
      conf.tuple,
      conf.ass,
      conf.expr,
      conf.method,
      conf.pureMethod,
      conf.predicate,
      conf.stmt,
      conf.typ,
      conf.loc
    )
  }

  /** copy constructor */
  override def :=(
                   tupleN: Tuples = tuple,
                   assN: Assertions = ass,
                   exprN: Expressions = expr,
                   methodN: Methods = method,
                   pureMethodN: PureMethods = pureMethod,
                   predicateN: Predicates = predicate,
                   stmtN: Statements = stmt,
                   typN: Types = typ,
                   locN: Locations = loc,
                 ): Context = copy(
    tuple,
    assN,
    exprN,
    methodN,
    pureMethodN,
    predicateN,
    stmtN,
    typN
  )

  override def addVars(vars: LocalVarDecl*): Context = this
}
