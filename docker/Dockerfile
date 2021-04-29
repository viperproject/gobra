# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2020 ETH Zurich.

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


WORKDIR /
