package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import in.Types.{isStructType, structType}
import in.Addressable.isAddressable
import viper.gobra.reporting.Source
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{PrimitiveGenerator, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.{withInfo => nodeWithInfo}
import viper.gobra.util.Violation

import scala.annotation.tailrec

class LocationsImpl extends Locations {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {
    _pointerField.finalize(col)
  }

  private lazy val _pointerField: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        val f = vpr.Field(name = Names.pointerFields(t), typ = t)()
        (f, Vector(f))
      }
    )

  private def pointerField(t: in.Type)(ctx: Context): vpr.Field = _pointerField(ctx.typ.translate(t)(ctx))

  /**
    * [v]w -> v
    */
  override def variable(v: in.Var)(ctx: Context): CodeWriter[vpr.LocalVar] = withDeepInfo(v){

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.Parameter(id, t)    => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Val(id, t) => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Ref(id, t) => unit(vpr.LocalVar(id, vpr.Ref)())
    }
  }

  override def fieldDecl(f: in.Field)(ctx: Context): Vector[vpr.Field] = {
    Vector(field(f)(ctx))
  }



  /**
    * [decl x: T] -> InitValue<x>; FOREACH a in Values[T]. a(x) := Default(Type(a(x)))
    */
  override def topDecl(v: in.TopDeclaration)(ctx: Context): ((Vector[vpr.LocalVarDecl], CodeWriter[vpr.Stmt]), Context) = {
    v match {
      case v: in.Var =>
        val valueInits = variable(v)(ctx) flatMap (x => initValues(x, isAddressable(v), v.typ)(ctx))
        val (valueUnit, decls) = valueInits.cut
        val as = values(v.typ)(ctx).map(_(v))
        val valueAssigns = seqn(as map { case (r, t) =>
            for {
              ax <- r
              dflt <- ctx.expr.defaultValue(t.typ)
            } yield valueAssign(ax, dflt)
        })
        ((decls, valueUnit flatMap (_ => valueAssigns)), ctx)
    }
  }

  /**
    * [decl x: T] -> InitValue<x>; FOREACH a in Values[T]. a(x) := Default(Type(a(x)))
    */
  override def bottomDecl(v: in.BottomDeclaration)(ctx: Context): ((Vector[vpr.Declaration], CodeWriter[vpr.Stmt]), Context) = {
    v match {
      case v: in.TopDeclaration => topDecl(v)(ctx)
    }
  }


  /**
    * [!r: T = e] -> FOREACH a in Values[T]. a(r) := a(e)
    */
  override def assignment(ass: in.SingleAss)(ctx: Context): CodeWriter[vpr.Stmt] = withDeepInfo(ass){
    val trans = values(ass.left.op.typ)(ctx)
    seqn(trans map { a =>
      for{l <- a(ass.left.op)._1; r <- a(ass.right)._1} yield valueAssign(l, r)
    })
  }

  /**
    * Copy[t: T]  -> var z; FOREACH a in Values[T]. Init<a(z)>; a(z) := a(t) ~ z
    */
  def copy(e: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    val trans = values(e.typ)(ctx)

    if(trans.size == 1) {
      trans.head(e)._1
    } else {
      val z = createMultiVar
      val iz = inverseVar(z, e.typ)(e.info)
      val assigns = seqn(trans map { a =>
        for {
          right <- a(e)._1
          left <- a(iz)._1
          ini <- init(left)
          _ <- global(ini: _*)
        } yield valueAssign(left, right)
      })

      assigns.flatMap(write(_)).map(_ => z)
    }
  }

  /** left := right */
  private def valueAssign(left: vpr.Exp, right: vpr.Exp): vpr.AbstractAssign = left match {
    case l: vpr.LocalVar => vpr.LocalVarAssign(l, right)()
    case l: vpr.FieldAccess => vpr.FieldAssign(l, right)()
    case _ => Violation.violation(s"expected vpr variable or field access, but got $left")
  }

  /**
    * [v := make(S{fs: es})] -> [decl z; ( Foreach (f, e) in S{fs: es}. inhale(acc(z.f)); z.f = e ); v := z ]
    */
  override def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val src = mk.info

    mk.typ match {
      case in.CompositeObject.Struct(slit) =>
        val interVar = in.LocalVar.Val(Names.freshName, mk.target.typ)(src)
        val perField = slit.fieldZip.flatMap{ case (f, e) =>
          val fieldRef = in.FieldRef(interVar, f, in.MemberPath(Vector.empty))(src)
          val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
          val init = in.SingleAss(in.Assignee.Field(fieldRef), e)(src)
          Vector(inhalePermission, init)
        }

        seqn{
          for {
            vTarget <- variable(mk.target)(ctx)

            v <- variable(interVar)(ctx)
            _ <- local(vu.toVarDecl(v))

            vMake <- seqn(perField map ctx.stmt.translateF(ctx))
            _ <- write(vMake)

          } yield vpr.LocalVarAssign(vTarget, v)()
        }
    }
  }

  case class SubValueRep(typ: in.Type)

  /**
    * Values[S] -> { \r. RProj[r]fs | fs in ValuePaths[S] }
    * Values[T] -> { \r. R[r] }
    */
  private def values(t: in.Type)(ctx: Context): Vector[in.Expr => (CodeWriter[vpr.Exp], SubValueRep)] = {
    t match {
      case u if isStructType(u) =>
        valuePaths(u).map(fs => (r: in.Expr) => (rproj(r, fs)(ctx), SubValueRep(fs.last.typ)))
      case u =>
        Vector(r => (rvalue(r)(ctx), SubValueRep(r.typ)))
    }
  }

  /**
    * InitValue<?t: S> -> { Init<t#fs> | fs in ValuePaths[S] }
    * InitValue<!t: S> -> { Init<t> } + { InitValue<m t.f: Q> | (m f: Q) in S }
    * InitValue<?t: T> -> { Init<t> }
    * InitValue<!t: T> -> { Init<t>, Init<t.val> }
    */
  private def initValues(t: vpr.Exp, addressable: Boolean, typ: in.Type)(ctx: Context): CodeWriter[Vector[vpr.LocalVarDecl]] = {

    structType(typ) match {
      case None if !addressable => init(t)

      case None if addressable  =>
        val fieldAcc = valAccess(t, typ)(ctx)
        for { a <- init(t); b <- init(fieldAcc) } yield a ++ b

      case Some(st) if !addressable =>
        sequence(valuePaths(st).map{ fs =>
          init(fs.foldLeft(t){ case (r,f) => fieldExtension(r, f)(ctx) })
        }).map(_.flatten)

      case Some(st) if addressable =>
        val fieldInits = sequence(st.fields.map(f =>
          initValues(fieldAccess(t, f)(ctx), addressable = isAddressable(f), f.typ)(ctx)
        )).map(_.flatten)

        for { a <- init(t); b <- fieldInits} yield a ++ b
    }
  }

  /**
    * Init<x>   -> var x
    * Init<e.f> -> inhale acc(e.f)
    */
  def init(e: vpr.Exp): CodeWriter[Vector[vpr.LocalVarDecl]] = {
    e match {
      case v: vpr.LocalVar => unit(Vector(vu.toVarDecl(v)))
      case f: vpr.FieldAccess =>
        write(vpr.Inhale(vpr.FieldAccessPredicate(f, vpr.FullPerm()())()())()).map(_ => Vector.empty)
      case _ => Violation.violation(s"expected variable of field access, but got $e")
    }
  }

  /**
    * ValuePaths[S] -> { (f) | (f: not S') in S } + { f,g | (f: S') in S and g in ValuePaths[S'] }
    */
  private def valuePaths(t: in.Type): Vector[Vector[in.Field]] = {
    require(isStructType(t))
    val st = structType(t).get

    st.fields.flatMap{
      case f if isStructType(f.typ) =>
        valuePaths(f.typ).map(fs => f +: fs)
      case f => Vector(Vector(f))
    }
  }


  /**
    * A[!x] -> x
    * A[*e] -> R[e]
    * A[!r.!f] -> RProj[r]path(r.f).f
    *
    * translates a location for a receiver position, except keeps addresses if possible
    */
  def avalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(l){
    require(isAddressable(l))

    l match {
      case v: in.Var => variable(v)(ctx)

      case in.Deref(exp, typ) =>
        for {
          rcv <- ctx.expr.translate(exp)(ctx)
        } yield valAccess(rcv, typ)(ctx)

      case in.FieldRef(recv, f, path) =>
        val fields = path.path.collect{ case in.MemberPath.Next(ef) => ef }
        for {
          rcv <- rproj(recv, fields)(ctx)
        } yield fieldAccess(rcv, f)(ctx)

      case _ => Violation.violation(s"encountered unexpected addressable location $l")
    }
  }


  /**
    * R[!r: not S] -> A[r].val
    * R[!r:     S] -> A[r]
    * R[?x]  -> x
    * R[&!r] -> assert A[r] != null; A[r]
    * R[e.f] -> RProj[e](path(e.f), f)
    *
    * translates a location for a receiver position
    */
  override def rvalue(l: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(l){
    l match {
      case l: in.Location =>
        (isAddressable(l), l) match {
          case (true, _) if isStructType(l.typ) => avalue(l)(ctx)
          case (true, _) => avalue(l)(ctx) map (r => vpr.FieldAccess(r, pointerField(l.typ)(ctx))())

          case (false, x: in.Var) => variable(x)(ctx)
          case (false, ref: in.Ref) =>
            for {
              address <- avalue(ref.ref.op)(ctx)
              // assert address != null
              _ <- write(vpr.Assert(vpr.NeCmp(address, vpr.NullLit()())())())
            } yield address

          case (false, ref: in.FieldRef) =>
            val fields = ref.path.path.collect{ case in.MemberPath.Next(ef) => ef }
            rproj(ref.recv, fields :+ ref.field)(ctx)
        }

      case _ => ctx.expr.translate(l)(ctx)
    }
  }

  /**
    * RProj[!r]fs -> RProjPath<!;R[r]>[fs]
    * RProj[?r]fs -> RProjPath<?;R[r]>[fs]
    *   where
    *     RPath<m;t>[       ()] -> t
    *     RPath<!;t>[@f: S, fs] -> RPath<@;t.f    >[fs]
    *     RPath<!;t>[?f: T, fs] -> RPath<?;t.f    >[fs]
    *     RPath<!;t>[!f: T, fs] -> RPath<!;t.f.val>[fs]
    *     RPath<?;t>[ f: S, fs] -> RPath<?;t#f    >[fs]
    *     RPath<?;t>[ f: T, fs] -> RPath<?;t.f    >[fs]
    *
    * translates a field access
    */
  def rproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): CodeWriter[vpr.Exp] = {

    @tailrec
    def rpath(m: Boolean, t: vpr.Exp, fields: Vector[in.Field]): CodeWriter[vpr.Exp] = {
      if (fields.isEmpty) unit(t)
      else {
        val (f, fs) = (fields.head, fields.tail)
        (m, isAddressable(f), isStructType(f.typ)) match {
          case ( true,     q,  true) => rpath(q, fieldAccess(t,f)(ctx), fs)
          case ( true, false, false) => rpath(m = false, fieldAccess(t,f)(ctx), fs)
          case ( true,  true, false) => rpath(m = true, valAccess(fieldAccess(t,f)(ctx), f.typ)(ctx), fs)
          case (false,     _,  true) => rpath(m = false, fieldExtension(t,f)(ctx), fs)
          case (false,     _, false) => rpath(m = false, fieldAccess(t,f)(ctx), fs)
        }
      }
    }

    for {
      r <- rvalue(recv)(ctx)
      res <- rpath(isAddressable(recv), r, fields)
    } yield res
  }

  /**
    * [r] -> Copy<R[r]>
    *
    * translates a location for an expression position
    */
  override def evalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp] = copy(l)(ctx)
//
//  /**
//    * PureE[!r: S] -> content(R[r])
//    * PureE[r]     -> R[r]
//    *
//    * translates a location for an expression position
//    */
//  def pureEvalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){
//    val rval = rvalue(l)(ctx)
//
//    if (isAddressable(l) && isStructType(l.typ)) {
//      rval flatMap (r => content(r, l.typ)(ctx))
//    } else {
//      rval
//    }
//  }


  /**
    * [acc(*e )] -> PointerAcc[*e]
    * [acc(e.f)] -> ProjAcc[e](path(e.f);f) && PointerAcc[e.f]
    *
    * // an idea:
    * [acc(&!e.!f)] -> ProjAcc[e](path(e.f);f)
    * [acc(&!x)]    -> true
    *
    * PointerAcc[!r: not S] -> acc(R[r].val)
    * PointerAcc[!r: S]     -> true
    * PointerAcc[?r]        -> true
    *
    * ProjAcc[!r](!fs;g       ) -> acc(RProj[r]fs.g)
    * ProjAcc[ r]( fs;g: not S) -> acc(RProj[r](fs,g))
    * ProjAcc[ r]( fs;g       ) -> true
    *
    * translates a a field access keeping the last address
    */
  override def access(acc: in.Access)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(acc){

    val perm = vpr.FullPerm()()

    def pointerAcc(recv: in.Location): CodeWriter[vpr.Exp] = {
      if (isAddressable(recv) && !isStructType(recv.typ)) {
        for {
          r <- rvalue(recv)(ctx)
        } yield vpr.FieldAccessPredicate(valAccess(r, recv.typ)(ctx), perm)()
      } else unit(vpr.TrueLit()())
    }

    def projAcc(recv: in.Expr, pathFields: Vector[in.Field], f: in.Field): CodeWriter[vpr.Exp] = {
      (isAddressable(recv), pathFields.forall(isAddressable), isStructType(f.typ)) match {
        case (true, true, _) =>
          for {
            r <- rproj(recv, pathFields)(ctx)
            facc = fieldAccess(r, f)(ctx)
          } yield vpr.FieldAccessPredicate(facc, perm)()

        case (_, _, false) =>
          for {
            r <- rproj(recv, pathFields :+ f)(ctx)
            facc = r.asInstanceOf[vpr.FieldAccess]
          } yield vpr.FieldAccessPredicate(facc, perm)()

        case (_, _,  true) => unit(vpr.TrueLit()())
      }
    }

    acc.e match {
      case in.Accessible.Pointer(der) => pointerAcc(der)

      case in.Accessible.Field(fa) =>
        val pathFields = fa.path.path.collect{ case in.MemberPath.Next(f) => f }
        for {
          path <- projAcc(fa.recv, pathFields, fa.field)
          pointer <- pointerAcc(fa)
        } yield vpr.And(path, pointer)()
    }
  }


  /**
    * CallReceiver[r](mp) -> Aux[l]
    *   where
    *     fs, l = mp such that l does not contain fields and fs is either empty or ends with a field
    *
    *     Aux[()] -> r.fs
    *     Aux[ &] -> &r.fs
    *     Aux[ *] -> +r.fs
    *
    * translates a receiver of a call
    */
  override def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): CodeWriter[vpr.Exp] = {
    val lastFieldIdx = path.path.lastIndexWhere(_.isInstanceOf[in.MemberPath.Next])
    val (promotionPath, afterPath) = path.path.splitAt(lastFieldIdx + 1)

    val newBase = if (promotionPath.nonEmpty) {
      val newF = promotionPath.head.asInstanceOf[in.MemberPath.Next].e
      val newPath = in.MemberPath(promotionPath.tail)
      in.FieldRef(recv, newF, newPath)(recv.info)
    } else recv

    val newRecv = afterPath match {
      case Vector() => newBase
      case Vector(in.MemberPath.Deref) => in.Deref(newBase)(recv.info)
      case Vector(in.MemberPath.Ref) => in.Ref(newBase)(recv.info)
      case _ => Violation.violation("Found ill formed resolution path")
    }

    ctx.expr.translate(newRecv)(ctx)
  }



  def field(f: in.Field)(ctx: Context): vpr.Field = f match {
    case _: in.Field.Val => nodeWithInfo(vpr.Field(f.name, ctx.typ.translate(f.typ)(ctx)))(f)
    case _: in.Field.Ref => nodeWithInfo(vpr.Field(f.name, vpr.Ref))(f)
  }

  /** e # f */
  private def fieldExtension(x: vpr.Exp, f: in.Field)(ctx: Context): vpr.Lhs = x match {
    case z: vpr.LocalVar => z.copy(name = Names.fieldExtension(z.name, f.name))(z.pos, z.info, z.errT)

    case z: vpr.FieldAccess =>
      val f = z.field
      z.copy(
        field = f.copy(name = Names.fieldExtension(f.name, f.name))(f.pos, f.info, f.errT)
      )(z.pos, z.info, z.errT)

    case _ => Violation.violation(s"expected vpr variable or field access, but got $x")
  }

  /** e.f */
  private def fieldAccess(x: vpr.Exp, f: in.Field)(ctx: Context): vpr.FieldAccess =
    vpr.FieldAccess(x, field(f)(ctx))()

  /** e.val */
  private def valAccess(x: vpr.Exp, t: in.Type)(ctx: Context): vpr.FieldAccess =
    vpr.FieldAccess(x, pointerField(t)(ctx))()


  // Utils

  private def createMultiVar: vpr.LocalVar =
    vpr.LocalVar(Names.freshName, vpr.Int)()

  private def inverseVar(x: vpr.LocalVar, typ: in.Type)(info: Source.Parser.Info): in.LocalVar.Val =
    in.LocalVar.Val(x.name, typ)(info)

  private def copyResult(r: vpr.Exp): CodeWriter[vpr.LocalVar] = {
    val z = vpr.LocalVar(Names.freshName, r.typ)(r.pos, r.info, r.errT)
    for {
      _ <- local(vu.toVarDecl(z))
      _ <- write(vpr.LocalVarAssign(z, r)(r.pos, r.info, r.errT))
    } yield z
  }
}
