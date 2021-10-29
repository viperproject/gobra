// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform
import viper.gobra.ast.{internal => in}
import viper.gobra.util.Violation

/* TODO: doc, header */
object CGEdgesTerminationTransform extends InternalTransform {
  override def name(): String = "add_cg_edges_for_termination_checking"

  /**
    * Program-to-program transformation
    */
  override def transform(p: in.Program): in.Program = p match {
    // TODO: check if types with an interface as underlying type also are encoded as InterfaceT
    case in.Program(types, members, table) =>
      var membersDelta: Map[in.Member, in.Member] = Map.empty
      var definedMethodsDelta: Map[in.MethodProxy, in.MethodLikeMember] = Map.empty

      table.memberProxies.foreach {
        case (t: in.InterfaceT, proxies) =>
          val implementations = table.interfaceImplementations(t)
          // TODO: test what happens for methods with receiver type interface that do not belong to signature method
          proxies.foreach {
            case proxy: in.MethodProxy =>
              table.lookup(proxy) match {
                case m: in.Method if m.terminationMeasures.nonEmpty && m.body.isEmpty =>
                  // only perform transformation if method has termination measures
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
                  membersDelta += m -> newMember
                  definedMethodsDelta += proxy -> newMember

                case m: in.PureMethod => /*
                  val src = m.getMeta
                  val newBody = ???
                  // TODO: generate a new function as the fallback of the case analysis on the type of the receiver
                  */

                  /* Cheng's code:
              val helperProxy = in.MethodProxy(proxy.name, proxy.uniqueName + "_helper" )(src)
              val helperFunction = in.PureMethod(recv, helperProxy, args, returns, pres, posts, terminationMeasures, None)(src)
              val default = in.PureMethodCall(recv, helperProxy, args, returns.head.typ)(src)
              definedMethods += (helperProxy -> helperFunction)
              def helper(list: List[in.Type]): in.Expr = {
                list match {
                  case x::xs => {
                    val implProxy = proxies.get(x) match {
                      case Some(set) =>
                        val setfiltered = set.filter(_.name == mem.name.name)
                        Violation.violation(setfiltered.size == 1, s"None unique method proxy for implementation found")
                        Violation.violation(setfiltered.head.isInstanceOf[in.MethodProxy], s"Unexpected proxy type for implementation")
                        setfiltered.head.asInstanceOf[in.MethodProxy]
                      case None => Violation.violation(s"Method proxies not found for the type $x")
                    }
                    in.Conditional(in.EqCmp(in.TypeOf(recv)(src), typeAsExpr(x)(src).res)(src),
                      in.PureMethodCall(recv, implProxy, args, returns.head.typ)(src), helper(xs), returns.head.typ)(src)
                  }
                  case Nil => default
                }
              }
              val impls = interfaceImplementations.get(dT)
              val expr: in.Expr = impls match {
                case Some(set) => {
                  val list = set.toList
                  in.Conditional(in.BoolLit(true)(src), default, helper(list), returns.head.typ)(src)
                }
                case None => in.Conditional(in.BoolLit(true)(src), default, default, returns.head.typ)(src)
              }
              val newMem = in.PureMethod(recv, proxy, args, returns, pres, posts, terminationMeasures, Some(expr))(src)
              definedMethods += (proxy -> newMem)
              AdditionalMembers.addMember(newMem)
            }
                   */

                  /*
                  val newMember = in.PureMethod(m.receiver, m.name, m.args, m.results, m.pres, m.posts, m.terminationMeasures, Some(newBody))(src)
                  // TODO: print generated members for debugging
                  membersDelta += m -> newMember
                  definedMethodsDelta += proxy -> newMember
                  ???
                  *
                   */

                case _ =>
              }
            case _ =>
          }

        case _ =>

      }

      in.Program(
        types = p.types,
        members = p.members.diff(membersDelta.keys.toSeq).appendedAll(membersDelta.values),
        table = new in.LookupTable(
          definedTypes = table.getDefinedTypes,
          definedMethods = table.getDefinedMethods ++ definedMethodsDelta,
          definedFunctions = table.getDefinedFunctions,
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
