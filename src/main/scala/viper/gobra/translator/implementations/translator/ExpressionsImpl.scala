package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {
  }

  override def trigger(trigger: in.Trigger)(ctx: Context) : CodeWriter[vpr.Trigger] = {
    val (pos, info, errT) = trigger.vprMeta
    for { expr <- sequence(trigger.exprs map (translate(_)(ctx))) }
      yield vpr.Trigger(expr)(pos, info, errT)
  }

  def quantifier(vars: Vector[in.BoundVar], triggers: Vector[in.Trigger], body: in.Expr)(ctx: Context) : CodeWriter[(Seq[vpr.LocalVarDecl], Seq[vpr.Trigger], vpr.Exp)] = {
    val (decls, _) = vars.map(ctx.loc.parameter(_)(ctx)).unzip
    val newVars = decls.flatten

    for {
      newTriggers <- sequence(triggers map (trigger(_)(ctx)))
      newBody <- translate(body)(ctx)
    } yield (newVars, newTriggers, newBody)
  }

  override def translate(x: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = x.vprMeta

    def goE(e: in.Expr): CodeWriter[vpr.Exp] = translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {

      case unfold: in.Unfolding =>
        for {
          a <- ctx.loc.predicateAccess(unfold.op)(ctx)
          e <- goE(unfold.in)
        } yield vpr.Unfolding(a, e)(pos, info, errT)

      case in.PureFunctionCall(func, args, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(func.name, vArgss.flatten)(pos, info, resultType, errT)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.PureMethodCall(recv, meth, args, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vRecvs <- ctx.loc.argument(recv)(ctx)
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(meth.uniqueName, vRecvs ++ vArgss.flatten)(pos, info, resultType, errT)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.DfltVal(t) => ctx.loc.defaultValue(t)(x)(ctx)
      case in.Tuple(args) => Violation.violation("Tuples expressions are not supported at this point in time")
      case p: in.Deref => ctx.loc.evalue(p)(ctx)
      case f: in.FieldRef => ctx.loc.evalue(f)(ctx)
      case r: in.Ref => ctx.loc.evalue(r)(ctx)

      case in.Negation(op) => for{o <- goE(op)} yield vpr.Not(o)(pos, info, errT)

      case in.EqCmp(l, r) => ctx.loc.equal(l, r)(x)(ctx)
      case in.UneqCmp(l, r) => ctx.loc.equal(l, r)(x)(ctx).map(vpr.Not(_)(pos, info, errT))
      case in.LessCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LtCmp(vl, vr)(pos, info, errT)
      case in.AtMostCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LeCmp(vl, vr)(pos, info, errT)
      case in.GreaterCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GtCmp(vl, vr)(pos, info, errT)
      case in.AtLeastCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GeCmp(vl, vr)(pos, info, errT)

      case in.And(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.And(vl, vr)(pos, info, errT)
      case in.Or(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Or(vl, vr)(pos, info, errT)

      case in.Add(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Add(vl, vr)(pos, info, errT)
      case in.Sub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Sub(vl, vr)(pos, info, errT)
      case in.Mul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mul(vl, vr)(pos, info, errT)
      case in.Mod(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mod(vl, vr)(pos, info, errT)
      case in.Div(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Div(vl, vr)(pos, info, errT)
      case in.Old(op) => for { o <- goE(op) } yield vpr.Old(o)(pos, info, errT)
      case in.Conditional(cond, thn, els, _) => for {vcond <- goE(cond); vthn <- goE(thn); vels <- goE(els)} yield vpr.CondExp(vcond, vthn, vels)(pos, info, errT)

      case in.PureForall(vars, triggers, body) => for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newForall = vpr.Forall(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newForall.check match {
        case Seq() => newForall
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

      case in.Exists(vars, triggers, body) => for {
        (newVars, newTriggers, newBody) <- quantifier(vars, triggers, body)(ctx)
        newExists =  vpr.Exists(newVars, newTriggers, newBody)(pos, info, errT).autoTrigger
      } yield newExists.check match {
        case Seq() => newExists
        case errors => Violation.violation(s"invalid trigger pattern (${errors.head.readableMessage})")
      }

      case in.ArrayLength(exp) => for {
        expT <- goE(exp)
      } yield ctx.array.length(expT)

      case in.IndexedExp(base, index) => for {
        baseT <- goE(base)
        indexT <- goE(index)
      } yield base.typ match {
        case in.ArrayT(_, t) => ctx.loc.arrayIndex(t, baseT, indexT)(ctx)(pos, info, errT)
        case in.ArraySequenceT(_, _) | in.SequenceT(_) => vpr.SeqIndex(baseT, indexT)(pos, info, errT)
        case t => Violation.violation(s"expected an array or sequence type, but got $t")
      }

      case in.SequenceLength(exp) => for {
        expT <- goE(exp)
      } yield vpr.SeqLength(expT)(pos, info, errT)

      case in.SequenceLiteral(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptySeq(typT)(pos, info, errT)
        case _ => vpr.ExplicitSeq(exprsT)(pos, info, errT)
      }
        
      case in.RangeSequence(low, high) => for {
        lowT <- goE(low)
        highT <- goE(high)
      } yield vpr.RangeSeq(lowT, highT)(pos, info, errT)

      case in.SequenceAppend(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.SeqAppend(leftT, rightT)(pos, info, errT)

      case in.SequenceUpdate(seq, left, right) => for {
        seqT <- goE(seq)
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.SeqUpdate(seqT, leftT, rightT)(pos, info, errT)

      case in.SequenceDrop(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.SeqDrop(leftT, rightT)(pos, info, errT)

      case in.SequenceTake(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.SeqTake(leftT, rightT)(pos, info, errT)

      case in.SetLiteral(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptySet(typT)(pos, info, errT)
        case _ => vpr.ExplicitSet(exprsT)(pos, info, errT)
      }

      case in.SetConversion(exp) => for {
        expT <- goE(exp)
      } yield expT.typ match {
        case vpr.SeqType(_) => ctx.seqToSet.create(expT)
        case t => Violation.violation(s"conversion of type $t to sets is not implemented")
      }

      case in.Union(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.AnySetUnion(leftT, rightT)(pos, info, errT)

      case in.Intersection(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.AnySetIntersection(leftT, rightT)(pos, info, errT)

      case in.SetMinus(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.AnySetMinus(leftT, rightT)(pos, info, errT)

      case in.Subset(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield vpr.AnySetSubset(leftT, rightT)(pos, info, errT)

      case in.Contains(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield rightT.typ match {
        case _ : vpr.SeqType => vpr.SeqContains(leftT, rightT)(pos, info, errT)
        case _ : vpr.SetType | _ : vpr.MultisetType =>
          vpr.AnySetContains(leftT, rightT)(pos, info, errT)
        case t => Violation.violation(s"expected a sequence or (multi)set, but got $t")
      }

      case in.Cardinality(exp) => for {
        expT <- goE(exp)
      } yield vpr.AnySetCardinality(expT)(pos, info, errT)

      case in.MultisetLiteral(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptyMultiset(typT)(pos, info, errT)
        case _ => vpr.ExplicitMultiset(exprsT)(pos, info, errT)
      }

      case in.MultisetConversion(exp) => for {
        expT <- goE(exp)
      } yield expT.typ match {
        case vpr.SeqType(_) => ctx.seqToMultiset.create(expT)
        case t => Violation.violation(s"conversion of type $t to multisets is not implemented")
      }

      case in.Multiplicity(left, right) => for {
        leftT <- goE(left)
        rightT <- goE(right)
      } yield rightT.typ match {
        case vpr.SeqType(_) => ctx.seqMultiplicity.create(leftT, rightT)
        case vpr.SetType(_) => vpr.CondExp(
          vpr.AnySetContains(leftT, rightT)(pos, info, errT),
          vpr.IntLit(1)(pos, info, errT),
          vpr.IntLit(0)(pos, info, errT)
        )(pos, info, errT)
        case vpr.MultisetType(_) => vpr.AnySetContains(leftT, rightT)(pos, info, errT)
        case t => Violation.violation(s"translation for multiplicity with type $t is not implemented")
      }

      case l: in.Lit => ctx.loc.literal(l)(ctx)
      case v: in.Var => ctx.loc.evalue(v)(ctx)
    }
  }
}
