package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.frontend.info.base.Type._
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

sealed trait TypeSet

/**
  * The type set of a type parameter constraint is the set of all types that satisfy this constraint
  * In this implementation, type sets that are unbounded are represented by the [[UnboundedTypeSet]]
  * Bounded type sets are represented by [[BoundedTypeSet]] and explicitly list the set of types they contains
  *
  * Additionally, an unbounded type set needs to keep track whether all types in the type set are comparable
  * The comparability of a bounded type set can just be calculated with the set of types it contains
  */
object TypeSet {
  case class UnboundedTypeSet(isComparable: Boolean = false) extends TypeSet
  case class BoundedTypeSet(ts: Set[Type]) extends TypeSet

  /**
    * Constructs the type set from a type parameter constraint
    */
  def from(constraint: PInterfaceType, ctx: TypeInfoImpl): TypeSet = typeSetFromInterfaceType(constraint, ctx)

  private def typeSetFromInterfaceType(inter: PInterfaceType, ctx: TypeInfoImpl): TypeSet = inter match {
    case PInterfaceType(embedded, _, _) => if (embedded.isEmpty) UnboundedTypeSet() else intersect(embedded.map(el => typeSetFromElement(el, ctx)))
  }

  private def typeSetFromElement(element: PTypeElement, ctx: TypeInfoImpl): TypeSet =
    if (element.terms.isEmpty) UnboundedTypeSet() else union(element.terms.map(term => typeSetFromTerm(term, ctx)))

  private def typeSetFromTerm(term: PTypeTerm, ctx: TypeInfoImpl): TypeSet = term match {
    case PNamedOperand(PIdnUse("comparable")) => UnboundedTypeSet(isComparable = true)
    case pType: PType => ctx.underlyingType(ctx.symbType(pType)) match {
      case InterfaceT(i, _) => typeSetFromInterfaceType(i, ctx)
      case t => BoundedTypeSet(Set(t))
    }
  }

  def empty(): TypeSet = {
    BoundedTypeSet(Set.empty[Type])
  }

  def union(tss: Vector[TypeSet]): TypeSet = tss.size match {
    case 0 => empty()
    case 1 => tss.head
    case _ => tss.fold(empty()) {
      case (tl: UnboundedTypeSet, tr: UnboundedTypeSet) => UnboundedTypeSet(tl.isComparable || tr.isComparable)
      case (t: UnboundedTypeSet, _) => UnboundedTypeSet(t.isComparable)
      case (_, t: UnboundedTypeSet) => UnboundedTypeSet(t.isComparable)
      case (BoundedTypeSet(x), BoundedTypeSet(y)) => BoundedTypeSet(x union y)
    }
  }

  def intersect(tss: Vector[TypeSet]): TypeSet = tss.size match {
    case 0 => empty()
    case 1 => tss.head
    case _ => tss.fold(UnboundedTypeSet(isComparable = true)) {
      case (UnboundedTypeSet(l), UnboundedTypeSet(r)) => UnboundedTypeSet(l && r)
      case (_: UnboundedTypeSet, ts) => ts
      case (ts, _: UnboundedTypeSet) => ts
      case (BoundedTypeSet(x), BoundedTypeSet(y)) => BoundedTypeSet(x intersect y)
    }
  }

  def contains(ts: TypeSet, t: Type): Boolean = ts match {
    case _: UnboundedTypeSet => true
    case BoundedTypeSet(s) => s.contains(t)
  }

  def isSubset(sub: TypeSet, of: TypeSet): Boolean = (sub, of) match {
    case (_, _: UnboundedTypeSet) => true
    case (_: UnboundedTypeSet, _) => false
    case (BoundedTypeSet(tsSub), BoundedTypeSet(tsOf)) => tsSub.subsetOf(tsOf)
  }

  def allMatch(all: TypeSet, matcher: PartialFunction[Type, Boolean]): Boolean = all match {
    case _: UnboundedTypeSet => false
    case BoundedTypeSet(ts) => ts.forall(t => matcher.isDefinedAt(t) && matcher(t))
  }
}
