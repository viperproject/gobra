#!/bin/sh
# this script is executed as part of the `test` workflow in the "build" target image built
# from workflow-container/Dockerfile

# start pidstats and write output to synced folder /build/gobra/sync
mkdir -p /build/gobra/sync
pidstat 1 -r -H -p ALL > /build/gobra/sync/pidstat.txt & PIDSTAT_PID=$!

# the following command would run SBT (instead of using the precompiled tests):
# execute sbt with the provided $SBTCOMMAND or 'test' and stop pidstat independent of outcome
# set `-Dsbt.color=always` such that sbt displays warnings in yellow, passed test cases in green and failed ones in red.
# it seems that sbt on it's own thinks that colors cannot be displayed when run in docker and thus turns them off by default.
# COMMAND="${SBTCOMMAND:-test}"
# sbt -Dsbt.color=always "$COMMAND"
# TEST_RES=$?
# kill -INT $PIDSTAT_PID

TEST_ENV_VARS="${TEST_ENV_VARS:-}"
TEST_ARGS="${TEST_ARGS:-}"
# print command and execute the precompiled tests:
echo "java -Xss128m -XshowSettings:vm -cp target/scala-2.13/gobra-test.jar -Dlogback.configurationFile=conf/logback.xml $TEST_ENV_VARS org.scalatest.tools.Runner -R target/scala-2.13/test-classes -o $TEST_ARGS"
java -Xss128m -XshowSettings:vm -cp target/scala-2.13/gobra-test.jar -Dlogback.configurationFile=conf/logback.xml $TEST_ENV_VARS org.scalatest.tools.Runner -R target/scala-2.13/test-classes -o $TEST_ARGS
TEST_RES=$?
kill -INT $PIDSTAT_PID

# set exit code depending on result of `sbt test`:
exit $TEST_RES
