// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.util.Violation

/* TODO: doc, header */
object CGEdgesTerminationTransform extends InternalTransform {
  override def name(): String = "add_cg_edges_for_termination_checking"

  /**
    * Program-to-program transformation
    */
  override def transform(p: in.Program): in.Program = p match {
    case in.Program(_, _, table) =>
      var methodsToRemove: Set[in.Member] = Set.empty
      var methodsToAdd: Set[in.Member] = Set.empty
      var functionsToAdd: Set[in.PureFunction] = Set.empty
      var definedMethodsDelta: Map[in.MethodProxy, in.MethodLikeMember] = Map.empty
      var definedFunctionsDelta: Map[in.FunctionProxy, in.FunctionLikeMember] = Map.empty

      table.memberProxies.foreach {
        case (t: in.InterfaceT, proxies) =>
          val implementations = table.interfaceImplementations(t)
          proxies.foreach {
            case proxy: in.MethodProxy =>
              table.lookup(proxy) match {
                case m: in.Method if m.terminationMeasures.nonEmpty && m.body.isEmpty =>
                  // only performs transformation if method has termination measures
                  val src = m.getMeta
                  val assumeFalse = in.Assume(in.ExprAssertion(in.BoolLit(b = false)(src))(src))(src)
                  val newBody = {
                    in.Block(
                      decls = Vector.empty,
                      stmts = assumeFalse +: implementations.toVector.flatMap { t: in.Type =>
                        table.lookup(t, proxy.name).map {
                          case implProxy: in.MethodProxy =>
                            in.If(
                              in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(t)(src))(src),
                              in.Seqn(Vector(in.MethodCall(m.results map parameterAsLocalValVar, m.receiver, implProxy, m.args)(src), in.Return()(src)))(src),
                              in.Seqn(Vector())(src)
                            )(src)
                          case v => Violation.violation(s"Expected a MethodProxy but got $v instead.")
                        }
                      }
                    )(src)
                  }
                  val newMember = in.Method(m.receiver, m.name, m.args, m.results, m.pres, m.posts, m.terminationMeasures, Some(newBody))(src)
                  methodsToRemove += m
                  methodsToAdd += newMember
                  definedMethodsDelta += proxy -> newMember

                case m: in.PureMethod if m.terminationMeasures.nonEmpty && m.body.isEmpty =>
                  // only performs transformation if method has termination measures
                  val src = m.getMeta

                  // the fallback function is called if no comparison succeeds
                  val fallbackName = s"${m.name}$$fallback"
                  val fallbackProxy = in.FunctionProxy(fallbackName)(src)
                  val fallbackFunction = in.PureFunction(fallbackProxy, m.receiver +: m.args, m.results, m.pres, m.posts, m.terminationMeasures, None)(src)
                  val returnType =
                    if (m.results.length == 1) {
                      m.results.head.typ
                    } else if (m.results.length > 1) {
                      in.TupleT(m.results.map(_.typ), Addressability.outParameter)
                    } else {
                      Violation.violation("Expected at least one out-parameter, but got 0 instead.")
                    }
                  val newBody = implementations.toVector.foldLeft[in.Expr](
                    in.PureFunctionCall(
                      fallbackProxy,
                      m.receiver +: m.args,
                      returnType
                    )(src)
                  ) {
                    case (accum: in.Expr, impl: in.Type) =>
                      table.lookup(impl, proxy.name) match {
                        case Some(implProxy: in.MethodProxy) =>
                          in.Conditional(
                            in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(impl)(src))(src),
                            in.PureMethodCall(m.receiver, implProxy, m.args, returnType)(src),
                            accum,
                            returnType
                          )(src)
                        case None => accum
                        case v => Violation.violation(s"Expected a MethodProxy but got $v instead.")
                      }
                  }
                  val newMember = in.PureMethod(m.receiver, m.name, m.args, m.results, m.pres, m.posts, m.terminationMeasures, Some(newBody))(src)

                  methodsToRemove += m
                  methodsToAdd += newMember
                  functionsToAdd += fallbackFunction
                  definedFunctionsDelta += fallbackProxy -> fallbackFunction
                  definedMethodsDelta += proxy -> newMember

                case _ =>
              }
            case _ =>
          }

        case _ =>

      }

      in.Program(
        types = p.types,
        members = p.members.diff(methodsToRemove.toSeq).appendedAll(methodsToAdd ++ functionsToAdd),
        table = new in.LookupTable(
          definedTypes = table.getDefinedTypes,
          definedMethods = table.getDefinedMethods ++ definedMethodsDelta,
          definedFunctions = table.getDefinedFunctions ++ definedFunctionsDelta,
          definedMPredicates = table.getDefinedMPredicates,
          definedFPredicates = table.getDefinedFPredicates,
          memberProxies = table.memberProxies,
          interfaceImplementations = table.interfaceImplementations,
          implementationProofPredicateAliases = table.getImplementationProofPredicateAliases
        )
      )(p.info)
  }

  private def parameterAsLocalValVar(p: in.Parameter): in.LocalVar = {
    in.LocalVar(p.id, p.typ)(p.info)
  }

  private def typeAsExpr(t: in.Type)(src: in.Node.Meta): in.Expr = {
    t match {
      case in.BoolT(_) => in.BoolTExpr()(src)
      case in.IntT(_, kind) => in.IntTExpr(kind)(src)
      case in.StringT(_) => in.StringTExpr()(src)
      case in.PermissionT(_) => in.PermTExpr()(src)
      case in.ArrayT(length, elems, _) => in.ArrayTExpr(in.IntLit(length)(src), typeAsExpr(elems)(src))(src)
      case in.SliceT(elems, _) => in.SliceTExpr(typeAsExpr(elems)(src))(src)
      case in.MapT(keys, values, _) => in.MapTExpr(typeAsExpr(keys)(src), typeAsExpr(values)(src))(src)
      case in.SequenceT(t, _) => in.SequenceTExpr(typeAsExpr(t)(src))(src)
      case in.SetT(t, _) => in.SetTExpr(typeAsExpr(t)(src))(src)
      case in.MultisetT(t, _) => in.MultisetTExpr(typeAsExpr(t)(src))(src)
      case in.MathMapT(keys, values, _) => in.MathMapTExpr(typeAsExpr(keys)(src), typeAsExpr(values)(src))(src)
      case in.OptionT(t, _) => in.OptionTExpr(typeAsExpr(t)(src))(src)
      case in.DefinedT(name, _) => in.DefinedTExpr(name)(src)
      case in.PointerT(t, _) => in.PointerTExpr(typeAsExpr(t)(src))(src)
      case in.TupleT(ts, _) => in.TupleTExpr(ts map(typeAsExpr(_)(src)))(src)
      case in.StructT(_, fields: Vector[in.Field], _) =>
        in.StructTExpr(fields.map(field => (field.name, typeAsExpr(field.typ)(src), field.ghost)))(src)
      case _ => Violation.violation(s"no corresponding type expression matched: $t")
    }
  }
}
