package viper.gobra.frontend

import viper.gobra.ast.frontend._
import viper.gobra.ast.internal.Node.Meta
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.reporting.{DesugaredMessage, Source}
import viper.gobra.util.{DesugarWriter, Violation}
import viper.silver.ast.SourcePosition

object Desugar {

  def desugar(program: PProgram, info: viper.gobra.frontend.info.TypeInfo)(config: Config): in.Program = {
    val internalProgram = new Desugarer(program.positions, info).programD(program)
    config.reporter report DesugaredMessage(config.inputFile, () => internalProgram)
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

    def functionProxyD(decl: PFunctionDecl): in.FunctionProxy = {
      getProxy(decl.id).getOrElse{
        val name = idName(decl.id)
        val proxy = in.FunctionProxy(name)(meta(decl))
        addProxy(decl.id, proxy)
        proxy
      }.asInstanceOf[in.FunctionProxy]
    }

    def methodProxyD(decl: PMethodDecl): in.MethodProxy = {
      getProxy(decl.id).getOrElse{
        val name = idName(decl.id)
        val proxy = in.MethodProxy(decl.id.name, name)(meta(decl))
        addProxy(decl.id, proxy)
        proxy
      }.asInstanceOf[in.MethodProxy]
    }

    def methodProxy(sym: st.Method): in.MethodProxy = {
      val (metaInfo, id) = sym match {
        case st.MethodImpl(decl, isGhost) => (meta(decl), decl.id)
        case st.MethodSpec(spec, isGhost) => (meta(spec), spec.id)
      }

      getProxy(id).getOrElse{
        val name = idName(id)
        val proxy = in.MethodProxy(id.name, name)(metaInfo)
        addProxy(id, proxy)
        proxy
      }.asInstanceOf[in.MethodProxy]
    }

    def fpredicateProxyD(decl: PFPredicateDecl): in.FPredicateProxy = {
      getProxy(decl.id).getOrElse{
        val name = idName(decl.id)
        val proxy = in.FPredicateProxy(name)(meta(decl))
        addProxy(decl.id, proxy)
        proxy
      }.asInstanceOf[in.FPredicateProxy]
    }

    def mpredicateProxyD(decl: PMPredicateDecl): in.MPredicateProxy = {
      getProxy(decl.id).getOrElse{
        val name = idName(decl.id)
        val proxy = in.MPredicateProxy(decl.id.name, name)(meta(decl))
        addProxy(decl.id, proxy)
        proxy
      }.asInstanceOf[in.MPredicateProxy]
    }

    def mpredicateProxy(sym: st.MPredicate): in.MPredicateProxy = {
      val (metaInfo, id) = sym match {
        case st.MPredicateImpl(decl) => (meta(decl), decl.id)
        case st.MPredicateSpec(_) => assert(false); ???
      }

      getProxy(id).getOrElse{
        val name = idName(id)
        val proxy = in.MPredicateProxy(id.name, name)(metaInfo)
        addProxy(id, proxy)
        proxy
      }.asInstanceOf[in.MPredicateProxy]
    }

    class FunctionContext(
                           val ret: Vector[in.Expr] => Meta => in.Stmt,
                           private var substitutions: Map[Identity, in.Var] = Map.empty
                         ) {

      def apply(id: PIdnNode): Option[in.Var] =
        substitutions.get(abstraction(id))


      def addSubst(from: PIdnNode, to: in.Var): Unit =
        substitutions += abstraction(from) -> to

      def copy: FunctionContext = new FunctionContext(ret, substitutions)
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
      val dMembers = p.declarations.flatMap{
        case NoGhost(x: PVarDecl) => varDeclGD(x)
        case NoGhost(x: PConstDecl) => constDeclD(x)
        case NoGhost(x: PMethodDecl) => Vector(methodD(x))
        case NoGhost(x: PFunctionDecl) => Vector(functionD(x))
        case x: PMPredicateDecl => Vector(mpredicateD(x))
        case x: PFPredicateDecl => Vector(fpredicateD(x))
        case _ => Vector.empty
      }

      p.declarations.foreach{
        case NoGhost(x: PTypeDef) => typeDefD(x)
        case _ =>
      }

      val table = new in.LookupTable(definedTypes)

      in.Program(types.toVector, dMembers, table)(meta(p))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConst] = ???

    def typeDefD(decl: PTypeDef): in.Type = typeD(DeclaredT(decl))

    def functionD(decl: PFunctionDecl): in.Member =
      if (decl.spec.isPure) pureFunctionD(decl) else {

      val name = functionProxyD(decl)
      val fsrc = meta(decl)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
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

      // create context for spec translation
      val specCtx = new FunctionContext(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach {
        // substitution has to be added since otherwise the parameter is translated as a addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      // translate pre- and postconditions before extending the context
      val pres = decl.spec.pres map preconditionD(specCtx)
      val posts = decl.spec.posts map postconditionD(specCtx)

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(in.SingleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(in.SingleAss(in.Assignee.Var(p), v)(fsrc))
          case _ => None
        } // :+ in.Return()(fsrc)

      // create context for body translation
      val ctx = new FunctionContext(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case (NoGhost(_: PUnnamedParameter), (_, Some(q))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }


      val bodyOpt = decl.body.map{ s =>
        val vars = argSubs.flatten ++ returnSubs.flatten
        val body = argInits ++ Vector(blockD(ctx)(s)) ++ resultAssignments
        in.Block(vars, body)(meta(s))
      }

      in.Function(name, args, returns, pres, posts, bodyOpt)(fsrc)
    }

    def pureFunctionD(decl: PFunctionDecl): in.PureFunction = {
      require(decl.spec.isPure)

      val name = functionProxyD(decl)
      val fsrc = meta(decl)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, _) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
      val (returns, _) = returnsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      // translate pre- and postconditions before extending the context
      val pres = decl.spec.pres map preconditionD(ctx)

      val bodyOpt = decl.body.map {
        b: PBlock =>
          b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case b => Violation.violation(s"unexpected pure function body: $b")
          }
      }

      in.PureFunction(name, args, returns, pres, bodyOpt)(fsrc)
    }



    def methodD(decl: PMethodDecl): in.Member =
      if (decl.spec.isPure) pureMethodD(decl) else {

      val name = methodProxyD(decl)
      val fsrc = meta(decl)

      val recvWithSubs = receiverD(decl.receiver)
      val (recv, recvSub) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
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

      // create context for spec translation
      val specCtx = new FunctionContext(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach{
        // substitution has to be added since otherwise the parameter is translated as a addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _, _)), (p, Some(q))) => specCtx.addSubst(id, parameterAsLocalValVar(p))
        case _ =>
      }

      // translate pre- and postconditions before extending the context
      val pres = decl.spec.pres map preconditionD(specCtx)
      val posts = decl.spec.posts map postconditionD(specCtx)

      // s' := s
      val recvInits = (recvWithSubs match {
        case (p, Some(q)) => Some(in.SingleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }).toVector

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(in.SingleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(in.SingleAss(in.Assignee.Var(p), v)(fsrc))
          case _ => None
        } // :+ in.Return()(fsrc)

      // create context for body translation
      val ctx = new FunctionContext(assignReturns)

      // extent context
      (decl.receiver, recvWithSubs) match {
        case (PNamedReceiver(id, _, _), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case (NoGhost(_: PUnnamedParameter), (_, Some(q))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }


      val bodyOpt = decl.body.map{ s =>
        val vars = recvSub.toVector ++ argSubs.flatten ++ returnSubs.flatten
        val body = recvInits ++ argInits ++ Vector(blockD(ctx)(s)) ++ resultAssignments
        in.Block(vars, body)(meta(s))
      }

      in.Method(recv, name, args, returns, pres, posts, bodyOpt)(fsrc)
    }

    def pureMethodD(decl: PMethodDecl): in.PureMethod = {
      require(decl.spec.isPure)

      val name = methodProxyD(decl)
      val fsrc = meta(decl)

      val recvWithSubs = receiverD(decl.receiver)
      val (recv, _) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, _) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
      val (returns, _) = returnsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      // translate pre- and postconditions before extending the context
      val pres = decl.spec.pres map preconditionD(ctx)

      val bodyOpt = decl.body.map {
        b: PBlock =>
          b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case b => Violation.violation(s"unexpected pure function body: $b")
          }
      }

      in.PureMethod(recv, name, args, returns, pres, bodyOpt)(fsrc)
    }

    def fpredicateD(decl: PFPredicateDecl): in.FPredicate = {
      val name = fpredicateProxyD(decl)
      val fsrc = meta(decl)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, _) = argsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      val bodyOpt = decl.body.map{ s =>
        specificationD(ctx)(s)
      }

      in.FPredicate(name, args, bodyOpt)(fsrc)
    }

    def mpredicateD(decl: PMPredicateDecl): in.MPredicate = {
      val name = mpredicateProxyD(decl)
      val fsrc = meta(decl)

      val recvWithSubs = receiverD(decl.receiver)
      val (recv, _) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, _) = argsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      val bodyOpt = decl.body.map{ s =>
        specificationD(ctx)(s)
      }

      in.MPredicate(recv, name, args, bodyOpt)(fsrc)
    }



    // Statements

    def maybeStmtD(ctx: FunctionContext)(stmt: Option[PStatement])(src: Source.Parser.Info): Writer[in.Stmt] =
      stmt.map(stmtD(ctx)).getOrElse(unit(in.Seqn(Vector.empty)(src)))

    def blockD(ctx: FunctionContext)(block: PBlock): in.Stmt = {
      val vars = info.variables(block) map localVarD(ctx)
      val ssW = sequence(block.nonEmptyStmts map (s => seqn(stmtD(ctx)(s))))
      in.Block(vars ++ ssW.decls, ssW.stmts ++ ssW.res)(meta(block))
    }

    def halfScopeFinish(ctx: FunctionContext)(scope: PScope)(desugared: in.Stmt): in.Block = {
      val decls = info.variables(scope) map localVarD(ctx)
      in.Block(decls, Vector(desugared))(desugared.info)
    }


    def stmtD(ctx: FunctionContext)(stmt: PStatement): Writer[in.Stmt] = {

      def goS(s: PStatement): Writer[in.Stmt] = stmtD(ctx)(s)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goA(a: PExpression): Writer[in.Assertion] = assertionD(ctx)(a)
      def goL(a: PAssignee): Writer[in.Assignee] = assigneeD(ctx)(a)

      val src: Meta = meta(stmt)

      stmt match {
        case NoGhost(noGhost) => noGhost match {
          case _: PEmptyStmt => unit(in.Seqn(Vector.empty)(src))

          case s: PSeq => for {ss <- sequence(s.nonEmptyStmts map goS)} yield in.Seqn(ss)(src)

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
              if (left.size == 1) {
                for{le <- goL(left.head); re <- goE(right.head)} yield in.SingleAss(le, re)(src)
              } else {
                // copy results to temporary variables and then to assigned variables
                val temps = left map (l => freshVar(typeD(info.typ(l)))(src))
                val resToTemps = (temps zip right).map{ case (l, r) =>
                  for{re <- goE(r)} yield in.SingleAss(in.Assignee.Var(l), re)(src)
                }
                val tempsToVars = (left zip temps).map{ case (l, r) =>
                  for{le <- goL(l)} yield in.SingleAss(le, r)(src)
                }
                declare(temps: _*) flatMap (_ =>
                  sequence(resToTemps ++ tempsToVars).map(in.Seqn(_)(src))
                  )
              }
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

          case PShortVarDecl(right, left, _) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  le <- unit(in.Assignee.Var(assignableVarD(ctx)(l)))
                  re <- goE(r)
                } yield in.SingleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                les <- unit(left.map{l => in.Assignee.Var(assignableVarD(ctx)(l))})
                re  <- goE(right.head)
              } yield multiassD(les, re)(src)
            } else { violation("invalid assignment") }

          case PVarDecl(typOpt, right, left, addressable) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  le <- unit(in.Assignee.Var(assignableVarD(ctx)(l)))
                  re <- goE(r)
                } yield in.SingleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                les <- unit(left.map{l =>  in.Assignee.Var(assignableVarD(ctx)(l))})
                re  <- goE(right.head)
              } yield multiassD(les, re)(src)
            } else if (right.isEmpty && typOpt.nonEmpty) {
              val lelems = left.map{ l => in.Assignee.Var(assignableVarD(ctx)(l)) }
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

    def derefD(ctx: FunctionContext)(p: ap.Deref)(src: Meta): Writer[in.Deref] = {
      exprD(ctx)(p.base) map (in.Deref(_)(src))
    }

    def fieldSelectionD(ctx: FunctionContext)(p: ap.FieldSelection)(src: Meta): Writer[in.FieldRef] = {
      val f = structMemberD(p.symb)
      for {
        r <- exprD(ctx)(p.base)
      } yield in.FieldRef(applyMemberPathD(r, p.path)(src), f)(src)
    }

    def functionCallD(ctx: FunctionContext)(p: ap.FunctionCall)(src: Meta): Writer[in.Expr] = {
      p.callee match {
        case base: ap.Symbolic =>
          base.symb match {
            case fsym: st.WithArguments with st.WithResult => // all patterns that have a callable symbol: function, received method, method expression

              // encode arguments
              val dArgs = sequence(p.args map exprD(ctx)).map {
                // go function chaining feature
                case Vector(in.Tuple(targs)) if fsym.args.size > 1 => targs
                case dargs => dargs
              }

              // encode result
              val resT = typeD(info.typ(fsym.result))
              val targets = fsym.result.outs map (o => freshVar(typeD(info.typ(o.typ)))(src))
              val res = if (targets.size == 1) targets.head else in.Tuple(targets)(src) // put returns into a tuple if necessary


              base match {
                case base: ap.Function =>
                  val fproxy = functionProxyD(base.symb.decl)

                  if (base.symb.isPure) {
                    for {
                      args <- dArgs
                    } yield in.PureFunctionCall(fproxy, args, resT)(src)
                  } else {
                    for {
                      args <- dArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.FunctionCall(targets, fproxy, args)(src))
                    } yield res
                  }

                case base: ap.ReceivedMethod =>
                  val fproxy = methodProxy(base.symb)

                  val dRecv = for {
                    r <- exprD(ctx)(base.recv)
                  } yield applyMemberPathD(r, base.path)(src)

                  if (base.symb.isPure) {
                    for {
                      recv <- dRecv
                      args <- dArgs
                    } yield in.PureMethodCall(recv, fproxy, args, resT)(src)
                  } else {
                    for {
                      recv <- dRecv
                      args <- dArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.MethodCall(targets, recv, fproxy, args)(src))
                    } yield res
                  }

                case base: ap.MethodExpr =>
                  val fproxy = methodProxy(base.symb)

                  // first argument is the receiver, the remaining arguments are the rest
                  val dRecvWithArgs = dArgs map (args => (applyMemberPathD(args.head, base.path)(src), args.tail))

                  if (base.symb.isPure) {
                    for {
                      (recv, args) <- dRecvWithArgs
                    } yield in.PureMethodCall(recv, fproxy, args, resT)(src)
                  } else {
                    for {
                      (recv, args) <- dRecvWithArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.MethodCall(targets, recv, fproxy, args)(src))
                    } yield res
                  }
              }

            case sym => Violation.violation(s"expected symbol with arguments and result, but got $sym")
          }
      }
    }

    def assigneeD(ctx: FunctionContext)(expr: PExpression): Writer[in.Assignee] = {

      val src: Meta = meta(expr)

      info.resolve(expr) match {
        case Some(p: ap.LocalVariable) =>
          unit(in.Assignee.Var(assignableVarD(ctx)(p.id)))
        case Some(p: ap.Deref) =>
          derefD(ctx)(p)(src) map in.Assignee.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx)(p)(src) map in.Assignee.Field

        case p => Violation.violation(s"unexpected ast pattern $p ")
      }
    }

    def addressableD(ctx: FunctionContext)(expr: PExpression): Writer[in.Addressable] = {


      val src: Meta = meta(expr)

      info.resolve(expr) match {
        case Some(p: ap.LocalVariable) =>
          varD(ctx)(p.id) match {
            case r: in.LocalVar.Ref => unit(in.Addressable.Var(r))
            case r => Violation.violation(s"expected variable reference but got $r")
          }
        case Some(p: ap.Deref) =>
          derefD(ctx)(p)(src) map in.Addressable.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx)(p)(src) map in.Addressable.Field

        case p => Violation.violation(s"unexpected ast pattern $p ")
      }
    }

    def exprD(ctx: FunctionContext)(expr: PExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(expr)

      val typ: in.Type = typeD(info.typ(expr))

      expr match {
        case NoGhost(noGhost) => noGhost match {
          case PNamedOperand(id) => unit[in.Expr](varD(ctx)(id))

          case n: PDeref => info.resolve(n) match {
            case Some(p: ap.Deref) => derefD(ctx)(p)(src)
            case _ => Violation.violation("cannot desugar pointer type to an expression")
          }

          case PReference(exp) => exp match {
              // go feature
            case c: PCompositeLit =>
              for {
                c <- compositeLitD(ctx)(c)
                co = compositeLitToObject(c)
                v = freshVar(in.PointerT(c.typ))(src)
                _ <- declare(v)
                _ <- write(in.Make(v, co)(src))
              } yield v

            case _ => addressableD(ctx)(exp) map (a => in.Ref(a, in.PointerT(a.op.typ))(src))
          }

          case n: PDot => info.resolve(n) match {
            case Some(p: ap.FieldSelection) => fieldSelectionD(ctx)(p)(src)
            case p => Violation.violation(s"only field selections can be desugared to an expression, but got $p")
          }

          case n: PInvoke =>
            info.resolve(n) match {
              case Some(p: ap.FunctionCall) => functionCallD(ctx)(p)(src)
              case Some(ap: ap.Conversion) => Violation.violation(s"desugarer: conversion $n is not supported")
              case Some(ap: ap.PredicateCall) => Violation.violation(s"cannot desugar a predicate call ($n) to an expression")
              case p => Violation.violation(s"expected function call, predicate call, or conversion, but got $p")
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

          case PUnfolding(acc, op) =>
            val dAcc = specificationD(ctx)(acc).asInstanceOf[in.Access]
            val dOp = pureExprD(ctx)(op)
            unit(in.Unfolding(dAcc, dOp)(src))

          case g: PGhostExpression => ghostExprD(ctx)(g)

          case e => Violation.violation(s"desugarer: $e is not supported")
        }
      }
    }

    def applyMemberPathD(base: in.Expr, path: Vector[MemberPath])(info: Source.Parser.Info): in.Expr = {
      path.foldLeft(base){ case (e, p) => p match {
        case MemberPath.Underlying => e
        case MemberPath.Deref => in.Deref(e)(info)
        case MemberPath.Ref => in.Ref(e)(info)
        case MemberPath.Next(g) => in.FieldRef(e, embeddedDeclD(g.decl))(info)
      }}
    }

    def litD(ctx: FunctionContext)(lit: PLiteral): Writer[in.Expr] = {

      val src: Meta = meta(lit)
      def single[E <: in.Expr](gen: Meta => E): Writer[in.Expr] = unit[in.Expr](gen(src))

      lit match {
        case PIntLit(v)  => single(in.IntLit(v))
        case PBoolLit(b) => single(in.BoolLit(b))
        case PNilLit() => single(in.NilLit())
        case c: PCompositeLit => compositeLitD(ctx)(c)
        case _ => ???
      }
    }

    def compositeLitToObject(lit: in.CompositeLit): in.CompositeObject = lit match {
      case l: in.StructLit => in.CompositeObject.Struct(l)
    }

    def compositeLitD(ctx: FunctionContext)(lit: PCompositeLit): Writer[in.CompositeLit] = lit.typ match {
      case t: PType =>
        val it = typeD(info.typ(t))
        literalValD(ctx)(lit.lit, it)

      case _ => ???
    }

    def underlyingType(t: Type.Type): Type.Type = t match {
      case Type.DeclaredT(d) => underlyingType(info.typ(d.right))
      case _ => t
    }

    sealed trait CompositeKind

    object CompositeKind {
      case class Struct(t: in.Type, st: in.StructT) extends CompositeKind
    }

    def compositeTypeD(t: in.Type): CompositeKind = t match {
      case _ if isStructType(t) => CompositeKind.Struct(t, structType(t).get)
      case _ => Violation.violation(s"expected composite type but got $t")
    }

    def underlyingType(typ: in.Type): in.Type = {
      typ match {
        case t: in.DefinedT => underlyingType(definedTypes(t.name)) // it is contained in the map, since 'typ' was translated
        case _ => typ
      }
    }

    def isStructType(typ: in.Type): Boolean = structType(typ).isDefined

    def structType(typ: in.Type): Option[in.StructT] = underlyingType(typ) match {
      case st: in.StructT => Some(st)
      case _ => None
    }



    def literalValD(ctx: FunctionContext)(lit: PLiteralValue, t: in.Type): Writer[in.CompositeLit] = {
      val src = meta(lit)

      compositeTypeD(t) match {

        case CompositeKind.Struct(it, ist) =>

          val fields = ist.fields

          if (lit.elems.exists(_.key.isEmpty)) {
            // all elements are not keyed
            val wArgs = fields.zip(lit.elems).map{ case (f, PKeyedElement(_, exp)) => exp match {
              case PExpCompositeVal(ev)  => exprD(ctx)(ev)
              case PLitCompositeVal(lv) => literalValD(ctx)(lv, f.typ)
            }}

            for {
              args <- sequence(wArgs)
            } yield in.StructLit(it, args)(src)

          } else { // all elements are keyed
            // maps field names to fields
            val fMap = fields.map(f => nm.inverse(f.name) -> f).toMap
            // maps fields to given value (if one is given)
            val vMap = lit.elems.map{
              case PKeyedElement(Some(PIdentifierKey(key)), exp) =>
                val f = fMap(key.name)
                exp match {
                  case PExpCompositeVal(ev) => f -> exprD(ctx)(ev)
                  case PLitCompositeVal(lv) => f -> literalValD(ctx)(lv, f.typ)
                }

              case _ => Violation.violation("expected identifier as a key")
            }.toMap
            // list of value per field
            val wArgs = fields.map{
              case f if vMap.isDefinedAt(f) => vMap(f)
              case f => unit(in.DfltVal(f.typ)(src))
            }

            for {
              args <- sequence(wArgs)
            } yield in.StructLit(it, args)(src)
          }
      }
    }

    // Type

    var types: Set[in.TopType] = Set.empty


    def registerType[T <: in.TopType](t: T): T = {
      types += t
      t
    }

    var definedTypes: Map[String, in.Type] = Map.empty
    var definedTypesSet: Set[String] = Set.empty

    def registerDefinedType(t: Type.DeclaredT): in.DefinedT = {
      val name = idName(t.decl.left)

      if (!definedTypesSet.contains(name)) {
        definedTypesSet += name
        val newEntry = typeD(info.typ(t.decl.right))
        definedTypes += (name -> newEntry)
      }

      in.DefinedT(name)
    }

    def embeddedTypeD(t: PEmbeddedType): in.Type = t match {
      case PEmbeddedName(typ) => typeD(info.typ(typ))
      case PEmbeddedPointer(typ) => registerType(in.PointerT(typeD(info.typ(typ))))
    }

    def typeD(t : Type) : in.Type = t match {
      case Type.VoidType => in.VoidT
      case Type.NilType => in.NilT
      case t: DeclaredT => registerType(registerDefinedType(t))
      case Type.BooleanT => in.BoolT
      case Type.IntT => in.IntT
      case Type.ArrayT(length, elem) => ???
      case Type.SliceT(elem) => ???
      case Type.MapT(key, elem) => ???
      case PointerT(elem) => registerType(in.PointerT(typeD(elem)))
      case Type.ChannelT(elem, mod) => ???
      case Type.SequenceT(elem) => in.SequenceT(typeD(elem))

      case Type.StructT(decl) =>
        var fields: List[in.Field] = List.empty

        decl.clauses foreach{
          case NoGhost(PFieldDecls(fs)) => fs foreach (f => fields ::= fieldDeclD(f))
          case NoGhost(e: PEmbeddedDecl) => fields ::= embeddedDeclD(e)
        }

        fields = fields.reverse

        val structName = nm.struct(decl, meta(decl))
        registerType(in.StructT(structName, fields.toVector))

      case Type.FunctionT(args, result) => ???
      case Type.InterfaceT(decl) => ???

      case Type.InternalTupleT(ts) => in.TupleT(ts map typeD)

      case _ => Violation.violation(s"got unexpected type $t")
    }


    // Identifier

    def idName(id: PIdnNode): String = info.regular(id) match {
      case _: st.Function => nm.function(id.name, info.scope(id))
      case _: st.MethodSpec => nm.spec(id.name, info.scope(id))
      case m: st.MethodImpl => nm.method(id.name, m.decl.receiver.typ)
      case _: st.FPredicate => nm.function(id.name, info.scope(id))
      case m: st.MPredicateImpl => nm.method(id.name, m.decl.receiver.typ)
      case _: st.Variable => nm.variable(id.name, info.scope(id))
      case _: st.Embbed | _: st.Field => nm.field(id.name, info.scope(id))
      case _: st.NamedType => nm.typ(id.name, info.scope(id))
      case _ => ???
    }

    def varD(ctx: FunctionContext)(id: PIdnNode): in.Var = {
      require(info.regular(id).isInstanceOf[st.Variable])

      ctx(id) match {
        case Some(v : in.Var) => v
        case Some(_) => violation("expected a variable")
        case None => localVarContextFreeD(id)
      }
    }

    def assignableVarD(ctx: FunctionContext)(id: PIdnNode) : in.AssignableVar = {
      require(info.regular(id).isInstanceOf[st.Variable])

      ctx(id) match {
        case Some(v: in.AssignableVar) => v
        case Some(_) => violation("expected an assignable variable")
        case None => localVarContextFreeD(id)
      }
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

      if (info.addressableVar(id)) {
        in.LocalVar.Ref(idName(id), typ)(src)
      } else {
        in.LocalVar.Val(idName(id), typ)(src)
      }
    }

    def parameterAsLocalValVar(p: in.Parameter): in.LocalVar.Val = {
      in.LocalVar.Val(p.id, p.typ)(p.info)
    }

    // Miscellaneous

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def inParameterD(p: PParameter, idx: Int): (in.Parameter.In, Option[in.LocalVar]) = p match {
      case NoGhost(noGhost: PActualParameter) =>
        noGhost match {
          case PNamedParameter(id, typ, _) =>
            val param = in.Parameter.In(idName(id), typeD(info.typ(typ)))(meta(p))
            val local = Some(localAlias(localVarContextFreeD(id)))
            (param, local)

          case PUnnamedParameter(typ) =>
            val param = in.Parameter.In(nm.inParam(idx, info.codeRoot(p)), typeD(info.typ(typ)))(meta(p))
            val local = None
            (param, local)
        }
    }

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def outParameterD(p: PParameter, idx: Int): (in.Parameter.Out, Option[in.LocalVar]) = p match {
      case NoGhost(noGhost: PActualParameter) =>
        noGhost match {
          case PNamedParameter(id, typ, _) =>
            val param = in.Parameter.Out(idName(id), typeD(info.typ(typ)))(meta(p))
            val local = Some(localAlias(localVarContextFreeD(id)))
            (param, local)

          case PUnnamedParameter(typ) =>
            val param = in.Parameter.Out(nm.outParam(idx, info.codeRoot(p)), typeD(info.typ(typ)))(meta(p))
            val local = None
            (param, local)
        }
    }

    def receiverD(p: PReceiver): (in.Parameter.In, Option[in.LocalVar]) = p match {
        case PNamedReceiver(id, typ, _) =>
          val param = in.Parameter.In(idName(id), typeD(info.typ(typ)))(meta(p))
          val local = Some(localAlias(localVarContextFreeD(id)))
          (param, local)

        case PUnnamedReceiver(typ) =>
          val param = in.Parameter.In(nm.receiver(info.codeRoot(p)), typeD(info.typ(typ)))(meta(p))
          val local = None
          (param, local)
    }

    def localAlias(internal: in.LocalVar): in.LocalVar = internal match {
      case in.LocalVar.Ref(id, typ) => in.LocalVar.Ref(nm.alias(id), typ)(internal.info)
      case in.LocalVar.Val(id, typ) => in.LocalVar.Val(nm.alias(id), typ)(internal.info)
      case in.LocalVar.Inter(id, typ) => assert(false); ???
    }

    def structClauseD(clause: PStructClause): Vector[in.Field] = clause match {
      case NoGhost(clause: PActualStructClause) => clause match {
        case PFieldDecls(fields) => fields map fieldDeclD
        case d: PEmbeddedDecl => Vector(embeddedDeclD(d))
      }
    }

    def structMemberD(m: st.StructMember): in.Field = m match {
      case st.Field(decl, _)  => fieldDeclD(decl)
      case st.Embbed(decl, _) => embeddedDeclD(decl)
    }

    def embeddedDeclD(decl: PEmbeddedDecl): in.Field =
      in.Field.Ref(idName(decl.id), embeddedTypeD(decl.typ))(meta(decl))

    def fieldDeclD(decl: PFieldDecl): in.Field = {
      val idname = idName(decl.id)
      val infoT = info.typ(decl.typ)
      val td = typeD(infoT)
      in.Field.Ref(idname, td)(meta(decl))
    }


    // Ghost Statement

    def ghostStmtD(ctx: FunctionContext)(stmt: PGhostStatement): Writer[in.Stmt] = {

      def goA(ass: PExpression): Writer[in.Assertion] = assertionD(ctx)(ass)

      val src: Meta = meta(stmt)

      stmt match {
        case PAssert(exp) => for {e <- goA(exp)} yield in.Assert(e)(src)
        case PAssume(exp) => for {e <- goA(exp)} yield in.Assume(e)(src)
        case PInhale(exp) => for {e <- goA(exp)} yield in.Inhale(e)(src)
        case PExhale(exp) => for {e <- goA(exp)} yield in.Exhale(e)(src)
        case PFold(exp)   => for {e <- goA(exp)} yield in.Fold(e.asInstanceOf[in.Access])(src)
        case PUnfold(exp) => for {e <- goA(exp)} yield in.Unfold(e.asInstanceOf[in.Access])(src)
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
        case POld(op) => for {o <- go(op)} yield in.Old(o)(src)
        case PConditional(cond, thn, els) =>
          for {
            wcond <- go(cond)
            wthn <- go(thn)
            wels <- go(els)
          } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PImplication(left, right) =>
          for {
            wcond <- go(left)
            wthn <- go(right)
            wels = in.BoolLit(b = true)(src)
          } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PSize(op) => for {
          dop <- go(op)
        } yield dop.typ match {
          case in.SequenceT(_) => in.SequenceLength(dop)(src)
          case t => violation(s"expected a sequence type but got '$t'")
        }

        case PIn(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield dright.typ match {
          case in.SequenceT(_) => in.SequenceContains(dleft, dright)(src)
          case t => violation(s"expected a sequence type but got '$t'")
        }

        case PSequenceLiteral(t, exprs) => for {
          dexprs <- sequence(exprs map go)
        } yield dexprs match {
          case Vector() => in.EmptySequence(typeD(info.typ(t)))(src)
          case _ => in.SequenceLiteral(dexprs)(src)
        }

        case PRangeSequence(low, high) => for {
          dlow <- go(low)
          dhigh <- go(high)
        } yield in.RangeSequence(dlow, dhigh)(src)

        case PSequenceAppend(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.SequenceAppend(dleft, dright)(src)

        case PSequenceUpdate(seq, left, right) => for {
          dseq <- go(seq)
          dleft <- go(left)
          dright <- go(right)
        } yield in.SequenceUpdate(dseq, dleft, dright)(src)

        case _ => Violation.violation(s"cannot desugar expression to an internal expression, $expr")
      }
    }

    def pureExprD(ctx: FunctionContext)(expr: PExpression): in.Expr = {
      val dExp = exprD(ctx)(expr)
      Violation.violation(dExp.stmts.isEmpty && dExp.decls.isEmpty, s"expected pure expression, but got $expr")
      dExp.res
    }


    // Assertion

    def specificationD(ctx: FunctionContext)(ass: PExpression): in.Assertion = {
      val condition = assertionD(ctx)(ass)
      Violation.violation(condition.stmts.isEmpty && condition.decls.isEmpty, s"assertion is not supported as a condition $ass")
      condition.res
    }

    def preconditionD(ctx: FunctionContext)(ass: PExpression): in.Assertion = {
      specificationD(ctx)(ass)
    }

    def postconditionD(ctx: FunctionContext)(ass: PExpression): in.Assertion = {
      specificationD(ctx)(ass)
    }


    def assertionD(ctx: FunctionContext)(n: PExpression): Writer[in.Assertion] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goA(a: PExpression): Writer[in.Assertion] = assertionD(ctx)(a)

      val src: Meta = meta(n)

      n match {
        case n: PImplication => for {l <- goE(n.left); r <- goA(n.right)} yield in.Implication(l, r)(src)
        case n: PConditional => // TODO: create Conditional Expression in internal ast
          for {
            cnd <- goE(n.cond)
            thn <- goA(n.thn)
            els <- goA(n.els)
          } yield in.SepAnd(in.Implication(cnd, thn)(src), in.Implication(in.Negation(cnd)(src), els)(src))(src)

        case n: PAnd => for {l <- goA(n.left); r <- goA(n.right)} yield in.SepAnd(l, r)(src)

        case n: PAccess => for {e <- accessibleD(ctx)(n.exp)} yield in.Access(e)(src)
        case n: PPredicateAccess => predicateCallD(ctx)(n.pred)

        case n: PInvoke => predicateCallD(ctx)(n)

        case _ => exprD(ctx)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallD(ctx: FunctionContext)(n: PInvoke): Writer[in.Assertion] = {

      val src: Meta = meta(n)

      info.resolve(n) match {
        case Some(p: ap.PredicateCall) =>
          predicateCallAccD(ctx)(p)(src) map (x => in.Access(in.Accessible.Predicate(x))(src))

        case _ => exprD(ctx)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallAccD(ctx: FunctionContext)(p: ap.PredicateCall)(src: Meta): Writer[in.PredicateAccess] = {

      val dArgs = p.args map pureExprD(ctx)

      p.predicate match {
        case b: ap.Predicate =>
          val fproxy = fpredicateProxyD(b.symb.decl)
          unit(in.FPredicateAccess(fproxy, dArgs)(src))

        case b: ap.ReceivedPredicate =>
          val dRecv = pureExprD(ctx)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val proxy = mpredicateProxy(b.symb)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs)(src))

        case b: ap.PredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val proxy = mpredicateProxy(b.symb)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs.tail)(src))
      }
    }



    def accessibleD(ctx: FunctionContext)(acc: PExpression): Writer[in.Accessible] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(acc)

      info.resolve(acc) match {
        case Some(p: ap.Deref) =>
          derefD(ctx)(p)(src) map in.Accessible.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx)(p)(src) map in.Accessible.Field
        case Some(p: ap.PredicateCall) =>
          predicateCallAccD(ctx)(p)(src) map (x => in.Accessible.Predicate(x))

        case p => Violation.violation(s"unexpected ast pattern $p ")
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
    private val IN_PARAMETER_PREFIX = "PI"
    private val OUT_PARAMETER_PREFIX = "PO"
    private val RECEIVER_PREFIX = "RECV"
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

    def inParam(idx: Int, s: PScope): String = name(IN_PARAMETER_PREFIX)("P" + idx, s)
    def outParam(idx: Int, s: PScope): String = name(OUT_PARAMETER_PREFIX)("P" + idx, s)
    def receiver(s: PScope): String = name(RECEIVER_PREFIX)("R", s)

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


