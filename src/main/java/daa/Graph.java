package daa;

import java.util.ArrayList;

public class Graph {

    private int n;
    private boolean directed;
    private ArrayList<ArrayList<Edge>> adj;

    public static class Edge {
        public int to;
        public int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;

        adj = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new Edge(v, weight));

        if (!directed) {
            adj.get(v).add(new Edge(u, weight));
        }
    }

    public ArrayList<Edge> neighbors(int vertex) {
        return adj.get(vertex);
    }

    public int size() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }
}