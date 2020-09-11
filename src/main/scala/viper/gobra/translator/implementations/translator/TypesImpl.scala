package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.translator.interfaces.translator.Types
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class TypesImpl extends Types {

  override def finalize(col: Collector): Unit = ()

  /**
    * [bool]   -> bool
    * [int]    -> int
    * [frac]   -> perm
    * [n := t] -> [t]
    * [*t]     -> ref
    * [nil]    -> ref
    * [S]      -> ??? // TODO: will be the tuple type
    * [array n t] -> array
    * [arraySeq n t] -> seq[t]
    * [seq t]  -> seq[t]
    * [set t]  -> set[t]
    * [mset t] -> mset[t]
    * [void] undef
    */
  override def translate(x: in.Type)(ctx: Context): vpr.Type = x match {
    case in.BoolT(_) => vpr.Bool
    case in.IntT(_)  => vpr.Int
    case in.PermissionT(_) => vpr.Perm
    case t: in.DefinedT => translate(ctx.typeProperty.underlyingType(t)(ctx))(ctx)
    case in.PointerT(_, _) => vpr.Ref
    case in.NilT => vpr.Ref
    case in.StructT(_, fields, _) => fields.length match {
      case 1 => translate(fields.head.typ)(ctx)
      case _ => ctx.tuple.typ(fields.map(f => translate(f.typ)(ctx)))
    }
    case in.TupleT(_, _) => Violation.violation("Tuple types are not supported at this point in time")
    case in.ArrayT(_, elem, Addressability.Exclusive) => vpr.SeqType(translate(elem)(ctx))
    case in.ArrayT(_, _, Addressability.Shared) => ctx.array.typ()
    case in.SequenceT(elem, _) => vpr.SeqType(translate(elem)(ctx))
    case in.SetT(elem, _) => vpr.SetType(translate(elem)(ctx))
    case in.MultisetT(elem, _) => vpr.MultisetType(translate(elem)(ctx))
    case in.VoidT => Violation.violation("void is not a translatable type")
  }
}
