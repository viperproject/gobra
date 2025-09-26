#!/bin/bash

BASE_FLAGS=
ANALYSIS_FLAGS="--enableAssumptionAnalysis --disableInfeasibilityChecks"
ANALYSIS_WITH_INFEAS_FLAGS="--enableAssumptionAnalysis"
PATH_TO_VERIFIED_SCION=[YOUR_PATH_TO_VERIFIED_SCION]

RESULT_FILE=scion_benchmark_$(date '+%Y_%m_%d_%H_%M_%S').csv
touch $RESULT_FILE
echo "test name,runtimes [s]" > $RESULT_FILE

function run(){
  ARGS="-Xss1g -Xmx4g 
  -jar $PATH_TO_VERIFIED_SCION/gobra/gobra.jar 
  -p $PATH_TO_VERIFIED_SCION/pkg/addr 
  --norespectFunctionPrePermAmounts 
  --backend SILICON 
  --chop 1 
  -I $PATH_TO_VERIFIED_SCION $PATH_TO_VERIFIED_SCION/verification/dependencies 
  --onlyFilesWithHeader 
  -m github.com/scionproto/scion 
  --assumeInjectivityOnInhale --checkConsistency 
  --mceMode=od --experimentalFriendClauses 
  --moreJoins off 
  -g /tmp/
  $2
  $3
  " 
  echo "Executing $1"
  echo "$ARGS"
  total_duration=0
  NUM_RUNS=3
  runtimes=""
  for i in $(seq 1 $NUM_RUNS); do
    echo $i
    start=$(date +%s)
    java $ARGS
    end=$(date +%s)
    duration=$((end-start))
    total_duration=$(((total_duration+duration)));
    runtimes+=",$duration"
  done
  res=$((total_duration/NUM_RUNS))
  echo "Finished $1: mean runtime [s] ${res}s"
  echo "$1$runtimes" >> $RESULT_FILE
}

run "baseline" $BASE_FLAGS 
run "analysis_sound" $ANALYSIS_FLAGS 
run "analysis_with_infeas_checks" $ANALYSIS_WITH_INFEAS_FLAGS 