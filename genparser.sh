#!/bin/bash
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2022 ETH Zurich.

# This script generates a new parser from the antlr4 files stored in this repository.
# This script MUST NOT be run from a symlink.

##### Constants #####
RED='\033[0;31m'
GREEN='\033[0;32m'
RESET='\033[0m'

# This path is taken to be able to call this script from any directory and have
# the same consistent behaviour. This was taken from
# https://stackoverflow.com/questions/24112727/relative-paths-based-on-file-location-instead-of-current-working-directory
SCRIPT_DIR=$( cd "$(dirname "${BASH_SOURCE[0]}")" || exit 1; pwd -P )

##### Configure if it is the first execution #####
CONFIGFILE="$SCRIPT_DIR/genparser.config"
if ! test -f "$CONFIGFILE"; then
  echo -e "What is the ${RED}ABSOLUTE${RESET} path to the antlr4 .jar file?"
  read -r ANSWER
  echo "$ANSWER" > "$CONFIGFILE"
fi

ANTLR4_PATH=$(cat "$CONFIGFILE")

if ! test -f "$ANTLR4_PATH"; then
  echo "The antlr4 .jar file was not found ($ANTLR4_PATH). Delete the file $CONFIGFILE to reconfigure this script."
  exit 2
fi

echo -e "${GREEN}Generating the lexer:${RESET}"
java -jar "$ANTLR4_PATH" "$SCRIPT_DIR"/src/main/antlr4/GobraLexer.g4 -package viper.gobra.frontend || { echo "Error while generating the lexer."; exit 3; }

echo -e "${GREEN}Generating the parser:${RESET}"
java -jar "$ANTLR4_PATH" "$SCRIPT_DIR"/src/main/antlr4/GobraParser.g4 -package viper.gobra.frontend -visitor -no-listener || { echo "Error while generating the parser."; exit 3; }

echo -e "${GREEN}Moving the generated files:${RESET}"
mv -v "$SCRIPT_DIR"/src/main/antlr4/*.java "$SCRIPT_DIR"/src/main/java/viper/gobra/frontend/
