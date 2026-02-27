import matplotlib.pyplot as plt
import numpy as np

def get_pretty_name(name: str):
  return name.replace("_", " ").replace("data", "").replace(".csv", "")

def import_result(file: str) -> dict:
  with open(file, mode="r") as f:
    contents = f.readlines()
  results = {}
  for line in contents[1:]:
      parts = line.split(',')
      test_name = parts[0].strip()
      runtimes = [int(x.strip()) for x in parts[1:]]
      results[test_name] = runtimes
  return results

def plot_result_with_err(ax, result_file, x, width):
  test_results = import_result(result_file)
  values = []
  stdDevs = []
  for name in test_results.keys():
    values.append(np.mean(test_results[name]))
    stdDevs.append(np.std(test_results[name]))
    
  print(values)
  ax.bar(x, values, width, yerr=stdDevs, label=get_pretty_name(result_file))


def plot_absolute(result_files: list[str], output_file: str):
  width = 0.8/len(result_files)
  print(width)
  fig, ax = plt.subplots(figsize=(12, 6))

  # get test names from first result file
  res = import_result(result_files[0])
  names = res.keys()
  x = np.arange(len(names))
  
  # plot results from each file
  for i, file in enumerate(result_files):
    plot_result_with_err(ax, file, x + (-0.4 + (i+0.5)*width), width)

  # finish plot
  ax.set_ylabel("Runtime [s]")
#   ax.set_title("Verification Runtime")
  ax.set_xticks(x)
  ax.set_xticklabels(names, rotation=90)
  # ax.legend()

  plt.tight_layout()
  plt.savefig(output_file)

input_files = input("Input file: ").split(",")
output_file = input("Output file: ")

plot_absolute(input_files, output_file)