# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

# this Dockerfile is based on ../docker/Dockerfile providing sbt, java 11, and Z3 4.8.6:
FROM gobraverifier/gobra-base:v5_z3_4.8.6

# copy scripts:
ADD benchmark.sh /
ADD gobra.sh /
ADD regressions.sh /

# can be a
# - tag (e.g. "v.21.01-release")
# - commit hash or
# - branch name (e.g. "origin/artifact" - note the leading "origin/")
ENV SILVER_COMMIT "v.21.01-release"
ENV CARBON_COMMIT "v.21.01-release"
ENV SILICON_COMMIT "v.21.01-release"
# TODO: fix to a particular commit or tag:
ENV GOBRA_COMMIT "origin/artifact"
# all commands are executed as part of a single "RUN" command in order that no intermediate layers with
# temporary files get created
RUN cd / && \
    # setup silver
    git clone https://github.com/viperproject/silver.git /silver && \
    cd /silver && \
    git reset --hard $SILVER_COMMIT && \
    cd / && \
    # setup carbon
    git clone https://github.com/viperproject/carbon.git /carbon && \
    cd /carbon && \
    git reset --hard $CARBON_COMMIT && \
    ln --symbolic /silver && \
    cd / && \
    # setup silicon
    git clone https://github.com/viperproject/silicon.git /silicon && \
    cd /silicon && \
    git reset --hard $SILICON_COMMIT && \
    ln --symbolic /silver && \
    cd / && \
    # setup gobra
    git clone https://github.com/viperproject/gobra.git /gobra && \
    cd /gobra && \
    git reset --hard $GOBRA_COMMIT && \
    ln --symbolic /silver && ln --symbolic /silicon && ln --symbolic /carbon && \
    # compile and assemble gobra.jar:
    sbt clean assembly && \
    # copy target/scala-2.13/gobra.jar to root directory:
    cp target/scala-2.13/gobra.jar /gobra.jar && \
    # compile and assemble gobra-test.jar:
    sbt clean test:assembly && \
    # copy target/scala-2.13/gobra-test.jar to root directory:
    cp target/scala-2.13/gobra-test.jar /gobra-test.jar && \
    # delete all temporary files (ignored, non-ignored, and directories):
    git clean -fxd && \
    # recreate symlink:
    ln --symbolic /silver && ln --symbolic /silicon && ln --symbolic /carbon && \
    # cleanup silver, silicon, and carbon as well:
    cd /silver && \
    git clean -fxd && \
    cd /carbon && \
    git clean -fxd && \
    ln --symbolic /silver && \
    cd /silicon && \
    git clean -fxd && \
    ln --symbolic /silver && \
    cd /

# setup test_suite folder:
RUN mkdir -p /test_suite/regressions /test_suite/evaluation && \
    # copy regression files:
    cp -r /gobra/src/test/resources/regressions/* /test_suite/regressions && \
    # copy evaluation files:
    cp -r /gobra/src/test/resources/regressions/examples/* /test_suite/evaluation
