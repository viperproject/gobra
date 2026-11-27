// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.transformers.hyper.extended

import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.{Config, Hyper}
import viper.gobra.translator.transformers.ViperTransformer
import viper.gobra.translator.util.VprInfo
import viper.silver.ast._
import viper.silver.plugin.sif.{SIFLowExp, SIFRelExp}
import viper.silver.plugin.standard.termination.DecreasesTuple
import viper.silver.verifier.AbstractError

import scala.collection.mutable

/**
  * In hyperMode extended, Viper's termination plugin runs before [[GobraSIFPlugin]] and does not support the
  * information-flow constructs `low` and `rel`. This transformation therefore replaces them in the definitions of
  * hyperfuncs by calls to abstract marker functions, which the termination plugin treats as ordinary calls and
  * [[GobraSIFExtendedTransformer]] translates back.
  */
class HyperfuncMarkerTransformer(config: Config) extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] =
    if (config.hyperModeOrDefault != Hyper.EnabledExtended) Right(task)
    else Right(BackendVerifier.Task(HyperfuncMarkerTransformer.mark(task.program), task.backtrack))
}

object HyperfuncMarkerTransformer {
  val lowMarkerKey: String = "hyperfuncLowMarker"
  val relMarkerKey: String = "hyperfuncRelMarker"

  def isLowMarker(info: Info): Boolean = info.getAllInfos[AnnotationInfo].exists(_.values.contains(lowMarkerKey))
  def isRelMarker(info: Info): Boolean = info.getAllInfos[AnnotationInfo].exists(_.values.contains(relMarkerKey))

  def mark(p: Program): Program = {
    val hyper = p.functions.filter(f => VprInfo.isHyperFunc(f.info))
    if (hyper.isEmpty) return p

    val used = mutable.Set.empty[String] ++ p.collect { case d: Declaration => d.name }
    def fresh(base: String): String = {
      var i = 0
      while (used(s"$base$i")) i += 1
      used += s"$base$i"
      s"$base$i"
    }
    val lowMarkers = mutable.LinkedHashMap.empty[Type, Function]
    val relMarkers = mutable.LinkedHashMap.empty[Type, Function]
    def marker(t: Type, low: Boolean): Function = {
      val (map, key) = if (low) (lowMarkers, lowMarkerKey) else (relMarkers, relMarkerKey)
      map.getOrElseUpdate(t, {
        val args = Seq(LocalVarDecl("e", t)()) ++ (if (low) Seq.empty else Seq(LocalVarDecl("i", Int)()))
        // the marker terminates (it is replaced by `low` or `rel`), which the termination plugin checks for calls
        val terminates = DecreasesTuple(Seq.empty, None)()
        Function(fresh(if (low) "hf_low_marker" else "hf_rel_marker"), args, if (low) Bool else t, Seq(terminates),
          Seq.empty, None)(info = AnnotationInfo(Map(key -> Seq.empty)))
      })
    }
    def markExp(e: Exp): Exp = e.transform {
      case l@SIFLowExp(x, None, _) => FuncApp(marker(x.typ, low = true), Seq(markExp(x)))(l.pos, l.info, l.errT)
      case r@SIFRelExp(x, i) => FuncApp(marker(x.typ, low = false), Seq(markExp(x), i))(r.pos, r.info, r.errT)
    }
    val functions = p.functions.map { f =>
      if (!VprInfo.isHyperFunc(f.info)) f
      else f.copy(pres = f.pres.map(markExp), posts = f.posts.map(markExp), body = f.body.map(markExp))(f.pos, f.info, f.errT)
    }
    p.copy(functions = functions ++ lowMarkers.values ++ relMarkers.values)(p.pos, p.info, p.errT)
  }
}
