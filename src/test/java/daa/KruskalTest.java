package daa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class KruskalTest {

    @Test
    public void testConnectedGraph() {

        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(0, 3, 5);

        Kruskal.Result result = Kruskal.mst(graph);

        assertEquals(6, result.totalWeight);
        assertEquals(3, result.edgeCount);
        assertEquals(1, result.components);
    }

    @Test
    public void testDisconnectedGraph() {

        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(3, 4, 4);

        Kruskal.Result result = Kruskal.mst(graph);

        assertEquals(9, result.totalWeight);
        assertEquals(3, result.edgeCount);
        assertEquals(2, result.components);
    }

    @Test
    public void testSingleVertex() {

        Graph graph = new Graph(1, false);

        Kruskal.Result result = Kruskal.mst(graph);

        assertEquals(0, result.totalWeight);
        assertEquals(0, result.edgeCount);
        assertEquals(1, result.components);
    }

    @Test
    public void testDirectedGraph() {

        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 5);

        assertThrows(IllegalArgumentException.class,
                () -> Kruskal.mst(graph));
    }

    @Test
    public void testRandomConnectedGraphs() {

        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {

            int n = 2 + random.nextInt(49);

            Graph graph = new Graph(n, false);

            for (int i = 1; i < n; i++) {
                int parent = random.nextInt(i);
                int weight = 1 + random.nextInt(100);

                graph.addEdge(parent, i, weight);
            }

            for (int i = 0; i < n; i++) {

                int u = random.nextInt(n);
                int v = random.nextInt(n);
                int weight = 1 + random.nextInt(100);

                graph.addEdge(u, v, weight);
            }

            Kruskal.Result result = Kruskal.mst(graph);

            assertEquals(n - 1, result.edgeCount);
            assertEquals(1, result.components);
        }
    }
}