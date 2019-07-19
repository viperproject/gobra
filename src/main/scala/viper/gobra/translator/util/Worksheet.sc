import viper.silver.{ast => vpr}

val src = vpr.Seqn(Vector.empty, Vector.empty)()
val label = vpr.Label("test", Vector.empty)()

label.duplicateMeta(src.pos, src.info, src.errT)
