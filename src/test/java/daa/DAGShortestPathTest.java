package daa;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DAGShortestPathTest {

    @Test
    public void testNegativeWeights() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 3, -4);
        graph.addEdge(2, 3, 1);

        DAGShortestPath.Result result =
                DAGShortestPath.shortestPaths(graph);

        assertArrayEquals(
                new long[]{0, 2, 5, -2},
                result.dist
        );

        assertEquals(4, result.edgeScans);
        assertEquals(5, result.sumDistances);

        assertEquals(
                List.of(0, 1, 3),
                DAGShortestPath.getPath(result, 3)
        );
    }

    @Test
    public void testMultipleSources() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 3, 3);
        graph.addEdge(1, 4, 7);

        DAGShortestPath.Result result =
                DAGShortestPath.shortestPaths(graph);

        // Both vertices 0 and 1 have indegree 0
        assertArrayEquals(
                new long[]{0, 0, 2, 5, 7},
                result.dist
        );

        assertEquals(14, result.sumDistances);
        assertEquals(4, result.edgeScans);
    }

    @Test
    public void testDisconnectedDAG() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 3);
        graph.addEdge(2, 3, 5);

        DAGShortestPath.Result result =
                DAGShortestPath.shortestPaths(graph);

        assertArrayEquals(
                new long[]{0, 3, 0, 5},
                result.dist
        );

        assertEquals(8, result.sumDistances);
    }

    @Test
    public void testCycleDetection() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 0, 4);

        assertThrows(
                IllegalArgumentException.class,
                () -> DAGShortestPath.shortestPaths(graph)
        );
    }

    @Test
    public void testSingleVertex() {

        Graph graph = new Graph(1, true);

        DAGShortestPath.Result result =
                DAGShortestPath.shortestPaths(graph);

        assertEquals(0, result.dist[0]);
        assertEquals(0, result.sumDistances);
        assertEquals(0, result.edgeScans);
    }

    @Test
    public void testBellmanFordComparison() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 8);
        graph.addEdge(1, 2, -5);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 2);
        graph.addEdge(3, 4, -1);

        DAGShortestPath.Result result =
                DAGShortestPath.shortestPaths(graph);

        // Reference algorithm: Bellman-Ford
        long[] expected = new long[graph.size()];
        Arrays.fill(expected, DAGShortestPath.INF);

        int[] indegree = new int[graph.size()];

        for (Graph.WeightedEdge edge : graph.getEdges()) {
            indegree[edge.to]++;
        }

        for (int i = 0; i < graph.size(); i++) {
            if (indegree[i] == 0) {
                expected[i] = 0;
            }
        }

        for (int i = 0; i < graph.size() - 1; i++) {

            for (Graph.WeightedEdge edge : graph.getEdges()) {

                if (expected[edge.from] != DAGShortestPath.INF) {

                    long newDistance =
                            expected[edge.from] + edge.weight;

                    if (newDistance < expected[edge.to]) {
                        expected[edge.to] = newDistance;
                    }
                }
            }
        }

        assertArrayEquals(expected, result.dist);
        assertEquals(6, result.edgeScans);
    }

    @Test
    public void testRandomDAGWithBellmanFord() {

        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {

            int n = 2 + random.nextInt(49);
            Graph graph = new Graph(n, true);

            // Create random DAG edges
            for (int i = 0; i < n * 3; i++) {

                int u = random.nextInt(n);
                int v = random.nextInt(n);

                if (u == v) {
                    continue;
                }

                // Edges always go from smaller to larger vertex
                if (u > v) {
                    int temp = u;
                    u = v;
                    v = temp;
                }

                int weight = random.nextInt(21) - 10;

                graph.addEdge(u, v, weight);
            }

            DAGShortestPath.Result result =
                    DAGShortestPath.shortestPaths(graph);

            // Bellman-Ford reference
            long[] expected = new long[n];
            Arrays.fill(expected, DAGShortestPath.INF);

            int[] indegree = new int[n];

            for (Graph.WeightedEdge edge : graph.getEdges()) {
                indegree[edge.to]++;
            }

            // All vertices with indegree 0 are sources
            for (int i = 0; i < n; i++) {
                if (indegree[i] == 0) {
                    expected[i] = 0;
                }
            }

            for (int i = 0; i < n - 1; i++) {

                for (Graph.WeightedEdge edge : graph.getEdges()) {

                    if (expected[edge.from] != DAGShortestPath.INF) {

                        long newDistance =
                                expected[edge.from] + edge.weight;

                        if (newDistance < expected[edge.to]) {
                            expected[edge.to] = newDistance;
                        }
                    }
                }
            }

            assertArrayEquals(expected, result.dist);

            assertEquals(
                    graph.getEdges().size(),
                    result.edgeScans
            );
        }
    }
}