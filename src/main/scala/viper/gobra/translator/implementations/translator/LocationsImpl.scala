package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import in.Addressable.isAddressable
import viper.gobra.reporting.Source
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ArrayUtil, PrimitiveGenerator, Registrator, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.gobra.translator.interfaces.translator.Locations.SubValueRep
import viper.gobra.util.Violation


class LocationsImpl extends Locations {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {
    fieldReg.finalize(col)
  }

  private lazy val fieldReg: Registrator[vpr.Field] = new Registrator[vpr.Field]

  /** fields are registered at [[valAccess]] and [[fieldAccess]] */
  private def regField(x: vpr.FieldAccess): vpr.FieldAccess = {
    fieldReg.register(x.field)
    x
  }

  private lazy val _pointerField: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        // No ref can hold permission to two val fields at once.
        // Therefore, field names based on the viper type are sufficient
        val f = vpr.Field(name = Names.pointerFields(t), typ = t)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        (f, Vector(f))
      }
    )

  private def pointerField(t: in.Type)(ctx: Context): vpr.Field = _pointerField(ctx.typ.translate(t)(ctx))


  /**
    * [v]w -> v
    */
  def variable(v: in.Var)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = v.vprMeta

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.BoundVar(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.Parameter.In(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.Parameter.Out(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case lv: in.LocalVar.Val => variableVal(lv)(ctx)
      case in.LocalVar.Inter(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.LocalVar.Ref(id, t) => t match {
        case _: in.SharedArrayT => unit(vpr.LocalVar(id, ctx.array.typ())(pos, info, errT))
        case _ => unit(vpr.LocalVar(id, vpr.Ref)(pos, info, errT))
      }
      case gc: in.GlobalConst => unit(ctx.fixpoint.get(gc)(ctx))
    }
  }

  /**
    * [v]w -> v
    */
  def variableVal(v: in.Var)(ctx: Context): CodeWriter[vpr.LocalVar] = {
    val (pos, info, errT) = v.vprMeta
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)
    unit(vpr.LocalVar(v.id, goT(v.typ))(pos, info, errT))
  }


  /**
    * Parameter[?x: [n]T] -> { x }
    * Parameter[?x: T] -> { a(x) | a in Values[T] }
    */
  override def parameter(v : in.Declaration)(ctx : Context) : (Vector[vpr.LocalVarDecl], CodeWriter[Unit]) = {
    val (pos, info, errT) = v.vprMeta

    v match {
      case v: in.Var => v.typ match {

        // array case
        case typ: in.ArrayType => {
          val lvar = vpr.LocalVar(v.id, ctx.typ.translate(typ)(ctx))(pos, info, errT)
          val ldecl = vu.toVarDecl(lvar)
          val conditions = ArrayUtil.footprintConditions(ldecl.localVar, typ)(v)(ctx)

          (Vector(ldecl), for {
            _ <- sequence(conditions map { c => wellDef(c) })
          } yield ())
        }

        // default case
        case typ => {
          val trans = values(typ)(ctx)
          val (decls, valueUnit) = sequence(trans map { a =>
            for {
              decl <- a(v)._1.map { x => // top declarations are not addressable, hence x is a variable
                vu.toVarDecl(x.asInstanceOf[vpr.LocalVar])
              }
            } yield decl
          }).cut
          (decls, valueUnit)
        }
      }
    }
  }

  /**
    * OutParameter[?x : [n]T] -> { x }; ensure footprint(x)
    * OutParameter[?x : T] -> { x }
    */
  override def outparameter(v : in.Parameter.Out)(ctx : Context) : (Vector[vpr.LocalVarDecl], CodeWriter[Unit]) = {
    val (decls, valueUnits) = localDecl(v)(ctx)
    val (_, declUnit) = valueUnits.cut
    val ldecls = decls.asInstanceOf[Vector[vpr.LocalVarDecl]]

    v.typ match {
      // array case (generates extra conditions)
      case typ: in.ArrayType => {
        val conditions = ldecls.flatMap(d => {
          ArrayUtil.footprintConditions(d.localVar, typ)(v)(ctx)
        })

        (ldecls, for {
          _ <- declUnit
          _ <- sequence(conditions map(c => wellDef(c)))
        } yield ())
      }

      // default case
      case _ => (ldecls, declUnit)
    }
  }

  /**
    * Arity[T] -> | Values[T] |
    */
  override def arity(typ: in.Type)(ctx: Context): Int =
    values(typ)(ctx).size


  override def ttype(typ: in.Type)(ctx: Context): vpr.Type = {

    ctx.typeProperty.underlyingType(typ)(ctx) match {
      case _: in.StructT =>
        val trans = values(typ)(ctx)
        val multiVar = in.LocalVar.Inter(Names.freshName, typ)(Source.Parser.Unsourced)
        ctx.tuple.typ(
          trans map (a => ctx.typ.translate(a(multiVar)._2.typ)(ctx))
        )

      case _ =>
        Violation.violation(values(typ)(ctx).size == 1, s"expected type of size 1 but got $typ")
        ctx.typ.translate(typ)(ctx)
    }


    val trans = values(typ)(ctx)

    if (trans.size == 1) {
      ctx.typ.translate(typ)(ctx)
    } else {
      val multiVar = in.LocalVar.Inter(Names.freshName, typ)(Source.Parser.Unsourced)

      ctx.tuple.typ(
        trans map (a => ctx.typ.translate(a(multiVar)._2.typ)(ctx))
      )
    }
  }


  override def target(v: in.LocalVar.Val)(ctx: Context): CodeWriter[Vector[vpr.LocalVar]] = {
    val (decls, writer) = parameter(v)(ctx)
    withoutWellDef(writer).map(_ => decls.map(vu.toVar))
  }


  /**
    * Argument[?e: [n]T] -> { e }
    * Argument[!e: [n]T] -> var tmp: [n]T; footprint(tmp); copy(tmp, e); { tmp }
    * Argument[e: T] -> { a(e) | a in Values[T] }
    */
  override def argument(e : in.Expr)(ctx : Context) : CodeWriter[Vector[vpr.Exp]] = e.typ match {
    // exclusive (non-addressable) array case
    case _: in.ExclusiveArrayT => for {
      arg <- ctx.expr.translate(e)(ctx)
    } yield Vector(arg)

    // shared (addressable) array case
    case typ: in.SharedArrayT => for {
      // declare a new 'tmp' variable
      tmp <- variableVal(ArrayUtil.anonymousLocalVar(typ)(e.info))(ctx)
      _ <- local(vu.toVarDecl(tmp))
      // inhale the footprint of 'tmp'
      footprint = ArrayUtil.footprintAssumptions(tmp, typ)(e)(ctx)
      _ <- sequence(footprint map (a => write(a)))
      // assume that every entry of `tmp` equals the one of `e`
      arg <- ctx.expr.translate(e)(ctx)
      comparison = ArrayUtil.equalsAssumption(tmp, typ, arg, typ)(e)(ctx)
      _ <- write(comparison)
    } yield Vector(tmp)

    // default case
    case typ => {
      val trans = values(typ)(ctx)
      sequence(trans map (a => a(e)._1))
    }
  }


  /**
    * [decl x: [n]T] -> InitValue<x>
    * [decl x: T] -> InitValue<x>; FOREACH a in Values[T]. a(x) := Default(Type(a(x)))
    */
  override def localDecl(v: in.BottomDeclaration)(ctx: Context): (Vector[vpr.Declaration], CodeWriter[vpr.Stmt]) = {
    v match {
      case v: in.Var => v.typ match {
        case _: in.ArrayType => {
          val valueInits = initValues(v)(ctx)
          val (decls, valueUnit) = valueInits.cut
          (decls, seqnUnit(valueUnit))
        }

        case _ => {
          val valueInits = initValues(v)(ctx)
          val (decls, valueUnit) = valueInits.cut
          val as = values(v.typ)(ctx).map(_(v))
          val valueAssigns = seqns(as map { case (r, t) =>
            for {
              ax <- r
              dflt <- ctx.loc.defaultValue(t.typ)(v)(ctx)
            } yield valueAssign(ax, dflt)(v)
          })
          (decls, valueUnit flatMap (_ => valueAssigns))
        }
      }
    }
  }


  override def initialize(v: in.TopDeclaration)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (pos, info, errT) = v.vprMeta
    v match {
      case v: in.BottomDeclaration => localDecl(v)(ctx)._2
      case v: in.Parameter => unit(vu.nop(pos, info, errT)) // parameters are not initialized
    }
  }


  /**
    * [r] -> Copy<R[r]>
    *
    * translates a location for an expression position
    */
  override def evalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp] = copy(l)(ctx)


  /**
    * Copy[e: T]  -> var z; FOREACH a in Values[T]. Init<a(z)>; a(z) := a(e) ~ z
    */
  def copy(e : in.Expr)(ctx : Context) : CodeWriter[vpr.Exp] = {

    ctx.typeProperty.underlyingType(e.typ)(ctx) match {
      case _: in.StructT => { // TODO: We could optimize the single var case
        val trans = values(e.typ)(ctx)
        val multiVar = in.LocalVar.Inter(Names.freshName, e.typ)(e.info)
        val assigns = sequence(trans map { a =>
          for {
            right <- a(e)._1
            left <- a(multiVar)._1
            l = left.asInstanceOf[vpr.LocalVar]
            _ <- global(vu.toVarDecl(l))
            _ <- bind(l, right)
          } yield ()
        })

        assigns.flatMap(_ => variable(multiVar)(ctx))
      }

      case _: in.ArrayType => rvalue(e)(ctx)

      case _ => {
        Violation.violation(values(e.typ)(ctx).size == 1, s"expected type of size 1 but got $e")
        rvalue(e)(ctx)
      }
    }
  }



  /**
    * T[e: T] -> tuple( a(e) | a in Values[T] )
    */
  def tvalue(exp: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    argument(exp)(ctx) map ctx.tuple.create
  }


  /**
    * CopyFromTuple<t: T> -> var z; FOREACH a in Values[T]. Init<a(z)>; a(z) = get_i(t) ~ z
    */
  override def copyFromTuple(tuple: vpr.Exp, typ: in.Type)(ctx: Context): CodeWriter[vpr.Exp] = {

    val trans = values(typ)(ctx)
    val arity = trans.size

    if(arity == 1) {
      unit(tuple)
    } else {
      val multiVar = in.LocalVar.Inter(Names.freshName, typ)(Source.Parser.Single.fromVpr(tuple))

      val assigns = sequence(trans.zipWithIndex map { case(a, idx) =>
        val right = ctx.tuple.get(tuple, idx, arity)
        for {
          left <- a(multiVar)._1
          l = left.asInstanceOf[vpr.LocalVar]
          _ <- global(vu.toVarDecl(l))
          _ <- bind(l, right)
        } yield ()
      })

      assigns.flatMap(_ => variable(multiVar)(ctx))
    }
  }


  /**
    * [l: T == r] -> FORALL a in Values[T]. a(l) == a(r)
    */
  override def equal(lhs: in.Expr, rhs: in.Expr)(src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta

    lhs.typ match {
      case ltyp: in.ArrayType => for {
        l <- ctx.expr.translate(lhs)(ctx)
        r <- ctx.expr.translate(rhs)(ctx)
        rtyp = rhs.typ.asInstanceOf[in.ArrayType] // ensured to be valid by the type checker
      } yield ArrayUtil.equalsCondition(l, ltyp, r, rtyp)(src)(ctx)

      case typ => {
        val trans = values(typ)(ctx)
        sequence(trans map { a =>
          for {
            l <- a(lhs)._1
            r <- a(rhs)._1
          } yield vpr.EqCmp(l ,r)(pos, info, errT)
        }).map(vu.bigAnd(_)(pos, info, errT))
      }
    }
  }

  /**
    * Gives a Viper field access representing "`array`[`index`]",
    * with `typ` the (result) type of the field access.
    * Expects `base` to be the translation of an addressable expression.
    */
  override def arrayIndexField(base : vpr.Exp, index : vpr.Exp, fieldType : in.Type)(ctx: Context)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo) : vpr.FieldAccess = {
    val field = fieldType match {
      case _: in.ArrayType => _pointerField(ctx.array.typ())
      case _ => pointerField(fieldType)(ctx)
    }

    regField(vpr.FieldAccess(
      ctx.array.slot(base, index)(pos, info, errT),
      field
    )(pos, info, errT))
  }

  def arrayIndexField(base : vpr.Exp, index : BigInt, fieldType : in.Type)(ctx: Context)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo) : vpr.FieldAccess =
    arrayIndexField(base, vpr.IntLit(index)(pos, info, errT), fieldType)(ctx)(pos, info, errT)

  /**
    * [default(T)] -> var l; FOREACH a in Values[T]. a(l) := simpleDefault(Type(a(l)))
    */
  override def defaultValue(t: in.Type)(src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = src.vprMeta

    val leafVal: PartialFunction[in.Type, CodeWriter[vpr.Exp]] = {
      case in.BoolT => unit(vpr.FalseLit()(pos, info, errT))
      case in.IntT => unit(vpr.IntLit(0)(pos, info, errT))
      case in.PermissionT => unit(vpr.NoPerm()(pos, info, errT))
      case in.PointerT(_) => unit(vpr.NullLit()(pos, info, errT))
      case in.NilT => unit(vpr.NullLit()(pos, info, errT))
      case in.SequenceT(elem) => {
        val elemT = ctx.typ.translate(elem)(ctx)
        unit(vpr.EmptySeq(elemT)(pos, info, errT))
      }
      case in.SetT(elem) => {
        val elemT = ctx.typ.translate(elem)(ctx)
        unit(vpr.EmptySet(elemT)(pos, info, errT))
      }
      case in.MultisetT(elem) => {
        val elemT = ctx.typ.translate(elem)(ctx)
        unit(vpr.EmptyMultiset(elemT)(pos, info, errT))
      }
    }

    ctx.typeProperty.underlyingType(t)(ctx) match {
      // array case
      case typ: in.ArrayType => for {
        // declare a new 'tmp' variable
        tmp <- variableVal(ArrayUtil.anonymousLocalVar(typ)(src.info))(ctx)
        _ <- local(vu.toVarDecl(tmp))
        // assume/inhale the (memory) footprint of 'tmp'
        footprint = ArrayUtil.footprintAssumptions(tmp, typ)(src)(ctx)
        _ <- sequence(footprint map (a => write(a)))
        // assume that all entries of 'tmp' have the expected default value
        defaultvalues = ArrayUtil.defaultValueAssumption(tmp, typ)(src)(ctx)
        _ <- write(defaultvalues)
      } yield tmp

      // struct case
      case _: in.StructT => {
        val trans = values(t)(ctx)
        val v = in.LocalVar.Inter(Names.freshName, t)(src.info)

        sequence(trans map { a =>
          val (wx, t) = a(v)
          for {
            x <- wx
            xVar = x.asInstanceOf[vpr.LocalVar]
            _ <- local(vu.toVarDecl(xVar))
            dv <- leafVal(ctx.typeProperty.underlyingType(t.typ)(ctx))
            _ <- bind(xVar, dv)
          } yield ()
        }).flatMap(_ => variable(v)(ctx))
      }

      // default case
      case ut if leafVal.isDefinedAt(ut) => leafVal(ut)
    }
  }


  /**
    * [b] -> b
    * [n] -> n
    * [seq T { e1,...,en }] -> Seq [T] { [e1],...,[en] }
    * [set T { e1,...,en }] -> Set [T] { [e1],...,[en] }
    * [mset T { e1,...,en }] -> Multiset [T] { [e1],...,[en] }
    * [s(F: E)] -> var l; FOREACH
    */
  override def literal(lit: in.Lit)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = lit.vprMeta

    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    lit match {
      case in.IntLit(v) => unit(vpr.IntLit(v)(pos, info, errT))
      case in.BoolLit(b) => unit(vpr.BoolLit(b)(pos, info, errT))
      case in.NilLit() => unit(vpr.NullLit()(pos, info, errT))

      case lit: in.ArrayLit => ctx.expr.translate(lit.asSeqLit)(ctx)

      case in.SequenceLit(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptySeq(typT)(pos, info, errT)
        case _ => vpr.ExplicitSeq(exprsT)(pos, info, errT)
      }

      case in.SetLit(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptySet(typT)(pos, info, errT)
        case _ => vpr.ExplicitSet(exprsT)(pos, info, errT)
      }

      case in.MultisetLit(typ, exprs) => for {
        exprsT <- sequence(exprs map goE)
        typT = goT(typ)
      } yield exprsT.length match {
        case 0 => vpr.EmptyMultiset(typT)(pos, info, errT)
        case _ => vpr.ExplicitMultiset(exprsT)(pos, info, errT)
      }

      case in.StructLit(typ, args) =>
        val lhsTrans = values(typ)(ctx)
        val rhsTrans = args map (arg => (arg, values(arg.typ)(ctx)))
        val partLengths = rhsTrans map { case (_, vec) => vec.size }

        Violation.violation(lhsTrans.size == partLengths.sum, "encountered ill formed literal")

        var partialLhsTrans = lhsTrans

        val splitLhsTrans = partLengths.map{ len =>
          val (segment, remainder) = partialLhsTrans.splitAt(len)
          partialLhsTrans = remainder
          segment
        }

        val z = in.LocalVar.Inter(Names.freshName, typ)(lit.info)

        sequence((splitLhsTrans zip rhsTrans).flatMap{ case (aLhss ,(arg, aRhss)) =>
          (aLhss zip aRhss) map { case (aLhs, aRhs) =>
            for {
              lhs <- aLhs(z)._1
              lhsVar = lhs.asInstanceOf[vpr.LocalVar]
              _ <- local(vu.toVarDecl(lhsVar))
              rhs <- aRhs(arg)._1
              _ <- bind(lhsVar, rhs)
            } yield ()
          }
        }).flatMap(_ => variable(z)(ctx))
    }
  }

  private def indexedExclArrayAssignment(left : in.Expr, right : vpr.Exp)(ctx : Context) : CodeWriter[vpr.Seqn] = {
    val (pos, info, errT) = left.vprMeta

    left match {
      case left : in.IndexedExp => {
        val tmpVar = in.LocalVar.Inter(Names.freshName, left.base.typ)(left.info)
        val tmpTyp = ctx.typ.translate(left.base.typ)(ctx)
        val tmp = vpr.LocalVar(tmpVar.id, tmpTyp)(pos, info, errT)

        for {
          baseT <- ctx.expr.translate(left.base)(ctx)
          indexT <- ctx.expr.translate(left.index)(ctx)
          tmpDecl : vpr.Declaration = vu.toVarDecl(tmp)
          tmpAssign : vpr.Stmt = valueAssign(tmp, vpr.SeqUpdate(baseT, indexT, right)(pos, info, errT))(left)
          stmtD <- indexedExclArrayAssignment(left.base, tmp)(ctx)
        } yield vpr.Seqn(tmpAssign +: stmtD.ss, tmpDecl +: stmtD.scopedDecls)(pos, info, errT)
      }

      case _ => for {
        leftT <- ctx.expr.translate(left)(ctx)
        leftAssign = valueAssign(leftT, right)(left)
      } yield vpr.Seqn(Seq(leftAssign), Seq())(pos, info, errT)
    }
  }

  /**
    * [!r: [n]T = e] -> var tmp: Array; footprint(tmp); copy(tmp, e); r := tmp
    * [?r: [n]T = e] -> var tmp: Seq[T]; footprint(tmp); copy(tmp, e); r := tmp
    * [!r: T = e] -> FOREACH a in Values[T]. a(r) := a(e)
    */
  override def assignment(ass : in.SingleAss)(ctx : Context) : CodeWriter[vpr.Stmt] = {
    val (pos, info, errT) = ass.vprMeta
    val right = ass.right

    ass.left.op match {
      case left: in.IndexedExp if left.base.typ.isInstanceOf[in.ExclusiveArrayT] => for {
        rightT <- ctx.expr.translate(right)(ctx)
        stmtsT <- indexedExclArrayAssignment(left, rightT)(ctx)
      } yield stmtsT

      case left => left.typ match {
        case _: in.ExclusiveArrayT if right.typ.isInstanceOf[in.ExclusiveArrayT] => for {
          lhs <- ctx.expr.translate(left)(ctx)
          rhs <- ctx.expr.translate(right)(ctx)
        } yield valueAssign(lhs, rhs)(ass)

        case leftTyp: in.ArrayType => {
          val tmpVar = ArrayUtil.anonymousLocalVar(leftTyp)(ass.info)
          val tmpTyp = ctx.typ.translate(leftTyp)(ctx)
          val tmp = vpr.LocalVar(tmpVar.id, tmpTyp)(pos, info, errT)

          for {
            leftT <- ctx.expr.translate(left)(ctx)
            rightT <- ctx.expr.translate(right)(ctx)
            tmpDecl = vu.toVarDecl(tmp)
            footprint = ArrayUtil.footprintAssumptions(tmp, leftTyp)(ass)(ctx)
            comparison = ArrayUtil.equalsAssumption(tmp, leftTyp, rightT, right.typ.asInstanceOf[in.ArrayType])(ass)(ctx)
            leftAssign = valueAssign(leftT, tmp)(ass)
          } yield vpr.Seqn(footprint ++ Seq(comparison, leftAssign), Seq(tmpDecl))(pos, info, errT)
        }

        case leftTyp => {
          val trans = values(leftTyp)(ctx)

          seqns(trans map { a => for {
            l <- a(left)._1
            r <- a(right)._1
          } yield valueAssign(l, r)(ass) })
        }
      }
    }
  }


  /** left := right */
  private def valueAssign(left: vpr.Exp, right: vpr.Exp)(src: in.Node): vpr.AbstractAssign = {
    val (pos, info, errT) = src.vprMeta
    left match {
      case l: vpr.LocalVar => vpr.LocalVarAssign(l, right)(pos, info, errT)
      case l: vpr.FieldAccess => vpr.FieldAssign(l, right)(pos, info, errT)
      case _ => Violation.violation(s"expected vpr variable or field access, but got $left")
    }
  }


  /**
    * [v := make(lit: S)] -> v := new(); InitValues[*v], [Foreach (f, e) in lit. (*v).f = e ]
    */
  override def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (pos, info, errT) = mk.vprMeta
    val src = mk.info

    mk.typ match {
      case in.CompositeObject.Array(_) => Violation.violation("not yet implemented")

      case in.CompositeObject.Struct(slit) => {
        val deref = in.Deref(mk.target)(src)
        val fieldZip = ctx.typeProperty.structType(slit.typ)(ctx).get.fields.zip(slit.args)
        val perField = fieldZip.flatMap { case (f, e) =>
          val fieldRef = in.FieldRef(deref, f)(src)
          // val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
          val init = in.SingleAss(in.Assignee.Field(fieldRef), e)(src)
          Vector(init)
        }

        seqn {
          for {
            vTarget <- variableVal(mk.target)(ctx)
            _ <- write(vpr.NewStmt(vTarget, Vector.empty)(pos, info, errT))
            _ <- initValues(deref)(ctx)
            vMake <- seqns(perField map ctx.stmt.translateF(ctx))
          } yield vMake
        }
      }

      case in.CompositeObject.Sequence(_) => Violation.violation("not yet implemented")
      case in.CompositeObject.Set(_) => Violation.violation("not yet implemented")
      case in.CompositeObject.Multiset(_) => Violation.violation("not yet implemented")
    }
  }


  /**
    *
    *
    * [acc(*e )] -> PointerAcc[*e]
    * [acc(e.f)] -> FieldAcc[e.f] && PointerAcc[e.f]
    * [acc(  p(as)] -> p(Argument[as])
    * [acc(e.p(as)] -> p(Argument[e], Argument[as])
    * [acc(a[i])] -> FieldAcc(aslot([a],[i]).f),
    *   with `f` the field generated for the type of `a`.
    *
    * // an idea:
    * [acc(&!e.!f)] -> ProjAcc[e](path(e.f);f)
    * [acc(&!x)]    -> true
    *
    * PointerAcc[!r: S] -> true
    * PointerAcc[!r: T] -> acc(A[r].val)
    * PointerAcc[?r]    -> true
    *
    * FieldAcc[!e.f] -> acc(R[e].f)
    * FieldAcc[?e.f] -> true
    *
    *
    *
    * translates a a field access keeping the last address
    */
  override def access(acc: in.Access)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = acc.vprMeta

    val perm = vpr.FullPerm()(pos, info, errT)

    def pointerAcc(recv: in.Location): CodeWriter[vpr.Exp] = {
      if (isAddressable(recv) && !ctx.typeProperty.isStructType(recv.typ)(ctx)) {
        for {
          r <- avalue(recv)(ctx)
        } yield vpr.FieldAccessPredicate(valAccess(r, recv.typ)(acc)(ctx), perm)(pos, info, errT)
      } else unit(vpr.TrueLit()(pos, info, errT))
    }

    def fieldAcc(fieldRef: in.FieldRef): CodeWriter[vpr.Exp] = {
      if (isAddressable(fieldRef.recv)) {
        for {
          r <- rvalue(fieldRef.recv)(ctx)
          facc = fieldAccess(r, fieldRef.field, isAddressable(fieldRef))(acc)(ctx)
        } yield vpr.FieldAccessPredicate(facc, perm)(pos, info, errT)
      } else unit(vpr.TrueLit()(pos, info, errT))
    }

    acc.e match {
      case in.Accessible.Pointer(der) => pointerAcc(der)

      case in.Accessible.Field(fa) =>
        for {
          path <- fieldAcc(fa)
          pointer <- pointerAcc(fa)
        } yield vpr.And(path, pointer)(pos, info, errT)

      case in.Accessible.Predicate(op) => predicateAccess(op)(ctx)

      case in.Accessible.Index(op) => for {
        baseT <- ctx.expr.translate(op.base)(ctx)
        indexT <- ctx.expr.translate(op.index)(ctx)
        lhs = arrayIndexField(baseT, indexT, op.typ)(ctx)(pos, info, errT)
      } yield vpr.FieldAccessPredicate(lhs, perm)(pos, info, errT)
    }
  }


  /**
    * [acc(  p(as)] -> p(Argument[as])
    * [acc(e.p(as)] -> p(Argument[e], Argument[as])
    */
  override def predicateAccess(acc: in.PredicateAccess)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate] = {

    val (pos, info, errT) = acc.vprMeta
    val perm = vpr.FullPerm()(pos, info, errT)

    acc match {
      case in.FPredicateAccess(pred, args) =>
        for {
          vArgss <- sequence(args map (argument(_)(ctx)))
          pacc = vpr.PredicateAccess(vArgss.flatten, pred.name)(pos, info, errT)
        } yield vpr.PredicateAccessPredicate(pacc, perm)(pos, info, errT)

      case in.MPredicateAccess(recv, pred, args) =>
        for {
          vRecvs <- ctx.loc.argument(recv)(ctx)
          vArgss <- sequence(args map (argument(_)(ctx)))
          pacc = vpr.PredicateAccess(vRecvs ++ vArgss.flatten, pred.uniqueName)(pos, info, errT)
        } yield vpr.PredicateAccessPredicate(pacc, perm)(pos, info, errT)

      case in.MemoryPredicateAccess(_) =>
        Violation.violation("memory predicate accesses are not supported")
    }
  }


  /**
    * Values[[n]T] -> { \r. R[v(r[i])] | 0 <= i < n, v in Values[T] }
    * Values[S] -> { \r. R[r.fs] | fs in ValuePaths[S] }
    * Values[T] -> { \r. R[r] }
    */
  override def values(t: in.Type)(ctx: Context): Vector[in.Expr => (CodeWriter[vpr.Exp], SubValueRep)] = {
    def extendBase(base: in.Expr, fields: Vector[in.Field]): in.Expr = {
      fields.foldLeft(base){ case (b, f) => in.FieldRef(b, f)(b.info) }
    }

    t match {
      case u if ctx.typeProperty.isStructType(u)(ctx) =>
        valuePaths(u)(ctx).map(fs => (r: in.Expr) => (rvalue(extendBase(r, fs))(ctx), SubValueRep(fs.last.typ)))

      case t : in.ArrayType => values(t.typ)(ctx).flatMap(v => {
        Range.BigInt(0, t.length, 1).map(i => (r : in.Expr) => {
          v(in.IndexedExp(r, in.IntLit(i)(r.info))(r.info))
        })
      })

      case _ => Vector(r => (rvalue(r)(ctx), SubValueRep(r.typ)))
    }
  }

  /**
    * InitValue[?e: S] -> { InitValue[e.f] | f in S }
    * InitValue[!e: S] -> { Init<R[e]> } + { InitValue[e.f] | f in S }
    * InitValue[?e: T] -> { Init<R[e]> }
    * InitValue[!e: T] -> { Init<A[e]>, Init<R[e]> }
    */
  private def initValues(base: in.Expr)(ctx: Context): CodeWriter[Vector[vpr.LocalVarDecl]] = {

    if (isAddressable(base)) {
      val vprABase = avalue(base.asInstanceOf[in.Location])(ctx)

      base.typ match {
        // struct case
        case u if ctx.typeProperty.isStructType(u)(ctx) => {
          val st = ctx.typeProperty.structType(u)(ctx).get
          val fieldInits = sequence(st.fields map { f =>
            val ext = in.FieldRef(base, f)(base.info)
            initValues(ext)(ctx)
          }).map(_.flatten)
          for {
            r <- vprABase;
            a <- init(r)(base);
            b <- fieldInits
          } yield a ++ b
        }

        // array case
        case u if ctx.typeProperty.isArrayType(u)(ctx) => for {
          r <- vprABase
          a <- init(r)(base)
          typ = ctx.typeProperty.arrayType(u)(ctx).get
          footprint = ArrayUtil.footprintAssumptions(r, typ)(base)(ctx)
          _ <- sequence(footprint map (a => write(a)))
          _ <- write(ArrayUtil.defaultValueAssumption(r, typ)(base)(ctx))
        } yield a

        // default case
        case _ => {
          val vprRBase = rvalue(base.asInstanceOf[in.Location])(ctx)
          for {
            ar <- vprABase;
            a <- init(ar)(base);
            br <- vprRBase;
            b <- init(br)(base)
          } yield a ++ b
        }
      }
    } else {
      val vprBase = rvalue(base)(ctx)

      base.typ match {
        // struct case
        case u if ctx.typeProperty.isStructType(u)(ctx) => {
          val st = ctx.typeProperty.structType(u)(ctx).get
          sequence(st.fields map { f =>
            val ext = in.FieldRef(base, f)(base.info)
            initValues(ext)(ctx)
          }).map(_.flatten)
        }

        // array case
        case u if ctx.typeProperty.isArrayType(u)(ctx) => for {
          r <- vprBase
          a <- init(r)(base)
          typ = ctx.typeProperty.arrayType(u)(ctx).get
          footprint = ArrayUtil.footprintAssumptions(r, typ)(base)(ctx)
          _ <- sequence(footprint map (a => write(a)))
          _ <- write(ArrayUtil.defaultValueAssumption(r, typ)(base)(ctx))
        } yield a

        // default case
        case _ => for {
          r <- vprBase
          a <- init(r)(base)
        } yield a
      }
    }
  }

  /**
    * Init<x>   -> var x
    * Init<e.f> -> inhale acc(e.f)
    */
  def init(e: vpr.Exp)(src: in.Node): CodeWriter[Vector[vpr.LocalVarDecl]] = {
    val (pos, info, errT) = src.vprMeta
    e match {
      case v: vpr.LocalVar => unit(Vector(vu.toVarDecl(v)))
      case f: vpr.FieldAccess =>
        write(vpr.Inhale(vpr.FieldAccessPredicate(f, vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT)).map(_ => Vector.empty)
      case _ => Violation.violation(s"expected variable of field access, but got $e")
    }
  }


  /**
    * ValuePaths[S] -> { (f) | (f: not S') in S } + { f,g | (f: S') in S and g in ValuePaths[S'] }
    */
  private def valuePaths(t: in.Type)(ctx: Context): Vector[Vector[in.Field]] = {
    require(ctx.typeProperty.isStructType(t)(ctx))
    val st = ctx.typeProperty.structType(t)(ctx).get

    st.fields.flatMap{
      case f if ctx.typeProperty.isStructType(f.typ)(ctx) =>
        valuePaths(f.typ)(ctx).map(fs => f +: fs)
      case f => Vector(Vector(f))
    }
  }


  /**
    * A[!x] -> x
    * A[*e] -> R[e]
    * A[!r.!f] -> R[r].f
    * A[?r.!f] -> R[r]#f
    *
    * translates to address of a location for a receiver position
    */
  def avalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp] = {
    require(isAddressable(l))

    l match {
      case v: in.Var => variable(v)(ctx)
      case in.Deref(exp, _) => rvalue(exp)(ctx)
      case in.FieldRef(recv, field) =>
        if (isAddressable(recv)) for { r <- rvalue(recv)(ctx) } yield fieldAccess(r, field, addressableField = true)(l)(ctx)
        else for { r <- rvalue(recv)(ctx) } yield fieldExtension(r, field, addressableField = true)(l)(ctx)

      case _ => Violation.violation(s"encountered unexpected addressable location $l")
    }
  }


  /**
    * R[!r: S] -> A[r]
    * R[!r: T] -> A[r].val
    * R[?x]  -> x
    * R[&r] -> assert A[r] != null; A[r]
    * A[!r.?f] -> R[r].f
    * A[?r.?f] -> R[r]#f
    *
    * tpo
    */
  def rvalue(l: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = l.vprMeta

    l match {
      case l: in.Location => if (isAddressable(l)) {
        l.typ match {
          case typ if ctx.typeProperty.isStructType(typ)(ctx) => avalue(l)(ctx)
          case typ if ctx.typeProperty.isArrayType(typ)(ctx) => avalue(l)(ctx)
          case typ => for {
            a <- avalue(l)(ctx)
          } yield valAccess(a, l.typ)(l)(ctx)
        }
      } else {
        l match {
          case x: in.Var => variable(x)(ctx)

          case ref: in.Ref =>
            for {
              address <- avalue(ref.ref.op)(ctx)
              // assert address != null
              _ <- wellDef(vpr.NeCmp(address, vpr.NullLit()(pos, info, errT))(pos, info, errT))
            } yield address

          case in.FieldRef(recv, field) =>
            if (isAddressable(recv)) for { r <- rvalue(recv)(ctx) } yield fieldAccess(r, field, addressableField = false)(l)(ctx)
            else for { r <- rvalue(recv)(ctx) } yield fieldExtension(r, field, addressableField = false)(l)(ctx)

          case _ => Violation.violation(s"unexpected location: $l")
        }
      }

      case _ => ctx.expr.translate(l)(ctx)
    }
  }



  /** [f] -> f */
  def field(f: in.Field, addressableField: Boolean)(ctx: Context): vpr.Field = {
    val (pos, info, errT) = f.vprMeta
    if (addressableField) {
      vpr.Field(Names.addressableField(f.name), vpr.Ref)(pos, info, errT)
    } else {
      vpr.Field(Names.nonAddressableField(f.name), ctx.typ.translate(f.typ)(ctx))(pos, info, errT)
    }
  }

  /** e # f */
  private def fieldExtension(x: vpr.Exp, f: in.Field, addressableField: Boolean)(src: in.Node)(ctx: Context): vpr.Lhs = {
    val (pos, info, errT) = src.vprMeta
    val vprF = field(f, addressableField)(ctx)
    x match {
      case z: vpr.LocalVar =>
        z.copy(name = Names.fieldExtension(z.name, vprF.name), typ = vprF.typ)(pos, info, errT)

      case z: vpr.FieldAccess =>
        z.copy(
          field = z.field.copy(name = Names.fieldExtension(z.field.name, vprF.name), typ = vprF.typ)(pos, info, errT)
        )(pos, info, errT)

      case _ => Violation.violation(s"expected vpr variable or field access, but got $x")
    }
  }

  /** e.f */
  private def fieldAccess(x: vpr.Exp, f: in.Field, addressableField: Boolean)(src: in.Node)(ctx: Context): vpr.FieldAccess = {
    val (pos, info, errT) = src.vprMeta
    val res = vpr.FieldAccess(x, field(f, addressableField)(ctx))(pos, info, errT)
    val complete = true // TODO: refine (only register field when it corresponds to a proper type)
    if (complete) { regField(res) }
    res
  }


  /** e.val */
  private def valAccess(x: vpr.Exp, t: in.Type)(src: in.Node)(ctx: Context): vpr.FieldAccess = {
    val (pos, info, errT) = src.vprMeta
    regField(vpr.FieldAccess(x, pointerField(t)(ctx))(pos, info, errT))
  }


  // Utils

  private def createMultiVar: vpr.LocalVar =
    vpr.LocalVar(Names.freshName, vpr.Int)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

  private def inverseVar(x: vpr.LocalVar, typ: in.Type)(info: Source.Parser.Info): in.LocalVar.Val =
    in.LocalVar.Val(x.name, typ)(info)
}
