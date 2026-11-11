// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.util

import java.nio.charset.StandardCharsets
import scala.collection.mutable

/** The value of a Go string, which is an immutable sequence of bytes. */
final class GoString private (val bytes: Vector[Byte]) {
  def length: BigInt = BigInt(bytes.length)

  def ++(other: GoString): GoString = GoString(bytes ++ other.bytes)

  def utf8: String = new String(bytes.toArray, StandardCharsets.UTF_8)

  /** Returns a canonical, lossless, interpreted Go string literal. */
  def quoted: String = {
    val body = bytes.iterator.map { byte =>
      (byte & 0xff) match {
        case 0x07 => "\\a"
        case 0x08 => "\\b"
        case 0x09 => "\\t"
        case 0x0a => "\\n"
        case 0x0b => "\\v"
        case 0x0c => "\\f"
        case 0x0d => "\\r"
        case 0x22 => "\\\""
        // we don't have to escape single quotes (0x27)
        case 0x5c => "\\\\"
        case value if 0x20 <= value && value <= 0x7e => value.toChar.toString
        case value => f"\\x$value%02x"
      }
    }.mkString
    s"\"$body\""
  }

  override def equals(other: Any): Boolean = other match {
    case that: GoString => bytes == that.bytes
    case _ => false
  }

  override def hashCode(): Int = bytes.hashCode()
}

object GoString {
  def apply(bytes: Vector[Byte]): GoString = new GoString(bytes)

  def unapply(string: GoString): Option[Vector[Byte]] = Some(string.bytes)

  val empty: GoString = GoString(Vector.empty)

  def fromRawLiteral(content: String): GoString =
    // remove carriage return characters within a raw string literal as
    // mandated by the Go spec:
    GoString(content.filterNot(_ == '\r').getBytes(StandardCharsets.UTF_8).toVector)

  def fromInterpretedLiteral(content: String): Either[String, GoString] = {
    val bytes = mutable.ArrayBuilder.make[Byte]

    def appendCodePoint(codePoint: Int): Unit =
      bytes ++= new String(Character.toChars(codePoint)).getBytes(StandardCharsets.UTF_8)

    def parseDigits(start: Int, count: Int, radix: Int): Either[String, Int] = {
      val end = start + count
      if (end > content.length) Left("incomplete string escape")
      else {
        val digits = content.substring(start, end)
        try Right(Integer.parseInt(digits, radix))
        catch { case _: NumberFormatException => Left(s"invalid string escape: $digits") }
      }
    }

    var index = 0
    while (index < content.length) {
      val current = content.charAt(index)
      if (current != '\\') {
        val codePoint = content.codePointAt(index)
        appendCodePoint(codePoint)
        index += Character.charCount(codePoint)
      } else if (index + 1 >= content.length) {
        return Left("incomplete string escape")
      } else {
        content.charAt(index + 1) match {
          case 'a' => bytes += 0x07.toByte; index += 2
          case 'b' => bytes += 0x08.toByte; index += 2
          case 't' => bytes += 0x09.toByte; index += 2
          case 'n' => bytes += 0x0a.toByte; index += 2
          case 'v' => bytes += 0x0b.toByte; index += 2
          case 'f' => bytes += 0x0c.toByte; index += 2
          case 'r' => bytes += 0x0d.toByte; index += 2
          case '"' => bytes += 0x22.toByte; index += 2
          case '\'' => bytes += 0x27.toByte; index += 2
          case '\\' => bytes += 0x5c.toByte; index += 2
          case 'x' =>
            parseDigits(index + 2, 2, 16) match {
              case Right(value) => bytes += value.toByte; index += 4
              case Left(error) => return Left(error)
            }
          case 'u' | 'U' =>
            val count = if (content.charAt(index + 1) == 'u') 4 else 8
            parseDigits(index + 2, count, 16) match {
              case Right(value) if Character.isValidCodePoint(value) &&
                !(Character.MIN_SURROGATE <= value && value <= Character.MAX_SURROGATE) =>
                appendCodePoint(value)
                index += count + 2
              case Right(value) => return Left(f"invalid Unicode code point: $value%x")
              case Left(error) => return Left(error)
            }
          case digit if '0' <= digit && digit <= '7' =>
            parseDigits(index + 1, 3, 8) match {
              case Right(value) if value <= 0xff => bytes += value.toByte; index += 4
              case Right(value) => return Left(f"octal escape is larger than one byte: $value%o")
              case Left(error) => return Left(error)
            }
          case escape => return Left(s"unknown string escape: \\$escape")
        }
      }
    }

    Right(GoString(bytes.result().toVector))
  }
}
