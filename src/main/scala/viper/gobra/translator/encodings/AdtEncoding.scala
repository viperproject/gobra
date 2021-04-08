package viper.gobra.translator.encodings


import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.{AdtClause}
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.{AnonymousDomainAxiom, DomainFunc, DomainFuncApp, Forall}
import viper.silver.{ast => vpr}

class AdtEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}
  import viper.gobra.translator.util.TypePatterns._

  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Adt(adt) / m =>
      m match {
        case Exclusive => adtType(adt.name)
        case Shared => vpr.Ref
      }
  }

  private def adtType(adtName: String): vpr.DomainType = vpr.DomainType(adtName, Map.empty)(Seq.empty)

  private def tagFunction(clauses: Vector[AdtClause])(clause: AdtClause): BigInt = {
    val sorted = clauses.sortBy(_.name.name)
    BigInt(sorted.indexOf(clause))
  }

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case adt: in.AdtDefinition =>
      val adtName = adt.name
      val (aPos, aInfo, aErrT) = adt.vprMeta
      def tagF = tagFunction(adt.clauses)(_)

      def destructorsClause(clause: AdtClause): Vector[DomainFunc] =
        clause.args.map(a => {
          val (argPos, argInfo, argErrT) = a.vprMeta
          DomainFunc(
            Names.destructorAdtName(adtName, clause.name.name, a.name),
            Seq(vpr.LocalVarDecl("t", adtType(adtName))(argPos, argInfo, argErrT)),
            ctx.typeEncoding.typ(ctx)(a.typ)
          )(argPos, argInfo, adtName, argErrT)
        })

      def clauseArgsAsLocalVarDecl(c: AdtClause): Vector[vpr.LocalVarDecl] = {
        val (cPos, cInfo, cErrT) = c.vprMeta
        c.args map { a =>
          val typ = ctx.typeEncoding.typ(ctx)(a.typ)
          val name = a.name
          vpr.LocalVarDecl(name, typ)(cPos, cInfo, cErrT)
        }
      }


      val clauses = adt.clauses map { c =>
        val (cPos, cInfo, cErrT) = c.vprMeta
        val args = clauseArgsAsLocalVarDecl(c)
        vpr.DomainFunc(Names.constructorAdtName(adtName, c.name.name), args, adtType(adtName))(cPos, cInfo, adtName, cErrT)
      }

      val defaultFunc = vpr.DomainFunc(
        Names.dfltAdtValue(adtName),
        Seq.empty,
        adtType(adtName)
      )(aPos, aInfo, adtName, aErrT)

      val tagFunc = vpr.DomainFunc(
        Names.tagAdtFunction(adtName),
        Seq(vpr.LocalVarDecl("t", adtType(adtName))(aPos, aInfo, aErrT)),
        vpr.Int
      )(aPos, aInfo, adtName, aErrT)

      val destructors: Vector[DomainFunc] = adt.clauses.flatMap(destructorsClause)

      val tagAxioms: Vector[AnonymousDomainAxiom] = adt.clauses.map(c => {
        val (cPos, cInfo, cErrT) = c.vprMeta
        val args: Seq[vpr.Exp] = c.args map (c => vpr.LocalVar(c.name, ctx.typeEncoding.typ(ctx)(c.typ))(cPos, cInfo, cErrT))
        val triggerVars: Seq[vpr.LocalVarDecl] = clauseArgsAsLocalVarDecl(c)
        val construct = Seq(vpr.DomainFuncApp(Names.constructorAdtName(adtName, c.name.name), args, Map.empty)
          (cPos, cInfo, adtType(adtName), adtName, cErrT))
        val trigger = vpr.Trigger(construct)(cPos, cInfo, cErrT)
        val lhs: vpr.Exp = vpr.DomainFuncApp(Names.tagAdtFunction(adtName), construct, Map.empty)(cPos, cInfo, vpr.Int, adtName,
          cErrT)
        val clauseTag = vpr.IntLit(tagF(c))(cPos, cInfo, cErrT)

        if (c.args.nonEmpty) {
          AnonymousDomainAxiom(Forall(triggerVars, Seq(trigger), vpr.EqCmp(lhs, clauseTag)(cPos, cInfo, cErrT))(cPos, cInfo, cErrT))(cPos, cInfo, adtName, cErrT)
        } else {
          AnonymousDomainAxiom(vpr.EqCmp(lhs, clauseTag)(cPos, cInfo, cErrT))(cPos, cInfo, adtName, cErrT)
        }
      })

      val destructorAxioms: Vector[AnonymousDomainAxiom] = adt.clauses.filter(c => c.args.nonEmpty).map(c => {
        val (cPos, cInfo, cErrT) = c.vprMeta
        val variable = vpr.LocalVarDecl("t", adtType(adtName))(cPos, cInfo, cErrT)
        val localVar = vpr.LocalVar("t", adtType(adtName))(cPos, cInfo, cErrT)

        def domainFuncApp(funcName: String, typ: vpr.Type): vpr.DomainFuncApp = vpr.DomainFuncApp(funcname = funcName,
          args = Seq(vpr.LocalVar("t", adtType(adtName))(cPos, cInfo, cErrT)), typVarMap = Map.empty)(cPos, cInfo, typ, adtName, cErrT)

        val destructors = c.args.map(a => domainFuncApp(Names.destructorAdtName(adtName, c.name.name, a.name), ctx.typeEncoding.typ(ctx)(a.typ)))

        val trigger = vpr.Trigger(
          destructors
        )(cPos, cInfo, cErrT)

        val clauseTag = vpr.IntLit(tagF(c))(cPos, cInfo, cErrT)
        val tagApp = vpr.DomainFuncApp(Names.tagAdtFunction(adtName), Seq(localVar),
          Map.empty)(cPos, cInfo, vpr.Int, adtName, cErrT)
        val implicationLhs = vpr.EqCmp(tagApp, clauseTag)(aPos, aInfo, aErrT)

        val implicationRhs = vpr.EqCmp(localVar,
          DomainFuncApp(Names.constructorAdtName(adtName, c.name.name), destructors, Map.empty)(cPos,
            cInfo, adtType(adtName), adtName, cErrT))(cPos, cInfo, cErrT)

        val implication = vpr.Implies(implicationLhs, implicationRhs)(cPos, cInfo, cErrT)

        vpr.AnonymousDomainAxiom(vpr.Forall(Seq(variable), Seq(trigger), implication)(cPos, cInfo, cErrT))(cPos,
          cInfo, adtName, cErrT)
      })

      val exclusiveAxiom = {
        val variableDecl = vpr.LocalVarDecl("t", adtType(adtName))(aPos, aInfo, aErrT)
        val variable = vpr.LocalVar("t", adtType(adtName))(aPos, aInfo, aErrT)

        val triggerExpression = DomainFuncApp(
          Names.tagAdtFunction(adtName),
          Seq(variable),
          Map.empty
        )(aPos, aInfo, vpr.Int, adtName, aErrT)

        val trigger = vpr.Trigger(Seq(triggerExpression))(aPos, aInfo, aErrT)

        def destructors(clause: AdtClause) = clause.args map(a => {
          val (argPos, argInfo, argErrT) = a.vprMeta
          DomainFuncApp(
            Names.destructorAdtName(adtName, clause.name.name, a.name),
            Seq(variable),
            Map.empty
          )(argPos, argInfo, ctx.typeEncoding.typ(ctx)(a.typ), adtName, argErrT)
        })

        val clauseDestructConstruct = adt.clauses map(c => {
          val (cPos, cInfo, cErrT) = c.vprMeta
          vpr.DomainFuncApp(
            Names.constructorAdtName(adtName, c.name.name),
            destructors(c),
            Map.empty
          )(cPos, cInfo, adtType(adtName), adtName, cErrT)
        })

        val equalities = clauseDestructConstruct
          .map(c => {
          vpr.EqCmp(variable, c)(c.pos, c.info, c.errT)
        })
          .foldLeft(vpr.TrueLit()(aPos, aInfo, aErrT) : vpr.Exp)({ (acc, next) => vpr.Or(acc, next)(aPos, aInfo, aErrT) : vpr.Exp })

        vpr.AnonymousDomainAxiom(
          vpr.Forall(Seq(variableDecl), Seq(trigger), equalities)(aPos, aInfo, aErrT)
        )(aPos, aInfo, adtName, aErrT)
      }

      val axioms = (tagAxioms ++ destructorAxioms) :+ exclusiveAxiom
      val funcs = (clauses ++ destructors) :+ defaultFunc :+ tagFunc

      ml.unit(Vector(vpr.Domain(adtName, functions = funcs, axioms = axioms)(pos = aPos, info = aInfo, errT = aErrT)))
  }

  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){
      case (e: in.DfltVal) :: ctx.Adt(a) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        unit (
          vpr.DomainFuncApp(
            funcname = Names.dfltAdtValue(a.name),
            Seq.empty,
            Map.empty
          )(pos, info, adtType(a.name), a.name, errT): vpr.Exp
        )

      case ac: in.AdtConstructorLit => {
        val (pos, info, errT) = ac.vprMeta
        for {
          args <- sequence(ac.args map goE)
        } yield vpr.DomainFuncApp(
          funcname = Names.constructorAdtName(ac.clause.adtName, ac.clause.name),
          args,
          Map.empty
        )(pos, info, adtType(ac.clause.adtName), ac.clause.adtName, errT)
      }
    }
  }


}
