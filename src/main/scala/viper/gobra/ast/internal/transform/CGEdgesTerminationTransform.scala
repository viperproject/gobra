// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.util.Violation

/**
  * Transformation responsible for generating call-graph edges from interface methods to their implementations' methods.
  * This is necessary to soundly verify termination in the presence of dynamic method binding.
  */
object CGEdgesTerminationTransform extends InternalTransform {
  override def name(): String = "add_cg_edges_for_termination_checking"

  /**
    * Program-to-program transformation
    */
  override def transform(p: in.Program): in.Program = p match {
    case in.Program(_, _, table) =>
      var methodsToRemove: Set[in.Member] = Set.empty
      var methodsToAdd: Set[in.Member] = Set.empty
      var definedMethodsDelta: Map[in.MethodProxy, in.MethodLikeMember] = Map.empty

      def isEmbeddedMethod(subTProxy: in.MethodProxy, superTProxy: in.MethodProxy): Boolean = {
        // The proxies for embedded methods defined in interface type superT are the same
        // as the method proxies for the corresponding method in an embedding interface type subT
        subTProxy == superTProxy
      }

      table.getMembers.foreach {
        case (t: in.InterfaceT, proxies) =>
          val implementations = table.lookupImplementations(t)
          proxies.foreach {
            case proxy: in.MethodProxy =>
              table.lookup(proxy) match {
                /**
                  * Transforms the abstract methods from interface declarations into non-abstract methods containing calls
                  * to all implementations' corresponding methods. The new body has the form
                  *   {
                  *     assume false
                  *     if typeOf(recv) == impl1 {
                  *       call implementation method from impl1 on recv.(impl1)
                  *     }
                  *     if typeOf(recv) == impl2 {
                  *       call implementation method from impl2 on recv.(impl2)
                  *     }
                  *     ...
                  *     if typeOf(recv) == implN {
                  *       call implementation method from implN on recv.(implN)
                  *     }
                  *   }
                  */
                case m: in.Method if m.terminationMeasures.nonEmpty && m.receiver.typ == t =>
                  // The restriction `m.receiver.typ` ensures that the member with the addtional call-graph edges
                  // is only generated once, when looking at the original definition of the method (and not, for
                  // example, when looking at an embedding of the method).

                  // only performs transformation if method has termination measures
                  val src = m.info
                  val assumeFalse = in.Assume(in.ExprAssertion(in.BoolLit(b = false)(src))(src))(src)
                  val optCallsToImpls = implementations.toVector.flatMap { subT: in.Type =>
                    table.lookup(subT, proxy.name).toVector.map {

                      case implProxy: in.MethodProxy if !subT.isInstanceOf[in.InterfaceT] =>
                        // looking at a concrete implementation of the method
                        in.If(
                          in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(subT)(src))(src),
                          in.Seqn(Vector(
                            in.MethodCall(
                              m.results map parameterAsLocalValVar,
                              in.TypeAssertion(m.receiver, subT)(src),
                              implProxy, m.args
                            )(src),
                            in.Return()(src)
                          ))(src),
                          in.Seqn(Vector())(src)
                        )(src)

                      case implProxy: in.MethodProxy if subT.isInstanceOf[in.InterfaceT]
                        && isEmbeddedMethod(implProxy, proxy) =>
                        // If the subtype (subT) is an interface type and the method is defined in subT
                        // via an interface embedding, then the contract of the method is the same and
                        // there is no need to generate extra proof obligations.
                        // The soundness of this argument critically relies on the fact that if a type T implements
                        // an interface B and B has interface A embedded, then T must implement A too.
                        in.Seqn(Vector())(src)

                      case _: in.MethodProxy if subT.isInstanceOf[in.InterfaceT] =>
                        Violation.violation(s"Type $subT contains a re-definition of method ${proxy.name}. This is still not supported.")

                      case v => Violation.violation(s"Expected a MethodProxy but got $v instead.")

                    }
                  }
                  val newBody = {
                    in.Block(
                      decls = Vector.empty,
                      stmts = assumeFalse +: optCallsToImpls
                    )(src)
                  }
                  val newMember = in.Method(m.receiver, m.name, m.args, m.results, m.pres, m.posts, m.terminationMeasures, None, Some(newBody.toMethodBody))(src)
                  methodsToRemove += m
                  methodsToAdd += newMember
                  definedMethodsDelta += proxy -> newMember

                case m: in.Method if m.terminationMeasures.nonEmpty && m.receiver.typ != t =>
                  val recvT = m.receiver.typ.asInstanceOf[in.InterfaceT]
                  // Sanity check: no method is ignored by this case analysis
                  Violation.violation(table.lookupImplementations(recvT).contains(t),
                    s"Method ${m.name} found for type $t even though its receiver is not $t or one of its supertypes.")

                /**
                  * Transforms the abstract pure methods from interface declarations into non-abstract pure methods containing calls
                  * to all implementations' corresponding methods. The new body has the form
                  *   {
                  *      true?
                  *       call fallbackFunction on recv :
                  *       (typeOf(recv) == impl1 ? call method from impl1 on recv :
                  *         (typeOf(recv) == impl2 ? call method from impl2 on recv :
                  *           (...  :
                  *             typeOf(recv) == implN ? call implementation method from implN on recv : call fallbackFunction on recv)))
                  *   }
                  *
                  *   This transformation generates a fallbackFunction, an abstract function which receives the receiver and parameters
                  *   of the original method and has the same return type and spec. For the pure method
                  *     requires [PRE]
                  *     ensures  [POST]
                  *     decreases [MEASURE]
                  *     pure func (r recv) M (x1 T1, ..., xN TN) (res TRes)
                  *
                  *   we generate the following fallback:
                  *     requires [PRE]
                  *     ensures  [POST]
                  *     decreases _
                  *     pure func (r recv) M_fallback(x1 T1, ... xN TN) (res TRes)
                  *
                  *   Notice that the postcondition `res == r.m(x1, ..., xN)` is required, because the interface encoding
                  *   generates postconditions of the `M` method at the Viper level (which must be satisfied by the generated body)
                  *   that are not easily reproducible via a transformation at the level of the internal code.
                  *
                  */
                case m: in.PureMethod if m.terminationMeasures.nonEmpty && m.receiver.typ == t =>
                  Violation.violation(m.results.length == 1, "Expected one and only one out-parameter.")
                  // only performs transformation if method has termination measures
                  val src = m.info

                  // the fallback function is called if no comparison succeeds
                  val fallbackProxy = Names.InterfaceMethod.copy(m.name, "fallback")
                  val fallbackTermMeasures = Vector(in.WildcardMeasure(None)(src))
                  val fallbackFunction = m.copy(name = fallbackProxy, terminationMeasures = fallbackTermMeasures, body = None)(src)

                  // new body to check termination
                  val terminationCheckBody = {
                    val returnType = m.results.head.typ
                    val fallbackProxyCall = in.PureMethodCall(m.receiver, fallbackProxy, m.args, returnType)(src)
                    val implProxies: Vector[(in.Type, in.MemberProxy)] = implementations.toVector.flatMap{ impl =>
                      table.lookup(impl, proxy.name).map(implProxy => (impl, implProxy))
                    }
                    val bodyFalseBranch = implProxies.foldLeft[in.Expr](fallbackProxyCall) {
                      case (accum: in.Expr, (subT: in.Type, implMemberProxy: in.MemberProxy)) =>
                        implMemberProxy match {
                          case implProxy: in.MethodProxy if !subT.isInstanceOf[in.InterfaceT] =>
                            in.Conditional(
                              in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(subT)(src))(src),
                              in.PureMethodCall(in.TypeAssertion(m.receiver, subT)(src), implProxy, m.args, returnType)(src),
                              accum,
                              returnType
                            )(src)

                          case implProxy: in.MethodProxy if subT.isInstanceOf[in.InterfaceT]
                            && isEmbeddedMethod(implProxy, proxy) =>
                            // If the subtype (subT) is an interface type and the method is defined in subT
                            // via an interface embedding, then the contract of the method is the same and
                            // there is no need to generate extra proof obligations.
                            // The soundness of this argument critically relies on the fact that if a type T implements
                            // and interface B and B has interface A embedded, then T must implement A too.
                            accum

                          case _: in.MethodProxy if subT.isInstanceOf[in.InterfaceT] =>
                            Violation.violation(s"Type $subT contains a re-definition of method ${proxy.name}. This is still not supported.")

                          case v => Violation.violation(s"Expected a MethodProxy but got $v instead.")
                        }
                    }
                    in.Conditional(in.BoolLit(b = true)(src), fallbackProxyCall, bodyFalseBranch, returnType)(src)
                  }
                  val transformedM = m.copy(terminationMeasures = m.terminationMeasures, body = Some(terminationCheckBody))(src)

                  methodsToRemove += m
                  methodsToAdd += transformedM
                  methodsToAdd += fallbackFunction
                  definedMethodsDelta += fallbackProxy -> fallbackFunction
                  definedMethodsDelta += proxy -> transformedM


                case m: in.PureMethod if m.terminationMeasures.nonEmpty && m.receiver.typ != t =>
                 val recvT = m.receiver.typ.asInstanceOf[in.InterfaceT]
                  // Sanity check: no method is ignored by this case analysis
                  Violation.violation(table.lookupImplementations(recvT).contains(t),
                    s"Pure method ${m.name} found for type $t even though its receiver is not $t or one of its supertypes.")

                case _ =>
              }
            case _ =>
          }

        case _ =>

      }

      in.Program(
        types = p.types,
        members = p.members.diff(methodsToRemove.toSeq).appendedAll(methodsToAdd),
        table = p.table.merge(new in.LookupTable(definedMethods = definedMethodsDelta)),
      )(p.info)
  }

  private def parameterAsLocalValVar(p: in.Parameter): in.LocalVar = {
    in.LocalVar(p.id, p.typ)(p.info)
  }

  private def typeAsExpr(t: in.Type)(src: in.Node.Info): in.Expr = {
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
      case in.StructT(fields: Vector[in.Field], _) =>
        in.StructTExpr(fields.map(field => (field.name, typeAsExpr(field.typ)(src), field.ghost)))(src)
      case _ => Violation.violation(s"no corresponding type expression matched: $t")
    }
  }
}
