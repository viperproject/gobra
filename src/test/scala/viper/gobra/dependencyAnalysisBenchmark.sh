#!/bin/bash

# execute `sh .\src\test\scala\viper\gobra\dependencyAnalysisBenchmark.sh`
# make sure no other sbt server is running

TARGET=src/test/resources/dependencyAnalysis/performance_benchmark
WARMUP=src/test/resources/dependencyAnalysis/warm-up
REPS=12
CSV_BASE=src/test/resources/dependencyAnalysis/performance_benchmark/data
TIMEOUT=900

BASE_FLAGS="--disablePureFunctsTerminationRequirement"
ANALYSIS_FLAGS="--disablePureFunctsTerminationRequirement,--enableDependencyAnalysis,--disableInfeasibilityChecks"
ANALYSIS_WITH_INFEAS_FLAGS="--disablePureFunctsTerminationRequirement,--enableDependencyAnalysis"

function run(){
  SBT_ARGS="
    test:runMain 
    -DGOBRATESTS_TARGET=$TARGET
    -DGOBRATESTS_WARMUP=$WARUMUP 
    -DGOBRATESTS_REPETITIONS=$REPS
    -DGOBRATESTS_CSV=${CSV_BASE}_$2.csv
    -DGOBRATESTS_TIMEOUT=$TIMEOUT
    -DGOBRATESTS_FLAGS="$1" 
    org.scalatest.tools.Runner 
    -o -s 
    viper.gobra.DependencyAnalysisBenchmarkTest
  "
  echo "Executing $2\n$SBT_ARGS"
  sbt "$SBT_ARGS"
}

run $BASE_FLAGS "baseline"
run $ANALYSIS_FLAGS "analysis"
run $ANALYSIS_WITH_INFEAS_FLAGS "analysis_with_infeas_checks"


