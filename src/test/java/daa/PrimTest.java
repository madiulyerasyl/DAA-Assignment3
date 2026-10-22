package daa;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PrimTest {

    @Test
    public void testConnectedGraph() {

        Graph graph = new Graph(4, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(0, 3, 5);

        Prim.Result prim = Prim.mst(graph);
        Kruskal.Result kruskal = Kruskal.mst(graph);

        assertEquals(6, prim.totalWeight);
        assertEquals(kruskal.totalWeight, prim.totalWeight);
        assertEquals(3, prim.edgeCount);
        assertEquals(1, prim.components);
    }

    @Test
    public void testDisconnectedGraph() {

        Graph graph = new Graph(5, false);

        graph.addEdge(0, 1, 2);
        graph.addEdge(1, 2, 3);
        graph.addEdge(3, 4, 4);

        Prim.Result prim = Prim.mst(graph);
        Kruskal.Result kruskal = Kruskal.mst(graph);

        assertEquals(9, prim.totalWeight);
        assertEquals(kruskal.totalWeight, prim.totalWeight);
        assertEquals(3, prim.edgeCount);
        assertEquals(2, prim.components);
    }

    @Test
    public void testSingleVertex() {

        Graph graph = new Graph(1, false);

        Prim.Result result = Prim.mst(graph);

        assertEquals(0, result.totalWeight);
        assertEquals(0, result.edgeCount);
        assertEquals(1, result.components);
    }

    @Test
    public void testDirectedGraph() {

        Graph graph = new Graph(3, true);

        assertThrows(IllegalArgumentException.class,
                () -> Prim.mst(graph));
    }

    @Test
    public void testRandomGraphs() {

        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {

            int n = 2 + random.nextInt(49);

            Graph graph = new Graph(n, false);

            // Create a connected graph
            for (int i = 1; i < n; i++) {

                int parent = random.nextInt(i);
                int weight = 1 + random.nextInt(100);

                graph.addEdge(parent, i, weight);
            }

            // Add random edges
            for (int i = 0; i < n; i++) {

                int u = random.nextInt(n);
                int v = random.nextInt(n);
                int weight = 1 + random.nextInt(100);

                graph.addEdge(u, v, weight);
            }

            Prim.Result prim = Prim.mst(graph);
            Kruskal.Result kruskal = Kruskal.mst(graph);

            assertEquals(kruskal.totalWeight, prim.totalWeight);
            assertEquals(n - 1, prim.edgeCount);
            assertEquals(n - 1, kruskal.edgeCount);
            assertEquals(1, prim.components);
        }
    }
}