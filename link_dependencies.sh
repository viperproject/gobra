#!/usr/bin/bash
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

source viper-toolchain-versions.sh

git submodule update --init
cd silver
git checkout $SILVER_REF
cd ../silicon
git checkout $SILICON_REF
ln -s ../silver silver
cd ../carbon
git checkout $CARBON_REF
ln -s ../silver silver
cd ../viperserver
git checkout $VIPERSERVER_REF
ln -s ../silver silver
ln -s ../silicon silicon
ln -s ../carbon carbon
