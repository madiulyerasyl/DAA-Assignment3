import pandas as pd
import numpy as np

df = pd.read_csv("results.csv")

print("LOG-LOG SLOPE ANALYSIS")
print("Graph density: m = 4n")
print("-" * 45)

for algorithm in df["algorithm"].unique():

    data = df[
        (df["algorithm"] == algorithm) &
        (df["m"] == df["n"] * 4)
        ].sort_values("n")

    n = data["n"].to_numpy()

    if algorithm == "Kruskal":
        operations = (
                data["dsu_ops"] + data["comparisons"]
        ).to_numpy()

    elif algorithm == "Prim":
        operations = data["comparisons"].to_numpy()

    else:
        operations = data["edge_scans"].to_numpy()

    # Fit a straight line to log(n) and log(operations)
    slope = np.polyfit(
        np.log(n),
        np.log(operations),
        1
    )[0]

    print(f"{algorithm}: slope = {slope:.3f}")