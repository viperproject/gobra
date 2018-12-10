package viper.gobra

import com.typesafe.scalalogging.StrictLogging
import org.slf4j.LoggerFactory
import viper.gobra.frontend.Config

object Gobra {
  val copyright = "(c) Copyright ETH Zurich 2012 - 2018"
}

class Gobra {}

class GobraFrontend extends StrictLogging {

  def createVerifier(config: Config) = {}
}

object GobraRunner extends GobraFrontend {
  def main(args: Array[String]): Unit = {
    createVerifier(new Config(args))
    System.out.println("Hey")
    new DummyAnalyser(null).hey()
  }
}
