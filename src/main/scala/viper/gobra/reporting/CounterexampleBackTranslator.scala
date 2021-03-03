package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.silicon.interfaces.SiliconCounterexample
import viper.silver

trait CounterexampleBackTranslator{
	/**
	 * 
	 *  gets back a translator with a counterexample written in the couterexample field of the VerificationError in VerifierError.scala
	 * 
	  * @param backtrack info for backtranslation i assume
	  * @return modified version of ErrorBacktranslator
	  */
	def getTranslator( backtrack: BackTranslator.BackTrackInfo) :BackTranslator.ErrorBackTranslator
}
/**
  * simple counterexample distinction
  */

object CounterexampleBackTranslators{
	
	 object MappedCounterexamples extends CounterexampleBackTranslator{
		 def getTranslator(backtrack:BackTranslator.BackTrackInfo) :BackTranslator.ErrorBackTranslator = new CounterexampleMappedTransformer(backtrack)
	 }
	object NativeCounterexamples extends CounterexampleBackTranslator{
		def getTranslator( backtrack: BackTranslator.BackTrackInfo) :BackTranslator.ErrorBackTranslator = new CounterexampleNativeTransformer(backtrack)
	 }
	 object ReducedCounterexamples extends CounterexampleBackTranslator{
		 def getTranslator( backtrack: BackTranslator.BackTrackInfo) :BackTranslator.ErrorBackTranslator = new DefaultErrorBackTranslator(backtrack)
	 }
	 object ExtendedCounterexamples extends CounterexampleBackTranslator{
		 def getTranslator(backtrack: BackTranslator.BackTrackInfo) :BackTranslator.ErrorBackTranslator = new DefaultErrorBackTranslator(backtrack)
	 }

	
}
//makes shure nothing but the counterexample changes
class CounterexampleTransformer(backtrack: BackTranslator.BackTrackInfo)  extends BackTranslator.ErrorBackTranslator{
	val default = new DefaultErrorBackTranslator(backtrack)
	def translate(error: silver.verifier.VerificationError): VerificationError = default.translate(error)
	def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason = default.translate(reason)
}


class CounterexampleNativeTransformer(backtrack: BackTranslator.BackTrackInfo) extends CounterexampleTransformer(backtrack){
	override def translate(error: silver.verifier.VerificationError): VerificationError = {
		val err =default.translate(error)
		err.counterexample = error.counterexample
		err
	}
}
//TODO: Implement
class CounterexampleMappedTransformer(backtrack: BackTranslator.BackTrackInfo) extends CounterexampleTransformer(backtrack){
	override def translate(error: silver.verifier.VerificationError): VerificationError = {
		val err =default.translate(error)
		val silcounterexample = error.counterexample
		err
	}
}
class CounterexampleReducedTransformer(backtrack: BackTranslator.BackTrackInfo) extends CounterexampleTransformer(backtrack){
	override def translate(error: silver.verifier.VerificationError): VerificationError = {
		val err =default.translate(error)
		val silcounterexample = error.counterexample
		err
	}
}
class CounterexampleExtendedTransformer(backtrack: BackTranslator.BackTrackInfo) extends CounterexampleTransformer(backtrack){
	override def translate(error: silver.verifier.VerificationError): VerificationError = {
		val err =default.translate(error)
		val silcounterexample = error.counterexample
		err
	}
}
