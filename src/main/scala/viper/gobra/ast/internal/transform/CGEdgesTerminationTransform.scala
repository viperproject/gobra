// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.InvalidImplTermMeasureAnnotation
import viper.gobra.reporting.Source.Parser.Single
import viper.gobra.translator.Names
import viper.gobra.util.Violation

/**
  * Transformation responsible for generating call-graph edges from interface methods to their implementations' methods.
  * This is necessary to soundly verify termination in the presence of dynamic method binding.
  */
object CGEdgesTerminationTransform extends InternalTransform {
  override def name(): String = "add_cg_edges_for_termination_checking"

  /** Prefix of the local variables receiving the results of the calls in a call-graph stub. */
  private val CGEdgeTargetPrefix: String = "$cgEdge$"

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
                  * Transforms the abstract method `m` from an interface declaration into a non-abstract method
                  * containing calls to all implementations' corresponding methods. This transformation introduces
                  * an edge in the call graph from the interface methods to its implementations, which allows the
                  * termination plugin of Viper to identify non-terminating recursion caused by calling interface methods
                  * from interface implementations. The new body has the form
                  *   {
                  *     if typeOf(recv) == impl1 {
                  *       assume (not inhale!) precondition of method from impl1 on recv.(impl1)
                  *       call implementation method from impl1 on recv.(impl1)
                  *       assume false // skip proof of postcondition
                  *       // These early returns may be useful to terminate branches early,
                  *       // instead of allowing them to go over all other if statements.
                  *       return
                  *     }
                  *     if typeOf(recv) == impl2 {
                  *       assume (not inhale!) precondition of method from impl2 on recv.(impl2)
                  *       call implementation method from impl2 on recv.(impl2)
                  *       assume false // skip proof of postcondition
                  *       return
                  *     }
                  *     ...
                  *     if typeOf(recv) == implN {
                  *       assume (not inhale!) precondition of method from implN on recv.(implN)
                  *       call implementation method from implN on recv.(implN)
                  *       assume false // skip proof of postcondition
                  *       return
                  *     }
                  *     assume false // skip proof of postcondition
                  *   }
                  *
                  *  The (impure) assumption of the precondition of each implementation of method m is introduced to
                  *  avoid reporting errors that are caused by the precondition of the implementation not being implied
                  *  by the precondition of the method specification in the interface declaration. These errors are
                  *  already reported by the implementation proofs, and there is no need to replicate them.
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
                        val (implMethRecv, implMethArgs, implMethPres, implMethTerm) = table.lookup(implProxy) match {
                          case m: in.MethodMember =>
                            (m.receiver, m.args, m.pres, m.terminationMeasures)
                          case m: in.BuiltInMethod =>
                            // currently, we are not able to extract the necessary pieces of information from a BuiltInMethod
                            Violation.violation(s"Unexpected member $m found.")
                        }
                        val substs: Map[in.Parameter.In, in.Expr] =
                          implMethArgs
                            .zip(m.args).toMap
                            .updated(implMethRecv, in.TypeAssertion(m.receiver, subT)(src))
                        val presToAssume = implMethPres.map(_.replace(substs))
                        val assumesImplPres = presToAssume.map[in.Stmt](in.Assume(_)(src))
                        val annotatedSrc = annotateWithTerminationError(implMethTerm.headOption.map(_.info).getOrElse(src))
                        // looking at a concrete implementation of the method
                        in.If(
                          in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(subT)(src))(src),
                          in.Seqn(assumesImplPres ++ Vector[in.Stmt](
                            // we annotate this method call with annotatedSrc so that termination errors are reported
                            // at the termination measure of the method implementation.
                            in.MethodCall(
                              m.results map parameterAsLocalValVar,
                              in.TypeAssertion(m.receiver, subT)(src),
                              implProxy,
                              m.args
                            )(annotatedSrc),
                            assumeFalse,
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
                  val stmts = optCallsToImpls :+ assumeFalse
                  val newBody = in.Block(decls = Vector.empty, stmts = stmts)(src)
                  val newMember = in.Method(m.receiver, m.name, m.args, m.results, m.pres, m.posts, m.terminationMeasures, Vector.empty, Some(newBody.toMethodBody))(src)
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
                  val fallbackTermMeasures = Vector(in.NonItfMethodWildcardMeasure(None)(src))
                  val fallbackFunction = m.copy(name = fallbackProxy, terminationMeasures = fallbackTermMeasures, body = None)(src)

                  // new body to check termination
                  val terminationCheckBody = {
                    val returnType = m.results.head.typ
                    val fallbackProxyCall = in.PureMethodCall(m.receiver, fallbackProxy, m.args, returnType, false)(src)
                    val implProxies: Vector[(in.Type, in.MemberProxy)] = implementations.toVector.flatMap{ impl =>
                      table.lookup(impl, proxy.name).map(implProxy => (impl, implProxy))
                    }
                    val bodyFalseBranch = implProxies.foldLeft[in.Expr](fallbackProxyCall) {
                      case (accum: in.Expr, (subT: in.Type, implMemberProxy: in.MemberProxy)) =>
                        implMemberProxy match {
                          case implProxy: in.MethodProxy if !subT.isInstanceOf[in.InterfaceT] =>
                            in.Conditional(
                              in.EqCmp(in.TypeOf(m.receiver)(src), typeAsExpr(subT)(src))(src),
                              in.PureMethodCall(in.TypeAssertion(m.receiver, subT)(src), implProxy, m.args, returnType, false)(src),
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

      val membersWithCGEdges = p.members.diff(methodsToRemove.toSeq).appendedAll(methodsToAdd)

      // Interface methods that a body-less member could dispatch to. Only methods with termination
      // measures matter: a call to a member without measures already fails the plugin's termination
      // condition check, irrespective of any cycle.
      val itfMethods = membersWithCGEdges.collect {
        case m: in.Method if m.receiver.typ.isInstanceOf[in.InterfaceT] && m.terminationMeasures.nonEmpty => m
      }.toVector

      var stubbedMethods: Map[in.MethodProxy, in.MethodLikeMember] = Map.empty
      var stubbedFunctions: Map[in.FunctionProxy, in.FunctionLikeMember] = Map.empty

      val membersWithStubs = if (itfMethods.isEmpty) membersWithCGEdges else membersWithCGEdges.map {
        // an interface method dispatches to the implementations of the package under verification,
        // and, for all we know here, to implementations declared in packages that import this one
        case m: in.Method if m.receiver.typ.isInstanceOf[in.InterfaceT] && m.terminationMeasures.nonEmpty =>
          val stubbed = m.copy(body = Some(appendCallGraphStub(m.body, itfMethods, m.info)))(m.info)
          stubbedMethods += m.name -> stubbed
          stubbed
        case m: in.Method if needsCallGraphStub(m.bodyErased, m.terminationMeasures) =>
          val stubbed = m.copy(body = Some(callGraphStub(itfMethods, m.info)))(m.info)
          stubbedMethods += m.name -> stubbed
          stubbed
        case f: in.Function if needsCallGraphStub(f.bodyErased, f.terminationMeasures) =>
          val stubbed = f.copy(body = Some(callGraphStub(itfMethods, f.info)))(f.info)
          stubbedFunctions += f.name -> stubbed
          stubbed
        case m => m
      }

      in.Program(
        types = p.types,
        members = membersWithStubs,
        table = p.table.merge(new in.LookupTable(
          definedMethods = definedMethodsDelta ++ stubbedMethods,
          definedFunctions = stubbedFunctions,
        )),
      )(p.info)
  }

  /**
    * Members of imported packages are encoded without a body, because imported packages are parsed
    * spec-only. Viper's termination plugin builds its call graph exclusively from bodies, so such a
    * member has no outgoing edges and can never be part of a strongly connected component. Every
    * recursion that runs back through an imported body is therefore invisible to the plugin, and the
    * decrease checks along that cycle are silently skipped.
    *
    * A member without a body is given a body that only exists to contribute those edges:
    *   {
    *     assume false
    *     call I1.m1(dflt, ..., dflt)
    *     ...
    *     call In.mn(dflt, ..., dflt)
    *   }
    * covering every interface method of the program, since it is not known which of them the erased
    * body could dispatch to. The leading `assume false` makes the body vacuous, so no proof
    * obligation arises from it; the calls serve only to close the call graph, which is enough for the
    * plugin to place the member in the right component and to check the measures of its callers.
    *
    * The same stub is appended to the dispatch body of every interface method. That body only covers
    * the implementations visible here, but an interface declared in this package may be implemented
    * by any package importing it, and those implementations may dispatch anywhere. Without the stub,
    * an interface method with no visible implementation has no outgoing edges at all, and a call to
    * it is never part of a component.
    *
    * Only members whose body was erased are stubbed. A member that is abstract or trusted by design
    * has no body to hide: its contract is what the author asked to be assumed, exactly like a
    * wildcard termination measure, and giving it edges would retract that assumption.
    */
  private def needsCallGraphStub(bodyErased: Boolean,
                                 measures: Vector[in.TerminationMeasure]): Boolean =
    bodyErased && measures.nonEmpty

  private def appendCallGraphStub(body: Option[in.MethodBody],
                                  itfMethods: Vector[in.Method],
                                  src: Source.Parser.Info): in.MethodBody = {
    val stub = callGraphStub(itfMethods, src)
    body match {
      case None => stub
      case Some(b) =>
        in.MethodBody(
          b.decls ++ stub.decls,
          in.MethodBodySeqn(b.seqn.stmts ++ stub.seqn.stmts)(b.seqn.info),
          b.postprocessing
        )(b.info)
    }
  }

  private def callGraphStub(itfMethods: Vector[in.Method], src: Source.Parser.Info): in.MethodBody = {
    val assumeFalse = in.Assume(in.ExprAssertion(in.BoolLit(b = false)(src))(src))(src)
    val (targetss, calls) = itfMethods.zipWithIndex.map { case (itf, i) =>
      val targets = itf.results.zipWithIndex.map { case (res, j) =>
        in.LocalVar(s"$CGEdgeTargetPrefix$i$$$j", res.typ)(src)
      }
      val call = in.MethodCall(
        targets,
        in.DfltVal(itf.receiver.typ)(src),
        itf.name,
        itf.args.map(a => in.DfltVal(a.typ)(src))
      )(src)
      (targets, call)
    }.unzip
    in.Block(targetss.flatten, assumeFalse +: calls)(src).toMethodBody
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
      case in.ArrayT(length, elems, _) => in.ArrayTExpr(length, typeAsExpr(elems)(src))(src)
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
      case in.StructT(fields: Vector[in.Field], _, _) =>
        in.StructTExpr(fields.map(field => (field.name, typeAsExpr(field.typ)(src), field.ghost)))(src)
      case _ => Violation.violation(s"no corresponding type expression matched: $t")
    }
  }

  private def annotateWithTerminationError(info: Source.Parser.Info): Source.Parser.Info = info match {
    case s: Single => s.createAnnotatedInfo(InvalidImplTermMeasureAnnotation())
    case i => i
  }
}
