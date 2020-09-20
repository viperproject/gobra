package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.encodings.EmbeddingComponent
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter
import viper.gobra.translator.encodings

class SharedArrayComponentImpl extends SharedArrayComponent {

  override def finalize(col: Collector): Unit = {
    emb.finalize(col)
  }

  /** Embeds Arrays of fixed length as specified by ComponentParameter. */
  private val emb: EmbeddingComponent[ComponentParameter] = new encodings.EmbeddingComponent.Impl[ComponentParameter](
    p = (e: vpr.Exp, id: ComponentParameter) => (ctx: Context) => vpr.EqCmp(ctx.array.length(e)(), vpr.IntLit(id._1)())(),
    t = (_: ComponentParameter) => (ctx: Context) => ctx.array.typ()
  )

  /** Returns type of exclusive-array domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type = emb.typ(t)(ctx)

  /** Getter of shared-array domain. */
  def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    ctx.array.slot(emb.unbox(base, t)(ctx), idx)(pos, info, errT) // unbox(base)[idx]
  }

}
