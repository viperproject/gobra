#!/bin/bash
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#
# Copyright (c) 2011-2022 ETH Zurich.

set -e

# This script generates a new parser from the antlr4 files stored in this repository.
# This script MUST NOT be run from a symlink.
# '--download' can optionally be passed as a parameter to download ANTLR and automatically create the config file
# if a config file does not exist yet.

##### Constants #####
RED='\033[0;31m'
GREEN='\033[0;32m'
RESET='\033[0m'

# This path is taken to be able to call this script from any directory and have
# the same consistent behaviour. This was taken from
# https://stackoverflow.com/questions/24112727/relative-paths-based-on-file-location-instead-of-current-working-directory
SCRIPT_DIR=$( cd "$(dirname "${BASH_SOURCE[0]}")" || exit 1; pwd -P )

mkdir -p "$SCRIPT_DIR/.genparser"

CONFIGFILE="$SCRIPT_DIR/genparser.config"
HASHFILE="$SCRIPT_DIR/.genparser/md5sums.txt"

##### Download and configure ANTLR if config file does not exist yet #####
if ! test -f "$CONFIGFILE" && [ "$1" = "--download" ]; then
  DESTINATION="$SCRIPT_DIR/.genparser/antlr-4.13.1-complete.jar"
  curl --fail --show-error -L "https://www.antlr.org/download/antlr-4.13.1-complete.jar" --output "$DESTINATION"
  echo "$DESTINATION" > "$CONFIGFILE"
fi

##### Configure if it is the first execution #####
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

##### Check whether parser generation is necessary #####
if test -f "$HASHFILE" && md5sum -c "$HASHFILE" --status; then
  echo "No changes detected in the antlr4 files. Skipping parser generation."
  exit 0
fi

echo -e "${GREEN}Generating the lexer:${RESET}"
java -jar "$ANTLR4_PATH" "$SCRIPT_DIR"/src/main/antlr4/GobraLexer.g4 -package viper.gobra.frontend || { echo -e "${RED}Error while generating the lexer.${RESET}"; exit 3; }

echo -e "${GREEN}Generating the parser:${RESET}"
java -jar "$ANTLR4_PATH" "$SCRIPT_DIR"/src/main/antlr4/GobraParser.g4 -package viper.gobra.frontend -visitor -no-listener || { echo -e "${RED}Error while generating the parser.${RESET}"; exit 3; }

echo -e "${GREEN}Moving the generated files:${RESET}"
mv -v "$SCRIPT_DIR"/src/main/antlr4/*.java "$SCRIPT_DIR"/src/main/java/viper/gobra/frontend/

##### Create hash file #####
# Since parser generation was successful, let's keep track of the hashes for all files on which parser generates depends
md5sum "$ANTLR4_PATH" "$SCRIPT_DIR"/src/main/antlr4/* > "$HASHFILE"
