#!/bin/bash

java \
  -Xss128m \
  -cp /gobra.jar \
  -Dlogback.configurationFile=/gobra/conf/logback.xml  \
  viper.gobra.GobraRunner \
  "$@"
