<img src=".github/docs/gobra.png" height="250">

[![Test Status](https://github.com/viperproject/gobra/workflows/test/badge.svg?branch=master)](https://github.com/viperproject/gobra/actions?query=workflow%3Atest+branch%3Amaster)
[![License: MPL 2.0](https://img.shields.io/badge/License-MPL%202.0-brightgreen.svg)](./LICENSE)

[Gobra](https://www.pm.inf.ethz.ch/research/gobra.html) is a prototype verifier for Go programs, based on the [Viper verification infrastructure](https://www.pm.inf.ethz.ch/research/viper.html).

We call annotated Go programs Gobra programs and use the file extension `.gobra` for them. Examples can be found in [`src/test/resources`](https://github.com/viperproject/gobra/blob/master/src/test/resources).

## Compile and Run Gobra
To compile Gobra you need a JDK (e.g. OpenJDK 11) and sbt.
In addition, symbolic links have to be created in the Gobra root directory to the following Viper dependencies:
- [silver](https://github.com/viperproject/silver)
- [silicon](https://github.com/viperproject/silicon)
- [carbon](https://github.com/viperproject/carbon)
- [viperserver](https://github.com/viperproject/viperserver)

Note that silicon and carbon require themselves a symlink to silver and viperserver symlinks to silver, silicon, and carbon.

`sbt "set test in assembly := {}" clean assembly` will compile Gobra and its dependency and create `target/scala-2.12/gobra.jar`.

`java -jar target/scala-2.12/gobra.jar -i <path to a Gobra file>` verifies a Gobra program.
As Microsoft's Z3 is used as underlying SMT solver, Z3 has to be installed. You can optionally pass a path to the Z3 binary using the `--z3Exe` option to Gobra in case Z3 is not in your path.

`java -jar target/scala-2.12/gobra.jar --help` lists all available command line options.

If you configure Gobra to use the verification-condition-generation-based verifier (Carbon) then Boogie and Mono have to additionally be installed.

All tests can be executed using `sbt test`.
