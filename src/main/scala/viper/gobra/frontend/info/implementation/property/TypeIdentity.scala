package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeIdentity extends BaseProperty { this: TypeInfoImpl =>

  lazy val identicalTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not identical to $right"
  } {
    case (Single(lst), Single(rst)) => (lst, rst) match {

      case (IntT, IntT) | (BooleanT, BooleanT) => true

      case (DeclaredT(l), DeclaredT(r)) => l == r

      case (ArrayT(ll, l), ArrayT(rl, r)) => ll == rl && identicalTypes(l, r)

      case (SliceT(l), SliceT(r)) => identicalTypes(l, r)

      case (StructT(l), StructT(r)) =>
        val (les, lfs, res, rfs) = (l.embedded, l.fields, r.embedded, r.fields)

        les.size == res.size && les.zip(res).forall {
          case (lm, rm) => identicalTypes(miscType(lm.typ), miscType(rm.typ))
        } && lfs.size == rfs.size && lfs.zip(rfs).forall {
          case (lm, rm) => lm.id.name == rm.id.name && identicalTypes(typeType(lm.typ), typeType(rm.typ))
        }

      case (l: InterfaceT, r: InterfaceT) =>
        val lm = interfaceMethodSet(l).toMap
        val rm = interfaceMethodSet(r).toMap
        lm.keySet.forall(k => rm.get(k).exists(m => identicalTypes(memberType(m), memberType(lm(k))))) &&
          rm.keySet.forall(k => lm.get(k).exists(m => identicalTypes(memberType(m), memberType(rm(k)))))

      case (PointerT(l), PointerT(r)) => identicalTypes(l, r)

      case (FunctionT(larg, lr), FunctionT(rarg, rr)) =>
        larg.size == rarg.size && larg.zip(rarg).forall {
          case (l, r) => identicalTypes(l, r)
        } && identicalTypes(lr, rr)

      case (MapT(lk, le), MapT(rk, re)) => identicalTypes(lk, rk) && identicalTypes(le, re)

      case (ChannelT(le, lm), ChannelT(re, rm)) => identicalTypes(le, re) && lm == rm

      //        case (InternalTupleT(lv), InternalTupleT(rv)) =>
      //          lv.size == rv.size && lv.zip(rv).forall {
      //            case (l, r) => identicalTypes(l, r)
      //          }

      case _ => false
    }
    case _ => false
  }
}
