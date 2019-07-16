package viper.gobra.translator.implementations

import viper.gobra.translator.interfaces.Collector
import viper.silver.{ast => vpr}

class CollectorImpl extends Collector {

  protected var _domains:    List[vpr.Domain]    = List.empty
  protected var _fields:     List[vpr.Field]     = List.empty
  protected var _predicates: List[vpr.Predicate] = List.empty
  protected var _functions:  List[vpr.Function]  = List.empty
  protected var _methods:    List[vpr.Method]    = List.empty


  override def addMember(m: vpr.Member): Unit = m match {
    case d: vpr.Domain    => _domains ::= d
    case f: vpr.Field     => _fields ::= f
    case p: vpr.Predicate => _predicates ::= p
    case f: vpr.Function  => _functions ::= f
    case m: vpr.Method    => _methods ::= m
  }


  override def domains: Seq[vpr.Domain] = _domains
  override def fields: Seq[vpr.Field] = _fields
  override def predicate: Seq[vpr.Predicate] = _predicates
  override def functions: Seq[vpr.Function] = _functions
  override def methods: Seq[vpr.Method] = _methods
}
