// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.ast.frontend.{PExpression, AstPattern => ap, _}
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.ast.internal.{DfltVal, Lit, LocalVar}
import viper.gobra.frontend.info.base.Type.ChannelModus
import viper.gobra.frontend.info.{ExternalTypeInfo, TypeInfo}
import viper.gobra.reporting.{DesugaredMessage, Source}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.util.{DesugarWriter, Violation}

object Desugar {

  def desugar(pkg: PPackage, info: viper.gobra.frontend.info.TypeInfo)(config: Config): in.Program = {
    // independently desugar each imported package. Only used members (i.e. members for which `isUsed` returns true will be desugared:
    val importedPrograms = info.context.getContexts map { tI => {
      val typeInfo: TypeInfo = tI.getTypeInfo
      val importedPackage = typeInfo.tree.originalRoot
      val d = new Desugarer(importedPackage.positions, typeInfo)
      (d, d.packageD(importedPackage, tI.isUsed))
    }}
    // desugar the main package, i.e. the package on which verification is performed:
    val mainDesugarer = new Desugarer(pkg.positions, info)
    // combine all desugared results into one Viper program:
    val internalProgram = combine(mainDesugarer, mainDesugarer.packageD(pkg), importedPrograms)
    config.reporter report DesugaredMessage(config.inputFiles.head, () => internalProgram)
    internalProgram
  }

  private def combine(mainDesugarer: Desugarer, mainProgram: in.Program, imported: Iterable[(Desugarer, in.Program)]): in.Program = {
    val importedDesugarers = imported.map(_._1)
    val importedPrograms = imported.map(_._2)
    val types = mainProgram.types ++ importedPrograms.flatMap(_.types)
    val members = mainProgram.members ++ importedPrograms.flatMap(_.members)
    def combineTableField[X,Y](f: Desugarer => Map[X,Y]): Map[X,Y] =
      f(mainDesugarer) ++ importedDesugarers.flatMap(f)
    val table = new in.LookupTable(
      combineTableField(_.definedTypes),
      combineTableField(_.definedMethods),
      combineTableField(_.definedFunctions),
      combineTableField(_.definedMPredicates),
      combineTableField(_.definedFPredicates))
    in.Program(types, members, table)(mainProgram.info)
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
    type Writer[+R] = desugarWriter.Writer[R]

//    def complete[X <: in.Stmt](w: Agg[X]): in.Stmt = {val (xs, x) = w.run; in.Seqn(xs :+ x)(x.info)}


    private val nm = new NameManager

    type Identity = Meta

    private def abstraction(id: PIdnNode): Identity = {
      val entity = info.regular(id)
      meta(entity.rep)
    }

    // TODO: make thread safe
    private var proxies: Map[Meta, in.Proxy] = Map.empty

    def getProxy(id: PIdnNode): Option[in.Proxy] =
      proxies.get(abstraction(id))

    def addProxy(from: PIdnNode, to: in.Proxy): Unit =
      proxies += abstraction(from) -> to

    def functionProxyD(decl: PFunctionDecl): in.FunctionProxy = {
      val name = idName(decl.id)
      in.FunctionProxy(name)(meta(decl))
    }

    def functionProxyD(id: PIdnUse): in.FunctionProxy = {
      val name = idName(id)
      in.FunctionProxy(name)(meta(id))
    }

    def methodProxyD(decl: PMethodDecl): in.MethodProxy = {
      val name = idName(decl.id)
      in.MethodProxy(decl.id.name, name)(meta(decl))
    }

    def methodProxy(id: PIdnUse): in.MethodProxy = {
      val name = idName(id)
      in.MethodProxy(id.name, name)(meta(id))
    }

    def fpredicateProxyD(decl: PFPredicateDecl): in.FPredicateProxy = {
      val name = idName(decl.id)
      in.FPredicateProxy(name)(meta(decl))
    }

    def fpredicateProxyD(id: PIdnUse): in.FPredicateProxy = {
      val name = idName(id)
      in.FPredicateProxy(name)(meta(id))
    }

    def mpredicateProxyD(decl: PMPredicateDecl): in.MPredicateProxy = {
      val name = idName(decl.id)
      in.MPredicateProxy(decl.id.name, name)(meta(decl))
    }

    def mpredicateProxy(id: PIdnUse): in.MPredicateProxy = {
      val name = idName(id)
      in.MPredicateProxy(id.name, name)(meta(id))
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

    /**
      * Desugars a package with an optional `shouldDesugar` function indicating whether a particular member should be
      * desugared or skipped
      */
    def packageD(p: PPackage, shouldDesugar: PMember => Boolean = _ => true): in.Program = {
      val consideredDecls = p.declarations.collect { case m@NoGhost(x: PMember) if shouldDesugar(x) => m }
      val dMembers = consideredDecls.flatMap{
        case NoGhost(x: PVarDecl) => varDeclGD(x)
        case NoGhost(x: PConstDecl) => constDeclD(x)
        case NoGhost(x: PMethodDecl) => Vector(registerMethod(x))
        case NoGhost(x: PFunctionDecl) => Vector(registerFunction(x))
        case x: PMPredicateDecl => Vector(mpredicateD(x))
        case x: PFPredicateDecl => Vector(fpredicateD(x))
        case _ => Vector.empty
      }

      consideredDecls.foreach{
        case NoGhost(x: PTypeDef) => desugarAllTypeDefVariants(x)
        case _ =>
      }

      val table = new in.LookupTable(definedTypes, definedMethods, definedFunctions, definedMPredicates, definedFPredicates)

      in.Program(types.toVector, dMembers, table)(meta(p))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConstDecl] = decl.left.flatMap(l => info.regular(l) match {
      case sc@st.SingleConstant(_, id, _, _, _, _) =>
        val src = meta(id)
        val gVar = globalConstD(sc)(src)
        val intLit: Lit = gVar.typ match {
          case in.BoolT(Addressability.Exclusive) =>
            val constValue = sc.context.boolConstantEvaluation(sc.exp)
            in.BoolLit(constValue.get)(src)
          case in.IntT(Addressability.Exclusive, _) =>
            val constValue = sc.context.intConstantEvaluation(sc.exp)
            in.IntLit(constValue.get)(src)
          case _ => ???
        }
        Vector(in.GlobalConstDecl(gVar, intLit)(src))

      // Constants defined with the blank identifier can be safely ignored as they
      // must be computable statically (and thus do not have side effects) and
      // they can never be read
      case st.Wildcard(_, _) => Vector()

      case _ => ???
    })

    // Note: Alternatively, we could return the set of type definitions directly.
    //       However, currently, this would require to have versions of [[typeD]].
    /** desugars a defined type for each addressability modifier to register them with [[definedTypes]] */
    def desugarAllTypeDefVariants(decl: PTypeDef): Unit = {
      typeD(DeclaredT(decl, info), Addressability.Shared)(meta(decl))
      typeD(DeclaredT(decl, info), Addressability.Exclusive)(meta(decl))
    }

    def functionD(decl: PFunctionDecl): in.FunctionMember =
      if (decl.spec.isPure) pureFunctionD(decl) else {

      val name = functionProxyD(decl)
      val fsrc = meta(decl)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
      val (returns, returnSubs) = returnsWithSubs.unzip

      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt = {
        if (rets.isEmpty) {
          in.Seqn(
            returnsWithSubs.flatMap{
              case (p, Some(v)) => Some(singleAss(in.Assignee.Var(p), v)(src))
              case _ => None
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == returns.size) {
          in.Seqn(
            returns.zip(rets).map{
              case (p, v) => singleAss(in.Assignee.Var(p), v)(src)
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
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      // translate pre- and postconditions
      val pres = decl.spec.pres map preconditionD(specCtx)
      val posts = decl.spec.posts map postconditionD(specCtx)

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(singleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(singleAss(in.Assignee.Var(p), v)(fsrc))
          case _ => None
        } // :+ in.Return()(fsrc)

      // create context for body translation
      val ctx = new FunctionContext(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case (NoGhost(_: PUnnamedParameter), (_, Some(_))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }

      val bodyOpt = decl.body.map{ case (_, s) =>
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

      // extent context
      (decl.args zip argsWithSubs).foreach {
        // substitution has to be added since otherwise the parameter is translated as a addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p)
        case _ =>
      }

      // translate pre- and postconditions
      val pres = decl.spec.pres map preconditionD(ctx)
      val posts = decl.spec.posts map postconditionD(ctx)

      val bodyOpt = decl.body.map {
        case (_, b: PBlock) =>
          b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case b => Violation.violation(s"unexpected pure function body: $b")
          }
      }

      in.PureFunction(name, args, returns, pres, posts, bodyOpt)(fsrc)
    }



    def methodD(decl: PMethodDecl): in.MethodMember =
      if (decl.spec.isPure) pureMethodD(decl) else {

      val name = methodProxyD(decl)
      val fsrc = meta(decl)

      val recvWithSubs = receiverD(decl.receiver)
      val (recv, recvSub) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p,i) }
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i) }
      val (returns, returnSubs) = returnsWithSubs.unzip

      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt = {
        if (rets.isEmpty) {
          in.Seqn(
            returnsWithSubs.flatMap{
              case (p, Some(v)) => Some(singleAss(in.Assignee.Var(p), v)(src))
              case _ => None
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == returns.size) {
          in.Seqn(
            returns.zip(rets).map{
              case (p, v) => singleAss(in.Assignee.Var(p), v)(src)
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
        // substitution has to be added since otherwise the parameter is translated as an addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      (decl.receiver, recvWithSubs) match {
        case (NoGhost(PNamedReceiver(id, _, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p)
        case _ =>
      }

      // translate pre- and postconditions
      val pres = decl.spec.pres map preconditionD(specCtx)
      val posts = decl.spec.posts map postconditionD(specCtx)

      // s' := s
      val recvInits = (recvWithSubs match {
        case (p, Some(q)) => Some(singleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }).toVector

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(singleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(singleAss(in.Assignee.Var(p), v)(fsrc))
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
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q)
        case (NoGhost(_: PUnnamedParameter), (_, Some(_))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }


      val bodyOpt = decl.body.map{ case (_, s) =>
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

      // extent context
      (decl.args zip argsWithSubs).foreach{
        // substitution has to be added since otherwise the parameter is translated as an addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p)
        case _ =>
      }

      (decl.receiver, recvWithSubs) match {
        case (NoGhost(PNamedReceiver(id, _, _)), (p, _)) => ctx.addSubst(id, p)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p)
        case _ =>
      }

      // translate pre- and postconditions
      val pres = decl.spec.pres map preconditionD(ctx)
      val posts = decl.spec.posts map postconditionD(ctx)

      val bodyOpt = decl.body.map {
        case (_, b: PBlock) =>
          b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case s => Violation.violation(s"unexpected pure function body: $s")
          }
      }

      in.PureMethod(recv, name, args, returns, pres, posts, bodyOpt)(fsrc)
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
      def goL(a: PAssignee): Writer[in.Assignee] = assigneeD(ctx)(a)

      val src: Meta = meta(stmt)

      // Generates a fresh variable if `idn` is a wildcard or returns the variable
      // associated with idn otherwise
      def getVar(idn: PIdnNode)(t: in.Type): in.AssignableVar = idn match {
        case _: PWildcard => freshExclusiveVar(t.withAddressability(Addressability.Exclusive))(src)
        case x => assignableVarD(ctx)(x)
      }

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
                for{le <- goL(left.head); re <- goE(right.head)} yield singleAss(le, re)(src)
              } else {
                // copy results to temporary variables and then to assigned variables
                val temps = left map (l => freshExclusiveVar(typeD(info.typ(l), Addressability.exclusiveVariable)(src))(src))
                val resToTemps = (temps zip right).map{ case (l, r) =>
                  for{re <- goE(r)} yield singleAss(in.Assignee.Var(l), re)(src)
                }
                val tempsToVars = (left zip temps).map{ case (l, r) =>
                  for{le <- goL(l)} yield singleAss(le, r)(src)
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
              } yield singleAss(l, rWithOp)(src)

          case PShortVarDecl(right, left, _) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  re <- goE(r)
                  le <- unit(in.Assignee.Var(getVar(l)(re.typ)))
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                re  <- goE(right.head)
                les <-
                  unit(left.map{l =>  in.Assignee.Var(getVar(l)(typeD(info.typ(l), Addressability.exclusiveVariable)(src)))})
              } yield multiassD(les, re)(src)
            } else { violation("invalid assignment") }

          case PVarDecl(typOpt, right, left, _) =>

            if (left.size == right.size) {
              sequence((left zip right).map{ case (l, r) =>
                for{
                  re <- goE(r)
                  typ: in.Type = typOpt.map(x => typeD(info.symbType(x), Addressability.exclusiveVariable)(src)).getOrElse(re.typ)
                  le <- unit(in.Assignee.Var(getVar(l)(typ)))
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src))
            } else if (right.size == 1) {
              for{
                re  <- goE(right.head)
                les <- unit(left.map{l =>  in.Assignee.Var(getVar(l)(re.typ))})
              } yield multiassD(les, re)(src)
            } else if (right.isEmpty && typOpt.nonEmpty) {
              val typ = typeD(info.symbType(typOpt.get), Addressability.exclusiveVariable)(src)
              val lelems = left.map{ l => in.Assignee.Var(getVar(l)(typ)) }
              val relems = left.map{ l => in.DfltVal(typeD(info.symbType(typOpt.get), Addressability.defaultValue)(meta(l)))(meta(l)) }
              unit(in.Seqn((lelems zip relems).map{ case (l, r) => singleAss(l, r)(src) })(src))
            } else { violation("invalid declaration") }

          case PReturn(exps) =>
            for{es <- sequence(exps map goE)} yield ctx.ret(es)(src)

          case g: PGhostStatement => ghostStmtD(ctx)(g)

          case PExprSwitchStmt(pre, exp, cases, dflt) =>
            for {
              dPre <- maybeStmtD(ctx)(pre)(src)
              dExp <- exprD(ctx)(exp)
              exprVar = freshExclusiveVar(dExp.typ.withAddressability(Addressability.exclusiveVariable))(dExp.info)
              _ <- declare(exprVar)
              exprAss = singleAss(in.Assignee.Var(exprVar), dExp)(dExp.info)
              _ <- write(exprAss)
              clauses <- sequence(cases.map(c => switchCaseD(c, exprVar)(ctx)))

              dfltStmt <- if (dflt.size > 1) {
                violation("switch statement has more than one default clause")
              } else if (dflt.size == 1) {
                stmtD(ctx)(dflt(0))
              } else {
                unit(in.Seqn(Vector.empty)(src))
              }

              clauseBody = clauses.foldRight(dfltStmt){
                (clauseD, tail) =>
                  clauseD match {
                    case (exprCond, body) => in.If(exprCond, body, tail)(body.info)
                  }
              }
            } yield in.Seqn(Vector(dPre, exprAss, clauseBody))(src)

          case PGoStmt(exp) =>
            def unexpectedExprError(exp: PExpression) = violation(s"unexpected expression $exp in go statement")

            // nParams is the number of parameters in the function/method definition, and
            // args is the actual list of arguments
            def getArgs(nParams: Int, args: Vector[PExpression]): Writer[Vector[in.Expr]] = {
              sequence(args map exprD(ctx)).map {
                // go function chaining feature
                case Vector(in.Tuple(targs)) if nParams > 1 => targs
                case dargs => dargs
              }
            }

            exp match {
              case inv: PInvoke => info.resolve(inv) match {
                case Some(p: ap.FunctionCall) => p.callee match {
                  case ap.Function(_, st.Function(decl, _, _)) =>
                    val func = functionProxyD(decl)
                    for { args <- getArgs(decl.args.length, p.args) } yield in.GoFunctionCall(func, args)(src)

                  case ap.ReceivedMethod(recv, _, _, st.MethodImpl(decl, _, _)) =>
                    val meth = methodProxyD(decl)
                    for {
                      args <- getArgs(decl.args.length, p.args)
                      recvIn <- goE(recv)
                    } yield in.GoMethodCall(recvIn, meth, args)(src)

                  case ap.MethodExpr(_, _, _, st.MethodImpl(decl, _, _)) =>
                    val meth = methodProxyD(decl)
                    for {
                      args <- getArgs(decl.args.length, p.args.tail)
                      recv <- goE(p.args.head)
                    } yield in.GoMethodCall(recv, meth, args)(src)

                  case _ => unexpectedExprError(exp)
                }
                case _ => unexpectedExprError(exp)
              }
              case _ => unexpectedExprError(exp)
            }

          case _ => ???
        }
      }
    }

    def switchCaseD(switchCase: PExprSwitchCase, scrutinee: LocalVar)(ctx: FunctionContext): Writer[(in.Expr, in.Stmt)] =
      switchCase match {
        case PExprSwitchCase(left, body) => for {
          acceptExprs <- sequence(left.map(clause => exprD(ctx)(clause)))
          acceptCond = acceptExprs.foldRight(in.BoolLit(false)(meta(left.head)) : in.Expr){
            (expr, tail) => in.Or(in.EqCmp(scrutinee, expr)(expr.info), tail)(expr.info)
          }
          stmt <- stmtD(ctx)(body)
        } yield (acceptCond, stmt)
      }

    def multiassD(lefts: Vector[in.Assignee], right: in.Expr)(src: Source.Parser.Info): in.Stmt = {

      right match {
        case in.Tuple(args) if args.size == lefts.size =>
          in.Seqn(lefts.zip(args) map { case (l, r) => singleAss(l, r)(src)})(src)

        case n: in.TypeAssertion if lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable))(src)
          val successTarget = freshExclusiveVar(lefts(1).op.typ.withAddressability(Addressability.exclusiveVariable))(src)
          in.Block(
            Vector(resTarget, successTarget),
            Vector(
              in.SafeTypeAssertion(resTarget, successTarget, n.exp, n.arg)(n.info),
              singleAss(lefts(0), resTarget)(src),
              singleAss(lefts(1), successTarget)(src)
            )
          )(src)


        case _ => Violation.violation(s"Multi assignment of $right to $lefts is not supported")
      }
    }


    // Expressions

    def derefD(ctx: FunctionContext)(p: ap.Deref)(src: Meta): Writer[in.Deref] = {
      exprD(ctx)(p.base) map (in.Deref(_)(src))
    }

    def fieldSelectionD(ctx: FunctionContext)(p: ap.FieldSelection)(src: Meta): Writer[in.FieldRef] = {
      for {
        r <- exprD(ctx)(p.base)
        base = applyMemberPathD(r, p.path)(src)
        f = structMemberD(p.symb, Addressability.fieldLookup(base.typ.addressability))(src)
      } yield in.FieldRef(base, f)(src)
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
              val resT = typeD(fsym.context.typ(fsym.result), Addressability.callResult)(src)
              val targets = fsym.result.outs map (o => freshExclusiveVar(typeD(fsym.context.symbType(o.typ), Addressability.exclusiveVariable)(src))(src))
              val res = if (targets.size == 1) targets.head else in.Tuple(targets)(src) // put returns into a tuple if necessary

              base match {
                case base: ap.Function =>
                  val fproxy = functionProxyD(base.id)

                  if (base.symb.isPure) {
                    for {
                      args <- dArgs
                    } yield in.PureFunctionCall(fproxy, arguments(base.symb, args), resT)(src)
                  } else {
                    for {
                      args <- dArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.FunctionCall(targets, fproxy, arguments(base.symb, args))(src))
                    } yield res
                  }

                case base: ap.ReceivedMethod =>
                  val fproxy = methodProxy(base.id)

                  val dRecv = for {
                    r <- exprD(ctx)(base.recv)
                  } yield applyMemberPathD(r, base.path)(src)

                  if (base.symb.isPure) {
                    for {
                      recv <- dRecv
                      args <- dArgs
                    } yield in.PureMethodCall(recv, fproxy, arguments(base.symb, args), resT)(src)
                  } else {
                    for {
                      recv <- dRecv
                      args <- dArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.MethodCall(targets, recv, fproxy, arguments(base.symb, args))(src))
                    } yield res
                  }

                case base: ap.MethodExpr =>
                  val fproxy = methodProxy(base.id)

                  // first argument is the receiver, the remaining arguments are the rest
                  val dRecvWithArgs = dArgs map (args => (applyMemberPathD(args.head, base.path)(src), args.tail))

                  if (base.symb.isPure) {
                    for {
                      (recv, args) <- dRecvWithArgs
                    } yield in.PureMethodCall(recv, fproxy, arguments(base.symb, args), resT)(src)
                  } else {
                    for {
                      (recv, args) <- dRecvWithArgs
                      _ <- declare(targets: _*)
                      _ <- write(in.MethodCall(targets, recv, fproxy, arguments(base.symb, args))(src))
                    } yield res
                  }
              }

            case sym => Violation.violation(s"expected symbol with arguments and result, but got $sym")
          }
      }
    }

    def implicitConversion(from: in.Type, to: in.Type, exp: in.Expr): in.Expr = {

      val fromUt = underlyingType(from)
      val toUt = underlyingType(to)

      if (toUt.isInstanceOf[in.InterfaceT] && !fromUt.isInstanceOf[in.InterfaceT]) {
        in.ToInterface(exp, toUt)(exp.info)
      } else {
        exp
      }
    }

    def singleAss(left: in.Assignee, right: in.Expr)(info: Source.Parser.Info): in.SingleAss = {
      in.SingleAss(left, implicitConversion(right.typ, left.op.typ, right))(info)
    }

    def arguments(symb: st.WithArguments, args: Vector[in.Expr]): Vector[in.Expr] = {
      val c = args zip symb.args
      val assignments = c.map{ case (from, pTo) => (from, typeD(symb.context.symbType(pTo.typ), Addressability.inParameter)(from.info)) }
      assignments.map{ case (from, to) => implicitConversion(from.typ, to, from) }
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
        case Some(p : ap.IndexedExp) =>
          indexedExprD(p.base, p.index)(ctx)(src) map in.Assignee.Index
        case Some(ap.BlankIdentifier(decl)) =>
          for { expr <- exprD(ctx)(decl) } yield in.Assignee.Var(freshExclusiveVar(expr.typ)(src))
        case p => Violation.violation(s"unexpected ast pattern $p")
      }
    }

    def addressableD(ctx: FunctionContext)(expr: PExpression): Writer[in.Addressable] = {
      val src: Meta = meta(expr)

      info.resolve(expr) match {
        case Some(p: ap.LocalVariable) =>
          varD(ctx)(p.id) match {
            case r: in.LocalVar => unit(in.Addressable.Var(r))
            case r => Violation.violation(s"expected variable reference but got $r")
          }
        case Some(p: ap.Deref) =>
          derefD(ctx)(p)(src) map in.Addressable.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx)(p)(src) map in.Addressable.Field
        case Some(p: ap.IndexedExp) =>
          indexedExprD(p)(ctx)(src) map in.Addressable.Index

        case p => Violation.violation(s"unexpected ast pattern $p ")
      }
    }


    private def indexedExprD(base : PExpression, index : PExpression)(ctx : FunctionContext)(src : Meta) : Writer[in.IndexedExp] = {
      for {
        dbase <- exprD(ctx)(base)
        dindex <- exprD(ctx)(index)
      } yield in.IndexedExp(dbase, dindex)(src)
    }

    def indexedExprD(expr : PIndexedExp)(ctx : FunctionContext) : Writer[in.IndexedExp] =
      indexedExprD(expr.base, expr.index)(ctx)(meta(expr))
    def indexedExprD(expr : ap.IndexedExp)(ctx : FunctionContext)(src : Meta) : Writer[in.IndexedExp] =
      indexedExprD(expr.base, expr.index)(ctx)(src)

    def exprD(ctx: FunctionContext)(expr: PExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goTExpr(e: PExpressionOrType): Writer[in.Expr] = exprAndTypeAsExpr(ctx)(e)

      val src: Meta = meta(expr)

      expr match {
        case NoGhost(noGhost) => noGhost match {
          case n: PNamedOperand => info.resolve(n) match {
            case Some(p: ap.Constant) => unit(globalConstD(p.symb)(src))
            case Some(_: ap.LocalVariable) => unit(varD(ctx)(n.id))
            case Some(_: ap.NamedType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case p => Violation.violation(s"encountered unexpected pattern: $p")
          }

          case n: PDeref => info.resolve(n) match {
            case Some(p: ap.Deref) => derefD(ctx)(p)(src)
            case Some(p: ap.PointerType) => for { inElem <- goTExpr(p.base) } yield in.PointerTExpr(inElem)(src)
            case p => Violation.violation(s"encountered unexpected pattern: $p")
          }

          case PReference(exp) => exp match {
            // The reference of a literal is desugared to a new call
            case c: PCompositeLit =>
              for {
                c <- compositeLitD(ctx)(c)
                v = freshExclusiveVar(in.PointerT(c.typ.withAddressability(Addressability.Shared), Addressability.reference))(src)
                _ <- declare(v)
                _ <- write(in.New(v, c)(src))
              } yield v

            case _ => addressableD(ctx)(exp) map (a => in.Ref(a, in.PointerT(a.op.typ, Addressability.reference))(src))
          }

          case n: PDot => info.resolve(n) match {
            case Some(p: ap.FieldSelection) => fieldSelectionD(ctx)(p)(src)
            case Some(p: ap.Constant) => unit[in.Expr](globalConstD(p.symb)(src))
            case Some(_: ap.NamedType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case Some(p) => Violation.violation(s"only field selections, global constants, and types can be desugared to an expression, but got $p")
            case _ => Violation.violation(s"could not resolve $n")
          }

          case n: PInvoke =>
            info.resolve(n) match {
              case Some(p: ap.FunctionCall) => functionCallD(ctx)(p)(src)
              case Some(ap.Conversion(typ, arg)) =>
                val desugaredTyp = typeD(info.symbType(typ), info.addressability(n))(src)
                if (arg.length == 1) {
                  for { expr <- exprD(ctx)(arg(0)) } yield in.Conversion(desugaredTyp, expr)(src)
                } else {
                  Violation.violation(s"desugarer: conversion $n is not supported")
                }
              case Some(_: ap.PredicateCall) => Violation.violation(s"cannot desugar a predicate call ($n) to an expression")
              case p => Violation.violation(s"expected function call, predicate call, or conversion, but got $p")
            }

          case n: PTypeAssertion =>
            for {
              inExpr <- go(n.base)
              inArg = typeD(info.symbType(n.typ), Addressability.typeAssertionResult)(src)
            } yield in.TypeAssertion(inExpr, inArg)(src)

          case PNegation(op) => for {o <- go(op)} yield in.Negation(o)(src)

          case PEquals(left, right) =>
            for {
              l <- exprAndTypeAsExpr(ctx)(left)
              r <- exprAndTypeAsExpr(ctx)(right)
            } yield in.EqCmp(l, r)(src)

          case PUnequals(left, right) =>
            for {
              l <- exprAndTypeAsExpr(ctx)(left)
              r <- exprAndTypeAsExpr(ctx)(right)
            } yield in.UneqCmp(l, r)(src)

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

          case n : PIndexedExp => indexedExprD(n)(ctx)

          case PSliceExp(base, low, high, cap) => for {
            dbase <- go(base)
            dlow <- option(low map go)
            dhigh <- option(high map go)
            dcap <- option(cap map go)
          } yield dbase.typ match {
            case _: in.SequenceT => (dlow, dhigh) match {
              case (None, None) => dbase
              case (Some(lo), None) => in.SequenceDrop(dbase, lo)(src)
              case (None, Some(hi)) => in.SequenceTake(dbase, hi)(src)
              case (Some(lo), Some(hi)) =>
                val sub = in.Sub(hi, lo)(src)
                val drop = in.SequenceDrop(dbase, lo)(src)
                in.SequenceTake(drop, sub)(src)
            }
            case _: in.ArrayT | _: in.SliceT => (dlow, dhigh) match {
              case (None, None) => in.Slice(dbase, in.IntLit(0)(src), in.Length(dbase)(src), dcap)(src)
              case (Some(lo), None) => in.Slice(dbase, lo, in.Length(dbase)(src), dcap)(src)
              case (None, Some(hi)) => in.Slice(dbase, in.IntLit(0)(src), hi, dcap)(src)
              case (Some(lo), Some(hi)) => in.Slice(dbase, lo, hi, dcap)(src)
            }
            case t => Violation.violation(s"desugaring of slice expressions of base type $t is currently not supported")
          }

          case PLength(op) => for {
            dop <- go(op)
          } yield dop match {
            case dop : in.ArrayLit => in.IntLit(dop.length)(src)
            case dop : in.SequenceLit => in.IntLit(dop.length)(src)
            case _ => in.Length(dop)(src)
          }

          case PCapacity(op) => for {
            dop <- go(op)
          } yield dop match {
            case dop : in.ArrayLit => in.IntLit(dop.length)(src)
            case _ => dop.typ match {
              case _: in.ArrayT => in.Length(dop)(src)
              case _: in.SliceT => in.Capacity(dop)(src)
              case t => violation(s"expected an array or slice type, but got $t")
            }
          }

          case g: PGhostExpression => ghostExprD(ctx)(g)

          case b: PBlankIdentifier =>
            val typ = typeD(info.typ(b), Addressability.exclusiveVariable)(src)
            unit(freshExclusiveVar(typ)(src))

          case PMake(t, args) =>
            // TODO: maybe abstract this further (the common parts)
            def assertIsNonNegative(x: in.Expr): in.Stmt =
              in.Assert(in.ExprAssertion(in.AtLeastCmp(x, in.IntLit(0)(src))(src))(src))(src)
            def assertAtMost(left: in.Expr, right: in.Expr): in.Stmt =
              in.Assert(in.ExprAssertion(in.AtMostCmp(left, right)(src))(src))(src)

            val resT = typeD(info.symbType(t), Addressability.Exclusive)(src) // TODO: should it be shared by default?
            val target = freshExclusiveVar(resT)(src)

            for {
              _ <- declare(target)

              argsD <- sequence(args map go)

              arg0 = argsD.lift(0)
              arg1 = argsD.lift(1)

              // TODO: simplify this or move to a function
              // If arg0 is negative at runtime, then a panic occurs
              // _ <- if (arg0.isDefined) {write(assertIsNonNegative(arg0.get))} else write()
              // this one may be better
              _ <- sequence(arg0.toVector.map(x => write(assertIsNonNegative(x))))
              // if n and m are available at runtime, then n must be at most m otherwise it panics
              _ <- if (resT.isInstanceOf[SliceT] && arg0.isDefined && arg1.isDefined) {
                write(assertAtMost(arg0.get, arg1.get))
              } else write()

                // TODO: change type to Writer[Stmt]
              internalMake: in.MakeStmt = info.symbType(t) match {
                case SliceT(elem) =>
                  // TODO: parse args accordingly and add corresponging checks to arguments
                  // TODO: write a seqn around all writes of statements that I do
                  // violation("A length must be provided when making a slice")
                  in.MakeSlice(target, typeD(elem, Addressability.defaultValue)(src), arg0.get, arg1)(src) // TODO: defaultValue makes sense here?

                case ChannelT(elem, ChannelModus.Bi) =>
                  // TODO: implement when channels are added to the language
                  in.MakeChannel(target, typeD(elem, Addressability.defaultValue)(src), arg0)(src) // TODO: defaultValue makes sense here?

                case MapT(key, elem) =>
                  // TODO: implement when maps are added to the language
                  in.MakeMap(target, typeD(key, Addressability.defaultValue)(src), typeD(elem, Addressability.defaultValue)(src), arg0)(src) // TODO: defaultValue makes sense here?
              }


              // TODO: check that the size of the argument list is adequate for each type here and generate the corresponding res
              _ <- write(internalMake)
            } yield target

          case PNew(t) =>
            // TODO: clean code
            val resT = typeD(info.symbType(t), Addressability.Exclusive)(src)
            val targetT = in.PointerT(resT.withAddressability(Addressability.Shared), Addressability.reference)
            val target = freshExclusiveVar(targetT)(src)

            for {
              _ <- declare(target)
              zero = DfltVal(resT)(src)
              _ <- write(in.New(target, zero)(src))
            } yield target

          case e => Violation.violation(s"desugarer: $e is not supported")
        }
      }
    }

    def applyMemberPathD(base: in.Expr, path: Vector[MemberPath])(pinfo: Source.Parser.Info): in.Expr = {
      path.foldLeft(base){ case (e, p) => p match {
        case MemberPath.Underlying => e
        case MemberPath.Deref => in.Deref(e)(pinfo)
        case MemberPath.Ref => in.Ref(e)(pinfo)
        case MemberPath.Next(g) =>
          in.FieldRef(e, embeddedDeclD(g.decl, Addressability.fieldLookup(base.typ.addressability), info)(pinfo))(pinfo)
      }}
    }

    def exprAndTypeAsExpr(ctx: FunctionContext)(expr: PExpressionOrType): Writer[in.Expr] = {

      def go(x: PExpressionOrType): Writer[in.Expr] = exprAndTypeAsExpr(ctx)(x)

      val src: Meta = meta(expr)

      expr match {
        case e: PExpression => exprD(ctx)(e)

        case PBoolType() => unit(in.BoolTExpr()(src))

        case t: PIntegerType =>
          val st = info.symbType(t).asInstanceOf[Type.IntT]
          unit(in.IntTExpr(st.kind)(src))

        case PArrayType(len, elem) =>
          for {
            inLen <- exprD(ctx)(len)
            inElem <- go(elem)
          } yield in.ArrayTExpr(inLen, inElem)(src)

        case PSliceType(elem) =>
          for {
            inElem <- go(elem)
          } yield in.SliceTExpr(inElem)(src)

        case PSequenceType(elem) => for { inElem <- go(elem) } yield in.SequenceTExpr(inElem)(src)
        case PSetType(elem) => for { inElem <- go(elem) } yield in.SetTExpr(inElem)(src)
        case PMultisetType(elem) => for { inElem <- go(elem) } yield in.MultisetTExpr(inElem)(src)
        case POptionType(elem) => for { inElem <- go(elem) } yield in.OptionTExpr(inElem)(src)

          // TODO: struct and interface types will be added later.
        case t => Violation.violation(s"encountered unsupported type expression: $t")
      }
    }


    def litD(ctx: FunctionContext)(lit: PLiteral): Writer[in.Expr] = {

      val src: Meta = meta(lit)
      def single[E <: in.Expr](gen: Meta => E): Writer[in.Expr] = unit[in.Expr](gen(src))

      lit match {
        case PIntLit(v)  => single(in.IntLit(v))
        case PBoolLit(b) => single(in.BoolLit(b))
        case nil: PNilLit => single(in.NilLit(typeD(info.nilType(nil).getOrElse(Type.PointerT(Type.BooleanT)), Addressability.literal)(src))) // if no type is found, then use *bool
        case c: PCompositeLit => compositeLitD(ctx)(c)
        case _ => ???
      }
    }

    def compositeLitToObject(lit : in.CompositeLit) : in.CompositeObject = lit match {
      case l: in.ArrayLit => in.CompositeObject.Array(l)
      case l: in.SliceLit => in.CompositeObject.Slice(l)
      case l: in.StructLit => in.CompositeObject.Struct(l)
      case l: in.SequenceLit => in.CompositeObject.Sequence(l)
      case l: in.SetLit => in.CompositeObject.Set(l)
      case l: in.MultisetLit => in.CompositeObject.Multiset(l)
    }

    def compositeLitD(ctx: FunctionContext)(lit: PCompositeLit): Writer[in.CompositeLit] = lit.typ match {

      case t: PImplicitSizeArrayType =>
        val arrayLen : BigInt = lit.lit.elems.length
        val arrayTyp = typeD(info.symbType(t.elem), Addressability.arrayElement(Addressability.literal))(meta(lit))
        literalValD(ctx)(lit.lit, in.ArrayT(arrayLen, arrayTyp, Addressability.literal))

      case t: PType =>
        val it = typeD(info.symbType(t), Addressability.literal)(meta(lit))
        literalValD(ctx)(lit.lit, it)

      case _ => ???
    }

    def underlyingType(t: Type.Type): Type.Type = t match {
      case Type.DeclaredT(d, context) => underlyingType(context.symbType(d.right))
      case _ => t
    }

    sealed trait CompositeKind

    object CompositeKind {
      case class Array(t : in.ArrayT) extends CompositeKind
      case class Slice(t : in.SliceT) extends CompositeKind
      case class Multiset(t : in.MultisetT) extends CompositeKind
      case class Sequence(t : in.SequenceT) extends CompositeKind
      case class Set(t : in.SetT) extends CompositeKind
      case class Struct(t: in.Type, st: in.StructT) extends CompositeKind
    }

    def compositeTypeD(t : in.Type) : CompositeKind = t match {
      case _ if isStructType(t) => CompositeKind.Struct(t, structType(t).get)
      case t: in.ArrayT => CompositeKind.Array(t)
      case t: in.SliceT => CompositeKind.Slice(t)
      case t: in.SequenceT => CompositeKind.Sequence(t)
      case t: in.SetT => CompositeKind.Set(t)
      case t: in.MultisetT => CompositeKind.Multiset(t)
      case _ => Violation.violation(s"expected composite type but got $t")
    }

    def underlyingType(typ: in.Type): in.Type = {
      typ match {
        case t: in.DefinedT => underlyingType(definedTypes(t.name, t.addressability)) // it is contained in the map, since 'typ' was translated
        case _ => typ
      }
    }

    def isStructType(typ: in.Type): Boolean = structType(typ).isDefined

    def structType(typ: in.Type): Option[in.StructT] = underlyingType(typ) match {
      case st: in.StructT => Some(st)
      case _ => None
    }

    def compositeValD(ctx : FunctionContext)(expr : PCompositeVal, typ : in.Type) : Writer[in.Expr] = {
      expr match {
        case PExpCompositeVal(e) => exprD(ctx)(e)
        case PLitCompositeVal(lit) => literalValD(ctx)(lit, typ)
      }
    }

    def literalValD(ctx: FunctionContext)(lit: PLiteralValue, t: in.Type): Writer[in.CompositeLit] = {
      val src = meta(lit)

      compositeTypeD(t) match {

        case CompositeKind.Struct(it, ist) => {
          val fields = ist.fields

          if (lit.elems.exists(_.key.isEmpty)) {
            // all elements are not keyed
            val wArgs = fields.zip(lit.elems).map { case (f, PKeyedElement(_, exp)) => exp match {
              case PExpCompositeVal(ev) => exprD(ctx)(ev)
              case PLitCompositeVal(lv) => literalValD(ctx)(lv, f.typ)
            }}

            for {
              args <- sequence(wArgs)
            } yield in.StructLit(it, args)(src)

          } else { // all elements are keyed
            // maps field names to fields
            val fMap = fields.map(f => nm.inverse(f.name) -> f).toMap
            // maps fields to given value (if one is given)
            val vMap = lit.elems.map {
              case PKeyedElement(Some(PIdentifierKey(key)), exp) =>
                val f = fMap(key.name)
                exp match {
                  case PExpCompositeVal(ev) => f -> exprD(ctx)(ev)
                  case PLitCompositeVal(lv) => f -> literalValD(ctx)(lv, f.typ)
                }

              case _ => Violation.violation("expected identifier as a key")
            }.toMap
            // list of value per field
            val wArgs = fields.map {
              case f if vMap.isDefinedAt(f) => vMap(f)
              case f => unit(in.DfltVal(f.typ)(src))
            }

            for {
              args <- sequence(wArgs)
            } yield in.StructLit(it, args)(src)
          }
        }

        case CompositeKind.Array(in.ArrayT(len, typ, addressability)) =>
          Violation.violation(addressability == Addressability.literal, "Literals have to be exclusive")
          for { elemsD <- sequence(lit.elems.map(e => compositeValD(ctx)(e.exp, typ))) }
            yield in.ArrayLit(len, typ, info.keyElementIndices(lit.elems).zip(elemsD).toMap)(src)

        case CompositeKind.Slice(in.SliceT(typ, addressability)) =>
          Violation.violation(addressability == Addressability.literal, "Literals have to be exclusive")
          for { elemsD <- sequence(lit.elems.map(e => compositeValD(ctx)(e.exp, typ))) }
            yield in.SliceLit(typ, info.keyElementIndices(lit.elems).zip(elemsD).toMap)(src)

        case CompositeKind.Sequence(in.SequenceT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx)(e.exp, typ)))
          elemsMap = info.keyElementIndices(lit.elems).zip(elemsD).toMap
          lengthD = if (elemsMap.isEmpty) BigInt(0) else elemsMap.maxBy(_._1)._1 + 1
        } yield in.SequenceLit(lengthD, typ, elemsMap)(src)

        case CompositeKind.Set(in.SetT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx)(e.exp, typ)))
        } yield in.SetLit(typ, elemsD)(src)

        case CompositeKind.Multiset(in.MultisetT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx)(e.exp, typ)))
        } yield in.MultisetLit(typ, elemsD)(src)
      }
    }

    // Type

    var types: Set[in.TopType] = Set.empty


    def registerType[T <: in.TopType](t: T): T = {
      types += t
      t
    }

    var definedTypes: Map[(String, Addressability), in.Type] = Map.empty
    var definedTypesSet: Set[(String, Addressability)] = Set.empty
    var definedFunctions : Map[in.FunctionProxy, in.FunctionMember] = Map.empty
    var definedMethods: Map[in.MethodProxy, in.MethodMember] = Map.empty
    var definedMPredicates: Map[in.MPredicateProxy, in.MPredicate] = Map.empty
    var definedFPredicates: Map[in.FPredicateProxy, in.FPredicate] = Map.empty

    def registerDefinedType(t: Type.DeclaredT, addrMod: Addressability)(src: Meta): in.DefinedT = {
      // this type was declared in the current package
      val name = nm.typ(t.decl.left.name, t.context)

      if (!definedTypesSet.contains(name, addrMod)) {
        definedTypesSet += ((name, addrMod))
        val newEntry = typeD(t.context.symbType(t.decl.right), Addressability.underlying(addrMod))(src)
        definedTypes += (name, addrMod) -> newEntry
      }

      in.DefinedT(name, addrMod)
    }

    def registerMethod(decl: PMethodDecl): in.MethodMember = {
      val method = methodD(decl)
      val methodProxy = methodProxyD(decl)
      definedMethods += methodProxy -> method
      method
    }

    def registerFunction(decl: PFunctionDecl): in.FunctionMember = {
      val function = functionD(decl)
      val functionProxy = functionProxyD(decl)
      definedFunctions += functionProxy -> function
      function
    }

    def registerMPredicate(decl: PMPredicateDecl): in.MPredicate = {
      val mPredProxy = mpredicateProxyD(decl)
      val mPred = mpredicateD(decl)
      definedMPredicates += mPredProxy -> mPred
      mPred
    }

    def registerMPredicate(decl: PFPredicateDecl): in.FPredicate = {
      val fPredProxy = fpredicateProxyD(decl)
      val fPred = fpredicateD(decl)
      definedFPredicates += fPredProxy -> fPred
      fPred
    }

    def embeddedTypeD(t: PEmbeddedType, addrMod: Addressability)(src: Meta): in.Type = t match {
      case PEmbeddedName(typ) => typeD(info.symbType(typ), addrMod)(src)
      case PEmbeddedPointer(typ) =>
        registerType(in.PointerT(typeD(info.symbType(typ), Addressability.pointerBase)(src), addrMod))
    }

    def typeD(t: Type, addrMod: Addressability)(src: Source.Parser.Info): in.Type = t match {
      case Type.VoidType => in.VoidT
      case t: DeclaredT => registerType(registerDefinedType(t, addrMod)(src))
      case Type.BooleanT => in.BoolT(addrMod)
      case Type.IntT(x) => in.IntT(addrMod, x)
      case Type.ArrayT(length, elem) => in.ArrayT(length, typeD(elem, Addressability.arrayElement(addrMod))(src), addrMod)
      case Type.SliceT(elem) => in.SliceT(typeD(elem, Addressability.sliceElement)(src), addrMod)
      case Type.MapT(_, _) => ???
      case Type.OptionT(elem) => in.OptionT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case PointerT(elem) => registerType(in.PointerT(typeD(elem, Addressability.pointerBase)(src), addrMod))
      case Type.ChannelT(_, _) => ???
      case Type.SequenceT(elem) => in.SequenceT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case Type.SetT(elem) => in.SetT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case Type.MultisetT(elem) => in.MultisetT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)

      case t: Type.StructT =>
        val inFields: Vector[in.Field] = structD(t, addrMod)(src)

        val structName = nm.struct(t)
        registerType(in.StructT(structName, inFields, addrMod))

      case Type.FunctionT(_, _) => ???
      case t: Type.InterfaceT =>
        val interfaceName = nm.interface(t)
        registerType(in.InterfaceT(interfaceName, addrMod))

      case Type.InternalTupleT(ts) => in.TupleT(ts.map(t => typeD(t, Addressability.mathDataStructureElement)(src)), addrMod)

      case Type.SortT => in.SortT

      case _ => Violation.violation(s"got unexpected type $t")
    }


    // Identifier

    def idName(id: PIdnNode, context: TypeInfo = info): String = context.regular(id) match {
      case f: st.Function => nm.function(id.name, f.context)
      case m: st.MethodSpec => nm.spec(id.name, m.context)
      case m: st.MethodImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case f: st.FPredicate => nm.function(id.name, f.context)
      case m: st.MPredicateImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case v: st.Variable => nm.variable(id.name, context.scope(id), v.context)
      case sc: st.SingleConstant => nm.global(id.name, sc.context)
      case st.Embbed(_, _, _) | st.Field(_, _, _) => violation(s"expected that fields and embedded field are desugared by using embeddedDeclD resp. fieldDeclD but idName was called with $id")
      case n: st.NamedType => nm.typ(id.name, n.context)
      case _ => ???
    }

    def globalConstD(c: st.Constant)(src: Meta): in.GlobalConst = {
      c match {
        case sc: st.SingleConstant =>
          val typ = typeD(c.context.typ(sc.idDef), Addressability.constant)(src)
          in.GlobalConst.Val(idName(sc.idDef, c.context.getTypeInfo), typ)(src)
        case _ => ???
      }
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

    def freshExclusiveVar(typ: in.Type)(info: Source.Parser.Info): in.LocalVar = {
      require(typ.addressability == Addressability.exclusiveVariable)
      in.LocalVar(nm.fresh, typ)(info)
    }

    def freshSharedVar(typ: in.Type)(info: Source.Parser.Info): in.LocalVar = {
      require(typ.addressability == Addressability.Shared)
      in.LocalVar(nm.fresh, typ)(info)
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

      val src: Meta = meta(id)

      val typ = typeD(info.typ(id), info.addressableVar(id))(meta(id))
      in.LocalVar(idName(id), typ)(src)
    }

    def parameterAsLocalValVar(p: in.Parameter): in.LocalVar = {
      in.LocalVar(p.id, p.typ)(p.info)
    }

    // Miscellaneous

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def inParameterD(p: PParameter, idx: Int): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.In(idName(id), typeD(info.symbType(typ), Addressability.inParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id)))
              (param, local)

            case PUnnamedParameter(typ) =>
              val param = in.Parameter.In(nm.inParam(idx, info.codeRoot(p), info), typeD(info.symbType(typ), Addressability.inParameter)(src))(src)
              val local = None
              (param, local)
          }
      }
    }

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def outParameterD(p: PParameter, idx: Int): (in.Parameter.Out, Option[in.LocalVar]) = {
      val src: Meta = meta(p)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.Out(idName(id), typeD(info.symbType(typ), Addressability.outParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id)))
              (param, local)

            case PUnnamedParameter(typ) =>
              val param = in.Parameter.Out(nm.outParam(idx, info.codeRoot(p), info), typeD(info.symbType(typ), Addressability.outParameter)(src))(src)
              val local = None
              (param, local)
          }
      }
    }

    def receiverD(p: PReceiver): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p)
      p match {
        case PNamedReceiver(id, typ, _) =>
          val param = in.Parameter.In(idName(id), typeD(info.symbType(typ), Addressability.receiver)(src))(src)
          val local = Some(localAlias(localVarContextFreeD(id)))
          (param, local)

        case PUnnamedReceiver(typ) =>
          val param = in.Parameter.In(nm.receiver(info.codeRoot(p), info), typeD(info.symbType(typ), Addressability.receiver)(src))(src)
          val local = None
          (param, local)
      }
    }

    def localAlias(internal: in.LocalVar): in.LocalVar = internal match {
      case in.LocalVar(id, typ) => in.LocalVar(nm.alias(id), typ)(internal.info)
    }

    def structD(struct: StructT, addrMod: Addressability)(src: Meta): Vector[in.Field] =
      struct.clauses.map {
        case (name, (true, typ)) => fieldDeclD((name, typ), Addressability.field(addrMod), struct)(src)
        case (name, (false, typ)) => embeddedDeclD((name, typ), Addressability.field(addrMod), struct)(src)
      }.toVector

    def structMemberD(m: st.StructMember, addrMod: Addressability)(src: Meta): in.Field = m match {
      case st.Field(decl, _, context) => fieldDeclD(decl, addrMod, context)(src)
      case st.Embbed(decl, _, context) => embeddedDeclD(decl, addrMod, context)(src)
    }

    def embeddedDeclD(embedded: (String, Type), fieldAddrMod: Addressability, struct: StructT)(src: Source.Parser.Info): in.Field = {
      val idname = nm.field(embedded._1, struct)
      val td = embeddedTypeD(???, fieldAddrMod)(src) // TODO fix me or embeddedTypeD
      in.Field(idname, td, ghost = false)(src) // TODO: fix ghost attribute
    }

    def embeddedDeclD(decl: PEmbeddedDecl, addrMod: Addressability, context: ExternalTypeInfo)(src: Meta): in.Field =
      in.Field(idName(decl.id, context.getTypeInfo), embeddedTypeD(decl.typ, addrMod)(src), ghost = false)(src) // TODO: fix ghost attribute

    def fieldDeclD(field: (String, Type), fieldAddrMod: Addressability, struct: StructT)(src: Source.Parser.Info): in.Field = {
      val idname = nm.field(field._1, struct)
      val td = typeD(field._2, fieldAddrMod)(src)
      in.Field(idname, td, ghost = false)(src) // TODO: fix ghost attribute
    }

    def fieldDeclD(decl: PFieldDecl, addrMod: Addressability, context: ExternalTypeInfo)(src: Meta): in.Field = {
      val struct = context.struct(decl)
      val field: (String, Type) = (decl.id.name, context.symbType(decl.typ))
      fieldDeclD(field, addrMod, struct.get)(src)
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

      val typ = typeD(info.typ(expr), info.addressability(expr))(src)

      expr match {
        case POld(op) => for {o <- go(op)} yield in.Old(o, typ)(src)
        case PConditional(cond, thn, els) =>  for {
          wcond <- go(cond)
          wthn <- go(thn)
          wels <- go(els)
        } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PForall(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx)(vars, triggers, body)(exprD) }
            yield in.PureForall(newVars, newTriggers, newBody)(src)

        case PExists(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx)(vars, triggers, body)(exprD) }
            yield in.Exists(newVars, newTriggers, newBody)(src)

        case PImplication(left, right) => for {
          wcond <- go(left)
          wthn <- go(right)
          wels = in.BoolLit(b = true)(src)
        } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PTypeOf(exp) => for { wExp <- go(exp) } yield in.TypeOf(wExp)(src)
        case PIsComparable(exp) => underlyingType(info.typOfExprOrType(exp)) match {
          case _: Type.InterfaceT => for { wExp <- exprAndTypeAsExpr(ctx)(exp) } yield in.IsComparableInterface(wExp)(src)
          case Type.SortT => for { wExp <- exprAndTypeAsExpr(ctx)(exp) } yield in.IsComparableType(wExp)(src)
          case t => Violation.violation(s"Expected interface or sort type, but got $t")
        }

        case PCardinality(op) => for {
          dop <- go(op)
        } yield dop.typ match {
          case _: in.SetT | _: in.MultisetT => in.Cardinality(dop)(src)
          case t => violation(s"expected a sequence or (multi)set type, but got $t")
        }

        case PIn(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield dright.typ match {
          case _: in.SequenceT | _: in.SetT => in.Contains(dleft, dright)(src)
          case _: in.MultisetT => in.LessCmp(in.IntLit(0)(src), in.Contains(dleft, dright)(src))(src)
          case t => violation(s"expected a sequence or (multi)set type, but got $t")
        }

        case PMultiplicity(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.Multiplicity(dleft, dright)(src)

        case PRangeSequence(low, high) => for {
          dlow <- go(low)
          dhigh <- go(high)
        } yield in.RangeSequence(dlow, dhigh)(src)

        case PSequenceAppend(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.SequenceAppend(dleft, dright)(src)

        case PSequenceUpdate(seq, clauses) => clauses.foldLeft(go(seq)) {
          case (dseq, clause) => for {
            dleft <- go(clause.left)
            dright <- go(clause.right)
          } yield in.SequenceUpdate(dseq.res, dleft, dright)(src)
        }

        case PSequenceConversion(op) => for {
          dop <- go(op)
        } yield dop.typ match {
          case _: in.SequenceT => dop
          case _: in.ArrayT => in.SequenceConversion(dop)(src)
          case _: in.OptionT => in.SequenceConversion(dop)(src)
          case t => violation(s"expected a sequence, array or option type, but got $t")
        }

        case PSetConversion(op) => for {
          dop <- go(op)
        } yield dop.typ match {
          case _: in.SetT => dop
          case _: in.SequenceT => in.SetConversion(dop)(src)
          case _: in.OptionT => in.SetConversion(in.SequenceConversion(dop)(src))(src)
          case t => violation(s"expected a sequence, set or option type, but found $t")
        }

        case PUnion(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.Union(dleft, dright)(src)

        case PIntersection(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.Intersection(dleft, dright)(src)

        case PSetMinus(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.SetMinus(dleft, dright)(src)

        case PSubset(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield in.Subset(dleft, dright)(src)

        case PMultisetConversion(op) => for {
          dop <- go(op)
        } yield dop.typ match {
          case _: in.MultisetT => dop
          case _: in.SequenceT => in.MultisetConversion(dop)(src)
          case _: in.OptionT => in.MultisetConversion(in.SequenceConversion(dop)(src))(src)
          case t => violation(s"expected a sequence, multiset or option type, but found $t")
        }

        case POptionNone(t) => {
          val dt = typeD(info.symbType(t), Addressability.rValue)(src)
          unit(in.OptionNone(dt)(src))
        }

        case POptionSome(op) => for {
          dop <- go(op)
        } yield in.OptionSome(dop)(src)

        case POptionGet(op) => for {
          dop <- go(op)
        } yield in.OptionGet(dop)(src)

        case _ => Violation.violation(s"cannot desugar expression to an internal expression, $expr")
      }
    }

    /**
      * Desugars a quantifier-like structure: a sequence `vars` of variable declarations,
      * together with a sequence `triggers` of triggers and a quantifier `body`.
      * @param ctx A function context consisting of variable substitutions.
      * @param vars The sequence of variable (declarations) bound by the quantifier.
      * @param triggers The sequence of triggers for the quantifier.
      * @param body The quantifier body.
      * @param go The desugarer for `body`, for example `exprD` or `assertionD`.
      * @tparam T The type of the desugared quantifier body (e.g., expression, or assertion).
      * @return The desugared versions of `vars`, `triggers` and `body`.
      */
    def quantifierD[T](ctx: FunctionContext)
                      (vars: Vector[PBoundVariable], triggers: Vector[PTrigger], body: PExpression)
                      (go : FunctionContext => PExpression => Writer[T])
        : Writer[(Vector[in.BoundVar], Vector[in.Trigger], T)] = {
      val newVars = vars map boundVariableD

      // substitution has to be added since otherwise all bound variables are translated to addressable variables
      val bodyCtx = ctx.copy
      (vars zip newVars).foreach { case (a, b) => bodyCtx.addSubst(a.id, b) }

      for {
        newTriggers <- sequence(triggers map triggerD(bodyCtx))
        newBody <- go(bodyCtx)(body)
      } yield (newVars, newTriggers, newBody)
    }

    def boundVariableD(x: PBoundVariable) : in.BoundVar =
      in.BoundVar(idName(x.id), typeD(info.symbType(x.typ), Addressability.boundVariable)(meta(x)))(meta(x))

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

        case n: PAccess => for {e <- accessibleD(ctx)(n.exp); p <- permissionD(ctx)(n.perm)} yield in.Access(e, p)(src)
        case n: PPredicateAccess => predicateCallD(ctx)(n.pred, n.perm)

        case n: PInvoke =>
          // a predicate invocation corresponds to a predicate access with full permissions
          // register the full permission AST node in the position manager such that its meta information
          // is retrievable in predicateCallD
          val perm = PFullPerm()
          pom.positions.dupPos(n, perm)
          predicateCallD(ctx)(n, perm)

        case PForall(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx)(vars, triggers, body)(assertionD) }
            yield newBody match {
              case in.ExprAssertion(exprBody) =>
                in.ExprAssertion(in.PureForall(newVars, newTriggers, exprBody)(src))(src)
              case _ => in.SepForall(newVars, newTriggers, newBody)(src)
            }

        case _ => exprD(ctx)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallD(ctx: FunctionContext)(n: PInvoke, perm: PPermission): Writer[in.Assertion] = {

      val src: Meta = meta(n)

      info.resolve(n) match {
        case Some(p: ap.PredicateCall) =>
          for {
            predAcc <- predicateCallAccD(ctx)(p)(src)
            p <- permissionD(ctx)(perm)
          } yield in.Access(in.Accessible.Predicate(predAcc), p)(src)

        case _ => exprD(ctx)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallAccD(ctx: FunctionContext)(p: ap.PredicateCall)(src: Meta): Writer[in.PredicateAccess] = {

      val dArgs = p.args map pureExprD(ctx)

      p.predicate match {
        case b: ap.Predicate =>
          val fproxy = fpredicateProxyD(b.id)
          unit(in.FPredicateAccess(fproxy, dArgs)(src))

        case b: ap.ReceivedPredicate =>
          val dRecv = pureExprD(ctx)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val proxy = mpredicateProxy(b.id)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs)(src))

        case b: ap.PredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val proxy = mpredicateProxy(b.id)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs.tail)(src))
      }
    }



    def accessibleD(ctx: FunctionContext)(acc: PExpression): Writer[in.Accessible] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(acc)

      info.resolve(acc) match {
        case Some(p: ap.PredicateCall) =>
          predicateCallAccD(ctx)(p)(src) map (x => in.Accessible.Predicate(x))

        case _ =>
          val argT = info.typ(acc)
          underlyingType(argT) match {
            case Single(ut: Type.PointerT) =>
              // [[in.Accessible.Address]] represents '&'.
              // If there is no outermost '&', then adds '&*'.
              acc match {
                case PReference(op) => addressableD(ctx)(op) map (x => in.Accessible.Address(x.op))
                case _ =>
                  goE(acc) map (x => in.Accessible.Address(in.Deref(x, typeD(ut.elem, Addressability.dereference)(src))(src)))
              }

            case _ => Violation.violation(s"expected pointer type, but got $argT")
          }
      }

    }

    def permissionD(ctx: FunctionContext)(perm: PPermission): Writer[in.Permission] = {
      val src: Meta = meta(perm)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      perm match {
        case PFullPerm() => unit(in.FullPerm(src))
        case PNoPerm() => unit(in.NoPerm(src))
        case PFractionalPerm(left, right) => for { l <- goE(left); r <- goE(right) } yield in.FractionalPerm(l, r)(src)
        case PWildcardPerm() => unit(in.WildcardPerm(src))
      }
    }

    def triggerD(ctx: FunctionContext)(trigger: PTrigger) : Writer[in.Trigger] = {
      val src: Meta = meta(trigger)
      for { exprs <- sequence(trigger.exps map exprD(ctx)) } yield in.Trigger(exprs)(src)
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

  /**
    * The NameManager returns unique names for various entities.
    * It adheres to the following naming conventions:
    * - variables, receiver, in-, and output parameter include scope counter in their names
    * - the above mentioned entities and all others except structs include the package name in which they are declared
    * - structs are differently handled as the struct type does not consist of a name. Hence, we include the positional
    *   information of the struct in its name. This positional information consists of the file name, line nr and
    *   column of the start of the struct declaration.
    * As a result, the desugared name of all entities that can be accesses from outside of a package does not depend on
    * any counters and/or maps but can be computed based on the package name and/or positional information of the
    * entity's declaration.
    * This is key to desugar packages in isolation without knowing the desugarer instance and/or name manager of each
    * imported package.
    */
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
    private val INTERFACE_PREFIX = "Y"
    private val GLOBAL_PREFIX = "G"

    private var counter = 0

    private var scopeCounter = 0
    private var scopeMap: Map[PScope, Int] = Map.empty

    private def maybeRegister(s: PScope): Unit = {
      if (!(scopeMap contains s)) {
        scopeMap += (s -> scopeCounter)
        scopeCounter += 1
      }
    }

    private def name(postfix: String)(n: String, s: PScope, context: ExternalTypeInfo): String = {
      maybeRegister(s)
      // n has occur first in order that function inverse properly works
      s"${n}_${context.pkgName}_$postfix${scopeMap(s)}" // deterministic
    }

    private def nameWithoutScope(postfix: String)(n: String, context: ExternalTypeInfo): String = {
      // n has occur first in order that function inverse properly works
      s"${n}_${context.pkgName}_$postfix" // deterministic
    }

    def variable(n: String, s: PScope, context: ExternalTypeInfo): String = name(VARIABLE_PREFIX)(n, s, context)
    def global  (n: String, context: ExternalTypeInfo): String = nameWithoutScope(GLOBAL_PREFIX)(n, context)
    def typ     (n: String, context: ExternalTypeInfo): String = nameWithoutScope(TYPE_PREFIX)(n, context)
    def field   (n: String, s: StructT): String = nameWithoutScope(s"$FIELD_PREFIX${struct(s)}")(n, s.context)
    def function(n: String, context: ExternalTypeInfo): String = nameWithoutScope(FUNCTION_PREFIX)(n, context)
    def spec    (n: String, context: ExternalTypeInfo): String = nameWithoutScope(METHODSPEC_PREFIX)(n, context)
    def method  (n: String, t: PMethodRecvType, context: ExternalTypeInfo): String = t match {
      case PMethodReceiveName(typ)    => nameWithoutScope(s"$METHOD_PREFIX${typ.name}")(n, context)
      case PMethodReceivePointer(typ) => nameWithoutScope(s"P$METHOD_PREFIX${typ.name}")(n, context)
    }

    def inverse(n: String): String = n.substring(0, n.indexOf('_'))

    def alias(n: String): String = s"${n}_$COPY_PREFIX$fresh"

    def fresh: String = {
      val f = FRESH_PREFIX + counter
      counter += 1
      f
    }

    def inParam(idx: Int, s: PScope, context: ExternalTypeInfo): String = name(IN_PARAMETER_PREFIX)("P" + idx, s, context)
    def outParam(idx: Int, s: PScope, context: ExternalTypeInfo): String = name(OUT_PARAMETER_PREFIX)("P" + idx, s, context)
    def receiver(s: PScope, context: ExternalTypeInfo): String = name(RECEIVER_PREFIX)("R", s, context)

    def struct(s: StructT): String = {
      // we assume that structs are uniquely identified by the SourcePosition at which they were declared:
      val pom = s.context.getTypeInfo.tree.originalRoot.positions
      val start = pom.positions.getStart(s.decl).get
      val finish = pom.positions.getFinish(s.decl).get
      val pos = pom.translate(start, finish)
      // replace characters that could be misinterpreted:
      val structName = pos.toString.replace(".", "$")
      s"$STRUCT_PREFIX$$$structName"
    }

    def interface(s: InterfaceT): String = {
      if (s.isEmpty) {
        Names.emptyInterface
      } else {
        Violation.violation("non-empty interfaces are not supported right now")
      }
    }
  }
}


