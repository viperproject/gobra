package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import in.Addressable.isAddressable
import viper.gobra.reporting.Source
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{PrimitiveGenerator, Registrator, ViperUtil => vu}
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
  def variable(v: in.Var)(ctx: Context): CodeWriter[vpr.LocalVar] = {

    val (pos, info, errT) = v.vprMeta

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.Parameter(id, t)    => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.LocalVar.Val(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.LocalVar.Inter(id, t) => unit(vpr.LocalVar(id, goT(t))(pos, info, errT))
      case in.LocalVar.Ref(id, _) => unit(vpr.LocalVar(id, vpr.Ref)(pos, info, errT))
    }
  }


  /**
    * Parameter[?x: T] -> { a(x) | a in Values[T] }
    */
  override def parameter(v: in.TopDeclaration)(ctx: Context): (Vector[vpr.LocalVarDecl], CodeWriter[Unit]) = {
    v match {
      case v: in.Var =>
        val trans = values(v.typ)(ctx)
        sequence(trans map { a =>
          a(v)._1.map{ x => // top declarations are not addressable, hence x is a variable
            vu.toVarDecl(x.asInstanceOf[vpr.LocalVar])
          }
        }).cut
    }
  }


  /**
    * Arity[T] -> | Values[T] |
    */
  override def arity(typ: in.Type)(ctx: Context): Int =
    values(typ)(ctx).size


  override def ttype(typ: in.Type)(ctx: Context): vpr.Type = {
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
    * Argument[e: T] -> { a(e) | a in Values[T] }
    */
  override def argument(e: in.Expr)(ctx: Context): CodeWriter[Vector[vpr.Exp]] = {
    val trans = values(e.typ)(ctx)
    sequence(trans map (a => a(e)._1))
  }


  /**
    * [decl x: T] -> InitValue<x>; FOREACH a in Values[T]. a(x) := Default(Type(a(x)))
    */
  override def localDecl(v: in.BottomDeclaration)(ctx: Context): (Vector[vpr.Declaration], CodeWriter[vpr.Stmt]) = {
    v match {
      case v: in.Var =>
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
  def copy(e: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
//    copyConsume(e.typ)(a => a(e)._1){ case (left, a) =>
//      a(e)._1 flatMap (right => bind(left, right))
//    }(ctx)

    val trans = values(e.typ)(ctx)

    if(trans.size == 1) {
      trans.head(e)._1
    } else {

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
    val trans = values(lhs.typ)(ctx)
    sequence(trans map { a =>
      for {
        l <- a(lhs)._1
        r <- a(rhs)._1
      } yield vpr.EqCmp(l ,r)(pos, info, errT)
    }).map(vu.bigAnd(_)(pos, info, errT))
  }


  /**
    * [default(T)] -> var l; FOREACH a in Values[T]. a(l) := simpleDefault(Type(a(l)))
    */
  override def defaultValue(t: in.Type)(src: in.Node)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = src.vprMeta

    def leafVal(t: in.Type): CodeWriter[vpr.Exp] = {
      t match {
        case in.BoolT => unit(vpr.FalseLit()(pos, info, errT))
        case in.IntT => unit(vpr.IntLit(0)(pos, info, errT))
        case in.PermissionT => unit(vpr.NoPerm()(pos, info, errT))
        case t: in.DefinedT => ctx.loc.defaultValue(ctx.typeProperty.underlyingType(t)(ctx))(src)(ctx)
        case in.PointerT(_) => unit(vpr.NullLit()(pos, info, errT))
        case in.NilT => unit(vpr.NullLit()(pos, info, errT))
        case _ => Violation.violation(s"encountered unexpected inner type $t")
      }
    }

    val trans = values(t)(ctx)
    val v = in.LocalVar.Inter(Names.freshName, t)(src.info)

    if (trans.size == 1) {
      val (_, it) = trans.head(v)
      leafVal(it.typ)
    } else {
      sequence(trans map { a =>
        val (wx, t) = a(v)
        for {
          x <- wx
          xVar = x.asInstanceOf[vpr.LocalVar]
          _ <- local(vu.toVarDecl(xVar))
          dv <- leafVal(t.typ)
          _ <- bind(xVar, dv)
        } yield ()
      }).flatMap(_ => variable(v)(ctx))
    }
  }


  /**
    * [b] -> b
    * [n] -> n
    * [s(F: E)] -> var l; FOREACH
    */
  override def literal(lit: in.Lit)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = lit.vprMeta

    lit match {
      case in.IntLit(v) => unit(vpr.IntLit(v)(pos, info, errT))
      case in.BoolLit(b) => unit(vpr.BoolLit(b)(pos, info, errT))
      case in.NilLit() => unit(vpr.NullLit()(pos, info, errT))
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


  /**
    * [!r: T = e] -> FOREACH a in Values[T]. a(r) := a(e)
    */
  override def assignment(ass: in.SingleAss)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val trans = values(ass.left.op.typ)(ctx)
    seqns(trans map { a =>
      for{l <- a(ass.left.op)._1; r <- a(ass.right)._1} yield valueAssign(l, r)(ass)
    })
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
    * [v := make(lit: S)] -> [Foreach (f, e) in lit. inhale(acc(v.f)); v.f = e ]
    */
  override def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (pos, info, errT) = mk.vprMeta
    val src = mk.info

    mk.typ match {
      case in.CompositeObject.Struct(slit) =>
        val interVar = mk.target
        val fieldZip = ctx.typeProperty.structType(slit.typ)(ctx).get.fields.zip(slit.args)
        val perField = fieldZip.flatMap{ case (f, e) =>
          val fieldRef = in.FieldRef(in.Deref(interVar)(src), f)(src)
          val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
          val init = in.SingleAss(in.Assignee.Field(fieldRef), e)(src)
          Vector(inhalePermission, init)
        }

        seqn{
          for {
            vTarget <- variable(mk.target)(ctx)
            _ <- write(vpr.NewStmt(vTarget, Vector.empty)(pos, info, errT))
            vMake <- seqns(perField map ctx.stmt.translateF(ctx))
          } yield vMake
        }
    }
  }


  /**
    * [acc(*e )] -> PointerAcc[*e]
    * [acc(e.f)] -> FieldAcc[e.f] && PointerAcc[e.f]
    * [acc(  p(as)] -> p(Argument[as])
    * [acc(e.p(as)] -> p(Argument[e], Argument[as])
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
      case _ =>
        Vector(r => (rvalue(r)(ctx), SubValueRep(r.typ)))
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
      ctx.typeProperty.structType(base.typ)(ctx) match {
        case None =>
          val vprRBase = rvalue(base.asInstanceOf[in.Location])(ctx)
          for { ar <- vprABase; a <- init(ar)(base); br <- vprRBase; b <- init(br)(base) } yield a ++ b
        case Some(st) =>
          val fieldInits = sequence(st.fields map { f =>
            val ext = in.FieldRef(base, f)(base.info)
            initValues(ext)(ctx)
          }).map(_.flatten)
          for { r <- vprABase; a <- init(r)(base); b <- fieldInits } yield a ++ b
      }
    } else {
      val vprBase = rvalue(base)(ctx)
      ctx.typeProperty.structType(base.typ)(ctx) match {
        case None => for { r <- vprBase; a <- init(r)(base) } yield a
        case Some(st) =>
          sequence(st.fields map { f =>
            val ext = in.FieldRef(base, f)(base.info)
            initValues(ext)(ctx)
          }).map(_.flatten)
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
    * translates a location for a receiver position
    */
  def rvalue(l: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = l.vprMeta

    l match {
      case l: in.Location =>
        if (isAddressable(l)) {
          if (ctx.typeProperty.isStructType(l.typ)(ctx)) avalue(l)(ctx)
          else for { a <- avalue(l)(ctx) } yield valAccess(a, l.typ)(l)(ctx)
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
