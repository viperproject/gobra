# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

# this Dockerfile is based on ../docker/Dockerfile providing sbt, java 11, and Z3 4.8.7:
FROM gobraverifier/gobra-base:v5_z3_4.8.7

# create a gobra user:
RUN useradd --create-home --shell /bin/bash gobra
USER gobra
ENV HOME "/home/gobra"
WORKDIR $HOME

# copy content into image:
ADD content/* /home/gobra/

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
RUN cd $HOME && \
    # setup silver
    git clone https://github.com/viperproject/silver.git $HOME/silver && \
    cd $HOME/silver && \
    git reset --hard $SILVER_COMMIT && \
    cd $HOME && \
    # setup carbon
    git clone https://github.com/viperproject/carbon.git $HOME/carbon && \
    cd $HOME/carbon && \
    git reset --hard $CARBON_COMMIT && \
    ln --symbolic $HOME/silver && \
    cd $HOME && \
    # setup silicon
    git clone https://github.com/viperproject/silicon.git $HOME/silicon && \
    cd $HOME/silicon && \
    git reset --hard $SILICON_COMMIT && \
    ln --symbolic $HOME/silver && \
    cd $HOME && \
    # setup gobra
    git clone https://github.com/viperproject/gobra.git $HOME/gobra && \
    cd $HOME/gobra && \
    git reset --hard $GOBRA_COMMIT && \
    ln --symbolic $HOME/silver && ln --symbolic $HOME/silicon && ln --symbolic $HOME/carbon && \
    # compile and assemble gobra.jar:
    sbt clean assembly && \
    # copy target/scala-2.13/gobra.jar to home directory:
    cp target/scala-2.13/gobra.jar $HOME/gobra.jar && \
    # compile and assemble gobra-test.jar:
    sbt clean test:assembly && \
    # copy target/scala-2.13/gobra-test.jar to home directory:
    cp target/scala-2.13/gobra-test.jar $HOME/gobra-test.jar && \
    # delete all temporary files (ignored, non-ignored, and directories):
    git clean -fxd && \
    # recreate symlink:
    ln --symbolic $HOME/silver && ln --symbolic $HOME/silicon && ln --symbolic $HOME/carbon && \
    # cleanup silver, silicon, and carbon as well:
    cd $HOME/silver && \
    git clean -fxd && \
    cd $HOME/carbon && \
    git clean -fxd && \
    ln --symbolic $HOME/silver && \
    cd $HOME/silicon && \
    git clean -fxd && \
    ln --symbolic $HOME/silver && \
    cd $HOME

# setup test_suite folder:
RUN mkdir -p $HOME/test_suite/regressions $HOME/test_suite/evaluation && \
    # copy regression files:
    cp -r $HOME/gobra/src/test/resources/regressions/* $HOME/test_suite/regressions && \
    # copy evaluation files:
    cp -r $HOME/gobra/src/test/resources/regressions/examples/* $HOME/test_suite/evaluation

ENV MOUNT_DIR $HOME/sync

RUN mkdir -p $MOUNT_DIR

ENTRYPOINT \
    # copy all resources that should be available in the mounted sync folder:
    cp $HOME/README.md $MOUNT_DIR && \
    # `$@` corresponds to the arguments passed to docker run:
    /bin/bash "$@"
