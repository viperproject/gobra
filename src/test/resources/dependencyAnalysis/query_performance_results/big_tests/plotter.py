import matplotlib.pyplot as plt
import numpy as np
import os

csv_files = [f for f in os.listdir(os.getcwd()) if f.endswith(".csv")]
colors = ["#4e6ea7", "#6aa84f", "#cc4125", "#c27ba0"]


def get_pretty_name(name: str):
  names = {"binary_search_tree.gobra": "Binary Search Tree",
           "binary_tree.gobra": "Binary Tree",
           "dutchflag.gobra": "Dutchflag",
           "example-2-1.gobra": "Example 2.1",
           "zune.gobra": "Zune",
           "relaxed_prefix.gobra": "Relaxed Prefix",
           "scion_addr.gobra": "VerifiedSCION addr package"}
  return names[name.replace(".csv", ".gobra")]

def import_result(file: str):
  with open(file, mode="r") as f:
    contents = f.readlines()
  results = []
  print("\multicolumn{3}{|l|}{" + get_pretty_name(file).replace("_", "\_") + "}&&\\\\")
  for line in contents[1:]:
      parts = line.split(',')
      line = parts[0].strip()
      lowLevelDeps = int(parts[1].strip())
      deps = int(parts[2].strip())
      runtimes = [float(x.strip()) for x in parts[3:]]
      curr_dict = {}
      curr_dict['line'] = line
      curr_dict['lowLevelDeps'] = lowLevelDeps
      curr_dict['deps'] = deps
      curr_dict['runtimes'] = runtimes
      print(f"{line} & {lowLevelDeps} & {deps} & {np.mean(runtimes):.1f} & {np.std(runtimes):.1f}\\\\")
      results.append(curr_dict)
  print("\hline\hline")
  return results


def plot_result(ax, result_file, x_key, marker, color):
  test_results = import_result(result_file)
  xs= []
  ys = []
  errs = []
  for rs in test_results:
    xs_curr = rs[x_key]
    xs.append(xs_curr)
    curr_runtime = np.mean(rs['runtimes'])
    ys.append(curr_runtime)
    curr_std = np.std(rs['runtimes'])
    errs.append(curr_std)

  ax.errorbar(xs, ys, yerr=errs, fmt=marker, color=color, linestyle='none', capsize=3, label=get_pretty_name(result_file))


def plot_absolute(result_files: list[str], x_key: str):
  fig, ax = plt.subplots(figsize=(12, 6))
  markers = ['o', 's', '^', 'D']

  print("\\textbf{queried line} & \\textbf{\\#low-level deps} & \\textbf{\\#Gobra deps} & \\textbf{mean [ms]} & \\textbf{std}\\\\\\hline")

  for i, file in enumerate(result_files):
    plot_result(ax, file, x_key, markers[i%len(markers)], color=colors[i])

  # finish plot
  ax.set_ylabel("Query Runtime [ms]")
  ax.set_xlabel("#Dependencies")
  # ax.set_title("Runtimes of Dependency Set Queries")
  ax.legend()

  plt.tight_layout()
  plt.savefig("results_by_" + x_key + ".png")


print(csv_files)

for x_key in ['deps', 'lowLevelDeps']:
  plot_absolute(csv_files, x_key)