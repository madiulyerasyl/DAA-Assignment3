package daa;

import java.util.ArrayList;
import java.util.ArrayDeque;

public class Kosaraju {

    public static class Result {
        public int components;
        public int[] componentId;
        public long edgeScans;
        public int maxDepth;
    }

    private static class Frame {
        int vertex;
        int nextEdge;

        Frame(int vertex) {
            this.vertex = vertex;
            this.nextEdge = 0;
        }
    }

    public static Result scc(Graph graph) {

        if (!graph.isDirected()) {
            throw new IllegalArgumentException(
                    "Kosaraju requires a directed graph");
        }

        int n = graph.size();

        Result result = new Result();
        result.componentId = new int[n];

        boolean[] visited = new boolean[n];
        ArrayList<Integer> order = new ArrayList<>();

        // First DFS: calculate finishing order
        for (int start = 0; start < n; start++) {

            if (visited[start]) {
                continue;
            }

            ArrayDeque<Frame> stack = new ArrayDeque<>();

            visited[start] = true;
            stack.push(new Frame(start));

            while (!stack.isEmpty()) {

                result.maxDepth = Math.max(
                        result.maxDepth, stack.size());

                Frame current = stack.peek();

                ArrayList<Graph.Edge> neighbors =
                        graph.neighbors(current.vertex);

                if (current.nextEdge < neighbors.size()) {

                    Graph.Edge edge =
                            neighbors.get(current.nextEdge);

                    current.nextEdge++;
                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(new Frame(edge.to));
                    }

                } else {
                    order.add(current.vertex);
                    stack.pop();
                }
            }
        }

        // Build reversed graph
        Graph reversed = new Graph(n, true);

        for (Graph.WeightedEdge edge : graph.getEdges()) {
            reversed.addEdge(edge.to, edge.from, edge.weight);
        }

        // Second DFS: find components
        visited = new boolean[n];

        for (int i = order.size() - 1; i >= 0; i--) {

            int start = order.get(i);

            if (visited[start]) {
                continue;
            }

            int component = result.components;
            result.components++;

            ArrayDeque<Integer> stack = new ArrayDeque<>();

            visited[start] = true;
            stack.push(start);

            while (!stack.isEmpty()) {

                result.maxDepth = Math.max(
                        result.maxDepth, stack.size());

                int vertex = stack.pop();
                result.componentId[vertex] = component;

                for (Graph.Edge edge : reversed.neighbors(vertex)) {

                    result.edgeScans++;

                    if (!visited[edge.to]) {
                        visited[edge.to] = true;
                        stack.push(edge.to);
                    }
                }
            }
        }

        return result;
    }
}