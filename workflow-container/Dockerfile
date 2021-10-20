# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

FROM openjdk:11.0.12-slim as build

WORKDIR /build

RUN apt-get update && \
	apt-get install -y \
	gnupg2 \
	curl \
	unzip \
	git

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
### end copy of docker/Dockerfile

ARG VIPER_BRANCH=tags/v.21.07-release

# Setup silver
RUN git clone https://github.com/viperproject/silver.git ./silver && \
	cd silver && \
	git checkout $VIPER_BRANCH && \
	cd ..

# Setup carbon
RUN git clone https://github.com/viperproject/carbon.git ./carbon && \
	cd carbon && \
	git checkout $VIPER_BRANCH && \
	ln -s ../silver silver && \
	cd ..

# Setup Silicon
RUN git clone https://github.com/viperproject/silicon.git ./silicon && \
	cd silicon && \
	git checkout $VIPER_BRANCH && \
	ln -s ../silver silver && \
	cd ..

# Setup gobra
WORKDIR /build/gobra/
COPY build.sbt /build/gobra/
COPY project /build/gobra/project
COPY conf /build/gobra/conf
COPY src /build/gobra/src
RUN ln -s ../silver silver && ln -s ../silicon silicon && ln -s ../carbon carbon

RUN sbt assembly

FROM openjdk:11-jre-slim

# Install Boogie


RUN apt-get update; \
	apt-get install -y apt-transport-https wget && \
	wget https://packages.microsoft.com/config/debian/11/packages-microsoft-prod.deb -O packages-microsoft-prod.deb && \
	dpkg -i packages-microsoft-prod.deb &&\
	rm packages-microsoft-prod.deb && \
	apt-get update && \
	apt-get install -y dotnet-sdk-5.0 && \
        apt-get remove -y apt-transport-https wget && apt-get autoremove -y

RUN dotnet tool install --global boogie

ENV BOOGIE_EXE /root/.dotnet/tools/boogie

# copy downloaded & unziped z3
COPY --from=build /usr/bin/z3 /usr/bin/
# executing `which z3` and setting Z3_EXE based on its result is unfortunately not possible, therefore it is hardcoded
ENV Z3_EXE /usr/bin/z3

WORKDIR /gobra
COPY --from=build /build/gobra/target/scala-2.13/gobra.jar .
ENTRYPOINT ["java", "-Xss128m", "-jar", "gobra.jar"]

