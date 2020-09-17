package viper.gobra.translator.encodings.combinators

import viper.gobra.translator.encodings.TypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.reporting.Source.Parser.Info
import viper.silver.{ast => vpr}

/**
  * Combines a vector of type encodings.
  */
abstract class TypeEncodingCombiner(encodings: Vector[TypeEncoding]) extends TypeEncoding {

  /**
    * Combines partial functions selected by 'get' into a single partial function
    * @param get function selecting a partial function of the type encoding.
    * @return combined partial function.
    */
  protected[combinators] abstract def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y


  override def finalize(col: Collector): Unit = encodings.foreach(_.finalize(col))
  override def variable(ctx: Context): in.Var ==> Vector[vpr.LocalVarDecl] = combiner(_.variable(ctx))
  override def precondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = combiner(_.precondition(ctx))
  override def postcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = combiner(_.postcondition(ctx))
  override def initialization(ctx: Context): in.BottomDeclaration ==> CodeWriter[vpr.Stmt] = combiner(_.initialization(ctx))
  override def typ(ctx: Context): in.Type ==> Vector[vpr.Type] = combiner(_.typ(ctx))
  override def assignment(ctx: Context): (in.Assignee, in.Expr, Info) ==> CodeWriter[vpr.Stmt] = combiner(_.assignment(ctx))
  override def equal(ctx: Context): (in.Expr, in.Expr, Info) ==> CodeWriter[vpr.Exp] = combiner(_.equal(ctx))
  override def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = combiner(_.rValue(ctx))
  override def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = combiner(_.lValue(ctx))
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = combiner(_.reference(ctx))
  override def access(ctx: Context): in.Access ==> CodeWriter[vpr.Exp] = combiner(_.access(ctx))
  override def make(ctx: Context): in.Make ==> CodeWriter[vpr.Stmt] = combiner(_.make(ctx))
}
