#!/bin/bash

# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2021 ETH Zurich.

java \
  -Xss128m \
  -cp $HOME/gobra.jar \
  -Dlogback.configurationFile=$HOME/gobra/conf/logback.xml  \
  viper.gobra.GobraRunner \
  -i "$@"
