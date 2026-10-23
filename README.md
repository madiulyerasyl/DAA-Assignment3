# DAA Assignment 3 — Graph Algorithms

**Student:** Yerassyl Madiuly  
**Group:** SE2525  
**Course:** Design and Analysis of Algorithms

## Project Description

This project contains implementations of graph algorithms in Java. The main purpose is to test their correctness and compare their performance on different graph sizes.

## Implemented Algorithms

- Disjoint Set Union (DSU) with path compression and union by rank
- Kruskal's Minimum Spanning Tree
- Prim's Minimum Spanning Tree
- Kosaraju's Strongly Connected Components
- Kahn's Topological Sorting
- DAG Shortest Paths with negative edge weight support

## Technologies

- Java 25
- Maven
- JUnit 5
- Python
- pandas, NumPy, matplotlib

## How to Run

### Run Tests

Open the project in IntelliJ IDEA or use:

```bash
mvn test
```

### Run Benchmark

```bash
mvn compile
mvn exec:java -Dexec.mainClass=daa.Benchmark
```

If the Maven Exec plugin is not configured, run `Benchmark.main()` directly from IntelliJ IDEA with VM option `-Xmx4g`.

The benchmark generates `results.csv`.

### Generate Plots

```bash
python plot_results.py
```

This creates three graphs in the `plots` folder.

### Calculate Slopes

```bash
python calculate_slopes.py
```

## Benchmark

The benchmark uses five graph sizes and two edge densities:

- n = 1000, 3162, 10000, 31623, 100000
- m = 4n and m = 20n

Each algorithm runs five times. The median execution time is recorded.

The graph generator uses `Random(42)` for reproducible results.

## Project Files

- `src/main/java/daa/` — graph algorithms and benchmark
- `src/test/java/daa/` — JUnit tests
- `results.csv` — benchmark results
- `plots/` — benchmark graphs
- `REPORT.md` — explanation and performance analysis
- `plot_results.py` — generates graphs
- `calculate_slopes.py` — calculates log-log slopes

## AI Usage Acknowledgment

I used ChatGPT as a learning assistant during this assignment. It helped me understand graph algorithms, develop code examples, prepare tests, and organize the report. I ran the implementations and tests in IntelliJ IDEA and checked the benchmark results against the assignment reference values.

## Conclusion

This project helped me understand graph algorithms, their correctness, and their time complexity. The benchmark results demonstrate how different algorithms behave as graph size increases.