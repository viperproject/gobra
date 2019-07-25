package viper.gobra.frontend

import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.FileUtils
import viper.gobra.ast.frontend._
import viper.gobra.ast.internal.Node.Meta
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.reporting.Source
import viper.gobra.util.{DesugarWriter, OutputUtil, Violation}
import viper.silver.ast.SourcePosition

object Desugar {

  def desugar(program: PProgram, info: viper.gobra.frontend.info.TypeInfo)(config: Config): in.Program = {
    val internalProgram = new Desugarer(program.positions, info).programD(program)

    // print internal if set in config
    if (config.printInternal()) {
      val outputFile = OutputUtil.postfixFile(config.inputFile(), "internal")
      FileUtils.writeStringToFile(
        outputFile,
        internalProgram.formatted,
        UTF_8
      )

//      val uglyOutputFile = OutputUtil.postfixFile(config.inputFile(), "ugly")
//      FileUtils.writeStringToFile(
//        uglyOutputFile,
//        internalProgram.toString,
//        UTF_8
//      )
    }

    internalProgram
  }

  object NoGhost {
    def unapply(arg: PNode): Option[PNode] = arg match {
      case PGhostifier(x) => Some(x)
      case x => Some(x)
    }
  }

  private class Desugarer(pom: PositionManager, info: viper.gobra.frontend.info.TypeInfo) {

    // TODO: clean initialization

    type Meta = Source.Parser.Info

    import viper.gobra.util.Violation._

    val desugarWriter = new DesugarWriter
    import desugarWriter._
    type Writer[R] = desugarWriter.Writer[R]

//    def complete[X <: in.Stmt](w: Agg[X]): in.Stmt = {val (xs, x) = w.run; in.Seqn(xs :+ x)(x.info)}


    private val nm = new NameManager

    type Identity = Meta

    private def abstraction(id: PIdnNode): Identity = {
      meta(info.regular(id).rep)
    }

    // TODO: make thread safe
    private var proxies: Map[Meta, in.Proxy] = Map.empty

    def getProxy(id: PIdnNode): Option[in.Proxy] =
      proxies.get(abstraction(id))

    def addProxy(from: PIdnNode, to: in.Proxy): Unit =
      proxies += abstraction(from) -> to

    def proxyD(decl: PFunctionDecl): in.FunctionProxy = {
      getProxy(decl.id).getOrElse{
        val name = idName(decl.id)
        val proxy = in.FunctionProxy(name)(meta(decl))
        addProxy(decl.id, proxy)
        proxy
      }.asInstanceOf[in.FunctionProxy]
    }

    def proxyD(sym: st.Method): in.FunctionProxy = {
      val (metaInfo, id) = sym match {
        case st.MethodImpl(decl, isGhost) => (meta(decl), decl.id)
        case st.MethodSpec(spec, isGhost) => (meta(spec), spec.id)
      }

      getProxy(id).getOrElse{
        val name = idName(id)
        val proxy = in.FunctionProxy(name)(metaInfo)
        addProxy(id, proxy)
        proxy
      }.asInstanceOf[in.FunctionProxy]
    }

    class FunctionContext(val ret: Vector[in.Expr] => Meta => in.Stmt) {

      private var substitutions: Map[Identity, in.BodyVar] = Map.empty

      def apply(id: PIdnNode): Option[in.BodyVar] =
        substitutions.get(abstraction(id))


      def addSubst(from: PIdnNode, to: in.BodyVar): Unit =
        substitutions += abstraction(from) -> to
//
//      private var proxies: Map[Identity, in.Proxy] = Map.empty
//
//      def getProxy(id: PIdnNode): Option[in.Proxy] =
//        proxies.get(abstraction(id))
//
//      def addProxy(from: PIdnNode, to: in.Proxy): Unit =
//        proxies += abstraction(from) -> to
    }

    def programD(p: PProgram): in.Program = {
      val globalVarDecls = p.declarations.collect { case NoGhost(x: PVarDecl) => varDeclGD(x) }.flatten.distinct
      val globalConstDecls = p.declarations.collect{ case NoGhost(x: PConstDecl) => constDeclD(x) }.flatten.distinct
      val methods = p.declarations.collect{ case NoGhost(x: PMethodDecl) => methodD(x) }.distinct
      val functions = p.declarations.collect{ case NoGhost(x: PFunctionDecl) => functionD(x) }.distinct

      p.declarations.foreach{
        case NoGhost(x: PTypeDef) => typeDefD(x)
        case _ =>
      }

      in.Program(types.toVector, globalVarDecls, globalConstDecls, methods, functions)(meta(p))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConst] = ???

    def typeDefD(decl: PTypeDef): in.Type = typeD(DeclaredT(decl))

    def functionD(decl: PFunctionDecl): in.Function = {

      val name = proxyD(decl).name
      val fsrc = meta(decl)

      val argsWithSubs = decl.args map parameterD
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result match {
        case NoGhost(PVoidResult()) => Vector.empty
        case NoGhost(PResultClause(outs)) => outs map { o =>
          val (param, sub) = parameterD(o)
          (in.LocalVar.Val(param.id, param.typ)(param.info), sub)
        }
      }
      val (returns, returnSubs) = returnsWithSubs.unzip
      val actualReturns = returnsWithSubs.map{
        case (_, Some(x)) => x
        case (x, None)    => x
      }

      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt = {
        if (rets.isEmpty) {
          in.Seqn(
            returnsWithSubs.flatMap{
              case (p, Some(v)) => Some(in.SingleAss(in.Assignee.Var(p), v)(src))
              case _ => None
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == returns.size) {
          in.Seqn(
            returns.zip(rets).map{
              case (p, v) => in.SingleAss(in.Assignee.Var(p), v)(src)
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == 1) { // multi assignment
          in.Seqn(Vector(
            multiassD(returns.map(v => in.Assignee.Var(v)), rets.head)(src),
            in.Return()(src)
          ))(src)
        } else {
          violation(s"found ${rets.size} returns but expected 0, 1, or ${returns.size}")
        }
      }

      // create context for body translation
      val ctx = new FunctionContext(assignReturns)

      // translate pre- and postconditions before extending the context
      val pres = decl.spec.pres map preconditionD(ctx)
      val posts = decl.spec.posts map postconditionD(ctx)

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(in.SingleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // r1' := _; ... ; rn' := _
      val returnInits = actualReturns.map{l =>
        in.SingleAss(in.Assignee.Var(l), in.DfltVal(l.typ)(Source.Parser.Internal))(l.info)
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(in.SingleAss(in.Assignee.Var(p), v)(fsrc))
          case _ => None
        } // :+ in.Return()(fsrc)


      // extent context
      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      decl.result match {
        case PVoidResult() =>
        case PResultClause(outs) => (outs zip returnsWithSubs).foreach{
          case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
          case (NoGhost(_: PUnnamedParameter), (_, Some(q))) => violation("cannot have an alias for an unnamed parameter")
          case _ =>
        }
      }


      val bodyOpt = decl.body.map{ s =>
        val vars = argSubs.flatten ++ returnSubs.flatten
        val body = argInits ++ returnInits ++ Vector(blockD(ctx)(s)) ++ resultAssignments
        in.Block(vars, body)(meta(s))
      }

      in.Function(name, args, returns, pres, posts, bodyOpt)(fsrc)
    }



    def methodD(decl: PMethodDecl): in.Method = ???



    // Statements

    def maybeStmtD(ctx: FunctionContext)(stmt: Option[PStatement])(src: Source.Parser.Info): Writer[in.Stmt] =
      stmt.map(stmtD(ctx)).getOrElse(unit(in.Seqn(Vector.empty)(src)))

    def blockD(ctx: FunctionContext)(block: PBlock): in.Stmt = {
      val vars = info.variables(block) map localVarD(ctx)
      val ssW = sequence(block.stmts map (s => seqn(stmtD(ctx)(s))))
      in.Block(vars ++ ssW.decls, ssW.stmts ++ ssW.res)(meta(block))
    }

    def halfScopeFinish(ctx: FunctionContext)(scope: PScope)(desugared: in.Stmt): in.Block = {
      val decls = info.variables(scope) map localVarD(ctx)
      in.Block(decls, Vector(desugared))(desugared.info)
    }


    def stmtD(ctx: FunctionContext)(stmt: PStatement): Writer[in.Stmt] = {

      def goS(s: PStatement): Writer[in.Stmt] = stmtD(ctx)(s)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goA(a: PAssertion): Writer[in.Assertion] = assertionD(ctx)(a)
      def goL(a: PAssignee): Writer[in.Assignee] = assigneeD(ctx)(a)

      val src: Meta = meta(stmt)

      stmt match {
        case NoGhost(noGhost) => noGhost match {
          case _: PEmptyStmt => unit(in.Seqn(Vector.empty)(src))

          case PSeq(stmts) => for {ss <- sequence(stmts map goS)} yield in.Seqn(ss)(src)

          case b: PBlock => unit(blockD(ctx)(b))

          case s@ PIfStmt(ifs, els) =>
            val elsStmt = maybeStmtD(ctx)(els)(src)
            ifs.foldRight(elsStmt){
              case (PIfClause(pre, cond, body), c) =>
                for {
                  dPre <- maybeStmtD(ctx)(pre)(src)
                  dCond <- exprD(ctx)(cond)
                  dBody = blockD(ctx)(body)
                  els <- seqn(c)
                } yield in.Seqn(Vector(dPre, in.If(dCond, dBody, els)(src)))(src)
            }.map(halfScopeFinish(ctx)(s))

          case s@ PForStmt(pre, cond, post, spec, body) =>
            for {
              dPre <- maybeStmtD(ctx)(pre)(src)
              (dCondPre, dCond) <- prelude(exprD(ctx)(cond))
              (dInvPre, dInv) <- prelude(sequence(spec.invariants map assertionD(ctx)))
              dBody = blockD(ctx)(body)
              dPost <- maybeStmtD(ctx)(post)(src)

              wh = in.Seqn(
                Vector(dPre) ++ dCondPre ++ dInvPre ++ Vector(
                  in.While(dCond, dInv, in.Seqn(
                  Vector(dBody, dPost) ++ dCondPre ++ dInvPre
                  )(src))(src)
                )
              )(src)
            } yield halfScopeFinish(ctx)(s)(wh)

          case PExpressionStmt(e) =>
            val w = goE(e)
            create(stmts = w.stmts, decls = w.decls, res = in.Seqn(Vector.empty)(src))

          case PAssignment(right, left) =>
            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{le <- goL(l); re <- goE(r)} yield in.SingleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{les <- sequence(left map goL); re  <- goE(right.head)}
                yield multiassD(les, re)(src)
            } else { violation("invalid assignment") }

          case PAssignmentWithOp(right, op, left) =>
              for {
                l <- goL(left)
                r <- goE(right)

                rWithOp = op match {
                  case PAddOp() => in.Add(l.op, r)(src)
                  case PSubOp() => in.Sub(l.op, r)(src)
                  case PMulOp() => in.Mul(l.op, r)(src)
                  case PDivOp() => in.Div(l.op, r)(src)
                  case PModOp() => in.Mod(l.op, r)(src)
                }
              } yield in.SingleAss(l, rWithOp)(src)

          case PShortVarDecl(right, left) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  le <- unit(in.Assignee.Var(localVarD(ctx)(l)))
                  re <- goE(r)
                } yield in.SingleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                les <- unit(left.map{l => in.Assignee.Var(localVarD(ctx)(l))})
                re  <- goE(right.head)
              } yield multiassD(les, re)(src)
            } else { violation("invalid assignment") }

          case PVarDecl(typOpt, right, left) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  le <- unit(in.Assignee.Var(localVarD(ctx)(l)))
                  re <- goE(r)
                } yield in.SingleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                les <- unit(left.map{l =>  in.Assignee.Var(localVarD(ctx)(l))})
                re  <- goE(right.head)
              } yield multiassD(les, re)(src)
            } else if (right.isEmpty && typOpt.nonEmpty) {
              val lelems = left.map{ l => in.Assignee.Var(localVarD(ctx)(l)) }
              val relems = left.map{ l => in.DfltVal(typeD(info.typ(typOpt.get)))(meta(l)) }
              unit(in.Seqn((lelems zip relems).map{ case (l, r) => in.SingleAss(l, r)(src) })(src))

            } else { violation("invalid declaration") }

          case PReturn(exps) =>
            for{es <- sequence(exps map goE)} yield ctx.ret(es)(src)

          case g: PGhostStatement => ghostStmtD(ctx)(g)

          case _ => ???
        }

      }
    }

    def multiassD(lefts: Vector[in.Assignee], right: in.Expr)(src: Source.Parser.Info): in.Stmt = right match {
      case in.Tuple(args) if args.size == lefts.size =>
        in.Seqn(lefts.zip(args) map { case (l, r) => in.SingleAss(l, r)(src)})(src)

      case _ => Violation.violation(s"Multi assignment of $right to $lefts is not supported")
    }


    // Expressions

    def derefD(ctx: FunctionContext)(deref: PDereference): Writer[in.Deref] =
      exprD(ctx)(deref.operand) map (in.Deref(_, typeD(info.typ(deref.operand)))(meta(deref)))

    sealed trait ExprEntity

    object ExprEntity {
      case class Variable(v: Writer[in.BodyVar]) extends ExprEntity
      case class ReceivedDeref(deref: Writer[in.Deref]) extends ExprEntity
      case class ReceivedField(op: st.StructMember, rfield: Writer[in.FieldRef]) extends ExprEntity
      case class Function(op: st.Function) extends ExprEntity
      case class ReceivedMethod(op: st.Method, recv: Writer[in.Expr], path: in.MemberPath) extends ExprEntity
      case class MethodExpr(op: st.Method, path: in.MemberPath) extends ExprEntity
    }

    def exprEntityD(ctx: FunctionContext)(expr: PExpression): ExprEntity = expr match {
      case PNamedOperand(id) => info.regular(id) match {
        case f: st.Function => ExprEntity.Function(f)
        case v: st.Variable => ExprEntity.Variable(unit(varD(ctx)(id)))
        case _ => Violation.violation("expected entity behind expression")
      }

      case n: PDereference => ExprEntity.ReceivedDeref(derefD(ctx)(n))

      case PSelection(base, id) => info.selectionLookup(info.typ(base), id) match {
        case (m: st.Method, path) => ExprEntity.ReceivedMethod(m, exprD(ctx)(base), memberPathD(path))
        case (s: st.ActualStructMember, path) =>
          val f = structMemberD(s)
          val rfield = for {r <- exprD(ctx)(base)} yield in.FieldRef(r,f, memberPathD(path))(meta(expr))
          ExprEntity.ReceivedField(s, rfield)

        case _ => Violation.violation("expected entity behind expression")
      }

      case PMethodExpr(base, id) => info.memberLookup(info.typ(base), id) match {
        case (m: st.Method, path) => ExprEntity.MethodExpr(m, memberPathD(path))
        case _ => Violation.violation("expected entity behind expression")
      }

      case PSelectionOrMethodExpr(base, id) => info.regular(base) match {
        case _: st.TypeEntity => info.memberLookup(info.typ(base), id) match {
          case (m: st.Method, path) => ExprEntity.MethodExpr(m, memberPathD(path))
          case _ => Violation.violation("expected entity behind expression")
        }

        case _ => info.selectionLookup(info.typ(base), id) match {
          case (m: st.Method, path) => ExprEntity.ReceivedMethod(m, unit(varD(ctx)(base)), memberPathD(path))
          case (s: st.ActualStructMember, path) =>
            val f = structMemberD(s)
            val rfield = unit(in.FieldRef(varD(ctx)(base), f, memberPathD(path))(meta(expr)))
            ExprEntity.ReceivedField(s, rfield)

          case _ => Violation.violation("expected entity behind expression")
        }
      }

      case _ => Violation.violation("expected entity behind expression")
    }

    def assigneeD(ctx: FunctionContext)(expr: PExpression): Writer[in.Assignee] = {

      exprEntityD(ctx)(expr) match {
        case ExprEntity.Variable(v) => v map in.Assignee.Var
        case ExprEntity.ReceivedDeref(d) => d map in.Assignee.Pointer
        case ExprEntity.ReceivedField(_, f) => f map in.Assignee.Field

        case _ => ???
      }
    }

    def addressableD(ctx: FunctionContext)(expr: PExpression): Writer[in.Addressable] = {

      exprEntityD(ctx)(expr) match {
        case ExprEntity.Variable(v) => v.res match {
          case r: in.LocalVar.Ref => v map (_ => in.Addressable.Var(r))
          case r => Violation.violation(s"expected variable reference but got $r")
        }
        case ExprEntity.ReceivedField(_, f) => f map in.Addressable.Field

        case _ => ???
      }
    }

    def exprD(ctx: FunctionContext)(expr: PExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(expr)

      val typ: in.Type = typeD(info.typ(expr))

      expr match {
        case NoGhost(noGhost) => noGhost match {
          case PNamedOperand(id) => unit[in.Expr](varD(ctx)(id))

          case n: PDereference => derefD(ctx)(n)
          case PReference(exp) => exp match {
              // go feature
            case c: PCompositeLit => compositeLitD(ctx)(c)
            case _ => addressableD(ctx)(exp) map (a => in.Ref(a, in.PointerT(a.op.typ))(src))
          }

          case PSelection(base, id) => info.selectionLookup(info.typ(base), id) match {
            case (f: st.Field, path)  => for {r <- go(base)} yield in.FieldRef(r, fieldDeclD(f.decl), memberPathD(path))(src)
            case (e: st.Embbed, path) => for {r <- go(base)} yield in.FieldRef(r, embeddedDeclD(e.decl), memberPathD(path))(src)
            case _ => Violation.violation("expected field or embedding")
          }

          case PSelectionOrMethodExpr(base, id) => info.selectionLookup(info.typ(base), id) match { // has to be a selection
            case (f: st.Field, path)  => unit(in.FieldRef(varD(ctx)(base), fieldDeclD(f.decl), memberPathD(path))(src))
            case (e: st.Embbed, path) => unit(in.FieldRef(varD(ctx)(base), embeddedDeclD(e.decl), memberPathD(path))(src))
            case _ => Violation.violation("expected field or embedding")
          }

          case PCall(callee, args) => exprEntityD(ctx)(callee) match {
            case ExprEntity.Function(op) =>
              val fsym = op
              val fproxy = proxyD(fsym.decl)

              for {
                dArgs <- sequence(args map exprD(ctx))
                realArgs = dArgs match {
                  // go function chaining feature
                  case Vector(in.Tuple(targs)) if fsym.decl.args.size > 1 => targs
                  case dargs => dargs
                }
                targets = fsym.decl.result match {
                  case PVoidResult() => Vector.empty
                  case PResultClause(outs) => outs map (o => freshVar(typeD(info.typ(o.typ)))(src))
                }

                _ <- declare(targets: _*)
                _ <- write(in.FunctionCall(targets, fproxy, realArgs)(src))

                v = if (targets.size == 1) targets.head else in.Tuple(targets)(src)
              } yield v

            case ExprEntity.ReceivedMethod(op, recv, path) =>
              val (fargs, fres) = op match {
                case st.MethodImpl(decl, _) => (decl.args, decl.result)
                case st.MethodSpec(spec, _) => (spec.args, spec.result)
              }

              val fproxy = proxyD(op)


              for {
                dRecv <- recv
                dArgs <- sequence(args map exprD(ctx))
                realArgs = dArgs match {
                  // go function chaining feature
                  case Vector(in.Tuple(targs)) if fargs.size > 1 => targs
                  case dargs => dargs
                }
                targets = fres match {
                  case PVoidResult() => Vector.empty
                  case PResultClause(outs) => outs map (o => freshVar(typeD(info.typ(o.typ)))(src))
                }

                _ <- declare(targets: _*)
                _ <- write(in.MethodCall(targets, dRecv, fproxy, realArgs, path)(src))

                v = if (targets.size == 1) targets.head else in.Tuple(targets)(src)
              } yield v

            case ExprEntity.MethodExpr(op, path) => ???

            case e => Violation.violation(s"expected callable entity, but got $e")
          }

          case PConversionOrUnaryCall(base, arg) => base match {
            case id if info.regular(id).isInstanceOf[st.Function] =>
              val fsym = info.regular(id).asInstanceOf[st.Function]
              val fproxy = proxyD(fsym.decl)

              for {
                dArg <- exprD(ctx)(arg)
                realArgs = dArg match {
                  // go function chaining feature
                  case in.Tuple(targs) if fsym.decl.args.size > 1 => targs
                  case darg => Vector(darg)
                }
                targets = fsym.decl.result match {
                  case PVoidResult() => Vector.empty
                  case PResultClause(outs) => outs map (o => freshVar(typeD(info.typ(o.typ)))(src))
                }

                _ <- declare(targets: _*)
                _ <- write(in.FunctionCall(targets, fproxy, realArgs)(src))

                v = if (targets.size == 1) targets.head else in.Tuple(targets)(src)
              } yield v

            case e => Violation.violation(s"desugarer: conversion $e is not supported")
          }

          case PNegation(op) => for {o <- go(op)} yield in.Negation(o)(src)

          case PEquals(left, right) => for {l <- go(left); r <- go(right)} yield in.EqCmp(l, r)(src)
          case PUnequals(left, right) => for {l <- go(left); r <- go(right)} yield in.UneqCmp(l, r)(src)
          case PLess(left, right) => for {l <- go(left); r <- go(right)} yield in.LessCmp(l, r)(src)
          case PAtMost(left, right) => for {l <- go(left); r <- go(right)} yield in.AtMostCmp(l, r)(src)
          case PGreater(left, right) => for {l <- go(left); r <- go(right)} yield in.GreaterCmp(l, r)(src)
          case PAtLeast(left, right) => for {l <- go(left); r <- go(right)} yield in.AtLeastCmp(l, r)(src)

          case PAnd(left, right) => for {l <- go(left); r <- go(right)} yield in.And(l, r)(src)
          case POr(left, right) => for {l <- go(left); r <- go(right)} yield in.Or(l, r)(src)

          case PAdd(left, right) => for {l <- go(left); r <- go(right)} yield in.Add(l, r)(src)
          case PSub(left, right) => for {l <- go(left); r <- go(right)} yield in.Sub(l, r)(src)
          case PMul(left, right) => for {l <- go(left); r <- go(right)} yield in.Mul(l, r)(src)
          case PMod(left, right) => for {l <- go(left); r <- go(right)} yield in.Mod(l, r)(src)
          case PDiv(left, right) => for {l <- go(left); r <- go(right)} yield in.Div(l, r)(src)

          case l: PLiteral => litD(ctx)(l)

          case g: PGhostExpression => ghostExprD(ctx)(g)

          case e => Violation.violation(s"desugarer: $e is not supported")
        }
      }
    }

    def memberPathD(path: Vector[MemberPath]): in.MemberPath = {
      in.MemberPath(
        path map {
          case MemberPath.Underlying => in.MemberPath.Underlying
          case MemberPath.Deref => in.MemberPath.Deref
          case MemberPath.Ref => in.MemberPath.Ref
          case MemberPath.Next(decl) => in.MemberPath.Next(embeddedDeclD(decl.decl))
        }
      )
    }

    def litD(ctx: FunctionContext)(lit: PLiteral): Writer[in.Expr] = {

      val src: Meta = meta(lit)
      def single[E <: in.Expr](gen: Meta => E): Writer[in.Expr] = unit[in.Expr](gen(src))

      lit match {
        case PIntLit(v)  => single(in.IntLit(v))
        case PBoolLit(b) => single(in.BoolLit(b))
        case c: PCompositeLit => compositeLitD(ctx)(c)
        case _ => ???
      }
    }

    def compositeLitD(ctx: FunctionContext)(lit: PCompositeLit): Writer[in.Expr] = lit.typ match {
      case t: PType =>
        val it = typeD(info.typ(t))
        literalValD(ctx)(lit.lit, it)

      case _ => ???
    }


    def underlyingType(t: Type.Type): Type.Type = t match {
      case Type.DeclaredT(d) => underlyingType(info.typ(d.right))
      case _ => t
    }



//    def compositeTypeD(t: Type.Type, addressed: Boolean): (in.Composite, CompositeKind) = t match {
//      case Type.DeclaredT(d) =>
//        val newName = ???
//        val nextT = info.typ(d.right)
//        val (nextC, cKind) = compositeTypeD(nextT, addressed)
//        (in.Composite.Defined(newName, nextC), cKind)
//
//      case Type.StructT(d) =>
//        var fields: List[in.Field] = List.empty
//
//        d.clauses foreach{
//          case NoGhost(PFieldDecls(fs)) => fs foreach (f => fields ::= fieldDeclD(f))
//          case NoGhost(e: PEmbeddedDecl) => fields ::= embeddedDeclD(e)
//        }
//
//        val structName = ??? // TODO: every struct is mapped to two names (ref and val version) ... maybe one is enough
//
//        if (addressed) {
//          val ct = in.Composite.RefStruct(in.RefStructT(structName, fields.toVector))
//          (ct, CompositeKind.RefStruct(ct.op))
//        } else {
//          val ct = in.Composite.ValStruct(in.ValStructT(structName, fields.toVector))
//          (ct, CompositeKind.ValStruct(ct.op))
//        }
//    }


    sealed trait CompositeKind

    object CompositeKind {
      case class RefStruct(t: in.RefStructT, p: in.MemberPath) extends CompositeKind
      case class ValStruct(t: in.ValStructT, p: in.MemberPath) extends CompositeKind
    }

    def compositeTypeD(t: in.Type): (in.Composite, CompositeKind) = {
      def go(t: in.Type, p: Vector[in.MemberPath.Step]): (in.Composite, CompositeKind) = t match {
        case in.DefinedT(name, right) =>
          val (nextC, cKind) = go(right, p :+ in.MemberPath.Underlying)
          (in.Composite.Defined(name, nextC), cKind)

        case t: in.ValStructT => (in.Composite.ValStruct(t), CompositeKind.ValStruct(t, in.MemberPath(p)))
        case t: in.RefStructT => (in.Composite.RefStruct(t), CompositeKind.RefStruct(t, in.MemberPath(p)))

        case _ => Violation.violation(s"expected composite type but got $t")
      }

      go(t, Vector.empty)
    }

    def literalValD(ctx: FunctionContext)(lit: PLiteralValue, t: in.Type): Writer[in.Expr] = {
      val src = meta(lit)

      val (cLit, cKind) = compositeTypeD(t)
      val v = freshVar(t)(src)
      val creation = in.NewComposite(v, cLit)(src)

      cKind match {
        case _: CompositeKind.RefStruct | _: CompositeKind.ValStruct =>

          val (fields, path) = cKind match {
            case CompositeKind.RefStruct(it, p) => (it.fields, p)
            case CompositeKind.ValStruct(it, p) => (it.fields, p)
          }

          def fass(f: in.Field, e: in.Expr)(src: Source.Parser.Info): in.Stmt =
            in.SingleAss(in.Assignee.Field(in.FieldRef(v, f, path)(src)), e)(src)

          val assignments = if (lit.elems.exists(_.key.isEmpty)) {
            // all elements are not keyed
            fields.zip(lit.elems).map{ case (f, PKeyedElement(_, exp)) => exp match {
              case PExpCompositeVal(ev)  => for {e <- exprD(ctx)(ev)} yield fass(f, e)(meta(exp))
              case PLitCompositeVal(lv) => for {e <- literalValD(ctx)(lv, f.typ)} yield fass(f, e)(meta(exp))
            }}

          } else {
            // all elements are keyed
            val fMap = fields.map(f => nm.inverse(f.name) -> f).toMap
            lit.elems.map{
              case PKeyedElement(Some(PIdentifierKey(key)), exp) =>
                val f = fMap(key.name)
                exp match {
                  case PExpCompositeVal(ev)  => for {e <- exprD(ctx)(ev)} yield fass(f, e)(meta(exp))
                  case PLitCompositeVal(lv) => for {e <- literalValD(ctx)(lv, f.typ)} yield fass(f, e)(meta(exp))
                }

              case _ => Violation.violation("expected identifier as a key")
            }
          }

          for {
            _ <- declare(v)
            _ <- write(creation)
            asss <- sequence(assignments)
            _ <- write(asss: _*)
          } yield v

      }
    }

    // Type

    var types: Set[in.TopType] = Set.empty


    def registerType[T <: in.TopType](t: T): T = {
      types += t
      t
    }

    def embeddedTypeD(t: PEmbeddedType): in.Type = t match {
      case PEmbeddedName(typ) => typeD(info.typ(typ))
      case PEmbeddedPointer(typ) => registerType(in.PointerT(typeD(info.typ(typ))))
    }

    def typeD(t: Type): in.Type = t match {
      case Type.VoidType => in.VoidT
      case Type.NilType => in.NilT
      case DeclaredT(decl) => registerType(in.DefinedT(idName(decl.left), typeD(info.typ(decl.right))))
      case Type.BooleanT => in.BoolT
      case Type.IntT => in.IntT
      case Type.ArrayT(length, elem) => ???
      case Type.SliceT(elem) => ???
      case Type.MapT(key, elem) => ???
      case PointerT(elem) => registerType(in.PointerT(typeD(elem)))
      case Type.ChannelT(elem, mod) => ???

      case Type.StructT(decl) =>
        var fields: List[in.Field] = List.empty

        decl.clauses foreach{
          case NoGhost(PFieldDecls(fs)) => fs foreach (f => fields ::= fieldDeclD(f))
          case NoGhost(e: PEmbeddedDecl) => fields ::= embeddedDeclD(e)
        }

        val structName = nm.struct(decl, meta(decl))
        if (info.addressed(decl)) {
          in.RefStructT(structName, fields.toVector)
        } else {
          in.ValStructT(structName, fields.toVector)
        }

      case Type.FunctionT(args, result) => ???
      case Type.InterfaceT(decl) => ???

      case _ => Violation.violation(s"got unexpected type $t")
    }


    // Identifier

    def idName(id: PIdnNode): String = info.regular(id) match {
      case _: st.Function => nm.function(id.name, info.scope(id))
      case _: st.MethodSpec => nm.spec(id.name, info.scope(id))
      case m: st.MethodImpl => nm.method(id.name, m.decl.receiver.typ)
      case _: st.Variable => nm.variable(id.name, info.scope(id))
      case _: st.Embbed | _: st.Field => nm.field(id.name, info.scope(id))
      case _: st.NamedType => nm.typ(id.name, info.scope(id))
      case _ => ???
    }

    def varD(ctx: FunctionContext)(id: PIdnNode): in.BodyVar = {
      require(info.regular(id).isInstanceOf[st.Variable])

      localVarD(ctx)(id)
    }

    def freshVar(typ: in.Type)(info: Source.Parser.Info): in.LocalVar.Val =
      in.LocalVar.Val(nm.fresh, typ)(info)

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

      val src: Meta = meta(id)

      val typ = typeD(info.typ(id))

      if (info.addressed(id)) {
        in.LocalVar.Ref(idName(id), typ)(src)
      } else {
        in.LocalVar.Val(idName(id), typ)(src)
      }
    }

    // Miscellaneous

    def parameterD(p: PParameter): (in.Parameter, Option[in.LocalVar]) = p match {
      case NoGhost(noGhost: PActualParameter) => noGhost match {
        case PNamedParameter(id, typ) =>
          val param = in.Parameter(idName(id), typeD(info.typ(typ)))(meta(p))
          val local = Some(localAlias(localVarContextFreeD(id)))
          (param, local)

        case PUnnamedParameter(typ) =>
          val param = in.Parameter(nm.fresh, typeD(info.typ(typ)))(meta(p))
          val local = None
          (param, local)
      }
    }

    def localAlias(internal: in.LocalVar): in.LocalVar = internal match {
      case in.LocalVar.Ref(id, typ) => in.LocalVar.Ref(nm.alias(id), typ)(internal.info)
      case in.LocalVar.Val(id, typ) => in.LocalVar.Val(nm.alias(id), typ)(internal.info)
    }

    def structClauseD(clause: PStructClause): Vector[in.Field] = clause match {
      case NoGhost(clause: PActualStructClause) => clause match {
        case PFieldDecls(fields) => fields map fieldDeclD
        case d: PEmbeddedDecl => Vector(embeddedDeclD(d))
      }
    }

    def structMemberD(m: st.ActualStructMember): in.Field = m match {
      case st.Field(decl, _)  => fieldDeclD(decl)
      case st.Embbed(decl, _) => embeddedDeclD(decl)
    }

    def embeddedDeclD(decl: PEmbeddedDecl): in.Field =
      in.Field(idName(decl.id), embeddedTypeD(decl.typ), isEmbedding = true)(meta(decl))


    def fieldDeclD(decl: PFieldDecl): in.Field =
      in.Field(idName(decl.id), typeD(info.typ(decl.typ)), isEmbedding = false)(meta(decl))

    // Ghost Statement

    def ghostStmtD(ctx: FunctionContext)(stmt: PGhostStatement): Writer[in.Stmt] = {

      def goA(ass: PAssertion): Writer[in.Assertion] = assertionD(ctx)(ass)

      val src: Meta = meta(stmt)

      stmt match {
        case PAssert(exp) => for {e <- goA(exp)} yield in.Assert(e)(src)
        case PAssume(exp) => for {e <- goA(exp)} yield in.Assume(e)(src)
        case PInhale(exp) => for {e <- goA(exp)} yield in.Inhale(e)(src)
        case PExhale(exp) => for {e <- goA(exp)} yield in.Exhale(e)(src)
        case PExplicitGhostStatement(actual) => stmtD(ctx)(actual)
        case _ => ???
      }
    }

    // Ghost Expression

    def ghostExprD(ctx: FunctionContext)(expr: PGhostExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(expr)

      val typ = typeD(info.typ(expr))

      expr match {
        case _ => ???
      }
    }


    // Assertion

    def prePostConditionD(ctx: FunctionContext)(ass: PAssertion): in.Assertion = {
      val condition = assertionD(ctx)(ass)
      Violation.violation(condition.stmts.isEmpty && condition.decls.isEmpty, s"assertion is not supported as a condition $ass")
      condition.res
    }

    def preconditionD(ctx: FunctionContext)(ass: PAssertion): in.Assertion = {
      prePostConditionD(ctx)(ass)
    }

    def postconditionD(ctx: FunctionContext)(ass: PAssertion): in.Assertion = {
      prePostConditionD(ctx)(ass)
    }


    def assertionD(ctx: FunctionContext)(ass: PAssertion): Writer[in.Assertion] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goA(a: PAssertion): Writer[in.Assertion] = assertionD(ctx)(a)

      val src: Meta = meta(ass)

      ass match {
        case PStar(left, right) =>        for {l <- goA(left); r <- goA(right)} yield in.SepAnd(l, r)(src)
        case PExprAssertion(exp) =>       for {e <- goE(exp)}                   yield in.ExprAssertion(e)(src)
        case PImplication(left, right) => for {l <- goE(left); r <- goA(right)} yield in.Implication(l, r)(src)
        case PAccess(acc) =>              for {e <- accessibleD(ctx)(acc)}      yield in.Access(e)(src)

        case _ => ???
      }
    }

    def accessibleD(ctx: FunctionContext)(acc: PAccessible): Writer[in.Accessible] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(acc)

      acc match {
        case exp: PExpression => exprEntityD(ctx)(exp) match {
          case ExprEntity.ReceivedDeref(d) => d map in.Accessible.Ref
          case ExprEntity.ReceivedField(_, f) => f map in.Accessible.Field

          case _ => ???
        }
      }
    }



//    private def origin(n: PNode): in.Origin = {
//      val start = pom.positions.getStart(n).get
//      val finish = pom.positions.getFinish(n).get
//      val pos = pom.translate(start, finish)
//      val code = pom.positions.substring(start, finish).get
//      in.Origin(code, pos)
//    }

    private def meta(n: PNode): Meta = {
      val start = pom.positions.getStart(n).get
      val finish = pom.positions.getFinish(n).get
      val pos = pom.translate(start, finish)
      val tag = pom.positions.substring(start, finish).get
      Source.Parser.Single(n, Source.Origin(pos, tag))
    }
  }

  private class NameManager {

    private val FRESH_PREFIX = "N"
    private val VARIABLE_PREFIX = "V"
    private val FIELD_PREFIX = "A"
    private val COPY_PREFIX = "C"
    private val FUNCTION_PREFIX = "F"
    private val METHODSPEC_PREFIX = "S"
    private val METHOD_PREFIX = "M"
    private val TYPE_PREFIX = "T"
    private val STRUCT_PREFIX = "X"

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

    private def name(postfix: String)(n: String, s: PScope): String = {
      maybeRegister(s)
      s"${n}_$postfix${scopeMap(s)}" // deterministic
    }

    def variable(n: String, s: PScope): String = name(VARIABLE_PREFIX)(n, s)
    def typ     (n: String, s: PScope): String = name(TYPE_PREFIX)(n, s)
    def field   (n: String, s: PScope): String = name(FIELD_PREFIX)(n, s)
    def function(n: String, s: PScope): String = name(FUNCTION_PREFIX)(n, s)
    def spec    (n: String, s: PScope): String = name(METHODSPEC_PREFIX)(n, s)

    def method  (n: String, t: PMethodRecvType): String = t match {
      case PMethodReceiveName(typ)    => s"${n}_$METHOD_PREFIX${typ.name}"
      case PMethodReceivePointer(typ) => s"${n}_P$METHOD_PREFIX${typ.name}"
    }

    def inverse(n: String): String = n.substring(0, n.lastIndexOf('_'))

    def alias(n: String): String = s"${n}_$COPY_PREFIX$fresh"

    def fresh: String = {
      val f = FRESH_PREFIX + counter
      counter += 1
      f
    }

    private var structCounter: Int = 0
    private var structNames: Map[SourcePosition, String] = Map.empty

    def struct(s: PStructType, m: Meta): String = {
      structNames.getOrElse(m.origin.get.pos, {
        val newName = s"$STRUCT_PREFIX$$$structCounter"
        structCounter += 1
        structNames += m.origin.get.pos -> newName
        newName
      })
    }

  }
}


