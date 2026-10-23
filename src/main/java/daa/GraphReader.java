package daa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphReader {

    public static Graph readGraph(String filename)
            throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(filename));

        try {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            String type = scanner.next();

            boolean directed;

            if (type.equals("directed")) {
                directed = true;
            } else if (type.equals("undirected")) {
                directed = false;
            } else {
                throw new IllegalArgumentException(
                        "Unknown graph type: " + type);
            }

            Graph graph = new Graph(n, directed);

            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int weight = scanner.nextInt();

                graph.addEdge(u, v, weight);
            }

            return graph;

        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args)
            throws FileNotFoundException {

        // Test 1: Disconnected graph
        Graph disconnected = readGraph("data/disconnected.txt");

        Kruskal.Result mst = Kruskal.mst(disconnected);

        System.out.println("DISCONNECTED GRAPH");
        System.out.println("Vertices: " + disconnected.size());
        System.out.println("Edges: " + disconnected.getEdges().size());
        System.out.println("Components: " + mst.components);
        System.out.println("Forest weight: " + mst.totalWeight);

        System.out.println();

        // Test 2: Directed graph with cycles
        Graph cyclic = readGraph("data/cyclic.txt");

        Kosaraju.Result scc = Kosaraju.scc(cyclic);
        Kahn.Result topo = Kahn.topologicalSort(cyclic);

        System.out.println("CYCLIC GRAPH");
        System.out.println("Vertices: " + cyclic.size());
        System.out.println("Edges: " + cyclic.getEdges().size());
        System.out.println("SCC count: " + scc.components);
        System.out.println("Has cycle: " + topo.hasCycle);
    }
}