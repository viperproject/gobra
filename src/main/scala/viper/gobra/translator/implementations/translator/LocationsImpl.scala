package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import in.Types.{isClassType, isStructType, structType}
import in.Addressable.{isAddressable, isFieldRefAddressable}
import viper.gobra.reporting.Source
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{PrimitiveGenerator, Registrator, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.{withInfo => nodeWithInfo}
import viper.gobra.translator.interfaces.translator.Locations.SubValueRep
import viper.gobra.util.Violation

import scala.annotation.tailrec

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
        val f = vpr.Field(name = Names.pointerFields(t), typ = t)()
        (f, Vector(f))
      }
    )

  private def pointerField(t: in.Type)(ctx: Context): vpr.Field = _pointerField(ctx.typ.translate(t)(ctx))


  /**
    * [v]w -> v
    */
  def variable(v: in.Var)(ctx: Context): CodeWriter[vpr.LocalVar] = withDeepInfo(v){

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.Parameter(id, t)    => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Val(id, t) => unit(vpr.LocalVar(id, goT(t))())
      case in.LocalVar.Ref(id, _) => unit(vpr.LocalVar(id, vpr.Ref)())
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
      val multiVar = in.LocalVar.Val(Names.freshName, typ)(Source.Parser.Internal)

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
    * CallReceiver[r](mp) -> Aux[l]
    *   where
    *     fs, l = mp such that l does not contain fields and fs is either empty or ends with a field
    *
    *     Aux[()] -> r.fs
    *     Aux[ &] -> &r.fs
    *     Aux[ *] -> *r.fs
    *
    * translates a receiver of a call
    */
  override def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): CodeWriter[Vector[vpr.Exp]] = {
    val lastFieldIdx = path.path.lastIndexWhere(_.isInstanceOf[in.MemberPath.Next])
    val correctedLastFieldIdx = if (lastFieldIdx == -1) path.path.size - 1 else lastFieldIdx
    val (promotionPath, afterPath) = path.path.splitAt(correctedLastFieldIdx + 1)

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

    argument(newRecv)(ctx)
  }


  /**
    * [decl x: T] -> InitValue<x>; FOREACH a in Values[T]. a(x) := Default(Type(a(x)))
    */
  override def localDecl(v: in.BottomDeclaration)(ctx: Context): (Vector[vpr.Declaration], CodeWriter[vpr.Stmt]) = {
    v match {
      case v: in.Var =>
        val valueInits = variable(v)(ctx) flatMap (x => initValues(isAddressable(v), x, v.typ)(ctx))
        val (decls, valueUnit) = valueInits.cut
        val as = values(v.typ)(ctx).map(_(v))
        val valueAssigns = seqns(as map { case (r, t) =>

          for {
            ax <- r
            dflt <- ctx.loc.defaultValue(t.typ)(ctx)
          } yield valueAssign(ax, dflt)
        })
        (decls, valueUnit flatMap (_ => valueAssigns))
    }
  }


  override def initialize(v: in.TopDeclaration)(ctx: Context): CodeWriter[vpr.Stmt] = {
    v match {
      case v: in.BottomDeclaration => localDecl(v)(ctx)._2
      case v: in.Parameter => unit(vu.nop) // parameters are not initialized
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

      val multiVar = in.LocalVar.Val(Names.freshName, e.typ)(e.info)
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


//  def copyConsume[R](typ: in.Type)
//                    (base: (in.Expr => (CodeWriter[vpr.Exp], SubValueRep)) => CodeWriter[vpr.Exp])
//                    (f: (vpr.LocalVar, in.Expr => (CodeWriter[vpr.Exp], SubValueRep)) => CodeWriter[R],
//                     fin: Option[(Vector[R], vpr.LocalVar) => vpr.Exp] = None)
//                    (ctx: Context): CodeWriter[vpr.Exp] = {
//
//    val trans = values(typ)(ctx)
//
//    if (trans.size == 1) {
//      base(trans.head)
//    } else {
//      val multiVar = in.LocalVar.Val(Names.freshName, typ)(Source.Parser.Unsourced)
//
//      val results = sequence(trans map { a =>
//        val (valueWriter, rep) = a(multiVar)
//        for {
//          valueExp <- valueWriter
//          valueVar = valueExp.asInstanceOf[vpr.LocalVar]
//          _ <- global(vu.toVarDecl(valueVar))
//          res <- f(valueVar, a)
//        } yield res
//      })
//
//      if (fin.isDefined) {
//        for {
//          rs <- results
//          mv <- variable(multiVar)(ctx)
//        } yield fin.get(rs, mv)
//      } else {
//        results.flatMap(_ => variable(multiVar)(ctx))
//      }
//    }
//  }


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
      val multiVar = in.LocalVar.Val(Names.freshName, typ)(Source.Parser.Single.fromVpr(tuple))

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
  override def equal(lhs: in.Expr, rhs: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    val trans = values(lhs.typ)(ctx)
    sequence(trans map { a =>
      for {
        l <- a(lhs)._1
        r <- a(rhs)._1
      } yield vpr.EqCmp(l ,r)()
    }).map(vu.bigAnd)
  }


  /**
    * [default(T)] -> var l; FOREACH a in Values[T]. a(l) := simpleDefault(Type(a(l)))
    */
  override def defaultValue(t: in.Type)(ctx: Context): CodeWriter[vpr.Exp] = {

    def leafVal(t: in.Type): CodeWriter[vpr.Exp] = {
      t match {
        case in.BoolT => unit(vpr.TrueLit()())
        case in.IntT => unit(vpr.IntLit(0)())
        case in.PermissionT => unit(vpr.NoPerm()())
        case in.DefinedT(_, t2) => ctx.loc.defaultValue(t2)(ctx)
        case in.PointerT(_) => unit(vpr.NullLit()())
        case in.NilT => unit(vpr.NullLit()())
        case _ => Violation.violation(s"encountered unexpected inner type $t")
      }
    }

    val trans = values(t)(ctx)
    val v = in.LocalVar.Val(Names.freshName, t)(Source.Parser.Internal)

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
  override def literal(lit: in.Lit)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(lit){


    lit match {
      case in.IntLit(v) => unit(vpr.IntLit(v)())
      case in.BoolLit(b) => unit(vpr.BoolLit(b)())
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

        val z = in.LocalVar.Val(Names.freshName, typ)(lit.info)

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
  override def assignment(ass: in.SingleAss)(ctx: Context): CodeWriter[vpr.Stmt] = withDeepInfo(ass){
    val trans = values(ass.left.op.typ)(ctx)
    seqns(trans map { a =>
      for{l <- a(ass.left.op)._1; r <- a(ass.right)._1} yield valueAssign(l, r)
    })
  }


  /** left := right */
  private def valueAssign(left: vpr.Exp, right: vpr.Exp): vpr.AbstractAssign = left match {
    case l: vpr.LocalVar => vpr.LocalVarAssign(l, right)()
    case l: vpr.FieldAccess => vpr.FieldAssign(l, right)()
    case _ => Violation.violation(s"expected vpr variable or field access, but got $left")
  }


  /**
    * [v := make(lit: S] -> [decl z S; ( Foreach (f, e) in lit. inhale(acc(z.f)); z.f = e ); v := z ]
    */
  override def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt] = withDeepInfo(mk){
    val src = mk.info

    mk.typ match {
      case in.CompositeObject.Struct(slit) =>
        val interVar = mk.target
        val perField = slit.fieldZip.flatMap{ case (f, e) =>
          val fieldRef = in.FieldRef(interVar, f, in.MemberPath(Vector.empty))(src)
          val inhalePermission = in.Inhale(in.Access(in.Accessible.Field(fieldRef))(src))(src)
          val init = in.SingleAss(in.Assignee.Field(fieldRef), e)(src)
          Vector(inhalePermission, init)
        }

        seqn{
          for {
            vTarget <- variable(mk.target)(ctx)
            _ <- write(vpr.NewStmt(vTarget, Vector.empty)())
            vMake <- seqns(perField map ctx.stmt.translateF(ctx))
          } yield vMake
        }
    }
  }


  /**
    * [acc(*e )] -> PointerAcc[*e]
    * [acc(e.f)] -> ProjAcc[e](path(e.f);f) && PointerAcc[e.f]
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
    * ProjAcc[!(e.f): not S] -> acc(RProj[r](path(e.f)).f)
    * ProjAcc[  e.f    ]     -> Acc<R[e.f]>
    *
    * Acc<t.f> -> acc(t.f)
    * Acc<x  > -> true
    *
    *
    * translates a a field access keeping the last address
    */
  override def access(acc: in.Access)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(acc){

    val perm = vpr.FullPerm()()

    def pointerAcc(recv: in.Location): CodeWriter[vpr.Exp] = {
      if (isAddressable(recv) && !isStructType(recv.typ)) {
        for {
          r <- avalue(recv)(ctx)
        } yield vpr.FieldAccessPredicate(valAccess(r, recv.typ)(ctx), perm)()
      } else unit(vpr.TrueLit()())
    }

    def projAcc(fieldRef: in.FieldRef): CodeWriter[vpr.Exp] = {
      if (isAddressable(fieldRef) && !isStructType(fieldRef.typ)) {
        val pathFields = in.MemberPath.cut(fieldRef.path)._1
        for {
          r <- rproj(fieldRef.recv, pathFields)(ctx)
          facc = fieldAccess(r, fieldRef.field, mightContainAddressable = true, complete = true)(ctx)
        } yield vpr.FieldAccessPredicate(facc, perm)()
      } else {
        for {
          r <- rvalue(fieldRef)(ctx)
          facc = r match { // Acc<R[e.f]>
            case access: vpr.FieldAccess => vpr.FieldAccessPredicate(access, perm)()
            case _: vpr.LocalVar => vpr.TrueLit()()
            case _ => Violation.violation(s"expected field access or variable, but got $r")
          }
        } yield facc
      }
    }

    acc.e match {
      case in.Accessible.Pointer(der) => pointerAcc(der)

      case in.Accessible.Field(fa) =>
        for {
          path <- projAcc(fa)
          pointer <- pointerAcc(fa)
        } yield vpr.And(path, pointer)()

      case in.Accessible.Predicate(op) => predicateAccess(op)(ctx)
    }
  }


  /**
    * [acc(  p(as)] -> p(Argument[as])
    * [acc(e.p(as)] -> p(Argument[e], Argument[as])
    */
  override def predicateAccess(acc: in.PredicateAccess)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate] = withDeepInfo(acc){
    val perm = vpr.FullPerm()()

    acc match {
      case in.FPredicateAccess(pred, args) =>
        for {
          vArgss <- sequence(args map (argument(_)(ctx)))
          pacc = vpr.PredicateAccess(vArgss.flatten, pred.name)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        } yield vpr.PredicateAccessPredicate(pacc, perm)()

      case in.MPredicateAccess(recv, pred, args, path) =>
        for {
          vRecvs <- ctx.loc.callReceiver(recv, path)(ctx)
          vArgss <- sequence(args map (argument(_)(ctx)))
          pacc = vpr.PredicateAccess(vRecvs ++ vArgss.flatten, pred.uniqueName)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        } yield vpr.PredicateAccessPredicate(pacc, perm)()

      case in.MemoryPredicateAccess(_) =>
        Violation.violation("memory predicate accesses are not supported")
    }
  }


  /**
    * Values[S] -> { \r. RProj[r]fs | fs in ValuePaths[S] }
    * Values[T] -> { \r. R[r] }
    */
  override def values(t: in.Type)(ctx: Context): Vector[in.Expr => (CodeWriter[vpr.Exp], SubValueRep)] = {
    t match {
      case u if isStructType(u) =>
        valuePaths(u).map(fs => (r: in.Expr) => (rproj(r, fs)(ctx), SubValueRep(fs.last.typ)))
      case _ =>
        Vector(r => (rvalue(r)(ctx), SubValueRep(r.typ)))
    }
  }

  /**
    * InitValue<?t: S> -> { InitValue<ext(?,S,f) t#f: Q> | (f: Q) in S }
    * InitValue<!t: S> -> { Init<t> } + { InitValue<ext(!,S,f) t.f: Q> | (f: Q) in S }
    * InitValue<?t: T> -> { Init<t> }
    * InitValue<!t: T> -> { Init<t>, Init<t.val> }
    */
  private def initValues(isBaseAddr: Boolean, t: vpr.Exp, typ: in.Type)(ctx: Context): CodeWriter[Vector[vpr.LocalVarDecl]] = {

    structType(typ) match {
      case None if !isBaseAddr => init(t)

      case None if isBaseAddr  =>
        val fieldAcc = valAccess(t, typ)(ctx)
        for { a <- init(t); b <- init(fieldAcc) } yield a ++ b

      case Some(st) if !isBaseAddr =>
        sequence(st.fields.map{ f =>
          val isNextAddr = isFieldRefAddressable(isBaseAddr, st, f)
          initValues(isNextAddr, fieldExtension(t,f)(ctx), f.typ)(ctx)
        }).map(_.flatten)

      case Some(st) if isBaseAddr =>
        val fieldInits = sequence(st.fields.map { f =>
          val isNextAddr = isFieldRefAddressable(isBaseAddr, st, f)
          val complete = !(!isNextAddr && isStructType(f.typ))
          val mightContainAddressable = isNextAddr

          initValues(isNextAddr, fieldAccess(t, f, mightContainAddressable, complete)(ctx), f.typ)(ctx)
        }).map(_.flatten)

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
        write(vpr.Inhale(vpr.FieldAccessPredicate(f, vpr.FullPerm()())())()).map(_ => Vector.empty)
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
    * A[!r.!f] -> RProj[r]path(r.f).f  where the path is addressable
    *
    * translates a location for a receiver position, except keeps addresses if possible
    */
  def avalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(l){
    require(isAddressable(l))

    l match {
      case v: in.Var => variable(v)(ctx)

      case in.Deref(exp, _) => rvalue(exp)(ctx)

      case in.FieldRef(recv, f, path) =>
        val fields = path.path.collect{ case in.MemberPath.Next(ef) => ef }
        for {
          rcv <- rproj(recv, fields)(ctx)
        } yield fieldAccess(rcv, f, mightContainAddressable = true, complete = true)(ctx)

      case _ => Violation.violation(s"encountered unexpected addressable location $l")
    }
  }


  /**
    * R[!r: S] -> A[r]
    * R[!r: T] -> A[r].val
    * R[?x]  -> x
    * R[&!r] -> assert A[r] != null; A[r]
    * R[e.f] -> RProj[e](path(e.f), f)
    *
    * translates a location for a receiver position
    */
  def rvalue(l: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(l){
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
              _ <- wellDef(vpr.NeCmp(address, vpr.NullLit()())())
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
    * RProj[?r]fs -> RProjPath<?;R[r]>[fs]  // only do inlining if 100% sure that 
    *   where
    *     RPath<m;t: T>[      ()] -> t
    *     RPath<?;t: S>[f: T, fs] -> RPath<?;t#f    >[fs]  with !ext(?, S, f)
    *     RPath<m;t: C>[f: S, fs] -> RPath<?;t.f    >[fs]  with !ext(m, C, f)
    *     RPath<m;t: C>[f: S, fs] -> RPath<!;t.f    >[fs]  with  ext(m, C, f)
    *     RPath<m;t: C>[f: T, fs] -> RPath<?;t.f    >[fs]  with !ext(m, C, f)
    *     RPath<m;t: C>[f: T, fs] -> RPath<!;t.f.val>[fs]  with  ext(m, C, f)
    *
    * translates a field access
    */
  def rproj(recv: in.Expr, fields: Vector[in.Field])(ctx: Context): CodeWriter[vpr.Exp] = {

    @tailrec
    def rpath(isBaseAddr: Boolean, t: vpr.Exp, baseType: in.Type, fields: Vector[in.Field]): CodeWriter[vpr.Exp] = {
      if (fields.isEmpty) unit(t)
      else {
        Violation.violation(isClassType(baseType), s"expected class type, but got $baseType")
        val (f, fs) = (fields.head, fields.tail)
        val isFieldExprAddr = isFieldRefAddressable(isBaseAddr, baseType, f)
        val isStructF = isStructType(f.typ)
        // field access will get extended when the next mode is ? and f is a struct
        val complete = !(!isFieldExprAddr && isStructF)
        val mightContainAddressable = isFieldExprAddr

        if (!isBaseAddr && isStructType(baseType)) {
          rpath(isFieldExprAddr, fieldExtension(t,f)(ctx), f.typ, fs)
        } else if (mightContainAddressable && !isStructF) {
          rpath(isFieldExprAddr, valAccess(fieldAccess(t, f, mightContainAddressable, complete)(ctx), f.typ)(ctx), f.typ, fs)
        } else {
          rpath(isFieldExprAddr, fieldAccess(t, f, mightContainAddressable, complete)(ctx), f.typ, fs)
        }
      }
    }

    for {
      r <- rvalue(recv)(ctx)
      res <- rpath(isAddressable(recv), r, recv.typ, fields)
    } yield res
  }


  /** [f] -> f */
  def field(mightContainAddressable: Boolean, f: in.Field)(ctx: Context): vpr.Field = {
    f match {
      case _: in.Field.Ref if mightContainAddressable => nodeWithInfo(vpr.Field(Names.addressableField(f.name), vpr.Ref))(f)
      case _ => nodeWithInfo(vpr.Field(Names.nonAddressableField(f.name), ctx.typ.translate(f.typ)(ctx)))(f)
    }
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
  private def fieldAccess(x: vpr.Exp, f: in.Field, mightContainAddressable: Boolean, complete: Boolean)(ctx: Context): vpr.FieldAccess = {
    val res = vpr.FieldAccess(x, field(mightContainAddressable, f)(ctx))()
    if (complete) { regField(res) }
    res
  }


  /** e.val */
  private def valAccess(x: vpr.Exp, t: in.Type)(ctx: Context): vpr.FieldAccess =
    regField(vpr.FieldAccess(x, pointerField(t)(ctx))())


  // Utils

  private def createMultiVar: vpr.LocalVar =
    vpr.LocalVar(Names.freshName, vpr.Int)()

  private def inverseVar(x: vpr.LocalVar, typ: in.Type)(info: Source.Parser.Info): in.LocalVar.Val =
    in.LocalVar.Val(x.name, typ)(info)
}
