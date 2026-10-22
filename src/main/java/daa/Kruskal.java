package daa;

import java.util.ArrayList;

public class Kruskal {

    public static class Result {
        public long totalWeight;
        public int edgeCount;
        public int components;
        public long dsuOps;
        public long edgeScans;
        public long comparisons;
    }

    public static Result mst(Graph graph) {

        if (graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kruskal requires an undirected graph");
        }

        ArrayList<Graph.WeightedEdge> edges =
                new ArrayList<>(graph.getEdges());

        Result result = new Result();

        edges.sort((a, b) -> {
            result.comparisons++;
            return Integer.compare(a.weight, b.weight);
        });

        DSU dsu = new DSU(graph.size());

        for (Graph.WeightedEdge edge : edges) {

            result.edgeScans++;

            if (dsu.union(edge.from, edge.to)) {
                result.totalWeight += edge.weight;
                result.edgeCount++;
            }
        }

        result.components = dsu.components();
        result.dsuOps = dsu.dsuOps();

        return result;
    }
}