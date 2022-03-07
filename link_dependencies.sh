#!/usr/bin/bash
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

git submodule update --init
cd silicon
ln -s ../silver silver
cd ../carbon
ln -s ../silver silver
cd ../viperserver
ln -s ../silver silver
ln -s ../silicon silicon
ln -s ../carbon carbon
