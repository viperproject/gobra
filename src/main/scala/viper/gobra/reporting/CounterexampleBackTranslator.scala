package viper.gobra.reporting

import viper.gobra.translator.interfaces.Context
import viper.silicon.interfaces.SiliconCounterexample

trait CounterexampleBackTranslator{
	def getString(report:SiliconCounterexample,context:Context): String
}


object CounterexampleBackTranslators{
	 object MappedCounterexamples extends CounterexampleBackTranslator{
		 def getString(report:SiliconCounterexample,context:Context):String = "TODO Mapped"
	 }
	object NativeCounterexamples extends CounterexampleBackTranslator{
		def getString(report:SiliconCounterexample,context:Context):String = "TODO Mapped"
	 }
	 object ReducedCounterexamples extends CounterexampleBackTranslator{
		 def getString(report:SiliconCounterexample,context:Context):String = "TODO Mapped"
	 }
	 object ExtendedCounterexamples extends CounterexampleBackTranslator{
		 def getString(report:SiliconCounterexample,context:Context):String = "TODO Mapped"
	 }

}