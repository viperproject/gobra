#!/bin/bash

function quit_or_continue() {
  read -p "Press Q to abort, any other key to continue ..." -n 1 -s KEY
  echo
  if [[ ${KEY^^} == "Q" ]]; then exit; fi # https://stackoverflow.com/a/32210715
}

GOBRATESTS_REPETITIONS=5 # Number of runs per test
GOBRATESTS_TARGET="/test_suite/evaluation" # Directory with Gobra test files
GOBRATESTS_CSV="/benchmark_$(date +%Y-%m-%d_%H-%M).csv" # CSV file for benchmark results

echo "This script will run Gobra ${GOBRATESTS_REPETITIONS} times on each test case found under ${GOBRATESTS_TARGET}, and produce a CSV file ${GOBRATESTS_CSV} with the benchmark results. This may take a long time, so please be patient (progress is reported between test case changes)."
echo
quit_or_continue

java \
  -Xss128m \
  -cp /gobra-test.jar \
  -Dlogback.configurationFile=/gobra/conf/logback.xml  \
  -DGOBRATESTS_TARGET=${GOBRATESTS_TARGET} \
  -DGOBRATESTS_REPETITIONS=${GOBRATESTS_REPETITIONS} \
  -DGOBRATESTS_CSV=${GOBRATESTS_CSV} \
  org.scalatest.run viper.gobra.OverallBenchmarkTests
