package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.silicon.interfaces.SiliconCounterexample
import viper.silver

trait CounterexampleBackTranslator{
	def getString(error:silver.verifier.VerificationError): String
}


object CounterexampleBackTranslators{
/* 	for future reference: val msg =errors.apply(0).counterexample match {
                          case None => "no counterexamples"
                          case Some(value) => value.toString()
                        }
	*/
	 object MappedCounterexamples extends CounterexampleBackTranslator{
		 def getString(error:silver.verifier.VerificationError): String = "TODO Mapped"
	 }
	object NativeCounterexamples extends CounterexampleBackTranslator{
		def getString(error:silver.verifier.VerificationError): String= error.counterexample match {
			case None => "no counterexamples"
        	case Some(value) => value.toString()
		}
	 }
	 object ReducedCounterexamples extends CounterexampleBackTranslator{
		 def getString(error:silver.verifier.VerificationError): String = "TODO reduced"
	 }
	 object ExtendedCounterexamples extends CounterexampleBackTranslator{
		 def getString(error:silver.verifier.VerificationError): String = "TODO extended"
	 }

}