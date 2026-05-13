package viper.gobra

import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.{Config, Source}

import java.nio.file.Path

class DependencyAnalysisTests extends GobraTests {
	override val testDirectories: Seq[String] = Vector("dependencyAnalysis")

	protected override def getConfig(source: Source): Config =
		Config(
			logLevel = Level.INFO,
			reporter = StringifyReporter,
			packageInfoInputMap = Map(Source.getPackageInfoOrCrash(source, Path.of("")) -> Vector(source)),
			checkConsistency = true,
			z3Exe = z3Exe,
			enableDependencyAnalysis = true,
			disableInfeasibilityChecks = true,
			disableTerminationPlugin = true,
			disableCheckTerminationPureFns = true,
			executeDependencyAnalysisTests = true,
		)
}
