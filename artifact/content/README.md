# Gobra: Modular Specification and Verification of Go Programs

## Running Gobra
Gobra comes as a ready-to-use docker image called `gobra_docker_image.tar` (TODO: add link to Zenodo).
It can be loaded into docker by using `docker load -i gobra_docker_image.tar`.
The loaded image is named `gobraverifier/gobra-artifact:v1`.

Docker should be configured that at least 6GB of memory is available to containers.
This can be configured in Docker Preferences (use the whale icon in the status bar) > Resources > Advanced > Memory.

We recommend to use a shared folder to directly access files provided by the image on your host:
- Create a new folder in your current directory.
  Let us assume that it is called `gobra_sync` for the following commands.

- Start an interactive version of the Gobra artifact using the following command:
  ```commandline
  docker run -it --memory=6g --volume $PWD/gobra_sync:/home/gobra/sync gobraverifier/gobra-artifact:v1
  ```
  You can now find this README, our paper, the Dockerfile used to create the artifact image, and our evaluation and regression examples in the shared folder `gobra_sync`.
  
  - As an example, let us verify `test_suite/evaluation/example-2-1.gobra` in the shared folder by running the following command in the Docker container:
    ```commandline
    /home/gobra/gobra.sh /home/gobra/sync/test_suite/evaluation/example-2-1.gobra
    ```
    If you do not have performed any changes, the following output should appear:
    ```commandline
    Gobra found no errors
    ```
    As a next step, open `test_suite/evaluation/example-2-1.gobra` in the shared folder on your host in your favorite text editor and add `assert false` as the last statement in the body of function `client`.
    This statement should trivially fail. We can check this by rerunning the same command as above and the following output should appear:
    ```commandline
    Gobra has found 1 error(s):
    <27:3> Assert might fail. 
    Assertion false might not hold
    ```
    The next section details syntax supported by Gobra to change existing examples in a more meaningful way or write your own Gobra programs from scratch. 

  - In addition to single file verification, the container provides scripts to execute our regression test suite and the benchmarks:
    ```commandline
    /home/gobra/regressions.sh
    ```
    ```commandline
    /home/gobra/benchmark.sh
    ```
    Both scripts first print a short description of which files will be verified.
    Pressing enter will actually start the verifications.
    `ctrl + c` terminates the scripts.
    
  - `exit` terminates the docker container.
    Please note that the shared folder remains on your host but gets overwritten if the artifact image will be started again.

## Gobra Syntax

## Relevant Source Code

## Build Artifact
The artifact image has been built with the Dockerfile included in the artifact.
It is located at `/home/gobra/Dockerfile` or can be found at the root level in the shared folder when following the instructions given in [Running Gobra](#running-gobra).
The same Dockerfile can also be found in [our repository](https://github.com/viperproject/gobra/tree/master/artifact).
Because additional files are included in the artifact image (e.g. this README, the paper or some shell scripts), the following command should be executed in the `artifact` folder in our repository:
```commandline
docker build -t gobraverifier/gobra-artifact:v1 .
```
Note that building the artifact image and executing the instructions given in the first section are performed and tested as part of our continuous integration to ensure that Gobra remains reusable by the community in the future.
The CI status can be checked [here](https://github.com/viperproject/gobra/actions?query=workflow%3Aartifact+branch%3Amaster).
