# Gobra Verification Dependency Analysis

To enable dependency analysis, run Gobra with `--enableDependencyAnalysis --disableInfeasibilityChecks`

Commands to be executed on the final dependency graph can be input via the `--dependencyAnalysisMode` config flag. 
Available commands include `interactive` and any command supported by the CLI tool. 
Commands can be combined using `;`. For example, `--dependencyAnalysisMode=export>graphExpors/test;progress;interactive`.
