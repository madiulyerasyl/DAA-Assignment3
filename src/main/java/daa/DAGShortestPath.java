package daa;

import java.util.ArrayList;
import java.util.Collections;

public class DAGShortestPath {

    public static final long INF = Long.MAX_VALUE / 4;

    public static class Result {
        public long[] dist;
        public int[] parent;
        public long edgeScans;
        public long comparisons;
        public long sumDistances;
    }

    public static Result shortestPaths(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "DAG shortest paths requires a directed graph");
        }

        Kahn.Result topo = Kahn.topologicalSort(graph);

        if (topo.hasCycle) {
            throw new IllegalArgumentException(
                    "Graph contains a cycle");
        }

        int n = graph.size();

        Result result = new Result();
        result.dist = new long[n];
        result.parent = new int[n];

        int[] indegree = new int[n];

        for (Graph.WeightedEdge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        for (int i = 0; i < n; i++) {
            result.dist[i] = INF;
            result.parent[i] = -1;

            if (indegree[i] == 0) {
                result.dist[i] = 0;
            }
        }

        // Relax edges in topological order
        for (int vertex : topo.order) {

            for (Graph.Edge edge : graph.neighbors(vertex)) {

                result.edgeScans++;

                if (result.dist[vertex] != INF) {

                    long newDistance =
                            result.dist[vertex] + edge.weight;

                    result.comparisons++;

                    if (newDistance < result.dist[edge.to]) {
                        result.dist[edge.to] = newDistance;
                        result.parent[edge.to] = vertex;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (result.dist[i] != INF) {
                result.sumDistances += result.dist[i];
            }
        }

        return result;
    }

    public static ArrayList<Integer> getPath(
            Result result, int target) {

        ArrayList<Integer> path = new ArrayList<>();

        if (result.dist[target] == INF) {
            return path;
        }

        int current = target;

        while (current != -1) {
            path.add(current);
            current = result.parent[current];
        }

        Collections.reverse(path);

        return path;
    }
}