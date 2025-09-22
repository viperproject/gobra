import matplotlib.pyplot as plt
import numpy as np

colors = ["#4e6ea7", "#6aa84f", "#cc4125", "#c27ba0"]

def get_pretty_name(name: str):
  return name.replace("data_", "").replace("_", " ").replace(".csv", "").capitalize()

def get_gobra_program_names(name: str):
  names = {"binary_search_tree.gobra": "Binary Search Tree",
           "binary_tree.gobra": "Binary Tree",
           "dutchflag.gobra": "Dutchflag",
           "example-2-1.gobra": "Example 2.1",
           "zune.gobra": "Zune",
           "relaxed_prefix.gobra": "Relaxed Prefix"}
  return names[name]

def import_result(file: str):
  with open(file, mode="r") as f:
    contents = f.readlines()
  header = [c.strip() for c in contents[0].split(",")]
  results = {}
  for line in contents[1:]:
      parts = line.split(',')
      test_name = parts[0].strip()
      runtimes = [float(x.strip())/1000.0 for x in parts[1:]]
      results[test_name] = runtimes
  return header[1:], results

def plot_result_with_err(ax, test_names, result_file, idx, x, width, color):
  h, test_results = import_result(result_file)
  stdDevIdx = h.index("StdDev [ms]")
  values = []
  stdDevs = []
  print(get_pretty_name(result_file).capitalize())
  for name in test_names:
    print(f"{get_gobra_program_names(name)} &  {test_results[name][idx]:.2f} \\\\\\hline")
    values.append(test_results[name][idx])
    stdDevs.append(test_results[name][stdDevIdx])

  ax.bar(x, values, width, yerr=stdDevs, capsize=3, label=get_pretty_name(result_file).capitalize(), color=color)

def plot_result(ax, test_names, result_file, idx, x, width, color):
  h, test_results = import_result(result_file)
  values = []
  for name in test_names:
    values.append(test_results[name][idx])

  ax.bar(x, values, width, label=get_pretty_name(result_file), color=color)


def plot_absolute(result_files: list[str], idx: int, withErr=False):
  width = 0.8/len(result_files)
  print(width)
  fig, ax = plt.subplots(figsize=(12, 6))

  # get test names from first result file
  h, res = import_result(result_files[0])
  names = res.keys()
  x = np.arange(len(names))
  
  # plot results from each file
  for i, file in enumerate(result_files):
    if withErr:
      plot_result_with_err(ax, names, file, idx, x + (-0.4 + (i+0.5)*width), width, color=colors[i])
    else:
      plot_result(ax, names, file, idx, x + (-0.4 + (i+0.5)*width), width, color=colors[i])

  # finish plot
  ax.set_ylabel(h[idx].replace("Mean [ms]", "Mean Verification Time [s]").replace("[ms]", "[s]"))
  # ax.set_title("Verification Times")
  ax.set_xticks(x)
  ax.set_xticklabels([get_gobra_program_names(n) for n in names])
  ax.legend()

  plt.tight_layout()
  plt.savefig(h[idx].replace("[ \t]", "_") + ".png")

# input_files = input("Input file: ").split(",")
input_files = ["data_baseline.csv", "data_analysis.csv", "data_analysis_with_infeas_checks.csv"]

h, res = import_result(input_files[0])
plot_absolute(input_files, 1, True)
# for idx, _ in enumerate(h):
  # plot_absolute(input_files, idx, True)