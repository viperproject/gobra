# Building & Using Artifact Container

The `Dockerfile` in this directory creates a ready-to-use Docker container with compiled versions of Gobra.
The full steps are as follows:
1. Build the image:
    ```
    docker build -t gobraverifier/gobra-artifact:v1 .
    ```
   This downloads the specified versions of Gobra, Silver, Silicon, and Carbon into the image and compiles them to `/home/gobra/gobra.jar` and `/home/gobra/gobra-test.jar`.
   `gobra.jar` just contains an executable version of Gobra whereas `gobra-test.jar` also includes the test classes. 
2. Start the container:
    - The following command starts an interactive shell in the container:
        ```
        docker run -it gobraverifier/gobra-artifact:v1
        ```
    - If you want to share a folder between your host and the container, use the following command that creates a new folder in the current directory on your host and mounts it to the container (it will be mounted at `/home/gobra/sync`):
        ```
        docker run -it --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1
        ```
    - Alternatively, Gobra can be run in preconfigured ways:
        - Verify `example-2-1.gobra` (the file has to be accessible from within the container):
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1 /bin/bash ./gobra.sh ./test_suite/regressions/examples/example-2-1.gobra
            ```
        - Run the regression test suite:
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync gobraverifier/gobra-artifact:v1 /bin/bash ./regressions.sh
            ```
        - Benchmark Gobra by verifying each file in the benchmark suite 5 times:
            ```
            docker run --volume $PWD/sync_$(date +%Y-%m-%d_%H-%M):/home/gobra/sync --env GOBRATESTS_REPETITIONS=5 gobraverifier/gobra-artifact:v1 /bin/bash ./benchmark.sh
            ```
Note that the non-interactive docker commands will automatically return after executing the specified commands.
In the interactive sessions, `exit` can be used in the container to exit and stop the container.
