package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.silicon.interfaces.SiliconCounterexample
import viper.silver
import _root_.viper.silver.verifier.Counterexample

trait CounterexampleConfig
/**
  * simple counterexample distinction
  */

object CounterexampleConfigs{
	object MappedCounterexamples extends CounterexampleConfig
	object NativeCounterexamples extends CounterexampleConfig
	object ReducedCounterexamples extends CounterexampleConfig
	object ExtendedCounterexamples extends CounterexampleConfig
}


//makes shure nothing but the counterexample changes
class CounterexampleBackTranslator(backtrack: BackTranslator.BackTrackInfo,info:CounterexampleConfig)  extends BackTranslator.ErrorBackTranslator{
	val default = new DefaultErrorBackTranslator(backtrack)
	val translator : ((Counterexample)=>Counterexample) = info match {
		case CounterexampleConfigs.MappedCounterexamples => mappedTranslation(_)
		case CounterexampleConfigs.NativeCounterexamples => nativeTranslation(_)
		case CounterexampleConfigs.ReducedCounterexamples => reducedTranslation(_)
		case CounterexampleConfigs.ExtendedCounterexamples => extendedTranslation(_)
	}
	def translate(error: silver.verifier.VerificationError): VerificationError ={
		val ret =default.translate(error)
		ret.counterexample = error.counterexample match {
			case Some(example) => Some(translator(example))
			case None => None
		}
		ret
	} 
	def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason = default.translate(reason)

	def mappedTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	}
	def nativeTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	} 
	def reducedTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	} 
	def extendedTranslation(counterexample:Counterexample):Counterexample ={
		counterexample
	}  

}
