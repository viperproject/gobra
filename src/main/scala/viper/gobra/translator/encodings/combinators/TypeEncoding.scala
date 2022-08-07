// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.ast.internal.theory.Comparability
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.{DefaultErrorBackTranslator, LoopInvariantNotWellFormedError, MethodContractNotWellFormedError, Source}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.library.Generator
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.verifier.{errors => vprerr}
import viper.silver.{ast => vpr}

import scala.annotation.unused

trait TypeEncoding extends Generator {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}

  /**
    * Translates a type into a Viper type.
    * Every Implementation should encode at least one type or be a subclass of [[Encoding]].
    */
  def typ(@unused ctx: Context): in.Type ==> vpr.Type

  /**
    * Translates variables that have the scope of a code body. Returns the encoded Viper variables.
    * As the default encoding, returns a local var with the argument name and the translated type.
    */
  def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = {
    case v if typ(ctx) isDefinedAt v.typ =>
      val (pos, info, errT) = v.vprMeta
      val t = typ(ctx)(v.typ)
      vpr.LocalVarDecl(v.id, t)(pos, info, errT)
  }

  /**
    * Translates variables that have a global scope. Returns the encoded Viper expression.
    * As the default encoding, returns a call to a function with the argument name and the translated type.
    */
  def globalVar(ctx: Context): in.Global ==> vpr.Exp = {
    case v: in.GlobalConst if typ(ctx) isDefinedAt v.typ => ctx.fixpoint.get(v)(ctx): vpr.Exp
    case v: in.GlobalVar =>
      val (pos, info, errT) = v.vprMeta
      val encodedVar = globalVarReference(ctx)(v)
      vpr.FieldAccess(encodedVar, ctx.field.field(v.typ.withAddressability(Exclusive))(ctx))(pos, info, errT)
  }

  def globalVarReference(ctx: Context): in.GlobalVar ==> vpr.Exp = { case v =>
    val (pos, info, errT) = v.vprMeta
    val typ = ctx.typ(v.typ)
    vpr.FuncApp(
      funcname = v.name.uniqueName,
      args = Seq.empty
    )(pos, info, typ, errT)
  }

  /**
    * Encodes members.
    *
    * This function is called once for every member of the input program.
    * All results are added to the Viper program.
    *
    * Viper members that are added through [[finalize]] must not be contained in the result.
    * Furthermore, Viper members that are the same for different internal members have to be handled by [[finalize]].
    *
    * The default returns the result of [[method]], [[function]], [[predicate]], [[globalVarDeclaration]]
    * */
  def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    val m = finalMethod(ctx); val f = finalFunction(ctx);
    val p = finalPredicate(ctx); val g = finalGlobalVarDeclatarion(ctx);
    {
      case m(r) => r.map(Vector(_))
      case f(r) => r.map(Vector(_))
      case p(r) => r.map(Vector(_))
      case g(r) => r
    }
  }

  /** Encodes members that are encoded to a single method. */
  def method(@unused ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    val biM = builtInMethod(ctx); { case biM(r) => ctx.method(r) }
  }

  /** Encodes members that are encoded to a single function. */
  def function(@unused ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    val biF = builtInFunction(ctx); { case biF(r) => ctx.function(r) }
  }

  /** Encodes members that are encoded to a single predicate. */
  def predicate(@unused ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = {
    val biFP = builtInFPredicate(ctx); val biMP = builtInMPredicate(ctx);
    {
      case biFP(r) => ctx.predicate(r)
      case biMP(r) => ctx.predicate(r)
    }
  }

  /**
    * Encodes global variable declarations.
    */
  def globalVarDeclaration(@unused ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Function]] = PartialFunction.empty

  /**
    * Returns extensions to the precondition for an in-parameter.
    */
  def varPrecondition(@unused ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns extensions to the postcondition for an out-parameter
    */
  def varPostcondition(@unused ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns initialization code for a declared location with the scope of a body.
    * The initialization code has to guarantee that:
    * (1) the declared variable has its default value afterwards
    * (2) all permissions for the declared variables are owned afterwards
    *
    * The default implements:
    * Initialize[loc: T°] -> assume [loc == dflt(T)]
    * Initialize[loc: T@] -> inhale Footprint[loc]; assume [loc == dflt(T°)] && [&loc != nil(*T)]
    */
  def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = {
    case loc :: t / Exclusive if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      for {
        eq <- ctx.equal(loc, in.DfltVal(t.withAddressability(Exclusive))(loc.info))(loc)
      } yield vpr.Inhale(eq)(pos, info, errT): vpr.Stmt

    case loc :: t / Shared if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      for {
        footprint <- addressFootprint(ctx)(loc, in.FullPerm(loc.info))
        eq1 <- ctx.equal(loc, in.DfltVal(t.withAddressability(Exclusive))(loc.info))(loc)
        eq2 <- ctx.equal(in.Ref(loc)(loc.info), in.NilLit(in.PointerT(t, Exclusive))(loc.info))(loc)
      } yield vpr.Inhale(vpr.And(footprint, vpr.And(eq1, vpr.Not(eq2)(pos, info, errT))(pos, info, errT))(pos, info, errT))(pos, info, errT)
  }

  /**
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * To avoid conflicts with other encodings, an encoding for type T
    * should be defined at the following left-hand sides:
    * (1) exclusive variables of type T
    * (2) exclusive operations on type T (e.g. a field access for structs)
    * (3) shared expressions of type T
    * In particular, being defined at shared operations on type T causes conflicts with (3)
    *
    * The default implements:
    * [v: T° = rhs] -> VAR[v] = [rhs]
    * [loc: T@ = rhs] -> exhale Footprint[loc]; inhale Footprint[loc] && [loc == rhs]
    */
  def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = {
    case (in.Assignee((v: in.BodyVar) :: t / Exclusive), rhs, src) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vRhs <- ctx.expression(rhs)
        vLhs = variable(ctx)(v).localVar
      } yield vpr.LocalVarAssign(vLhs, vRhs)(pos, info, errT)

    case (in.Assignee((loc: in.Location) :: t / Shared), rhs, src) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      seqn(
        for {
          footprint <- addressFootprint(ctx)(loc, in.FullPerm(loc.info))
          eq <- ctx.equal(loc, rhs)(src)
          _ <- write(vpr.Exhale(footprint)(pos, info, errT))
          inhale = vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT)
        } yield inhale
      )
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    * An encoding for type T should be defined at left-hand sides of type T and exclusive *T.
    * (Except the encoding of pointer types, which is not defined at exclusive *T to avoid a conflict).
    *
    * The default implements:
    * [lhs: T == rhs: T] -> [lhs] == [rhs]
    * [lhs: *T° == rhs: *T] -> [lhs] == [rhs]
    */
  def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: t, rhs :: s, src) if typ(ctx).isDefinedAt(t) && typ(ctx).isDefinedAt(s) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield vpr.EqCmp(vLhs, vRhs)(pos, info, errT): vpr.Exp

    case (lhs :: ctx.*(t) / Exclusive, rhs :: ctx.*(s), src) if typ(ctx).isDefinedAt(t) && typ(ctx).isDefinedAt(s) =>
      val (pos, info, errT) = src.vprMeta
      for {
        vLhs <- ctx.expression(lhs)
        vRhs <- ctx.expression(rhs)
      } yield vpr.EqCmp(vLhs, vRhs)(pos, info, errT)
  }

  /** Encodes equal operation with go semantics */
  def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = equal(ctx)

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * (2) shared expression of type T
    * The default implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    * Furthermore, the default implements [T(e: T)] -> [e]
    */
  def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: t / Exclusive if typ(ctx).isDefinedAt(t) => unit(variable(ctx)(v).localVar)
    case (v: in.Global) :: t / Exclusive if typ(ctx).isDefinedAt(t) => unit(globalVar(ctx)(v))
    case in.Conversion(t2, expr :: t) if typ(ctx).isDefinedAt(t) && typ(ctx).isDefinedAt(t2) => ctx.expression(expr)
  }

  /**
    * Encodes assertions.
    *
    * Constraints:
    * - in.Access with in.PredicateAccess has to encode to vpr.PredicateAccessPredicate.
    */
  def assertion(@unused ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  final def invariant(ctx: Context): in.Assertion ==> (CodeWriter[Unit], vpr.Exp) = {
    def invErr(inv: vpr.Exp): ErrorTransformer = {
      case e@ vprerr.ContractNotWellformed(Source(info), reason, _) if e causedBy inv =>
        LoopInvariantNotWellFormedError(info)
          .dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    }

    val ass = finalAssertion(ctx); {
      case ass(x) =>
        val invWithErrorT = for {
          inv <- x
          _ <- errorT(invErr(inv))
        } yield inv

        invWithErrorT.cut.swap
    }
  }

  final private def contract(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    def contractErr(inv: vpr.Exp): ErrorTransformer = {
      case e@ vprerr.ContractNotWellformed(Source(info), reason, _) if e causedBy inv =>
        MethodContractNotWellFormedError(info)
          .dueTo(DefaultErrorBackTranslator.defaultTranslate(reason))
    }

    val ass = finalAssertion(ctx); {
      case ass(x) =>
        for {
          contract <- x
          _ <- errorT(contractErr(contract))
        } yield contract
    }
  }

  final def precondition(ctx: Context): in.Assertion ==> MemberWriter[vpr.Exp] = contract(ctx) andThen(ml.pure(_)(ctx))

  final def postcondition(ctx: Context): in.Assertion ==> MemberWriter[vpr.Exp] = contract(ctx) andThen(ml.pure(_)(ctx))

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * The default implements shared variables with [[variable]].
    */
  def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = {
    case (v: in.BodyVar) :: t / Shared if typ(ctx).isDefinedAt(t) => unit(variable(ctx)(v).localVar: vpr.Exp)
    case (v: in.GlobalVar) :: t / Shared if typ(ctx).isDefinedAt(t) => unit(globalVarReference(ctx)(v))
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    */
  def addressFootprint(@unused ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes whether a value is comparable or not.
    * If comparability is unambiguously determined by the type of the value, then Left is returned.
    */
  def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case exp :: t if typ(ctx).isDefinedAt(t) =>
      Comparability.comparable(t)(ctx.lookup).toLeft(unit(withSrc(vpr.FalseLit(), exp): vpr.Exp))
  }

  /**
    * Encodes statements.
    * This includes new-statements.
    *
    * The default implements:
    * [v: *T = new(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    */
  def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case newStmt@in.New(target, expr) if typ(ctx).isDefinedAt(expr.typ) =>
      val (pos, info, errT) = newStmt.vprMeta
      val z = in.LocalVar(ctx.freshNames.next(), target.typ.withAddressability(Exclusive))(newStmt.info)
      val zDeref = in.Deref(z, underlyingType(z.typ)(ctx))(newStmt.info)
      seqn(
        for {
          _ <- local(ctx.variable(z))
          footprint <- addressFootprint(ctx)(zDeref, in.FullPerm(zDeref.info))
          eq <- ctx.equal(zDeref, expr)(newStmt)
          _ <- write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
          ass <- ctx.assignment(in.Assignee.Var(target), z)(newStmt)
        } yield ass
      ): CodeWriter[vpr.Stmt]
  }


  /** Returns declaration of built-in method. */
  def builtInMethod(@unused ctx: Context): in.BuiltInMethod ==> in.MethodMember = PartialFunction.empty

  /** Returns declaration of built-in function. */
  def builtInFunction(@unused ctx: Context): in.BuiltInFunction ==> in.FunctionMember = PartialFunction.empty

  /** Returns declaration of built-in fpredicate. */
  def builtInFPredicate(@unused ctx: Context): in.BuiltInFPredicate ==> in.FPredicate = PartialFunction.empty

  /** Returns declaration of built-in mpredicate. */
  def builtInMPredicate(@unused ctx: Context): in.BuiltInMPredicate ==> in.MPredicate = PartialFunction.empty


  /** Transforms the result of an encoding. */
  type Extension[X] = X => X

  /** Adds to the encoding of [[method]]. The extension is applied to the result of the final method encoding. */
  def extendMethod(@unused ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Method]] = PartialFunction.empty
  final def finalMethod(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    val f = method(ctx); { case n@f(v) => extendMethod(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[function]]. The extension is applied to the result of the final function encoding. */
  def extendFunction(@unused ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Function]] = PartialFunction.empty
  final def finalFunction(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    val f = function(ctx); { case n@f(v) => extendFunction(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[predicate]]. The extension is applied to the result of the final predicate encoding. */
  def extendPredicate(@unused ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Predicate]] = PartialFunction.empty
  final def finalPredicate(ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = {
    val f = predicate(ctx); { case n@f(v) => extendPredicate(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[globalVarDeclaration]]. The extension is applied to the result of the global variable
    * declaration encoding.
    */
  def extendGlobalVarDeclaration(@unused ctx: Context): in.Member ==> Extension[MemberWriter[Vector[vpr.Function]]] =
      PartialFunction.empty
  final def finalGlobalVarDeclatarion(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Function]] = {
    val f = globalVarDeclaration(ctx); { case n@f(v) => extendGlobalVarDeclaration(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[expression]]. The extension is applied to the result of the final expression encoding. */
  def extendExpression(@unused ctx: Context): in.Expr ==> Extension[CodeWriter[vpr.Exp]] = PartialFunction.empty
  final def finalExpression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    val f = expression(ctx); { case n@f(v) => extendExpression(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[assertion]]. The extension is applied to the result of the final assertion encoding. */
  def extendAssertion(@unused ctx: Context): in.Assertion ==> Extension[CodeWriter[vpr.Exp]] = PartialFunction.empty
  final def finalAssertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    val f = assertion(ctx); { case n@f(v) => extendAssertion(ctx).lift(n).fold(v)(_(v)) }
  }

  /** Adds to the encoding of [[statement]]. The extension is applied to the result of the final statement encoding. */
  def extendStatement(@unused ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = PartialFunction.empty
  final def finalStatement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    val f = statement(ctx); {
      case n@f(v) =>
        // makes sure that the statements aggregated by the writer are emitted at the proper position.
        val closedV = seqn(v)
        extendStatement(ctx).lift(n).fold(closedV)(_(closedV))
    }
  }


  /**
    * Alternative version of `orElse` to simplify delegations to super implementations.
    *
    * @param dflt default partial function, applied if 'f' is not defined at argument
    * @return
    */
  protected def default[X, Y](dflt: X ==> Y)(f: X ==> Y): X ==> Y = f orElse dflt

  /** Adds source information to a node without source information. */
  protected def withSrc[T](node: (vpr.Position, vpr.Info, vpr.ErrorTrafo) => T, src: in.Node): T = {
    val (pos, info, errT) = src.vprMeta
    node(pos, info, errT)
  }

  /** Adds source information to a node without source information. */
  protected def withSrc[T](node: (vpr.Position, vpr.Info, vpr.ErrorTrafo) => Context => T, src: in.Node, ctx: Context): T = {
    val (pos, info, errT) = src.vprMeta
    node(pos, info, errT)(ctx)
  }

  /** Adds simple (source) information to a node without source information. */
  protected def synthesized[T](node: (vpr.Position, vpr.Info, vpr.ErrorTrafo) => T)(comment: String): T =
    node(vpr.NoPosition, vpr.SimpleInfo(Seq(comment)), vpr.NoTrafos)
}
