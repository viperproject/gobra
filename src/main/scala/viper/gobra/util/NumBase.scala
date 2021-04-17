package viper.gobra.util

sealed abstract class NumBase(val base: Int)
object Hexadecimal extends NumBase(16)
object Decimal extends NumBase(10)
