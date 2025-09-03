import matplotlib.pyplot as plt
import numpy as np
import os

csv_files = [f for f in os.listdir(os.getcwd()) if f.endswith(".csv")]

def get_pretty_name(name: str):
  return name.replace(".csv", ".gobra")

def import_result(file: str):
  with open(file, mode="r") as f:
    contents = f.readlines()
  results = []
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
      results.append(curr_dict)
  return results


def plot_result(ax, result_file, x_key, marker):
  test_results = import_result(result_file)
  xs= []
  ys = []
  errs = []
  for rs in test_results:
    xs.append(rs[x_key])
    ys.append(np.sum(rs['runtimes']))
    errs.append(np.std(rs['runtimes']))

  ax.errorbar(xs, ys, yerr=errs, fmt=marker, linestyle='none', capsize=4, label=get_pretty_name(result_file))


def plot_absolute(result_files: list[str], x_key: str):
  fig, ax = plt.subplots(figsize=(12, 6))
  markers = ['o', 's', '^', 'D']

  for i, file in enumerate(result_files):
    plot_result(ax, file, x_key, markers[i%len(markers)])

  # finish plot
  ax.set_ylabel("runtime [ms]")
  ax.set_xlabel("#" + x_key)
  ax.set_title("Runtimes of Dependency Set Queries")
  ax.legend()

  plt.tight_layout()
  plt.savefig("results_by_" + x_key + ".png")


print(csv_files)

for x_key in ['deps', 'lowLevelDeps']:
  plot_absolute(csv_files, x_key)