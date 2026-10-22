package daa;

import java.util.PriorityQueue;

public class Prim {

    public static class Result {
        public long totalWeight;
        public int edgeCount;
        public int components;
        public long edgeScans;
        public long comparisons;
    }

    private static class QueueEdge {
        int to;
        int weight;

        QueueEdge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static Result mst(Graph graph) {

        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Prim requires an undirected graph");
        }

        int n = graph.size();
        boolean[] visited = new boolean[n];

        Result result = new Result();

        PriorityQueue<QueueEdge> pq =
                new PriorityQueue<>((a, b) -> {
                    result.comparisons++;
                    return Integer.compare(a.weight, b.weight);
                });

        // Restart Prim for every disconnected component
        for (int start = 0; start < n; start++) {

            if (visited[start]) {
                continue;
            }

            result.components++;

            pq.add(new QueueEdge(start, 0));

            while (!pq.isEmpty()) {

                QueueEdge current = pq.poll();

                if (visited[current.to]) {
                    continue;
                }

                visited[current.to] = true;

                if (current.to != start) {
                    result.totalWeight += current.weight;
                    result.edgeCount++;
                }

                for (Graph.Edge edge : graph.neighbors(current.to)) {

                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        pq.add(new QueueEdge(edge.to, edge.weight));
                    }
                }
            }
        }

        return result;
    }
}