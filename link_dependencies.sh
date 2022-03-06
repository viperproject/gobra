#!/usr/bin/bash

cd silver
git checkout 7228e7144d41c91f02a70a68a93b6b3efae57d14
cd ../silicon
git checkout 22551b40509c381991a493da0108ea4c97fd602d
ln -s ../silver silver
cd ../carbon
git checkout 4393d154a5ae24d994a0c2c578374bdd49c3a3b3
ln -s ../silver silver
cd ../viperserver
git checkout 5907ce1744501b7949d25cc0d5356145431ab6f7
ln -s ../silver silver
ln -s ../silicon silicon
ln -s ../carbon carbon
