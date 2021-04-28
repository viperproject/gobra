# Building & Using Artifact Container

The `Dockerfile` in this directory creates a ready-to-use Docker container with compiled versions of Gobra.
The full steps are as follows:
1. Executing the following command in the `artifact` directory of this repository (i.e. the directory in which this README is located) builds the image:
    ```
    docker build -t gobraverifier/gobra-artifact:v1 .
    ```
   This downloads the specified versions of Gobra, Silver, Silicon, and Carbon into the image and compiles them to `/home/gobra/gobra.jar` and `/home/gobra/gobra-test.jar` inside the docker image.
   `gobra.jar` just contains an executable version of Gobra whereas `gobra-test.jar` also includes the test classes. 
2. Start the container:
    - The following command starts an interactive shell in the container:
        ```
        docker run -it gobraverifier/gobra-artifact:v1
        ```
    - If you want to share a folder between your host and the container, use the following command that creates a new folder in the current directory on your host and mounts it to the container (it will be mounted at `/home/gobra/sync` inside the docker container):
        ```
        docker run -it --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1
        ```
      This command only works on macOS and linux and uses the current time to construct a folder in the current
      directory with a kind-of-unique folder name.
      Instead, a folder can also be manually created and used in this command. Note however that an absolute path to the
      created folder has to be used.
      The command using an existing empty folder would look as follows:
      ```commandline
      docker run -it --volume <absolute path to empty folder>:/home/gobra/sync gobraverifier/gobra-artifact:v1
      ```
    - Alternatively, Gobra can be run in preconfigured ways:
        - Verify `example-2-1.gobra` (the file has to be accessible from within the container):
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1 /bin/bash ./gobra.sh ./test_suite/evaluation/example-2-1.gobra
            ```
        - Run the regression test suite:
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1 /bin/bash ./regressions.sh
            ```
        - Benchmark Gobra by verifying each file in the benchmark suite 5 times:
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync --env GOBRATESTS_REPETITIONS=5 gobraverifier/gobra-artifact:v1 /bin/bash ./benchmark.sh
            ```
Note that the non-interactive docker commands will automatically return after executing the specified command.
In the interactive sessions, `exit` can be used in the container to exit and stop the container.

In case of permission errors while using `--volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync` the following command line option can be used instead. Note however that `$PWD/sync` has to already exist on the host:
```
--mount type=volume,dst=/home/gobra/sync,volume-driver=local,volume-opt=type=none,volume-opt=o=bind,volume-opt=device=$PWD/sync
```
