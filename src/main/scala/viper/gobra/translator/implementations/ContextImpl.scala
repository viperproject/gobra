package viper.gobra.translator.implementations

import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.translator._
import viper.gobra.translator.interfaces.components._
import viper.silver.ast.LocalVarDecl

case class ContextImpl(
                        fixpoint: Fixpoint,
                        tuple: Tuples,
                        typeProperty: TypeProperties,
                        ass: Assertions,
                        expr: Expressions,
                        method: Methods,
                        pureMethod: PureMethods,
                        predicate: Predicates,
                        stmt: Statements,
                        typ: Types,
                        loc: Locations,
                        table: LookupTable
                      ) extends Context {

  def this(conf: TranslatorConfig, table: LookupTable) {
    this(
      conf.fixpoint,
      conf.tuple,
      conf.typeProperty,
      conf.ass,
      conf.expr,
      conf.method,
      conf.pureMethod,
      conf.predicate,
      conf.stmt,
      conf.typ,
      conf.loc,
      table
    )
  }

  /** copy constructor */
  override def :=(
                   fixpointN: Fixpoint = fixpoint,
                   tupleN: Tuples = tuple,
                   typePropertyN: TypeProperties = typeProperty,
                   assN: Assertions = ass,
                   exprN: Expressions = expr,
                   methodN: Methods = method,
                   pureMethodN: PureMethods = pureMethod,
                   predicateN: Predicates = predicate,
                   stmtN: Statements = stmt,
                   typN: Types = typ,
                   locN: Locations = loc,
                 ): Context = copy(
    fixpoint,
    tuple,
    typeProperty,
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
