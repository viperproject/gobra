// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.theory.{Comparability, TypeHead}
import viper.gobra.ast.{internal => in}
import viper.gobra.dependencyAnalysis.ImplementationProofSourceInfo
import viper.gobra.reporting._
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.{Algorithms, Violation}
import viper.silicon.dependencyAnalysis.DependencyAnalysisJoinNodeInfo
import viper.silver.ast.MakeInfoPair
import viper.silver.plugin.standard.termination
import viper.silver.verifier.ErrorReason
import viper.silver.{ast => vpr}

import scala.collection.SortedSet

class InterfaceEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberWriter, MemberLevel => ml}

  private val interfaces: InterfaceComponent = new InterfaceComponentImpl
  private val types: TypeComponent = new TypeComponentImpl
  private val poly: PolymorphValueComponent = new PolymorphValueComponentImpl(interfaces, types)
  private val utils: InterfaceUtils = new InterfaceUtils(interfaces, types, poly)


  private var genMembers: List[vpr.Member] = List.empty

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    poly.finalize(addMemberFn)
    types.finalize(addMemberFn)
    toInterfaceFunc.finalize(addMemberFn)
    genMembers foreach addMemberFn
    typeOfWithSubtypeFactFuncMap.values foreach addMemberFn
    genPredicates foreach addMemberFn
  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Interface(_) / m =>
      m match {
        case Exclusive => vprInterfaceType(ctx)
        case Shared    => vpr.Ref: vpr.Type
      }

    case in.SortT / m =>
      m match {
        case Exclusive => types.typ()(ctx)
        case Shared    => vpr.Ref
      }
  }

  private def vprInterfaceType(ctx: Context): vpr.Type = {
    interfaces.typ(poly.typ()(ctx), types.typ()(ctx))(ctx)
  }

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = default(super.member(ctx)) {
    // predicate encoding is overwritten because different predicates are encoded to the same Viper predicate.
    case p: in.FPredicate if hasFamily(p.name)(ctx) => ctx.predicate(p); ml.unit(Vector.empty)
    case p: in.MPredicate if hasFamily(p.name)(ctx) => ctx.predicate(p); ml.unit(Vector.empty)
  }

  override def predicate(ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = {
    case p: in.MPredicate if hasFamily(p.name)(ctx) =>
      mpredicate(p)(ctx)

    case p: in.FPredicate if hasFamily(p.name)(ctx) =>
      fpredicate(p)(ctx)
  }

  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    case p: in.PureMethod if p.receiver.typ.isInstanceOf[in.InterfaceT] =>
      function(p)(ctx)

    case p: in.PureMethodSubtypeProof =>
      functionProof(p)(ctx)
  }

  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    case p: in.Method if p.receiver.typ.isInstanceOf[in.InterfaceT] =>
      // adds the precondition that the receiver is not equal to the nil interface
      val (pos, info: Source.Verifier.Info, errT) = p.vprMeta
      for {
        m <- ctx.defaultEncoding.method(p)(ctx)
        recv = m.formalArgs.head.localVar // receiver is always the first parameter
        mWithNotNilCheck = m.copy(pres = utils.receiverNotNil(recv)(pos, info, errT)(ctx) +: m.pres)(pos, info, errT)
      } yield mWithNotNilCheck

    case p: in.MethodSubtypeProof =>
      methodProof(p)(ctx)
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    * An encoding for type T should be defined at left-hand sides of type T and exclusive *T.
    * (Except the encoding of pointer types, which is not defined at exclusive *T to avoid a conflict).
    *
    * The default implements:
    * [lhs: *interface{...}° == rhs: *interface{...}] -> [lhs] == [rhs]
    *
    * [lhs: interface{...} == rhs: interface{...}] -> [lhs] == [rhs]
    * [itf: interface{...} == oth: T] -> [itf == toInterface(oth)]
    * [oth: T == itf: interface{...}] -> [itf == toInterface(oth)]
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = default(super.equal(ctx)) {
    case (lhs :: ctx.Interface(_), rhs :: ctx.Interface(_), src) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield vpr.EqCmp(vLhs, vRhs)(pos, info, errT)

    case (itf :: ctx.Interface(_), oth :: ctx.NotInterface(), src) =>
      equal(ctx)(itf, in.ToInterface(oth, itf.typ)(src.info), src)

    case (oth :: ctx.NotInterface(), itf :: ctx.Interface(_), src) =>
      equal(ctx)(itf, in.ToInterface(oth, itf.typ)(src.info), src)
  }

  /** also checks that the compared interface is comparable. */
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = default(super.goEqual(ctx)) {
    case (lhs :: ctx.Interface(_), rhs :: ctx.Interface(_), src) if lhs.isInstanceOf[in.NilLit] || rhs.isInstanceOf[in.NilLit] =>
      // Optimization:
      // The case where an interface value is compared to the nil literal is very common. In those cases,
      // we can skip the proof obligations that ensure that at least one of the operands is comparable.
      val (pos, info, errT) = src.vprMeta
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
        res = vpr.EqCmp(vLhs, vRhs)(pos, info, errT)
      } yield  res
    case (lhs :: ctx.Interface(_), rhs :: ctx.Interface(_), src) =>
      val (pos, info, errT) = src.vprMeta
      val errorT = (x: Source.Verifier.Info, _: ErrorReason) =>
        ComparisonError(x).dueTo(ComparisonOnIncomparableInterfaces(x))
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
        cond = vpr.Or(
          isComparabeInterface(vLhs)(pos, info, errT)(ctx),
          isComparabeInterface(vRhs)(pos, info, errT)(ctx)
        )(pos, info, errT)
        res <- assert(cond, vpr.EqCmp(vLhs, vRhs)(pos, info, errT), errorT)(ctx)
      } yield  res
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * R[ dflt(interface{...}°) ] -> tuple2(null, nilT)
    * R[ nil: interface{...} ] -> tuple2(null, nilT)
    * R[ toInterface(e: interface{...}, _) ] -> [e]
    * R[ toInterface(e: T, _) ] -> tuple2([e], TYPE(T))
    * R[ e.(interface{...}) ] -> assert behavioralSubtype(TYPE_OF([e]), T); [e]
    * R[ e.(T) ] -> assert behavioralSubtype(TYPE_OF([e]), T); VALUE_OF([e], [T])
    * R[ typeOf(e) ] -> TYPE_OF([e])
    * Encoding of type expression is straightforward
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.expression(ctx)){
      case n@ (  (_: in.DfltVal) :: ctx.Interface(_) / Exclusive
               | (_: in.NilLit) :: ctx.Interface(_)  ) =>
        val (pos, info, errT) = n.vprMeta
        unit(utils.nilInterface()(pos, info, errT)(ctx)): CodeWriter[vpr.Exp]

      case in.ToInterface(exp :: ctx.Interface(_), _) =>
        goE(exp)

      case n@ in.ToInterface(exp, toType) =>
        val (pos, info, errT) = n.vprMeta
        val dependencyAnalysisEnhancedInfo = MakeInfoPair(info, DependencyAnalysisJoinNodeInfo(ImplementationProofSourceInfo(exp.typ, toType)))
        if (Comparability.comparable(exp.typ)(ctx.lookup).isDefined) {
          for {
            dynValue <- goE(exp)
            typ = types.typeToExpr(exp.typ)(pos, info, errT)(ctx)
          } yield boxInterface(dynValue, typ)(pos, dependencyAnalysisEnhancedInfo, errT)(ctx)
        } else {
          for {
            dynValue <- goE(exp)
          } yield toInterfaceFunc(Vector(dynValue), exp.typ)(pos, dependencyAnalysisEnhancedInfo, errT)(ctx)
        }

      case n@ in.IsBehaviouralSubtype(subtype, supertype) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vprSubtype <- goE(subtype)
          vprSupertype <- goE(supertype)
        } yield types.behavioralSubtype(vprSubtype, vprSupertype)(pos, info, errT)(ctx)

      case n@ in.TypeAssertion(exp :: ctx.Interface(itf), t) =>
        val (pos, info, errT) = n.vprMeta
        val errorT = (x: Source.Verifier.Info, _: ErrorReason) =>
          TypeAssertionError(x).dueTo(DynamicValueNotASubtypeReason(x))
        for {
          arg <- goE(exp)
          dynType = typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)
          staticType = types.typeToExpr(t)(pos, info, errT)(ctx)
          cond  = types.behavioralSubtype(dynType, staticType)(pos, info, errT)(ctx)
          res = t match {
            case ctx.Interface(_) => arg
            case _ => valueOf(arg, t)(pos, info, errT)(ctx)
          }
          resWithCheck <- assert(cond, res, errorT)(ctx)
        } yield resWithCheck

      case n@ in.TypeOf(exp :: ctx.Interface(itf)) =>
        val (pos, info, errT) = n.vprMeta
        for {
          arg <- goE(exp)
        } yield typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)

      case n@ in.IsComparableInterface(exp) =>
        val (pos, info, errT) = n.vprMeta
        for {
          arg <- goE(exp)
        } yield isComparabeInterface(arg)(pos, info, errT)(ctx)

      case n@ in.IsComparableType(exp) =>
        val (pos, info, errT) = n.vprMeta
        for {
          arg <- goE(exp)
        } yield types.isComparableType(arg)(pos, info, errT)(ctx)

      case n: in.TypeExpr =>
        for { es <- sequence(TypeHead.children(n) map goE) } yield withSrc(types.typeApp(TypeHead.typeHead(n), es), n, ctx)
    }
  }

  /**
    * Encodes assertions.
    *
    * Constraints:
    * - in.Access with in.PredicateAccess has to encode to vpr.PredicateAccessPredicate.
    *
    *
    */
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.assertion(ctx)) {
      case n@ in.Access(in.Accessible.Predicate(in.MPredicateAccess(recv, p, args)), perm) if hasFamily(p)(ctx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          instance <- mpredicateInstance(recv, p, args)(n)(ctx)
          perm <- goE(perm)
        } yield vpr.PredicateAccessPredicate(instance, Some(perm))(pos, info, errT): vpr.Exp

      case n@ in.Access(in.Accessible.Predicate(in.FPredicateAccess(p, args)), perm) if hasFamily(p)(ctx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          instance <- fpredicateInstance(p, args)(n)(ctx)
          perm <- goE(perm)
        } yield vpr.PredicateAccessPredicate(instance, Some(perm))(pos, info, errT): vpr.Exp
    }
  }



  /**
    * Encodes whether a value is comparable or not.
    *
    * isComp[ e: interface{...} ] -> isComparableInterface(e)
    */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: ctx.Interface(_) =>
      super.isComparable(ctx)(exp).map{ _ =>
        val (pos, info, errT) = exp.vprMeta
        for {
          vExp <- ctx.expression(exp)
        } yield isComparabeInterface(vExp)(pos, info ,errT)(ctx): vpr.Exp
      }
  }

  /** returns dynamic type of an interface expression. */
  private def typeOfWithSubtypeFact(arg: vpr.Exp, itfT: in.InterfaceT)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    if (itfT.isEmpty) {
      typeOf(arg)(pos, info, errT)(ctx)
    } else {
      vpr.FuncApp(func = typeOfWithSubtypeFactFunc(itfT)(ctx), Seq(arg))(pos, info, errT)
    }
  }

  /** returns dynamic type of an interface expression. */
  private def typeOf(arg: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    interfaces.dynTypeOf(arg)(pos, info, errT)(ctx)
  }

  /** Returns dynamic value of an interface expression as a type 'typ'. */
  private def valueOf(arg: vpr.Exp, typ: in.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    val polyVal = interfaces.polyValOf(arg)(pos, info, errT)(ctx)
    poly.unbox(polyVal, typ)(pos, info, errT)(ctx)
  }

  private def boxInterface(value: vpr.Exp, typ: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    val polyVar = poly.box(value)(pos, info, errT)(ctx)
    interfaces.create(polyVar, typ)(pos, info ,errT)(ctx)
  }


  /** Function returning whether a type is comparable. */
  private def isComparabeInterface(arg: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.DomainFuncApp = {
    val func = genComparableInterfaceFunc(ctx)
    vpr.DomainFuncApp(func = func, Seq(arg), Map.empty)(pos, info, errT)
  }

  /** Generates:
    * ComparableInterfaceDomain {
    *
    *   function comparableInterface(i: [interface{}]): Bool
    *
    *   axiom {
    *     forall i: [interface{}] :: { comparableInterface(i) }
    *       comparableType([typeOf(i)]) ==> comparableInterface(i)
    *   }
    * }
    */
  private def genComparableInterfaceFunc(ctx: Context): vpr.DomainFunc = {
    generatedComparableFunc.getOrElse{
      val domainName = "ComparableInterfaceDomain"

      val vItfT = vprInterfaceType(ctx)
      val iDecl = vpr.LocalVarDecl("i", vItfT)(); val i = iDecl.localVar

      val func = vpr.DomainFunc(
        name = "comparableInterface", formalArgs = Seq(iDecl), typ = vpr.Bool
      )(domainName = domainName)
      val funcApp = vpr.DomainFuncApp(func = func, Seq(i), Map.empty)()

      val axiom = vpr.AnonymousDomainAxiom(
        vpr.Forall(
          variables = Seq(iDecl),
          triggers = Seq(vpr.Trigger(Seq(funcApp))()),
          exp = vpr.Implies(
            types.isComparableType(typeOf(i)()(ctx))()(ctx),
            funcApp
          )()
        )()
      )(domainName = domainName)

      val domain = vpr.Domain(
        name = domainName,
        functions = Seq(func),
        axioms = Seq(axiom),
      )()

      genMembers ::= domain
      generatedComparableFunc = Some(func)
      func
    }
  }
  private var generatedComparableFunc: Option[vpr.DomainFunc] = None


  /**
    * Encodes statements.
    * This includes make-statements.
    *
    * The default implements:
    * [v: *T = new(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    *
    * [x, ok = e.(T = interface{...})] ->
    *   assert behavioralSubtype(TYPE_OF([e]), [T])
    *   ok = true
    *   x = [e]
    *
    * [x, ok = e.(T)] ->
    *   ok = TYPE_OF([e]) == [T]
    *   if (ok) { x = valueOf([e], [T]) }
    *   else { x = [ dflt(T) ] }
    *
    *
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)){
      case n@ in.SafeTypeAssertion(resTarget, successTarget, expr :: ctx.Interface(itf), typ@ ctx.Interface(_)) =>
        val (pos, info, errT) = n.vprMeta
        val errorT = (x: Source.Verifier.Info, _: ErrorReason) =>
          TypeAssertionError(x).dueTo(SafeTypeAssertionsToInterfaceNotSucceedingReason(x))
        seqn(
          for {
            arg <- ctx.expression(expr)
            dynType = typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)
            staticType = types.typeToExpr(typ)(pos, info, errT)(ctx)
            _ <- assert(types.behavioralSubtype(dynType, staticType)(pos, info, errT)(ctx), errorT)
            vResTarget = ctx.variable(resTarget).localVar
            vSuccessTarget = ctx.variable(successTarget).localVar
          } yield vpr.Seqn(Seq(
            vpr.LocalVarAssign(vResTarget, arg)(pos, info, errT),
            vpr.LocalVarAssign(vSuccessTarget, vpr.TrueLit()(pos, info, errT))(pos, info, errT)
          ), Seq.empty)(pos, info, errT)
        )

      case n@ in.SafeTypeAssertion(resTarget, successTarget, expr :: ctx.Interface(itf), typ) =>
        val (pos, info, errT) = n.vprMeta
        types.genPreciseEqualityAxioms(typ)(ctx)
        seqn(
          for {
            arg <- ctx.expression(expr)
            dynType = typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)
            staticType = types.typeToExpr(typ)(pos, info, errT)(ctx)
            vResTarget = ctx.variable(resTarget).localVar
            vSuccessTarget = ctx.variable(successTarget).localVar
            _ <- bind(vSuccessTarget, vpr.EqCmp(dynType, staticType)(pos, info, errT))
            vDflt <- ctx.expression(in.DfltVal(resTarget.typ)(n.info))
            res = vpr.If(
              vSuccessTarget,
              vpr.Seqn(Seq(vpr.LocalVarAssign(vResTarget, valueOf(arg, typ)(pos, info, errT)(ctx))(pos, info, errT)), Seq.empty)(pos, info, errT),
              vpr.Seqn(Seq(vpr.LocalVarAssign(vResTarget, vDflt)(pos, info, errT)), Seq.empty)(pos, info, errT)
            )(pos, info, errT)
          } yield res
        ): Writer[vpr.Stmt]
    }
  }

  /**
    * Generates:
    * function toInterface(x: [T°]): [interface{}]
    *   ensures result == tuple2(x, TYPE(T))
    *   ensures isComp[x: T°] ==> comparableInterface(result)
    *
    */
  private val toInterfaceFunc: FunctionGenerator[in.Type] = new FunctionGenerator[in.Type] {
    override def genFunction(t: in.Type)(ctx: Context): vpr.Function = {
      val x = in.LocalVar("x", t.withAddressability(Addressability.Exclusive))(Source.Parser.Internal)
      val vX = ctx.variable(x)

      val isComp = ctx.isComparable(x).fold(vpr.BoolLit(_)(), pure(_)(ctx).res)
      val emptyItfT = vprInterfaceType(ctx)
      val vRes = vpr.Result(emptyItfT)()

      vpr.Function(
        name = Names.toInterfaceFunc,
        formalArgs = Seq(vX),
        typ = emptyItfT,
        pres = Seq.empty,
        posts = Seq(
          vpr.EqCmp(vRes, boxInterface(vX.localVar, types.typeToExpr(t)()(ctx))()(ctx))(),
          vpr.Implies(isComp, isComparabeInterface(vRes)()(ctx))()
        ),
        body = None
      )()
    }
  }

  /**
    * Generates:
    *
    * function typeOfFunc_I(itf: [itf{}]): Type
    *   ensures result == typeOf(itf)
    *   ensures behaviouralSubtype(result, [I])
    *   decreases
    */
  private def typeOfWithSubtypeFactFunc(itfT: in.InterfaceT)(ctx: Context): vpr.Function = {
    typeOfWithSubtypeFactFuncMap.getOrElse(itfT.name, {
      val interfaceT = vprInterfaceType(ctx)
      val resT = types.typ()(ctx)
      val formal = vpr.LocalVarDecl("itf", interfaceT)()

      val resFunc = vpr.Function(
        name = s"${Names.typeOfFunc}_${itfT.name}",
        formalArgs = Seq(formal),
        typ = resT,
        pres = Seq(termination.DecreasesWildcard(None)()),
        posts = Seq(
          vpr.EqCmp(vpr.Result(resT)(), typeOf(formal.localVar)()(ctx))(),
          types.behavioralSubtype(vpr.Result(resT)(), types.typeToExpr(itfT)()(ctx))()(ctx)
        ),
        body = None
      )()

      typeOfWithSubtypeFactFuncMap += (itfT.name -> resFunc)
      resFunc
    })
  }
  private var typeOfWithSubtypeFactFuncMap: Map[String, vpr.Function] = Map.empty


  private def mpredicate(p: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {
    val id = familyID(p.name)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    ml.unit(genPredicate(id)(ctx))
  }

  private def fpredicate(p: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = {
    val id = familyID(p.name)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    ml.unit(genPredicate(id)(ctx))
  }

  /**
    * Generates:
    *
    * predicate I_P(itf, args) {
    *     typeOf(itf) == T ? BODY[T_P( valueOf(itf, T), args)] :
    *     ...
    *     I_P_unknown(itf, args)
    * }
    *
    * predicate I_P_unknown(itf, args)
    */
  private def genPredicate(id: Int)(ctx: Context): vpr.Predicate = {
    genPredicateMap.getOrElse(id, {
      val res: vpr.Predicate = {
        val family = predicateFamily(id)(ctx)

        val recvDecl = vpr.LocalVarDecl("i", vprInterfaceType(ctx))()
        val recv = recvDecl.localVar

        val (sigName, inArgTypes) = predicateFamilySignature(id)(ctx)
        val argTypes = inArgTypes.map(ctx.typ)
        val argDecls = argTypes.zipWithIndex map { case (t, idx) => vpr.LocalVarDecl(s"x$idx", t)() }
        val args = argDecls.map(_.localVar)

        def clause(p: in.PredicateProxy): Option[(in.Type, vpr.Exp, vpr.Exp)] = {
          val (typ, vPred) = p match {
            case p: in.MPredicateProxy =>
              val symb = ctx.lookup(p)
              (symb.receiver.typ, ctx.defaultEncoding.mpredicate(symb)(ctx).res)

            case p: in.FPredicateProxy =>
              val symb = ctx.lookup(p)
              (symb.args.head.typ, ctx.defaultEncoding.fpredicate(symb)(ctx).res)
          }

          vPred.body.map{ _ =>
            // generate precise equality axioms to prove inequality
            types.genPreciseEqualityAxioms(typ)(ctx)
            // typeOf(i) == T
            val lhs = vpr.EqCmp(typeOf(recv)()(ctx), types.typeToExpr(typ)()(ctx))()
            // Body[p(valueOf(i): [T], args)]
            val fullArgs = valueOf(recv, typ)()(ctx) +: args
            val rhs = vpr.utility.Expressions.instantiateVariables(vPred.body.get, vPred.formalArgs, fullArgs, Set.empty)
            (typ, lhs, vpr.utility.Simplifier.simplify(rhs))
          }
        }

        val name = genPredicateName(id)
        val defaultName = s"${name}_unknown"

        val defaultPredicate = vpr.Predicate(name = defaultName, formalArgs = recvDecl +: argDecls, body = None)()
        genPredicates ::= defaultPredicate
        val default = {
          vpr.PredicateAccessPredicate(
            vpr.PredicateAccess(recv +: args, defaultPredicate.name)(),
            Some(vpr.FullPerm()())
          )()

        }

        val clauses = family.toVector.flatMap(clause)
        val clauseTypes = clauses.map(_._1)

        if (clauseTypes.size != clauses.size) {
          // detecting this error in the type checking phase is challenging.
          throw new Violation.UglyErrorMessage(DiamondError(
            s"Detected an inheritance diamond for predicate $sigName. " +
              s"\nThat means that there exists a subtype S and three interfaces A, B, and C, together with " +
              s"\nthe implementation proofs (S implements A), (S implements B), (A implements C), and (B implements C)," +
              s"\nwhere the predicate $sigName is aliased with different predicates in the proofs (S implements A) and (S implements B)."
          ))
        }


        val res = vpr.Predicate(
          name = name,
          formalArgs = recvDecl +: argDecls,
          body = Some(
            clauses.foldRight(default: vpr.Exp){
              case ((_, l, r), res) => vpr.CondExp(l, r, res)()
            }
          )
        )()
        genPredicates ::= res
        res
      }

      genPredicateMap += (id -> res)
      res
    })
  }
  private def genPredicateName(id: Int): String = s"${Names.dynamicPredicate}_$id"
  private var genPredicateMap: Map[Int, vpr.Predicate] = Map.empty
  private var genPredicates: List[vpr.Predicate] = List.empty


  def hasFamily(p: in.PredicateProxy)(ctx: Context): Boolean = familyID(p)(ctx).isDefined
  private def familyID(p: in.PredicateProxy)(ctx: Context): Option[Int] = predicateFamilyTuple(ctx)._1.get(p)
  private def predicateFamily(id: Int)(ctx: Context): SortedSet[in.PredicateProxy] = predicateFamilyTuple(ctx)._2.getOrElse(id, SortedSet.empty)
  private def predicateFamilySignature(id: Int)(ctx: Context): (String, Vector[in.Type]) = predicateFamilyTuple(ctx)._3(id)
  private def predicateFamilyTuple(ctx: Context): (Map[in.PredicateProxy, Int], Map[Int, SortedSet[in.PredicateProxy]], Map[Int, (String, Vector[in.Type])]) = {
    predicateFamilyTupleRes.getOrElse{
      implicit val tuple3Ordering: Ordering[(in.MPredicateProxy, in.InterfaceT, SortedSet[in.Type])] = Ordering.by(_._1)

      val itfNodes = for {
        (itf, impls) <- ctx.table.getImplementations.toSet
        itfProxy <- ctx.table.lookupMembers(itf).collect{ case m: in.MPredicateProxy => m }
      } yield (itfProxy, itf, impls)

      val edges = for {
        (itfProxy, itf, impls) <- itfNodes
        impl <- impls
        implProxy = ctx.table.lookupImplementationPredicate(impl, itf, itfProxy.name)
          .getOrElse(Violation.violation(s"Did not find predicate ${itfProxy.name} for type $impl."))
      } yield (itfProxy, implProxy)

      val nodes = itfNodes.map(_._1) ++ edges.map(_._2)
      val graphEdges = edges.flatMap{ case (l,r) => Vector((l,r),(r,l)) }

      val (nodesId, families) = Algorithms.connected(nodes, graphEdges)

      val sigs = itfNodes.map{
        case (itfProxy, _, _) => nodesId(itfProxy) -> (itfProxy.name, ctx.lookup(itfProxy).args.map(_.typ))
      }.toMap

      val sortedFamilies = families.map { case (key, proxies) => key -> SortedSet(proxies.toSeq: _*) }
      predicateFamilyTupleRes = Some((nodesId, sortedFamilies, sigs))
      (nodesId, sortedFamilies, sigs)
    }
  }
  private var predicateFamilyTupleRes: Option[(Map[in.PredicateProxy, Int], Map[Int, SortedSet[in.PredicateProxy]], Map[Int, (String, Vector[in.Type])])] = None


  private def mpredicateInstance(recv: in.Expr, proxy: in.MPredicateProxy, args: Vector[in.Expr])(src: in.Node)(ctx: Context): CodeWriter[vpr.PredicateAccess] = {
    val (pos, info, errT) = src.vprMeta
    val id = familyID(proxy)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    for {
      dynValue <- goE(recv)
      vRecv = if (!ctx.lookup(proxy).receiver.typ.isInstanceOf[in.InterfaceT]) {
        val typ = types.typeToExpr(recv.typ)(pos, info, errT)(ctx)
        boxInterface(dynValue, typ)(pos, info, errT)(ctx)
      } else dynValue
      vArgs <- sequence(args map goE)
    } yield vpr.PredicateAccess(vRecv +: vArgs, predicateName = genPredicateName(id))(pos, info, errT)
  }

  private def fpredicateInstance(proxy: in.FPredicateProxy, args: Vector[in.Expr])(src: in.Node)(ctx: Context): CodeWriter[vpr.PredicateAccess] = {
    val (pos, info, errT) = src.vprMeta
    val id = familyID(proxy)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    for {
      dynValue <- goE(args.head)
      vRecv = if (!ctx.lookup(proxy).args.head.typ.isInstanceOf[in.InterfaceT]) {
        val typ = types.typeToExpr(args.head.typ)(pos, info, errT)(ctx)
        boxInterface(dynValue, typ)(pos, info, errT)(ctx)
      } else dynValue
      vArgs <- sequence(args.tail map goE)
    } yield vpr.PredicateAccess(vRecv +: vArgs, predicateName = genPredicateName(id))(pos, info, errT)
  }

  /**
    * Returns:
    *
    * function I_F(itf: I, args)
    *   requires [itf != nil: interface{...}] && [PRE]
    *   //> foreach T that has a proof to implement I:
    *     ensures  typeOf(itf) == T ==> result == proof_T_I_F(valueOf(itf, T), args)
    *   ensures  [POST]
    */
  private def function(p: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = {
    Violation.violation(p.results.size == 1, s"expected a single result, but got ${p.results}")

    val (pos, info: Source.Verifier.Info, errT) = p.vprMeta

    val pProxy = Names.InterfaceMethod.origin(p.name)

    val itfT = p.receiver.typ.asInstanceOf[in.InterfaceT]
    val impls = ctx.table.lookupNonInterfaceImplementations(itfT).toVector
    val cases = impls.flatMap(impl => ctx.table.lookup(impl, pProxy.name).map(impl -> _))

    val recvDecl = vpr.LocalVarDecl(Names.implicitThis, vprInterfaceType(ctx))(pos, info, errT)
    val recv = recvDecl.localVar

    val argDecls = p.args map ctx.variable
    val args = argDecls.map(_.localVar)

    val resultType = ctx.variable(p.results.head).typ
    val result = vpr.Result(resultType)(pos, info, errT)

    def clause(impl: in.Type, proxy: in.MemberProxy): vpr.Exp = {
      // typeOf(i) == T
      val lhs = vpr.EqCmp(typeOf(recv)(pos, info, errT)(ctx), types.typeToExpr(impl)(pos, info, errT)(ctx))(pos, info, errT)
      // proof_T_I_F(valueOf(itf, T), args)
      val fullArgs = valueOf(recv, impl)(pos, info, errT)(ctx) +: args
      val call = vpr.FuncApp(funcname = proofName(impl, proxy, pProxy), fullArgs)(pos, info, typ = resultType, errT)
      // typeOf(i) == T ==> result == proof_T_I_F(valueOf(itf, T), args)
      vpr.Implies(lhs, vpr.EqCmp(result, call)(pos, info, errT))(pos, info, errT)
    }

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == p.results.head.id => vpr.Result(resultType)() }
    }

    for {
      vPres <- ml.sequence(p.pres map ctx.precondition)
      measures <- ml.sequence(p.terminationMeasures.map(e => ml.pure(ctx.assertion(e))(ctx)))
      posts <- ml.sequence(p.posts.map(ctx.postcondition(_).map(fixResultvar(_))))
      body  <- ml.option(p.body.map(p => ml.pure(ctx.expression(p))(ctx)))
      func = vpr.Function(
        name = p.name.uniqueName,
        formalArgs = recvDecl +: argDecls,
        typ = resultType,
        pres = utils.receiverNotNil(recv)(pos, info, errT)(ctx) +: (vPres ++ measures),
        posts = (cases map { case (impl, implProxy) => clause(impl, implProxy) }) ++ posts,
        body = body
      )(pos, info, errT)
    } yield func
  }

  /**
    * function proof_T_I_F(x: T, args)
    *   requires PRE where PRE = [I_F.PRE][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    *   ensures  POST where POST = [I_F.POST][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    * {
    *   [body]
    * }
    */
  private def functionProof(p: in.PureMethodSubtypeProof)(ctx: Context): MemberWriter[vpr.Function] = {
    Violation.violation(p.results.size == 1, s"expected a single result, but got ${p.results}")

    val itfSymb = ctx.lookup(p.superProxy).asInstanceOf[in.PureMethod]
    val vItfFun = ctx.defaultEncoding.pureMethod(itfSymb)(ctx).res

    val body = p.body.getOrElse(in.PureMethodCall(p.receiver, p.subProxy, p.args, p.results.head.typ, false)(p.info))

    val pureMethodDummy = ctx.defaultEncoding.pureMethod(in.PureMethod(
      receiver = p.receiver,
      name = in.MethodProxy(p.superProxy.name, proofName(p.receiver.typ, p.subProxy, p.superProxy))(p.info),
      args = p.args,
      results = p.results,
      pres = Vector.empty,
      posts = Vector.empty,
      terminationMeasures = Vector.empty,
      backendAnnotations = Vector.empty,
      body = Some(body),
      isOpaque = false
    )(p.info))(ctx)

    val pres = vItfFun.pres.map { pre =>
      instantiateInterfaceSpecForProof(pre, vItfFun.formalArgs.toVector, p.receiver, p.args, p.superT)(p)(ctx)
    }

    val posts = vItfFun.posts.map { post =>
      instantiateInterfaceSpecForProof(post, vItfFun.formalArgs.toVector, p.receiver, p.args, p.superT)(p)(ctx)
    }

    val (pos, info, errT) = p.vprMeta
    val depAnInfo = DependencyAnalysisJoinNodeInfo(ImplementationProofSourceInfo(p.receiver.typ, p.superT))

    pureMethodDummy.map(res => res.copy(pres = pres, posts = posts)(pos, MakeInfoPair(info, depAnInfo), errT))
  }

  /**
    * method proof_T_I_M(x: T, args) returns (results)
    *   requires PRE where PRE = [I_M.PRE][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    *   ensures  POST where POST = [I_M.POST][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    * {
    *   [body]
    * }
    */
  private def methodProof(p: in.MethodSubtypeProof)(ctx: Context): MemberWriter[vpr.Method] = {

    val itfSymb = ctx.lookup(p.superProxy).asInstanceOf[in.Method]
    val vItfMeth = ctx.defaultEncoding.method(itfSymb)(ctx).res

    val body = p.body match {
      case Some(b) => b.toMethodBody
      case _ =>
        val targets = p.results.map(out => in.LocalVar(out.id, out.typ)(out.info))
        val call = in.MethodCall(targets, p.receiver, p.subProxy, p.args)(p.info)
        in.Block(Vector.empty, Vector(call))(p.info).toMethodBody
    }

    val methodDummy = ctx.defaultEncoding.method(in.Method(
      receiver = p.receiver,
      name = in.MethodProxy(p.superProxy.name, proofName(p.receiver.typ, p.subProxy, p.superProxy))(p.info),
      args = p.args,
      results = p.results,
      pres = Vector.empty,
      posts = Vector.empty,
      terminationMeasures = Vector.empty,
      backendAnnotations = Vector.empty,
      body = Some(body)
    )(p.info))(ctx)

    val pres = vItfMeth.pres.map { exp =>
      val variablesOfExp = vItfMeth.formalArgs.toVector ++ vItfMeth.formalReturns.toVector
      val parameters = p.args ++ p.results
      instantiateInterfaceSpecForProof(exp, variablesOfExp, p.receiver, parameters, p.superT)(p)(ctx)
    }
    val posts = vItfMeth.posts.map { exp =>
      val variablesOfExp = vItfMeth.formalArgs.toVector ++ vItfMeth.formalReturns.toVector
      val parameters = p.args ++ p.results
      instantiateInterfaceSpecForProof(exp, variablesOfExp, p.receiver, parameters, p.superT)(p)(ctx)
    }

    val (pos, info, errT) = p.vprMeta
    val depAnInfo = DependencyAnalysisJoinNodeInfo(ImplementationProofSourceInfo(p.receiver.typ, p.superT))

    methodDummy.map(res => res.copy(pres = pres, posts = posts)(pos, MakeInfoPair(depAnInfo, info), errT))
  }


  /**
    * Instantiates a condition of a spec from an interface I for an implementation T.
    * 1) The this reference (of type interface) is replaced with a this of type T that is put into an interface
    * 2) Calls to pure methods of the interface are replaced with calls to the proof that T implements the pure method.
    *
    * returns exp[ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    * */
  private def instantiateInterfaceSpecForProof(
                         exp: vpr.Exp,
                         variablesOfExpression: Vector[vpr.LocalVarDecl], /** The first variable must be the receiver */
                         recv: in.Parameter.In,
                         otherParameters: Vector[in.Parameter],
                         itfT: in.InterfaceT
                       )(src: in.Node)(ctx: Context): vpr.Exp = {
    val impl = recv.typ
    val vRecvDecls = ctx.variable(recv)
    val vRecv = vRecvDecls.localVar

    val vArgDecls = otherParameters map ctx.variable
    val vArgs = vArgDecls map (_.localVar)


    val itfFuncs = ctx.table.lookupMembers(itfT).collect{ case x: in.MethodProxy if ctx.lookup(x).isInstanceOf[in.PureMethod] => x }
    val matchingFuncs = itfFuncs.map(f => (f, ctx.table.lookup(impl, f.name).get))
    val nameMap = matchingFuncs.map{ case (itfProxy, implProxy) => (itfProxy.uniqueName, proofName(recv.typ, implProxy, itfProxy)) }.toMap

    val newRecv = boxInterface(vRecv, types.typeToExpr(impl)()(ctx))()(ctx)
    val changedFormals = vpr.utility.Expressions.instantiateVariables(exp, variablesOfExpression, newRecv +: vArgs, Set.empty)
    val changedFuncs = changedFormals.transform{
      // equality check is fine since it was substituted with exactly `newRecv` beforehand.
      case call: vpr.FuncApp if nameMap.isDefinedAt(call.funcname) && call.args.head == newRecv =>
        val recv = vRecv
        call.copy(funcname = nameMap(call.funcname), args = recv +: call.args.tail)(call.pos, call.info, call.typ, call.errT)
    }


    val (pos, info, errT) = src.vprMeta
    changedFuncs.withMeta(pos, info, errT)
  }

  private def proofName(subType: in.Type, subProxy: in.MemberProxy, supperProxy: in.MemberProxy): String =
    s"${Names.serializeType(subType)}_${subProxy.uniqueName}_${supperProxy.uniqueName}_proof"


}
