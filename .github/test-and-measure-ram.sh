#!/bin/sh

# start pidstats and write output to synced folder /build/gobra/sync
pidstat 1 -r -H -p ALL > /build/gobra/sync/pidstat.txt & PIDSTAT_PID=$!

# execute sbt test and stop pidstat independent of outcome
# sbt test
java -Xss128m -jar /build/gobra/target/scala-2.13/gobra.jar -i /build/gobra/src/test/resources/regressions/examples/tutorial-examples/basicAnnotations.gobra
TEST_RES=$?
kill -INT $PIDSTAT_PID

# the following command would execute the precompiled tests:
# java -Xss128m -XshowSettings:vm -cp target/scala-2.13/gobra-test.jar -Dlogback.configurationFile=conf/logback.xml -DGOBRATESTS_REGRESSIONS_DIR=src/test/resources/regressions -DGOBRATESTS_SAME_PACKAGE_DIR=src/test/resources/same_package org.scalatest.tools.Runner -R target/scala-2.13/test-classes -o

# set exit code depending on result of `sbt test`:
exit $TEST_RES
