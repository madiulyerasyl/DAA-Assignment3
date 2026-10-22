package daa;

import java.util.ArrayList;

public class Graph {

    private int n;
    private boolean directed;
    private ArrayList<ArrayList<Edge>> adj;
    private ArrayList<WeightedEdge> edges;

    public static class Edge {
        public int to;
        public int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static class WeightedEdge {
        public int from;
        public int to;
        public int weight;

        public WeightedEdge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;

        adj = new ArrayList<>();
        edges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw new IllegalArgumentException("Invalid vertex");
        }

        adj.get(u).add(new Edge(v, weight));

        if (!directed) {
            adj.get(v).add(new Edge(u, weight));
        }

        edges.add(new WeightedEdge(u, v, weight));
    }

    public ArrayList<Edge> neighbors(int vertex) {
        return adj.get(vertex);
    }

    public ArrayList<WeightedEdge> getEdges() {
        return edges;
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }
}