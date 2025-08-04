import matplotlib.pyplot as plt
import numpy as np

def import_result(file: str):
  with open(file, mode="r") as f:
    contents = f.readlines()
  header = [c.strip() for c in contents[0].split(",")]
  results = {}
  for line in contents[1:]:
      parts = line.split(',')
      test_name = parts[0].strip()
      runtimes = [int(x.strip()) for x in parts[1:]]
      results[test_name] = runtimes
  return header[1:], results

def plot_result(ax, test_names, result_file, idx, x, width):
  h, test_results = import_result(result_file)
  values = []
  for name in test_names:
    values.append(test_results[name][idx])

  ax.bar(x, values, width, label=result_file)


def plot_absolute(result_files: list[str], idx: int):
  width = 0.8/len(result_files)
  print(width)
  fig, ax = plt.subplots(figsize=(12, 6))

  # get test names from first result file
  h, res = import_result(result_files[0])
  names = res.keys()
  x = np.arange(len(names))
  
  # plot results from each file
  for i, file in enumerate(result_files):
    plot_result(ax, names, file, idx, x + (-0.4 + (i+0.5)*width), width)

  # finish plot
  ax.set_ylabel(h[idx])
  ax.set_title(h[idx])
  ax.set_xticks(x)
  ax.set_xticklabels(names, rotation=90)
  ax.legend()

  plt.tight_layout()
  plt.savefig(h[idx].replace("[ \t]", "_") + ".png")

input_files = input("Input file: ").split(",")

h, res = import_result(input_files[0])
for idx, _ in enumerate(h):
  plot_absolute(input_files, idx)