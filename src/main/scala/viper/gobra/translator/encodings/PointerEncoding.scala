package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PointerEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    *
    * Type[(*T)°] -> Type[T]
    * Type[(*T)@] -> Ref
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.*(t) / m =>
      m match {
        case Exclusive => ctx.typeEncoding.typ(ctx)(t)
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as l-values, i.e. values that do occupy some identifiable location in memory.
    * This includes literals and default values.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) shared operations on T
    *
    * Super implements: L[v: T@] -> Var[v].val
    *
    * L[*(e: T°)] -> L[e]
    * L[*(e: T@)] -> L[e].val
    */
  override def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = super.lValue(ctx) orElse {
    case (loc: in.Deref) :: t / Shared =>
      loc.exp.typ.addressability match {
        case Exclusive =>
          ctx.typeEncoding.lValue(ctx)(loc.exp.asInstanceOf[in.Location])

        case Shared =>
          val (pos, info, errT) = loc.vprMeta
          for {
            recv <- ctx.typeEncoding.lValue(ctx)(loc.exp.asInstanceOf[in.Location])
            f = ctx.fields.field(t)(ctx)
          } yield vpr.FieldAccess(recv, f)(pos, info, errT)
      }
  }

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * Super implements shared variables with [[variable]].
    *
    * Ref[*e] -> L[e]
    */
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = super.reference(ctx) orElse {
    case (loc: in.Deref) :: _ / Shared =>
      ctx.typeEncoding.lValue(ctx)(loc.exp.asInstanceOf[in.Location])
  }
}
