# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.


### begin copy of docker/Dockerfile
FROM ubuntu:20.04

ENV DEBIAN_FRONTEND noninteractive

# Z3_URL and Z3_DIR ENV variables are configured further down in this Dockerfile such that changes to them to not
# affect the first layers of this container

# Install prerequisites
RUN apt-get update && \
    apt-get install -y \
        curl \
        git \
        gnupg2 \
        locales \
        unzip \
    && \
    rm -rf /var/lib/apt/lists/*

# Set up locale
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

# Install Java
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    rm -rf /var/lib/apt/lists/*

# Install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt && \
    rm -rf /var/lib/apt/lists/*


# Install Z3
# ENV Z3_URL="https://github.com/Z3Prover/z3/releases/download/z3-4.8.6/z3-4.8.6-x64-ubuntu-16.04.zip"
ENV Z3_URL="https://github.com/Z3Prover/z3/releases/download/z3-4.8.7/z3-4.8.7-x64-ubuntu-16.04.zip"
# path to z3 binary unzipping Z3_ZIP:
# ENV Z3_BIN="z3-4.8.6-x64-ubuntu-16.04/bin/z3"
ENV Z3_BIN="z3-4.8.7-x64-ubuntu-16.04/bin/z3"

# download z3 release from GitHub, unzip, and copy to bin:
RUN curl -L $Z3_URL --output /z3.zip && \
    unzip /z3.zip -d /z3/ && \
    rm /z3.zip && \
    cp /z3/$Z3_BIN /usr/bin/ && \
    rm -r /z3
# executing `which z3` and setting Z3_EXE based on its result is unfortunately not possible, therefore it is hardcoded
ENV Z3_EXE /usr/bin/z3
### end copy of docker/Dockerfile


# create a gobra user:
RUN useradd --create-home --shell /bin/bash gobra
USER gobra
ENV HOME "/home/gobra"
WORKDIR $HOME

# can be a
# - tag (e.g. "v.21.01-release")
# - commit hash or
# - branch name (e.g. "origin/artifact" - note the leading "origin/")
ENV SILVER_COMMIT "v.21.01-release"
ENV CARBON_COMMIT "v.21.01-release"
ENV SILICON_COMMIT "v.21.01-release"
# GOBRA_COMMIT can be set while building the docker image by using `docker build --build-arg GOBRA_COMMIT=<some branch>`
# by default, "origin/master" is used:
ARG GOBRA_COMMIT="origin/master"
# store GOBRA_COMMIT ARG in an environment variable with the same name such that it is still available after building
# the container:
ENV GOBRA_COMMIT $GOBRA_COMMIT
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
    echo "Switched to Gobra commit $GOBRA_COMMIT" && \
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
RUN mkdir -p $HOME/test_suite/regressions $HOME/test_suite/evaluation/log_files $HOME/test_suite/tutorial-examples && \
    # copy regression files:
    cp -r $HOME/gobra/src/test/resources/regressions/* $HOME/test_suite/regressions && \
    # copy evaluation files:
    cp -r $HOME/gobra/src/test/resources/regressions/examples/evaluation/* $HOME/test_suite/evaluation && \
    # copy tutorial files:
    cp -r $HOME/gobra/src/test/resources/regressions/examples/tutorial-examples/* $HOME/test_suite/tutorial-examples

# copy content into image:
ADD content/* $HOME/
ADD Dockerfile $HOME/
# copy evaluation log files:
ADD evaluation/* $HOME/test_suite/evaluation/log_files/

ENV MOUNT_DIR $HOME/sync

RUN mkdir -p $MOUNT_DIR

ENTRYPOINT \
    # copy all resources that should be available in the mounted sync folder:
    cp $HOME/README.md $MOUNT_DIR && \
    cp $HOME/Dockerfile $MOUNT_DIR && \
    cp $HOME/tutorial.md $MOUNT_DIR && \
    cp $HOME/paper.pdf $MOUNT_DIR && \
    cp -r $HOME/test_suite $MOUNT_DIR && \
    # `$@` corresponds to the arguments passed to docker run:
    /bin/bash "$@"
