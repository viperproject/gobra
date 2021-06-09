// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.theory.{Comparability, TypeHead}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{ComparisonError, ComparisonOnIncomparableInterfaces, DiamondError, DynamicValueNotASubtypeReason, SafeTypeAssertionsToInterfaceNotSucceedingReason, Source, TypeAssertionError}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.{Algorithms, Violation}
import viper.silver.verifier.ErrorReason
import viper.silver.{ast => vpr}

class InterfaceEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.MemberWriter
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}
  import viper.gobra.translator.util.TypePatterns._

  private val interfaces: InterfaceComponent = new InterfaceComponent {
    def typ(polyType: vpr.Type, dynTypeType: vpr.Type)(ctx: Context): vpr.Type = ctx.tuple.typ(Vector(polyType, dynTypeType))
    def create(polyVal: vpr.Exp, dynType: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = ctx.tuple.create(Vector(polyVal, dynType))(pos, info, errT)
    def dynTypeOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = ctx.tuple.get(itf, 1, 2)(pos, info, errT)
    def polyValOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = ctx.tuple.get(itf, 0, 2)(pos, info, errT)
  }
  private val types: TypeComponent = new TypeComponentImpl
  private val poly: PolymorphValueComponent = {
    val handle = new PolymorphValueInterfaceHandle {
      def typ(polyType: vpr.Type)(ctx: Context): vpr.Type = interfaces.typ(polyType, types.typ()(ctx))(ctx)
      def create(polyVal: vpr.Exp, dynType: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = interfaces.create(polyVal, dynType)(pos, info, errT)(ctx)
      def dynTypeOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = interfaces.dynTypeOf(itf)(pos, info, errT)(ctx)
      def polyValOf(itf: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = interfaces.polyValOf(itf)(pos, info, errT)(ctx)
      def typeToExpr(typ: in.Type)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = types.typeToExpr(typ)(pos, info, errT)(ctx)
    }
    new PolymorphValueComponentImpl(handle)
  }


  private var genMembers: List[vpr.Member] = List.empty

  override def finalize(col: Collector): Unit = {
    poly.finalize(col)
    types.finalize(col)
    toInterfaceFunc.finalize(col)
    genMembers foreach col.addMember
    typeOfWithSubtypeFactFuncMap.values foreach col.addMember
    genPredicates foreach col.addMember
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

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case p: in.MPredicate if hasFamily(p.name)(ctx) =>
      mpredicate(p)(ctx)

    case p: in.FPredicate if hasFamily(p.name)(ctx) =>
      fpredicate(p)(ctx)

    case p: in.PureMethod if p.receiver.typ.isInstanceOf[in.InterfaceT] =>
      function(p)(ctx)

    case p: in.Method if p.receiver.typ.isInstanceOf[in.InterfaceT] =>
      ctx.method.method(p)(ctx).map(Vector(_))

    case p: in.MethodSubtypeProof =>
      methodProof(p)(ctx)

    case p: in.PureMethodSubtypeProof =>
      functionProof(p)(ctx)
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
        vLhs <- ctx.expr.translate(lhs)(ctx)
        vRhs <- ctx.expr.translate(rhs)(ctx)
      } yield vpr.EqCmp(vLhs, vRhs)(pos, info, errT)

    case (itf :: ctx.Interface(_), oth :: ctx.NotInterface(), src) =>
      equal(ctx)(itf, in.ToInterface(oth, itf.typ)(src.info), src)

    case (oth :: ctx.NotInterface(), itf :: ctx.Interface(_), src) =>
      equal(ctx)(itf, in.ToInterface(oth, itf.typ)(src.info), src)
  }

  /** also checks that the compared interface is comparable. */
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = default(super.goEqual(ctx)) {
    case (lhs :: ctx.Interface(_), rhs :: ctx.Interface(_), src) =>
      val (pos, info, errT) = src.vprMeta
      val errorT = (x: Source.Verifier.Info, _: ErrorReason) =>
        ComparisonError(x).dueTo(ComparisonOnIncomparableInterfaces(x))
      for {
        vLhs <- ctx.expr.translate(lhs)(ctx)
        vRhs <- ctx.expr.translate(rhs)(ctx)
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
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){
      case n@ (  (_: in.DfltVal) :: ctx.Interface(_) / Exclusive
               | (_: in.NilLit) :: ctx.Interface(_)  ) =>
        val (pos, info, errT) = n.vprMeta
        val value = poly.box(vpr.NullLit()(pos, info, errT))(pos, info, errT)(ctx)
        val typ = types.typeApp(TypeHead.NilHD)(pos, info, errT)(ctx)
        unit(interfaces.create(value, typ)(pos, info, errT)(ctx)): CodeWriter[vpr.Exp]

      case in.ToInterface(exp :: ctx.Interface(_), _) =>
        goE(exp)

      case n@ in.ToInterface(exp, _) =>
        val (pos, info, errT) = n.vprMeta
        if (Comparability.comparable(exp.typ)(ctx.lookup).isDefined) {
          for {
            dynValue <- goE(exp)
            typ = types.typeToExpr(exp.typ)(pos, info, errT)(ctx)
          } yield boxInterface(dynValue, typ)(pos, info, errT)(ctx)
        } else {
          for {
            dynValue <- goE(exp)
          } yield toInterfaceFunc(Vector(dynValue), exp.typ)(pos, info, errT)(ctx)
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
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.assertion(ctx)) {
      case n@ in.Access(in.Accessible.Predicate(in.MPredicateAccess(recv, p, args)), perm) if hasFamily(p)(ctx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          instance <- mpredicateInstance(recv, p, args)(n)(ctx)
          perm <- goE(perm)
        } yield vpr.PredicateAccessPredicate(instance, perm)(pos, info, errT): vpr.Exp

      case n@ in.Access(in.Accessible.Predicate(in.FPredicateAccess(p, args)), perm) if hasFamily(p)(ctx) =>
        val (pos, info, errT) = n.vprMeta
        for {
          instance <- fpredicateInstance(p, args)(n)(ctx)
          perm <- goE(perm)
        } yield vpr.PredicateAccessPredicate(instance, perm)(pos, info, errT): vpr.Exp
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
          vExp <- ctx.expr.translate(exp)(ctx)
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
            arg <- ctx.expr.translate(expr)(ctx)
            dynType = typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)
            staticType = types.typeToExpr(typ)(pos, info, errT)(ctx)
            _ <- assert(types.behavioralSubtype(dynType, staticType)(pos, info, errT)(ctx), errorT)
            vResTarget = ctx.typeEncoding.variable(ctx)(resTarget).localVar
            vSuccessTarget = ctx.typeEncoding.variable(ctx)(successTarget).localVar
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
            arg <- ctx.expr.translate(expr)(ctx)
            dynType = typeOfWithSubtypeFact(arg, in.InterfaceT(itf, Addressability.Exclusive))(pos, info, errT)(ctx)
            staticType = types.typeToExpr(typ)(pos, info, errT)(ctx)
            vResTarget = ctx.typeEncoding.variable(ctx)(resTarget).localVar
            vSuccessTarget = ctx.typeEncoding.variable(ctx)(successTarget).localVar
            _ <- bind(vSuccessTarget, vpr.EqCmp(dynType, staticType)(pos, info, errT))
            vDflt <- ctx.expr.translate(in.DfltVal(resTarget.typ)(n.info))(ctx)
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
      val vX = ctx.typeEncoding.variable(ctx)(x)

      val isComp = ctx.typeEncoding.isComparable(ctx)(x).fold(vpr.BoolLit(_)(), pure(_)(ctx).res)
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
        pres = Seq.empty,
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


  private def mpredicate(p: in.MPredicate)(ctx: Context): MemberWriter[Vector[vpr.Predicate]] = {
    val id = familyID(p.name)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    genPredicate(id)(ctx)
    ml.unit(Vector.empty)
  }

  private def fpredicate(p: in.FPredicate)(ctx: Context): MemberWriter[Vector[vpr.Predicate]] = {
    val id = familyID(p.name)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    genPredicate(id)(ctx)
    ml.unit(Vector.empty)
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
        val argTypes = inArgTypes.map(ctx.typeEncoding.typ(ctx))
        val argDecls = argTypes.zipWithIndex map { case (t, idx) => vpr.LocalVarDecl(s"x$idx", t)() }
        val args = argDecls.map(_.localVar)

        def clause(p: in.PredicateProxy): Option[(in.Type, vpr.Exp, vpr.Exp)] = {
          val (typ, vPred) = p match {
            case p: in.MPredicateProxy =>
              val symb = ctx.lookup(p)
              (symb.receiver.typ, ctx.predicate.mpredicate(symb)(ctx).res)

            case p: in.FPredicateProxy =>
              val symb = ctx.lookup(p)
              (symb.args.head.typ, ctx.predicate.fpredicate(symb)(ctx).res)
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
            vpr.FullPerm()()
          )()

        }

        val clauses = family.flatMap(clause)
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


  private def hasFamily(p: in.PredicateProxy)(ctx: Context): Boolean = familyID(p)(ctx).isDefined
  private def familyID(p: in.PredicateProxy)(ctx: Context): Option[Int] = predicateFamilyTuple(ctx)._1.get(p)
  private def predicateFamily(id: Int)(ctx: Context): Set[in.PredicateProxy] = predicateFamilyTuple(ctx)._2.getOrElse(id, Set.empty)
  private def predicateFamilySignature(id: Int)(ctx: Context): (String, Vector[in.Type]) = predicateFamilyTuple(ctx)._3(id)
  private def predicateFamilyTuple(ctx: Context): (Map[in.PredicateProxy, Int], Map[Int, Set[in.PredicateProxy]], Map[Int, (String, Vector[in.Type])]) = {
    predicateFamilyTupleRes.getOrElse{
      val itfNodes = for {
        (itf, impls) <- ctx.table.interfaceImplementations.toSet
        itfProxy <- ctx.table.members(itf).collect{ case m: in.MPredicateProxy => m }
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

      predicateFamilyTupleRes = Some((nodesId, families, sigs))
      (nodesId, families, sigs)
    }
  }
  private var predicateFamilyTupleRes: Option[(Map[in.PredicateProxy, Int], Map[Int, Set[in.PredicateProxy]], Map[Int, (String, Vector[in.Type])])] = None


  private def mpredicateInstance(recv: in.Expr, proxy: in.MPredicateProxy, args: Vector[in.Expr])(src: in.Node)(ctx: Context): CodeWriter[vpr.PredicateAccess] = {
    val (pos, info, errT) = src.vprMeta
    val id = familyID(proxy)(ctx).getOrElse(Violation.violation("expected dynamic predicate"))
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

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
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

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
    * Generates:
    *
    * function I_F(itf: I, args)
    *   requires [PRE]
    *   ensures typeOf(itf) == T ==> result == proof_T_I_F(valueOf(itf, T), args)
    */
  private def function(p: in.PureMethod)(ctx: Context): MemberWriter[Vector[vpr.Function]] = {
    Violation.violation(p.results.size == 1, s"expected a single result, but got ${p.results}")
    Violation.violation(p.posts.isEmpty, s"expected no postcondition, but got ${p.posts}")
    Violation.violation(p.body.isEmpty, s"expected no body, but got ${p.body}")

    val itfT = p.receiver.typ.asInstanceOf[in.InterfaceT]
    val impls = ctx.table.implementations(itfT)
    val cases = impls.map(impl => (impl, ctx.table.lookup(impl, p.name.name).get))

    val recvDecl = vpr.LocalVarDecl(Names.implicitThis, vprInterfaceType(ctx))()
    val recv = recvDecl.localVar

    val argDecls = p.args map ctx.typeEncoding.variable(ctx)
    val args = argDecls.map(_.localVar)

    val resultType = ctx.typeEncoding.variable(ctx)(p.results.head).typ
    val result = vpr.Result(resultType)()

    def clause(impl: in.Type, proxy: in.MemberProxy): vpr.Exp = {
      // typeOf(i) == T
      val lhs = vpr.EqCmp(typeOf(recv)()(ctx), types.typeToExpr(impl)()(ctx))()
      // proof_T_I_F(valueOf(itf, T), args)
      val fullArgs = valueOf(recv, impl)()(ctx) +: args
      val call = vpr.FuncApp(funcname = proofName(proxy, p.name), fullArgs)(vpr.NoPosition, vpr.NoInfo, typ = resultType, vpr.NoTrafos)
      // typeOf(i) == T ==> result == proof_T_I_F(valueOf(itf, T), args)
      vpr.Implies(lhs, vpr.EqCmp(result, call)())()
    }

    for {
      vPres <- ml.sequence(p.pres map (ctx.ass.precondition(_)(ctx)))
      func = vpr.Function(
        name = p.name.uniqueName,
        formalArgs = recvDecl +: argDecls,
        typ = resultType,
        pres = vPres,
        posts = cases.toVector map { case (impl, implProxy) => clause(impl, implProxy) },
        body = None
      )()
    } yield Vector(func)
  }

  /**
    * function proof_T_I_F(x: T, args)
    *   requires PRE where PRE = [I_F.PRE][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    * {
    *   [body]
    * }
    */
  private def functionProof(p: in.PureMethodSubtypeProof)(ctx: Context): MemberWriter[Vector[vpr.Function]] = {
    Violation.violation(p.results.size == 1, s"expected a single result, but got ${p.results}")

    val itfSymb = ctx.lookup(p.superProxy).asInstanceOf[in.PureMethod]
    val vItfFun = ctx.pureMethod.pureMethod(itfSymb)(ctx).res

    val body = p.body.getOrElse(in.PureMethodCall(p.receiver, p.subProxy, p.args, p.results.head.typ)(p.info))

    val pureMethodDummy = ctx.pureMethod.pureMethod(in.PureMethod(
      receiver = p.receiver,
      name = in.MethodProxy(p.superProxy.name, proofName(p.subProxy, p.superProxy))(p.info),
      args = p.args,
      results = p.results,
      pres = Vector.empty,
      posts = Vector.empty,
      terminationMeasure=Option.empty,
      body = Some(body)
    )(p.info))(ctx)

    val pres = vItfFun.pres.map { pre =>
      instantiateInterfaceSpecForProof(pre, vItfFun.formalArgs.toVector, p.receiver, p.args, p.superT)(p)(ctx)
    }

    val (pos, info, errT) = p.vprMeta
    pureMethodDummy.map(res => Vector(res.copy(pres = pres)(pos, info, errT)))
  }

  /**
    * method proof_T_I_M(x: T, args) returns (results)
    *   requires PRE where PRE = [I_M.PRE][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    *   ensures  POST where POST = [I_M.POST][ this -> tuple2(this, Type(this)); tuple2(this, Type(this)).I_F -> this.proof_T_implements_I_F ]
    * {
    *   [body]
    * }
    */
  private def methodProof(p: in.MethodSubtypeProof)(ctx: Context): MemberWriter[Vector[vpr.Method]] = {

    val itfSymb = ctx.lookup(p.superProxy).asInstanceOf[in.Method]
    val vItfMeth = ctx.method.method(itfSymb)(ctx).res

    val body = p.body.getOrElse {
      val targets = p.results.map(out => in.LocalVar(out.id, out.typ)(out.info))
      val call = in.MethodCall(targets, p.receiver, p.subProxy, p.args)(p.info)
      in.Block(Vector.empty, Vector(call))(p.info)
    }

    val methodDummy = ctx.method.method(in.Method(
      receiver = p.receiver,
      name = in.MethodProxy(p.superProxy.name, proofName(p.subProxy, p.superProxy))(p.info),
      args = p.args,
      results = p.results,
      pres = Vector.empty,
      posts = Vector.empty,
      terminationMeasure=Option.empty,
      body = Some(body)
    )(p.info))(ctx)

    val pres = vItfMeth.pres.map { exp =>
      instantiateInterfaceSpecForProof(exp, vItfMeth.formalArgs.toVector, p.receiver, p.args, p.superT)(p)(ctx)
    }
    val posts = vItfMeth.posts.map { exp =>
      instantiateInterfaceSpecForProof(exp, vItfMeth.formalArgs.toVector, p.receiver, p.args, p.superT)(p)(ctx)
    }

    val (pos, info, errT) = p.vprMeta
    methodDummy.map(res => Vector(res.copy(pres = pres, posts = posts)(pos, info, errT)))
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
                         formalsOfExp: Vector[vpr.LocalVarDecl],
                         recv: in.Parameter.In,
                         ins: Vector[in.Parameter.In],
                         itfT: in.InterfaceT
                       )(src: in.Node)(ctx: Context): vpr.Exp = {
    val impl = recv.typ
    val vRecvDecls = ctx.typeEncoding.variable(ctx)(recv)
    val vRecv = vRecvDecls.localVar

    val vArgDecls = ins map ctx.typeEncoding.variable(ctx)
    val vArgs = vArgDecls map (_.localVar)


    val itfFuncs = ctx.table.members(itfT).collect{ case x: in.MethodProxy if ctx.lookup(x).isInstanceOf[in.PureMethod] => x }
    val matchingFuncs = itfFuncs.map(f => (f, ctx.table.lookup(impl, f.name).get))
    val nameMap = matchingFuncs.map{ case (itfProxy, implProxy) => (itfProxy.uniqueName, proofName(implProxy, itfProxy)) }.toMap

    val newRecv = boxInterface(vRecv, types.typeToExpr(impl)()(ctx))()(ctx)
    val changedFormals = vpr.utility.Expressions.instantiateVariables(exp, formalsOfExp, newRecv +: vArgs, Set.empty)
    val changedFuncs = changedFormals.transform{
      case call: vpr.FuncApp if nameMap.isDefinedAt(call.funcname) =>
        val recv = vRecv // maybe check that receiver is the same as newRecv
        call.copy(funcname = nameMap(call.funcname), args = recv +: call.args.tail)(call.pos, call.info, call.typ, call.errT)
    }


    val (pos, info, errT) = src.vprMeta
    changedFuncs.withMeta(pos, info, errT)
  }

  private def proofName(subProxy: in.MemberProxy, supperProxy: in.MemberProxy): String =
    s"${subProxy.uniqueName}_${supperProxy.uniqueName}_proof"


}
