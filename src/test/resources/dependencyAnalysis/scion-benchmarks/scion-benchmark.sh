#!/bin/bash

BASE_FLAGS=
ANALYSIS_FLAGS="--enableDependencyAnalysis --disableInfeasibilityChecks"
ANALYSIS_WITH_INFEAS_FLAGS="--enableDependencyAnalysis"
BASE_WITH_UNSAT_FLAGS="--enableUnsatCores"
PATH_TO_VERIFIED_SCION="C:/Users/andre/dev/viper/VerifiedSCION"
PATH_TO_GOBRA="C:/Users/andre/dev/viper/gobra"
SCION_PACKAGE=$1
RESULT_FILE=$2_$(date '+%Y_%m_%d_%H_%M_%S').csv

touch $RESULT_FILE
echo "test name,runtimes [s]" > $RESULT_FILE

function run(){
  ARGS="-Xss1g -Xmx4g 
  -jar $PATH_TO_GOBRA/target/scala-2.13/gobra.jar
  -p $PATH_TO_VERIFIED_SCION/$SCION_PACKAGE
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
  NUM_RUNS=10
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

run "baseline" $BASE_FLAGS # warumup
run "baseline_with_unsat" $BASE_WITH_UNSAT_FLAGS
run "analysis_sound" $ANALYSIS_FLAGS
run "analysis_with_infeas_checks" $ANALYSIS_WITH_INFEAS_FLAGS
run "baseline" $BASE_FLAGS