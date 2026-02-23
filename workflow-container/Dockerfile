# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

FROM eclipse-temurin:21.0.10_7-jdk-jammy AS build

WORKDIR /build

RUN apt-get update && \
	apt-get install -y \
	gnupg2 \
	curl \
	unzip \
	git \
	sysstat

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install sbt
# Install Z3
ENV Z3_URL="https://github.com/Z3Prover/z3/releases/download/z3-4.8.7/z3-4.8.7-x64-ubuntu-16.04.zip"
# path to z3 binary unzipping Z3_ZIP:
ENV Z3_BIN="z3-4.8.7-x64-ubuntu-16.04/bin/z3"

# download z3 release from GitHub, unzip, and copy to bin:
RUN curl -L $Z3_URL --output /z3.zip && \
    unzip /z3.zip -d /z3/ && \
    rm /z3.zip && \
    cp /z3/$Z3_BIN /usr/bin/ && \
    rm -r /z3

ENV Z3_EXE=/usr/bin/z3

# Download Boogie
ENV BOOGIE_URL="https://github.com/viperproject/boogie-builder/releases/download/bae82d3a7b383161/boogie-linux.zip"

RUN curl -L $BOOGIE_URL --output /boogie.zip && \
    unzip /boogie.zip -d /boogie/ && \
    rm /boogie.zip

ENV BOOGIE_EXE=/boogie/binaries-linux/Boogie
# To fix boogie exception
ENV DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=true

# Setup Gobra
WORKDIR /build/gobra/
COPY build.sbt /build/gobra/
COPY genparser.sh /build/gobra/
COPY project /build/gobra/project
COPY conf /build/gobra/conf
COPY src /build/gobra/src
# Copies viperserver and, transitively, all of its dependencies (carbon, silicon and silver)
COPY viperserver /build/gobra/viperserver

RUN sbt assembly


# a second build stage starts here that is based of a fresh image.
# note that only stuff explicitly copied from "build" will be present.
# in particular, `sbt`, the Gobra sources or the Viper dependencies are not part of this image
# environment variables are not carried over either and need to be redeclared.
FROM eclipse-temurin:21.0.10_7-jdk-jammy

# copy downloaded & unzipped boogie
COPY --from=build /boogie/binaries-linux /boogie
ENV BOOGIE_EXE=/boogie/Boogie

# To fix boogie exception
ENV DOTNET_SYSTEM_GLOBALIZATION_INVARIANT=true

# copy downloaded & unzipped z3
COPY --from=build /usr/bin/z3 /usr/bin/
ENV Z3_EXE=/usr/bin/z3

WORKDIR /gobra
COPY --from=build /build/gobra/target/scala-2.13/gobra.jar .

# copy evaluation and tutorial examples
COPY src/test/resources/regressions/examples/evaluation ./evaluation
COPY src/test/resources/regressions/examples/tutorial-examples ./tutorial-examples

ENTRYPOINT ["java", "-Xss128m", "-jar", "gobra.jar"]
