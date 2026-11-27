// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import fastparse._
import viper.silver.ast.Program
import viper.silver.ast.utility.rewriter.StrategyBuilder
import viper.silver.parser._
import viper.silver.plugin.sif.{PLowEventExp, PLowEventKeyword, PLowExp, PLowKeyword, PRelKeyword}
import viper.silver.plugin.{ParserPluginTemplate, SilverPlugin}

import scala.annotation.unused
import scala.collection.mutable

/**
  * Hyperfunctions for Viper, on top of Viper's information-flow (SIF) plugin.
  *
  * Syntax (in addition to Viper's):
  * {{{
  * hyperfunction H(x: T, i: Exec, ...): R   // a function over the executions; its parameters are unary, its value rigid
  *   requires P                             // relational assertions
  *   ensures  Q
  * { E }
  *
  * low(e)            // e agrees in the active executions (SIF plugin)
  * rel(e, i)         // the value of e in execution i (0, 1, or an expression of type Exec); defined only if active(i)
  * active(i)         // execution i is active at the current program point
  * lowEvent          // both executions reach this point or none does (SIF plugin)
  * forall i: Exec :: E, exists i: Exec :: E   // quantifiers over the executions
  * }}}
  *
  * Use it instead of the SIF plugin: `--plugin viper.hyperfuncs.HyperfuncsPlugin`. See README.md for the semantics and
  * the static rules.
  */
class HyperfuncsPlugin(@unused reporter: viper.silver.reporter.Reporter,
                       @unused logger: ch.qos.logback.classic.Logger,
                       @unused config: viper.silver.frontend.SilFrontendConfig,
                       fp: FastParser) extends SilverPlugin with ParserPluginTemplate {

  import fp.{ParserExtension, _file, exp, lineCol}
  import viper.silver.parser.FastParserCompanion.{PositionParsing, reservedKw, whitespace}

  def low[$: P]: P[PLowExp] = P(P(PLowKeyword) ~ "(" ~ exp ~ ")").map { case (_, e) => PLowExp(e)(_) }.pos
  def lowEvent[$: P]: P[PLowEventExp] = P(P(PLowEventKeyword)).map { _ => PLowEventExp()(_) }.pos
  def rel[$: P]: P[PHFRelExp] = P(P(PRelKeyword) ~ "(" ~ exp ~ "," ~ exp ~ ")").map { case (_, e, i) => PHFRelExp(e, i)(_) }.pos
  def active[$: P]: P[PHFActiveExp] = P(P(PActiveKeyword) ~ "(" ~ exp ~ ")").map { case (_, i) => PHFActiveExp(i)(_) }.pos

  override def beforeParse(input: String, isImported: Boolean): String = {
    if (HyperfuncsPlugin.registered.add(fp)) {
      ParserExtension.addNewKeywords(Set(PLowKeyword, PLowEventKeyword, PRelKeyword, PActiveKeyword))
      ParserExtension.addNewExpAtEnd(low(_))
      ParserExtension.addNewExpAtEnd(rel(_))
      ParserExtension.addNewExpAtEnd(active(_))
      ParserExtension.addNewExpAtEnd(lowEvent(_))
    }
    // A hyperfunction declaration is an ordinary function declaration with the annotation `@hf()`, which Viper's
    // name analysis, type checking, translation, and the termination plugin treat like any other function. The
    // replacement has the same length as the keyword, such that source positions are unchanged.
    val declarations = HyperfuncsPlugin.declaration.replaceAllIn(input, m => m.group(1) + HyperfuncsPlugin.annotatedFunction)
    // the type of execution identifiers; declared once, in the file that is verified
    if (isImported) declarations else declarations + s"\n\ndomain ${HyperfuncsAst.ExecTypeName} {}\n"
  }

  /** Makes integer literals passed for parameters of type Exec execution identifiers (Viper's surface syntax only). */
  override def beforeResolve(input: PProgram): PProgram = {
    val hyper = input.functions.filter(_.annotations.exists(_.key.str == HyperfuncsAnnotations.surfaceHyperfunctionKey))
    val execPositions: Map[String, Set[Int]] = hyper.map(f => f.idndef.name -> f.formalArgs.zipWithIndex.collect {
      case (a, i) if PExecIndexHelper.isExec(a.typ) => i
    }.toSet).filter(_._2.nonEmpty).toMap

    val literals = StrategyBuilder.Slim[PNode]({
      case c: PCall if execPositions.contains(c.idnref.name) =>
        val positions = execPositions(c.idnref.name)
        val args = c.args.zipWithIndex.map {
          case (l: PIntLit, k) if positions(k) =>
            PHFExecLit(l, PDomainType(PIdnRef[PTypeDeclaration](HyperfuncsAst.ExecTypeName)(l.pos), None)(l.pos))(l.pos)
          case (a, _) => a
        }
        PCall(c.idnref, c.callArgs.update(args), c.typeAnnotated)(c.pos)
    })

    def rewrite(p: PProgram): PProgram = {
      val members = p.members.map(m => if (execPositions.isEmpty) m else literals.execute[PMember](m))
      PProgram(p.imported.map(rewrite), members)(p.pos, p.localErrors, p.offsets, p.rawProgram)
    }
    rewrite(input)
  }

  private var execParams: Map[String, Set[Int]] = Map.empty

  /**
    * Checks the static rules and prepares the program for the termination plugin and the product construction. This
    * hook runs both for programs parsed by Viper and for Viper ASTs produced by front ends, so the hyperfunctions are
    * found through their annotations (see [[HyperfuncsAnnotations]]).
    */
  override def beforeMethodFilter(input: Program): Program = {
    val normalized = HyperfuncsAnnotations.normalize(input)
    val hyperNames = normalized.functions.filter(f => HyperfuncsAnnotations.isHyperfunction(f.info)).map(_.name).toSet
    if (HyperfuncsConfig.checkStaticRules) new HyperfuncsChecks(normalized, hyperNames, reportError).check()
    if (errors.nonEmpty) input
    else {
      val (prepared, params) = HyperfuncsPreprocessing(normalized, hyperNames)
      execParams = params
      prepared
    }
  }

  /** Builds the product program; runs after Viper's default plugins, in particular the termination plugin. */
  override def beforeVerify(input: Program): Program = {
    val transformer = new HyperfuncsTransformer
    transformer.Config.reportError = this.reportError
    // By default, the SIF plugin assumes at the start of every method that execution 0 is active, which removes the
    // case where only execution 1 is active as the mirror image of the case where only execution 0 is. This is sound
    // only for specifications that treat both executions alike (such as `low`). `active(i)` and `rel(e, i)` single
    // out one execution, so methods must be verified for every activation (jcp19/hyperfuncs#13; Gobra does the same
    // in viperproject/gobra#1130).
    transformer.Config.optimizeRestrictActVars = false
    transformer.Config.enableExperimentalFeatures = HyperfuncsConfig.enableExperimentalFeatures
    transformer.Config.enableGotoLowEventEncoding = HyperfuncsConfig.enableGotoLowEventEncoding
    transformer.Config.transformedProgramCallback = HyperfuncsConfig.transformedProgramCallback
    transformer.transformProgram(HyperfuncsPreprocessing.unmark(input), execParams)
  }
}

object HyperfuncsPlugin {
  /** The keyword `hyperfunction` at the start of a line, i.e., of a declaration. */
  private val declaration = "(?m)^([ \\t]*)hyperfunction(?=[ \\t\\n])".r
  /** `@hf()function` has the length of `hyperfunction`. */
  private val annotatedFunction = s"@${HyperfuncsAnnotations.surfaceHyperfunctionKey}()function"
  assert(annotatedFunction.length == "hyperfunction".length)

  /** Parsers whose parser extension already contains this plugin's syntax. */
  private val registered: mutable.Set[FastParser] =
    java.util.Collections.newSetFromMap(new java.util.WeakHashMap[FastParser, java.lang.Boolean]()).asInstanceOf[java.util.Set[FastParser]] match {
      case s => scala.jdk.CollectionConverters.SetHasAsScala(s).asScala
    }
}

private object PExecIndexHelper {
  def isExec(t: PType): Boolean = t match {
    case d: PDomainType => d.domain.name == HyperfuncsAst.ExecTypeName && d.typeArgs.isEmpty
    case _ => false
  }
}
