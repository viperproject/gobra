// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.
package viper.gobra.translator.library.tuples

import viper.gobra.translator.Names
import viper.gobra.translator.library.Simplifier
import viper.silver.ast.utility.rewriter.Traverse
import viper.silver.{ast => vpr}

import scala.collection.mutable

class TuplesImpl extends Tuples {

  /**
    * Finalizes translation. `addMemberFn` is called with any member that is part of the encoding.
    */
  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    generatedDomains foreach addMemberFn
  }

  override def typ(args: Vector[vpr.Type]): vpr.DomainType = {
    val arity = args.size

    vpr.DomainType(
      domain = domain(arity),
      typVarsMap = typeVarMap(args)
    )
  }

  override def create(args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    val arity = args.size

    vpr.DomainFuncApp(
      func = tuple(arity),
      args = args,
      typVarMap = typeVarMap(args map (_.typ))
    )(pos, info, errT)
  }

  override def get(arg: vpr.Exp, index: Int, arity: Int)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    vpr.DomainFuncApp(
      func = getter(index, arity),
      args = Vector(arg),
      typVarMap = arg.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  def tuple(arity: Int): vpr.DomainFunc =
    constructors.getOrElse(arity, {addNTuplesDomain(arity); constructors(arity)})
  def getter(index: Int, arity: Int): vpr.DomainFunc =
    getters.getOrElse((index, arity), {addNTuplesDomain(arity); getters((index, arity))})

  def domain(arity: Int): vpr.Domain =
    domains.getOrElse(arity, {addNTuplesDomain(arity); domains(arity)})

  def typeVarMap(ts: Vector[vpr.Type]): Map[vpr.TypeVar, vpr.Type] =
    domain(ts.length).typVars.zip(ts).toMap

  def generatedDomains: List[vpr.Domain] = _generatedDomains

  private var _generatedDomains: List[vpr.Domain] = List.empty
  private val domains: mutable.Map[Int, vpr.Domain] = mutable.Map.empty
  private val constructors: mutable.Map[Int, vpr.DomainFunc] = mutable.Map.empty
  private val getters: mutable.Map[(Int,Int), vpr.DomainFunc] = mutable.Map.empty

  private def addNTuplesDomain(arity: Int): Unit = {
    val domainName = s"${Names.tupleDomain}$arity"

    val typeVars = 0.until(arity) map (ix => vpr.TypeVar(s"T$ix"))
    val decls = 0.until(arity) map (ix => vpr.LocalVarDecl(s"t$ix", typeVars(ix))())
    val vars = decls map (_.localVar)
    val typVarMap = typeVars.zip(typeVars).toMap

    val domainTyp = vpr.DomainType(domainName, typVarMap)(typeVars)
    val domainDecl = vpr.LocalVarDecl("p", domainTyp)()
    val domainVar = domainDecl.localVar

    val tupleFunc = vpr.DomainFunc(s"tuple$arity",decls, domainTyp)(domainName = domainName)
    val getFuncs = 0.until(arity) map (ix =>
      vpr.DomainFunc(s"get${ix}of$arity", Seq(domainDecl), typeVars(ix))(domainName = domainName)
      )

    val getOverTupleAxiom = {
      val nTupleApp = vpr.DomainFuncApp(tupleFunc, vars, typVarMap)()
      val eqs = 0.until(arity) map {ix =>
        vpr.EqCmp(
          vpr.DomainFuncApp(
            getFuncs(ix),
            Seq(nTupleApp),
            typVarMap
          )(),
          vars(ix)
        )()
      }

      vpr.NamedDomainAxiom(
        name = s"getter_over_tuple$arity",
        exp = vpr.Forall(
          decls,
          Seq(vpr.Trigger(Seq(nTupleApp))()),
          viper.silicon.utils.ast.BigAnd(eqs)
        )()
      )(domainName = domainName)
    }

    val tupleOverGetAxiom = {
      val nGetApp = getFuncs map (f =>
        vpr.DomainFuncApp(f, Seq(domainVar), typVarMap)()
        )

      vpr.NamedDomainAxiom(
        name = s"tuple${arity}_over_getter",
        exp = vpr.Forall(
          Seq(domainDecl),
          nGetApp map (g => vpr.Trigger(Seq(g))()),
          vpr.EqCmp(
            vpr.DomainFuncApp(
              tupleFunc,
              nGetApp,
              typVarMap
            )(),
            domainVar
          )()
        )()
      )(domainName = domainName)
    }

    // there are not quantified variables for tuples of 0 arity. Thus, do not generate any axioms in this case:
    val axioms = if (arity == 0) Seq.empty else Seq(getOverTupleAxiom, tupleOverGetAxiom)
    val domain = vpr.Domain(
      domainName,
      tupleFunc +: getFuncs,
      axioms,
      typeVars
    )()

    domains.update(arity, domain)
    constructors.update(arity, tupleFunc)
    (0 until arity) foreach (ix => getters.update((ix, arity), getFuncs(ix)))

    _generatedDomains ::= domain
  }
}

object TuplesImpl {

  private val typeRegex = """Tuple(\d+)""".r
  private val getterRegex = """get(\d+)of(\d+)""".r
  private val tupleRegex = """tuple(\d+)""".r

  object GetterOverTupleSimplifier extends Simplifier {
    def simplify(node: vpr.Node): Option[vpr.Node] = node match {
      // get_i(tuple(args)) = args[i]
      case vpr.DomainFuncApp(getterRegex(idx, _), Seq(vpr.DomainFuncApp(tupleRegex(_), args, _)), _) => Some(args(idx.toInt))
      case _ => None
    }
  }

  object InlineSimplifier extends Simplifier {

    private def inlineMap(xs: Seq[vpr.Declaration]): Map[String, Seq[vpr.LocalVarDecl]] = {
      def aux(x: vpr.Declaration): Map[String, Seq[vpr.LocalVarDecl]] = x match {
        case x@ vpr.LocalVarDecl(_, t@ vpr.DomainType(typeRegex(_), _)) =>
          val (pos, info, errT) = x.meta
          val types = t.typeParameters.map(te => t.partialTypVarsMap.getOrElse(te, te))
          val shallowInline = types.zipWithIndex.map{ case (te, idx) => vpr.LocalVarDecl(s"${x.name}_$idx", te)(pos, info, errT) }
          Map(x.name -> shallowInline) ++ inlineMap(shallowInline)

        case _ => Map.empty
      }
      xs.foldLeft(Map.empty[String, Seq[vpr.LocalVarDecl]]){ case (l,r) => l ++ aux(r) }
    }

    private def inlineVarDecl(inlineMap: Map[String, Seq[vpr.LocalVarDecl]])(x: vpr.Declaration): Seq[vpr.Declaration] = {
      inlineMap.get(x.name).fold(Seq(x))(_.flatMap(inlineVarDecl(inlineMap)))
    }

    private def transformBody(inlineMap: Map[String, Seq[vpr.LocalVarDecl]])(stmt: vpr.Stmt): vpr.Stmt = {

      def read(x: vpr.LocalVar): vpr.Exp = {
        inlineMap.get(x.name) match {
          case None => x
          case Some(vars) =>
            val typeVars = vars.indices.map(idx => vpr.TypeVar(s"T$idx"))
            val typeVarMap = (typeVars zip vars.map(_.typ)).toMap
            val domainName = s"Tuple${vars.size}"
            val domainType = vpr.DomainType(domainName, typeVarMap)(typeVars)
            vpr.DomainFuncApp(s"tuple${vars.size}", vars map (x => read(x.localVar)), typeVarMap)(x.pos, x.info, domainType, domainName, x.errT)
        }
      }

      def write(s: vpr.LocalVarAssign): vpr.Stmt = {
        inlineMap.get(s.lhs.name) match {
          case None => s
          case Some(vars) =>
            val typeVars = vars.indices.map(idx => vpr.TypeVar(s"T$idx"))
            val typeVarMap = (typeVars zip vars.map(_.typ)).toMap
            val domainName = s"Tuple${vars.size}"

            vpr.Seqn(
              vars.zipWithIndex.map{ case (x, idx) =>
                val assignment = vpr.LocalVarAssign(
                  x.localVar,
                  vpr.DomainFuncApp(s"get${idx}of${vars.size}", Seq(s.rhs), typeVarMap)(s.pos, s.info, x.typ, domainName, s.errT)
                )(s.pos, s.info, s.errT)
                write(assignment)
              }, Seq.empty
            )(s.pos, s.info, s.errT)
        }
      }

      def get(e: vpr.Exp): vpr.Exp = e match {
        case vpr.DomainFuncApp(getterRegex(idx, _), Seq(arg), _) => get(arg) match {
          case x: vpr.LocalVar if inlineMap.contains(x.name) => inlineMap(x.name)(idx.toInt).localVar
          case _ => e
        }
        case _ => e
      }

      stmt.transform({
        case e@ vpr.DomainFuncApp(getterRegex(_, _), _, _) => get(e)
        case x: vpr.LocalVar if inlineMap.contains(x.name) => read(x)
        case s: vpr.LocalVarAssign if inlineMap.contains(s.lhs.name) => write(s)
      }, Traverse.TopDown)
    }

    def simplify (node: vpr.Node): Option[vpr.Node] = {

      node match {
        case seqn: vpr.Seqn =>
          val m = inlineMap(seqn.scopedDecls)

          if (m.isEmpty) None
          else Some(
            vpr.Seqn(
              seqn.ss.map(transformBody(m)),
              seqn.scopedDecls.flatMap(inlineVarDecl(m))
            )(seqn.pos, seqn.info, seqn.errT)
          )

        case _ => None
      }
    }
  }
}
