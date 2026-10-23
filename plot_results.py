import os
import pandas as pd
import matplotlib.pyplot as plt

# Read benchmark results
df = pd.read_csv("results.csv")

# Create folder for graphs
os.makedirs("plots", exist_ok=True)

# Graph 1: Operations vs n (log-log)
plt.figure(figsize=(9, 6))

for algorithm in df["algorithm"].unique():
    data = df[
        (df["algorithm"] == algorithm) &
        (df["m"] == df["n"] * 4)
        ].sort_values("n")

    if algorithm == "Kruskal":
        operations = data["dsu_ops"] + data["comparisons"]
    elif algorithm == "Prim":
        operations = data["comparisons"]
    else:
        operations = data["edge_scans"]

    plt.plot(
        data["n"],
        operations,
        marker="o",
        label=algorithm
    )

plt.xscale("log")
plt.yscale("log")
plt.xlabel("Number of vertices (n)")
plt.ylabel("Operations")
plt.title("Operations vs n (log-log), m = 4n")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("plots/operations_vs_n.png", dpi=200)
plt.close()

# Graph 2: Edge scans per edge
plt.figure(figsize=(9, 6))

for algorithm in df["algorithm"].unique():
    data = df[
        (df["m"] == df["n"] * 4) &
        (df["algorithm"] == algorithm)
        ].sort_values("n")

    ratio = data["edge_scans"] / data["m"]

    plt.plot(
        data["n"],
        ratio,
        marker="o",
        label=algorithm
    )

plt.xlabel("Number of vertices (n)")
plt.ylabel("Edge scans / m")
plt.title("Edge scans per edge, m = 4n")
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("plots/edge_scans_per_edge.png", dpi=200)
plt.close()

# Graph 3: Kruskal examined edge share
kruskal = df[df["algorithm"] == "Kruskal"].copy()
kruskal["density"] = kruskal["m"] / kruskal["n"]

plt.figure(figsize=(9, 6))

for density in [4, 20]:
    data = kruskal[
        kruskal["density"] == density
        ].sort_values("n")

    share = data["edge_scans"] / data["m"]

    plt.plot(
        data["n"],
        share,
        marker="o",
        label=f"m = {density}n"
    )

plt.xlabel("Number of vertices (n)")
plt.ylabel("Examined edges / m")
plt.title("Kruskal examined edge share")
plt.ylim(0, 1.1)
plt.legend()
plt.grid(True)
plt.tight_layout()
plt.savefig("plots/kruskal_edge_share.png", dpi=200)
plt.close()

print("All 3 graphs created successfully!")