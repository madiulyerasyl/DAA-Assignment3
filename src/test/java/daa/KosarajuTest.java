package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayDeque;
import java.util.Random;

public class KosarajuTest {

    @Test
    public void testGraphWithCycles() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);

        Kosaraju.Result result = Kosaraju.scc(graph);

        assertEquals(2, result.components);
        assertEquals(12, result.edgeScans);

        assertEquals(result.componentId[0], result.componentId[1]);
        assertEquals(result.componentId[1], result.componentId[2]);

        assertEquals(result.componentId[3], result.componentId[4]);
        assertNotEquals(result.componentId[0], result.componentId[3]);
    }

    @Test
    public void testGraphWithoutCycles() {

        Graph graph = new Graph(4, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);

        Kosaraju.Result result = Kosaraju.scc(graph);

        assertEquals(4, result.components);
        assertEquals(6, result.edgeScans);
    }

    @Test
    public void testDisconnectedGraph() {

        Graph graph = new Graph(5, true);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);

        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 2, 1);

        Kosaraju.Result result = Kosaraju.scc(graph);

        assertEquals(3, result.components);
        assertEquals(8, result.edgeScans);
    }

    @Test
    public void testLargeGraph() {

        int n = 100000;

        Graph graph = new Graph(n, true);

        for (int i = 0; i < n - 1; i++) {
            graph.addEdge(i, i + 1, 1);
        }

        Kosaraju.Result result = Kosaraju.scc(graph);

        assertEquals(n, result.components);
        assertEquals(2L * (n - 1), result.edgeScans);
    }

    @Test
    public void testDirectedOnly() {

        Graph graph = new Graph(3, false);

        assertThrows(IllegalArgumentException.class,
                () -> Kosaraju.scc(graph));
    }

    private boolean canReach(Graph graph, int start, int target) {

        boolean[] visited = new boolean[graph.size()];
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {

            int vertex = queue.poll();

            if (vertex == target) {
                return true;
            }

            for (Graph.Edge edge : graph.neighbors(vertex)) {

                if (!visited[edge.to]) {
                    visited[edge.to] = true;
                    queue.add(edge.to);
                }
            }
        }

        return false;
    }

    @Test
    public void testRandomSCCWithBFS() {

        Random random = new Random(42);

        for (int test = 0; test < 20; test++) {

            int n = 2 + random.nextInt(19);
            Graph graph = new Graph(n, true);

            int m = random.nextInt(n * 3 + 1);

            for (int i = 0; i < m; i++) {
                int u = random.nextInt(n);
                int v = random.nextInt(n);

                graph.addEdge(u, v, 1);
            }

            Kosaraju.Result result = Kosaraju.scc(graph);

            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {

                    boolean sameSCC =
                            canReach(graph, u, v)
                                    && canReach(graph, v, u);

                    boolean kosarajuSame =
                            result.componentId[u] == result.componentId[v];

                    assertEquals(sameSCC, kosarajuSame);
                }
            }

            assertEquals(2L * m, result.edgeScans);
        }
    }
}