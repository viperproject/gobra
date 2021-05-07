# Gobra: Modular Specification and Verification of Go Programs

## Running Gobra

Gobra comes as a ready-to-use Docker image called `gobra_docker_image.tar`. It can be loaded into Docker by using `docker load -i gobra_docker_image.tar`. The loaded image is named `gobraverifier/gobra-artifact:v1`.

Docker should be configured such that at least 6GB of RAM is available to containers. Otherwise, the benchmark might not terminate or throw an out-of-memory exception (see the [troubleshooting](#troubleshooting) section). On Mac and Linux, this can be configured in the Docker Preferences (click the whale icon in the status bar > Resources > Advanced > Memory). On Windows, the advanced setting is only available in Hyper-V mode. In WSL2 mode, the default memory settings should suffice, otherwise [the WSL2 configuration has to be adapted](https://docs.microsoft.com/en-us/windows/wsl/wsl-config#configure-global-options-with-wslconfig).

To run the artifact, we recommend the use of a shared folder to access files provided by the Docker image directly on the host. Of course, the artifact can also be evaluated without a shared folder (then skip the first bullet point).

- Create a new folder in your current directory. This will be your shared folder on the host. For the remainder of this README, we will assume that this folder is called `gobra_sync`.

- Next we start an interactive version of the Gobra artifact in your terminal. If you use a shared folder, then execute the following command:
  ```commandline
  docker run -it --memory=6g --volume $PWD/gobra_sync:/home/gobra/sync gobraverifier/gobra-artifact:v1
  ```
  In the shared folder `gobra_sync` on your host machine, you can now find this README (`README.md`), a tutorial (`tutorial.md`), our paper (`paper.pdf`), the Dockerfile used to create the artifact image (`Dockerfile`), and our evaluation, regression, and tutorial examples (`test_suite/evaluation`, `test_suite/regressions`, and `test_suite/tutorial-examples`, respectively). The `gobra_sync` folder on your host machine is now synced with `/home/gobra/sync/` folder on the Docker container. If you change a file on the host, then the corresponding file on the Docker container will change, as well. If you encounter permission errors when running the previous command, please consult the [troubleshooting section](#troubleshooting).
 
  To run the container without a shared folder, drop the `--volume` option:
  ```commandline
  docker run -it --memory=6g gobraverifier/gobra-artifact:v1
  ```
  Copies of all files mentioned above are at `/home/gobra` on the Docker container with the same path structure. Furthermore, the files are available at our GitHub repository ([tutorial](https://github.com/viperproject/gobra/blob/artifact1/artifact/content/tutorial.md), [Dockerfile](https://github.com/viperproject/gobra/blob/artifact1/artifact/Dockerfile), [evaluation](https://github.com/viperproject/gobra/tree/artifact1/src/test/resources/regressions/examples/evaluation), and [tutorial examples](https://github.com/viperproject/gobra/tree/artifact1/src/test/resources/regressions/examples/tutorial-examples)).
 
- As an example, let us verify `test_suite/evaluation/example-2-1.gobra`. If you use a shared folder, then run the following command in the Docker container:
  ```commandline
  /home/gobra/gobra.sh /home/gobra/sync/test_suite/evaluation/example-2-1.gobra
  ```
  Without a shared folder, run the following command in the Docker container:
  ```commandline
  /home/gobra/gobra.sh /home/gobra/test_suite/evaluation/example-2-1.gobra
  ```
  If you did not make any changes, verification should succeed with the following output (among a copyright notice and Gobra's version):
  ```commandline
  Gobra found no errors
  ```
  Next, we will change the file and verify it again. If you use a shared folder, then open `test_suite/evaluation/example-2-1.gobra` in the shared folder on your host with your favorite text editor and add `assert false` as the last statement in the body of function `client`. Without a shared folder, make the same change to the file `home/gobra/test_suite/evaluation/example-2-1.gobra` in the Docker container.
  The added assertion should trivially fail. We can check this by rerunning the same command as above where following output should appear:
  ```commandline
  Gobra has found 1 error(s):
  <27:3> Assert might fail.
  Assertion false might not hold
  ```
  Please consult Section ["Gobra Syntax"](#gobra-syntax) for information on how to change existing examples in a more meaningful way and how to write your own Gobra programs from scratch.

- In addition to single file verification, the container provides scripts to execute our regression test suite and the benchmarks:
  ```commandline
  /home/gobra/regressions.sh
  ```
  ```commandline
  /home/gobra/benchmark.sh
  ```
  Both scripts first print a short description of which files will be verified. Pressing enter will actually start the verifications. `ctrl + c` terminates the scripts. If you encounter any issues then consult the Section ["Troubleshooting"](#troubleshooting).
    
- `exit` terminates the Docker container.
  Please note that the shared folder remains on your host **but gets overwritten if the artifact image is started again**.



## Troubleshooting

### Permission error on `docker run`

If executing `docker run` with a mounted folder causes a permissions error, then use the following command instead:
```commandline
docker run -it --memory=6g --mount type=volume,dst=/home/gobra/sync,volume-driver=local,volume-opt=type=none,volume-opt=o=bind,volume-opt=device=$PWD/gobra_sync gobraverifier/gobra-artifact:v1
```
**Note: `$PWD/gobra_sync` has to already exist on the host.**


### Non-Termination and OutOfMemoryError
If you experience an `OutOfMemoryError` exception or the benchmark suite does not terminate, then increase the amount of RAM provided to the container by modifying the value of the `memory` parameter of the `docker run` command. For example, if you want to run the container with 10GB of RAM instead of 6, run Docker as follows:
```commandline
docker run -it --memory=10g --volume $PWD/gobra_sync:/home/gobra/sync gobraverifier/gobra-artifact:v1
```


### Strange Syntax Errors and Type Errors

We reimplemented the parser and type checker of Gobra. At this point in time, parser errors and type checker errors can be misleading. For instance, if an opening bracket `{` is not put on the same line as the function definition (which is mandatory in Go), then Gobra will output `'ghost' expected but '{' found`. As another example, if a call has an incorrect number of arguments, Gobra will complain that it expected the last parameter of the function to have a variadic type. If the parser fails, then often adding more parentheses can solve the problem. As a concrete example, `x.f.g()` should be written as `(x.f).g()`. For writing new examples, we recommend inspecting existing examples to see how a feature concretely works.



## Gobra Syntax

We have created a tutorial for Gobra introducing Gobra's syntax and features in several examples. The tutorial can be found in the Docker container (at `home/gobra/tutorial.md`), in the shared holder of the host, or online in our repository ([here](https://github.com/viperproject/gobra/blob/artifact1/docs/tutorial.md)).

There are several ways to exercise the tool on new inputs. One way of doing so is to seed errors, as we did for our evaluation: there, we deliberately broke loop invariants, postconditions, predicate definitions, and the code itself, to trigger verification failures. Such transformations can be applied on examples from the tutorial or the evaluation. These examples are at `home/gobra/test_suite/tutorial_examples` and `home/gobra/test_suite/evaluation` in the Docker container. If a shared folder is used, then you can modify the examples in `/test_suite/tutorial_examples` and `/test_suite/evaluation` in your shared folder on the host, but you will have to target the corresponding files in the Docker container, meaning a file in `home/gobra/sync/test_suite/tutorial_examples` or `home/gobra/sync/test_suite/evaluation`.

As an inspiration for errors, one can compare a correct version in our evaluation with one of its corresponding error seeded versions. A reliable way to seed errors is to weaken preconditions, loop invariants, or predicate bodies by removing conjuncts such that the verifier is missing information to deduce a property. Conversely, one can strengthen postconditions, loop invariants, or predicate bodies by adding conjuncts such that they are not entailed anymore.

Another, more ambitious, way to exercise the tool on new inputs is to take an example from the tutorial and to expand on it. As a very concrete example, one can take the `contains` function from the ghost code section and proof that if `false` is returned, then the integer argument is not contained in the slice. This requires strengthening the loop invariant. For the list examples of the predicate section, one can write additional functions on lists, for instance `get` or `append`. Another idea is to write new client code that calls functions from the tutorial. We recommend doing this in the files of the tutorial examples directly (the files in the `tutorial_examples` folder).



## Log Files

We have included two sets of log files in the Docker container. The folder `/home/gobra/test_suite/evaluation/log_files/paper` on the Docker container has the log files that we produced for the evaluation section of the paper. Please note that the names of some examples are slightly different and that not all measured examples were used in the paper. The folder `/home/gobra/test_suite/evaluation/log_files/docker` contains measurements that we have taken with the submitted Docker image on the same machine as the origin evaluation. There, we use the same names as in the paper.



## Relevant Source Code

Gobra consists of a parser, type checker, a transformation from parse AST to internal AST, an encoding into Viper, and a back translation of Viper errors. We believe that the encoding is the most interesting part of the code base. The encoding is split into several parts. The folder `/home/gobra/gobra/src/main/scala/viper/gobra/translator/encodings` contains the encodings of different types. For instance, `PointerEncoding` implements the encoding of pointer types. Many encodings are extensively documented with comments describing what is computed. We do not guarantee that all comments are completely accurate. The folder `/home/gobra/gobra/src/main/scala/viper/gobra/translator/implementations/components` contains the encoding of auxiliary data structures such as tuples, options, or arrays. Lastly, the folder `/home/gobra/gobra/src/main/scala/viper/gobra/translator/implementations/translator` contains the encoding of basic assertions and statements, which is not well documented.



## Build Artifact

The artifact image has been built with the Dockerfile included in this artifact.
It is located at `/home/gobra/Dockerfile` or can be found in the shared folder on the host when following the instructions given in section ["Running Gobra"](#running-gobra). The same Dockerfile can also be found in [our repository](https://github.com/viperproject/gobra/tree/artifact1/artifact).
Because additional files are included in the artifact image (e.g. this README, the paper, and some shell scripts), the following command has to be executed in the `artifact` folder in our repository:
```commandline
docker build -t gobraverifier/gobra-artifact:v1 .
```
Building the artifact image and executing the instructions given in the first section are performed and tested as part of our continuous integration to ensure that Gobra remains reusable by the community in the future.

Our GitHub repository also has instructions on how to install Gobra from scratch without using Docker. These instructions can be found [here](https://github.com/viperproject/gobra/blob/artifact1/README.md).



## License

Gobra and its sources are licensed under the [Mozilla Public License Version 2.0](http://www.mozilla.org/MPL/2.0/) with the following exceptions:
- The `.gobra` files used for regression testing are in [Public Domain](http://creativecommons.org/publicdomain/zero/1.0/)
- Certain files containing stubs for Go built-ins or the Go standard library (located in `/home/gobra/gobra/src/main/resources/builtin` and `/home/gobra/gobra/src/main/resources/stubs/sync`, respectively) are copyrighted by the Go Authors ([license](https://golang.org/LICENSE)) because these files are largely inspired by their counterparts in the Go source code.

