package daa;

import java.util.Random;

public class GraphGenerator {

    private static final int SEED = 42;

    private static void shuffle(int[] array, Random random) {

        for (int i = array.length - 1; i > 0; i--) {

            int j = random.nextInt(i + 1);

            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // Generate a connected undirected graph
    public static Graph undirectedGraph(int n, int m) {

        if (n < 1 || m < n - 1) {
            throw new IllegalArgumentException(
                    "Invalid n or m for connected graph");
        }

        Random random = new Random(SEED);
        Graph graph = new Graph(n, false);

        int[] permutation = new int[n];

        for (int i = 0; i < n; i++) {
            permutation[i] = i;
        }

        shuffle(permutation, random);

        // First create a spanning tree
        for (int i = 1; i < n; i++) {

            int u = permutation[random.nextInt(i)];
            int v = permutation[i];

            int weight = 1 + random.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        // Add remaining random edges
        for (int i = n - 1; i < m; i++) {

            int u = random.nextInt(n);
            int v = random.nextInt(n);

            while (u == v) {
                v = random.nextInt(n);
            }

            int weight = 1 + random.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        return graph;
    }

    // Generate a directed graph for SCC
    public static Graph directedGraph(int n, int m) {

        if (n < 2 || m < 0) {
            throw new IllegalArgumentException("Invalid n or m");
        }

        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);

        for (int i = 0; i < m; i++) {

            int u = random.nextInt(n);
            int v = random.nextInt(n);

            while (u == v) {
                v = random.nextInt(n);
            }

            int weight = 1 + random.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        return graph;
    }

    // Generate a Directed Acyclic Graph
    public static Graph dagGraph(int n, int m) {

        if (n < 2 || m < 0) {
            throw new IllegalArgumentException("Invalid n or m");
        }

        Random random = new Random(SEED);
        Graph graph = new Graph(n, true);

        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            rank[i] = i;
        }

        shuffle(rank, random);

        for (int i = 0; i < m; i++) {

            int u = random.nextInt(n);
            int v = random.nextInt(n);

            while (u == v) {
                v = random.nextInt(n);
            }

            if (rank[u] > rank[v]) {

                int temp = u;
                u = v;
                v = temp;
            }

            int weight = 1 + random.nextInt(1000);

            graph.addEdge(u, v, weight);
        }

        return graph;
    }
}