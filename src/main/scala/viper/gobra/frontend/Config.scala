package viper.gobra.frontend

import com.typesafe.scalalogging.StrictLogging
import org.rogach.scallop.{ScallopConf, ScallopOption, singleArgConverter}

class Config(arguments: Seq[String])
    extends ScallopConf(arguments)
    with StrictLogging {

  /**
    * Prologue
    */
  /**
    * Command-line options
    */
  val inputFileName: ScallopOption[String] = opt[String](
    name = "input",
    descr = "Go program to verify is read from this file",
    required = true
  )

  val logLevel: ScallopOption[String] = opt[String](
    name = "logLevel",
    descr =
      "One of the log levels ALL, TRACE, DEBUG, INFO, WARN, ERROR, OFF (default: OFF)",
    default = Some("INFO"),
    noshort = true
  )(singleArgConverter(level => level.toUpperCase))

  /**
    * Exception handling
    */
  /**
    * Epilogue
    */
  verify()

}
