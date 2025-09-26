# Dependency Analysis Benchmarks

Benchmark programs are provided in `benchmark_programs`.
To verify them with dependency analysis enabled, use configuration `--disablePureFunctsTerminationRequirement --enableAssumptionAnalysis --disableInfeasibilityChecks`.
The command-line tool needs to be enabled separately using `--startAssumptionAnalysisTool`.

## Precision Benchmark

The determined ground truth of each benchmarked assertions is provided as a Gobra program in `precision_ground_truth`.
The reported dependencies have been extracted using dependency query (`dep`) in the command-line tool.


## Performance Benchmark

Impact on verification time has been measured using the benchmark script `assumptionAnalysisBenchmark.sh`.
Execute it using `sh src/test/scala/viper/gobra/assumptionAnalysisBenchmark.sh`.
The results are written to CSV files in the folder `performance_benchmark`.

The benchmark scripts for the VerifiedSCION performance benchmark are provided in `scion-benchmarks`.
In `scion-benchmark.sh`, `[YOUR_PATH_TO_VERIFIED_SCION]` needs to be replaced by the absolute path to VerifiedSCION (e.g., `C:/dev/dependencyAnalysis/VerifiedSCION`) corresponding to the repository cloned from [https://github.com/viperproject/VerifiedSCION/tree/master](https://github.com/viperproject/VerifiedSCION/tree/master).

The query response times are measured in the command-line tool.
To start the benchmark, use the hidden command `benchmark` and enter the desired name of the output file.
Now, you can repeatedly enter line numbers. 
For each one, the dependency query (`dep`) is executed several times and the average response times is reported.
The results are also written to the output file.

Folders `performance_benchmark`, `scion-benchmarks`, and `query_performance_results` contain the results obtained from the corresponding benchmarks and a `plotter.py` script to visualize them.


## Proof Coverage Benchmark

The determined ground truth of each benchmark function is provided as a Gobra program in `proof_coverage_ground_truth`.
The reported proof coverage has been extracted using proof coverage query (`cov`) in the command-line tool.

## Case Study on Analyzing the Impact of Bugs 

The Gobra program (Binary Search Tree with a bug in the delete function) is provided in `benchmark_programs/binary_search_tree_with_bug.gobra`.