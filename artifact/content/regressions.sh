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

GOBRATESTS_REGRESSIONS_DIR="$HOME/test_suite/regressions" # Directory with Gobra test files

echo "This script will run Gobra regression test cases found under ${GOBRATESTS_REGRESSIONS_DIR}. This may take a long time, so please be patient (progress is reported between test case changes)."
echo
quit_or_continue

java \
  -Xss128m \
  -cp $HOME/gobra-test.jar \
  -Dlogback.configurationFile=$HOME/gobra/conf/logback.xml  \
  -DGOBRATESTS_REGRESSIONS_DIR=${GOBRATESTS_REGRESSIONS_DIR} \
  org.scalatest.run viper.gobra.GobraTests
