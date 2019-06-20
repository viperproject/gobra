package viper.gobra.util

import org.bitbucket.inkytonik.kiama.attribution.Attribution


trait Computation[-A, +R] extends (A => R) {

def compute(n: A): R

override def apply(n: A): R = compute(n)
}

trait Memoization[-A, +R] extends Computation[A, R] { this: Attribution =>

  lazy val store: A => R = attr(super.apply)

  override def apply(v: A): R = store(v)
}

trait Safety[-A, +R] extends Computation[A, R] {
  def safe(n: A): Boolean

  def unsafe: R

  override def apply(n: A): R = if (safe(n)) super.apply(n) else unsafe
}

trait Validity[-A, R] extends Computation[A, R] {

  def invalid(ret: R): Boolean

  def valid(n: A): Boolean = !invalid(apply(n))
}

object Computation {
  def cashedComputation[A, R](f: A => R): A => R = new Attribution with Memoization[A, R] {
    override def compute(n: A): R = f(n)
  }
}

