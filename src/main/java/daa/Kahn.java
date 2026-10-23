package daa;

import java.util.ArrayList;
import java.util.ArrayDeque;

public class Kahn {

    public static class Result {
        public ArrayList<Integer> order = new ArrayList<>();
        public boolean hasCycle;
        public long edgeScans;
    }

    public static Result topologicalSort(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kahn requires a directed graph");
        }

        int n = graph.size();
        int[] indegree = new int[n];

        Result result = new Result();

        // Calculate indegree of every vertex
        for (Graph.WeightedEdge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        // Add vertices with indegree 0
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {

            int vertex = queue.poll();
            result.order.add(vertex);

            for (Graph.Edge edge : graph.neighbors(vertex)) {

                result.edgeScans++;

                indegree[edge.to]--;

                if (indegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        // A cycle exists if not all vertices were processed
        result.hasCycle = result.order.size() < n;

        return result;
    }
}