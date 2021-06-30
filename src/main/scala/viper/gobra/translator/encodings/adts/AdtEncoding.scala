// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.adts

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.{AdtClause, AdtT, ArrayT, MatchAdt, MatchBindVar, MatchValue, MatchWildcard, PrettyType, SequenceT, SetT}
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.DerivableTags
import viper.gobra.frontend.info.base.DerivableTags.Collection
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.{MatchError, Source}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.util.TypeBounds
import viper.silver.ast.{AnonymousDomainAxiom, DomainFunc, DomainFuncApp, ErrorTrafo, Forall, Info, Position}
import viper.silver.{ast => vpr}

class AdtEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}
  import viper.silver.verifier.{errors => err}

  override def finalize(col: Collector): Unit = {
    generatedDomains foreach {d => col.addMember(d)}
  }

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Adt(adt) / m =>
      m match {
        case Exclusive => adtType(adt.name)
        case Shared => vpr.Ref
      }
    case ctx.AdtClause(t) / m =>
      m match {
        case Exclusive => adtType(t.adtT.name)
        case Shared => vpr.Ref
      }
  }

  private def adtType(adtName: String): vpr.DomainType = vpr.DomainType(adtName, Map.empty)(Seq.empty)

  private def getTag(clauses: Vector[AdtClause])(clause: AdtClause): BigInt = {
    val sorted = clauses.sortBy(_.name.name)
    BigInt(sorted.indexOf(clause))
  }

  private var generatedDomains: List[vpr.Domain]  = List.empty
  private var lenFunc: Option[vpr.DomainFunc] = None

  def genLenFunc: vpr.DomainFunc = {
    val f = {
      val domainName = "AdtLenDomain"
      val typeVar    = vpr.TypeVar("T")

      val lFunc = vpr.DomainFunc(
        name = "lenAdt", formalArgs = Seq(vpr.LocalVarDecl("t", typeVar)()), typ = vpr.Int
      )(domainName = domainName)

      val domain = vpr.Domain(
        name = domainName,
        functions = Seq(lFunc), axioms = Seq(), typVars = Seq(typeVar)
      )()

      generatedDomains ::= domain
      lFunc
    }

    lenFunc = Some(f)
    f
  }

  def getLenFunc: vpr.DomainFunc = {
    lenFunc.getOrElse(genLenFunc)
  }

  def lenApp(arg: vpr.Exp) = {
    vpr.DomainFuncApp(
      getLenFunc,
      Seq(arg),
      Seq(vpr.TypeVar("T") -> arg.typ).toMap
    )()
  }

  private def lenAxioms(adtName: String, clauses: Seq[in.AdtClause])(ctx: Context): Seq[vpr.AnonymousDomainAxiom] = {
    val (pos, info, errorT) = (vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

    def axiom(c: in.AdtClause) = {
      val argsDecl = clauseArgsAsLocalVarDecl(c)(ctx)
      val args = argsDecl.map(_.localVar)
      val cons = constructorCall(c, args)(pos, info, errorT)
      val e    = if (args.isEmpty) {
          vpr.EqCmp(
            lenApp(cons),
            vpr.IntLit(BigInt(0))()
          )()
        } else {
          val recLen = args.filter(a => a.typ == adtType(adtName)).map(lenApp)
          val sum    = recLen.foldLeft(vpr.IntLit(BigInt(1))(): vpr.Exp)((acc, n) => vpr.Add(acc, n)())
          vpr.Forall (
            argsDecl,
            Seq(
              vpr.Trigger(Seq(cons))()
            ),
            vpr.EqCmp(
              lenApp(cons),
              sum
            )()
          )()
      }
      vpr.AnonymousDomainAxiom(
        e
      )(domainName = adtName)
    }

    clauses.map(axiom)
  }

  private var toSetFunc: Option[vpr.DomainFunc] = None

  /**
    * Generates
    * domain AdtToSetDomain[T,Q] {
    *   function toSetAdt(t: T): Set[Q]
    * }
    *
    */

  private def getToSetFunc: vpr.DomainFunc = toSetFunc.getOrElse {
    val domainName = "AdtToSetDomain"
    val typeVar    = vpr.TypeVar("T")
    val setTypeVar = vpr.TypeVar("Q")
    val variable   = vpr.LocalVarDecl("t", typeVar)()

    val toSetF = vpr.DomainFunc(
      name = "toSetAdt", formalArgs = Seq(variable), typ = vpr.SetType(setTypeVar)
    )(domainName = domainName)

    val domain = vpr.Domain(
      domainName, Seq(toSetF), Seq(), Seq(typeVar, setTypeVar)
    )()

    generatedDomains ::= domain
    toSetFunc = Some(toSetF)
    toSetF
  }

  private def toSetApp(e: vpr.Exp, target: vpr.Type)(pos: Position = vpr.NoPosition, info: Info = vpr.NoInfo, error: ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp = {
    vpr.DomainFuncApp(
      getToSetFunc,
      Seq(e),
      Seq(vpr.TypeVar("T") -> e.typ, vpr.TypeVar("Q") -> target).toMap
    )(pos, info, error)
  }

  private def toSetAxioms(adtName: String, clauses: Vector[AdtClause], blacklist: Map[String, Vector[in.Field]], typ: in.Type)(ctx: Context): Seq[vpr.AnonymousDomainAxiom] = {

    /**
      * Helper function to determine if some Type t contains typ.
      * Returns true for
      * set[typ], sequence[typ], [typ]N and adts which are deriving Collection[typ]
     */
    def containsType(t: in.Type): Boolean = underlyingType(t)(ctx) match {
      case prettyType: PrettyType => prettyType match {
        case ArrayT(_, elems, _) / Exclusive => elems.equals(typ)
        case SequenceT(t, _) => t.equals(typ)
        case SetT(t, _) => t.equals(typ)
        case _ => false
      }
      case t : AdtT => t.derives.derivable match {
        case DerivableTags.Collection(_, _) => typ.equalsWithoutMod(t.derives.typeVars.head)
        case _ => false
      } //Derives Collection[A]
      case _ => false
    }

    /**
      * Helper function to determine if t is the same adt type
      */
    def sameAdt(t: in.Type): Boolean = underlyingType(t)(ctx) match {
      case t: AdtT => t.name.equals(adtName)
      case _ => false
    }

    // Viper Type of Set
    val vprType = ctx.typeEncoding.typ(ctx)(typ)

    /**
      *
      * @param t type to check
      * @return true iff t is a set
      */
    def isSet(t: in.Type): Boolean = t match {
      case prettyType: PrettyType => prettyType match {
        case SetT(_, _) => true
        case _ => false
      }
      case _ => false
    }

    def isSeq(t: in.Type): Boolean = t match {
      case prettyType: PrettyType => prettyType match {
        case SequenceT(_, _) => true
        case _ => false
      }
      case _ => false
    }

    def isArray(t: in.Type): Boolean = t match {
      case prettyType: PrettyType => prettyType match {
        case in.ArrayT(_, _, _) / Exclusive => true
        case _ => false
      }
      case _ => false
    }

    /**
      *
      * @param localVars Local Variables for some constructor of an adt clause
      * @param relevantFields the relevant fields in internal Represantation
      * @return The localVars which are corresponding to the relevantFields
      */
    def filterVars(localVars: Vector[vpr.LocalVarDecl], relevantFields: Vector[in.Field]): Seq[vpr.LocalVarDecl] =
      localVars.filter(v => relevantFields.map(_.name).contains(v.name))


    /**
      * Helper function to return the union of the sets.
      */
    def unionSets(sets: Seq[vpr.Exp])(pos: Position = vpr.NoPosition, info: Info = vpr.NoInfo, error: ErrorTrafo = vpr.NoTrafos) = {
      if (sets.isEmpty) {
        vpr.EmptySet(vprType)(pos, info, error)
      } else if (sets.length == 1) {
        sets.head
      } else {
        sets.reduceLeft((a,b) => vpr.AnySetUnion(a,b)(pos, info, error))
      }
    }

    /**
      * This function generates the toSet axiom for one clause
      */
    def clauseAxiom(clause: AdtClause): vpr.AnonymousDomainAxiom = {

      //val clauseBlacklisted
      val blacklistedFields = blacklist.getOrElse(clause.name.name, Vector.empty)
      val args = clause.args.filter(f => !blacklistedFields.contains(f))

      //All fields of type A
      val sameTypeFields = args.filter(a => a.typ.equals(typ))
      //All fields which contain A
      val containsTypeF  = args.filter(a => containsType(a.typ))
      //Fields which contain A and are a Set
      val withSets       = containsTypeF.filter(f => isSet(f.typ))
      //Field which contain A and are a Seq
      val withSeqs       = containsTypeF.filter(f => isSeq(f.typ))
      //Arrays
      val withArrays     = containsTypeF.filter(f => isArray(f.typ))
      //All recursive fields
      val recursive      = args.filter(a => sameAdt(a.typ))

      //Get the local Vars for the corresponding fields
      val localVars = clauseArgsAsLocalVarDecl(clause)(ctx)
      val setLitVars = filterVars(localVars, sameTypeFields)
      val setsOfA = filterVars(localVars, withSets)
      val seqsOfA = filterVars(localVars, withSeqs)
      val recursiveVars = filterVars(localVars, recursive)

      val constructor = constructorCall(clause, localVars.map(_.localVar))()

      //All fields with the same type can be bundled in one Set Literal
      val setLitExp = if (setLitVars.nonEmpty)
          vpr.ExplicitSet(setLitVars.map(_.localVar))()
        else
          vpr.EmptySet(vprType)()
      //Recursive fields and fields which contains A are called with toSet
      val containsExp = unionSets(recursiveVars.map(n => toSetApp(n.localVar, vprType)()))()
      //Sets just can be unioned
      val setsOfAExp = unionSets(setsOfA.map(_.localVar))()
      val seqsOfAExp = unionSets(seqsOfA.map(v => ctx.seqToSet.create(v.localVar)()))()

      val arrayOfAExp = unionSets(withArrays.map(v => ctx.expr.translate(in.SetConversion(in.LocalVar(v.name, v.typ)(v.info))(v.info))(ctx).res))()

      //Check if the Sets are not just empty sets. Then generate the union
      var nonEmptySets: Vector[vpr.Exp] = Vector.empty

      if (setLitVars.nonEmpty) {
        nonEmptySets = nonEmptySets :+ setLitExp
      }

      if (recursiveVars.nonEmpty) {
        nonEmptySets = nonEmptySets :+ containsExp
      }

      if (setsOfA.nonEmpty) {
        nonEmptySets = nonEmptySets :+ setsOfAExp
      }

      if (seqsOfA.nonEmpty) {
        nonEmptySets = nonEmptySets :+ seqsOfAExp
      }

      if (arrayOfAExp.nonEmpty) {
        nonEmptySets = nonEmptySets :+ arrayOfAExp
      }

      val setUnion = unionSets(nonEmptySets)()

      vpr.AnonymousDomainAxiom(
        vpr.Forall(
          localVars,
          Seq(
            vpr.Trigger(Seq(constructor))()
          ),
          vpr.EqCmp(
            toSetApp(constructor, vprType)(),
            setUnion
          )()
        )()
      )(domainName = adtName)
    }

    clauses.filter(c => c.args.nonEmpty).map(clauseAxiom)
  }

  private def constructorCall(clause: AdtClause, args: Seq[vpr.Exp])(pos: Position = vpr.NoPosition, info: Info = vpr.NoInfo, errT: ErrorTrafo = vpr.NoTrafos): DomainFuncApp = {
    val adtName = clause.name.adtName
    vpr.DomainFuncApp(
      Names.constructorAdtName(adtName, clause.name.name),
      args,
      Map.empty
    )(pos,info,adtType(adtName), adtName, errT)
  }

  private def clauseArgsAsLocalVarDecl(c: AdtClause)(ctx: Context): Vector[vpr.LocalVarDecl] = {
    val (cPos, cInfo, cErrT) = c.vprMeta
    c.args map { a =>
      val typ = ctx.typeEncoding.typ(ctx)(a.typ)
      val name = a.name
      vpr.LocalVarDecl(name, typ)(cPos, cInfo, cErrT)
    }
  }

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case adt: in.AdtDefinition =>
      val adtName = adt.name
      val (aPos, aInfo, aErrT) = adt.vprMeta

      def localVarTDecl = vpr.LocalVarDecl("t", adtType(adtName))(_,_,_)
      def localVarT = vpr.LocalVar("t", adtType(adtName))(_,_,_)

      def tagF = getTag(adt.clauses)(_)

      def destructorsClause(clause: AdtClause): Vector[DomainFunc] =
        clause.args.map(a => {
          val (argPos, argInfo, argErrT) = a.vprMeta
          DomainFunc(
            Names.destructorAdtName(adtName, a.name),
            Seq(localVarTDecl(argPos, argInfo, argErrT)),
            ctx.typeEncoding.typ(ctx)(a.typ)
          )(argPos, argInfo, adtName, argErrT)
        })



      def clauseArgsAsLocalVarExp(c: AdtClause): Vector[vpr.LocalVar] = {
        val (cPos, cInfo, cErrT) = c.vprMeta
        c.args map { a =>
          val typ = ctx.typeEncoding.typ(ctx)(a.typ)
          val name = a.name
          vpr.LocalVar(name, typ)(cPos, cInfo, cErrT)
        }
      }

      def tagApp(arg: vpr.Exp) = {
        vpr.DomainFuncApp(
          Names.tagAdtFunction(adtName),
          Seq(arg),
          Map.empty
        )(_,_, vpr.Int, adtName, _)
      }

      def containsAxiom(clause: AdtClause): vpr.DomainAxiom = {
        val (cPos, cInfo, cErrT) = clause.vprMeta

        val argsDecl    = clauseArgsAsLocalVarDecl(clause)(ctx)
        val args        = clauseArgsAsLocalVarExp(clause)
        val constructor = constructorCall(clause, args)(cPos, cInfo, cErrT)

        val allContians: vpr.Exp = viper.silicon.utils.ast.BigAnd(
          args map {a => ctx.contains.contains(a, constructor)(cPos, cInfo, cErrT)}
        )

        val trigger = vpr.Trigger(Seq(constructor))(cPos, cInfo, cErrT)
        val forall  = vpr.Forall(argsDecl, Seq(trigger), allContians)(cPos, cInfo, cErrT)

        AnonymousDomainAxiom(
          forall
        )(cPos, cInfo, adtName, cErrT)
      }

      val contAxioms = adt.clauses.filter(c => c.args.nonEmpty).map(containsAxiom)

      def deconstructorCall(field: String, arg: vpr.Exp, retTyp: vpr.Type) =
        {
          vpr.DomainFuncApp(
            Names.destructorAdtName(adtName, field),
            Seq(arg),
            Map.empty
          )(_, _, retTyp, adtName, _)
        }

      val clauses = adt.clauses map { c =>
        val (cPos, cInfo, cErrT) = c.vprMeta
        val args = clauseArgsAsLocalVarDecl(c)(ctx)
        vpr.DomainFunc(Names.constructorAdtName(adtName, c.name.name), args, adtType(adtName))(cPos, cInfo, adtName, cErrT)
      }

      val defaultFunc = vpr.DomainFunc(
        Names.dfltAdtValue(adtName),
        Seq.empty,
        adtType(adtName)
      )(aPos, aInfo, adtName, aErrT)

      val tagFunc = vpr.DomainFunc(
        Names.tagAdtFunction(adtName),
        Seq(localVarTDecl(aPos, aInfo, aErrT)),
        vpr.Int
      )(aPos, aInfo, adtName, aErrT)

      val destructors: Vector[DomainFunc] = adt.clauses.flatMap(destructorsClause)

      val tagAxioms: Vector[AnonymousDomainAxiom] = adt.clauses.map(c => {
        val (cPos, cInfo, cErrT) = c.vprMeta
        val args: Seq[vpr.Exp] = clauseArgsAsLocalVarExp(c)
        val triggerVars: Seq[vpr.LocalVarDecl] = clauseArgsAsLocalVarDecl(c)(ctx)
        val construct = constructorCall(c, args)(cPos, cInfo, cErrT)
        val trigger = vpr.Trigger(Seq(construct))(cPos, cInfo, cErrT)
        val lhs: vpr.Exp = tagApp(construct)(cPos, cInfo, cErrT)
        val clauseTag = vpr.IntLit(tagF(c))(cPos, cInfo, cErrT)

        val destructors = c.args.map(a =>
          deconstructorCall(a.name, construct, ctx.typeEncoding.typ(ctx)(a.typ))(cPos, cInfo, cErrT)
        )

        if (c.args.nonEmpty) {
          val destructOverConstruct : vpr.Exp = (destructors.zip(args).map {
            case (d, a) => vpr.EqCmp(
              d, a
            )(cPos, cInfo, cErrT) : vpr.Exp
          }: Seq[vpr.Exp]).reduceLeft {
            (l: vpr.Exp, r: vpr.Exp) => vpr.And(l, r)(cPos, cInfo, cErrT) : vpr.Exp
          }

          AnonymousDomainAxiom(Forall(triggerVars, Seq(trigger), vpr.And(
            vpr.EqCmp(lhs, clauseTag)(cPos, cInfo, cErrT),
            destructOverConstruct
          )(cPos, cInfo, cErrT)
          )(cPos, cInfo, cErrT))(cPos, cInfo, adtName, cErrT)
        } else {
          AnonymousDomainAxiom(vpr.EqCmp(lhs, clauseTag)(cPos, cInfo, cErrT))(cPos, cInfo, adtName, cErrT)
        }
      })

      val destructorAxioms: Vector[AnonymousDomainAxiom] = adt.clauses.filter(c => c.args.nonEmpty).map(c => {
        val (cPos, cInfo, cErrT) = c.vprMeta
        val variable = localVarTDecl(cPos, cInfo, cErrT)
        val localVar = localVarT(cPos, cInfo, cErrT)

        val destructors = c.args.map(a =>
          deconstructorCall(a.name, localVar, ctx.typeEncoding.typ(ctx)(a.typ))(cPos, cInfo, cErrT)
        )

        val trigger = destructors.map(d => vpr.Trigger(Seq(d))(cPos, cInfo, cErrT))
        val clauseTag = vpr.IntLit(tagF(c))(cPos, cInfo, cErrT)
        val triggerTagApp = tagApp(localVar)(cPos, cInfo, cErrT)
        val implicationLhs = vpr.EqCmp(triggerTagApp, clauseTag)(aPos, aInfo, aErrT)
        val implicationRhs = vpr.EqCmp(
          localVar,
          constructorCall(c, destructors)(cPos, cInfo, cErrT)
          )(cPos, cInfo, cErrT)

        val implication = vpr.Implies(implicationLhs, implicationRhs)(cPos, cInfo, cErrT)

        vpr.AnonymousDomainAxiom(vpr.Forall(Seq(variable), trigger, implication)(cPos, cInfo, cErrT))(cPos,
          cInfo, adtName, cErrT)
      })

      val exclusiveAxiom = {
        val variableDecl = localVarTDecl(aPos, aInfo, aErrT)
        val variable = localVarT(aPos, aInfo, aErrT)
        val triggerExpression = tagApp(variable)(aPos, aInfo, aErrT)
        val trigger = vpr.Trigger(Seq(triggerExpression))(aPos, aInfo, aErrT)

        def destructors(clause: AdtClause) = clause.args map(a => {
          val (argPos, argInfo, argErrT) = a.vprMeta
          deconstructorCall(a.name, variable, ctx.typeEncoding.typ(ctx)(a.typ))(argPos, argInfo, argErrT)
        })

        val equalities = adt.clauses.map(c => {
            val (cPos, cInfo, cErrT) = c.vprMeta
            constructorCall(c, destructors(c))(cPos, cInfo, cErrT)
          })
          .map(c => {
              vpr.EqCmp(variable, c)(c.pos, c.info, c.errT)
          })
          .foldLeft(vpr.FalseLit()(aPos, aInfo, aErrT) : vpr.Exp)({ (acc, next) => vpr.Or(acc, next)(aPos, aInfo, aErrT) : vpr.Exp })

        vpr.AnonymousDomainAxiom(
          vpr.Forall(Seq(variableDecl), Seq(trigger), equalities)(aPos, aInfo, aErrT)
        )(aPos, aInfo, adtName, aErrT)
      }


      val additonalAxioms  = adt.derives.derivable match {
        case _: Collection =>
          lenAxioms(adtName, adt.clauses)(ctx) ++ toSetAxioms(adtName, adt.clauses, adt.derives.blacklist, adt.derives.typeVars.head)(ctx)
        case _ => Vector.empty
      }

      val axioms = (tagAxioms ++ destructorAxioms ++ contAxioms ++ additonalAxioms) :+ exclusiveAxiom
      val funcs = (clauses ++ destructors) :+ defaultFunc :+ tagFunc

      ml.unit(Vector(vpr.Domain(adtName, functions = funcs, axioms = axioms)(pos = aPos, info = aInfo, errT = aErrT)))
  }

  def translateContains(c: in.Contains)(ctx: Context) = {
    def goE(e: in.Expr) = ctx.expr.translate(e)(ctx)

    val (pos, info, errT) = c.vprMeta

    for {
      l <- goE(c.left)
      r <- goE(c.right)
    } yield ctx.contains.contains(l, r)(pos, info, errT)
  }

  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def defaultVal(e: in.DfltVal, a: in.AdtT) = {
      val (pos, info, errT) = e.vprMeta
      unit(
        vpr.DomainFuncApp(
          funcname = Names.dfltAdtValue(a.name),
          Seq.empty,
          Map.empty
        )(pos, info, adtType(a.name), a.name, errT): vpr.Exp
      )
    }

    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.Adt(a) / Exclusive => defaultVal(e, a)
      case (e: in.DfltVal) :: ctx.AdtClause(a) / Exclusive => defaultVal(e, a.adtT)
      case ac: in.AdtConstructorLit => adtConstructor(ac, ctx)
      case ad: in.AdtDiscriminator => adtDiscriminator(ad, ctx)
      case ad: in.AdtDestructor => adtDestructor(ad, ctx)
      case p:  in.PatternMatchExp => translatePatternMatchExp(ctx)(p)
      case c@in.Contains(_, _ :: ctx.Adt(_)) => translateContains(c)(ctx)
      case in.Length(exp :: ctx.Adt(_)) => for {
          e <- ctx.expr.translate(exp)(ctx)
        } yield lenApp(e)
      case in.SetConversion(exp :: ctx.Adt(_)) =>
        val (pos, info, err) = exp.vprMeta
        for {
          e <- ctx.expr.translate(exp)(ctx)
        } yield toSetApp(e, ctx.typeEncoding.typ(ctx)(in.IntT(Addressability.Exclusive, TypeBounds.DefaultInt)))(pos, info, err)
    }
  }

  def adtConstructor(ac: in.AdtConstructorLit, ctx: Context) = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    val (pos, info, errT) = ac.vprMeta
    for {
      args <- sequence(ac.args map goE)
    } yield vpr.DomainFuncApp(
      funcname = Names.constructorAdtName(ac.clause.adtName, ac.clause.name),
      args,
      Map.empty
    )(pos, info, adtType(ac.clause.adtName), ac.clause.adtName, errT)
  }

  def adtDiscriminator(ac: in.AdtDiscriminator, ctx: Context) = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    val adtType = underlyingType(ac.base.typ)(ctx).asInstanceOf[in.AdtT]
    val (pos, info, errT) = ac.vprMeta
    for {
      value <- goE(ac.base)
    } yield vpr.EqCmp(vpr.DomainFuncApp(
        Names.tagAdtFunction(adtType.name),
        Seq(value), Map.empty)(pos, info, vpr.Int, adtType.name, errT),
      vpr.IntLit(adtType.clauseToTag(ac.clause.name))(pos, info, errT)
    )(pos, info, errT)
  }

  def adtDestructor(ac: in.AdtDestructor, ctx: Context) = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    val adtType = underlyingType(ac.base.typ)(ctx).asInstanceOf[in.AdtT]
    val (pos, info, errT) = ac.vprMeta
    for {
      value <- goE(ac.base)
    } yield vpr.DomainFuncApp(
      Names.destructorAdtName(adtType.name, ac.field.name),
      Seq(value), Map.empty)(pos, info, ctx.typeEncoding.typ(ctx)(ac.field.typ), adtType.name, errT)
  }

  def declareIn(ctx: Context)(e: in.Expr, p: in.MatchPattern, z: vpr.Exp): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = p.vprMeta

    p match {
      case MatchValue(_) | MatchWildcard() => unit(z)
      case MatchBindVar(name, typ) =>
        for {
          eV <- ctx.expr.translate(e)(ctx)
        } yield vpr.Let(
          vpr.LocalVarDecl(name, ctx.typeEncoding.typ(ctx)(typ))(pos, info, errT),
          eV,
          z
        )(pos, info, errT)
      case MatchAdt(clause, expr) =>
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(e, f)(e.info))
        val zipWithPattern = inDeconstructors.zip(expr)
        zipWithPattern.foldRight(unit(z))((des, acc) => for {
          v <- acc
          d <- declareIn(ctx)(des._1, des._2, v)
        } yield d)
    }
  }

  def translatePatternMatchExp(ctx: Context)(e: in.PatternMatchExp): CodeWriter[vpr.Exp] = {

    def translateCases(cases: Vector[in.PatternMatchCaseExp], dflt: in.Expr): CodeWriter[vpr.Exp] = {

      val (ePos, eInfo, eErrT) = if (cases.isEmpty) dflt.vprMeta else cases.head.vprMeta

      if (cases.isEmpty) {
        ctx.expr.translate(dflt)(ctx)
      } else {
        val c = cases.head
        for {
          check <- translateMatchPatternCheck(ctx)(e.exp, c.mExp)
          body <- ctx.expr.translate(c.exp)(ctx)
          decl <- declareIn(ctx)(e.exp, c.mExp, body)
          el <- translateCases(cases.tail, dflt)
        } yield vpr.CondExp(check, decl, el)(ePos, eInfo, eErrT)
      }
    }

    if (e.default.isDefined) {
      translateCases(e.cases, e.default.get)
    } else {

      val (pos, info, errT) = e.vprMeta

      val allChecks = e.cases
        .map(c => translateMatchPatternCheck(ctx)(e.exp, c.mExp))
        .foldLeft(unit(vpr.FalseLit()() : vpr.Exp))((acc, next) =>
          for {
            a <- acc
            n <- next
          } yield vpr.Or(a,n)(pos, info, errT))


      for {
        dummy <- ctx.expr.translate(in.DfltVal(e.typ)(e.info))(ctx)
        cond <- translateCases(e.cases, in.DfltVal(e.typ)(e.info))
        checks <- allChecks
        (checkFunc, errCheck) = ctx.condition.assert(checks, (info, _) => MatchError(info))
        _ <- errorT(errCheck)
      } yield vpr.CondExp(checkFunc, cond, dummy)(pos, info, errT)
    }


  }

  def translateMatchPatternCheck(ctx: Context)(expr: in.Expr, pattern: in.MatchPattern): CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    val (pos, info, errT) = pattern.vprMeta

    def matchSimpleExp(exp: in.Expr): Writer[vpr.Exp] = for {
      e1 <- goE(exp)
      e2 <- goE(expr)
    } yield vpr.EqCmp(e1, e2)(pos, info, errT)

    val (mPos, mInfo, mErr) = pattern.vprMeta

    pattern match {
      case in.MatchValue(exp) => matchSimpleExp(exp)
      case in.MatchBindVar(_, _) | in.MatchWildcard() => unit(vpr.TrueLit()(pos,info,errT))
      case in.MatchAdt(clause, exp) =>
        val tagFunction = Names.tagAdtFunction(clause.adtT.name)
        val tag = vpr.IntLit(clause.adtT.clauseToTag(clause.name))(mPos, mInfo, mErr)
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(expr, f)(pattern.info))
        for {
          e1 <- goE(expr)
          tF = vpr.DomainFuncApp(tagFunction, Vector(e1), Map.empty)(mPos, mInfo, vpr.Int, clause.adtT.name, mErr)
          checkTag = vpr.EqCmp(tF, tag)(mPos)
          rec <- sequence(inDeconstructors.zip(exp) map {case (e, p) => translateMatchPatternCheck(ctx)(e,p)})
        } yield rec.foldLeft(checkTag:vpr.Exp)({case (acc, next) => vpr.And(acc, next)(mPos, mInfo, mErr)})
    }
  }

  def translateMatchPatternDeclarations(ctx: Context)(expr: in.Expr, pattern: in.MatchPattern): Option[CodeWriter[vpr.Seqn]] = {
    val (mPos, mInfo, mErrT) = pattern.vprMeta
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    pattern match {
      case MatchBindVar(name, typ) =>
        val t = ctx.typeEncoding.typ(ctx)(typ)

        val writer = for {
          e <- goE(expr)
          v = vpr.LocalVarDecl(name, t)(mPos, mInfo, mErrT)
          a = vpr.LocalVarAssign(vpr.LocalVar(name, t)(mPos, mInfo, mErrT), e)(mPos, mInfo, mErrT)
        } yield vpr.Seqn(Seq(a), Seq(v))(mPos, mInfo, mErrT)

        Some(writer)

      case MatchAdt(clause, exprs) =>
        val inDeconstructors = clause.fields.map(f => in.AdtDestructor(expr, f)(pattern.info))
        val recAss: Vector[CodeWriter[vpr.Seqn]] =
          inDeconstructors.zip(exprs) map {case (e, p) => translateMatchPatternDeclarations(ctx)(e,p)} collect {case Some(s) => s}
        val assignments: Vector[vpr.Seqn] = recAss map {a => a.res}
        val reduced = assignments.foldLeft(vpr.Seqn(Seq(), Seq())(mPos, mInfo, mErrT))(
          {case (l, r) => vpr.Seqn(l.ss ++ r .ss, l.scopedDecls ++ r.scopedDecls)(mPos, mInfo, mErrT)}
        )
        Some(unit(reduced))

      case _ : MatchValue | _: in.MatchWildcard => None
    }
  }

  def translatePatternMatch(ctx: Context)(s: in.PatternMatchStmt): CodeWriter[vpr.Stmt] = {
    val expr = s.exp
    val cases = s.cases

    val (sPos, sInfo, sErrT) = s.vprMeta

    val checkExVarDecl = vpr.LocalVarDecl(Names.freshName, vpr.Bool)(sPos, sInfo, sErrT)
    val checkExVar = checkExVarDecl.localVar
    val initialExVar = unit(vpr.LocalVarAssign(checkExVar, vpr.FalseLit()(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))
    def exErr(ass: vpr.Stmt): ErrorTransformer = {
      case e@err.AssertFailed(Source(info), _, _) if e causedBy ass => MatchError(info)
    }

    val assertWithError = for {
      a <- unit(vpr.Assert(vpr.EqCmp(checkExVar, vpr.TrueLit()(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))(sPos, sInfo, sErrT))
      _ <- errorT(exErr(a))
    } yield a

    def setExVar(p: vpr.Position, i: vpr.Info, e: vpr.ErrorTrafo) =
      unit(vpr.LocalVarAssign(checkExVar, vpr.TrueLit()(p,i,e))(p,i,e))

    def translateCase(c: in.PatternMatchCaseStmt): CodeWriter[vpr.Stmt] = {
      val (cPos, cInfo, cErrT) = c.vprMeta

      val assignments = translateMatchPatternDeclarations(ctx)(expr, c.mExp)

      val (ass: Seq[vpr.Stmt], decls: Seq[vpr.Declaration]) =
        if (assignments.isDefined) {
          val w = assignments.get.res
          (w.ss, w.scopedDecls)
        } else {
          (Seq(), Seq())
        }

      for {
        check <- translateMatchPatternCheck(ctx)(expr, c.mExp)
        exVar <- setExVar(cPos, cInfo, cErrT)
        body  <- sequence(c.body map {f => ctx.stmt.translate(f)(ctx)})
      } yield vpr.If(vpr.And(check, vpr.Not(checkExVar)(cPos, cInfo, cErrT))(cPos, cInfo, cErrT),
        vpr.Seqn(exVar +: (ass ++ body), decls)(cPos, cInfo, cErrT), vpr.Seqn(Seq(), Seq())(cPos, cInfo, cErrT))(cPos, cInfo, cErrT)
    }

    if (s.strict) {
      for {
        init <- initialExVar
        cs <- sequence(cases map translateCase)
        a <- assertWithError
      } yield vpr.Seqn(init +: cs :+ a, Seq(checkExVarDecl))(sPos, sInfo, sErrT)
    } else {
      for {
        init <- initialExVar
        cs <- sequence(cases map translateCase)
      } yield vpr.Seqn(init +: cs, Seq(checkExVarDecl))(sPos, sInfo, sErrT)
    }
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)){
      case p: in.PatternMatchStmt => translatePatternMatch(ctx)(p)
    }
  }
}
