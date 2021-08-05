#!/bin/bash

# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

function quit_or_continue() {
  read -p "Press Q to abort, any other key to continue ..." -n 1 -s KEY
  echo
  if [[ ${KEY^^} == "Q" ]]; then exit; fi # https://stackoverflow.com/a/32210715
}

GOBRATESTS_REPETITIONS_DEFAULT=5 # Number of runs per test if GOBRATESTS_REPETITIONS ENV variable is not specified
GOBRATESTS_REPETITIONS=${GOBRATESTS_REPETITIONS:-$GOBRATESTS_REPETITIONS_DEFAULT} # Number of runs per test
GOBRATESTS_TIMEOUT_DEFAULT=600 # max timeout in s per run if GOBRATESTS_TIMEOUT ENV variable is not specified
GOBRATESTS_TIMEOUT=${GOBRATESTS_TIMEOUT:-$GOBRATESTS_TIMEOUT_DEFAULT} # max timeout in s per run
GOBRATESTS_TARGET="$HOME/test_suite/evaluation" # Directory with Gobra test files
GOBRATESTS_CSV="$HOME/benchmark_$(date +%Y-%m-%d_%H-%M).csv" # CSV file for benchmark results

echo "This script will run Gobra ${GOBRATESTS_REPETITIONS} times (with a timeout of ${GOBRATESTS_TIMEOUT}s each) on each test case found under ${GOBRATESTS_TARGET}, and produce a CSV file ${GOBRATESTS_CSV} with the benchmark results. This may take a long time, so please be patient (progress is reported between test case changes)."
echo
quit_or_continue

java \
  -Xss128m \
  -cp $HOME/gobra-test.jar \
  -Dlogback.configurationFile=$HOME/gobra/conf/logback.xml  \
  -DGOBRATESTS_TARGET=${GOBRATESTS_TARGET} \
  -DGOBRATESTS_REPETITIONS=${GOBRATESTS_REPETITIONS} \
  -DGOBRATESTS_CSV=${GOBRATESTS_CSV} \
  -DGOBRATESTS_TIMEOUT=${GOBRATESTS_TIMEOUT} \
  org.scalatest.run viper.gobra.OverallBenchmarkTests

# copy benchmark results to the possibly mounted directory:
cp ${GOBRATESTS_CSV} ${MOUNT_DIR}
