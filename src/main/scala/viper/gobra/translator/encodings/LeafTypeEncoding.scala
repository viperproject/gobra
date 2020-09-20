package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.Shared
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

trait LeafTypeEncoding extends TypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Encodes expressions as r-values, i.e. values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * Super implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    *
    * R[ loc: T@ ] -> L[loc]
    */
  override def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = super.rValue(ctx) orElse {
    case (loc: in.Location) :: t / Shared if typ(ctx).isDefinedAt(t) => ctx.typeEncoding.lValue(ctx)(loc)
  }

  /**
    * Encodes expressions as l-values, i.e. values that do occupy some identifiable location in memory.
    * This includes literals and default values.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) shared operations on T
    *
    * L[v: T@] -> Var[v].val
    */
  override def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: t / Shared =>
      val (pos, info, errT) = v.vprMeta
      val vV = variable(ctx)(v).localVar
      unit(vpr.FieldAccess(vV, ctx.fields.field(t)(ctx))(pos, info, errT))
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    *
    * Footprint[loc: T@] -> acc(L[loc])
    */
  override def addressFootprint(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case loc :: t / Shared if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      val perm = vpr.FullPerm()(pos, info, errT)
      val lval = ctx.typeEncoding.lValue(ctx)(loc).map(_.asInstanceOf[vpr.FieldAccess])
      lval.map(l => vpr.FieldAccessPredicate(l, perm)(pos, info, errT))
  }
}
