// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend

import viper.gobra.ast.frontend.{PExpression, AstPattern => ap, _}
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.BuiltInMemberTag._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{BuiltInMemberTag, Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.frontend.info.{ExternalTypeInfo, TypeInfo}
import viper.gobra.reporting.Source.AutoImplProofAnnotation
import viper.gobra.reporting.{DesugaredMessage, Source}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.util.Violation.violation
import viper.gobra.util.{DesugarWriter, TypeBounds, Violation}

import scala.annotation.{tailrec, unused}
import scala.collection.{Iterable, SortedSet}
import scala.reflect.ClassTag

object Desugar {

  def desugar(pkg: PPackage, info: viper.gobra.frontend.info.TypeInfo)(config: Config): in.Program = {
    // independently desugar each imported package. Only used members (i.e. members for which `isUsed` returns true will be desugared:
    val importedPrograms = info.context.getContexts map { tI => {
      val typeInfo: TypeInfo = tI.getTypeInfo
      val importedPackage = typeInfo.tree.originalRoot
      val d = new Desugarer(importedPackage.positions, typeInfo)
      (d, d.packageD(importedPackage))
    }}
    // desugar the main package, i.e. the package on which verification is performed:
    val mainDesugarer = new Desugarer(pkg.positions, info)
    // combine all desugared results into one Viper program:
    val internalProgram = combine(mainDesugarer, mainDesugarer.packageD(pkg), importedPrograms)
    config.reporter report DesugaredMessage(config.packageInfoInputMap(pkg.info).map(_.name), () => internalProgram)
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
    val combinedImplementations = {
      val interfaceImplMaps = desugarers.flatMap(_.interfaceImplementations.toSeq)
      interfaceImplMaps.groupMapReduce[in.InterfaceT, SortedSet[in.Type]](_._1)(_._2)(_ ++ _)
    }
    val combinedMemberProxies = computeMemberProxies(combinedMethods.values ++ combinedMPredicates.values, combinedImplementations, combinedDefinedT)
    val combineImpProofPredAliases = combineTableField(_.implementationProofPredicateAliases)
    val table = new in.LookupTable(
      combinedDefinedT,
      combinedMethods ++ builtInMethods,
      combineTableField(_.definedFunctions) ++ builtInFunctions,
      combinedMPredicates ++ builtInMPredicates,
      combineTableField(_.definedFPredicates) ++ builtInFPredicates,
      combineTableField(_.definedFuncLiterals),
      combinedMemberProxies,
      combinedImplementations,
      combineImpProofPredAliases
      )
    val builtInMembers = builtInMethods.values ++ builtInFunctions.values ++ builtInMPredicates.values ++ builtInFPredicates.values
    (table, builtInMembers)
  }

  /** For now, the memberset is computed in an inefficient way. */
  def computeMemberProxies(decls: Iterable[in.Member], itfImpls: Map[in.InterfaceT, SortedSet[in.Type]], definedT: Map[(String, Addressability), in.Type]): Map[in.Type, SortedSet[in.MemberProxy]] = {
    val keys = itfImpls.flatMap{ case (k, v) => v union Set(k) }.toSet
    val pairs: Set[(in.Type, SortedSet[in.MemberProxy])] = keys.map{ key =>

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

      key -> SortedSet(decls.collect{
        case m: in.Method if m.receiver.typ == underlyingRecv => m.name
        case m: in.PureMethod if m.receiver.typ == underlyingRecv => m.name
        case m: in.MPredicate if m.receiver.typ == underlyingRecv => m.name
      }.toSeq: _*)
    }
    pairs.toMap[in.Type, SortedSet[in.MemberProxy]]
  }

  object NoGhost {
    def unapply(arg: PNode): Some[PNode] = arg match {
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

    type Identity = (Meta, TypeInfo)

    private def abstraction(id: PIdnNode, info: TypeInfo): Identity = {
      val entity = info.regular(id)
      (meta(entity.rep, entity.context.getTypeInfo), entity.context.getTypeInfo)
    }

    // TODO: make thread safe
    private var proxies: Map[Meta, in.Proxy] = Map.empty

    def getProxy(id: PIdnNode): Option[in.Proxy] =
      proxies.get(abstraction(id, info)._1)

    def addProxy(from: PIdnNode, to: in.Proxy): Unit =
      proxies += abstraction(from, info)._1 -> to

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

    def functionLitProxyD(lit: PFunctionLit, context: TypeInfo): in.FunctionLitProxy = {
      // If the literal is nameless, generate a unique name
      val name = if (lit.id.isEmpty) nm.anonFuncLit(context.enclosingFunction(lit).get, context) else idName(lit.id.get, context)
      val info = if (lit.id.isEmpty) meta(lit, context) else meta(lit.id.get, context)
      in.FunctionLitProxy(name)(info)
    }

    def functionLitProxyD(id: PIdnUse, context: TypeInfo): in.FunctionLitProxy = {
      val name = idName(id, context)
      in.FunctionLitProxy(name)(meta(id, context))
    }

    def closureSpecD(ctx: FunctionContext, info: TypeInfo)(s: PClosureSpecInstance): in.ClosureSpec = {
      val (funcTypeInfo, fArgs, proxy) = info.resolve(s.func) match {
        case Some(ap.Function(id, symb)) => (symb.context.getTypeInfo, symb.decl.args, functionProxy(id, info))
        case Some(ap.Closure(id, symb)) => (symb.context.getTypeInfo, symb.lit.args, functionLitProxyD(id, info))
        case _ => violation("expected function or function literal")
      }
      val paramsWithIdx = if (s.paramKeys.isEmpty) s.paramExprs.zipWithIndex.map {
        case (exp, idx) => idx+1 -> exprD(ctx, info)(exp).res
      } else {
        val argsToIdx = fArgs.zipWithIndex.collect {
          case (PNamedParameter(a, _), idx) => a.name -> (idx+1)
          case (PExplicitGhostParameter(PNamedParameter(a, _)), idx) => a.name -> (idx+1)
        }.toMap
        (s.paramKeys zip s.paramExprs) map { case (k, exp) => argsToIdx(k) -> exprD(ctx, info)(exp).res }
      }
      val params = paramsWithIdx.map {
        case (i, exp) => i -> implicitConversion(exp.typ, typeD(funcTypeInfo.typ(fArgs(i-1)), Addressability.inParameter)(exp.info), exp)
      }.toMap
      in.ClosureSpec(proxy, params)(meta(s, info))
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
                           private var substitutions: Map[Identity, in.Expr] = Map.empty,
                           val expD: PExpression => Option[Writer[in.Expr]] = _ => None,
                         ) {
      def apply(id: PIdnNode, info: TypeInfo): Option[in.Expr] =
        substitutions.get(abstraction(id, info))


      def addSubst(from: PIdnNode, to: in.Expr, info: TypeInfo): Unit =
        substitutions += abstraction(from, info) -> to

      def copy: FunctionContext = new FunctionContext(ret, substitutions, expD)

      def copyWith(ret: Vector[in.Expr] => Meta => in.Stmt): FunctionContext = new FunctionContext(ret, substitutions)

      def copyWithExpD(expD: PExpression => Option[Writer[in.Expr]]) = new FunctionContext(ret, substitutions, expD)
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
        definedFuncLiterals,
        computeMemberProxies(dMembers ++ additionalMembers, interfaceImplementations, definedTypes),
        interfaceImplementations,
        implementationProofPredicateAliases
      )

      in.Program(types.toVector, dMembers ++ additionalMembers, table)(meta(p, info))
    }

    def varDeclGD(decl: PVarDecl): Vector[in.GlobalVarDecl] = {
      val leftAreWildcards = decl.left.forall(_.isInstanceOf[PWildcard])
      val typIsInterface = decl.typ.exists { p =>
        val typ = info.symbType(p)
        underlyingType(typ).isInstanceOf[Type.InterfaceT]
      }
      val rightArePureExpr = decl.right.forall(info.isPureExpression)
      if (leftAreWildcards && typIsInterface && rightArePureExpr) {
        // When the lhs is a wildcard of an interface type and the rhs are pure expressions,
        // the variable declaration can be safely ignored. In some codebases, this idiom is
        // used to check that types implement interfaces.
        Vector()
      } else ???
    }

    def constDeclD(block: PConstDecl): Vector[in.GlobalConstDecl] = block.specs.flatMap(constSpecD)

    def constSpecD(decl: PConstSpec): Vector[in.GlobalConstDecl] = decl.left.flatMap(l => info.regular(l) match {
      case sc@st.SingleConstant(_, id, _, _, _, _) =>
        val src = meta(id, info)
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
          case in.PermissionT(Addressability.Exclusive) =>
            val constValue = sc.context.permConstantEvaluation(sc.exp)
            in.PermLit(constValue.get._1, constValue.get._2)(src)
          case _ => ???
        }
        Vector(in.GlobalConstDecl(gVar, lit)(src))
      case st.Wildcard(_, _) =>
        // Constants defined with the blank identifier can be safely ignored as they
        // must be computable statically (and thus do not have side effects) and
        // they can never be read
        Vector()
      case _ => ???
    })

    // Note: Alternatively, we could return the set of type definitions directly.
    //       However, currently, this would require to have versions of [[typeD]].
    /** desugars a defined type for each addressability modifier to register them with [[definedTypes]] */
    def desugarAllTypeDefVariants(decl: PTypeDef): Unit = {
      typeD(DeclaredT(decl, info), Addressability.Shared)(meta(decl, info))
      typeD(DeclaredT(decl, info), Addressability.Exclusive)(meta(decl, info))
    }

    def functionD(decl: PFunctionDecl): in.FunctionMember =
      if (decl.spec.isPure) pureFunctionD(decl) else {

      val name = functionProxyD(decl, info)
      val fsrc = meta(decl, info)
      val functionInfo = functionMemberOrLitD(decl, fsrc, new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)))

      in.Function(name, functionInfo.args, functionInfo.results, functionInfo.pres, functionInfo.posts,
        functionInfo.terminationMeasures, functionInfo.body)(fsrc)
    }

    private case class FunctionInfo(args: Vector[in.Parameter.In],
                                    captured: Vector[(in.Expr, in.Parameter.In)],
                                    results: Vector[in.Parameter.Out],
                                    pres: Vector[in.Assertion],
                                    posts: Vector[in.Assertion],
                                    terminationMeasures: Vector[in.TerminationMeasure],
                                    body: Option[in.MethodBody])

    private def functionMemberOrLitD(decl: PFunctionOrClosureDecl, fsrc: Meta, outerCtx: FunctionContext): FunctionInfo = {
      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, argSubs) = argsWithSubs.unzip

      val captured = decl match {
        case d: PClosureDecl => info.capturedVariables(d)
        case _ => Vector.empty
      }
      val capturedWithSubs = captured map capturedVarD
      val (capturedPar, capturedSubs) = capturedWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p, i, info) }
      val returnsMergedWithSubs = returnsWithSubs.map{ case (p,s) => s.getOrElse(p) }
      val (returns, returnSubs) = returnsWithSubs.unzip

      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt = {
        if (rets.isEmpty) {
          in.Return()(src)
        } else if (rets.size == returns.size) {
          in.Seqn(
            returnsMergedWithSubs.zip(rets).map{
              case (p, v) => singleAss(in.Assignee.Var(p), v)(src)
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == 1) { // multi assignment
          in.Seqn(Vector(
            multiassD(returnsMergedWithSubs.map(v => in.Assignee.Var(v)), rets.head, decl)(src),
            in.Return()(src)
          ))(src)
        } else {
          violation(s"found ${rets.size} returns but expected 0, 1, or ${returns.size}")
        }
      }

      // create context for spec translation
      val specCtx = outerCtx.copyWith(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach {
        // substitution has to be added since otherwise the parameter is translated as a addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p, info)
        case _ =>
      }

      // replace captured variables in function literals
      (captured zip capturedWithSubs).foreach {
        // we use a newly-generated pointer parameter p to replace captured variable v (v -> *p)
        // p is treated as a normal argument, information about the original variable is kept in the function literal object
        case (v, (p, _)) => val src = meta(v, info)
          specCtx.addSubst(v, in.Deref(p, underlyingType(p.typ))(src), info)
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p, info)
        case _ =>
      }

      // translate pre- and postconditions and termination measures
      val pres = (decl.spec.pres ++ decl.spec.preserves) map preconditionD(specCtx, info)
      val posts = (decl.spec.preserves ++ decl.spec.posts) map postconditionD(specCtx, info)
      val terminationMeasures = sequence(decl.spec.terminationMeasures map terminationMeasureD(specCtx, info)).res

      // p1' := p1; ... ; pn' := pn
      val argInits = argsWithSubs.flatMap{
        case (p, Some(q)) => Some(singleAss(in.Assignee.Var(q), p)(p.info))
        case _ => None
      }

      // c1' := c1; ...; cn' := cn
      val capturedInits = capturedWithSubs.map{
        case (p, q) => singleAss(in.Assignee.Var(q), p)(p.info)
      }

      // r1 := r1'; .... rn := rn'
      val resultAssignments =
        returnsWithSubs.flatMap{
          case (p, Some(v)) => Some(singleAss(in.Assignee.Var(p), v)(fsrc))
          case _ => None
        } // :+ in.Return()(fsrc)

      // create context for body translation
      val ctx =  outerCtx.copyWith(assignReturns)

      // extent context
      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q, info)
        case _ =>
      }

      (captured zip capturedSubs).foreach {
        case (v, p) => val src = meta(v, info)
          ctx.addSubst(v, in.Deref(p, underlyingType(p.typ))(src), info)
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q, info)
        case (NoGhost(_: PUnnamedParameter), (_, Some(_))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }

      val capturedWithAliases = (captured.map { v => in.Ref(localVarD(outerCtx, info)(v))(meta(v, info)) } zip capturedPar)

      val bodyOpt = decl.body.map{ case (_, s) =>
        val vars = argSubs.flatten ++ capturedSubs ++ returnSubs.flatten
        val varsInit = vars map (v => in.Initialization(v)(v.info))
        val body = varsInit ++ argInits ++ capturedInits ++ Vector(blockD(ctx, info)(s))
        in.MethodBody(vars, in.MethodBodySeqn(body)(fsrc), resultAssignments)(fsrc)
      }

      FunctionInfo(args, capturedWithAliases, returns, pres, posts, terminationMeasures, bodyOpt)
    }

    def pureFunctionD(decl: PFunctionDecl): in.PureFunction = {
      val name = functionProxyD(decl, info)
      val fsrc = meta(decl, info)
      val funcInfo = pureFunctionMemberOrLitD(decl, fsrc, new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)), info)

      in.PureFunction(name, funcInfo.args, funcInfo.results, funcInfo.pres, funcInfo.posts, funcInfo.terminationMeasures, funcInfo.body)(fsrc)
    }

    private case class PureFunctionInfo(args: Vector[in.Parameter.In],
                                        captured: Vector[(in.Expr, in.Parameter.In)],
                                        results: Vector[in.Parameter.Out],
                                        pres: Vector[in.Assertion],
                                        posts: Vector[in.Assertion],
                                        terminationMeasures: Vector[in.TerminationMeasure],
                                        body: Option[in.Expr])


    private def pureFunctionMemberOrLitD(decl: PFunctionOrClosureDecl, fsrc: Meta, outerCtx: FunctionContext, info: TypeInfo): PureFunctionInfo = {
      require(decl.spec.isPure)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, _) = argsWithSubs.unzip

      val captured = decl match {
        case d: PClosureDecl => info.capturedVariables(d)
        case _ => Vector.empty
      }
      val capturedWithSubs = captured map capturedVarD
      val (capturedPar, _) = capturedWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p, i, info) }
      val (returns, _) = returnsWithSubs.unzip

      // create context for body translation
      val dummyRet: Vector[in.Expr] => Meta => in.Stmt = _ => _ => in.Seqn(Vector.empty)(fsrc)
      val ctx = outerCtx.copyWith(dummyRet)

      // extent context
      (decl.args zip argsWithSubs).foreach {
        // substitution has to be added since otherwise the parameter is translated as a addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p, info)
        case _ =>
      }

      (captured zip capturedWithSubs).foreach {
        case (v, (p, _)) => val src = meta(v, info)
          ctx.addSubst(v, in.Deref(p, underlyingType(p.typ))(src), info)
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p, info)
        case _ =>
      }

      // translate pre- and postconditions and termination measures
      val pres = decl.spec.pres map preconditionD(ctx, info)
      val posts = decl.spec.posts map postconditionD(ctx, info)
      val terminationMeasure = sequence(decl.spec.terminationMeasures map terminationMeasureD(ctx, info)).res

      val capturedWithAliases = (captured.map { v => in.Ref(localVarD(outerCtx, info)(v))(meta(v, info)) } zip capturedPar)

      val bodyOpt = decl.body.map {
        case (_, b: PBlock) =>
          val res = b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx, info)(ret)
            case b => Violation.violation(s"unexpected pure function body: $b")
          }
          implicitConversion(res.typ, returns.head.typ, res)
      }

      PureFunctionInfo(args, capturedWithAliases, returns, pres, posts, terminationMeasure, bodyOpt)
    }


    def methodD(decl: PMethodDecl): in.MethodMember =
      if (decl.spec.isPure) pureMethodD(decl) else {

      val name = methodProxyD(decl, info)
      val fsrc = meta(decl, info)

      val recvWithSubs = receiverD(decl.receiver, info)
      val (recv, recvSub) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, argSubs) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p, i, info) }
      val returnsMergedWithSubs = returnsWithSubs.map{ case (p,s) => s.getOrElse(p) }
      val (returns, returnSubs) = returnsWithSubs.unzip

      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt = {
        if (rets.isEmpty) {
          in.Return()(src)
        } else if (rets.size == returns.size) {
          in.Seqn(
            returnsMergedWithSubs.zip(rets).map{
              case (p, v) => singleAss(in.Assignee.Var(p), v)(src)
            } :+ in.Return()(src)
          )(src)
        } else if (rets.size == 1) { // multi assignment
          in.Seqn(Vector(
            multiassD(returnsMergedWithSubs.map(v => in.Assignee.Var(v)), rets.head, decl)(src),
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
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p, info)
        case _ =>
      }

      (decl.receiver, recvWithSubs) match {
        case (NoGhost(PNamedReceiver(id, _, _)), (p, _)) => specCtx.addSubst(id, p, info)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => specCtx.addSubst(id, p, info)
        case _ =>
      }

      // translate pre- and postconditions and termination measures
      val pres = (decl.spec.pres ++ decl.spec.preserves) map preconditionD(specCtx, info)
      val posts = (decl.spec.preserves ++ decl.spec.posts) map postconditionD(specCtx, info)
      val terminationMeasure = sequence(decl.spec.terminationMeasures map terminationMeasureD(specCtx, info)).res

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
        case (PNamedReceiver(id, _, _), (_, Some(q))) => ctx.addSubst(id, q, info)
        case _ =>
      }

      (decl.args zip argsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q, info)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach{
        case (NoGhost(PNamedParameter(id, _)), (_, Some(q))) => ctx.addSubst(id, q, info)
        case (NoGhost(_: PUnnamedParameter), (_, Some(_))) => violation("cannot have an alias for an unnamed parameter")
        case _ =>
      }


      val bodyOpt = decl.body.map{ case (_, s) =>
        val vars = recvSub.toVector ++ argSubs.flatten ++ returnSubs.flatten
        val varsInit = vars map (v => in.Initialization(v)(v.info))
        val body = varsInit ++ recvInits ++ argInits ++ Vector(blockD(ctx, info)(s))
        in.MethodBody(vars, in.MethodBodySeqn(body)(fsrc), resultAssignments)(fsrc)
      }

      in.Method(recv, name, args, returns, pres, posts, terminationMeasure, bodyOpt)(fsrc)
    }

    def pureMethodD(decl: PMethodDecl): in.PureMethod = {
      require(decl.spec.isPure)

      val name = methodProxyD(decl, info)
      val fsrc = meta(decl, info)

      val recvWithSubs = receiverD(decl.receiver, info)
      val (recv, _) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, _) = argsWithSubs.unzip

      val returnsWithSubs = decl.result.outs.zipWithIndex map { case (p,i) => outParameterD(p, i, info) }
      val (returns, _) = returnsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      // extent context
      (decl.args zip argsWithSubs).foreach{
        // substitution has to be added since otherwise the parameter is translated as an addressable variable
        // TODO: another, maybe more consistent, option is to always add a context entry
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p, info)
        case _ =>
      }

      (decl.receiver, recvWithSubs) match {
        case (NoGhost(PNamedReceiver(id, _, _)), (p, _)) => ctx.addSubst(id, p, info)
        case _ =>
      }

      (decl.result.outs zip returnsWithSubs).foreach {
        case (NoGhost(PNamedParameter(id, _)), (p, _)) => ctx.addSubst(id, p, info)
        case _ =>
      }

      // translate pre- and postconditions
      val pres = (decl.spec.pres ++ decl.spec.preserves) map preconditionD(ctx, info)
      val posts = (decl.spec.preserves ++ decl.spec.posts) map postconditionD(ctx, info)
      val terminationMeasure = sequence(decl.spec.terminationMeasures map terminationMeasureD(ctx, info)).res

      val bodyOpt = decl.body.map {
        case (_, b: PBlock) =>
          val res = b.nonEmptyStmts match {
            case Vector(PReturn(Vector(ret))) => pureExprD(ctx, info)(ret)
            case s => Violation.violation(s"unexpected pure function body: $s")
          }
          implicitConversion(res.typ, returns.head.typ, res)
      }

      in.PureMethod(recv, name, args, returns, pres, posts, terminationMeasure, bodyOpt)(fsrc)
    }

    def fpredicateD(decl: PFPredicateDecl): in.FPredicate = {
      val name = fpredicateProxyD(decl, info)
      val fsrc = meta(decl, info)

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, _) = argsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      val bodyOpt = decl.body.map{ s =>
        specificationD(ctx, info)(s)
      }

      in.FPredicate(name, args, bodyOpt)(fsrc)
    }

    def mpredicateD(decl: PMPredicateDecl): in.MPredicate = {
      val name = mpredicateProxyD(decl, info)
      val fsrc = meta(decl, info)

      val recvWithSubs = receiverD(decl.receiver, info)
      val (recv, _) = recvWithSubs

      val argsWithSubs = decl.args.zipWithIndex map { case (p,i) => inParameterD(p, i, info) }
      val (args, _) = argsWithSubs.unzip

      // create context for body translation
      val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(fsrc)) // dummy assign

      val bodyOpt = decl.body.map{ s =>
        specificationD(ctx, info)(s)
      }

      in.MPredicate(recv, name, args, bodyOpt)(fsrc)
    }



    // Statements

    def maybeStmtD(ctx: FunctionContext)(stmt: Option[PStatement])(src: Source.Parser.Info): Writer[in.Stmt] =
      stmt.map(stmtD(ctx, info)).getOrElse(unit(in.Seqn(Vector.empty)(src)))

    def blockD(ctx: FunctionContext, info: TypeInfo)(block: PBlock): in.Stmt = {
      val dStatements = sequence(block.nonEmptyStmts map (s => seqn(stmtD(ctx, info)(s))))
      blockV(dStatements)(meta(block, info))
    }

    def stmtD(ctx: FunctionContext, info: TypeInfo)(stmt: PStatement): Writer[in.Stmt] = {

      def goS(s: PStatement): Writer[in.Stmt] = stmtD(ctx, info)(s)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)
      def goL(a: PAssignee): Writer[in.Assignee] = assigneeD(ctx)(a)

      val src: Meta = meta(stmt, info)

      /**
        * Desugars the left side of an assignment, short variable declaration, and normal variable declaration.
        * If the left side is an identifier definition, a variable declaration and initialization is written, as well.
        */
      def leftOfAssignmentD(idn: PIdnNode, info: TypeInfo)(t: in.Type): Writer[in.Assignee] = {
        val isDef = idn match {
          case _: PIdnDef => true
          case unk: PIdnUnk if info.isDef(unk) => true
          case _ => false
        }

        idn match {
          case _: PWildcard => freshDeclaredExclusiveVar(t.withAddressability(Addressability.Exclusive), idn, info)(src).map(in.Assignee.Var)

          case _ =>
            val x = assignableVarD(ctx, info)(idn)
            if (isDef) {
              val v = x.asInstanceOf[in.LocalVar]
              for {
                _ <- declare(v)
                _ <- write(in.Initialization(v)(src))
              } yield in.Assignee(v)
            } else unit(in.Assignee(x))
        }
      }

      val result = stmt match {
        case NoGhost(noGhost) => noGhost match {
          case _: PEmptyStmt => unit(in.Seqn(Vector.empty)(src))

          case s: PSeq => for {ss <- sequence(s.nonEmptyStmts map goS)} yield in.Seqn(ss)(src)

          case b: PBlock => unit(blockD(ctx, info)(b))

          case l: PLabeledStmt => {
            val proxy = labelProxy(l.label)
            for {
              _ <- declare(proxy)
              _ <- write(in.Label(proxy)(src))
              s <- goS(l.stmt)
            } yield s
          }

          case PIfStmt(ifs, els) =>
            val elsStmt = maybeStmtD(ctx)(els)(src)
            unit(block( // is a block because 'pre' might define new variables
              ifs.foldRight(elsStmt){
                case (PIfClause(pre, cond, body), c) =>
                  for {
                    dPre <- maybeStmtD(ctx)(pre)(src)
                    dCond <- exprD(ctx, info)(cond)
                    dBody = blockD(ctx, info)(body)
                    els <- seqn(c)
                  } yield in.Seqn(Vector(dPre, in.If(dCond, dBody, els)(src)))(src)
              }
            ))

          case n@PForStmt(pre, cond, post, spec, body) =>
            unit(block( // is a block because 'pre' might define new variables
              for {
                dPre <- maybeStmtD(ctx)(pre)(src)
                (dCondPre, dCond) <- prelude(exprD(ctx, info)(cond))
                (dInvPre, dInv) <- prelude(sequence(spec.invariants map assertionD(ctx, info)))
                (dTerPre, dTer) <- prelude(option(spec.terminationMeasure map terminationMeasureD(ctx, info)))

                continueLabelName = nm.continueLabel(n, info)
                continueLoopLabelProxy = in.LabelProxy(continueLabelName)(src)
                continueLoopLabel = in.Label(continueLoopLabelProxy)(src)

                breakLabelName = nm.breakLabel(n, info)
                breakLoopLabelProxy = in.LabelProxy(breakLabelName)(src)
                _ <- declare(breakLoopLabelProxy)
                breakLoopLabel = in.Label(breakLoopLabelProxy)(src)

                dBody = blockD(ctx, info)(body)
                dPost <- maybeStmtD(ctx)(post)(src)

                wh = in.Seqn(
                  Vector(dPre) ++ dCondPre ++ dInvPre ++ dTerPre ++ Vector(
                    in.While(dCond, dInv, dTer, in.Block(Vector(continueLoopLabelProxy),
                      Vector(dBody, continueLoopLabel, dPost) ++ dCondPre ++ dInvPre ++ dTerPre
                    )(src))(src), breakLoopLabel
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
                val temps = left map (l => freshExclusiveVar(typeD(info.typ(l), Addressability.exclusiveVariable)(src), stmt, info)(src))
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
                yield multiassD(les, re, stmt)(src)
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
                  case PBitAndOp() => in.BitAnd(l.op, r)(src)
                  case PBitOrOp() => in.BitOr(l.op, r)(src)
                  case PBitXorOp() => in.BitXor(l.op, r)(src)
                  case PBitClearOp() => in.BitClear(l.op, r)(src)
                  case PShiftLeftOp() => in.ShiftLeft(l.op, r)(src)
                  case PShiftRightOp() => in.ShiftRight(l.op, r)(src)
                }
              } yield singleAss(l, rWithOp)(src)

          case PShortVarDecl(right, left, _) =>
            if (left.size == right.size) {
              seqn(sequence((left zip right).map{ case (l, r) =>
                for {
                  re <- goE(r)
                  le <- leftOfAssignmentD(l, info)(re.typ)
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src)))
            } else if (right.size == 1) {
              seqn(for {
                re  <- goE(right.head)
                les <- sequence(left.map{ l =>
                  for {
                    dL <- leftOfAssignmentD(l, info)(typeD(info.typ(l), Addressability.exclusiveVariable)(src))
                  } yield dL
                })
              } yield multiassD(les, re, stmt)(src))
            } else { violation("invalid assignment") }

          case PVarDecl(typOpt, right, left, _) =>

            if (left.size == right.size) {
              seqn(sequence((left zip right).map{ case (l, r) =>
                for {
                  re <- goE(r)
                  typ = typOpt.map(x => typeD(info.symbType(x), Addressability.exclusiveVariable)(src)).getOrElse(re.typ)
                  dL <- leftOfAssignmentD(l, info)(typ)
                  le <- unit(dL)
                } yield singleAss(le, re)(src)
              }).map(in.Seqn(_)(src)))
            } else if (right.size == 1) {
              seqn(for {
                re  <- goE(right.head)
                les <- sequence(left.map{l =>
                  for {
                    dL <- leftOfAssignmentD(l, info)(re.typ)
                  } yield dL
                })
              } yield multiassD(les, re, stmt)(src))
            } else if (right.isEmpty && typOpt.nonEmpty) {
              val typ = typeD(info.symbType(typOpt.get), Addressability.exclusiveVariable)(src)
              val lelems = sequence(left.map{ l =>
                for {
                  dL <- leftOfAssignmentD(l, info)(typ)
                } yield dL
              })
              val relems = left.map{ l => in.DfltVal(typeD(info.symbType(typOpt.get), Addressability.defaultValue)(meta(l, info)))(meta(l, info)) }
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
              dExp <- exprD(ctx, info)(exp)
              exprVar <- freshDeclaredExclusiveVar(dExp.typ.withAddressability(Addressability.exclusiveVariable), stmt, info)(dExp.info)
              exprAss = singleAss(in.Assignee.Var(exprVar), dExp)(dExp.info)
              _ <- write(exprAss)
              clauses <- sequence(cases.map(c => switchCaseD(c, exprVar)(ctx)))

              dfltStmt <- if (dflt.size > 1) {
                violation("switch statement has more than one default clause")
              } else if (dflt.size == 1) {
                stmtD(ctx, info)(dflt(0))
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

            exp match {
              case inv: PInvoke =>
                info.resolve(inv) match {
                  case Some(p: ap.FunctionCall) =>
                    // No closure calls for now
                    functionCallDAux(ctx, info)(p, inv)(src) map {
                      case Left((_, call: in.FunctionCall)) =>
                        in.GoFunctionCall(call.func, call.args)(src)
                      case Left((_, call: in.MethodCall)) =>
                        in.GoMethodCall(call.recv, call.meth, call.args)(src)
                      case Right(call: in.PureFunctionCall) =>
                        in.GoFunctionCall(call.func, call.args)(src)
                      case Right(call: in.PureMethodCall) =>
                        in.GoMethodCall(call.recv, call.meth, call.args)(src)
                      case _ => unexpectedExprError(exp)
                    }
                  case _ => unexpectedExprError(exp)
                }
              case _ => unexpectedExprError(exp)
            }

          case PDeferStmt(exp) =>
            def unexpectedExprError(exp: PNode) = violation(s"unexpected expression $exp in defer statement")

            exp match {
              case inv: PInvoke =>
                info.resolve(inv) match {
                  case Some(p: ap.FunctionLikeCall) =>
                    functionLikeCallDAux(ctx, info)(p, inv)(src) map {
                      case Left((_, call: in.Deferrable)) => in.Defer(call)(src)
                      case _ => unexpectedExprError(exp)
                    }
                  case _ => unexpectedExprError(exp)
                }

              case exp: PStatement =>
                stmtD(ctx, info)(exp) map {
                  case d: in.Deferrable => in.Defer(d)(src)
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

          case PTypeSwitchStmt(pre, exp, binder, cases, dflt) =>
            for {
              dPre <- maybeStmtD(ctx)(pre)(src)
              dExp <- exprD(ctx, info)(exp)
              exprVar <- freshDeclaredExclusiveVar(dExp.typ.withAddressability(Addressability.exclusiveVariable), stmt, info)(dExp.info)
              exprAss = singleAss(in.Assignee.Var(exprVar), dExp)(dExp.info)
              _ <- write(exprAss)
              clauses <- sequence(cases.map(c => typeSwitchCaseD(c, exprVar, binder)(ctx)))

              dfltStmt <- if (dflt.size > 1) {
                violation("switch statement has more than one default clause")
              } else if (dflt.size == 1) {
                stmtD(ctx, info)(dflt(0))
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

          case n: POutline =>
            val name = s"${rootName(n, info)}$$${nm.relativeId(n, info)}"
            val pres = (n.spec.pres ++ n.spec.preserves) map preconditionD(ctx, info)
            val posts = (n.spec.preserves ++ n.spec.posts) map postconditionD(ctx, info)
            val terminationMeasures = sequence(n.spec.terminationMeasures map terminationMeasureD(ctx, info)).res

            if (!n.spec.isTrusted) {
              for {
                body <- seqn(stmtD(ctx, info)(n.body))
              } yield in.Outline(name, pres, posts, terminationMeasures, body, trusted = false)(src)
            } else {
              val declared = info.freeDeclared(n).map(localVarContextFreeD(_, info))
              // The dummy body preserves the reads and writes of the real body that target free variables.
              // This is done to avoid desugaring the actual body which may fail for trusted code.
              //
              // var arguments' := arguments
              // modified := dflt
              //  where
              //    arguments is the set of free variables in the body
              //    modified  is the set of free variables in the body that are modified
              val dummyBody = {
                val arguments = info.freeVariables(n).map(localVarContextFreeD(_, info))
                val modified = info.freeModified(n).map(localVarContextFreeD(_, info)).filter(_.typ.addressability.isExclusive)
                val argumentsCopy = arguments map (v => v.copy(id = s"${v.id}$$copy")(src))
                in.Block(
                  argumentsCopy,
                  (argumentsCopy zip arguments).map { case (l, r) => in.SingleAss(in.Assignee.Var(l), r)(src) } ++
                    modified.map(l => in.SingleAss(in.Assignee.Var(l), in.DfltVal(l.typ)(src))(src))
                )(src)
              }

              for {
                // since the body of an outline is not a separate scope, we have to preserve variable declarations.
                _ <- declare(declared:_*)
              } yield in.Outline(name, pres, posts, terminationMeasures, dummyBody, trusted = true)(src)
            }


          case n@PContinue(label) => unit(in.Continue(label.map(x => x.name), nm.fetchContinueLabel(n, info))(src))

          case n@PBreak(label) => unit(in.Break(label.map(x => x.name), nm.fetchBreakLabel(n, info))(src))

          case n@PShortForRange(range, shorts, spec, body) =>
            // is a block as it introduces new variables
            unit(block(for {
              exp <- goE(range.exp)
              (elems, typ) = underlyingType(exp.typ) match {
                case slice : in.SliceT => (slice.elems, slice)
                case array : in.ArrayT => (array.elems, array)
                case _ : in.MapT => violation("Maps are not supported yet in range")
                case _ : in.StringT => violation("Strings are not supported yet in range")
                case t => violation(s"Range not applicable to type $t")
              }

              // index is the place where we store the indices of the range expression
              indexLeft <- leftOfAssignmentD(shorts(0), info)(in.IntT(Addressability.exclusiveVariable))
              indexVar = in.Assignee.Var(indexLeft.op.asInstanceOf[in.AssignableVar])
              indexSrc = meta(shorts(0), info)
              rangeSrc = meta(range, info)
              indexAss = singleAss(indexVar, in.IntLit(0)(rangeSrc))(indexSrc)
              incrIndex = singleAss(indexVar, in.Add(indexLeft.op, in.IntLit(1)(indexSrc))(indexSrc))(indexSrc)

              rangeExpSrc = meta(range.exp, info)

              // in go the range expression is only computed once before the iteration begins
              // we do that by storing it in copiedVar
              // this also ensures that the elements iterated through do not change
              // even if the range expression is modified in the loop body
              copiedVar <- freshDeclaredExclusiveVar(exp.typ, n, info)(rangeExpSrc)
              copyAss = singleAss(in.Assignee.Var(copiedVar), exp)(rangeExpSrc)

              // get the length of the expression
              length = in.Length(copiedVar)(rangeSrc)
              cond = in.LessCmp(indexLeft.op, length)(rangeSrc)

              // this invariant states that the index is between 0 and the length of the array/slice (both inclusive)
              indexBoundsInv = in.ExprAssertion(in.And(
                in.AtMostCmp(in.IntLit(0)(rangeSrc), indexLeft.op)(rangeSrc),
                in.AtMostCmp(indexLeft.op, length)(rangeSrc))(rangeSrc))(rangeSrc)

              // standard for loop desugaring
              (dInvPre, dInv) <- prelude(sequence(spec.invariants map assertionD(ctx, info)))
              (dTerPre, dTer) <- prelude(option(spec.terminationMeasure map terminationMeasureD(ctx, info)))

              dBody = blockD(ctx, info)(body)

              continueLabelName = nm.continueLabel(n, info)
              continueLoopLabelProxy = in.LabelProxy(continueLabelName)(src)
              continueLoopLabel = in.Label(continueLoopLabelProxy)(src)

              breakLabelName = nm.breakLabel(n, info)
              breakLoopLabelProxy = in.LabelProxy(breakLabelName)(src)
              _ <- declare(breakLoopLabelProxy)
              breakLoopLabel = in.Label(breakLoopLabelProxy)(src)

              wh = if (shorts.length == 2) {
                // in this case we know that the loop looks like this:
                // for i, j := range x { ...
                // until now we have only created the variable i since it is not mandatory for j to exist
                // if it does, we have to declare it and add the code that will update it in each iteration
                // which looks like this:
                // if i < length(x) { j = x[i] }
                // note that this will happen after we have incremented i
                val valueSrc = meta(shorts(1), info)
                val valueLeft = assignableVarD(ctx, info)(shorts(1))
                val valueVar = in.Assignee.Var(valueLeft.asInstanceOf[in.AssignableVar])
                val valueAss = singleAss(valueVar, in.IndexedExp(copiedVar, in.IntLit(0)(valueSrc), typ)(valueSrc))(valueSrc)
                val updateValue = in.If(cond, singleAss(valueVar, in.IndexedExp(copiedVar, indexLeft.op, typ)(valueSrc))(valueSrc), in.Seqn(Vector())(valueSrc))(valueSrc)

                // we also need an invariant that states that
                // for index i and value j and range expression x
                // invariant 0 <= i && i < len(x) ==> j == x[i]
                val indexValueEq = in.Implication(
                  in.And(
                    in.AtMostCmp(in.IntLit(0)(indexSrc), indexLeft.op)(indexSrc),
                    in.LessCmp(indexLeft.op, length)(indexSrc))(indexSrc),
                  in.ExprAssertion(in.EqCmp(in.IndexedExp(copiedVar, indexLeft.op, typ)(rangeExpSrc), valueLeft)(rangeExpSrc))(rangeExpSrc)
              )(rangeSrc)
                in.Block(
                  Vector(valueLeft.asInstanceOf[in.LocalVar]),
                  Vector(copyAss, indexAss, valueAss) ++ dInvPre ++ dTerPre ++ Vector(
                    in.While(cond, dInv ++ Vector(indexBoundsInv, indexValueEq), dTer, in.Block(Vector(continueLoopLabelProxy),
                      Vector(dBody, continueLoopLabel, incrIndex, updateValue) ++ dInvPre ++ dTerPre
                    )(src))(src), breakLoopLabel
                  )
                )(src)
              } else {
                // else we do not have a value variable and the while loop has only
                // the index in bounds invariant added
                // the loop in this case looks like this:
                // for i := range x { ...
                in.Seqn(
                  Vector(copyAss, indexAss) ++ dInvPre ++ dTerPre ++ Vector(
                    in.While(cond, dInv ++ Vector(indexBoundsInv), dTer, in.Block(Vector(continueLoopLabelProxy),
                      Vector(dBody, continueLoopLabel, incrIndex) ++ dInvPre ++ dTerPre
                    )(src))(src), breakLoopLabel
                  )
                )(src)
              }
            } yield wh))

          case n@PAssForRange(range, ass, spec, body) =>
            // similar logic as PShortForRange
            unit(block(for {
              exp <- goE(range.exp)
              typ = underlyingType(exp.typ) match {
                case slice : in.SliceT => slice
                case array : in.ArrayT => array
                case _ : in.MapT => violation("Maps are not supported yet in range")
                case _ : in.StringT => violation("Strings are not supported yet in range")
                case t => violation(s"Range not applicable to type $t")
              }
              indexLeft <- goL(ass(0))
              rangeSrc = meta(range, info)
              indexSrc = meta(ass(0), info)
              rangeExpSrc = meta(range.exp, info)
              indexAss = singleAss(indexLeft, in.IntLit(0)(rangeSrc))(indexSrc)
              incrIndex = singleAss(indexLeft, in.Add(indexLeft.op, in.IntLit(1)(indexSrc))(indexSrc))(indexSrc)

              copiedVar <- freshDeclaredExclusiveVar(exp.typ, n, info)(rangeExpSrc)
              copyAss = singleAss(in.Assignee.Var(copiedVar), exp)(rangeExpSrc)

              length = in.Length(copiedVar)(rangeSrc)
              cond = in.LessCmp(indexLeft.op, length)(rangeSrc)

              indexBoundsInv = in.ExprAssertion(in.And(
                in.AtMostCmp(in.IntLit(0)(rangeSrc), indexLeft.op)(rangeSrc),
                in.AtMostCmp(indexLeft.op, length)(rangeSrc))(rangeSrc))(rangeSrc)

              (dInvPre, dInv) <- prelude(sequence(spec.invariants map assertionD(ctx, info)))
              (dTerPre, dTer) <- prelude(option(spec.terminationMeasure map terminationMeasureD(ctx, info)))

              dBody = blockD(ctx, info)(body)

              continueLabelName = nm.continueLabel(n, info)
              continueLoopLabelProxy = in.LabelProxy(continueLabelName)(src)
              continueLoopLabel = in.Label(continueLoopLabelProxy)(src)

              breakLabelName = nm.breakLabel(n, info)
              breakLoopLabelProxy = in.LabelProxy(breakLabelName)(src)
              _ <- declare(breakLoopLabelProxy)
              breakLoopLabel = in.Label(breakLoopLabelProxy)(src)

              wh = if (ass.length == 2) {
                val valueLeft = goL(ass(1)).res
                val decls = ass(1) match {
                  case _: PBlankIdentifier => Vector(valueLeft.asInstanceOf[in.Assignee.Var].op.asInstanceOf[in.LocalVar])
                  case _ => Vector.empty
                }
                val valueSrc = meta(ass(1), info)
                val valueAss = singleAss(valueLeft, in.IndexedExp(copiedVar, in.IntLit(0)(valueSrc), typ)(valueSrc))(valueSrc)
                val updateValue = in.If(cond, singleAss(valueLeft, in.IndexedExp(copiedVar, indexLeft.op, typ)(valueSrc))(valueSrc), in.Seqn(Vector())(valueSrc))(valueSrc)
                
                val indexValueEq = in.Implication(
                  in.And(
                    in.AtMostCmp(in.IntLit(0)(indexSrc), indexLeft.op)(indexSrc),
                    in.LessCmp(indexLeft.op, length)(indexSrc))(indexSrc),
                  in.ExprAssertion(in.EqCmp(in.IndexedExp(copiedVar, indexLeft.op, typ)(rangeExpSrc), valueLeft.op)(rangeExpSrc))(rangeExpSrc)
              )(rangeSrc)
                in.Block(
                  decls,
                  Vector(copyAss, indexAss, valueAss) ++ dInvPre ++ dTerPre ++ Vector(
                    in.While(cond, dInv ++ Vector(indexBoundsInv, indexValueEq), dTer, in.Block(Vector(continueLoopLabelProxy),
                      Vector(dBody, continueLoopLabel, incrIndex, updateValue) ++ dInvPre ++ dTerPre
                    )(src))(src), breakLoopLabel
                  )
                )(src)
              } else {
                in.Seqn(
                  Vector(copyAss, indexAss) ++ dInvPre ++ dTerPre ++ Vector(
                    in.While(cond, dInv ++ Vector(indexBoundsInv), dTer, in.Block(Vector(continueLoopLabelProxy),
                      Vector(dBody, continueLoopLabel, incrIndex) ++ dInvPre ++ dTerPre
                    )(src))(src), breakLoopLabel
                  )
                )(src)
              }
            } yield wh))

          case _ => ???
        }
      }
      seqn(result)
    }

    def switchCaseD(switchCase: PExprSwitchCase, scrutinee: in.AssignableVar)(ctx: FunctionContext): Writer[(in.Expr, in.Stmt)] = {
      val left = switchCase.left
      val body = switchCase.body
      for {
        acceptExprs <- sequence(left.map(clause => exprD(ctx, info)(clause)))
        acceptCond = acceptExprs.foldRight(in.BoolLit(b = false)(meta(switchCase, info)): in.Expr){
          (expr, tail) => in.Or(in.EqCmp(scrutinee, expr)(expr.info), tail)(expr.info)
        }
        stmt <- stmtD(ctx, info)(body)
      } yield (acceptCond, stmt)
    }

    def typeSwitchCaseD(switchCase: PTypeSwitchCase, scrutinee: in.AssignableVar, bind: Option[PIdnDef])(ctx: FunctionContext): Writer[(in.Expr, in.Stmt)] = {
      val left = switchCase.left
      val body = switchCase.body
      val metaCase = meta(switchCase, info)
      for {
        acceptExprs <- sequence(left.map {
          case t: PType => underlyingType(info.symbType(t)) match {
            case _: Type.InterfaceT => violation(s"Interface Types are not supported in case clauses yet, but got $t")
            case _ => for { e <- exprAndTypeAsExpr(ctx, info)(t) } yield in.EqCmp(in.TypeOf(scrutinee)(meta(t, info)), e)(metaCase)
          }
          case n: PNilLit => for { e <- exprAndTypeAsExpr(ctx, info)(n) } yield in.EqCmp(scrutinee, e)(metaCase)
          case n => violation(s"Expected either a type or nil, but got $n instead")
        })
        acceptCond = acceptExprs.foldRight(in.BoolLit(b = false)(metaCase): in.Expr) {
          (expr, tail) => in.Or(expr, tail)(expr.info)
        }
        // In clauses with a case listing exactly one type, the variable has that type;
        // otherwise, the variable has the type of the expression in the TypeSwitchGuard
        assign = for {
          bId <- bind
          typ = left match {
            case Vector(t: PType) => typeD(info.symbType(t), Addressability.rValue)(Source.Parser.Internal)
            case Vector(_: PNilLit) => scrutinee.typ
            case l if l.length > 1 => scrutinee.typ
            case c => violation(s"This case should be unreachable, but got $c")
          }
          name = idName(bId, info)
        } yield in.LocalVar(name, typ)(Source.Parser.Internal)

        context = (bind, assign) match {
          case (Some(id), Some(v)) =>
            val newCtx = ctx.copy
            newCtx.addSubst(id, v, info)
            newCtx
          case (None, None) => ctx
          case c => violation(s"This case should be unreachable, but got $c")
        }

        init = assign.map(v => in.SingleAss(in.Assignee.Var(v), in.TypeAssertion(scrutinee, v.typ)(v.info))(v.info)).toVector

        stmt = blockD(context, info)(body) match {
          case in.Block(decls, stmts) => in.Block(assign.toVector ++ decls, init ++ stmts)(meta(body, info))
          case c => violation(s"Expected Block as result from blockD, but got $c")
        }
      } yield (acceptCond, stmt)
    }

    def multiassD(lefts: Vector[in.Assignee], right: in.Expr, astCtx: PNode)(src: Source.Parser.Info): in.Stmt = {

      right match {
        case in.Tuple(args) if args.size == lefts.size =>
          in.Seqn(lefts.zip(args) map { case (l, r) => singleAss(l, r)(src)})(src)

        case n: in.TypeAssertion if lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable), astCtx, info)(src)
          val successTarget = freshExclusiveVar(lefts(1).op.typ.withAddressability(Addressability.exclusiveVariable), astCtx, info)(src)
          in.Block(
            Vector(resTarget, successTarget),
            Vector( // declare for the fresh variables is not necessary because they are put into a block
              in.SafeTypeAssertion(resTarget, successTarget, n.exp, n.arg)(n.info),
              singleAss(lefts(0), resTarget)(src),
              singleAss(lefts(1), successTarget)(src)
            )
          )(src)

        case n: in.Receive if lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable), astCtx, info)(src)
          val successTarget = freshExclusiveVar(in.BoolT(Addressability.exclusiveVariable), astCtx, info)(src)
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

        case l@ in.IndexedExp(base, _, _) if base.typ.isInstanceOf[in.MapT] && lefts.size == 2 =>
          val resTarget = freshExclusiveVar(lefts(0).op.typ.withAddressability(Addressability.exclusiveVariable), astCtx, info)(src)
          val successTarget = freshExclusiveVar(in.BoolT(Addressability.exclusiveVariable), astCtx, info)(src)
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

    def derefD(ctx: FunctionContext, info: TypeInfo)(p: ap.Deref)(src: Meta): Writer[in.Deref] = {
      exprD(ctx, info)(p.base).map(e => in.Deref(e, underlyingType(e.typ))(src))
    }

    def fieldSelectionD(ctx: FunctionContext, info: TypeInfo)(p: ap.FieldSelection)(src: Meta): Writer[in.FieldRef] = {
      for {
        r <- exprD(ctx, info)(p.base)
        base = applyMemberPathD(r, p.path)(src)
        f = structMemberD(p.symb, Addressability.fieldLookup(base.typ.addressability))(src)
      } yield in.FieldRef(base, f)(src)
    }

    def functionLikeCallD(ctx: FunctionContext, info: TypeInfo)(p: ap.FunctionLikeCall, expr: PInvoke)(src: Meta): Writer[in.Expr] = {
      functionLikeCallDAux(ctx, info)(p, expr)(src) flatMap {
        case Right(exp) => unit(exp)
        case Left((targets, callStmt)) =>
          val res = if (targets.size == 1) targets.head else in.Tuple(targets)(src) // put returns into a tuple if necessary
          for {
            _ <- declare(targets: _*)
            _ <- write(callStmt)
          } yield res
      }
    }

    def functionLikeCallDAux(ctx: FunctionContext, info: TypeInfo)(p: ap.FunctionLikeCall, expr: PInvoke)(src: Meta): Writer[Either[(Vector[in.LocalVar], in.Stmt), in.Expr]] = p match {
      case p: ap.FunctionCall => functionCallDAux(ctx, info)(p, expr)(src)
      case _: ap.ClosureCall => closureCallDAux(ctx, info)(expr)(src)
    }

    /** Returns either a tuple with targets and call statement or, if the call is pure, the call expression directly. */
    def functionCallDAux(ctx: FunctionContext, info: TypeInfo)(p: ap.FunctionCall, expr: PInvoke)(src: Meta): Writer[Either[(Vector[in.LocalVar], in.Stmt), in.Expr]] = {
      def getBuiltInFuncType(f: ap.BuiltInFunctionKind): FunctionT = {
        val abstractType = info.typ(f.symb.tag)
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

      def functionCall(targets: Vector[in.LocalVar], func: ap.FunctionKind, args: Vector[in.Expr], spec: Option[in.ClosureSpec]): in.Stmt = spec match {
        case Some(spec) =>
          val funcObject = in.FunctionObject(getFunctionProxy(func, args), typeD(info.typ(func.id), Addressability.rValue)(src))(src)
          in.ClosureCall(targets, funcObject, args, spec)(src)
        case _ => in.FunctionCall(targets, getFunctionProxy(func, args), args)(src)
      }

      def pureFunctionCall(func: ap.FunctionKind, args: Vector[in.Expr], spec: Option[in.ClosureSpec], resT: in.Type): in.Expr = spec match {
        case Some(spec) =>
          val funcObject = in.FunctionObject(getFunctionProxy(func, args), typeD(info.typ(func.id), Addressability.rValue)(src))(src)
          in.PureClosureCall(funcObject, args, spec, resT)(src)
        case _ => in.PureFunctionCall(getFunctionProxy(func, args), args, resT)(src)
      }

      def getMethodProxy(f: ap.FunctionKind, recv: in.Expr, args: Vector[in.Expr]): in.MethodProxy = f match {
        case ap.ReceivedMethod(_, id, _, _) => methodProxy(id, info)
        case ap.MethodExpr(_, id, _, _) => methodProxy(id, info)
        case bm: ap.BuiltInMethodKind => methodProxy(bm.symb.tag, recv.typ, args.map(_.typ))(src)
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }

      def methodCall(targets: Vector[in.LocalVar], recv: in.Expr, meth: in.MethodProxy, args: Vector[in.Expr], resT: in.Type, spec: Option[in.ClosureSpec]): in.Stmt = spec match {
        case Some(spec) =>
          val resType = resT match {
            case in.TupleT(ts, _) => ts
            case _ => Vector(resT)
          }
          val methObject = in.MethodObject(recv, meth, in.FunctionT(args.map(_.typ), resType, Addressability.rValue))(src)
          in.ClosureCall(targets, methObject, args, spec)(src)
        case _ => in.MethodCall(targets, recv, meth, args)(src)
      }

      def pureMethodCall(recv: in.Expr, meth: in.MethodProxy, args: Vector[in.Expr], spec: Option[in.ClosureSpec], resT: in.Type): in.Expr = spec match {
        case Some(spec) =>
          val resType = resT match {
            case in.TupleT(ts, _) => ts
            case _ => Vector(resT)
          }
          val methObject = in.MethodObject(recv, meth, in.FunctionT(args.map(_.typ), resType, Addressability.rValue))(src)
          in.PureClosureCall(methObject, args, spec, resT)(src)
        case _ => in.PureMethodCall(recv, meth, args, resT)(src)
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

        functionCallArgsD(p.args, params)(ctx, info, src)
      }

      // encode results
      val (resT, targets) = p.callee match {
        // `BuiltInFunctionKind` has to be checked first since it implements `Symbolic` as well
        case f: ap.BuiltInFunctionKind =>
          val ft = getBuiltInFuncType(f)
          val resT = typeD(ft.result, Addressability.callResult)(src)
          val targets = resT match {
            case in.VoidT => Vector()
            case _ => Vector(freshExclusiveVar(resT, expr, info)(src))
          }
          (resT, targets)
        case base: ap.Symbolic => base.symb match {
          case fsym: st.WithResult =>
            val resT = typeD(fsym.context.typ(fsym.result), Addressability.callResult)(src)
            val targets = fsym.result.outs map (o => freshExclusiveVar(typeD(fsym.context.symbType(o.typ), Addressability.exclusiveVariable)(src), expr, info)(src))
            (resT, targets)
          case c => Violation.violation(s"This case should be unreachable, but got $c")
        }
      }

      val isPure = p.callee match {
        case base: ap.Symbolic => base.symb match {
          case f: st.Function => f.isPure
          case c: st.Closure => c.isPure
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
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Right(pureFunctionCall(base, convertedArgs, spec, resT))
            } else {
              for {
                args <- dArgs
                convertedArgs = convertArgs(args)
                fproxy = getFunctionProxy(base, convertedArgs)
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Left((targets, functionCall(targets, base, convertedArgs, spec)))
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
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Right(pureMethodCall(implicitThisD(recvType)(src), proxy, args, spec, resT))
            } else {
              for {
                args <- dArgs
                convertedArgs = convertArgs(args)
                proxy = methodProxy(iim.id, iim.symb.context.getTypeInfo)
                recvType = typeD(iim.symb.itfType, Addressability.receiver)(src)
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Left((targets, methodCall(targets, implicitThisD(recvType)(src), proxy, convertedArgs, resT, spec)))
            }

          case df: ap.DomainFunction =>
            for {
              args <- dArgs
              convertedArgs = convertArgs(args)
              proxy = domainFunctionProxy(df.symb)
            } yield Right(in.DomainFunctionCall(proxy, convertedArgs, resT)(src))

          case _: ap.ReceivedMethod | _: ap.MethodExpr | _: ap.BuiltInReceivedMethod | _: ap.BuiltInMethodExpr => {
            val dRecvWithArgs = base match {
              case base: ap.ReceivedMethod =>
                for {
                  r <- exprD(ctx, info)(base.recv)
                  args <- dArgs
                } yield (applyMemberPathD(r, base.path)(src), args)
              case base: ap.MethodExpr =>
                // first argument is the receiver, the remaining arguments are the rest
                dArgs map (args => (applyMemberPathD(args.head, base.path)(src), args.tail))
              case base: ap.BuiltInReceivedMethod =>
                for {
                  r <- exprD(ctx, info)(base.recv)
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
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Right(pureMethodCall(recv, mproxy, convertedArgs, spec, resT))
            } else {
              for {
                (recv, args) <- dRecvWithArgs
                convertedArgs = convertArgs(args)
                mproxy = getMethodProxy(base, recv, convertedArgs)
                spec = p.maybeSpec.map(closureSpecD(ctx, info))
              } yield Left((targets, methodCall(targets, recv, mproxy, convertedArgs, resT, spec)))
            }
          }
          case sym => Violation.violation(s"expected symbol with arguments and result or a built-in entity, but got $sym")
        }
      }
    }

    /** Returns either a tuple with targets and call statement or, if the call is pure, the call expression directly.
      * This method encodes closure calls, i.e. calls of the type c(_) as _, where c is a closure expression (and not a function/method proxy). */
    def closureCallDAux(ctx: FunctionContext, info: TypeInfo)(expr: PInvoke)(src: Meta): Writer[Either[(Vector[in.LocalVar], in.Stmt), in.Expr]] = {
      val (func, isPure) = info.resolve(expr.spec.get.func) match {
        case Some(ap.Function(_, f)) => (f, f.isPure)
        case Some(ap.Closure(_, c)) => (c, c.isPure)
        case _ => violation("expected function or function literal")
      }

      val spec = closureSpecD(ctx, info)(expr.spec.get)
      val params = func.args.zipWithIndex.collect { case (p, idx) if !spec.params.contains(idx + 1) => func.context.typ(p) }

      if (isPure) for {
        dArgs <- functionCallArgsD(expr.args, params)(ctx, info, src)
        args = arguments(dArgs zip params)
        closure <- exprD(ctx, info)(expr.base.asInstanceOf[PExpression])
        resT = typeD(func.context.typ(func.result), Addressability.callResult)(src)
      } yield Right(in.PureClosureCall(closure, arguments(args zip params), spec, resT)(src))
      else for {
        dArgs <- functionCallArgsD(expr.args, params)(ctx, info, src)
        args = arguments(dArgs zip params)
        closure <- exprD(ctx, info)(expr.base.asInstanceOf[PExpression])
        targets = func.result.outs map (o => freshExclusiveVar(typeD(func.context.symbType(o.typ), Addressability.exclusiveVariable)(src), expr, info)(src))
      } yield Left((targets, in.ClosureCall(targets, closure, arguments(args zip params), spec)(src)))
    }


    /** Desugars the arguments to a function call or a call with spec.
      * @param args The expressions passed as arguments
      * @param params The types of the arguments of the callee (or the spec) */
    private def functionCallArgsD(args: Vector[PExpression], params: Vector[Type])(ctx: FunctionContext, info: TypeInfo, src: Meta): Writer[Vector[in.Expr]] = {
      val parameterCount: Int = params.length

      // is of the form Some(x) if the type of the last param is variadic and the type of its elements is x
      val variadicTypeOption: Option[Type] = params.lastOption match {
        case Some(VariadicT(elem)) => Some(elem)
        case _ => None
      }

      val wRes: Writer[Vector[in.Expr]] = sequence(args map exprD(ctx, info)).map {
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
            case Some(t) if underlyingType(t).isInstanceOf[in.SliceT] &&
              len == parameterCount && underlyingType(t).asInstanceOf[in.SliceT].elems == variadicInTyp =>
              // corresponds to the case where an unpacked slice is already passed as an argument
              res
            case Some(in.TupleT(_, _)) if len == 1 && parameterCount == 1 =>
              // supports chaining function calls with variadic functions of one argument
              val argsMap = getArgsMap(res.last.asInstanceOf[in.Tuple].args, variadicInTyp)
              Vector(in.SliceLit(variadicInTyp, argsMap)(src))
            case _ if len >= parameterCount =>
              val argsMap = getArgsMap(res.drop(parameterCount - 1), variadicInTyp)
              res.take(parameterCount - 1) :+ in.SliceLit(variadicInTyp, argsMap)(src)
            case _ if len == parameterCount - 1 =>
              // variadic argument not passed
              res :+ in.NilLit(in.SliceT(variadicInTyp, Addressability.nil))(src)
            case t => violation(s"this case should be unreachable, but got $t")
          }
        } yield argList

        case None => wRes
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
      val c = args zip symb.args.map(param => symb.context.symbType(param.typ))
      arguments(c)
    }

    def arguments(ft: FunctionT, args: Vector[in.Expr]): Vector[in.Expr] = {
      Violation.violation(args.length == ft.args.length, s"expected same number of arguments and types")
      arguments(args zip ft.args)
    }

    def arguments(pt: PredT, args: Vector[in.Expr]): Vector[in.Expr] = {
      Violation.violation(args.length == pt.args.length, s"expected same number of arguments and types")
      arguments(args zip pt.args)
    }

    def arguments(args: Vector[(in.Expr, Type)]): Vector[in.Expr] = {
      val assignments = args.map{ case (from, pTo) => (from, typeD(pTo, Addressability.inParameter)(from.info)) }
      assignments.map{ case (from, to) => implicitConversion(from.typ, to, from) }
    }

    def assigneeD(ctx: FunctionContext)(expr: PExpression): Writer[in.Assignee] = {
      val src: Meta = meta(expr, info)

      info.resolve(expr) match {
        case Some(p: ap.LocalVariable) =>
          unit(in.Assignee(assignableVarD(ctx, info)(p.id)))
        case Some(p: ap.Deref) =>
          derefD(ctx, info)(p)(src) map in.Assignee.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx, info)(p)(src) map in.Assignee.Field
        case Some(p : ap.IndexedExp) =>
          indexedExprD(p.base, p.index)(ctx, info)(src) map in.Assignee.Index
        case Some(ap.BlankIdentifier(decl)) =>
          for {
            dExpr <- exprD(ctx, info)(decl)
            v <- freshDeclaredExclusiveVar(dExpr.typ, expr, info)(src)
          } yield in.Assignee.Var(v)
        case p => Violation.violation(s"unexpected ast pattern $p")
      }
    }

    def addressableD(ctx: FunctionContext, info: TypeInfo)(expr: PExpression): Writer[in.Addressable] = {
      val src: Meta = meta(expr, info)

      info.resolve(expr) match {
        case Some(p: ap.LocalVariable) =>
          varD(ctx, info)(p.id) match {
            case r: in.LocalVar => unit(in.Addressable.Var(r))
            case r: in.Deref => unit(in.Addressable.Pointer(r))
            case r => Violation.violation(s"expected variable reference but got $r")
          }
        case Some(p: ap.Deref) =>
          derefD(ctx, info)(p)(src) map in.Addressable.Pointer
        case Some(p: ap.FieldSelection) =>
          fieldSelectionD(ctx, info)(p)(src) map in.Addressable.Field
        case Some(p: ap.IndexedExp) =>
          indexedExprD(p)(ctx, info)(src) map in.Addressable.Index

        case p => Violation.violation(s"unexpected ast pattern $p ")
      }
    }


    private def indexedExprD(base : PExpression, index : PExpression)(ctx : FunctionContext, info : TypeInfo)(src : Meta) : Writer[in.IndexedExp] = {
      for {
        dbase <- exprD(ctx, info)(base)
        dindex <- exprD(ctx, info)(index)
        baseUnderlyingType = underlyingType(dbase.typ)
      } yield in.IndexedExp(dbase, dindex, baseUnderlyingType)(src)
    }

    def indexedExprD(expr : PIndexedExp)(ctx : FunctionContext, info : TypeInfo) : Writer[in.IndexedExp] =
      indexedExprD(expr.base, expr.index)(ctx, info)(meta(expr, info))
    def indexedExprD(expr : ap.IndexedExp)(ctx : FunctionContext, info : TypeInfo)(src : Meta) : Writer[in.IndexedExp] =
      indexedExprD(expr.base, expr.index)(ctx, info)(src)

    def exprD(ctx: FunctionContext, info: TypeInfo)(expr: PExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)
      def goTExpr(e: PExpressionOrType): Writer[in.Expr] = exprAndTypeAsExpr(ctx, info)(e)

      val src: Meta = meta(expr, info)

      // if expr is a permission and its case is defined in maybePermissionD,
      // then desugaring expr should yield the value returned by that method
      if(info.typ(expr) == PermissionT) {
        val maybePerm = maybePermissionD(ctx, info)(expr)
        if (maybePerm.isDefined) {
          return maybePerm.head
        }
      }

      // if expD is defined by the context, use that to desugar the expression
      ctx.expD(expr) match {
        case Some(res) => return res
        case None =>
      }

      expr match {
        case NoGhost(noGhost) => noGhost match {
          case n: PNamedOperand => info.resolve(n) match {
            case Some(p: ap.Constant) => unit(globalConstD(p.symb)(src))
            case Some(_: ap.LocalVariable) => unit(varD(ctx, info)(n.id))
            case Some(_: ap.NamedType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case Some(_: ap.BuiltInType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case Some(f: ap.Function) =>
              val name = idName(f.id, info)
              unit(in.FunctionObject(in.FunctionProxy(name)(src), typeD(info.typ(f.id), Addressability.rValue)(src))(src))
            case Some(c: ap.Closure) =>
              val name = idName(c.id, info)
              unit(in.ClosureObject(in.FunctionLitProxy(name)(src), typeD(info.typ(c.id), Addressability.rValue)(src))(src))

            case p => Violation.violation(s"encountered unexpected pattern: $p")
          }

          case n: PDeref => info.resolve(n) match {
            case Some(p: ap.Deref) => derefD(ctx, info)(p)(src)
            case Some(p: ap.PointerType) => for { inElem <- goTExpr(p.base) } yield in.PointerTExpr(inElem)(src)
            case p => Violation.violation(s"encountered unexpected pattern: $p")
          }

          case PReference(exp) => exp match {
            // The reference of a literal is desugared to a new call
            case c: PCompositeLit =>
              for {
                c <- compositeLitD(ctx, info)(c)
                v <- freshDeclaredExclusiveVar(in.PointerT(c.typ.withAddressability(Addressability.Shared), Addressability.reference), expr, info)(src)
                _ <- write(in.New(v, c)(src))
              } yield v

            case _ => addressableD(ctx, info)(exp) map (a => in.Ref(a, in.PointerT(a.op.typ, Addressability.reference))(src))
          }

          case n: PDot => info.resolve(n) match {
            case Some(p: ap.FieldSelection) => fieldSelectionD(ctx, info)(p)(src)
            case Some(p: ap.Constant) => unit[in.Expr](globalConstD(p.symb)(src))
            case Some(_: ap.NamedType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case Some(_: ap.BuiltInType) =>
              val name = typeD(info.symbType(n), Addressability.Exclusive)(src).asInstanceOf[in.DefinedT].name
              unit(in.DefinedTExpr(name)(src))
            case Some(f: ap.Function) =>
              val name = idName(f.id, info)
              unit(in.FunctionObject(in.FunctionProxy(name)(src), typeD(info.typ(f.id), Addressability.rValue)(src))(src))
            case Some(m: ap.ReceivedMethod) =>
              for {
                r <- exprD(ctx, info)(m.recv)
              } yield in.MethodObject(applyMemberPathD(r, m.path)(src), methodProxy(m.id, info), typeD(info.typ(n), Addressability.rValue)(src))(src)
            case Some(p) => Violation.violation(s"only field selections, global constants, types and methods can be desugared to an expression, but got $p")
            case _ => Violation.violation(s"could not resolve $n")
          }

          case n: PInvoke => invokeD(ctx, info)(n)

          case n: PTypeAssertion =>
            for {
              inExpr <- go(n.base)
              inArg = typeD(info.symbType(n.typ), Addressability.typeAssertionResult)(src)
            } yield in.TypeAssertion(inExpr, inArg)(src)

          case PNegation(op) => for {o <- go(op)} yield in.Negation(o)(src)

          case PEquals(left, right) =>
            if (info.typOfExprOrType(left) == PermissionT || info.typOfExprOrType(right) == PermissionT) {
              for {
                l <- permissionD(ctx, info)(left.asInstanceOf[PExpression])
                r <- permissionD(ctx, info)(right.asInstanceOf[PExpression])
              } yield in.EqCmp(l, r)(src)
            } else {
              for {
                l <- exprAndTypeAsExpr(ctx, info)(left)
                r <- exprAndTypeAsExpr(ctx, info)(right)
              } yield in.EqCmp(l, r)(src)
            }

          case PUnequals(left, right) =>
            if (info.typOfExprOrType(left) == PermissionT || info.typOfExprOrType(right) == PermissionT) {
              for {
                l <- permissionD(ctx, info)(left.asInstanceOf[PExpression])
                r <- permissionD(ctx, info)(right.asInstanceOf[PExpression])
              } yield in.UneqCmp(l, r)(src)
            } else {
              for {
                l <- exprAndTypeAsExpr(ctx, info)(left)
                r <- exprAndTypeAsExpr(ctx, info)(right)
              } yield in.UneqCmp(l, r)(src)
            }

          case PLess(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx, info)(left); r <- permissionD(ctx, info)(right)} yield in.PermLtCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.LessCmp(l, r)(src)
            }

          case PAtMost(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx, info)(left); r <- permissionD(ctx, info)(right)} yield in.PermLeCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.AtMostCmp(l, r)(src)
            }

          case PGreater(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx, info)(left); r <- permissionD(ctx, info)(right)} yield in.PermGtCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.GreaterCmp(l, r)(src)
            }

          case PAtLeast(left, right) =>
            if (info.typ(left) == PermissionT || info.typ(right) == PermissionT) {
              for {l <- permissionD(ctx, info)(left); r <- permissionD(ctx, info)(right)} yield in.PermGeCmp(l, r)(src)
            } else {
              for {l <- go(left); r <- go(right)} yield in.AtLeastCmp(l, r)(src)
            }

          case PAnd(left, right) => for {l <- go(left); r <- go(right)} yield in.And(l, r)(src)
          case POr(left, right) => for {l <- go(left); r <- go(right)} yield in.Or(l, r)(src)

          case PAdd(left, right) => for {l <- go(left); r <- go(right)} yield in.Add(l, r)(src)
          case PSub(left, right) => for {l <- go(left); r <- go(right)} yield in.Sub(l, r)(src)
          case PMul(left, right) => for {l <- go(left); r <- go(right)} yield in.Mul(l, r)(src)
          case PMod(left, right) => for {l <- go(left); r <- go(right)} yield in.Mod(l, r)(src)
          case PDiv(left, right) => for {l <- go(left); r <- go(right)} yield in.Div(l, r)(src)

          case PBitAnd(left, right) => for {l <- go(left); r <- go(right)} yield in.BitAnd(l, r)(src)
          case PBitOr(left, right) => for {l <- go(left); r <- go(right)} yield in.BitOr(l, r)(src)
          case PBitXor(left, right) => for {l <- go(left); r <- go(right)} yield in.BitXor(l, r)(src)
          case PBitClear(left, right) => for {l <- go(left); r <- go(right)} yield in.BitClear(l, r)(src)
          case PShiftLeft(left, right) => for {l <- go(left); r <- go(right)} yield in.ShiftLeft(l, r)(src)
          case PShiftRight(left, right) => for {l <- go(left); r <- go(right)} yield in.ShiftRight(l, r)(src)
          case PBitNegation(exp) => for {e <- go(exp)} yield in.BitNeg(e)(src)

          case l: PLiteral => litD(ctx, info)(l)

          case PUnfolding(acc, op) =>
            val dAcc = specificationD(ctx, info)(acc).asInstanceOf[in.Access]
            val dOp = pureExprD(ctx, info)(op)
            unit(in.Unfolding(dAcc, dOp)(src))

          case n : PIndexedExp => indexedExprD(n)(ctx, info)

          case PSliceExp(base, low, high, cap) => for {
            dbase <- go(base)
            dlow <- option(low map go)
            dhigh <- option(high map go)
            dcap <- option(cap map go)
          } yield underlyingType(dbase.typ) match {
            case _: in.SequenceT => (dlow, dhigh) match {
              case (None, None) => dbase
              case (Some(lo), None) => in.SequenceDrop(dbase, lo)(src)
              case (None, Some(hi)) => in.SequenceTake(dbase, hi)(src)
              case (Some(lo), Some(hi)) =>
                val sub = in.Sub(hi, lo)(src)
                val drop = in.SequenceDrop(dbase, lo)(src)
                in.SequenceTake(drop, sub)(src)
            }
            case baseT @ (_: in.ArrayT | _: in.SliceT) => (dlow, dhigh) match {
              case (None, None) => in.Slice(dbase, in.IntLit(0)(src), in.Length(dbase)(src), dcap, baseT)(src)
              case (Some(lo), None) => in.Slice(dbase, lo, in.Length(dbase)(src), dcap, baseT)(src)
              case (None, Some(hi)) => in.Slice(dbase, in.IntLit(0)(src), hi, dcap, baseT)(src)
              case (Some(lo), Some(hi)) => in.Slice(dbase, lo, hi, dcap, baseT)(src)
            }
            case baseT: in.StringT =>
              Violation.violation(dcap.isEmpty, s"expected dcap to be None when slicing strings, but got $dcap instead")
              (dlow, dhigh) match {
                case (None, None) => in.Slice(dbase, in.IntLit(0)(src), in.Length(dbase)(src), None, baseT)(src)
                case (Some(lo), None) => in.Slice(dbase, lo, in.Length(dbase)(src), None, baseT)(src)
                case (None, Some(hi)) => in.Slice(dbase, in.IntLit(0)(src), hi, None, baseT)(src)
                case (Some(lo), Some(hi)) => in.Slice(dbase, lo, hi, None, baseT)(src)
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

          case g: PGhostExpression => ghostExprD(ctx, info)(g)

          case b: PBlankIdentifier =>
            val typ = typeD(info.typ(b), Addressability.exclusiveVariable)(src)
            freshDeclaredExclusiveVar(typ, expr, info)(src)

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
              target <- freshDeclaredExclusiveVar(resT, expr, info)(src)
              argsD <- sequence(args map go)
              arg0 = argsD.lift(0)
              arg1 = argsD.lift(1)

              make: in.MakeStmt = underlyingType(info.symbType(t)) match {
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
              target <- freshDeclaredExclusiveVar(targetT, expr, info)(src)
              zero = in.DfltVal(allocatedType)(src)
              _ <- write(in.New(target, zero)(src))
            } yield target

          case PPredConstructor(base, args) =>
            def handleBase(base: PPredConstructorBase, w: (in.PredT, Vector[Option[in.Expr]]) => Writer[in.Expr]) = {
              for {
                dArgs <- sequence(args.map { x => option(x.map(exprD(ctx, info)(_))) })
                idT = info.typ(base) match {
                  case FunctionT(fnArgs, AssertionT) => in.PredT(fnArgs.map(typeD(_, Addressability.rValue)(src)), Addressability.rValue)
                  case _: AbstractType =>
                    violation(dArgs.length == dArgs.flatten.length, "non-applied arguments in abstract type")
                    // The result can have arguments, namely the arguments that are provided.
                    // The receiver type is not necessary, since this should already be partially applied by the typing of base
                    in.PredT(dArgs.flatten.map(_.typ), Addressability.rValue)
                  case c => Violation.violation(s"This case should be unreachable, but got $c")
                }
                dImplicitlyConvertedArgs = (dArgs zip idT.args).map { case (optFrom, to) => optFrom.map(from => implicitConversion(from.typ, to, from)) }
                res <- w(idT, dImplicitlyConvertedArgs)
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
                      dRecv <- exprAndTypeAsExpr(ctx, info)(b.recv)
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
                      dRecv <- exprAndTypeAsExpr(ctx, info)(b.recv)
                      proxy = getMPredProxy(dRecv.typ, baseT.args.tail) // args must include at least the receiver
                      idT = in.PredT(baseT.args, Addressability.rValue)
                    } yield in.PredicateConstructor(proxy, idT, dArgs)(src)
                  })
                case c => Violation.violation(s"This case should be unreachable, but got $c")
              }
          }

          case PUnpackSlice(slice) => exprD(ctx, info)(slice)
          case e => Violation.violation(s"desugarer: $e is not supported")
        }
      }
    }

    def invokeD(ctx: FunctionContext, info: TypeInfo)(expr: PInvoke): Writer[in.Expr] = {
      val src: Meta = meta(expr, info)
      info.resolve(expr) match {
        case Some(p: ap.FunctionLikeCall) => functionLikeCallD(ctx, info)(p, expr)(src)
        case Some(ap.Conversion(typ, arg)) =>
          val typType = info.symbType(typ)
          val argType = info.typ(arg)

          (underlyingType(typType), underlyingType(argType)) match {
            case (SliceT(IntT(TypeBounds.Byte)), StringT) =>
              val resT = typeD(SliceT(IntT(TypeBounds.Byte)), Addressability.Exclusive)(src)
              for {
                target <- freshDeclaredExclusiveVar(resT, expr, info)(src)
                dArg <- exprD(ctx, info)(arg)
                conv: in.EffectfulConversion = in.EffectfulConversion(target, resT, dArg)(src)
                _ <- write(conv)
              } yield target
            case _ =>
              val desugaredTyp = typeD(typType, info.addressability(expr))(src)
              for { expr <- exprD(ctx, info)(arg) } yield in.Conversion(desugaredTyp, expr)(src)
          }

        case Some(_: ap.PredicateCall) => Violation.violation(s"cannot desugar a predicate call ($expr) to an expression")
        case p => Violation.violation(s"expected function call, predicate call, or conversion, but got $p")
      }
    }

    def applyMemberPathD(base: in.Expr, path: Vector[MemberPath])(pinfo: Source.Parser.Info): in.Expr = {
      path.foldLeft(base){ case (e, p) => p match {
        case MemberPath.Underlying => e
        case MemberPath.Deref => in.Deref(e, underlyingType(e.typ))(pinfo)
        case MemberPath.Ref => in.Ref(e)(pinfo)
        case MemberPath.Next(g) =>
          in.FieldRef(e, embeddedDeclD(g.decl, Addressability.fieldLookup(e.typ.addressability), g.context)(pinfo))(pinfo)
        case _: MemberPath.EmbeddedInterface => e
      }}
    }

    def exprAndTypeAsExpr(ctx: FunctionContext, info: TypeInfo)(expr: PExpressionOrType): Writer[in.Expr] = {

      def go(x: PExpressionOrType): Writer[in.Expr] = exprAndTypeAsExpr(ctx, info)(x)

      val src: Meta = meta(expr, info)

      expr match {

        case PTypeExpr(t) => go(t)

        case e: PExpression => exprD(ctx, info)(e)

        case PBoolType() => unit(in.BoolTExpr()(src))

        case PStringType() => unit(in.StringTExpr()(src))

        case t: PIntegerType =>
          val st = info.symbType(t).asInstanceOf[Type.IntT]
          unit(in.IntTExpr(st.kind)(src))

        case PArrayType(len, elem) =>
          for {
            inLen <- exprD(ctx, info)(len)
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


    def litD(ctx: FunctionContext, info: TypeInfo)(lit: PLiteral): Writer[in.Expr] = {

      val src: Meta = meta(lit, info)
      def single[E <: in.Expr](gen: Meta => E): Writer[in.Expr] = unit[in.Expr](gen(src))

      lit match {
        case PIntLit(v, base)  => single(in.IntLit(v, base = base))
        case PBoolLit(b) => single(in.BoolLit(b))
        case PStringLit(s) => single(in.StringLit(s))
        case nil: PNilLit => single(in.NilLit(typeD(info.nilType(nil).getOrElse(Type.PointerT(Type.BooleanT)), Addressability.literal)(src))) // if no type is found, then use *bool
        case f: PFunctionLit => registerFunctionLit(ctx, info)(f)
        case c: PCompositeLit => compositeLitD(ctx, info)(c)
        case _ => ???
      }
    }

    def functionLitD(ctx: FunctionContext)(lit: PFunctionLit): in.FunctionLit = {
      val funcInfo = functionMemberOrLitD(lit.decl, meta(lit, info), ctx)
      val src = meta(lit, info)
      val name = functionLitProxyD(lit, info)
      in.FunctionLit(name, funcInfo.args, funcInfo.captured, funcInfo.results, funcInfo.pres, funcInfo.posts, funcInfo.terminationMeasures, funcInfo.body)(src)
    }

    def pureFunctionLitD(ctx: FunctionContext, info: TypeInfo)(lit: PFunctionLit): in.PureFunctionLit = {
      val funcInfo = pureFunctionMemberOrLitD(lit.decl, meta(lit, info), ctx, info)
      val name = functionLitProxyD(lit, info)
      in.PureFunctionLit(name, funcInfo.args, funcInfo.captured, funcInfo.results, funcInfo.pres, funcInfo.posts, funcInfo.terminationMeasures, funcInfo.body)(meta(lit, info))
    }

    def capturedVarD(v: PIdnNode): (in.Parameter.In, in.LocalVar) = {
      // Given a captured variable v, generate an in-parameter and a local variable for the function literal
      // Both will have type Pointer(typeOf(v))
      val src: Meta = meta(v, info)
      val refAlias = nm.refAlias(idName(v, info), info.scope(v), info)
      val param = in.Parameter.In(refAlias, typeD(PointerT(info.typ(v)), Addressability.inParameter)(src))(src)
      val localVar = in.LocalVar(nm.alias(refAlias, info.scope(v), info), param.typ)(src)
      (param, localVar)
    }

    def compositeLitD(ctx: FunctionContext, info: TypeInfo)(lit: PCompositeLit): Writer[in.CompositeLit] = lit.typ match {

      case t: PImplicitSizeArrayType =>
        val arrayLen : BigInt = lit.lit.elems.length
        val arrayTyp = typeD(info.symbType(t.elem), Addressability.arrayElement(Addressability.literal))(meta(lit, info))
        literalValD(ctx, info)(lit.lit, in.ArrayT(arrayLen, arrayTyp, Addressability.literal))

      case t: PType =>
        val it = typeD(info.symbType(t), Addressability.literal)(meta(lit, info))
        literalValD(ctx, info)(lit.lit, it)

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

    def compositeTypeD(t : in.Type) : CompositeKind = underlyingType(t) match {
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

    def compositeValD(ctx : FunctionContext, info: TypeInfo)(expr : PCompositeVal, typ : in.Type) : Writer[in.Expr] = {
      violation(typ.addressability.isExclusive, s"Literals always have exclusive types, but got $typ")

      val e = expr match {
        case PExpCompositeVal(e) => exprD(ctx, info)(e)
        case PLitCompositeVal(lit) => literalValD(ctx, info)(lit, typ)
      }
      e map { exp => implicitConversion(exp.typ, typ, exp) }
    }

    def literalValD(ctx: FunctionContext, info: TypeInfo)(lit: PLiteralValue, t: in.Type): Writer[in.CompositeLit] = {
      violation(t.addressability.isExclusive, s"Literals always have exclusive types, but got $t")

      val src = meta(lit, info)

      compositeTypeD(t) match {

        case CompositeKind.Struct(it, ist) => {
          val fields = ist.fields

          if (lit.elems.exists(_.key.isEmpty)) {
            // all elements are not keyed
            val wArgs = fields.zip(lit.elems).map { case (f, PKeyedElement(_, exp)) => exp match {
              case PExpCompositeVal(ev) => exprD(ctx, info)(ev)
              case PLitCompositeVal(lv) => literalValD(ctx, info)(lv, f.typ)
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
                  case PExpCompositeVal(ev) => f -> exprD(ctx, info)(ev)
                  case PLitCompositeVal(lv) => f -> literalValD(ctx, info)(lv, f.typ)
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
          for { elemsD <- sequence(lit.elems.map(e => compositeValD(ctx, info)(e.exp, typ))) }
            yield in.ArrayLit(len, typ, info.keyElementIndices(lit.elems).zip(elemsD).toMap)(src)

        case CompositeKind.Slice(in.SliceT(typ, addressability)) =>
          Violation.violation(addressability == Addressability.literal, "Literals have to be exclusive")
          for { elemsD <- sequence(lit.elems.map(e => compositeValD(ctx, info)(e.exp, typ.withAddressability(Addressability.Exclusive)))) }
            yield in.SliceLit(typ, info.keyElementIndices(lit.elems).zip(elemsD).toMap)(src)

        case CompositeKind.Sequence(in.SequenceT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx, info)(e.exp, typ)))
          elemsMap = info.keyElementIndices(lit.elems).zip(elemsD).toMap
          lengthD = if (elemsMap.isEmpty) BigInt(0) else elemsMap.maxBy(_._1)._1 + 1
        } yield in.SequenceLit(lengthD, typ, elemsMap)(src)

        case CompositeKind.Set(in.SetT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx, info)(e.exp, typ)))
        } yield in.SetLit(typ, elemsD)(src)

        case CompositeKind.Multiset(in.MultisetT(typ, _)) => for {
          elemsD <- sequence(lit.elems.map(e => compositeValD(ctx, info)(e.exp, typ)))
        } yield in.MultisetLit(typ, elemsD)(src)

        case CompositeKind.Map(in.MapT(keys, values, _)) => for {
          entriesD <- handleMapEntries(ctx, info)(lit, keys, values)
        } yield in.MapLit(keys, values, entriesD)(src)

        case CompositeKind.MathematicalMap(in.MathMapT(keys, values, _)) => for {
          entriesD <- handleMapEntries(ctx, info)(lit, keys, values)
        } yield in.MathMapLit(keys, values, entriesD)(src)
      }
    }

    private def handleMapEntries(ctx: FunctionContext, info: TypeInfo)(lit: PLiteralValue, keys: in.Type, values: in.Type): Writer[Seq[(in.Expr, in.Expr)]] = {
      violation(keys.addressability.isExclusive, s"Map literal keys always have exclusive types, but got $keys")
      violation(values.addressability.isExclusive, s"Map literal values always have exclusive types, but got $values")

      sequence(
        lit.elems map {
          case PKeyedElement(Some(key), value) => for {
            entryKey <- key match {
              case v: PCompositeVal => compositeValD(ctx, info)(v, keys)
              case k: PIdentifierKey => info.regular(k.id) match {
                case _: st.Variable => unit(varD(ctx, info)(k.id))
                case c: st.Constant => unit(globalConstD(c)(meta(k, info)))
                case _ => violation(s"unexpected key $key")
              }
            }
            entryVal <- compositeValD(ctx, info)(value, values)
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
    var definedFuncLiterals: Map[in.FunctionLitProxy, in.FunctionLitLike] = Map.empty

    def registerDefinedType(t: Type.DeclaredT, addrMod: Addressability)(src: Meta): in.DefinedT = {
      // this type was declared in the current package
      val name = nm.typ(t.decl.left.name, t.context)

      def register(addrMod: Addressability): Unit = {
        if (!definedTypesSet.contains(name, addrMod)) {
          definedTypesSet += ((name, addrMod))
          val newEntry = typeD(t.context.symbType(t.decl.right), Addressability.underlying(addrMod))(src)
          definedTypes += (name, addrMod) -> newEntry
        }
      }

      val invAddrMod = addrMod match {
        case Addressability.Exclusive => Addressability.Shared
        case Addressability.Shared => Addressability.Exclusive
      }

      register(addrMod)

      // [[underlyingType(in.Type)]] assumes that the RHS of a type declaration is in `definedTypes`
      // if the corresponding type declaration was translated. This is necessary to avoid cycles in the translation.
      register(invAddrMod)

      in.DefinedT(name, addrMod)
    }

    def registerInterface(t: Type.InterfaceT, dT: in.InterfaceT): Unit = {

      if (!registeredInterfaces.contains(dT.name) && info == t.context.getTypeInfo) {
        registeredInterfaces += dT.name

        val itfT = dT.withAddressability(Addressability.Exclusive)
        val xInfo = t.context.getTypeInfo

        t.decl.predSpecs foreach { p =>
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
          val pres = (m.spec.pres ++ m.spec.preserves) map preconditionD(specCtx, info)
          val posts = (m.spec.preserves ++ m.spec.posts) map postconditionD(specCtx, info)
          val terminationMeasures = sequence(m.spec.terminationMeasures map terminationMeasureD(specCtx, info)).res

          val mem = if (m.spec.isPure) {
            in.PureMethod(recv, proxy, args, returns, pres, posts, terminationMeasures, None)(src)
          } else {
            in.Method(recv, proxy, args, returns, pres, posts, terminationMeasures, None)(src)
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
            in.DomainAxiom(pureExprD(specCtx, info)(ax.exp))(src)
          }

          AdditionalMembers.addMember(
            in.DomainDefinition(dT.name, funcs, axioms)(meta(t.decl, xInfo))
          )
        }
      }
    }
    var registeredDomains: Set[String] = Set.empty

    def registerImplementationProof(decl: PImplementationProof): Unit = {

      val src = meta(decl, info)
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

        val src = meta(mp, info)
        val recvWithSubs = inParameterD(mp.receiver, 0, info)
        val (recv, _) = recvWithSubs
        val argsWithSubs = mp.args.zipWithIndex map { case (p, i) => inParameterD(p, i, info) }
        val (args, _) = argsWithSubs.unzip
        val returnsWithSubs = mp.result.outs.zipWithIndex map { case (p, i) => outParameterD(p, i, info) }
        val (returns, _) = returnsWithSubs.unzip

        val ctx = new FunctionContext(_ => _ => in.Seqn(Vector.empty)(src)) // dummy assign
        val res = if (mp.isPure) {
          val bodyOpt = mp.body.map {
            case (_, b: PBlock) =>
              b.nonEmptyStmts match {
                case Vector(PReturn(Vector(ret))) => pureExprD(ctx, info)(ret)
                case s => Violation.violation(s"unexpected pure function body: $s")
              }
          }
          in.PureMethodSubtypeProof(subProxy, dSuperT, superProxy, recv, args, returns, bodyOpt)(src)
        } else {
          val bodyOpt = mp.body.map { case (_, s) => blockD(ctx, info)(s).asInstanceOf[in.Block] }
          in.MethodSubtypeProof(subProxy, dSuperT, superProxy, recv, args, returns, bodyOpt)(src)
        }

        AdditionalMembers.addMember(res)
      }
    }

    lazy val interfaceImplementations: Map[in.InterfaceT, SortedSet[in.Type]] = {
      info.interfaceImplementations.map{ case (itfT, implTs) =>
        (
          interfaceType(typeD(itfT, Addressability.Exclusive)(Source.Parser.Unsourced)).get,
          SortedSet(implTs.map(implT => typeD(implT, Addressability.Exclusive)(Source.Parser.Unsourced)).toSeq: _*)
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

    /** Desugar the function literal and add it to the map. */
    private def registerFunctionLit(ctx: FunctionContext, info: TypeInfo)(lit: PFunctionLit): Writer[in.Expr] = {
      val fLit = if (lit.spec.isPure) pureFunctionLitD(ctx, info)(lit) else functionLitD(ctx)(lit)
      definedFuncLiterals += fLit.name -> fLit
      unit(fLit)
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
      case Type.Float32T => in.Float32T(addrMod)
      case Type.Float64T => in.Float64T(addrMod)
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
        registerType(in.StructT(inFields, addrMod))

      case Type.PredT(args) => in.PredT(args.map(typeD(_, Addressability.rValue)(src)), Addressability.rValue)

      case Type.FunctionT(args, result) =>
        val res = result match {
          case InternalTupleT(r) => r
          case r: Type => Vector(r)
        }
        in.FunctionT(args.map(typeD(_, Addressability.rValue)(src)), res.map(typeD(_, Addressability.rValue)(src)), addrMod)

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

    def idName(id: PIdnNode, context: TypeInfo): String = context.regular(id) match {
      case f: st.Function => nm.function(id.name, f.context)
      case m: st.MethodImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case m: st.MethodSpec => nm.spec(id.name, m.itfType, m.context)
      case f: st.FPredicate => nm.function(id.name, f.context)
      case m: st.MPredicateImpl => nm.method(id.name, m.decl.receiver.typ, m.context)
      case m: st.MPredicateSpec => nm.spec(id.name, m.itfType, m.context)
      case f: st.DomainFunction => nm.function(id.name, f.context)
      case v: st.Variable => nm.variable(id.name, context.scope(id), v.context)
      case c: st.Closure => nm.funcLit(id.name, context.enclosingFunction(id).get, c.context)
      case sc: st.SingleConstant => nm.global(id.name, sc.context)
      case st.Embbed(_, _, _) | st.Field(_, _, _) => violation(s"expected that fields and embedded field are desugared by using embeddedDeclD resp. fieldDeclD but idName was called with $id")
      case n: st.NamedType => nm.typ(id.name, n.context)
      case _ => ???
    }

    /** Returns a name for the code root that `n` is contained in. */
    def rootName(n: PNode, context: TypeInfo): String = {
      info.codeRoot(n) match {
        case decl: PMethodDecl     => idName(decl.id, context)
        case decl: PFunctionDecl   => idName(decl.id, context)
        case decl: PMPredicateDecl => idName(decl.id, context)
        case decl: PFPredicateDecl => idName(decl.id, context)
        case decl: PMethodSig      => idName(decl.id, context)
        case decl: PMPredicateSig  => idName(decl.id, context)
        case decl: PDomainFunction => idName(decl.id, context)
        case _ => ??? // axiom and method-implementation-proof
      }
    }

    def globalConstD(c: st.Constant)(src: Meta): in.GlobalConst = {
      c match {
        case sc: st.SingleConstant =>
          val typ = typeD(c.context.typ(sc.idDef), Addressability.constant)(src)
          in.GlobalConst.Val(idName(sc.idDef, c.context.getTypeInfo), typ)(src)
        case _ => ???
      }
    }

    def varD(ctx: FunctionContext, info: TypeInfo)(id: PIdnNode): in.Expr = {
      require(info.regular(id).isInstanceOf[st.Variable])
      ctx(id, info) match {
        case Some(v : in.Var) => v
        case Some(d@in.Deref(_: in.Var, _)) => d
        case Some(_) => violation("expected a variable or the dereference of a pointer")
        case None => localVarContextFreeD(id, info)
      }
    }

    def assignableVarD(ctx: FunctionContext, info: TypeInfo)(id: PIdnNode) : in.Expr = {
      require(info.regular(id).isInstanceOf[st.Variable])

      ctx(id, info) match {
        case Some(v: in.AssignableVar) => v
        case Some(d@in.Deref(_: in.Var, _)) => d
        case Some(_) => violation("expected an assignable variable or the dereference of a variable")
        case None => localVarContextFreeD(id, info)
      }
    }

    def freshExclusiveVar(typ: in.Type, scope: PNode, ctx: ExternalTypeInfo)(info: Source.Parser.Info): in.LocalVar = {
      require(typ.addressability == Addressability.exclusiveVariable)
      in.LocalVar(nm.fresh(scope, ctx), typ)(info)
    }

    def freshDeclaredExclusiveVar(typ: in.Type, scope: PNode, ctx: ExternalTypeInfo)(info: Source.Parser.Info): Writer[in.LocalVar] = {
      require(typ.addressability == Addressability.exclusiveVariable)
      val res = in.LocalVar(nm.fresh(scope, ctx), typ)(info)
      declaredExclusiveVar(res)
    }

    def declaredExclusiveVar(v: in.LocalVar): Writer[in.LocalVar] = {
      declare(v).map(_ => v)
    }

    def localVarD(ctx: FunctionContext, info: TypeInfo)(id: PIdnNode): in.Expr = {
      require(info.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      ctx(id, info) match {
        case Some(v: in.LocalVar) => v
        case Some(d@in.Deref(v, _)) if v.isInstanceOf[in.LocalVar] => d
        case None => localVarContextFreeD(id, info)
        case _ => violation("expected local variable")
      }
    }

    def localVarContextFreeD(id: PIdnNode, context: TypeInfo): in.LocalVar = {
      require(context.regular(id).isInstanceOf[st.Variable]) // TODO: add local check

      val src: Meta = meta(id, context)

      val typ = typeD(context.typ(id), context.addressableVar(id))(meta(id, context))
      in.LocalVar(idName(id, context), typ)(src)
    }

    def parameterAsLocalValVar(p: in.Parameter): in.LocalVar = {
      in.LocalVar(p.id, p.typ)(p.info)
    }

    def labelProxy(l: PLabelNode): in.LabelProxy = {
      val src = meta(l, info)
      in.LabelProxy(nm.label(l.name))(src)
    }

    // Miscellaneous

    /** desugars parameter.
      * The second return argument contains an addressable copy, if necessary */
    def inParameterD(p: PParameter, idx: Int, context: TypeInfo): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p, context)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.In(idName(id, context), typeD(context.symbType(typ), Addressability.inParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id, context), id, context))
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
    def outParameterD(p: PParameter, idx: Int, context: TypeInfo): (in.Parameter.Out, Option[in.LocalVar]) = {
      val src: Meta = meta(p, context)
      p match {
        case NoGhost(noGhost: PActualParameter) =>
          noGhost match {
            case PNamedParameter(id, typ) =>
              val param = in.Parameter.Out(idName(id, context), typeD(context.symbType(typ), Addressability.outParameter)(src))(src)
              val local = Some(localAlias(localVarContextFreeD(id, context), id, context))
              (param, local)

            case PUnnamedParameter(typ) =>
              val param = in.Parameter.Out(nm.outParam(idx, context.codeRoot(p), context), typeD(context.symbType(typ), Addressability.outParameter)(src))(src)
              val local = None
              (param, local)
          }
        case c => Violation.violation(s"This case should be unreachable, but got $c")
      }
    }

    def receiverD(p: PReceiver, context: TypeInfo): (in.Parameter.In, Option[in.LocalVar]) = {
      val src: Meta = meta(p, context)
      p match {
        case PNamedReceiver(id, typ, _) =>
          val param = in.Parameter.In(idName(id, context), typeD(context.symbType(typ), Addressability.receiver)(src))(src)
          val local = Some(localAlias(localVarContextFreeD(id, context), id, context))
          (param, local)

        case PUnnamedReceiver(typ) =>
          val param = in.Parameter.In(nm.receiver(context.codeRoot(p), context), typeD(context.symbType(typ), Addressability.receiver)(src))(src)
          val local = None
          (param, local)
      }
    }

    def localAlias(internal: in.LocalVar, scope: PNode, ctx: ExternalTypeInfo): in.LocalVar = internal match {
      case in.LocalVar(id, typ) => in.LocalVar(nm.alias(id, scope, ctx), typ)(internal.info)
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
      val td = typeD(embedded._2, fieldAddrMod)(src)
      in.Field(idname, td, ghost = false)(src) // TODO: fix ghost attribute
    }

    def embeddedDeclD(decl: PEmbeddedDecl, addrMod: Addressability, context: ExternalTypeInfo)(src: Meta): in.Field = {
      val struct = context.struct(decl)
      val embedded: (String, Type) = (decl.id.name, context.typ(decl.typ))
      embeddedDeclD(embedded, addrMod, struct.get)(src)
    }

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

      def goA(ass: PExpression): Writer[in.Assertion] = assertionD(ctx, info)(ass)

      val src: Meta = meta(stmt, info)

      stmt match {
        case PAssert(exp) => for {e <- goA(exp)} yield in.Assert(e)(src)
        case PAssume(exp) => for {e <- goA(exp)} yield in.Assume(e)(src)
        case PInhale(exp) => for {e <- goA(exp)} yield in.Inhale(e)(src)
        case PExhale(exp) => for {e <- goA(exp)} yield in.Exhale(e)(src)
        case PFold(exp)   =>
          info.resolve(exp.pred) match {
            case Some(b: ap.PredExprInstance) => for {
              // the well-definedness checks guarantees that a pred expr instance in a Fold is in format predName{p1,...,pn}(a1, ...., am)
              e <- goA(exp)
              access = e.asInstanceOf[in.Access]
              predExpInstance = access.e.op.asInstanceOf[in.PredExprInstance]
              args = arguments(b.typ, predExpInstance.args)
            } yield in.PredExprFold(predExpInstance.base.asInstanceOf[in.PredicateConstructor], args, access.p)(src)

            case _ => for {e <- goA(exp)} yield in.Fold(e.asInstanceOf[in.Access])(src)
          }
        case PUnfold(exp) =>
          info.resolve(exp.pred) match {
            case Some(b: ap.PredExprInstance) => for {
              // the well-definedness checks guarantees that a pred expr instance in an Unfold is in format predName{p1,...,pn}(a1, ...., am)
              e <- goA(exp)
              access = e.asInstanceOf[in.Access]
              predExpInstance = access.e.op.asInstanceOf[in.PredExprInstance]
              args = arguments(b.typ, predExpInstance.args)
            } yield in.PredExprUnfold(predExpInstance.base.asInstanceOf[in.PredicateConstructor], args, access.p)(src)
            case _ => for {e <- goA(exp)} yield in.Unfold(e.asInstanceOf[in.Access])(src)
          }
        case PPackageWand(wand, blockOpt) =>
          for {
            w <- goA(wand)
            b <- option(blockOpt map stmtD(ctx, info))
          } yield w match {
            case w: in.MagicWand => in.PackageWand(w, b)(src)
            case e => Violation.violation(s"Expected a magic wand, but got $e")
          }
        case PApplyWand(wand) =>
          for {
            w <- goA(wand)
          } yield w match {
            case w: in.MagicWand => in.ApplyWand(w)(src)
            case e => Violation.violation(s"Expected a magic wand, but got $e")
          }
        case p: PClosureImplProof => closureImplProofD(ctx)(p)
        case PExplicitGhostStatement(actual) => stmtD(ctx, info)(actual)
        case _ => ???
      }
    }

    /**
      * Desugar a specification entailment proof (proof c implements spec{params} { BODY }), as follows:
      * - Declare a fresh variable for all the arguments and results of spec.
      * - If c is a closure variable, assign it to a new fresh variable and replace it within the body.
      * - If c has the shape `recv.methodName`, assign recv to a new fresh variable and replace it within the body.
      * - Assign the parameters to the fresh variable corresponding to the related argument.
      * - Replace all argument and result names inside the body with the newly-generated variables.
      * - Replace all argument names inside the pre- and postcondition of spec. These modified pre- and
      *     postconditions are part of the internal node [[in.SpecImplementationProof]]
      * - Replace any `return` statements with an assignment.
      */
    private def closureImplProofD(ctx: FunctionContext)(proof: PClosureImplProof): Writer[in.Stmt] = {
      val (func, funcTypeInfo) = info.resolve(proof.impl.spec.func) match {
        case Some(ap.Function(_, f)) => (f, f.context.getTypeInfo)
        case Some(ap.Closure(_, c)) => (c, c.context.getTypeInfo)
        case _ => violation("expected function member or literal")
      }

      // Generate a new local variable for an argument or result of the spec function.
      def localVarFromParam(p: PParameter): in.LocalVar = {
        val src = meta(p, funcTypeInfo)
        val typ = typeD(funcTypeInfo.typ(p), Addressability.inParameter)(src)
        in.LocalVar(nm.fresh(proof, info), typ)(src)
      }

      val argSubs = func.args map localVarFromParam
      val retSubs = func.result.outs map localVarFromParam

      // We replace a return statement inside the proof with the equivalent assignment to the result variables.
      @tailrec
      def assignReturns(rets: Vector[in.Expr])(src: Meta): in.Stmt =
        if (rets.isEmpty) in.Seqn(Vector.empty)(src)
        else if (rets.size == retSubs.size) in.Seqn(retSubs.zip(rets).map{ case (p, v) => singleAss(in.Assignee.Var(p), v)(src) })(src)
        else if (rets.size == 1) rets.head match {
          case in.Tuple(args) => assignReturns(args)(src)
          case _ => multiassD(retSubs.map(v => in.Assignee.Var(v)), rets.head, proof.block)(src)
        }
        else violation(s"found ${rets.size} returns but expected 0, 1, or ${retSubs.size}")

      // Depending on the shape of c in `proof c implements ...`, get:
      // - the receiver, if c corresponds to a received method
      // - the closure expression, if c is the name of a closure variable
      // - nothing, if c is a function name
      val recvOrClosure: Option[PExpression] = info.resolve(proof.impl.closure) match {
        case Some(ap.ReceivedMethod(recv, _, _, _)) => Some(recv)
        case Some(ap.BuiltInReceivedMethod(recv, _, _, _)) => Some(recv)
        case Some(_: ap.Function) => None
        case _ if !proof.impl.closure.isInstanceOf[PNamedOperand] => None
        case _ => Some(proof.impl.closure)
      }

      // Create a local variable, as an alias of c (or recv, if c has the form recv.methodName)
      val recvOrClosureAlias = recvOrClosure map { exp =>
        val src = meta(exp, info)
        val typ = typeD(info.typ(exp), Addressability.inParameter)(src)
        in.LocalVar(nm.fresh(proof, info), typ)(src)
      }

      // If the expression matches [[recvOrClosure]], replace it with [[recvOrClosureAlias]]
      def replaceRecvOrClosure(exp: PExpression): Option[Writer[in.Expr]] =
        if (recvOrClosure.contains(exp)) recvOrClosureAlias.map(unit)
        else None

      val newCtx = ctx.copyWith(assignReturns).copyWithExpD(replaceRecvOrClosure)
      // We need to substitute the argument and result names with the corresponding fresh variables.
      ((argSubs zip func.args) ++ (retSubs zip func.result.outs)) foreach {
        case (s, PNamedParameter(id, _)) => newCtx.addSubst(id, s, funcTypeInfo)
        case (s, PExplicitGhostParameter(PNamedParameter(id, _))) => newCtx.addSubst(id, s, funcTypeInfo)
        case _ =>
      }

      val src = meta(proof, info)

      val spec = closureSpecD(newCtx, info)(proof.impl.spec)

      // Declare all the aliases
      val declarations = argSubs ++ retSubs ++ recvOrClosureAlias.toVector
      // Assign the parameter values to the corresponding argument aliases
      val assignments =
        recvOrClosure.map(exp => singleAss(in.Assignee(recvOrClosureAlias.get), exprD(ctx, info)(exp).res)(meta(exp, info))).toVector ++
          argSubs.zipWithIndex.collect {
            case (v, idx) if spec.params.contains(idx+1) =>
              val exp = spec.params(idx+1)
              singleAss(in.Assignee(v), exp)(src)
          }

      val fSpec = func match {
        case f: st.Function => f.decl.spec
        case c: st.Closure => c.lit.spec
        case _ => violation("function or closure expected")
      }

      // Desugar the precondition of spec, replacing the argument and results with their aliases
      val pres = (fSpec.pres ++ fSpec.preserves) map preconditionD(newCtx, funcTypeInfo)

      // For the postcondition, we need to replace all old() expressions with labeled old expressions,
      // and add a label at the beginning of the proof body
      var postsCtx = newCtx.copy
      val oldLabelProxy = in.LabelProxy(nm.label(nm.fresh(proof, info)))(src)
      val oldLabel = in.Label(oldLabelProxy)(src)
      def replaceOldLabel(old: POld): Writer[in.Expr] = for {
        operand <- exprD(postsCtx, funcTypeInfo)(old.operand)
      } yield in.LabeledOld(oldLabelProxy, operand)(src)
      postsCtx = postsCtx.copyWithExpD({
        case old: POld =>
          Some(replaceOldLabel(old))
        case exp =>
          replaceRecvOrClosure(exp)
      })
      val posts = (fSpec.preserves ++ fSpec.posts) map postconditionD(postsCtx, funcTypeInfo)

      // Desugar the proof as a block containing all the aliases declarations and assignments, and
      // the corresponding internal proof node.
      for {
        proof <- for {
          closure <- exprD(newCtx, info)(proof.impl.closure)
          spec = closureSpecD(newCtx, info)(proof.impl.spec)
          body <- stmtD(newCtx, info)(proof.block)
          block = in.Block(Vector.empty, Vector(oldLabel, body))(meta(proof.block, info))
        } yield in.SpecImplementationProof(closure, spec, block, pres, posts)(src)
      } yield in.Block(declarations ++ Vector(oldLabelProxy), assignments ++ Vector(proof))(src)
    }

    // Ghost Expression
    def ghostExprD(ctx: FunctionContext, info: TypeInfo)(expr: PGhostExpression): Writer[in.Expr] = {

      def go(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)

      val src: Meta = meta(expr, info)

      val typ = typeD(info.typ(expr), info.addressability(expr))(src)

      expr match {
        case POld(op) => for {o <- go(op)} yield in.Old(o, typ)(src)
        case PLabeledOld(l, op) => for {o <- go(op)} yield in.LabeledOld(labelProxy(l), o)(src)
        case PBefore(op) => for {o <- go(op)} yield in.LabeledOld(in.LabelProxy("before")(src), o)(src)
        case PConditional(cond, thn, els) =>  for {
          wcond <- go(cond)
          wthn <- go(thn)
          wels <- go(els)
        } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PForall(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx, info)(vars, triggers, body)(ctx => exprD(ctx, info)) }
            yield in.PureForall(newVars, newTriggers, newBody)(src)

        case PExists(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx, info)(vars, triggers, body)(ctx => exprD(ctx, info)) }
            yield in.Exists(newVars, newTriggers, newBody)(src)

        case PImplication(left, right) => for {
          wcond <- go(left)
          wthn <- go(right)
          wels = in.BoolLit(b = true)(src)
        } yield in.Conditional(wcond, wthn, wels, typ)(src)

        case PTypeOf(exp) => for { wExp <- go(exp) } yield in.TypeOf(wExp)(src)
        case PIsComparable(exp) => underlyingType(info.typOfExprOrType(exp)) match {
          case _: Type.InterfaceT => for { wExp <- exprAndTypeAsExpr(ctx, info)(exp) } yield in.IsComparableInterface(wExp)(src)
          case Type.SortT => for { wExp <- exprAndTypeAsExpr(ctx, info)(exp) } yield in.IsComparableType(wExp)(src)
          case t => Violation.violation(s"Expected interface or sort type, but got $t")
        }

        case PIn(left, right) => for {
          dleft <- go(left)
          dright <- go(right)
        } yield underlyingType(dright.typ) match {
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
            dcolExp <- dcol
            baseUnderlyingType = underlyingType(dcolExp.typ)
            dleft <- go(clause.left)
            dright <- go(clause.right)
          } yield in.GhostCollectionUpdate(dcol.res, dleft, dright, baseUnderlyingType)(src)
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
          t = underlyingType(e.typ)
        } yield in.MapKeys(e, t)(src)

        case PMapValues(exp) => for {
          e <- go(exp)
          t = underlyingType(e.typ)
        } yield in.MapValues(e, t)(src)

        case PClosureImplements(closure, spec) => for {
          c <- exprD(ctx, info)(closure)
        } yield in.ClosureImplements(c, closureSpecD(ctx, info)(spec))(src)

        case _ => Violation.violation(s"cannot desugar expression to an internal expression, $expr")
      }
    }

    /**
      * Desugars a quantifier-like structure: a sequence `vars` of variable declarations,
      * together with a sequence `triggers` of triggers and a quantifier `body`.
      * @param ctx A function context consisting of variable substitutions.
      * @param info External type info, useful if the expression being desugared is from an imported package.
      * @param vars The sequence of variable (declarations) bound by the quantifier.
      * @param triggers The sequence of triggers for the quantifier.
      * @param body The quantifier body.
      * @param go The desugarer for `body`, for example `exprD` or `assertionD`.
      * @tparam T The type of the desugared quantifier body (e.g., expression, or assertion).
      * @return The desugared versions of `vars`, `triggers` and `body`.
      */
    def quantifierD[T](ctx: FunctionContext, info: TypeInfo)
                      (vars: Vector[PBoundVariable], triggers: Vector[PTrigger], body: PExpression)
                      (go : FunctionContext => PExpression => Writer[T])
        : Writer[(Vector[in.BoundVar], Vector[in.Trigger], T)] = {
      val newVars = vars map boundVariableD

      // substitution has to be added since otherwise all bound variables are translated to addressable variables
      val bodyCtx = ctx.copy
      (vars zip newVars).foreach { case (a, b) => bodyCtx.addSubst(a.id, b, info) }

      for {
        newTriggers <- sequence(triggers map triggerD(bodyCtx, info))
        newBody <- go(bodyCtx)(body)
      } yield (newVars, newTriggers, newBody)
    }

    def boundVariableD(x: PBoundVariable) : in.BoundVar =
      in.BoundVar(idName(x.id, info), typeD(info.symbType(x.typ), Addressability.boundVariable)(meta(x, info)))(meta(x, info))

    def pureExprD(ctx: FunctionContext, info: TypeInfo)(expr: PExpression): in.Expr = {
      val dExp = exprD(ctx, info)(expr)
      Violation.violation(dExp.stmts.isEmpty && dExp.decls.isEmpty, s"expected pure expression, but got $expr")
      dExp.res
    }


    // Assertion

    def specificationD(ctx: FunctionContext, info: TypeInfo)(ass: PExpression): in.Assertion = {
      val condition = assertionD(ctx, info)(ass)
      Violation.violation(condition.stmts.isEmpty && condition.decls.isEmpty, s"$ass is not an assertion")
      condition.res
    }

    def preconditionD(ctx: FunctionContext, info: TypeInfo)(ass: PExpression): in.Assertion = {
      specificationD(ctx, info)(ass)
    }

    def postconditionD(ctx: FunctionContext, info: TypeInfo)(ass: PExpression): in.Assertion = {
      specificationD(ctx, info)(ass)
    }

    def terminationMeasureD(ctx: FunctionContext, info: TypeInfo)(measure: PTerminationMeasure): Writer[in.TerminationMeasure] = {
      val src: Meta = meta(measure, info)

      def goE(expr: PExpression): Writer[in.Node] = expr match {
        case p: PInvoke => info.resolve(p) match {
          case Some(x: ap.PredicateCall) => predicateCallAccD(ctx, info)(x)(src)
          case Some(_: ap.FunctionCall) => exprD(ctx, info)(p)
          case _ => violation(s"Unexpected expression $expr")
        }
        case _ => exprD(ctx, info)(expr)
      }

      measure match {
        case PWildcardMeasure(cond) =>
          for { c <- option(cond map exprD(ctx, info)) } yield in.WildcardMeasure(c)(src)
        case PTupleTerminationMeasure(tuple, cond) =>
          for {
            t <- sequence(tuple map goE)
            c <- option(cond map exprD(ctx, info))
          } yield in.TupleTerminationMeasure(t, c)(src)
      }
    }

    def assertionD(ctx: FunctionContext, info: TypeInfo)(n: PExpression): Writer[in.Assertion] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)
      def goA(a: PExpression): Writer[in.Assertion] = assertionD(ctx, info)(a)

      val src: Meta = meta(n, info)

      n match {
        case n: PImplication => for {l <- goE(n.left); r <- goA(n.right)} yield in.Implication(l, r)(src)
        case n: PConditional => // TODO: create Conditional Expression in internal ast
          for {
            cnd <- goE(n.cond)
            thn <- goA(n.thn)
            els <- goA(n.els)
          } yield in.SepAnd(in.Implication(cnd, thn)(src), in.Implication(in.Negation(cnd)(src), els)(src))(src)

        case PMagicWand(left, right) =>
          for {l <- goA(left); r <- goA(right)} yield in.MagicWand(l, r)(src)

        case n: PAnd => for {l <- goA(n.left); r <- goA(n.right)} yield in.SepAnd(l, r)(src)

        case n: PAccess => for {e <- accessibleD(ctx, info)(n.exp); p <- permissionD(ctx, info)(n.perm)} yield in.Access(e, p)(src)
        case n: PPredicateAccess => predicateCallD(ctx, info)(n.pred, n.perm)

        case n: PInvoke =>
          // a predicate invocation corresponds to a predicate access with full permissions
          // register the full permission AST node in the position manager such that its meta information
          // is retrievable in predicateCallD
          val perm = PFullPerm()
          pom.positions.dupPos(n, perm)
          predicateCallD(ctx, info)(n, perm)

        case PForall(vars, triggers, body) =>
          for { (newVars, newTriggers, newBody) <- quantifierD(ctx, info)(vars, triggers, body)(ctx => assertionD(ctx, info)) }
            yield newBody match {
              case in.ExprAssertion(exprBody) =>
                in.ExprAssertion(in.PureForall(newVars, newTriggers, exprBody)(src))(src)
              case _ => in.SepForall(newVars, newTriggers, newBody)(src)
            }

        case _ => exprD(ctx, info)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallD(ctx: FunctionContext, info: TypeInfo)(n: PInvoke, perm: PExpression): Writer[in.Assertion] = {

      val src: Meta = meta(n, info)

      info.resolve(n) match {
        case Some(p: ap.PredicateCall) =>
          for {
            predAcc <- predicateCallAccD(ctx, info)(p)(src)
            p <- permissionD(ctx, info)(perm)
          } yield in.Access(in.Accessible.Predicate(predAcc), p)(src)

        case Some(b: ap.PredExprInstance) =>
          for {
            base <- exprD(ctx, info)(n.base.asInstanceOf[PExpression])
            args <- sequence(n.args.map(exprD(ctx, info)(_)))
            implicitlyConvertedArgs = arguments(b.typ, args)
            predExprInstance = in.PredExprInstance(base, implicitlyConvertedArgs)(src)
            p <- permissionD(ctx, info)(perm)
          } yield in.Access(in.Accessible.PredExpr(predExprInstance), p)(src)

        case _ => exprD(ctx, info)(n) map (in.ExprAssertion(_)(src)) // a boolean expression
      }
    }

    def predicateCallAccD(ctx: FunctionContext, info: TypeInfo)(p: ap.PredicateCall)(src: Meta): Writer[in.PredicateAccess] = {

      def getBuiltInPredType(pk: ap.BuiltInPredicateKind): FunctionT = {
        val abstractType = info.typ(pk.symb.tag)
        val argsForTyping = pk match {
          case _: ap.BuiltInPredicate =>
            p.args.map(info.typ)
          case base: ap.BuiltInReceivedPredicate =>
            Vector(info.typ(base.recv))
          case base: ap.BuiltInPredicateExpr =>
            Vector(info.symbType(base.typ))
        }
        Violation.violation(abstractType.typing.isDefinedAt(argsForTyping), s"cannot type built-in member ${pk.symb.tag} as it is not defined for arguments $argsForTyping")
        abstractType.typing(argsForTyping)
      }

      /** args must not include the receiver in the case of received predicates in order that the FunctionT corresponding
        * to the given predicate symbol has the same amount of parameters / parameter types */
      def convertArgs(args: Vector[in.Expr]): Vector[in.Expr] = {
        p.predicate match {
          case b: ap.BuiltInPredicateKind => arguments(getBuiltInPredType(b), args)
          case b: ap.Symbolic => b.symb match {
            case psym: st.Predicate => arguments(psym, args)
            case s => Violation.violation(s"expected a predicate symbol for a predicate call but got $s")
          }
          case p => Violation.violation(s"unexpected predicate kind, got $p")
        }
      }

      /** not-yet implicitly converted args */
      val dArgs = p.args map pureExprD(ctx, info)

      p.predicate match {
        case b: ap.Predicate =>
          val fproxy = fpredicateProxy(b.id, info)
          unit(in.FPredicateAccess(fproxy, convertArgs(dArgs))(src))

        case b: ap.ReceivedPredicate =>
          val dRecv = pureExprD(ctx, info)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val proxy = mpredicateProxyD(b.id, info)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, convertArgs(dArgs))(src))

        case b: ap.PredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val proxy = mpredicateProxyD(b.id, info)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, convertArgs(dArgs.tail))(src))

        case _: ap.PredExprInstance => Violation.violation("this case should be handled somewhere else")

        case b: ap.ImplicitlyReceivedInterfacePredicate =>
          val proxy = mpredicateProxyD(b.id, info)
          val recvType = typeD(b.symb.itfType, Addressability.receiver)(src)
          unit(in.MPredicateAccess(implicitThisD(recvType)(src), proxy, convertArgs(dArgs))(src))

        case b: ap.BuiltInPredicate =>
          val fproxy = fpredicateProxy(b.id, info)
          unit(in.FPredicateAccess(fproxy, convertArgs(dArgs))(src))

        case b: ap.BuiltInReceivedPredicate =>
          val dRecv = pureExprD(ctx, info)(b.recv)
          val dRecvWithPath = applyMemberPathD(dRecv, b.path)(src)
          val convertedArgs = convertArgs(dArgs)
          val proxy = mpredicateProxy(b.symb.tag, dRecvWithPath.typ, convertedArgs.map(_.typ))(src)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, convertedArgs)(src))

        case b: ap.BuiltInPredicateExpr =>
          val dRecvWithPath = applyMemberPathD(dArgs.head, b.path)(src)
          val convertedArgsWithoutRecv = convertArgs(dArgs.tail)
          /** consists of the receiver type and the type of the converted arguments */
          val argTypes = (dArgs.head +: convertedArgsWithoutRecv).map(_.typ)
          val proxy = mpredicateProxy(b.symb.tag, dRecvWithPath.typ, argTypes)(src)
          unit(in.MPredicateAccess(dRecvWithPath, proxy, convertedArgsWithoutRecv)(src))
      }
    }

    def implicitThisD(p: in.Type)(src: Source.Parser.Info): in.Parameter.In =
      in.Parameter.In(nm.implicitThis, p.withAddressability(Addressability.receiver))(src)

    def accessibleD(ctx: FunctionContext, info: TypeInfo)(acc: PExpression): Writer[in.Accessible] = {

      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)

      val src: Meta = meta(acc, info)

      info.resolve(acc) match {
        case Some(p: ap.PredicateCall) =>
          predicateCallAccD(ctx, info)(p)(src) map (x => in.Accessible.Predicate(x))

        case Some(p: ap.PredExprInstance) =>
          for {
            base <- goE(p.base)
            predInstArgs <- sequence(p.args map goE)
            implicitlyConvertedArgs = arguments(p.typ, predInstArgs)
          } yield in.Accessible.PredExpr(in.PredExprInstance(base, implicitlyConvertedArgs)(src))

        case _ =>
          val argT = info.typ(acc)
          underlyingType(argT) match {
            case Single(ut: Type.PointerT) =>
              // [[in.Accessible.Address]] represents '&'.
              // If there is no outermost '&', then adds '&*'.
              acc match {
                case PReference(op) => addressableD(ctx, info)(op) map (x => in.Accessible.Address(x.op))
                case _ =>
                  goE(acc).map{ x =>
                    val underlyingT = typeD(ut, Addressability.reference)(src)
                    in.Accessible.Address(in.Deref(x, underlyingT)(src))
                  }
              }

            case Single(_: Type.SliceT) =>
              goE(acc) map (x => in.Accessible.ExprAccess(x))

            case Single(_: Type.MapT) =>
              goE(acc) map (x => in.Accessible.ExprAccess(x))

            case _ => Violation.violation(s"expected pointer type or a predicate, but got $argT")
          }
      }

    }

    def permissionD(ctx: FunctionContext, info: TypeInfo)(exp: PExpression): Writer[in.Expr] = {
      maybePermissionD(ctx, info)(exp) getOrElse exprD(ctx, info)(exp)
    }

    def maybePermissionD(ctx: FunctionContext, info: TypeInfo)(exp: PExpression): Option[Writer[in.Expr]] = {
      val src: Meta = meta(exp, info)
      def goE(e: PExpression): Writer[in.Expr] = exprD(ctx, info)(e)

      exp match {
        case n: PInvoke => info.resolve(n) match {
          case Some(_: ap.Conversion) =>
            Some(for {
              // the well-definedness checker ensures that there is exactly one argument
              arg <- permissionD(ctx, info)(n.args.head)
            } yield in.Conversion(in.PermissionT(Addressability.conversionResult), arg)(src))
          case _ => None
        }
        case PFullPerm() => Some(unit(in.FullPerm(src)))
        case PNoPerm() => Some(unit(in.NoPerm(src)))
        case PWildcardPerm() => Some(unit(in.WildcardPerm(src)))
        case PDiv(l, r) => (info.typ(l), info.typ(r)) match {
          case (PermissionT, IntT(_)) => Some(for { vl <- permissionD(ctx, info)(l); vr <- goE(r) } yield in.PermDiv(vl, vr)(src))
          case (IntT(_), IntT(_)) => Some(for { vl <- goE(l); vr <- goE(r) } yield in.FractionalPerm(vl, vr)(src))
          case err => violation(s"This case should be unreachable, but got $err")
        }
        case PNegation(exp) => Some(for {e <- permissionD(ctx, info)(exp)} yield in.PermMinus(e)(src))
        case PAdd(l, r) => Some(for { vl <- permissionD(ctx, info)(l); vr <- permissionD(ctx, info)(r) } yield in.PermAdd(vl, vr)(src))
        case PSub(l, r) => Some(for { vl <- permissionD(ctx, info)(l); vr <- permissionD(ctx, info)(r) } yield in.PermSub(vl, vr)(src))
        case PMul(l, r) => Some(for {vl <- goE(l); vr <- permissionD(ctx, info)(r)} yield in.PermMul(vl, vr)(src))
        case x if info.typ(x).isInstanceOf[IntT] => Some(for { e <- goE(x) } yield in.FractionalPerm(e, in.IntLit(BigInt(1))(src))(src))
        case _ => None
      }
    }

    def triggerD(ctx: FunctionContext, info: TypeInfo)(trigger: PTrigger) : Writer[in.Trigger] = {
      val src: Meta = meta(trigger, info)
      for { exprs <- sequence(trigger.exps map triggerExprD(ctx, info)) } yield in.Trigger(exprs)(src)
    }

    def triggerExprD(ctx: FunctionContext, info: TypeInfo)(triggerExp: PExpression): Writer[in.TriggerExpr] = info.resolve(triggerExp) match {
      case Some(p: ap.PredicateCall) => for { pa <- predicateCallAccD(ctx, info)(p)(meta(triggerExp, info)) } yield in.Accessible.Predicate(pa)
      case _ => exprD(ctx, info)(triggerExp)
    }

    private def meta(n: PNode, context: TypeInfo): Source.Parser.Single = {
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
    private val REFERENCE_PREFIX = "R"
    private val FUNCTION_PREFIX = "F"
    private val METHODSPEC_PREFIX = "S"
    private val METHOD_PREFIX = "M"
    private val TYPE_PREFIX = "T"
    private val INTERFACE_PREFIX = "Y"
    private val DOMAIN_PREFIX = "D"
    private val LABEL_PREFIX = "L"
    private val GLOBAL_PREFIX = "G"
    private val BUILTIN_PREFIX = "B"
    private val CONTINUE_LABEL_SUFFIX = "$Continue"
    private val BREAK_LABEL_SUFFIX = "$Break"

    /** the counter to generate fresh names depending on the current code root for which a fresh name should be generated */
    private var nonceCounter: Map[PCodeRoot, Int] = Map.empty

    /**
      * there is a separate counter per code root in order that scope, identifiers, etc. of an unchanged function
      * are consistently named across verification runs (i.e. independent of modifications to other function)
      */
    private var scopeCounter: Map[PCodeRoot, Int] = Map.empty

    /**
      * 'scopeMap' should return a unique identifier only within a package,
      * and thus may not be unique in the resulting Viper encoding.
      * This should be fine as long as the resulting name is only used in a non-global scope.
      * Currently, it is used for variables, in and out parameters, and receivers.
      */
    private var scopeMap: Map[PScope, Int] = Map.empty

    private def maybeRegister(s: PScope, ctx: ExternalTypeInfo): Unit = {
      if (!(scopeMap contains s)) {
        val codeRoot = ctx.codeRoot(s)
        val value = scopeCounter.getOrElse(codeRoot, 0)

        scopeMap += (s -> value)
        scopeCounter += (codeRoot -> (value + 1))
      }
    }

    val implicitThis: String = Names.implicitThis

    private def name(postfix: String)(n: String, s: PScope, context: ExternalTypeInfo): String = {
      maybeRegister(s, context)

      // n has occur first in order that function inverse properly works
      s"${n}_$postfix${scopeMap(s)}" // deterministic
    }

    private def nameWithEnclosingFunction(postfix: String)(n: String, enclosing: PFunctionDecl, context: ExternalTypeInfo): String = {
      maybeRegister(enclosing, context)
      s"${n}_${enclosing.id.name}_${context.pkgInfo.viperId}_$postfix${scopeMap(enclosing)}" // deterministic
    }

    private def topLevelName(postfix: String)(n: String, context: ExternalTypeInfo): String = {
      // n has occur first in order that function inverse properly works
      s"${n}_${context.pkgInfo.viperId}_$postfix" // deterministic
    }

    def variable(n: String, s: PScope, context: ExternalTypeInfo): String = name(VARIABLE_PREFIX)(n, s, context)
    def funcLit(n: String, enclosing: PFunctionDecl, context: ExternalTypeInfo): String = nameWithEnclosingFunction(FUNCTION_PREFIX)(n, enclosing, context)
    def anonFuncLit(enclosing: PFunctionDecl, context: ExternalTypeInfo): String = nameWithEnclosingFunction(FUNCTION_PREFIX)("func", enclosing, context) ++ s"_${fresh(enclosing, context)}"
    def global  (n: String, context: ExternalTypeInfo): String = topLevelName(GLOBAL_PREFIX)(n, context)
    def typ     (n: String, context: ExternalTypeInfo): String = topLevelName(TYPE_PREFIX)(n, context)
    def field   (n: String, @unused s: StructT): String = s"$n$FIELD_PREFIX" // Field names must preserve their equality from the Go level
    def function(n: String, context: ExternalTypeInfo): String = topLevelName(FUNCTION_PREFIX)(n, context)
    def spec    (n: String, t: Type.InterfaceT, context: ExternalTypeInfo): String =
      topLevelName(s"$METHODSPEC_PREFIX${interface(t)}")(n, context)
    def method  (n: String, t: PMethodRecvType, context: ExternalTypeInfo): String = t match {
      case PMethodReceiveName(typ)    => topLevelName(s"$METHOD_PREFIX${typ.name}")(n, context)
      case PMethodReceivePointer(typ) => topLevelName(s"P$METHOD_PREFIX${typ.name}")(n, context)
    }
    private def stringifyType(typ: in.Type): String = Names.serializeType(typ)
    def builtInMember(tag: BuiltInMemberTag, dependantTypes: Vector[in.Type]): String = {
      val typeString = dependantTypes.map(stringifyType).mkString("_")
      s"${tag.identifier}_$BUILTIN_PREFIX$FUNCTION_PREFIX$typeString"
    }

    def inverse(n: String): String = n.substring(0, n.length - FIELD_PREFIX.length)

    def alias(n: String, scope: PNode, ctx: ExternalTypeInfo): String = s"${n}_$COPY_PREFIX${fresh(scope, ctx)}"

    def refAlias(n: String, scope: PNode, ctx: ExternalTypeInfo): String = s"${n}_$REFERENCE_PREFIX${fresh(scope, ctx)}"

    /** returns a fresh string that is guaranteed to be unique in the root scope of `scope` (i.e. in the enclosing code root or domain in which `scope` occurs) */
    def fresh(scope: PNode, ctx: ExternalTypeInfo): String = {
      val codeRoot = ctx.codeRoot(scope)
      val value = nonceCounter.getOrElse(codeRoot, 0)

      val f = FRESH_PREFIX + value
      nonceCounter += (codeRoot -> (value + 1))
      f
    }

    /**
      * Returns an id for a node with respect to its code root.
      * The id is of the form 'L$<a>$<b>' where a is the difference of the lines
      * of the node with the code root and b is the difference of the columns
      * of the node with the code root.
      * If a differce is negative, the '-' character is replaced with '_'.
      *
      * @param node the node we are interested in
      * @param info type info to get position information
      * @return     string
      */
    def relativeId(node: PNode, info: TypeInfo) : String = {
      val pom = info.getTypeInfo.tree.originalRoot.positions
      val lpos = pom.positions.getStart(node).get
      val rpos = pom.positions.getStart(info.codeRoot(node)).get
      return ("L$" + (lpos.line - rpos.line) + "$" + (lpos.column - rpos.column)).replace("-", "_")
    }

    /** returns the relativeId with the CONTINUE_LABEL_SUFFIX appended */
    def continueLabel(loop: PGeneralForStmt, info: TypeInfo) : String = relativeId(loop, info) + CONTINUE_LABEL_SUFFIX

    /** returns the relativeId with the BREAK_LABEL_SUFFIX appended */
    def breakLabel(loop: PGeneralForStmt, info: TypeInfo) : String = relativeId(loop, info) + BREAK_LABEL_SUFFIX

    /**
      * Finds the enclosing loop which the continue statement refers to and fetches its
      * continue label.
      */
    def fetchContinueLabel(n: PContinue, info: TypeInfo) : String = {
      n.label match {
        case None =>
          val Some(loop) = info.enclosingLoopNode(n)
          continueLabel(loop, info)
        case Some(label) =>
          val Some(loop) = info.enclosingLabeledLoopNode(label, n)
          continueLabel(loop, info)
      }
    }

    /**
      * Finds the enclosing loop which the break statement refers to and fetches its
      * break label.
      */
    def fetchBreakLabel(n: PBreak, info: TypeInfo) : String = {
      n.label match {
        case None =>
          val Some(loop) = info.enclosingLoopNode(n)
          breakLabel(loop, info)
        case Some(label) =>
          val Some(loop) = info.enclosingLabeledLoopNode(label, n)
          breakLabel(loop, info)
      }
    }

    def inParam(idx: Int, s: PScope, context: ExternalTypeInfo): String = name(IN_PARAMETER_PREFIX)("P" + idx, s, context)
    def outParam(idx: Int, s: PScope, context: ExternalTypeInfo): String = name(OUT_PARAMETER_PREFIX)("P" + idx, s, context)
    def receiver(s: PScope, context: ExternalTypeInfo): String = name(RECEIVER_PREFIX)("R", s, context)

    def interface(s: InterfaceT): String = {
      if (s.isEmpty) {
        Names.emptyInterface
      } else {
        val pom = s.context.getTypeInfo.tree.originalRoot.positions
        val hash = srcTextName(pom, s.decl.embedded, s.decl.methSpecs, s.decl.predSpecs)
        s"$INTERFACE_PREFIX$$${topLevelName("")(hash, s.context)}"
      }
    }

    def domain(s: DomainT): String = {
      val pom = s.context.getTypeInfo.tree.originalRoot.positions
      val hash = srcTextName(pom, s.decl.funcs, s.decl.axioms)
      s"$DOMAIN_PREFIX$$${topLevelName("")(hash, s.context)}"
    }

    def label(n: String): String = n match {
      case "#lhs" => "lhs"
      case _ => s"${n}_$LABEL_PREFIX"
    }

    /**
      * Maps nodes to a hash based on the source text of the nodes (not the source position!).
      * The source texts belonging to the same Vector[N] argument are sorted before they are hashed.
      * */
    private def srcTextName[N <: PNode](pom: PositionManager, nodes: Vector[N]*): String = {

      def trimmedSrcText(n: N): String = {
        val start = pom.positions.getStart(n).get
        val finish = pom.positions.getFinish(n).get
        val srcText = pom.positions.substring(start, finish).get
        srcText.filterNot(_.isWhitespace)
      }

      val sortedStrings = nodes.map(_.map(trimmedSrcText).sorted)
      Names.hash(sortedStrings.flatten.mkString("|"))
    }
  }
}

