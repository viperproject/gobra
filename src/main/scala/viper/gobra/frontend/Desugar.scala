package viper.gobra.frontend

import viper.gobra.ast.frontend._
import viper.gobra.ast.internal._
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type.{BooleanT, IntT, PointerT, Type, DeclaredT, VoidType}
import viper.gobra.frontend.info.base.{SymbolTable => st}

object Desugar {

  def desugar(program: PProgram, info: viper.gobra.frontend.info.TypeInfo): in.Program = {
    new Desugarer(program.positions, info).programD(program)
  }

  private class Desugarer(pom: PositionManager, info: viper.gobra.frontend.info.TypeInfo) {

    // TODO: clean initialization

    import viper.gobra.util.Violation._

    private val nm = new NameManager

    class FunctionContext(val ret: Vector[in.Expr] => Origin => in.Stmt) {

      type VarIdentity = in.Origin

      private def abstraction(id: PIdnNode): VarIdentity = {
        origin(info.regular(id).rep)
      }

      private var substitutions: Map[VarIdentity, Source => in.BodyVar] = Map.empty

      def apply(id: PIdnNode): Option[in.BodyVar] =
        substitutions.get(abstraction(id)).map(u => u(Source.Single(origin(id))))


      def addSubst(from: PIdnNode, to: Source => in.BodyVar): Unit =
        substitutions.updated(abstraction(from), to)
    }

    def programD(p: PProgram): in.Program = {
      val globalVarDecls = p.declarations.collect{ case x: PVarDecl => varDeclGD(x) }.flatten.toSet
      val globalConstDecls = p.declarations.collect{ case x: PConstDecl => constDeclD(x) }.flatten.toSet
      val methods = p.declarations.collect{ case x: PMethodDecl => methodD(x) }.toSet
      val functions = p.declarations collect { case x: PFunctionDecl => functionD(x) }
      p.declarations collect { case x: PTypeDef => typeDefD(x) }

      in.Program(
        types,
        globalVarDecls,
        globalConstDecls,
        methods,
        functions.toSet
      )(Source.Single(origin(p)))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConst] = ???

    def typeDefD(decl: PTypeDef): in.Type = typeD(DeclaredT(decl))

    def functionD(decl: PFunctionDecl): in.Function = {

      val name = idName(decl.id)

      val argLinks = decl.args map parameterD
      val arg = argLinks.unzip._1


      val returnLinks = decl.result match {
        case PVoidResult() => Vector.empty
        case PResultClause(outs) =>
          outs.map(parameterD).map{ case (p, r) => (in.LocalVar.Val(p.id,p.typ)(p.src), r) }
      }
      val (returnAssign, returnVars) = returnLinks.unzip


      def assignReturns(rets: Vector[in.Expr])(origin: Origin): in.Stmt = {
        if (rets.isEmpty) {
          in.Seq(
            returnLinks.map{
              case (_, None) => violation("found empty return and unnamed returns")
              case (p, Some(v)) => in.SingleAss(in.Assignee.Var(p), v(Source.Multi(origin)))(Source.Multi(origin))
            } :+ in.Return()(Source.Multi(origin))
          )(Source.Multi(origin))
        } else if (rets.size == returnAssign.size) {
          in.Seq(
            returnAssign.zip(rets).map{
              case (p, v) => in.SingleAss(in.Assignee.Var(p), v)(Source.Multi(origin))
            } :+ in.Return()(Source.Multi(origin))
          )(Source.Multi(origin))
        } else if (rets.size == 1) { // multi assignment
          in.Seq(Vector(
            in.MultiAss(returnAssign.map(v => in.Assignee.Var(v)), rets.head)(Source.Multi(origin)),
            in.Return()(Source.Multi(origin))
          ))(Source.Multi(origin))
        } else {
          violation(s"found ${rets.size} returns but expected 0, 1, or ${returnAssign.size}")
        }
      }

      // create context for body translation
      val ctx = new FunctionContext(assignReturns)

      // p1' := p1; ... ; pn' := pn
      val argInits = argLinks.flatMap{
        case (_, None) => None
        case (p, Some(q)) => Some(in.SingleAss(in.Assignee.Var(q(Source.Internal)), p)(Source.Internal))
      }

      (decl.args zip argLinks).foreach{
        case (PNamedParameter(id, _), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      // r1' := _; ... ; rn' := _
      val returnInits = returnVars.flatten.map{l =>
        val sl = l(Source.Internal)
        in.SingleAss(in.Assignee.Var(sl), in.DfltVal(sl.typ)(Source.Internal))(sl.src)
      }

      (decl.args zip argLinks).foreach{
        case (PNamedParameter(id, _), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }


      val bodyOpt = decl.body.map{s =>
        val vars = info.variables(s) map localVarD(ctx)
        val body = argInits ++ returnInits ++ stmtD(ctx)(s)
        in.Block(vars, body)(Source.Single(origin(s)))
      }

      in.Function(name, arg, returnAssign, Vector.empty, Vector.empty, bodyOpt)(Source.Single(origin(decl)))
    }

    def methodD(decl: PMethodDecl): in.Method = ???

    // Statements

    def stmtD(ctx: FunctionContext)(stmt: PStatement): Vector[in.Stmt] = {
      def go(s: PStatement): Vector[in.Stmt] = stmtD(ctx)(s)

      val src1 = Source.Single(origin(stmt))
      val srcN = Source.Multi(origin(stmt))
      def single(s: Source => in.Stmt): Vector[in.Stmt] = Vector(s(src1))

      import PrepM.{sequence, unit}

      stmt match {
        case _: PEmptyStmt => single(in.Seq(Vector.empty))

        case PSeq(stmts) => single(in.Seq(stmts flatMap go))

        case s@ PBlock(stmts) =>
          val vars = info.variables(s) map localVarD(ctx)
          single(in.Block(vars, stmts flatMap go))

        case PExpressionStmt(e) => exprD(ctx)(e).pre // TODO: check this translation

        case PAssignment(left, right) =>
          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
              (for{
                le <- assigneeD(ctx)(l)
                re <- exprD(ctx)(r)
              } yield in.SingleAss(le, re)(srcN) ).combine
            }
          } else if (right.size == 1) {
            (for{
              les <- sequence(left map assigneeD(ctx))
              re  <- exprD(ctx)(right.head)
            } yield in.MultiAss(les, re)(src1) ).combine
          } else { violation("invalid assignment") }

        case PShortVarDecl(right, left) =>

          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
              (for{
                le <- unit(Assignee.Var(localVarD(ctx)(l)))
                re <- exprD(ctx)(r)
              } yield in.SingleAss(le, re)(srcN) ).combine
            }
          } else if (right.size == 1) {
            (for{
              les <- unit(left.map{l =>  Assignee.Var(localVarD(ctx)(l))})
              re  <- exprD(ctx)(right.head)
            } yield in.MultiAss(les, re)(src1) ).combine
          } else { violation("invalid assignment") }

        case PVarDecl(typOpt, right, left) =>

          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
              (for{
                le <- unit(Assignee.Var(localVarD(ctx)(l)))
                re <- exprD(ctx)(r)
              } yield in.SingleAss(le, re)(srcN) ).combine
            }
          } else if (right.size == 1) {
            (for{
              les <- unit(left.map{l =>  Assignee.Var(localVarD(ctx)(l))})
              re  <- exprD(ctx)(right.head)
            } yield in.MultiAss(les, re)(src1) ).combine
          } else if (right.isEmpty && typOpt.nonEmpty) {
            val lelems = left.map{ l => Assignee.Var(localVarD(ctx)(l)) }
            val relems = left.map{ l => DfltVal(typeD(info.typ(typOpt.get)))(Source.Multi(origin(l))) }
            (lelems zip relems).map{ case (l, r) => in.SingleAss(l, r)(srcN) }

          } else { violation("invalid declaration") }

        case s@ PReturn(exps) =>
          (for{
            elems <- sequence(exps map exprD(ctx))
          } yield ctx.ret(elems)(origin(s)) ).combine


        case PAssert(ass) => ???

        case _ => ???
      }
    }

    case class PrepM[+R](pre: Vector[in.Stmt], res: R) {

      def map[Q](fun: R => Q): PrepM[Q] = PrepM(pre, fun(res))

      def star[Q](fun: PrepM[R => Q]): PrepM[Q] = fun match {
        case PrepM(opre, ores) => PrepM(pre ++ opre, ores(res))
      }

      def flatMap[Q](fun: R => PrepM[Q]): PrepM[Q] = fun(res) match {
        case PrepM(opre, ores) => PrepM(pre ++ opre, ores)
      }


      def unwrap: (Vector[in.Stmt], R) = (pre, res)
    }

    object PrepM {

      def unit[R](res: R): PrepM[R] = new PrepM(Vector.empty, res)

      def sequence[R](ws: Vector[PrepM[R]]): PrepM[Vector[R]] = {
        val (ss, rs) = ws.map(_.unwrap).unzip
        PrepM(ss.flatten, rs)
      }

      implicit class WamboFunctor[A,R](fun: A => R) {
        def <#>(w: PrepM[A]): PrepM[R] = w.map(fun)
      }

      implicit class WamboApplicable[A,R](fun: PrepM[A => R]) {
        def <*>(w: PrepM[A]): PrepM[R] = w.star(fun)
      }

      implicit class WamboFinish(w: PrepM[in.Stmt]) {
        def combine: Vector[in.Stmt] = w.pre :+ w.res
      }
    }


    // Expressions

    def assigneeD(ctx: FunctionContext)(expr: PExpression): PrepM[in.Assignee] = {

      import PrepM._

      expr match {
        case PNamedOperand(id) => unit(Assignee.Var(varD(ctx)(id)))

        case n@ PDereference(e) =>
          val typ = typeD(info.typ(n))
          for { eD <- exprD(ctx)(e) } yield Assignee.Ref(Deref(eD, typ)(Source.Single(origin(expr))))

        case _ => ???
      }
    }

    def exprD(ctx: FunctionContext)(expr: PExpression): PrepM[in.Expr] = {

      import PrepM._

      def go(e: PExpression): PrepM[in.Expr] = exprD(ctx)(e)

      val src1 = Source.Single(origin(expr))
      val srcN = Source.Multi(origin(expr))
      def single(s: PrepM[Source => in.Expr]): PrepM[in.Expr] = s.map(_(src1))

      val typ = typeD(info.typ(expr))

      expr match {
        case PNamedOperand(id) => unit(varD(ctx)(id))
        case PDereference(exp) => single(go(exp) map (in.Deref(_, typ)))
      }
    }


    def litD(ctx: FunctionContext)(lit: PLiteral): (Vector[in.Stmt], in.Expr) = {

      val src1 = Source.Single(origin(lit))
      val srcN = Source.Multi(origin(lit))
      def single(s: Source => in.Expr): (Vector[in.Stmt], in.Expr) = (Vector.empty, s(src1))

      lit match {
        case PIntLit(v) => single(in.IntLit(v))
        case PBoolLit(b) => single(in.BoolLit(b))
        case _ => ???
      }
    }



    // Type

    var types: Set[in.TopType] = Set.empty

    // TODO split into two phases to aggregate inlined struct and interface types
    def typeD(t: Type): in.TopType = {

      def typeDInner(t: Type): in.TopType = t match {
        case BooleanT => in.BoolT
        case IntT => in.IntT
        case PointerT(st) => in.PointerT(typeD(st))
        case VoidType => in.VoidT
        case DeclaredT(decl) => in.DefinedT(idName(decl.left), typeDInnermost(info.typ(decl.right)))
        case _ => ???
      }

      def typeDInnermost(t: Type): in.Type = t match { // TODO translate struct and interface type
        case _ => typeDInner(t)
      }

      val inT = t match {
          // if struct then change it to declared type
        case _ => typeDInner(t)
      }

      if (!(types contains inT)) {
        types += inT
      }

      inT
    }




    // Identifier

    def idName(id: PIdnNode): String = info.regular(id) match {
      case _: st.Function => nm.function(id.name, info.scope(id))
      case _: st.Variable => nm.variable(id.name, info.scope(id))
      case _: st.NamedType => nm.typ(id.name, info.scope(id))
      case _ => ???
    }

    def varD(ctx: FunctionContext)(id: PIdnNode): in.BodyVar = {
      require(info.regular(id).isInstanceOf[st.Variable])

      localVarD(ctx)(id)
    }

    def localVarD(ctx: FunctionContext)(id: PIdnNode): in.LocalVar = {
      require(info.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      ctx(id) match {
        case Some(v: in.LocalVar) => v
        case None => localVarContextFreeD(id)
        case _ => violation("expected local variable")
      }
    }

    def localVarContextFreeD(id: PIdnNode): in.LocalVar = {
      require(info.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      val typ = typeD(info.typ(id))

      if (info.addressed(id)) {
        LocalVar.Ref(idName(id), typ)(Source.Single(origin(id)))
      } else {
        LocalVar.Val(idName(id), typ)(Source.Single(origin(id)))
      }
    }

    // Miscellaneous

    def parameterD(p: PParameter): (in.Parameter, Option[Source => in.LocalVar]) = p match {
      case PNamedParameter(id, typ) =>
        val ip = in.Parameter(idName(id), typeD(info.typ(typ)))(Source.Single(origin(p)))
        (ip, Some(Sourced.unsource(Factory(() => localVarContextFreeD(id))))) // TODO: replace with copy

      case PUnnamedParameter(typ) =>
        (in.Parameter(nm.fresh, typeD(info.typ(typ)))(Source.Single(origin(p))), None)
    }

    // Ghost Statements





    private def origin(n: PNode): in.Origin = {
      val start = pom.positions.getStart(n).get
      val finish = pom.positions.getFinish(n).get
      val pos = pom.translate(start, finish)
      val code = pom.positions.substring(start, finish).get
      in.Origin(code, pos)
    }
  }

  private class NameManager {

    private val FRESH_PREFIX = "N"
    private val VARIABLE_PREFIX = "V"
    private val COPY_PREFIX = "C"
    private val FUNCTION_PREFIX = "F"
//    private val METHOD_PREFIX = "M"
    private val TYPE_PREFIX = "T"

    private var counter = 0

    private var scopeCounter = 0
    private var scopeMap: Map[PScope, Int] = Map.empty

    private def maybeRegister(s: PScope): Unit = {
      if (!(scopeMap contains s)) {
        scopeMap += (s -> scopeCounter)
        scopeCounter += 1
      }
    }

    private var substitutions: Map[(String, PScope), String] = Map.empty

    private def name(pre: String)(n: String, s: PScope): String = {
      maybeRegister(s)
      pre + scopeMap(s) + "_" + n // deterministic
    }

    def variable(n: String, s: PScope): String = name(VARIABLE_PREFIX)(n, s)
    def function(n: String, s: PScope): String = name(FUNCTION_PREFIX)(n, s)
    def typ     (n: String, s: PScope): String = name(TYPE_PREFIX)(n, s)

    def method  (n: String, t: Type): String = ???
    def field   (n: String, t: Type): String = ???

    def copyExt(n: String): String = COPY_PREFIX + n

    def fresh: String = {
      val f = FRESH_PREFIX + counter
      counter += 1
      f
    }




  }
}


