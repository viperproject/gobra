package viper.gobra.util

import java.io.File


object OutputUtil {

  def postfixFile(f: File, postfix: String): File = {
    val abolutePath = f.getAbsolutePath
    val newFile = s"${abolutePath}.$postfix"
    new File(newFile)
  }
}
