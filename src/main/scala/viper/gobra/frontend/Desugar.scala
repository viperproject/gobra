package viper.gobra.frontend

import viper.gobra.ast.frontend._
import viper.gobra.ast.internal._
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type.{IntT, PointerT, Type}
import viper.gobra.frontend.info.base.{SymbolTable => st}

object Desugar {

  def desugar(program: PProgram, info: viper.gobra.frontend.info.TypeInfo): in.Program = {
    in.Program()
  }

  private class Desugarer(pom: PositionManager, info: viper.gobra.frontend.info.TypeInfo) {

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

    def programD(p: PProgram): in.Program = ???

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConst] = ???

    def typeDefD(decl: PTypeDef): in.Type = ???

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

      stmt match {
        case _: PEmptyStmt => single(in.Seq(Vector.empty))

        case PSeq(stmts) => single(in.Seq(stmts flatMap go))

        case s@ PBlock(stmts) =>
          val vars = info.variables(s) map localVarD(ctx)
          single(in.Block(vars, stmts flatMap go))

        case PExpressionStmt(e) => exprD(ctx)(e)._1 // TODO: check this translation

        case PAssignment(left, right) =>
          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
                val (lpre, lelem) = assigneeD(ctx)(l)
                val (rpre, relem) = exprD(ctx)(r)
                lpre ++ rpre :+ in.SingleAss(lelem, relem)(srcN)
            }
          } else if (right.size == 1) {
            val (lpres, lelems) = left.map{ assigneeD(ctx) }.unzip
            val (rpre, relem) = exprD(ctx)(right.head)
            lpres.flatten ++ rpre :+ in.MultiAss(lelems, relem)(src1)

          } else { violation("invalid assignment") }

        case PShortVarDecl(right, left) =>

          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
              val lelem = Assignee.Var(localVarD(ctx)(l))
              val (rpre, relem) = exprD(ctx)(r)
              rpre :+ in.SingleAss(lelem, relem)(srcN)
            }
          } else if (right.size == 1) {
            val lelems = left.map{l =>  Assignee.Var(localVarD(ctx)(l))}
            val (rpre, relem) = exprD(ctx)(right.head)
            rpre :+ in.MultiAss(lelems, relem)(src1)

          } else { violation("invalid assignment") }

        case PVarDecl(typOpt, right, left) =>

          if (left.size == right.size) {
            (left zip right).flatMap{ case (l, r) =>
              val lelem = Assignee.Var(localVarD(ctx)(l))
              val (rpre, relem) = exprD(ctx)(r)
              rpre :+ in.SingleAss(lelem, relem)(srcN)
            }
          } else if (right.size == 1) {
            val lelems = left.map { l => Assignee.Var(localVarD(ctx)(l)) }
            val (rpre, relem) = exprD(ctx)(right.head)
            rpre :+ in.MultiAss(lelems, relem)(src1)

          } else if (right.isEmpty && typOpt.nonEmpty) {
            val lelems = left.map{ l => Assignee.Var(localVarD(ctx)(l)) }
            val relems = left.map{ l => DfltVal(typeD(info.typ(typOpt.get)))(Source.Multi(origin(l))) }
            (lelems zip relems).map{ case (l, r) => in.SingleAss(l, r)(srcN) }

          } else { violation("invalid declaration") }
      }
    }

    // Expressions

    def assigneeD(ctx: FunctionContext)(expr: PExpression): (Vector[in.Stmt], in.Assignee) = {
      expr match {
        case PNamedOperand(id) => (Vector.empty, Assignee.Var(varD(ctx)(id)))

        case n@ PDereference(e) =>
          val typ = typeD(info.typ(n))
          val (ss, ed) = exprD(ctx)(e)
          (ss, Assignee.Ref(Deref(ed, typ)(Source.Single(origin(expr)))))

        case _ => ???
      }
    }

    def exprD(ctx: FunctionContext)(expr: PExpression): (Vector[in.Stmt], in.Expr) = ???

    // Type

    // TODO split into two phases to aggregate inlined struct and interface types
    def typeD(t: Type): in.Type = t match {
      case IntT => in.IntT
      case PointerT(st) => in.PointerT(typeD(st))
      case _ => ???
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

    private def name(pre: String)(n: String, s: PScope): String = s{
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


