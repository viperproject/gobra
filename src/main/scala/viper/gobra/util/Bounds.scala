package viper.gobra.util

case class Bounds(lower: BigInt, upper: BigInt)

case object Bounds {
  val Int: Bounds = Bounds(-2147483648, 2147483647) // TODO: allow for changes according to the int size of the machine arch (either 32 or 64 bit)
  val Int8: Bounds = Bounds(-128, 127)
  val Int16: Bounds = Bounds(-32768, 32767)
  val Int32: Bounds = Bounds(-2147483648, 2147483647)
  val Int64: Bounds = Bounds(-9223372036854775808L, 9223372036854775807L)

  val UInt: Bounds = Bounds(0, 4294967295L) // TODO: allow for changes according to the int size of the machine arch (either 32 or 64 bit)
  val UInt8: Bounds = Bounds(0, 255)
  val UInt16: Bounds = Bounds(0, 65535)
  val UInt32: Bounds = Bounds(0, 4294967295L)
  val UInt64: Bounds = Bounds(0, 4294967295L * 4294967295L) // Cannot represent the upper bound as a literal
  val UIntPtr: Bounds = Bounds(0, 4294967295L + 4294967295L) //TODO: change according to spec
  // val UInt64: Bounds = Bounds(0, 18446744073709551615L)
  // val UIntPtr: Bounds = Bounds(0, 18446744073709551615L)
}