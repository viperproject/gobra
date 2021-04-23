#!/bin/bash

java \
  -Xss128m \
  -cp $HOME/gobra.jar \
  -Dlogback.configurationFile=$HOME/gobra/conf/logback.xml  \
  viper.gobra.GobraRunner \
  -i "$@"
