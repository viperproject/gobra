# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2020 ETH Zurich.

FROM ubuntu:20.04

ENV DEBIAN_FRONTEND noninteractive

# Install prerequisites
RUN apt-get update && \
    apt-get install -y \
        curl \
        git \
        gnupg2 \
        locales \
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
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt && \
    rm -rf /var/lib/apt/lists/*

# Install Z3
RUN apt-get update && \
    apt-get install -y z3 && \
    rm -rf /var/lib/apt/lists/*
# executing `which z3` and setting Z3_EXE based on its result is unfortunately not possible, therefore it is hardcoded
ENV Z3_EXE /usr/bin/z3

WORKDIR /
