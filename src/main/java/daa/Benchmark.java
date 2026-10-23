package daa;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;

public class Benchmark {

    private static final int[] SIZES = {
            1000, 3162, 10000, 31623, 100000
    };

    private static final int[] MULTIPLIERS = {4, 20};

    private static final int RUNS = 5;

    public static void main(String[] args) throws Exception {

        File output = new File("results.csv");

        try (PrintWriter writer = new PrintWriter(output)) {

            writer.println(
                    "algorithm,graph,n,m,time_ms,dsu_ops," +
                            "edge_scans,comparisons,max_depth,result"
            );

            for (int n : SIZES) {
                for (int multiplier : MULTIPLIERS) {

                    int m = n * multiplier;

                    System.out.println(
                            "Running n=" + n + ", m=" + m
                    );

                    Graph undirected =
                            GraphGenerator.undirectedGraph(n, m);

                    Graph directed =
                            GraphGenerator.directedGraph(n, m);

                    Graph dag =
                            GraphGenerator.dagGraph(n, m);

                    runKruskal(writer, undirected, n, m);
                    runPrim(writer, undirected, n, m);
                    runKosaraju(writer, directed, n, m);
                    runKahn(writer, dag, n, m);
                    runDAG(writer, dag, n, m);

                    writer.flush();

                    System.out.println("Completed!");
                }
            }
        }

        System.out.println("Saved to results.csv");
    }

    private static double median(long[] times) {

        Arrays.sort(times);

        return times[times.length / 2] / 1_000_000.0;
    }

    private static void writeRow(
            PrintWriter writer,
            String algorithm,
            String graph,
            int n,
            int m,
            double time,
            long dsuOps,
            long edgeScans,
            long comparisons,
            long maxDepth,
            long result) {

        writer.printf(
                Locale.US,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                algorithm, graph, n, m, time,
                dsuOps, edgeScans, comparisons,
                maxDepth, result
        );
    }

    private static void runKruskal(
            PrintWriter writer, Graph graph, int n, int m) {

        long[] times = new long[RUNS];
        Kruskal.Result result = null;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            result = Kruskal.mst(graph);

            times[i] = System.nanoTime() - start;
        }

        writeRow(writer, "Kruskal", "undirected",
                n, m, median(times),
                result.dsuOps, result.edgeScans,
                result.comparisons, 0,
                result.totalWeight);
    }

    private static void runPrim(
            PrintWriter writer, Graph graph, int n, int m) {

        long[] times = new long[RUNS];
        Prim.Result result = null;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            result = Prim.mst(graph);

            times[i] = System.nanoTime() - start;
        }

        writeRow(writer, "Prim", "undirected",
                n, m, median(times),
                0, result.edgeScans,
                result.comparisons, 0,
                result.totalWeight);
    }

    private static void runKosaraju(
            PrintWriter writer, Graph graph, int n, int m) {

        long[] times = new long[RUNS];
        Kosaraju.Result result = null;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            result = Kosaraju.scc(graph);

            times[i] = System.nanoTime() - start;
        }

        writeRow(writer, "Kosaraju", "directed",
                n, m, median(times),
                0, result.edgeScans,
                0, result.maxDepth,
                result.components);
    }

    private static void runKahn(
            PrintWriter writer, Graph graph, int n, int m) {

        long[] times = new long[RUNS];
        Kahn.Result result = null;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            result = Kahn.topologicalSort(graph);

            times[i] = System.nanoTime() - start;
        }

        writeRow(writer, "Kahn", "DAG",
                n, m, median(times),
                0, result.edgeScans,
                0, 0,
                result.order.size());
    }

    private static void runDAG(
            PrintWriter writer, Graph graph, int n, int m) {

        long[] times = new long[RUNS];
        DAGShortestPath.Result result = null;

        for (int i = 0; i < RUNS; i++) {

            long start = System.nanoTime();

            result = DAGShortestPath.shortestPaths(graph);

            times[i] = System.nanoTime() - start;
        }

        writeRow(writer, "DAGShortestPath", "DAG",
                n, m, median(times),
                0, result.edgeScans,
                result.comparisons, 0,
                result.sumDistances);
    }
}