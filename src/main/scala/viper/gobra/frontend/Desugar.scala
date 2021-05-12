// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.ast.frontend.{PExpression, AstPattern => ap, _}
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{BuiltInMemberTag, Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.frontend.info.base.BuiltInMemberTag._
import viper.gobra.frontend.info.{ExternalTypeInfo, TypeInfo}
import viper.gobra.reporting.Source.AutoImplProofAnnotation
import viper.gobra.reporting.{DesugaredMessage, Source}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.util.Violation.violation
import viper.gobra.util.{DesugarWriter, Violation}

import scala.annotation.{tailrec, unused}
import scala.collection.Iterable
import scala.reflect.ClassTag

object Desugar {

  def desugar(pkg: PPackage, info: viper.gobra.frontend.info.TypeInfo)(config: Config): in.Program = {
    // independently desugar each imported package. Only used members (i.e. members for which `isUsed` returns true will be desugared:
    val importedPrograms = info.context.getContexts map { tI => {
      val typeInfo: TypeInfo = tI.getTypeInfo
      val importedPackage = typeInfo.tree.originalRoot
      val d = new Desugarer(importedPackage.positions, typeInfo)(config)
      (d, d.packageD(importedPackage))
    }}
    // desugar the main package, i.e. the package on which verification is performed:
    val mainDesugarer = new Desugarer(pkg.positions, info)(config)
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
    val (table, builtInMembers) = combineLookupTable(mainDesugarer +: importedDesugarers.toSeq)
    in.Program(types, builtInMembers.toVector ++ members, table)(mainProgram.info)
  }

  /**
    * Combines the lookup tables of multiple desugarers incl. taking built-in members into account.
    * As a by-product, all built-in members that are used in all desugarers will be returned.
    */
  private def combineLookupTable(desugarers: Iterable[Desugarer]): (in.LookupTable, Iterable[in.Member]) = {
    // note that the same built-in member might exist in multiple tables. Thus, we need to deduplicate.
    // however, a sanity check is performed that filtered built-in members are actually equal
    def combineTableFieldForBuiltInMember[M <: in.BuiltInMember : ClassTag,P <: in.Proxy](getProxy: M => P): Map[P, M] = {
      // apply `f` to every desugarer and filter the resulting maps to only contain values of type `M`
      val res: Iterable[Map[_, M]] = desugarers.map(_.builtInMembers).map(memberMap => memberMap.collect {
        case (tag, m: M) => (tag, m) // note that checking for type `M` here works only because of `: ClassTag`
      })
      // combine maps and group by key to get a list of values per key:
      val duplicateMap = res.flatMap(_.toSeq).groupMap(_._1)(_._2)
      // sanity check that all values for a particular key are equal:
      val deduplicatedMap = duplicateMap map {
        // the first case should never occur since every key has to have at least one value coming from any input map
        case (key, values) if values.isEmpty => violation(s"found a built-in member key ($key) for which no member exists")
        case (key, values) =>
          val set = values.toSet // deduplicate by converting values to a set
          violation(set.size == 1, s"built-in member is not unique, found ${set.mkString(",")} for key $key")
          val member: M = set.head
          (getProxy(member), member) // use proxy as key
      }
      deduplicatedMap
    }

    def combineTableField[X,Y](f: Desugarer => Map[X,Y]): Map[X,Y] =
      desugarers.flatMap(f).toMap

    val builtInMethods = combineTableFieldForBuiltInMember((m: in.BuiltInMethod) => m.name)
    val builtInFunctions = combineTableFieldForBuiltInMember((m: in.BuiltInFunction) => m.name)
    val builtInMPredicates = combineTableFieldForBuiltInMember((m: in.BuiltInMPredicate) => m.name)
    val builtInFPredicates = combineTableFieldForBuiltInMember((m: in.BuiltInFPredicate) => m.name)

    val combinedDefinedT = combineTableField(_.definedTypes)
    val combinedMethods = combineTableField(_.definedMethods)
    val combinedMPredicates = combineTableField(_.definedMPredicates)
    val combinedImplementations = combineTableField(_.interfaceImplementations)
    val combinedMemberProxies = computeMemberProxies(combinedMethods.values ++ combinedMPredicates.values, combinedImplementations, combinedDefinedT)
    val combineImpProofPredAliases = combineTableField(_.implementationProofPredicateAliases)
    val table = new in.LookupTable(
      combinedDefinedT,
      combinedMethods ++ builtInMethods,
      combineTableField(_.definedFunctions) ++ builtInFunctions,
      combinedMPredicates ++ builtInMPredicates,
      combineTableField(_.definedFPredicates) ++ builtInFPredicates      ,
      combinedMemberProxies,
      combinedImplementations,
      combineImpProofPredAliases
      )
    val builtInMembers = builtInMethods.values ++ builtInFunctions.values ++ builtInMPredicates.values ++ builtInFPredicates.values
    (table, builtInMembers)
  }

  /** For now, the memberset is computed in an inefficient way. */
  def computeMemberProxies(decls: Iterable[in.Member], itfImpls: Map[in.InterfaceT, Set[in.Type]], definedT: Map[(String, Addressability), in.Type]): Map[in.Type, Set[in.MemberProxy]] = {
    val keys = itfImpls.flatMap{ case (k, v) => v + k }.toSet
    val pairs: Set[(in.Type, Set[in.MemberProxy])] = keys.map{ key =>

      val underlyingRecv = {
        @tailrec
        def underlyingItf(t: in.Type): Option[in.InterfaceT] = {
          t match {
            case t: in.InterfaceT => Some(t)
            case t: in.DefinedT =>
              definedT.get(t.name, t.addressability) match {
                case Some(ut) => underlyingItf(ut)
                case None => None
              }
            case _ => None
          }
        }

        underlyingItf(key).getOrElse(key)
      }

      key -> decls.collect{
        case m: in.Method if m.receiver.typ == underlyingRecv => m.name
        case m: in.PureMethod if m.receiver.typ == underlyingRecv => m.name
        case m: in.MPredicate if m.receiver.typ == underlyingRecv => m.name
      }.toSet
    }
    pairs.toMap[in.Type, Set[in.MemberProxy]]
  }

  object NoGhost {
    def unapply(arg: PNode): Some[PNode] = arg match {
      case PGhostifier(x) => Some(x)
      case x => Some(x)
    }
  }

  private class Desugarer(pom: PositionManager, info: viper.gobra.frontend.info.TypeInfo)(config: Config) {

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

    def functionProxyD(decl: PFunctionDecl, context: TypeInfo): in.FunctionProxy = {
      val name = idName(decl.id, context)
      in.FunctionProxy(name)(meta(decl, context))
    }

    def functionProxy(id: PIdnUse, context: TypeInfo): in.FunctionProxy = {
      val name = idName(id, context)
      in.FunctionProxy(name)(meta(id, context))
    }

    def domainFunctionProxy(symb: st.DomainFunction): in.DomainFuncProxy = {
      val domainName = nm.domain(Type.DomainT(symb.domain, symb.context))
      val functionName = idName(symb.decl.id, symb.context.getTypeInfo)
      in.DomainFuncProxy(functionName, domainName)(meta(symb.decl.id, symb.context.getTypeInfo))
    }

    def methodProxyD(decl: PMethodDecl, context: TypeInfo): in.MethodProxy = {
      val name = idName(decl.id, context)
      in.MethodProxy(decl.id.name, name)(meta(decl, context))
    }

    def methodProxyD(decl: PMethodSig, context: TypeInfo): in.MethodProxy = {
      val name = idName(decl.id, context)
      in.MethodProxy(decl.id.name, name)(meta(decl, context))
    }

    def methodProxyFromSymb(symb: st.Method): in.MethodProxy = {
      symb match {
        case symb: st.MethodImpl => in.MethodProxy(symb.decl.id.name, idName(symb.decl.id, symb.context.getTypeInfo))(meta(symb.decl, symb.context.getTypeInfo))
        case symb: st.MethodSpec => in.MethodProxy(symb.spec.id.name, idName(symb.spec.id, symb.context.getTypeInfo))(meta(symb.spec, symb.context.getTypeInfo))
      }
    }

    def methodProxy(id: PIdnUse, context: TypeInfo): in.MethodProxy = {
      val name = idName(id, context)
      in.MethodProxy(id.name, name)(meta(id, context))
    }

    def fpredicateProxyD(decl: PFPredicateDecl, context: TypeInfo): in.FPredicateProxy = {
      val name = idName(decl.id, context)
      in.FPredicateProxy(name)(meta(decl, context))
    }

    def fpredicateProxy(id: PIdnUse, context: TypeInfo): in.FPredicateProxy = {
      val name = idName(id, context)
      in.FPredicateProxy(name)(meta(id, context))
    }

    def mpredicateProxyD(decl: PMPredicateDecl, context: TypeInfo): in.MPredicateProxy = {
      val name = idName(decl.id, context)
      in.MPredicateProxy(decl.id.name, name)(meta(decl, context))
    }

    def mpredicateProxyD(decl: PMPredicateSig, context: TypeInfo): in.MPredicateProxy = {
      val name = idName(decl.id, context)
      in.MPredicateProxy(decl.id.name, name)(meta(decl, context))
    }

    def mpredicateProxyD(id: PIdnUse, context: TypeInfo): in.MPredicateProxy = {
      val name = idName(id, context)
      in.MPredicateProxy(id.name, name)(meta(id, context))
    }


    // proxies to built-in members
    def methodProxy(tag: BuiltInMethodTag, recv: in.Type, args: Vector[in.Type])(src: Meta): in.MethodProxy = {
      def create(tag: BuiltInMethodTag, inRecvWithArgs: Vector[in.Type]): in.BuiltInMethod = {
        violation(inRecvWithArgs.nonEmpty, "receiver has to be among args")
        val inRecv = inRecvWithArgs.head
        val inArgs = inRecvWithArgs.tail
        val proxy = in.MethodProxy(tag.identifier, nm.builtInMember(tag, Vector(inRecv)))(src)
        in.BuiltInMethod(inRecv, tag, proxy, inArgs)(src)
      }

      val inRecv = recv.withAddressability(Addressability.inParameter)
      val inArgs = args.map(_.withAddressability(Addressability.inParameter))
      memberProxy(tag, inRecv +: inArgs, create).name
    }

    def functionProxy(tag: BuiltInFunctionTag, args: Vector[in.Type])(src: Meta): in.FunctionProxy = {
      def create(tag: BuiltInFunctionTag, inArgs: Vector[in.Type]): in.BuiltInFunction = {
        val proxy = in.FunctionProxy(nm.builtInMember(tag, inArgs))(src)
        in.BuiltInFunction(tag, proxy, inArgs)(src)
      }

      val inArgs = args.map(_.withAddressability(Addressability.inParameter))
      memberProxy(tag, inArgs, create).name
    }

    def fpredicateProxy(tag: BuiltInFPredicateTag, args: Vector[in.Type])(src: Meta): in.FPredicateProxy = {
      def create(tag: BuiltInFPredicateTag, inArgs: Vector[in.Type]): in.BuiltInFPredicate = {
        val proxy = in.FPredicateProxy(nm.builtInMember(tag, inArgs))(src)
        in.BuiltInFPredicate(tag, proxy, inArgs)(src)
      }

      val inArgs = args.map(_.withAddressability(Addressability.inParameter))
      memberProxy(tag, inArgs, create).name
    }

    def mpredicateProxy(tag: BuiltInMPredicateTag, recv: in.Type, args: Vector[in.Type])(src: Meta): in.MPredicateProxy = {
      def create(tag: BuiltInMPredicateTag, inRecvWithArgs: Vector[in.Type]): in.BuiltInMPredicate = {
        violation(inRecvWithArgs.nonEmpty, "receiver has to be among args")
        val inRecv = inRecvWithArgs.head
        val inArgs = inRecvWithArgs.tail
        val proxy = in.MPredicateProxy(tag.identifier, nm.builtInMember(tag, Vector(inRecv)))(src)
        in.BuiltInMPredicate(inRecv, tag, proxy, inArgs)(src)
      }

      val inRecv = recv.withAddressability(Addressability.inParameter)
      val inArgs = args.map(_.withAddressability(Addressability.inParameter))
      memberProxy(tag, inRecv +: inArgs, create).name
    }


    var builtInMembers: Map[(BuiltInMemberTag, Vector[in.Type]), in.BuiltInMember] = Map.empty
    /** createMember function is only allowed to depend on its arguments, otherwise caching of members and subsequently generating members are unsound */
    def memberProxy[T <: BuiltInMemberTag, M <: in.BuiltInMember](tag: T, args: Vector[in.Type], createMember: (T, Vector[in.Type]) => M): M = {
      def genAndStore(): M = {
        val member = createMember(tag, args)
        builtInMembers += (tag, args) -> member
        member
      }

      val filteredMembers: Map[(T, Vector[in.Type]), M] = builtInMembers.collect {
        case ((t: T@unchecked, as), m: M@unchecked) => (t, as) -> m
      }
      filteredMembers.getOrElse((tag, args), genAndStore())
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
        case x: PMPredicateDecl => Vector(registerMPredicate(x))
        case x: PFPredicateDecl => Vector(registerFPredicate(x))
        case x: PImplementationProof => registerImplementationProof(x); Vector.empty
        case _ => Vector.empty
      }

      consideredDecls.foreach {
        case NoGhost(x: PTypeDef) => desugarAllTypeDefVariants(x)
        case _ =>
      }

      val additionalMembers = AdditionalMembers.finalizedMembers ++ missingImplProofs

      // built-in members are not (yet) added to the program's members or the lookup table
      // instead, they remain only accessible via this desugarer's getter function.
      // The `combine` function will treat all built-in members across packages (i.e. desugarers) and update the
      // program's members and lookup table accordingly
      val table = new in.LookupTable(
        definedTypes,
        definedMethods,
        definedFunctions,
        definedMPredicates,
        definedFPredicates,
        computeMemberProxies(dMembers ++ additionalMembers, interfaceImplementations, definedTypes),
        interfaceImplementations,
        implementationProofPredicateAliases
      )

      in.Program(types.toVector, dMembers ++ additionalMembers, table)(meta(p))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = ???

    def constDeclD(decl: PConstDecl): Vector[in.GlobalConstDecl] = decl.left.flatMap(l => info.regular(l) match {
      case sc@st.SingleConstant(_, id, _, _, _, _) =>
        val src = meta(id)
        val gVar = globalConstD(sc)(src)
        val lit: in.Lit = gVar.typ match {
          case in.BoolT(Addressability.Exclusive) =>
            val constValue = sc.context.boolConstantEvaluation(sc.exp)
            in.BoolLit(constValue.get)(src)
          case in.StringT(Addressability.Exclusive) =>
            val constValue = sc.context.stringConstantEvaluation(sc.exp)
            in.StringLit(constValue.get)(src)
          case x if underlyingType(x).isInstanceOf[in.IntT] && x.addressability == Addressability.Exclusive =>
            val constValue = sc.context.intConstantEvaluation(sc.exp)
            in.IntLit(constValue.get)(src)
          case _ => ???
        }
        Vector(in.GlobalConstDecl(gVar, lit)(src))

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

      val name = functionProxyD(decl, info)
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
        val varsInit = vars map (v => in.Initialization(v)(v.info))
        val body = varsInit ++ argInits ++ Vector(blockD(ctx)(s)) ++ resultAssignments
        in.Block(vars, body)(meta(s))
      }

      in.Function(name, args, returns, pres, posts, bodyOpt)(fsrc)
    }

    def pureFunctionD(decl: PFunctionDecl): in.PureFunction = {
      require(decl.spec.isPure)

      val name = functionProxyD(decl, info)
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
          val res = b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case b => Violation.violation(s"unexpected pure function body: $b")
          }
          implicitConversion(res.typ, returns.head.typ, res)
      }

      in.PureFunction(name, args, returns, pres, posts, bodyOpt)(fsrc)
    }



    def methodD(decl: PMethodDecl): in.MethodMember =
      if (decl.spec.isPure) pureMethodD(decl) else {

      val name = methodProxyD(decl, info)
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
        val varsInit = vars map (v => in.Initialization(v)(v.info))
        val body = varsInit ++ recvInits ++ argInits ++ Vector(blockD(ctx)(s)) ++ resultAssignments
        in.Block(vars, body)(meta(s))
      }

      in.Method(recv, name, args, returns, pres, posts, bodyOpt)(fsrc)
    }

    def pureMethodD(decl: PMethodDecl): in.PureMethod = {
      require(decl.spec.isPure)

      val name = methodProxyD(decl, info)
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
          val res = b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
            case s => Violation.violation(s"unexpected pure function body: $s")
          }
          implicitConversion(res.typ, returns.head.typ, res)
      }

      in.PureMethod(recv, name, args, returns, pres, posts, bodyOpt)(fsrc)
    }

    def fpredicateD(decl: PFPredicateDecl): in.FPredicate = {
      val name = fpredicateProxyD(decl, info)
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
      val name = mpredicateProxyD(decl, info)
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
      val dStatements = sequence(block.nonEmptyStmts map (s => seqn(stmtD(ctx)(s))))
      blockV(dStatements)(meta(block))
    }

    def stmtD(ctx: FunctionContext)(stmt: PStatement): Writer[in.Stmt] = {

      def goS(s: PStatement): Writer[in.Stmt] = stmtD(ctx)(s)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)
      def goL(a: PAssignee): Writer[in.Assignee] = assigneeD(ctx)(a)

      val src: Meta = meta(stmt)

      /**
        * Desugars the left side of an assignment, short variable declaration, and normal variable declaration.
        * If the left side is an identifier definition, a variable declaration and initialization is written, as well.
        */
      def leftOfAssignmentD(idn: PIdnNode)(t: in.Type): Writer[in.AssignableVar] = {
        val isDef = idn match {
          case _: PIdnDef => true
          case unk: PIdnUnk if info.isDef(unk) => true
          case _ => false
        }

        idn match {
          case _: PWildcard =>
            freshDeclaredExclusiveVar(t.withAddressability(Addressability.Exclusive))(src)

          case _ =>
            val x = assignableVarD(ctx)(idn)
            if (isDef) {
              val v = x.asInstanceOf[in.LocalVar]
              for {
                _ <- declare(v)
                _ <- write(in.Initialization(v)(src))
              } yield v
            } else unit(x)
        }
      }

      stmt match {
        case NoGhost(noGhost) => noGhost match {
          case _: PEmptyStmt => unit(in.Seqn(Vector.empty)(src))

          case s: PSeq => for {ss <- sequence(s.nonEmptyStmts map goS)} yield in.Seqn(ss)(src)

          case b: PBlock => unit(blockD(ctx)(b))

          case l: PLabeledStmt =>
            val proxy = labelProxy(l.label)
            for {
              _ <- declare(proxy)
              _ <- write(in.Label(proxy)(src))
              s <- goS(l.stmt)
            } yield s

          case PIfStmt(ifs, els) =>
            val elsStmt = maybeStmtD(ctx)(els)(src)
            unit(block( // is a block because 'pre' might define new variables
              ifs.foldRight(elsStmt){
                case (PIfClause(pre, cond, body), c) =>
                  for {
                    dPre <- maybeStmtD(ctx)(pre)(src)
                    dCond <- exprD(ctx)(cond)
                    dBody = blockD(ctx)(body)
                    els <- seqn(c)
                  } yield in.Seqn(Vector(dPre, in.If(dCond, dBody, els)(src)))(src)
              }
            ))

          case PForStmt(pre, cond, post, spec, body) =>
            unit(block( // is a block because 'pre' might define new variables
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
              } yield wh
            ))

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
              seqn(sequence((left zip right).map{ case (l, r) =>
                for {
                  re <- goE(r)
                  dL <- leftOfAssignmentD(l)(re.typ)
                  le = in.Assignee.Var(dL)
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src)))
            } else if (right.size == 1) {
              seqn(for {
                re  <- goE(right.head)
                les <- sequence(left.map{ l =>
                  for {
                    dL <- leftOfAssignmentD(l)(typeD(info.typ(l), Addressability.exclusiveVariable)(src))
                  } yield in.Assignee.Var(dL)
                })
              } yield multiassD(les, re)(src))
            } else { violation("invalid assignment") }

          case PVarDecl(typOpt, right, left, _) =>

            if (left.size == right.size) {
              seqn(sequence((left zip right).map{ case (l, r) =>
                for {
                  re <- goE(r)
                  typ = typOpt.map(x => typeD(info.symbType(x), Addressability.exclusiveVariable)(src)).getOrElse(re.typ)
                  dL <- leftOfAssignmentD(l)(typ)
                  le <- unit(in.Assignee.Var(dL))
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src)))
            } else if (right.size == 1) {
              seqn(for {
                re  <- goE(right.head)
                les <- sequence(left.map{l =>
                  for {
                    dL <- leftOfAssignmentD(l)(re.typ)
                  } yield in.Assignee.Var(dL)
                })
              } yield multiassD(les, re)(src))
            } else if (right.isEmpty && typOpt.nonEmpty) {
              val typ = typeD(info.symbType(typOpt.get), Addressability.exclusiveVariable)(src)
              val lelems = sequence(left.map{ l =>
                for {
                  dL <- leftOfAssignmentD(l)(typ)
                } yield in.Assignee.Var(dL)
              })
              val relems = left.map{ l => in.DfltVal(typeD(info.symbType(typOpt.get), Addressability.defaultValue)(meta(l)))(meta(l)) }
              seqn(lelems.map{ lelemsV =>
                in.Seqn((lelemsV zip relems).map{
                  case (l, r) => singleAss(l, r)(src)
                })(src)
              })
            } else { violation("invalid declaration") }

          case PReturn(exps) =>
            for{es <- sequence(exps map goE)} yield ctx.ret(es)(src)

          case g: PGhostStatement => ghostStmtD(ctx)(g)

          case PExprSwitchStmt(pre, exp, cases, dflt) =>
            for {
              dPre <- maybeStmtD(ctx)(pre)(src)
              dExp <- exprD(ctx)(exp)
              exprVar <- freshDeclaredExclusiveVar(dExp.typ.withAddressability(Addressability.exclusiveVariable))(dExp.info)
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
                // TODO: the whole thing should just be desugared as a call. The code below has multiple bugs.
                case Some(p: ap.FunctionCall) => p.callee match {
                  case ap.Function(_, st.Function(decl, _, context)) =>
                    val func = functionProxyD(decl, context.getTypeInfo)
                    for { args <- getArgs(decl.args.length, p.args) } yield in.GoFunctionCall(func, args)(src)

                  case ap.ReceivedMethod(recv, _, _, st.MethodImpl(decl, _, context)) =>
                    val meth = methodProxyD(decl, context.getTypeInfo)
                    for {
                      args <- getArgs(decl.args.length, p.args)
                      recvIn <- goE(recv)
                    } yield in.GoMethodCall(recvIn, meth, args)(src)

                  case ap.MethodExpr(_, _, _, st.MethodImpl(decl, _, context)) =>
                    val meth = methodProxyD(decl, context.getTypeInfo)
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

          case PSendStmt(channel, msg) =>
            for {
              dchannel <- goE(channel)
              dmsg <- goE(msg)
              sendChannelProxy = mpredicateProxy(BuiltInMemberTag.SendChannelMPredTag, dchannel.typ, Vector())(src)
              sendGivenPermProxy = methodProxy(BuiltInMemberTag.SendGivenPermMethodTag, dchannel.typ, Vector())(src)
              sendGotPermProxy = methodProxy(BuiltInMemberTag.SendGotPermMethodTag, dchannel.typ, Vector())(src)
            } yield in.Send(dchannel, dmsg, sendChannelProxy, sendGivenPermProxy, sendGotPermProxy)(src)

          case _ => ???
        }
      }
    }

    def switchCaseD(switchCase: PExprSwitchCase, scrutinee: in.LocalVar)(ctx: FunctionContext): Writer[(in.Expr, in.Stmt)] =
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
            Vector( // declare for the fresh variables is not necessary because they are put into a block
              in.SafeTypeAssertion(resTarget, successTarget, n.exp, n.arg)(n.info),
              singleAss(lefts(0), resTarget)(src),
              singleAss(lefts(1), successTarget)(src)
            )
          )(src)

        case n: in.Receive if lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable))(src)
          val successTarget = freshExclusiveVar(in.BoolT(Addressability.exclusiveVariable))(src)
          val recvChannelProxy = n.recvChannel
          val recvGivenPermProxy = n.recvGivenPerm
          val recvGotPermProxy = n.recvGotPerm
          val closedProxy = mpredicateProxy(BuiltInMemberTag.ClosedMPredTag, n.channel.typ, Vector())(src)
          in.Block(
            Vector(resTarget, successTarget),
            Vector( // declare for the fresh variables is not necessary because they are put into a block
              in.SafeReceive(resTarget, successTarget, n.channel, recvChannelProxy, recvGivenPermProxy, recvGotPermProxy, closedProxy)(n.info),
              singleAss(lefts(0), resTarget)(src),
              singleAss(lefts(1), successTarget)(src)
            )
          )(src)

        case l@ in.IndexedExp(base, _) if base.typ.isInstanceOf[in.MapT] && lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable))(src)
          val successTarget = freshExclusiveVar(in.BoolT(Addressability.exclusiveVariable))(src)
          in.Block(
            Vector(resTarget, successTarget),
            Vector(
              in.SafeMapLookup(resTarget, successTarget, l)(l.info),
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
      def getBuiltInFuncType(f: ap.BuiltInFunctionKind): FunctionT = {
        val abstractType = f.symb.tag.typ(config)
        val argsForTyping = f match {
          case _: ap.BuiltInFunction =>
            p.args.map(info.typ)
          case base: ap.BuiltInReceivedMethod =>
            Vector(info.typ(base.recv))
          case base: ap.BuiltInMethodExpr =>
            Vector(info.symbType(base.typ))
        }
        Violation.violation(abstractType.typing.isDefinedAt(argsForTyping), s"cannot type built-in member ${f.symb.tag} as it is not defined for arguments $argsForTyping")
        abstractType.typing(argsForTyping)
      }

      def getFunctionProxy(f: ap.FunctionKind, args: Vector[in.Expr]): in.FunctionProxy = f match {
        case ap.Function(id, _) => functionProxy(id, info)
        case ap.BuiltInFunction(_, symb) => functionProxy(symb.tag, args.map(_.typ))(src)
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }

      def getMethodProxy(f: ap.FunctionKind, recv: in.Expr, args: Vector[in.Expr]): in.MethodProxy = f match {
        case ap.ReceivedMethod(_, id, _, _) => methodProxy(id, info)
        case ap.MethodExpr(_, id, _, _) => methodProxy(id, info)
        case bm: ap.BuiltInMethodKind => methodProxy(bm.symb.tag, recv.typ, args.map(_.typ))(src)
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }

      def convertArgs(args: Vector[in.Expr]): Vector[in.Expr] = {
        // implicitly convert arguments:
        p.callee match {
          // `BuiltInFunctionKind` has to be checked first since it implements `Symbolic` as well
          case f: ap.BuiltInFunctionKind => arguments(getBuiltInFuncType(f), args)
          case base: ap.Symbolic => base.symb match {
            case fsym: st.WithArguments => arguments(fsym, args)
            case c => Violation.violation(s"This case should be unreachable, but got $c")
          }
        }
      }

      // encode arguments
      val dArgs = {
        val params: Vector[Type] = p.callee match {
          // `BuiltInFunctionKind` has to be checked first since it implements `Symbolic` as well
          case f: ap.BuiltInFunctionKind => getBuiltInFuncType(f).args
          case base: ap.Symbolic => base.symb match {
            case fsym: st.WithArguments => fsym.args.map(fsym.context.typ(_))
            case c => Violation.violation(s"This case should be unreachable, but got $c")
          }
        }

        val parameterCount: Int = params.length

        // is of the form Some(x) if the type of the last param is variadic and the type of its elements is x
        val variadicTypeOption: Option[Type] = params.lastOption match {
          case Some(VariadicT(elem)) => Some(elem)
          case _ => None
        }

        val wRes: Writer[Vector[in.Expr]] = sequence(p.args map exprD(ctx)).map {
          // go function chaining feature
          case Vector(in.Tuple(targs)) if parameterCount > 1 => targs
          case dargs => dargs
        }

        lazy val getArgsMap = (args: Vector[in.Expr], typ: in.Type) =>
          args.zipWithIndex.map {
            case (arg, index) => BigInt(index) -> implicitConversion(arg.typ, typ, arg)
          }.toMap

        variadicTypeOption match {
          case Some(variadicTyp) => for {
            res <- wRes
            variadicInTyp = typeD(variadicTyp, Addressability.sliceElement)(src)
            len = res.length
            argList = res.lastOption.map(_.typ) match {
              case Some(in.SliceT(elems, _)) if len == parameterCount && elems == variadicInTyp =>
                // corresponds to the case where an unpacked slice is already passed as an argument
                res
              case Some(in.TupleT(_, _)) if len == 1 && parameterCount == 1 =>
                // supports chaining function calls with variadic functions of one argument
                val argsMap = getArgsMap(res.last.asInstanceOf[in.Tuple].args, variadicInTyp)
                Vector(in.SliceLit(variadicInTyp, argsMap)(src))
              case _ if len >= parameterCount =>
                val argsMap = getArgsMap(res.drop(parameterCount-1), variadicInTyp)
                res.take(parameterCount-1) :+ in.SliceLit(variadicInTyp, argsMap)(src)
              case _ if len == parameterCount - 1 =>
                // variadic argument not passed
                res :+ in.NilLit(in.SliceT(variadicInTyp, Addressability.nil))(src)
              case t => violation(s"this case should be unreachable, but got $t")
            }
          } yield argList

          case None => wRes
        }
      }

      // encode results
      val (resT, targets) = p.callee match {
        // `BuiltInFunctionKind` has to be checked first since it implements `Symbolic` as well
        case f: ap.BuiltInFunctionKind =>
          val ft = getBuiltInFuncType(f)
          val resT = typeD(ft.result, Addressability.callResult)(src)
          val targets = resT match {
            case in.VoidT => Vector()
            case _ => Vector(freshExclusiveVar(resT)(src))
          }
          (resT, targets)
        case base: ap.Symbolic => base.symb match {
          case fsym: st.WithResult =>
            val resT = typeD(fsym.context.typ(fsym.result), Addressability.callResult)(src)
            val targets = fsym.result.outs map (o => freshExclusiveVar(typeD(fsym.context.symbType(o.typ), Addressability.exclusiveVariable)(src))(src))
            (resT, targets)
          case c => Violation.violation(s"This case should be unreachable, but got $c")
        }
      }
      val res = if (targets.size == 1) targets.head else in.Tuple(targets)(src) // put returns into a tuple if necessary

      val isPure = p.callee match {
        case base: ap.Symbolic => base.symb match {
          case f: st.Function => f.isPure
          case m: st.Method => m.isPure
          case _: st.DomainFunction => true
          case f: st.BuiltInFunction => f.isPure
          case m: st.BuiltInMethod => m.isPure
          case c => Violation.violation(s"This case should be unreachable, but got $c")
        }
      }

      p.callee match {
        case base: ap.FunctionKind => base match {
          case _: ap.Function | _: ap.BuiltInFunction =>
            if (isPure) {
              for {
                args <- dArgs
                convertedArgs = convertArgs(args)
                fproxy = getFunctionProxy(base, convertedArgs)
              } yield in.PureFunctionCall(fproxy, convertedArgs, resT)(src)
            } else {
              for {
                args <- dArgs
                _ <- declare(targets: _*)
                convertedArgs = convertArgs(args)
                fproxy = getFunctionProxy(base, convertedArgs)
                _ <- write(in.FunctionCall(targets, fproxy, convertedArgs)(src))
              } yield res
            }

          case iim: ap.ImplicitlyReceivedInterfaceMethod =>
            violation(info == iim.symb.context.getTypeInfo,
              "invariance in desugarer violated: interface methods can only be implicitly call in the interface itself")
            if (isPure) {
              for {
                args <- dArgs
                convertedArgs = convertArgs(args)
                proxy = methodProxy(iim.id, iim.symb.context.getTypeInfo)
                recvType = typeD(iim.symb.itfType, Addressability.receiver)(src)
              } yield in.PureMethodCall(implicitThisD(recvType)(src), proxy, convertedArgs, resT)(src)
            } else {
              for {
                args <- dArgs
                _ <- declare(targets: _*)
                convertedArgs = convertArgs(args)
                proxy = methodProxy(iim.id, iim.symb.context.getTypeInfo)
                recvType = typeD(iim.symb.itfType, Addressability.receiver)(src)
                _ <- write(in.MethodCall(targets, implicitThisD(recvType)(src), proxy, convertedArgs)(src))
              } yield res
            }

          case df: ap.DomainFunction =>
            for {
              args <- dArgs
              convertedArgs = convertArgs(args)
              proxy = domainFunctionProxy(df.symb)
            } yield in.DomainFunctionCall(proxy, convertedArgs, resT)(src)

          case _: ap.ReceivedMethod | _: ap.MethodExpr | _: ap.BuiltInReceivedMethod | _: ap.BuiltInMethodExpr => {
            val dRecvWithArgs = base match {
              case base: ap.ReceivedMethod =>
                for {
                  r <- exprD(ctx)(base.recv)
                  args <- dArgs
                } yield (applyMemberPathD(r, base.path)(src), args)
              case base: ap.MethodExpr =>
                // first argument is the receiver, the remaining arguments are the rest
                dArgs map (args => (applyMemberPathD(args.head, base.path)(src), args.tail))
              case base: ap.BuiltInReceivedMethod =>
                for {
                  r <- exprD(ctx)(base.recv)
                  args <- dArgs
                } yield (applyMemberPathD(r, base.path)(src), args)
              case base: ap.BuiltInMethodExpr =>
                // first argument is the receiver, the remaining arguments are the rest
                dArgs map (args => (applyMemberPathD(args.head, base.path)(src), args.tail))
              case c => Violation.violation(s"This case should be unreachable, but got $c")
            }

            if (isPure) {
              for {
                (recv, args) <- dRecvWithArgs
                convertedArgs = convertArgs(args)
                mproxy = getMethodProxy(base, recv, convertedArgs)
              } yield in.PureMethodCall(recv, mproxy, convertedArgs, resT)(src)
            } else {
              for {
                (recv, args) <- dRecvWithArgs
                _ <- declare(targets: _*)
                convertedArgs = convertArgs(args)
                mproxy = getMethodProxy(base, recv, convertedArgs)
                _ <- write(in.MethodCall(targets, recv, mproxy, convertedArgs)(src))
              } yield res
            }
          }
          case sym => Violation.violation(s"expected symbol with arguments and result or a built-in entity, but got $sym")
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

    def arguments(ft: FunctionT, args: Vector[in.Expr]): Vector[in.Expr] = {
      val c = args zip ft.args
      val assignments = c.map{ case (from, pTo) => (from, typeD(pTo, Addressability.inParameter)(from.info)) }
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
          for {
            expr <- exprD(ctx)(decl)
            v <- freshDeclaredExclusiveVar(expr.typ)(src)
          } yield in.Assignee.Var(v)
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

      // if expr is a permission and its case is defined in maybePermissionD,
      // then desugaring expr should yield the value returned by that method
      if(info.typ(expr) == PermissionT) {
        val maybePerm = maybePermissionD(ctx)(expr)
        if (maybePerm.isDefined) {
          return maybePerm.head
        }
      }

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
                v <- freshDeclaredExclusiveVar(in.PointerT(c.typ.withAddressability(Addressability.Shared), Addressability.reference))(src)
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

          case n: PInvoke => invokeD(ctx)(n)

          case n: PTypeAssertion =>
            for {
              inExpr <- go(n.base)
              inArg = typeD(info.symbType(n.typ), Addressability.typeAssertionResult)(src)
            } yield in.TypeAssertion(inExpr, inArg)(src)

          case PNegation(op) => for {o <- go(op)} yield in.Negation(o)(src)

          case PEquals(left, right) =>
            if (info.typOfExprOrType(left) == PermissionT || info.typOfExprOrType(right) == PermissionT) {
              for {
                l <- permissionD(ctx)(left.asInstanceOf[PExpression])
                r <- permissionD(ctx)(right.asInstanceOf[PExpression])
              } yield in.EqCmp(l, r)(src)
            } else {
              for {
                l <- exprAndTypeAsExpr(ctx)(left)
                r <- exprAndTypeAsExpr(ctx)(right)
              } yield in.EqCmp(l, r)(src)
            }

          case PUnequals(left, right) =>
            if (info.typOfExprOrType(left) == PermissionT || info.typOfExprOrType(right) == PermissionT) {
              for {
                l <- permissionD(ctx)(left.asInstanceOf[PExpression])
                r <- permissionD(ctx)(right.asInstanceOf[PExpression])
              } yield in.UneqCmp(l, r)(src)
            } else {
              for {
                l <- exprAndTypeAsExpr(ctx)(left)
                r <- exprAndTypeAsExpr(ctx)(right)
              } yield in.UneqCmp(l, r)(src)
            }

          case PLess(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx)(left); r <- permissionD(ctx)(right)} yield in.PermLtCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.LessCmp(l, r)(src)
            }

          case PAtMost(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx)(left); r <- permissionD(ctx)(right)} yield in.PermLeCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.AtMostCmp(l, r)(src)
            }

          case PGreater(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx)(left); r <- permissionD(ctx)(right)} yield in.PermGtCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.GreaterCmp(l, r)(src)
            }

          case PAtLeast(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx)(left); r <- permissionD(ctx)(right)} yield in.PermGeCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.AtLeastCmp(l, r)(src)
            }

          case PAnd(left, right) => for {l <- go(left); r <- go(right)} yield in.And(l, r)(src)
          case POr(left, right) => for {l <- go(left); r <- go(right)} yield in.Or(l, r)(src)

          case PAdd(left, right) =>
            for {
              l <- go(left)
              r <- go(right)
              res = (info.typ(left), info.typ(right)) match {
                case (StringT, StringT) => in.Concat(l, r)(src)
                case _ => in.Add(l, r)(src)
              }
            } yield res
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
            freshDeclaredExclusiveVar(typ)(src)

          case PReceive(op) =>
            for {
              dop <- go(op)
              recvChannel = mpredicateProxy(BuiltInMemberTag.RecvChannelMPredTag, dop.typ, Vector())(src)
              recvGivenPerm = methodProxy(BuiltInMemberTag.RecvGivenPermMethodTag, dop.typ, Vector())(src)
              recvGotPerm = methodProxy(BuiltInMemberTag.RecvGotPermMethodTag, dop.typ, Vector())(src)
            } yield in.Receive(dop, recvChannel, recvGivenPerm, recvGotPerm)(src)

          case PMake(t, args) =>
            def elemD(t: Type): in.Type = typeD(t, Addressability.make)(src)

            // the target arguments must be always exclusive, that is an invariant of the desugarer
            val resT = typeD(info.symbType(t), Addressability.Exclusive)(src)

            for {
              target <- freshDeclaredExclusiveVar(resT)(src)
              argsD <- sequence(args map go)
              arg0 = argsD.lift(0)
              arg1 = argsD.lift(1)

              make: in.MakeStmt = info.symbType(t) match {
                case s: SliceT => in.MakeSlice(target, elemD(s).asInstanceOf[in.SliceT], arg0.get, arg1)(src)
                case s: GhostSliceT => in.MakeSlice(target, elemD(s).asInstanceOf[in.SliceT], arg0.get, arg1)(src)
                case c@ChannelT(_, _) =>
                  val channelType = elemD(c).asInstanceOf[in.ChannelT]
                  val isChannelProxy = mpredicateProxy(BuiltInMemberTag.IsChannelMPredTag, channelType, Vector())(src)
                  val bufferSizeProxy = methodProxy(BuiltInMemberTag.BufferSizeMethodTag, channelType, Vector())(src)
                  in.MakeChannel(target, channelType, arg0, isChannelProxy, bufferSizeProxy)(src)
                case m@MapT(_, _) => in.MakeMap(target, elemD(m).asInstanceOf[in.MapT], arg0)(src)
                case c => Violation.violation(s"This case should be unreachable, but got $c")
              }
              _ <- write(make)
            } yield target

          case PNew(t) =>
            val allocatedType = typeD(info.symbType(t), Addressability.Exclusive)(src)
            val targetT = in.PointerT(allocatedType.withAddressability(Addressability.Shared), Addressability.reference)
            for {
              target <- freshDeclaredExclusiveVar(targetT)(src)
              zero = in.DfltVal(allocatedType)(src)
              _ <- write(in.New(target, zero)(src))
            } yield target

          case PPredConstructor(base, args) =>
            def handleBase(base: PPredConstructorBase, w: (in.PredT, Vector[Option[in.Expr]]) => Writer[in.Expr]) = {
              for {
                dArgs <- sequence(args.map { x => option(x.map(exprD(ctx)(_))) })
                idT = info.typ(base) match {
                  case FunctionT(fnArgs, AssertionT) => in.PredT(fnArgs.map(typeD(_, Addressability.rValue)(src)), Addressability.rValue)
                  case _: AbstractType =>
                    violation(dArgs.length == dArgs.flatten.length, "non-applied arguments in abstract type")
                    // The result can have arguments, namely the arguments that are provided.
                    // The receiver type is not necessary, since this should already be partially applied by the typing of base
                    in.PredT(dArgs.flatten.map(_.typ), Addressability.rValue)
                  case c => Violation.violation(s"This case should be unreachable, but got $c")
                }
                res <- w(idT, dArgs)
              } yield res
            }

            def getFPredProxy(args: Vector[Option[in.Expr]]) = info.regular(base.id) match {
              case _: st.FPredicate => fpredicateProxy(base.id, info)
              case st.BuiltInFPredicate(tag, _, _) =>
                // the type checker ensures that all arguments are applied
                val appliedArgs = args.flatten
                violation(appliedArgs.length == args.length, s"unsupported predicate construction of a built-in predicate with partially applied arguments")
                fpredicateProxy(tag, appliedArgs.map(_.typ))(src)
              case c => Violation.violation(s"This case should be unreachable, but got $c")
            }

            def getMPredProxy(recvT: in.Type, argsT: Vector[in.Type]) = info.regular(base.id) match {
              case _: st.MPredicate => mpredicateProxyD(base.id, info)
              case st.BuiltInMPredicate(tag, _, _) => mpredicateProxy(tag, recvT, argsT)(src)
              case c => Violation.violation(s"This case should be unreachable, but got $c")
            }

            base match {
              case b: PFPredBase =>
                handleBase(b, { case (baseT: in.PredT, dArgs: Vector[Option[in.Expr]]) =>
                  unit( in.PredicateConstructor(getFPredProxy(dArgs), baseT, dArgs)(src) )
                })

              case b@PDottedBase(recvWithId) => info.resolve(recvWithId) match {
                case Some(_: ap.ReceivedPredicate) =>
                  handleBase(b, { case (baseT: in.PredT, dArgs: Vector[Option[in.Expr]]) =>
                    for {
                      dRecv <- exprAndTypeAsExpr(ctx)(b.recv)
                      proxy = getMPredProxy(dRecv.typ, baseT.args)
                      idT = in.PredT(dRecv.typ +: baseT.args, Addressability.rValue)
                    } yield in.PredicateConstructor(proxy, idT, Some(dRecv) +: dArgs)(src)
                  })

                case Some(_: ap.Predicate) =>
                  handleBase(b, { case (baseT: in.PredT, dArgs: Vector[Option[in.Expr]]) =>
                    unit( in.PredicateConstructor(getFPredProxy(dArgs), baseT, dArgs)(src) )
                  })

                case Some(_: ap.PredicateExpr) =>
                  handleBase(b, { case (baseT: in.PredT, dArgs: Vector[Option[in.Expr]]) =>
                    for {
                      dRecv <- exprAndTypeAsExpr(ctx)(b.recv)
                      proxy = getMPredProxy(dRecv.typ, baseT.args.tail) // args must include at least the receiver
                      idT = in.PredT(baseT.args, Addressability.rValue)
                    } yield in.PredicateConstructor(proxy, idT, dArgs)(src)
                  })
                case c => Violation.violation(s"This case should be unreachable, but got $c")
              }
          }

          case PUnpackSlice(slice) => exprD(ctx)(slice)
          case e => Violation.violation(s"desugarer: $e is not supported")
        }
      }
    }

    def invokeD(ctx: FunctionContext)(expr: PExpression): Writer[in.Expr] = {
      val src: Meta = meta(expr)
      info.resolve(expr) match {
        case Some(p: ap.FunctionCall) => functionCallD(ctx)(p)(src)
        case Some(ap.Conversion(typ, arg)) =>
          val desugaredTyp = typeD(info.symbType(typ), info.addressability(expr))(src)
          if (arg.length == 1) {
            for { expr <- exprD(ctx)(arg(0)) } yield in.Conversion(desugaredTyp, expr)(src)
          } else {
            Violation.violation(s"desugarer: conversion $expr is not supported")
          }
        case Some(_: ap.PredicateCall) => Violation.violation(s"cannot desugar a predicate call ($expr) to an expression")
        case p => Violation.violation(s"expected function call, predicate call, or conversion, but got $p")
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

        case PStringType() => unit(in.StringTExpr()(src))

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
        case PStringLit(s) => single(in.StringLit(s))
        case nil: PNilLit => single(in.NilLit(typeD(info.nilType(nil).getOrElse(Type.PointerT(Type.BooleanT)), Addressability.literal)(src))) // if no type is found, then use *bool
        case c: PCompositeLit => compositeLitD(ctx)(c)
        case _ => ???
      }
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
      case class Map(t : in.MapT) extends CompositeKind
      case class MathematicalMap(t : in.MathMapT) extends CompositeKind
      case class Struct(t: in.Type, st: in.StructT) extends CompositeKind
    }

    def compositeTypeD(t : in.Type) : CompositeKind = t match {
      case _ if isStructType(t) => CompositeKind.Struct(t, structType(t).get)
      case t: in.ArrayT => CompositeKind.Array(t)
      case t: in.SliceT => CompositeKind.Slice(t)
      case t: in.SequenceT => CompositeKind.Sequence(t)
      case t: in.SetT => CompositeKind.Set(t)
      case t: in.MultisetT => CompositeKind.Multiset(t)
      case t: in.MapT => CompositeKind.Map(t)
      case t: in.MathMapT => CompositeKind.MathematicalMap(t)
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

    def interfaceType(typ: in.Type): Option[in.InterfaceT] = underlyingType(typ) match {
      case st: in.InterfaceT => Some(st)
      case _ => None
    }

    def compositeValD(ctx : FunctionContext)(expr : PCompositeVal, typ : in.Type) : Writer[in.Expr] = {
      val e = expr match {
        case PExpCompositeVal(e) => exprD(ctx)(e)
        case PLitCompositeVal(lit) => literalValD(ctx)(lit, typ)
      }
      e map { exp => implicitConversion(exp.typ, typ, exp) }
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

        case CompositeKind.Map(in.MapT(keys, values, _)) => for {
          entriesD <- handleMapEntries(ctx)(lit, keys, values)
        } yield in.MapLit(keys, values, entriesD)(src)

        case CompositeKind.MathematicalMap(in.MathMapT(keys, values, _)) => for {
          entriesD <- handleMapEntries(ctx)(lit, keys, values)
        } yield in.MathMapLit(keys, values, entriesD)(src)
      }
    }

    private def handleMapEntries(ctx: FunctionContext)(lit: PLiteralValue, keys: in.Type, values: in.Type): Writer[Seq[(in.Expr, in.Expr)]] = {
      sequence(
        lit.elems map {
          case PKeyedElement(Some(key), value) => for {
            entryKey <- key match {
              case v: PCompositeVal => compositeValD(ctx)(v, keys)
              case k: PIdentifierKey => info.regular(k.id) match {
                case _: st.Variable => unit(varD(ctx)(k.id))
                case c: st.Constant => unit(globalConstD(c)(meta(k)))
                case _ => violation(s"unexpected key $key")
              }
            }
            entryVal <- compositeValD(ctx)(value, values)
          } yield (entryKey, entryVal)

          case _ => violation("unexpected pattern, missing key in map literal")
        })
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



    def registerInterface(t: Type.InterfaceT, dT: in.InterfaceT): Unit = {
      Violation.violation(t.decl.embedded.isEmpty, "embeddings in interfaces are currently not supported")

      if (!registeredInterfaces.contains(dT.name) && info == t.context.getTypeInfo) {
        registeredInterfaces += dT.name

        val itfT = dT.withAddressability(Addressability.Exclusive)
        val xInfo = t.context.getTypeInfo

        t.decl.predSpec foreach { p =>
          val src = meta(p, xInfo)
          val proxy = mpredicateProxyD(p, xInfo)
          val recv = implicitThisD(itfT)(src)
          val argsWithSubs = p.args.zipWithIndex map { case (p,i) => inParameterD(p,i,xInfo) }
          val (args, _) = argsWithSubs.unzip

          val mem = in.MPredicate(recv, proxy, args, None)(src)

          definedMPredicates += (proxy -> mem)
          AdditionalMembers.addMember(mem)
        }

        t.decl.methSpecs foreach { m =>
          val src = meta(m, xInfo)
          val proxy = methodProxyD(m, xInfo)
          val recv = implicitThisD(itfT)(src)
          val argsWithSubs = m.args.zipWithIndex map { case (p,i) => inParameterD(p,i,xInfo) }
          val (args, _) = argsWithSubs.unzip
          val returnsWithSubs = m.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i,xInfo) }
          val (returns, _) = returnsWithSubs.unzip
          val specCtx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(src)) // dummy assign
          val pres = m.spec.pres map preconditionD(specCtx)
          val posts = m.spec.posts map postconditionD(specCtx)

          val mem = if (m.spec.isPure) {
            in.PureMethod(recv, proxy, args, returns, pres, posts, None)(src)
          } else {
            in.Method(recv, proxy, args, returns, pres, posts, None)(src)
          }

          definedMethods += (proxy -> mem)
          AdditionalMembers.addMember(mem)
        }
      }
    }
    var registeredInterfaces: Set[String] = Set.empty



    object AdditionalMembers {

      private var additionalMembers: List[in.Member] = List.empty
      private var finalized: Boolean = false
      private var computationsBeforeFinalize: List[() => Unit] = List.empty

      def addMember(m: in.Member): Unit = additionalMembers ::= m
      def addFinalizingComputation(f: () => Unit): Unit = computationsBeforeFinalize ::= f
      def finalizedMembers: List[in.Member] = {
        require(!finalized)

        if (!finalized) {
          computationsBeforeFinalize.foreach(_())
          finalized = true
        }

        additionalMembers
      }
    }


    def registerDomain(t: Type.DomainT, dT: in.DomainT): Unit = {
      if (!registeredDomains.contains(dT.name) && info == t.context.getTypeInfo) {
        registeredDomains += dT.name

        AdditionalMembers.addFinalizingComputation{ () =>

          val xInfo = t.context.getTypeInfo

          val funcs = t.decl.funcs.map{ f =>
            val src = meta(f, xInfo)
            val proxy = domainFunctionProxy(st.DomainFunction(f, t.decl, t.context))
            val argsWithSubs = f.args.zipWithIndex map { case (p,i) => inParameterD(p,i,xInfo) }
            val (args, _) = argsWithSubs.unzip
            val returnsWithSubs = f.result.outs.zipWithIndex map { case (p,i) => outParameterD(p,i,xInfo) }
            val (returns, _) = returnsWithSubs.unzip
            in.DomainFunc(proxy, args, returns.head)(src)
          }

          val axioms = t.decl.axioms.map{ ax =>
            val src = meta(ax, xInfo)
            val specCtx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(src)) // dummy assign
            in.DomainAxiom(pureExprD(specCtx)(ax.exp))(src)
          }

          AdditionalMembers.addMember(
            in.DomainDefinition(dT.name, funcs, axioms)(meta(t.decl, xInfo))
          )
        }
      }
    }
    var registeredDomains: Set[String] = Set.empty

    def registerImplementationProof(decl: PImplementationProof): Unit = {

      val src = meta(decl)
      val subT = info.symbType(decl.subT)
      val dSubT = typeD(subT, Addressability.Exclusive)(src)
      val superT = info.symbType(decl.superT)
      val dSuperT = interfaceType(typeD(superT, Addressability.Exclusive)(src)).get

      decl.alias foreach { al =>
        info.resolve(al.right) match {
          case Some(p: ap.Predicate) =>
            implementationProofPredicateAliases += ((dSubT, dSuperT, al.left.name) -> fpredicateProxyD(p.symb.decl, p.symb.context.getTypeInfo))
          case _ => violation("Right-hand side of an predicate assignment in an implementation proof must be a predicate")
        }
      }

      decl.memberProofs foreach { mp =>

        val subSymb = info.getMember(subT, mp.id.name).get._1.asInstanceOf[st.MethodImpl]
        val subProxy = methodProxyFromSymb(subSymb)
        val superSymb = info.getMember(superT, mp.id.name).get._1.asInstanceOf[st.MethodSpec]
        val superProxy = methodProxyFromSymb(superSymb)

        val src = meta(mp)
        val recvWithSubs = inParameterD(mp.receiver, 0)
        val (recv, _) = recvWithSubs
        val argsWithSubs = mp.args.zipWithIndex map { case (p, i) => inParameterD(p, i) }
        val (args, _) = argsWithSubs.unzip
        val returnsWithSubs = mp.result.outs.zipWithIndex map { case (p, i) => outParameterD(p, i) }
        val (returns, _) = returnsWithSubs.unzip

        val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(src)) // dummy assign
        val res = if (mp.isPure) {
          val bodyOpt = mp.body.map {
            case (_, b: PBlock) =>
              b.nonEmptyStmts match {
                case Vector(PReturn(Vector(ret))) => pureExprD(ctx)(ret)
                case s => Violation.violation(s"unexpected pure function body: $s")
              }
          }
          in.PureMethodSubtypeProof(subProxy, dSuperT, superProxy, recv, args, returns, bodyOpt)(src)
        } else {
          val bodyOpt = mp.body.map { case (_, s) => blockD(ctx)(s).asInstanceOf[in.Block] }
          in.MethodSubtypeProof(subProxy, dSuperT, superProxy, recv, args, returns, bodyOpt)(src)
        }

        AdditionalMembers.addMember(res)
      }
    }

    lazy val interfaceImplementations: Map[in.InterfaceT, Set[in.Type]] = {
      info.interfaceImplementations.map{ case (itfT, implTs) =>
        (
          interfaceType(typeD(itfT, Addressability.Exclusive)(Source.Parser.Unsourced)).get,
          implTs.map(implT => typeD(implT, Addressability.Exclusive)(Source.Parser.Unsourced))
        )
      }
    }
    def missingImplProofs: Vector[in.Member] = {

      info.missingImplProofs.map{ case (implT, itfT, implSymb, itfSymb) =>
        val subProxy = methodProxyFromSymb(implSymb)
        val superT = interfaceType(typeD(itfT, Addressability.Exclusive)(Source.Parser.Unsourced)).get
        val superProxy = methodProxyFromSymb(itfSymb)
        val receiver = receiverD(implSymb.decl.receiver, implSymb.context.getTypeInfo)._1
        val args = implSymb.args.zipWithIndex.map{ case (arg, idx) => inParameterD(arg, idx, implSymb.context.getTypeInfo)._1 }
        val results = implSymb.result.outs.zipWithIndex.map{ case (res, idx) => outParameterD(res, idx, implSymb.context.getTypeInfo)._1 }

        val src = meta(implSymb.decl, implSymb.context.getTypeInfo).createAnnotatedInfo(AutoImplProofAnnotation(implT.toString, itfT.toString))

        if (itfSymb.isPure) in.PureMethodSubtypeProof(subProxy, superT, superProxy, receiver, args, results, None)(src)
        else in.MethodSubtypeProof(subProxy, superT, superProxy, receiver, args, results, None)(src)
      }
    }
    var implementationProofPredicateAliases: Map[(in.Type, in.InterfaceT, String), in.FPredicateProxy] = Map.empty


    def registerMethod(decl: PMethodDecl): in.MethodMember = {
      val method = methodD(decl)
      val methodProxy = methodProxyD(decl, info)
      definedMethods += methodProxy -> method
      method
    }

    def registerFunction(decl: PFunctionDecl): in.FunctionMember = {
      val function = functionD(decl)
      val functionProxy = functionProxyD(decl, info)
      definedFunctions += functionProxy -> function
      function
    }

    def registerMPredicate(decl: PMPredicateDecl): in.MPredicate = {
      val mPredProxy = mpredicateProxyD(decl, info)
      val mPred = mpredicateD(decl)
      definedMPredicates += mPredProxy -> mPred
      mPred
    }

    def registerFPredicate(decl: PFPredicateDecl): in.FPredicate = {
      val fPredProxy = fpredicateProxyD(decl, info)
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
      case Type.StringT => in.StringT(addrMod)
      case Type.IntT(x) => in.IntT(addrMod, x)
      case Type.ArrayT(length, elem) => in.ArrayT(length, typeD(elem, Addressability.arrayElement(addrMod))(src), addrMod)
      case Type.SliceT(elem) => in.SliceT(typeD(elem, Addressability.sliceElement)(src), addrMod)
      case Type.MapT(keys, values) =>
        val keysD = typeD(keys, Addressability.mapKey)(src)
        val valuesD = typeD(values, Addressability.mapValue)(src)
        in.MapT(keysD, valuesD, addrMod)
      case Type.GhostSliceT(elem) => in.SliceT(typeD(elem, Addressability.sliceElement)(src), addrMod)
      case Type.OptionT(elem) => in.OptionT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case PointerT(elem) => registerType(in.PointerT(typeD(elem, Addressability.pointerBase)(src), addrMod))
      case Type.ChannelT(elem, _) => in.ChannelT(typeD(elem, Addressability.channelElement)(src), addrMod)
      case Type.SequenceT(elem) => in.SequenceT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case Type.SetT(elem) => in.SetT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case Type.MultisetT(elem) => in.MultisetT(typeD(elem, Addressability.mathDataStructureElement)(src), addrMod)
      case Type.MathMapT(keys, values) =>
        val keysD = typeD(keys, Addressability.mathDataStructureElement)(src)
        val valuesD = typeD(values, Addressability.mathDataStructureElement)(src)
        in.MathMapT(keysD, valuesD, addrMod)

      case t: Type.StructT =>
        val inFields: Vector[in.Field] = structD(t, addrMod)(src)

        val structName = nm.struct(t)
        registerType(in.StructT(structName, inFields, addrMod))

      case Type.PredT(args) => in.PredT(args.map(typeD(_, Addressability.rValue)(src)), Addressability.rValue)

      case Type.FunctionT(_, _) => ???

      case t: Type.InterfaceT =>
        val interfaceName = nm.interface(t)
        val res = registerType(in.InterfaceT(interfaceName, addrMod))
        registerInterface(t, res)
        res

      case t: Type.DomainT =>
        val domainT = in.DomainT(nm.domain(t), addrMod)
        registerType(domainT)
        registerDomain(t, domainT)
        domainT

      case Type.InternalTupleT(ts) => in.TupleT(ts.map(t => typeD(t, Addressability.mathDataStructureElement)(src)), addrMod)

      case Type.SortT => in.SortT

      case Type.VariadicT(elem) =>
        val elemD = typeD(elem, Addressability.sliceElement)(src)
        in.SliceT(elemD, addrMod)

      case Type.PermissionT => in.PermissionT(addrMod)

      case _ => Violation.violation(s"got unexpected type $t")
    }


    // Identifier

    def idName(id: PIdnNode, context: TypeInfo = info): String = context.regular(id) match {
      case f: st.Function => nm.function(id.name, f.context)
      case m: st.MethodImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case m: st.MethodSpec => nm.spec(id.name, m.itfType, m.context)
      case f: st.FPredicate => nm.function(id.name, f.context)
      case m: st.MPredicateImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case m: st.MPredicateSpec => nm.spec(id.name, m.itfType, m.context)
      case f: st.DomainFunction => nm.function(id.name, f.context)
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

    def freshDeclaredExclusiveVar(typ: in.Type)(info: Source.Parser.Info): Writer[in.LocalVar] = {
      require(typ.addressability == Addressability.exclusiveVariable)
      val res = in.LocalVar(nm.fresh, typ)(info)
      declare(res).map(_ => res)
    }

    def localVarD(ctx: FunctionContext)(id: PIdnNode): in.LocalVar = {
      require(info.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      ctx(id) match {
        case Some(v: in.LocalVar) => v
        case None => localVarContextFreeD(id)
        case _ => violation("expected local variable")
      }
    }

    def localVarContextFreeD(id: PIdnNode, context: TypeInfo = info): in.LocalVar = {
      require(context.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      val src: Meta = meta(id, context)

      val typ = typeD(context.typ(id), context.addressableVar(id))(meta(id, context))
      in.LocalVar(idName(id, context), typ)(src)
    }

    def parameterAsLocalValVar(p: in.Parameter): in.LocalVar = {
      in.LocalVar(p.id, p.typ)(p.info)
    }

    def labelProxy(l: PLabelNode): in.LabelProxy = {
      val src = meta(l)
      in.LabelProxy(nm.label(l.name))(src)
    }

    // Miscellaneous

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def inParameterD(p: PParameter, idx: Int, context: TypeInfo = info): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p, context)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.In(idName(id, context), typeD(context.symbType(typ), Addressability.inParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id, context)))
              (param, local)

            case PUnnamedParameter(typ) =>
              val param = in.Parameter.In(nm.inParam(idx, context.codeRoot(p), context), typeD(context.symbType(typ), Addressability.inParameter)(src))(src)
              val local = None
              (param, local)
          }
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def outParameterD(p: PParameter, idx: Int, context: TypeInfo = info): (in.Parameter.Out, Option[in.LocalVar]) = {
      val src: Meta = meta(p, context)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.Out(idName(id, context), typeD(context.symbType(typ), Addressability.outParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id, context)))
              (param, local)

            case PUnnamedParameter(typ) =>
              val param = in.Parameter.Out(nm.outParam(idx, context.codeRoot(p), context), typeD(context.symbType(typ), Addressability.outParameter)(src))(src)
              val local = None
              (param, local)
          }
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    def receiverD(p: PReceiver, context: TypeInfo = info): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p)
      p match {
        case PNamedReceiver(id, typ, _) =>
          val param = in.Parameter.In(idName(id, context), typeD(context.symbType(typ), Addressability.receiver)(src))(src)
          val local = Some(localAlias(localVarContextFreeD(id, context)))
          (param, local)

        case PUnnamedReceiver(typ) =>
          val param = in.Parameter.In(nm.receiver(context.codeRoot(p), context), typeD(context.symbType(typ), Addressability.receiver)(src))(src)
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
        case PFold(exp)   =>
          info.resolve(exp.pred) match {
            case Some(_: ap.PredExprInstance) => for {
              // the well-definedness checks guarantees that a pred expr instance in a Fold is in format predName{p1,...,pn}(a1, ...., am)
              e <- goA(exp)
              access = e.asInstanceOf[in.Access]
              predExpInstance = access.e.op.asInstanceOf[in.PredExprInstance]
            } yield in.PredExprFold(predExpInstance.base.asInstanceOf[in.PredicateConstructor],  predExpInstance.args, access.p)(src)

            case _ => for {e <- goA(exp)} yield in.Fold(e.asInstanceOf[in.Access])(src)
          }
        case PUnfold(exp) =>
          info.resolve(exp.pred) match {
            case Some(_: ap.PredExprInstance) => for {
              // the well-definedness checks guarantees that a pred expr instance in an Unfold is in format predName{p1,...,pn}(a1, ...., am)
              e <- goA(exp)
              access = e.asInstanceOf[in.Access]
              predExpInstance = access.e.op.asInstanceOf[in.PredExprInstance]
            } yield in.PredExprUnfold(predExpInstance.base.asInstanceOf[in.PredicateConstructor],  predExpInstance.args, access.p)(src)
            case _ => for {e <- goA(exp)} yield in.Unfold(e.asInstanceOf[in.Access])(src)
          }
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
        case PLabeledOld(l, op) => for {o <- go(op)} yield in.LabeledOld(labelProxy(l), o)(src)
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

        case PGhostCollectionUpdate(col, clauses) => clauses.foldLeft(go(col)) {
          case (dcol, clause) => for {
            dleft <- go(clause.left)
            dright <- go(clause.right)
          } yield in.GhostCollectionUpdate(dcol.res, dleft, dright)(src)
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

        case PMapKeys(exp) => for {
          e <- go(exp)
        } yield in.MapKeys(e)(src)

        case PMapValues(exp) => for {
          e <- go(exp)
        } yield in.MapValues(e)(src)

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
      Violation.violation(condition.stmts.isEmpty && condition.decls.isEmpty, s"$ass is not an assertion")
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

    def predicateCallD(ctx: FunctionContext)(n: PInvoke, perm: PExpression): Writer[in.Assertion] = {

      val src: Meta = meta(n)

      info.resolve(n) match {
        case Some(p: ap.PredicateCall) =>
          for {
            predAcc <- predicateCallAccD(ctx)(p)(src)
            p <- permissionD(ctx)(perm)
          } yield in.Access(in.Accessible.Predicate(predAcc), p)(src)

        case Some(_: ap.PredExprInstance) =>
          for {
            base <- exprD(ctx)(n.base.asInstanceOf[PExpression])
            args <- sequence(n.args.map(exprD(ctx)(_)))
            predExprInstance = in.PredExprInstance(base, args)(src)
            p <- permissionD(ctx)(perm)
          } yield in.Access(in.Accessible.PredExpr(predExprInstance), p)(src)

        case _ => exprD(ctx)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallAccD(ctx: FunctionContext)(p: ap.PredicateCall)(src: Meta): Writer[in.PredicateAccess] = {

      val dArgs = p.args map pureExprD(ctx)

      p.predicate match {
        case b: ap.Predicate =>
          val fproxy = fpredicateProxy(b.id, info)
          unit(in.FPredicateAccess(fproxy, dArgs)(src))

        case b: ap.ReceivedPredicate =>
          val dRecv = pureExprD(ctx)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val proxy = mpredicateProxyD(b.id, info)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs)(src))

        case b: ap.PredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val proxy = mpredicateProxyD(b.id, info)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs.tail)(src))

        case _: ap.PredExprInstance => Violation.violation("this case should be handled somewhere else")

        case b: ap.ImplicitlyReceivedInterfacePredicate =>
          val proxy = mpredicateProxyD(b.id, info)
          val recvType = typeD(b.symb.itfType, Addressability.receiver)(src)
          unit(in.MPredicateAccess(implicitThisD(recvType)(src), proxy, dArgs)(src))

        case b: ap.BuiltInPredicate =>
          val fproxy = fpredicateProxy(b.id, info)
          unit(in.FPredicateAccess(fproxy, dArgs)(src))

        case b: ap.BuiltInReceivedPredicate =>
          val dRecv = pureExprD(ctx)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val proxy = mpredicateProxy(b.symb.tag, dRecvWithPath.typ, dArgs.map(_.typ))(src)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs)(src))

        case b: ap.BuiltInPredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val proxy = mpredicateProxy(b.symb.tag, dRecvWithPath.typ, dArgs.map(_.typ))(src)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, dArgs.tail)(src))
      }
    }

    def implicitThisD(p: in.Type)(src: Source.Parser.Info): in.Parameter.In =
      in.Parameter.In(nm.implicitThis, p.withAddressability(Addressability.receiver))(src)

    def accessibleD(ctx: FunctionContext)(acc: PExpression): Writer[in.Accessible] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      val src: Meta = meta(acc)

      info.resolve(acc) match {
        case Some(p: ap.PredicateCall) =>
          predicateCallAccD(ctx)(p)(src) map (x => in.Accessible.Predicate(x))

        case Some(p: ap.PredExprInstance) =>
          for {
            base <- goE(p.base)
            predInstArgs <- sequence(p.args map goE)
          } yield in.Accessible.PredExpr(in.PredExprInstance(base, predInstArgs)(src))

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

            // TODO: do similarly same for slices (issue #238)
            case Single(_: Type.MapT) =>
              goE(acc) map (x => in.Accessible.ExprAccess(x))

            case _ => Violation.violation(s"expected pointer type or a predicate, but got $argT")
          }
      }

    }

    def permissionD(ctx: FunctionContext)(exp: PExpression): Writer[in.Expr] = {
      maybePermissionD(ctx)(exp) getOrElse exprD(ctx)(exp)
    }

    def maybePermissionD(ctx: FunctionContext)(exp: PExpression): Option[Writer[in.Expr]] = {
      val src: Meta = meta(exp)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx)(e)

      exp match {
        case n: PInvoke => info.resolve(n) match {
          case Some(_: ap.Conversion) =>
            Some(for {
              // the well-definedness checker ensures that there is exactly one argument
              arg <- permissionD(ctx)(n.args.head)
            } yield in.Conversion(in.PermissionT(Addressability.conversionResult), arg)(src))
          case _ => None
        }
        case PFullPerm() => Some(unit(in.FullPerm(src)))
        case PNoPerm() => Some(unit(in.NoPerm(src)))
        case PWildcardPerm() => Some(unit(in.WildcardPerm(src)))
        case PDiv(l, r) => (info.typ(l), info.typ(r)) match {
          case (PermissionT, IntT(_)) => Some(for { vl <- permissionD(ctx)(l); vr <- goE(r) } yield in.PermDiv(vl, vr)(src))
          case (IntT(_), IntT(_)) => Some(for { vl <- goE(l); vr <- goE(r) } yield in.FractionalPerm(vl, vr)(src))
          case err => violation(s"This case should be unreachable, but got $err")
        }
        case PNegation(exp) => Some(for {e <- permissionD(ctx)(exp)} yield in.PermMinus(e)(src))
        case PAdd(l, r) => Some(for { vl <- permissionD(ctx)(l); vr <- permissionD(ctx)(r) } yield in.PermAdd(vl, vr)(src))
        case PSub(l, r) => Some(for { vl <- permissionD(ctx)(l); vr <- permissionD(ctx)(r) } yield in.PermSub(vl, vr)(src))
        case PMul(l, r) => Some(for {vl <- goE(l); vr <- permissionD(ctx)(r)} yield in.PermMul(vl, vr)(src))
        case x if info.typ(x).isInstanceOf[IntT] => Some(for { e <- goE(x) } yield in.FractionalPerm(e, in.IntLit(BigInt(1))(src))(src))
        case _ => None
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

    private def meta(n: PNode, context: TypeInfo = info): Source.Parser.Single = {
      val pom = context.getTypeInfo.tree.originalRoot.positions
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
    private val DOMAIN_PREFIX = "D"
    private val LABEL_PREFIX = "L"
    private val GLOBAL_PREFIX = "G"
    private val BUILTIN_PREFIX = "B"

    private var counter = 0

    private var scopeCounter = 0
    private var scopeMap: Map[PScope, Int] = Map.empty

    private def maybeRegister(s: PScope): Unit = {
      if (!(scopeMap contains s)) {
        scopeMap += (s -> scopeCounter)
        scopeCounter += 1
      }
    }

    val implicitThis: String = Names.implicitThis

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
    def field   (n: String, @unused s: StructT): String = s"$n$FIELD_PREFIX" // Field names must preserve their equality from the Go level
    def function(n: String, context: ExternalTypeInfo): String = nameWithoutScope(FUNCTION_PREFIX)(n, context)
    def spec    (n: String, t: Type.InterfaceT, context: ExternalTypeInfo): String =
      nameWithoutScope(s"$METHODSPEC_PREFIX${interface(t)}")(n, context)
    def method  (n: String, t: PMethodRecvType, context: ExternalTypeInfo): String = t match {
      case PMethodReceiveName(typ)    => nameWithoutScope(s"$METHOD_PREFIX${typ.name}")(n, context)
      case PMethodReceivePointer(typ) => nameWithoutScope(s"P$METHOD_PREFIX${typ.name}")(n, context)
    }
    private def stringifyType(typ: in.Type): String = typ match {
      case _: in.BoolT => "Bool"
      case _: in.StringT => "String"
      case in.IntT(_, kind) => s"Int${kind.name}"
      case in.VoidT => ""
      case _: in.PermissionT => "Permission"
      case in.SortT => "Sort"
      case in.ArrayT(len, elemT, _) => s"Array$len${stringifyType(elemT)}"
      case in.SliceT(elemT, _) => s"Slice${stringifyType(elemT)}"
      case in.SequenceT(elemT, _) => s"Sequence${stringifyType(elemT)}"
      case in.SetT(elemT, _) => s"Set${stringifyType(elemT)}"
      case in.MultisetT(elemT, _) => s"Multiset${stringifyType(elemT)}"
      case in.OptionT(elemT, _) => s"Option${stringifyType(elemT)}"
      case in.DefinedT(name, _) => s"Defined$name"
      case in.PointerT(t, _) => s"Pointer${stringifyType(t)}"
      // we use a dollar sign to mark the beginning and end of the type list to avoid that `Tuple(Tuple(X), Y)` and `Tuple(Tuple(X, Y))` map to the same name:
      case in.TupleT(ts, _) => s"Tuple$$${ts.map(stringifyType).mkString("")}$$"
      case in.PredT(ts, _) => s"Pred$$${ts.map(stringifyType).mkString("")}$$"
      case in.StructT(name, fields, _) => s"Struct$name$$${fields.map(_.typ).map(stringifyType).mkString("")}$$"
      case in.InterfaceT(name, _) => s"Interface$name"
      case in.ChannelT(elemT, _) => s"Channel${stringifyType(elemT)}"
      case t => Violation.violation(s"cannot stringify type $t")
    }
    def builtInMember(tag: BuiltInMemberTag, dependantTypes: Vector[in.Type]): String = {
      val typeString = dependantTypes.map(stringifyType).mkString("_")
      s"${tag.identifier}_$BUILTIN_PREFIX$FUNCTION_PREFIX$typeString"
    }

    def inverse(n: String): String = n.substring(0, n.length - FIELD_PREFIX.length)

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
        val pom = s.context.getTypeInfo.tree.originalRoot.positions
        val start = pom.positions.getStart(s.decl).get
        val finish = pom.positions.getFinish(s.decl).get
        val pos = pom.translate(start, finish)
        // replace characters that could be misinterpreted:
        val interfaceName = pos.toString
          .replace(".", "$")
          .replace("@", "")
          .replace("-", "_")
        s"$INTERFACE_PREFIX$$$interfaceName"
      }
    }

    def domain(s: DomainT): String = {
      val pom = s.context.getTypeInfo.tree.originalRoot.positions
      val start = pom.positions.getStart(s.decl).get
      val finish = pom.positions.getFinish(s.decl).get
      val pos = pom.translate(start, finish)
      // replace characters that could be misinterpreted:
      val domainName = pos.toString
        .replace(".", "$")
        .replace("@", "")
        .replace("-", "_")
      s"$DOMAIN_PREFIX$$$domainName"
    }

    def label(n: String): String = s"${n}_$LABEL_PREFIX"
  }
}


